package alistar.app;

import alistar.app.alarm.*;
import alistar.app.brain.*;
import alistar.app.map.*;
import alistar.app.utils.*;
import android.app.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.location.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import android.telephony.*;
import android.util.*;
import com.google.android.gms.maps.model.*;

import java.io.*;
import java.lang.ref.*;
import java.nio.channels.*;
import java.text.*;
import java.util.*;
import android.telephony.gsm.SmsManager;
import com.readystatesoftware.notificationlog.Log;
import java.lang.Process;
import android.media.*;
import android.view.*;
import alistar.app.screen_lock.service.*;
import android.database.Cursor;

public class Utils
{

	private Context context;
	private SharedPreferences sets;
	public static final long SECOND = 1000;
	public static final long MINUTE = SECOND * 60;
	public static final long HOUR = MINUTE * 60;
	public static final long DAY = HOUR * 24;
	public static final long MONTH = DAY * 30;
	public static final long YEAR = MONTH * 12;
	public static final String SARA_FOLDER = Environment.getExternalStorageDirectory() + "/sara/";
	public static final String CATCH_FOLDER = SARA_FOLDER + "catch/";
	public static final String CRASH_FOLDER = SARA_FOLDER + "crash/";
	public static final String MEMORY_FOLDER = SARA_FOLDER + "memory/";
	public static final String SARA_PROJECT_RES_FOLDER = "/mnt/extSdCard/" + "Workspaces/AppProjects/Aide/MyApp/app/src/main/res/";
	public static final String DRM_FOLDER = "drawable-mdpi/";
	public static final String DRH_FOLDER = "drawable-hdpi/";
	public static final String DRXH_FOLDER = "drawable-xhdpi/";
	public static final String DRXXH_FOLDER = "drawable-xxhdpi/";
	public static final String DRXXXH_FOLDER = "drawable-xxxhdpi/";
	public static final String EMOJI_FOLDER = SARA_FOLDER + "emoji/";
	public static final String WAKEFUL_ALARM = "START_ALARM";
	public static final String WAKEFUL_SEARCH_NEARBY_PLACE = "SEARCH_NEARBY_PLACE";
	private final static String COMMAND_L_ON = "svc data enable\n ";
	private final static String COMMAND_L_OFF = "svc data disable\n ";
	private final static String COMMAND_SU = "su";
	public static final int SOUND_CONTROL_TYPE_SILENT = 0;
	public static final int SOUND_CONTROL_TYPE_VIBRATE = 1;
	private ACache mCache;
	private AudioManager mAudioManager;
	public static final String CMDTOGGLEPAUSE = "togglepause";
	public static final String CMDPAUSE = "pause";
	public static final String CMDPREVIOUS = "previous";
	public static final String CMDNEXT = "next";
	public static final String SERVICECMD = "com.android.music.musicservicecommand";
	public static final String CMDNAME = "command";
	public static final String CMDSTOP = "stop";

	private Utils(Context context)
	{
        this.context = context;
		sets = context.getSharedPreferences("data", 0);
		mCache = ACache.get(context);
		mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    private static WeakReference<Utils> myWeakInstance;

	public void lockScreen ( )
	{
		context.startService(new Intent(context, LockscreenService.class).putExtra("action", "lock"));
	}

	public int dpToTx ( int dp )
	{
		// TODO: Implement this method
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics() );
	}
	
