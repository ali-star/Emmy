package alistar.app;

import android.Manifest;
import android.app.*;
import android.content.*;
import android.content.pm.PackageManager;
import android.os.*;
import android.widget.*;
import android.location.*;

import com.readystatesoftware.notificationlog.Log;

import androidx.core.app.*;

import android.media.*;

import alistar.app.alarm.*;

import java.util.*;

import alistar.app.map.*;
import alistar.app.brain.*;

import com.google.android.gms.maps.model.*;

import alistar.app.screen_lock.*;
import alistar.app.ear.*;

import java.io.*;


public class MyService extends Service {
	public static final String DETECT_LOCATION = "DETECT_LOCATION";
	public static final String CANCEL_DETECT_LOCATION = "CANCEL_DETECT_LOCATION";
	public static final String CHECK_SIM = "CHECK_SIM";
	private final IBinder serviceBinder = new ServiceBinder();
	private LocationManager locationManager;
	private LocationListener locationListener;
	private boolean detectingLocation = false;
	private boolean canSendLocationSms = false;
	private Handler locationSmsHandler;
	private NotificationManager notificationManager;
	private NotificationCompat.Builder notificationBuilder;
	private int notificationId = 1;
	private AudioManager audioManager;
	private BroadcastReceiver mScreenStateReceiver;
	private static String TAG = "Service";
	private boolean headphonesFirstTimeChrcked = true;
	private boolean control_sound = false;
	private long searchStartTime;
	private boolean locationSaved = false;
	private boolean searchingForLocation = false;
	private WorkQueue workQueue = null;
	private boolean hideSaraAfterWlDone = false;
	private Utils utils;
	private OnLocationFoundListener locationFoundListener;
	private RecMicToMp3 mRecMicToMp3;
	private BroadcastReceiver musicPlayReceiver;

	public class ServiceBinder extends Binder {
		public MyService getService() {
			return MyService.this;
		}
	}

	protected ServiceConnection mServerConn = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder binder) {
			workQueue = ((WorkQueue.ServiceBinder) binder).getService();
			Log.d("tag", "onServiceConnected");
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.d("tag", "onServiceDisconnected");
		}
	};

	public void start() {
		// mContext is defined upper in code, I think it is not necessary to explain what is it
		Intent intent = new Intent(getApplicationContext(), WorkQueue.class);
		bindService(intent, mServerConn, Context.BIND_AUTO_CREATE);
		startService(intent);
	}

	public void stop() {
		//mContext.stopService(new Intent(mContext, ServiceRemote.class));
		unbindService(mServerConn);
	}

	@Override
	public IBinder onBind(Intent p1) {
		// TODO: Implement this method
		return serviceBinder;
	}

	@Override
	public void onCreate() {
		// TODO: Implement this method
		super.onCreate();
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationBuilder = new NotificationCompat.Builder(this);
		notificationBuilder.setSmallIcon(R.drawable.app_icon);
		notificationBuilder.setContentTitle("Sara");
		notificationBuilder.setContentText("Hi Ali ^.^");
		notificationBuilder.setOnlyAlertOnce(true);
		notificationBuilder.setOngoing(true);
		//notificationBuilder.setPriority(Notification.PRIORITY_MIN);
		notificationManager.notify(notificationId, notificationBuilder.getNotification());
		registerReceivers();
		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		SharedPreferencesUtil.setBoolean(Lockscreen.ISLOCK, true);
		Lockscreen.getInstance(this).startLockscreenService();
		start();
		utils = Utils.getInstance(this);
		IntentFilter musicChangeFilter = new IntentFilter();
		musicChangeFilter.addAction("com.android.music.metachanged");
		musicChangeFilter.addAction("com.android.music.playstatechanged");
		musicChangeFilter.addAction("com.android.music.playbackcomplete");
		musicChangeFilter.addAction("com.android.music.queuechanged");

		registerReceiver(musicPlayReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context p1, Intent p2) {
				// TODO: Implement this method
				Log.d("service", "music state changed");
				if (audioManager.isMusicActive()) {
					Log.d("music", "music active");
				} else {
					Log.d("music", "music stoped");
				}
			}


		}, musicChangeFilter);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO: Implement this method
		Utils.getInstance(getApplicationContext()).setAlarmForSearchNearbyPlace();
		try {
			Bundle bundle = null;
			if (intent != null)
				bundle = intent.getExtras();
			{
				if (bundle == null)
					return super.onStartCommand(intent, flags, startId);
				String command = bundle.getString("command");
				if (command != null) {
					switch (command) {
						case DETECT_LOCATION:
							if (!detectingLocation) {
								detectLocation();
							}
							break;
						case CANCEL_DETECT_LOCATION:
							stopDetectLocation();
							break;
						case CHECK_SIM:
							checkSim();
							break;
						case Utils.WAKEFUL_SEARCH_NEARBY_PLACE:
							findCurrentLocation();
							//saraSay("finding you...");
							break;
						default:
							break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO: Implement this method
		super.onDestroy();
		unregisterReceiver(mScreenStateReceiver);
		unregisterReceiver(musicPlayReceiver);
		stopDetectLocation();
		Toast.makeText(getApplicationContext(), "service stoped", Toast.LENGTH_LONG).show();
		notificationManager.cancel(notificationId);
		notificationBuilder = null;
		notificationManager = null;
		stop();
	}

	public void detectLocation() {
		Utils.sendSmsMessage(Utils.getSavedReceivedSmsPhoneNumber(getApplicationContext()), "sara: detecting...");
		detectingLocation = true;
		locationSmsHandler = new Handler();
		locationSmsHandler.postDelayed(locationSmsRunnable, 60000);
		updateNotification(null, "finding location...");
		Toast.makeText(getApplicationContext(), "finding location...", Toast.LENGTH_LONG).show();
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				if (canSendLocationSms) {
					double latitude = location.getLatitude();
					double longitude = location.getLongitude();
					double speed = location.getSpeed(); //spedd in meter/minute
					speed = (speed * 3600) / 1000;      // speed in km/minute
					String data = "latitude: " + String.valueOf(latitude) + "\n" +
							"longitude: " + String.valueOf(longitude) + "\n" +
							"Current speed:" + location.getSpeed();
					String mapLink = "http://maps.google.com/?ie=UTF8&q=" + String.valueOf(latitude) + "," + String.valueOf(longitude) + "&ll=" + String.valueOf(latitude) + "," + String.valueOf(longitude) + "&z=17";
					Log.d("data", data + "\n" + mapLink);
					Utils.sendSmsMessage(Utils.getSavedReceivedSmsPhoneNumber(getApplicationContext()), mapLink);
					canSendLocationSms = false;
					locationSmsHandler.postDelayed(locationSmsRunnable, 60000);
				}
			}
		};
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
			}
		}

	}

	public void detectLocation(final String phoneNumber) {
		detectingLocation = true;
		locationSmsHandler = new Handler();
		locationSmsHandler.postDelayed(locationSmsRunnable, 60000);
		updateNotification(null, "finding location...");
		Toast.makeText(getApplicationContext(), "finding location...", Toast.LENGTH_LONG).show();
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				double latitude = location.getLatitude();
				double longitude = location.getLongitude();
				double speed = location.getSpeed(); //spedd in meter/minute
				speed = (speed * 3600) / 1000;      // speed in km/minute
				String data = "latitude: " + String.valueOf(latitude) + "\n" +
						"longitude: " + String.valueOf(longitude) + "\n" +
						"Current speed:" + location.getSpeed();
				String mapLink = "http://maps.google.com/?ie=UTF8&q=" + String.valueOf(latitude) + "," + String.valueOf(longitude) + "&ll=" + String.valueOf(latitude) + "," + String.valueOf(longitude) + "&z=17";
				Log.d("data", data + "\n" + mapLink);

				if (location.getAccuracy() <= 30) {
					Utils.sendSmsMessage(phoneNumber, mapLink);
					canSendLocationSms = false;
					locationSmsHandler.postDelayed(locationSmsRunnable, 15000);
				}
				if (canSendLocationSms & location.getAccuracy() > 30) {
					Utils.sendSmsMessage(phoneNumber, mapLink);
					canSendLocationSms = false;
					locationSmsHandler.postDelayed(locationSmsRunnable, 30000);
				}
			}
		};
		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);

	}

	;

	private Runnable locationSmsRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO: Implement this method
			canSendLocationSms = true;
		}


	};

	private void stopDetectLocation() {
		detectingLocation = false;
		if (locationManager != null) {
			Log.d("[MyService]", "detect location stoped");
			locationManager.removeUpdates(locationListener);
			locationListener = null;
			locationManager = null;
		}
		if (locationSmsHandler != null) {
			locationSmsHandler.removeCallbacks(locationSmsRunnable);
		}
	}

	private void updateNotification(String title, String text) {
		if (title != null & text == null) {
			notificationBuilder.setContentTitle("Sara: " + title);
		} else if (text != null & title == null) {
			notificationBuilder.setContentText(text);
		} else if (text == null & title == null) {
			notificationBuilder.setContentTitle("Sara");
			notificationBuilder.setContentText("YO!");
		} else {
			notificationBuilder.setContentTitle("Sara: " + title);
			notificationBuilder.setContentText(text);
		}
		notificationManager.notify(notificationId, notificationBuilder.getNotification());
	}

	public void checkSim() {
		String simSerial = Utils.getSimSerial(getApplicationContext());

		if (Utils.getInstance(getApplicationContext()).checkSimcard(simSerial)
				& Utils.getInstance(getApplicationContext()).isSimcardsInMemory()) {
			Log.d("tag", "Unfamiliar simcard");
			if (AppConfig.DEBUG)
				return;
			Utils.sendSmsMessage("+989907473592", "سیمکارت ناشناس در گوشی علی قرار گرفت");
			Utils.sendSmsMessage("+989361409072", "سیمکارت ناشناس در گوشی علی قرار گرفت");
			Utils.sendSmsMessage("+989380911934", "سیمکارت ناشناس در گوشی علی قرار گرفت");
			detectLocation("+989907473592");
			return;
		}
		Log.d("tag", "simcard OK");
	}

	private void registerReceivers() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction("android.location.PROVIDERS_CHANGED");
		filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		filter.addAction(Intent.ACTION_HEADSET_PLUG);
		mScreenStateReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				//TODO delete this after done work
				boolean off = false;
				if (off)
					return;
				if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
					Log.i("Check", "Screen OFF");
					checkAlarm();
					initVolumeForScreenOff();
					checkMainSwitchs();
					initLockScreen();
				}
				if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
					Log.i("Check", "Screen ON");
					if (utils.canAskFeeling()) {
						workQueue.addWork(System.currentTimeMillis(), Work.WorkName.ASK_FEELING);
					}
				}
				if (intent.getAction().equals("android.location.PROVIDERS_CHANGED") | intent.getAction().equals(Intent.ACTION_AIRPLANE_MODE_CHANGED)) {
					Log.i("Check", "PROVIDERS_CHANGED");
					checkMainSwitchs();
				}

				if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG) & !headphonesFirstTimeChrcked) {
					int state = intent.getIntExtra("state", -1);
					switch (state) {
						case 0:
							Log.d(TAG, "Headset is unplugged");
							initVolumeForHeadsetUnpluged();

							break;
						case 1:
							Log.d(TAG, "Headset is plugged");
							initVolumeForHeadsetPluged();
							break;
						default:
							Log.d(TAG, "I have no idea what the headset state is");
					}
					headphonesFirstTimeChrcked = false;
				}
			}
		};
		registerReceiver(mScreenStateReceiver, filter);
	}

	private void initVolumeForScreenOff() {
		control_sound = utils.isSoundControlOn();
		if (!audioManager.isWiredHeadsetOn() & control_sound) {
			audioManager.setRingerMode(utils.getSoundControlType() == Utils.SOUND_CONTROL_TYPE_VIBRATE ? AudioManager.RINGER_MODE_VIBRATE : AudioManager.RINGER_MODE_SILENT);
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
			audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0, 0);
			audioManager.setStreamVolume(AudioManager.STREAM_ALARM, 0, 0);
			audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, 0, 0);
		}
	}

	private void initVolumeForHeadsetUnpluged() {
		if (control_sound) {
			audioManager.setRingerMode(utils.getSoundControlType() == Utils.SOUND_CONTROL_TYPE_VIBRATE ? AudioManager.RINGER_MODE_VIBRATE : AudioManager.RINGER_MODE_SILENT);
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
			audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0, 0);
			audioManager.setStreamVolume(AudioManager.STREAM_ALARM, 0, 0);
			audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, 0, 0);
		}
	}

	private void initVolumeForHeadsetPluged() {
		if (control_sound) {
			audioManager.setRingerMode(utils.getSoundControlType() == Utils.SOUND_CONTROL_TYPE_VIBRATE ? AudioManager.RINGER_MODE_VIBRATE : AudioManager.RINGER_MODE_SILENT);
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 3, 0);
			audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0, 0);
			audioManager.setStreamVolume(AudioManager.STREAM_ALARM, 0, 0);
			audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, 0, 0);
		}
	}

	public void checkMainSwitchs() {
		if (Utils.isAirplaneModeOn(getApplicationContext())) {
			updateNotification("warning!", "Airplane mode is On.");
			Utils.warningVibrate(getApplicationContext());
		}

		if (!Utils.isGpsOn(getApplicationContext())) {
			updateNotification("warning!", "GPS is Off.");
			Utils.warningVibrate(getApplicationContext());
		}

		if (!Utils.isAirplaneModeOn(getApplicationContext()) & Utils.isGpsOn(getApplicationContext())) {
			updateNotification(null, null);
		}
	}

	public void checkAlarm() {
		Alarm alarm = new Alarm(getApplicationContext());
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		PendingIntent bcpi = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(getApplicationContext(), WakefulBroadcast.class).putExtra("data", Utils.WAKEFUL_ALARM), 0);
		AlarmManager.AlarmClockInfo ac =
				new AlarmManager.AlarmClockInfo(alarm.getTimeMillis(), bcpi);
		if (alarm.isAlarmActive()) {
			am.setAlarmClock(ac, bcpi);
			Log.i(TAG, "Alarm Active");
		} else {
			am.cancel(bcpi);
			Log.i(TAG, "Alarm Off");
		}
	}

	public void findCurrentLocation() {
		if (searchingForLocation)
			return;
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		searchingForLocation = true;
		searchStartTime = System.currentTimeMillis();
		final Handler handler = new Handler();
		searchStartTime = System.currentTimeMillis();
		final Timer timer = new Timer();
		updateNotification(null, "Checking where you are...");
		/*locationManager.sendExtraCommand(LocationManager.GPS_PROVIDER,"delete_aiding_data", null);
		Bundle bundle = new Bundle();
		locationManager.sendExtraCommand("gps", "force_xtra_injection", bundle);
		locationManager.sendExtraCommand("gps", "force_time_injection", bundle);*/
		locationListener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				Log.d("gps", "onProviderEnabled " + provider);

			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				Log.d("gps", "onProviderDisabled " + provider);

			}

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				//location.getAccuracy();

				Memory memory = new Memory(getApplicationContext());
				String textToSay = "";
				if (location.getAccuracy() <= 50) {
					MapMarker marker = Utils.getInstance(getApplicationContext()).getNearbyPlace(memory.getPlaces(), new LatLng(location.getLatitude(), location.getLongitude()));
					if (marker != null) {
						memory.saveCurrentLocation(marker);
						textToSay = "You are in " + marker.getName();
						updateNotification(null, textToSay);
						if (locationFoundListener != null) {
							locationFoundListener.onLocationFound(marker, textToSay);
						}
					} else {
						marker = new MapMarker();
						marker.setLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
						marker.setAccuracy(location.getAccuracy());
						memory.saveCurrentLocation(marker);
						textToSay = "You are in unknown place ◐.̃◐";
						updateNotification(null, textToSay);
						if (locationFoundListener != null) {
							locationFoundListener.onLocationFound(marker, textToSay);
						}
					}
					locationSaved = true;
					//locationManager.removeUpdates(locationListener);
				} else if (location.getAccuracy() > 50 & searchStartTime + Utils.MINUTE < System.currentTimeMillis()) {
					MapMarker marker = Utils.getInstance(getApplicationContext()).getNearbyPlace(memory.getPlaces(), new LatLng(location.getLatitude(), location.getLongitude()));
					if (marker != null) {
						memory.saveCurrentLocation(marker);
						textToSay = "You are in " + marker.getName();
						updateNotification(null, textToSay);
						if (locationFoundListener != null) {
							locationFoundListener.onLocationFound(marker, textToSay);
						}
					} else {
						marker = new MapMarker();
						marker.setLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
						marker.setAccuracy(location.getAccuracy());
						memory.saveCurrentLocation(marker);
						textToSay = "You are in unknown place ◐.̃◐";
						updateNotification(null, textToSay);
						if (locationFoundListener != null) {
							locationFoundListener.onLocationFound(marker, textToSay);
						}
					}
					locationSaved = true;
					//locationManager.removeUpdates(locationListener);
				} else if (searchStartTime + (Utils.MINUTE * 2) < System.currentTimeMillis()) {
					MapMarker marker = Utils.getInstance(getApplicationContext()).getNearbyPlace(memory.getPlaces(), new LatLng(location.getLatitude(), location.getLongitude()));
					if (marker != null) {
						memory.saveCurrentLocation(marker);
						textToSay = "You are in " + marker.getName();
						updateNotification(null, textToSay);
						if (locationFoundListener != null) {
							locationFoundListener.onLocationFound(marker, textToSay);
						}
					} else {
						marker = new MapMarker();
						marker.setLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
						marker.setAccuracy(location.getAccuracy());
						memory.saveCurrentLocation(marker);
						textToSay = "You are in unknown place ◐.̃◐";
						updateNotification(null, textToSay);
						if (locationFoundListener != null) {
							locationFoundListener.onLocationFound(marker, textToSay);
						}
					}
					locationSaved = true;
					//locationManager.removeUpdates(locationListener);
				}

				if (locationSaved) {
					searchingForLocation = false;
					locationManager.removeUpdates(locationListener);
					timer.cancel();
					Log.d("location", "location Saved");
					handler.postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO: Implement this method
							updateNotification(null, "^ω^");
							locationSaved = false;
							locationFoundListener = null;
						}


					}, 3000);
					return;
				}

			}
		};
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				// TODO: Implement this method
				if (locationSaved) {
					searchingForLocation = false;
					return;
				}
				locationManager.removeUpdates(locationListener);
				Log.d("location", "location not found");
				final String textToSay = "I can't find you -_-||";
				updateNotification(null, textToSay);
				new Handler(Looper.getMainLooper()).post(new Runnable() {

					@Override
					public void run() {
						if (locationFoundListener != null) {
							locationFoundListener.onLocationFound(null, textToSay);
						}
					}

				});
				locationSaved = false;
				locationFoundListener = null;
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO: Implement this method
						updateNotification(null, "~_~");
					}


				}, 3000);
			}


		};
		timer.schedule(task, Utils.MINUTE * 3);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
				Utils.getInstance(getApplicationContext()).setAlarmForSearchNearbyPlace();
			}
		}
	}
	
	public interface OnLocationFoundListener
	{
		public void onLocationFound ( MapMarker marker, String textToSay );
	}
	
	public void setOnLocationFoundListener ( OnLocationFoundListener olfl )
	{
		locationFoundListener = olfl;
	}

	private void initLockScreen()
	{
		Log.d("service", "init lock screen from service");
		sendBroadcast(new Intent(getApplicationContext(), LockScreenReceiver.class).putExtra("data", "lock"));
	}
	
	public void startVoiceRecord ( )
	{
		if ( isRecordingVoice() )
			return;
		String fileName = "Recording_";
		File filesDir = new File ( Environment.getExternalStorageDirectory() + "/sara/recordings/" );
		if ( filesDir.exists() )
		{
			int filesCount = filesDir.listFiles().length + 1;
			fileName += filesCount;
		}
		mRecMicToMp3 = new RecMicToMp3(
		Environment.getExternalStorageDirectory() + "/sara/recordings/" + fileName + ".mp3", 44100);
		mRecMicToMp3.start();
	}
	
	public boolean isRecordingVoice ( )
	{
		if (mRecMicToMp3 != null )
		{
			return mRecMicToMp3.isRecording();
		}
		return false;
	}
	
	public void stopVoiceRecord ( )
	{
		if ( mRecMicToMp3 != null)
			mRecMicToMp3.stop();
	}
	
	/*private void saraSay(String str)
	{
		if(saraViewService.getSara() != null)
		{
			if(saraViewService.getState() == saraViewService.STATE_LIST)
				return;
			if(saraViewService.isSaraVisible)
			{
				saraViewService.getSara().say(str);
			}
			else
			{
				saraViewService.showSara(str);
			}
			
		}
	}
	
	private void saraSay(String str, long d, SaraView.OnTextCleared listener)
	{
		if(saraViewService.getSara() != null)
		{
			if(saraViewService.getState() == saraViewService.STATE_LIST)
				return;
			if(saraViewService.isSaraVisible)
			{
				saraViewService.getSara().say(str, d, listener);
			}
			else
			{
				saraViewService.showSara(str, d, listener);
			}

		}
	}*/

}