	public float dpToTx ( float dp )
	{
		// TODO: Implement this method
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics() );
	}
	
	public int spToPx ( float sp )
	{
		int px = (int) TypedValue.applyDimension ( TypedValue.COMPLEX_UNIT_SP, sp, context.getResources ( ).getDisplayMetrics ( ) );
		return px; 
	}

    public static Utils getInstance(Context context)
	{
        if (myWeakInstance == null || myWeakInstance.get() == null)
		{
			//Log.d("Utils", "utils is null");
            myWeakInstance = new WeakReference<>(new Utils(context.getApplicationContext()));
        }
		else
		{
			//Log.d("Utils", "utils is not null");
		}
        return myWeakInstance.get();
    }

	public static void sendSmsMessage(String phoneNumber, String message)
	{
		SmsManager.getDefault().sendTextMessage(phoneNumber, null, message, null, null);
	}

	public static String getLocationKeyWord(Context ctx)
	{
		SharedPreferences sets = ctx.getSharedPreferences("KEYWORDS", 0);
		return sets.getString("LOCATION_KEYWORD", "sara, detect");
	}

	public static String getCancelLocationKeyWord(Context ctx)
	{
		SharedPreferences sets = ctx.getSharedPreferences("KEYWORDS", 0);
		return sets.getString("CANCEL_LOCATION_KEYWORD", "sara, cancel detect");
	}

	public static void saveReceivedSmsPhoneNumber(Context ctx, String phoneNumber)
	{
		SharedPreferences sets = ctx.getSharedPreferences("DATA", 0);
		SharedPreferences.Editor editor = sets.edit();
		editor.putString("RECEIVED_SMS_PHONE_NUMVER", phoneNumber);
		editor.commit();
	}

	public static String getSavedReceivedSmsPhoneNumber(Context ctx)
	{
		SharedPreferences sets = ctx.getSharedPreferences("DATA", 0);
		return sets.getString("RECEIVED_SMS_PHONE_NUMVER", "+989907473592");
	}

	public static void saveSim1Serial(Context ctx, String serial)
	{
		SharedPreferences sets = ctx.getSharedPreferences("DATA", 0);
		SharedPreferences.Editor editor = sets.edit();
		editor.putString("SIM_1_SERIAL", serial);
		editor.commit();
	}

	public static void saveSim2Serial(Context ctx, String serial)
	{
		SharedPreferences sets = ctx.getSharedPreferences("DATA", 0);
		SharedPreferences.Editor editor = sets.edit();
		editor.putString("SIM_2_SERIAL", serial);
		editor.commit();
	}

	public static String getSim1Serial(Context ctx)
	{
		SharedPreferences sets = ctx.getSharedPreferences("DATA", 0);
		return sets.getString("SIM_1_SERIAL", null);
	}

	public static String getSim2Serial(Context ctx)
	{
		SharedPreferences sets = ctx.getSharedPreferences("DATA", 0);
		return sets.getString("SIM_2_SERIAL", null);
	}

	public static String getSimSerial(Context context)
	{
		return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSimSerialNumber();
	}

	public static long getSolderingFinishDate()
	{
		Calendar c = Calendar.getInstance();
		c.set(2018, Calendar.JANUARY, 21, 0, 0, 0);
		Log.d("tag", String.valueOf((c.getTimeInMillis() - System.currentTimeMillis())));
		return c.getTimeInMillis() - System.currentTimeMillis();
	}

	public static String getSolderingFinishDateString()
	{
		// Create a DateFormatter object for displaying date in specified format.
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy - MM - dd");

		// Create a calendar object that will convert the date and time value in milliseconds to date. 
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(getSolderingFinishDate());
		int[] data = convertLongToTimeText(getSolderingFinishDate());
		return String.valueOf(data[0] + "/" + data[1] + "/" + data[2]);
	}

	public static int[] convertLongToTimeText(long l)
	{
		int years = (int) (l / YEAR);
		int months = (int) ((l - (years * YEAR)) / MONTH);
		int days = (int) ((l - ((years * YEAR) + (months * MONTH))) / DAY);

		return new int[] {years,months,days};
		//String daysString = String.valueOf();
		//return String.valueOf(years) + " / " + String.valueOf(months) + " / " + String.valueOf(days);
	}

	public String convertLongToTimeString(long l)
	{
		SimpleDateFormat sdf = new SimpleDateFormat();
		return sdf.format(new Date(l));
	}

	public static long toLong(int year, int month, int day, int hour, int minute, int second)
	{
		return (year * YEAR) + (month * MONTH) + (day * DAY) + (hour * HOUR) + (minute + MINUTE) + (second * SECOND);
	}

	public static boolean isAirplaneModeOn(Context context)
	{
		return Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) != 0;
	}

	public static boolean isGpsOn(Context context)
	{
		return ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	public static void warningVibrate(Context context)
	{
		long[] patternt = {0, 100, 150, 100, 100, 250};
		((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(patternt, -1);
	}

	public void Vibrate(long l)
	{
		((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(l);
	}

	public void vaibrate(long[] p, int r)
	{
		((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(p , r);
	}

	public void saveAliFeeling(int feeling, String note, String emoji)
	{
		Memory memory = new Memory(context);
		memory.saveAliFeeling(feeling, note, System.currentTimeMillis(), emoji);
	}

	public List<Emotion> getEmotions(int count)
	{
		return new Memory(context).getEmotions(count);
	}

	public List<Emotion> getEmotionsByTimeRange(long start, long end)
	{
		return new Memory(context).getEmotionsByTimeRange(start, end);
	}

	public void initFolders()
	{
		File saraFolder = new File(Environment.getExternalStorageDirectory() + File.separator + "sara");
		if (!saraFolder.exists())
		{
			saraFolder.mkdirs();
		}

		File catchFolder = new File(Environment.getExternalStorageDirectory() + File.separator + "sara/catch");
		if (!catchFolder.exists())
		{
			catchFolder.mkdirs();
		}
	}

	public static void copyFileOrDirectory(String srcDir, String dstDir)
	{

		try
		{
			File src = new File(srcDir);
			File dst = new File(dstDir, src.getName());

			if (src.isDirectory())
			{

				String files[] = src.list();
				int filesLength = files.length;
				for (int i = 0; i < filesLength; i++)
				{
					String src1 = (new File(src, files[i]).getPath());
					String dst1 = dst.getPath();
					copyFileOrDirectory(src1, dst1);

				}
			}
			else
			{
				copyFile(src, dst);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void copyFile(File sourceFile, File destFile) throws IOException
	{
		if (!destFile.getParentFile().exists())
			destFile.getParentFile().mkdirs();

		if (!destFile.exists())
		{
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;

		try
		{
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		}
		finally
		{
			if (source != null)
			{
				source.close();
			}
			if (destination != null)
			{
				destination.close();
			}
		}
	}

	public void copyIconsFromZip(String zip)
	{
		Log.d("tag", zip);
		DecompressFast df = new DecompressFast(zip, Utils.CATCH_FOLDER);
		df.unzip();
		String exFolderName = exFolderName = Utils.CATCH_FOLDER;
		exFolderName += df.getFolderName();
		Log.d("tag", exFolderName);
		String pngName = convertFormatName(zip, ".png");
		Log.d("png", pngName);
		Log.d("source", exFolderName + "android/" + DRM_FOLDER + pngName);
		Log.d("dis", SARA_PROJECT_RES_FOLDER + DRM_FOLDER + pngName);
		//File f = new File(SARA_PROJECT_RES_FOLDER + DRM_FLODER + pngName);
		try
		{
			copyFile(new File(exFolderName + "android/" + DRM_FOLDER + pngName), new File(SARA_PROJECT_RES_FOLDER + DRM_FOLDER + pngName));
			copyFile(new File(exFolderName + "android/" + DRH_FOLDER + pngName), new File(SARA_PROJECT_RES_FOLDER + DRH_FOLDER + pngName));
			copyFile(new File(exFolderName + "android/" + DRXH_FOLDER + pngName), new File(SARA_PROJECT_RES_FOLDER + DRXH_FOLDER + pngName));
			copyFile(new File(exFolderName + "android/" + DRXXH_FOLDER + pngName), new File(SARA_PROJECT_RES_FOLDER + DRXXH_FOLDER + pngName));
			//copyFile(new File(exFolderName + "android/" + DRXXXH_FOLDER + pngName), new File(SARA_PROJECT_RES_FOLDER + DRXXXH_FOLDER + pngName));
		}
		catch (IOException e)
		{}
		//copyFile(CATCH_FOLDER + "android/" + DRM_FLODER, pngName, SARA_PROJECT_RES_FOLDER + DRM_FLODER + pngName);
	}

	public String convertFormatName(String fileName, String format)
	{
		String filenameArray[] = fileName.split("\\.");
		String fileNameStringSet[] = filenameArray[0].split("/");
        return fileNameStringSet[fileNameStringSet.length - 1] + format;

	}

	public MapMarker saveMarker(String name, LatLng p1, String description, double distance, boolean alart, boolean star)
	{
		if (name == null)
			return null;

		Memory memory = new Memory(context);

		return memory.savePlace(name, p1.latitude, p1.longitude, description, distance, alart, star);

	}

	public double getDistanceOnMap(LatLng l1, LatLng l2)
	{
		Location lo1 = new Location("");
		lo1.setLatitude(l1.latitude);
		lo1.setLongitude(l1.longitude);

		Location lo2 = new Location("");
		lo2.setLatitude(l2.latitude);
		lo2.setLongitude(l2.longitude);

		return lo1.distanceTo(lo2);
	}

	public MapMarker getNearbyPlace(List<MapMarker> mapMarkers, LatLng latLng)
	{
		for (int i=0; i < mapMarkers.size(); i++)
		{
			if (getDistanceOnMap(latLng, mapMarkers.get(i).getLatLng()) <= mapMarkers.get(i).getDistance())
			{
				return mapMarkers.get(i);
			}
		}
		return null;
	}

	public void setAlarmForSearchNearbyPlace()
	{
		SharedPreferences sets = context.getSharedPreferences("sets", 0);
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		PendingIntent bcpi = PendingIntent.getBroadcast(context, 1, new Intent(context, WakefulBroadcast.class).putExtra("data", Utils.WAKEFUL_SEARCH_NEARBY_PLACE), 0);
		long currentTime = System.currentTimeMillis();
		if (sets.getLong("search_nearby_place_alarm_time", 0) < currentTime)
		{
			Random r = new Random();
			long nxtLong = r.nextInt((int) (Utils.HOUR / 2) - (int) (Utils.HOUR / 4)) + (int) (Utils.HOUR / 4);
			long alarmTime = currentTime + nxtLong;
			sets.edit().putLong("search_nearby_place_alarm_time", alarmTime).commit();
			am.cancel(bcpi);
			am.set(AlarmManager.RTC_WAKEUP, alarmTime, bcpi);
		}
		else
		{
			am.cancel(bcpi);
			am.set(AlarmManager.RTC_WAKEUP, sets.getLong("search_nearby_place_alarm_time", 0), bcpi);
		}
		Log.d("service", "next check location time:" + Utils.getInstance(context).convertLongToTimeString(sets.getLong("search_nearby_place_alarm_time", 0)));
	}

	public static void setConnection(boolean enable)
	{

		String command;
		if (enable)
			command = COMMAND_L_ON;
		else
			command = COMMAND_L_OFF;        

		try
		{
			Process su = Runtime.getRuntime().exec(COMMAND_SU);
			DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

			outputStream.writeBytes(command);
			outputStream.flush();

			outputStream.writeBytes("exit\n");
			outputStream.flush();
			try
			{
				su.waitFor();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}

			outputStream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void testRoot()
	{

		try
		{
			Process su = Runtime.getRuntime().exec(COMMAND_SU);
			DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

			outputStream.writeBytes(COMMAND_SU);
			outputStream.flush();

			outputStream.writeBytes("exit\n");
			outputStream.flush();
			try
			{
				su.waitFor();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}

			outputStream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static class NetworkUtil
	{
		public static int TYPE_WIFI = 1;
		public static int TYPE_MOBILE = 2;
		public static int TYPE_NOT_CONNECTED = 0;
		public static final int NETWORK_STATUS_NOT_CONNECTED=0,NETWORK_STAUS_WIFI=1,NETWORK_STATUS_MOBILE=2;

		public static int getConnectivityStatus(Context context)
		{
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
			if (null != activeNetwork)
			{
				if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
					return TYPE_WIFI;

				if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
					return TYPE_MOBILE;
			} 
			return TYPE_NOT_CONNECTED;
		}

		public static int getConnectivityStatusString(Context context)
		{
			int conn = NetworkUtil.getConnectivityStatus(context);
			int status = 0;
			if (conn == NetworkUtil.TYPE_WIFI)
			{
				status = NETWORK_STAUS_WIFI;
			}
			else if (conn == NetworkUtil.TYPE_MOBILE)
			{
				status = NETWORK_STATUS_MOBILE;
			}
			else if (conn == NetworkUtil.TYPE_NOT_CONNECTED)
			{
				status = NETWORK_STATUS_NOT_CONNECTED;
			}
			return status;
		}
	}

	public void toggleData()
	{
		if (NetworkUtil.getConnectivityStatus(context) != Utils.NetworkUtil.TYPE_NOT_CONNECTED)
		{
			setConnection(false);
		}
		else
		{
			setConnection(true);
		}
	}

	public Bitmap openBitmap(String path)
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = false;
		options.inDither = false;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		return bitmap;
	}

	public Bitmap getEmoji(String name)
	{
		return openBitmap(EMOJI_FOLDER + name);
	}

	public Bitmap resizeBitmap(String file, int newWidth, int newHeight)
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = false;
		options.inDither = false;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap bitmap = BitmapFactory.decodeFile(file, options);
		// "RECREATE" THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
		//bm.recycle();
		return resizedBitmap;
	}

	public void saveBitmap(Bitmap bitmap, String path)
	{
		FileOutputStream out = null;
		try
		{
			out = new FileOutputStream(path);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
			// PNG is a lossless format, the compression factor (100) is ignored
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (out != null)
				{
					out.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

	}

	public void writeStringToFile(String str, File file)
	{
		try
		{
			if(!file.exists())
				file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(str.getBytes());
			fos.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public String readFile(String path)
	{
		//Find the directory for the SD Card using the API
		//*Don't* hardcode "/sdcard"
		//File sdcard = Environment.getExternalStorageDirectory();

		//Get the text file
		File file = new File(path);

		//Read text from file
		StringBuilder text = new StringBuilder();

		try
		{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;

			while ((line = br.readLine()) != null)
			{
				text.append(line);
				text.append('\n');
			}
			br.close();
		}
		catch (Exception e)
		{
			//You'll need to add proper error handling here
			e.printStackTrace();
		}
		return text.toString();
	}

	public void saveMoment(String emoji, String note)
	{
		Memory memory = new Memory(context);
		memory.saveMoment(emoji, note);
	}
	
	public float convertDpToPixel(float dp){
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
		return px;
	}

    public boolean checkSimcard(String serial)
    {
        Memory memory = new Memory(context);
        return memory.isSimcardSaved(serial);
    }

    public boolean isSimcardsInMemory()
    {
        Memory memory = new Memory(context);
        return memory.hasRecords(Memory.TABLE_SIMCARDS);
    }
	
	public void setSoundControlOn ( boolean b )
	{
		sets.edit().putBoolean("sound_control", b).commit();
	}
	
	public boolean isSoundControlOn()
	{
		return sets.getBoolean("sound_control", true);
	}
	
	public void setSoundControlType (int type )
	{
		sets.edit().putInt("sound_control_type", type).commit();
	}
	
	public int getSoundControlType()
	{
		return sets.getInt("sound_control_type", SOUND_CONTROL_TYPE_VIBRATE);
	}
	
	public boolean canAskFeeling ( )
	{
		boolean b = sets.getLong( "last_saved_feeling_time", 0 ) + ( 60000 * 10 ) < System.currentTimeMillis();
		Log.d("utils", "can ask? " + b);
		return b;
	}
	
	public void musicPlayPause ( )
	{
		KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
		mAudioManager.dispatchMediaKeyEvent(event);
	}
	
	public void musicNext ( )
	{
		if(mAudioManager.isMusicActive()) {
			KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_NEXT);
			mAudioManager.dispatchMediaKeyEvent(event);
		}
	}
	
	public void musicPrevious ( )
	{
		if(mAudioManager.isMusicActive()) {
			KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PREVIOUS);
			mAudioManager.dispatchMediaKeyEvent(event);
		}
	}
	
	public boolean isMusicActive ( )
	{
		return mAudioManager.isMusicActive();
	}
	
	public String getClockString()
	{
		return new SimpleDateFormat("HH:mm").format(new Date());
	}
	
	public int getBatteryPercentage() {

		IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryStatus = context.registerReceiver(null, iFilter);

		int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
		int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

		float batteryPct = level / (float) scale;

		return (int) (batteryPct * 100);
	}
	
	public float readCpuUsage() {
		try {
			RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
			String load = reader.readLine();

			String[] toks = load.split(" ");

			long idle1 = Long.parseLong(toks[5]);
			long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
				+ Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

			try {
				Thread.sleep(360);
			} catch (Exception e) {}

			reader.seek(0);
			load = reader.readLine();
			reader.close();

			toks = load.split(" ");

			long idle2 = Long.parseLong(toks[5]);
			long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
				+ Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

			return (float)(cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return 0;
	}
	
	public int getNumberOfMissedCalls ()
	{
		String[] projection = { CallLog.Calls.CACHED_NAME, CallLog.Calls.CACHED_NUMBER_LABEL, CallLog.Calls.TYPE };
		String where = CallLog.Calls.TYPE+"="+CallLog.Calls.MISSED_TYPE + " AND " + CallLog.Calls.NEW + "=1";          
		Cursor c = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, projection,where, null, null);
		c.moveToFirst();
		return c.getCount();
	}

	public int getNumberOfNewMessage ()
	{
		Uri sms_content = Uri.parse("content://sms");
		Cursor c = context.getContentResolver().query(sms_content, null,"read=0", null, null);
		c.moveToFirst();
		return c.getCount();
	}

}
