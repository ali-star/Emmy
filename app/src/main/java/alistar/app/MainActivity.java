package alistar.app;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.View.*;
import android.view.*;
import com.readystatesoftware.notificationlog.*;
import android.graphics.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.ComponentName;
import android.content.Context;
import android.content.*;
import alistar.app.alarm.*;
import android.graphics.drawable.*;
import java.text.*;
import java.util.*;
import alistar.app.brain.anomalyDetection.anomaly.DetectAnoms;
import alistar.app.map.*;
import com.nanotasks.*;
import alistar.app.brain.*;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import alistar.app.screen_lock.service.*;
import alistar.app.screen_lock.*;
import su.levenetc.android.badgeview.BadgeView;
import java.io.File;

public class MainActivity extends Activity 
{

	private WorkQueue mService;
	private Intent serviceConnectionIntent;
	private SituationLinearLightView gpsSituationView, signalSituationView;
	private BroadcastReceiver mReceiver;
	private TextView solderingYear, solderingMonth, solderingDay, alarmTimeTv, soundControlTv,
			soundControlTypeTv, todayEmotionTextView;
	private Alarm alarm;
	private LinearLightSwitch alarmSwitch, soundControlSwitch, soundControlTypeSwitch;
	private LocationHistoryAdapter lhAdapter;
	private List<LocationHistory> lhData;
	private Memory memory;
	private Utils utils;
	private HashMap<Integer, MapMarker> markers;
	private RecyclerView lhRecyclerView;
	private ScrollView sv;
	private String[] alarmTypeNames = {"WakeUp", "Work", "Sleep", "Flag"};
	private String[] alarmType = {Alarm.TYPE_WAKEUP, Alarm.TYPE_WORK, Alarm.TYPE_SLEEP, Alarm.TYPE_FLAG};
	private BadgeView badgeView;
	private int crashCount;
	
	//private SeekBar seekBar;

	public ServiceConnection mConnection = new ServiceConnection(){

		@Override
		public void onServiceConnected(ComponentName p1, IBinder p2)
		{
			// TODO: Implement this method
			mService = ((WorkQueue.ServiceBinder) p2).getService();
			Toast.makeText(MainActivity.this, "service started", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onServiceDisconnected(ComponentName p1)
		{
			// TODO: Implement this method
		}

	};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		Window window = getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
		window.setStatusBarColor(Color.parseColor("#423F74"));
        setContentView(R.layout.main);
		
		utils = utils.getInstance(this);
		alarm = new Alarm(this);
		
		startService(new Intent(this, SaraViewService.class));
		//utils.testRoot();
		
		SharedPreferencesUtil.init(this);
		if(!SharedPreferencesUtil.get(Lockscreen.ISLOCK)){
			startService(new Intent(this, LockscreenService.class));
		}
		
		utils.initFolders();
		sv = (ScrollView) findViewById(R.id.scrollView);
		sv.setEnabled(false);
		lhRecyclerView = (RecyclerView) findViewById(R.id.location_history_recycler_view);
		lhRecyclerView.setFocusable(false);
        lhRecyclerView.setHasFixedSize(true);
		memory = new Memory(this);
		lhRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		
		
		getLocationHistoryData();
		
		
		gpsSituationView = (SituationLinearLightView) findViewById(R.id.gpsSituationView);
		signalSituationView = (SituationLinearLightView) findViewById(R.id.signalSituationView);
		solderingYear = (TextView) findViewById(R.id.soldering_year);
		solderingMonth = (TextView) findViewById(R.id.soldering_month);
		solderingDay = (TextView) findViewById(R.id.soldering_day);
		alarmTimeTv = (TextView) findViewById(R.id.alarm_time);
		alarmSwitch = (LinearLightSwitch) findViewById(R.id.alarm_switch);
		soundControlSwitch = (LinearLightSwitch) findViewById(R.id.sound_control_switch);
		soundControlTypeSwitch = (LinearLightSwitch) findViewById(R.id.sound_control_type_switch);
		soundControlTv = (TextView) findViewById(R.id.sound_control_tv);
		soundControlTypeTv = (TextView) findViewById(R.id.sound_control_type_tv);

		todayEmotionTextView = findViewById(R.id.todayEmotionTextView);
		
		soundControlTypeSwitch.setOn(utils.getSoundControlType() == 1);
		soundControlTypeTv.setText(utils.getSoundControlType() == 1 ? "VIBRATE" : "SILENT");
		soundControlTypeSwitch.setOnSwitchListenear(new LinearLightSwitch.SwitchListenear()
			{

				@Override
				public void onSwitched ( boolean switched )
				{
					// TODO: Implement this method
					utils.setSoundControlType(switched ? Utils.SOUND_CONTROL_TYPE_VIBRATE : Utils.SOUND_CONTROL_TYPE_SILENT);
					soundControlTypeTv.setText(switched ? "VIBRATE" : "SILENT");
				}
				
			
		});
		
		soundControlSwitch.setOn(utils.isSoundControlOn());
		soundControlTv.setText(utils.isSoundControlOn() ? "SC ON" : "SC OFF");
		soundControlSwitch.setOnSwitchListenear(new LinearLightSwitch.SwitchListenear()
			{

				@Override
				public void onSwitched ( boolean switched )
				{
					// TODO: Implement this method
					utils.setSoundControlOn(switched);
					soundControlTv.setText(switched ? "SC ON" : "SC OFF");
				}
				
			
		});
		
		alarmSwitch.setOn(alarm.isAlarmActive());
		
		alarmSwitch.setOnSwitchListenear(new LinearLightSwitch.SwitchListenear()
			{

				@Override
				public void onSwitched(boolean switched)
				{
					// TODO: Implement this method
					if(alarm.getTimeMillis() < System.currentTimeMillis())
					{
						Calendar oldC = Calendar.getInstance();
						Calendar newC = Calendar.getInstance();
						oldC.setTimeInMillis(alarm.getTimeMillis());
						newC.set(Calendar.HOUR_OF_DAY, oldC.get(Calendar.HOUR_OF_DAY));
						newC.set(Calendar.MINUTE, oldC.get(Calendar.MINUTE));
						alarm.setTimeMillis(newC.getTimeInMillis() + Utils.DAY);
					}
					alarm.setActive(switched);
					//mService.checkAlarm();
				}
				
			
		});
		
		badgeView = (BadgeView) findViewById(R.id.badge);
		
		badgeView.post(new Runnable()
			{

				@Override
				public void run ( )
				{
					badgeView.setValue("Hi Ali");
					new Handler().postDelayed(new Runnable()
						{

							@Override
							public void run ( )
							{
								checkCrashes();
							}						
						
					}, 2000);
				}
				
			
		});
		
		badgeView.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick ( View p1 )
				{
					startActivity(new Intent(MainActivity.this, CrashCenter.class));
				}
				
			
		});
		
		setAlarmText();
		
		alarmTimeTv.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					final SelectTimeDialog std = new SelectTimeDialog(MainActivity.this);
					std.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
					std.show();
					std.setTime(alarm.getTimeMillis());
					std.ok.setOnClickListener(new OnClickListener()
						{

							@Override
							public void onClick(View p1)
							{
								// TODO: Implement this method
								alarm.setTimeMillis(std.getTimeMillis());
								std.dismiss();
								setAlarmText();
							}
							
						
					});
				}
				
			
		});
		
		findViewById(R.id.at_button).setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					final SelectItemDialog sid = new SelectItemDialog(MainActivity.this);
					sid.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
					sid.show();
					sid.setItems(alarmTypeNames);
					for(int i=0; i<alarmType.length; i++)
					{
						if(alarmType[i].equals(alarm.getType()))
						{
							sid.setSelectedItem(alarmTypeNames[i]);
						}
					}
					sid.setOnDismissListener(new Dialog.OnDismissListener()
						{

							@Override
							public void onDismiss(DialogInterface p1)
							{
								alarm.setType(alarmType[sid.getSelectedInteger()]);
							}
							
						
					});
				}
		});
		
		findViewById(R.id.lhl_button).setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					startActivity(new Intent(MainActivity.this, LocationHistoryActivity.class));
				}
				
			
		});

		AliEmotionsChart aliEmotionsChart = findViewById(R.id.ali_emotions_chart);

		aliEmotionsChart.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					startActivity(new Intent(MainActivity.this, SaraMap.class));
				}
		});

		initEmotionsChart(aliEmotionsChart);
		
		/*LinearLightSwitch mSwitch = (LinearLightSwitch) findViewById(R.id.mSwitch);
		mSwitch.setOnSwitchListenear(new LinearLightSwitch.SwitchListenear()
			{

				@Override
				public void onSwitched(boolean switched)
				{
					// TODO: Implement this method
					Toast.makeText(MainActivity.this, "switch " + switched, Toast.LENGTH_LONG).show();
				}
		});*/
		
		//seekBar = (SeekBar) findViewById(R.id.seekBar);
			
		//seekBar.setMax(10);
		//seekBar.setProgress(5);
		
		/*final PodSlider podSlider = (PodSlider) findViewById(R.id.pod_slider);
        assert podSlider != null;
        podSlider.setPodClickListener(new OnPodClickListener() {
				@Override
				public void onPodClick(int position) {
					Log.d("PodPosition", "position = " + position);
				}
			});
        podSlider.setCurrentlySelectedPod(1);
		podSlider.setNumberOfPods(8);
		podSlider.setPodTexts(new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"});
        new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					
				}
			}, 5000);

        new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					
				}
			}, 10000);*/

		checkSignal();
		checkGPS();

		registerReceivers();

		int[] solderingData = Utils.convertLongToTimeText(Utils.getSolderingFinishDate());
		if (solderingData[0] + solderingData[1] + solderingData[2] > 0)
		{
			solderingYear.setText(String.valueOf(solderingData[0]));
			solderingMonth.setText(String.valueOf(solderingData[1]));
			solderingDay.setText(String.valueOf(solderingData[2]));
		}

		//((ImageView) findViewById(R.id.actionBarIcon)).setColorFilter(Color.parseColor("#CCDBFF"));
		Toast.makeText(MainActivity.this, "welcome ali", Toast.LENGTH_LONG).show();
		Log.d("tag", "app running");
		//Log.d("soldering", Utils.getSolderingFinishDateString());
		serviceConnectionIntent = new Intent(MainActivity.this, WorkQueue.class);
		bindService(serviceConnectionIntent, mConnection, Context.BIND_AUTO_CREATE);

		findViewById(R.id.actionBarIcon).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					DevelopDialog dd = new DevelopDialog(MainActivity.this);
					dd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
					dd.show();
				}
			});

		final TextView s1s = (TextView) findViewById(R.id.s1s);
		s1s.setText(Utils.getSim1Serial(MainActivity.this));
		((TextView) findViewById(R.id.s1b)).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					//mService.detectLocation();
					final AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this)
						.setTitle("Change sim serial")
						.setMessage("are you sure to change sim1 serial?")
						.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which)
							{ 
								Utils.saveSim1Serial(MainActivity.this, Utils.getSimSerial(MainActivity.this));
								Log.d("sim serial", Utils.getSimSerial(MainActivity.this));
								s1s.setText(Utils.getSim1Serial(MainActivity.this));
							}
						})
						.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which)
							{ 
								// do nothing
							}
						});
					ad.show();
				}
			});

		final TextView s2s = (TextView) findViewById(R.id.s2s);
		s2s.setText(Utils.getSim2Serial(MainActivity.this));
		((TextView) findViewById(R.id.s2b)).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					//mService.detectLocation();
					final AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this)
						.setTitle("Change sim serial")
						.setMessage("are you sure to change sim2 serial?")
						.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which)
							{ 
								Utils.saveSim2Serial(MainActivity.this, Utils.getSimSerial(MainActivity.this));
								Log.d("sim serial", Utils.getSimSerial(MainActivity.this));
								s2s.setText(Utils.getSim2Serial(MainActivity.this));
							}
						})
						.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which)
							{ 
								// do nothing
							}
						});
					ad.show();
				}
			});
			
			//startService(new Intent(this, MyService.class).putExtra("command", Utils.WAKEFUL_SEARCH_NEARBY_PLACE));

    }

	private void initEmotionsChart(AliEmotionsChart aliEmotionsChart) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.set(Calendar.HOUR_OF_DAY, 0);
    	calendar.set(Calendar.MINUTE, 0);
    	calendar.set(Calendar.SECOND, 0);
    	calendar.set(Calendar.MILLISECOND, 0);

    	long startTime = calendar.getTimeInMillis();
    	long endTime = System.currentTimeMillis();

    	long todayFirstRecordTime = 0;

		List<Emotion> todayEmotions = utils.getEmotionsByTimeRange(startTime, endTime);

		if (!todayEmotions.isEmpty())
			todayFirstRecordTime = todayEmotions.get(0).getDate();

		int overall = 0;

		for(Emotion emotion : todayEmotions) {
			overall += emotion.getFeeling();
		}

		double todayAverage = 0;

		if (todayEmotions.size() > 0)
			todayAverage = (double) overall / (double) todayEmotions.size();

		if (todayAverage >= 0.1) {
			todayEmotionTextView.setTextColor(ContextCompat.getColor(this, R.color.green));
		} else if (todayAverage < 0.1 && todayAverage > -0.1)
			todayEmotionTextView.setTextColor(ContextCompat.getColor(this, R.color.white));
		else
			todayEmotionTextView.setTextColor(ContextCompat.getColor(this, R.color.red));

		String todayAverageText = String.format(Locale.ENGLISH,"%.3f", todayAverage);
		todayEmotionTextView.setText(todayAverage > 0 ? "+" + todayAverageText : todayAverageText);

		List<Emotion> chartEmotions = utils.getEmotions(24);
		for (Emotion emotion : chartEmotions) {
			if (emotion.getDate() == todayFirstRecordTime) {
				emotion.setDayFirstRecord(true);
			}
		}

		List<Emotion> detectAnomsEmotions = utils.getEmotions(500);

		DetectAnoms.Config config = new DetectAnoms.Config();
		config.setMaxAnoms(0.2);
		config.setAnomsThreshold(0.02);
		config.setAlpha(0.05);
		config.setNumObsPerPeriod(100);

		DetectAnoms detectAnoms = new DetectAnoms(config);

		long[] times = new long[detectAnomsEmotions.size()];
		double[] series = new double[detectAnomsEmotions.size()];
		for (int i = 0; i < detectAnomsEmotions.size(); i++) {
			times[i] = detectAnomsEmotions.get(i).getDate();
			series[i] = detectAnomsEmotions.get(i).getFeeling();
		}

		try {
			DetectAnoms.ANOMSResult anomsResult = detectAnoms.anomalyDetection(times, series);
			for (long index : anomsResult.getAnomsIndex()) {
				for (Emotion emotion : chartEmotions) {
					if (emotion.getDate() == detectAnomsEmotions.get((int) index).getDate()) {
						emotion.setAnomaly(true);
					}
				}
				Log.i("anomaly detected", "feeling: " + detectAnomsEmotions.get((int) index).getFeeling() + " index: " + index);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		aliEmotionsChart.setData(chartEmotions);
	}

	private void checkGPS()
	{

		if (Utils.isGpsOn(MainActivity.this))
		{
			gpsSituationView.setSituation(SituationLinearLightView.SITUATION_GOOD);
		}
		else
		{
			gpsSituationView.setSituation(SituationLinearLightView.SITUATION_BAD);
		}
	}

	private void checkSignal()
	{
		if (!Utils.isAirplaneModeOn(MainActivity.this))
		{
			signalSituationView.setSituation(SituationLinearLightView.SITUATION_GOOD);
		}
		else
		{
			signalSituationView.setSituation(SituationLinearLightView.SITUATION_BAD);
		}
	}

	private void registerReceivers()
	{
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.location.PROVIDERS_CHANGED");
		filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		mReceiver = new BroadcastReceiver(){
			@Override
			public void onReceive(Context context, Intent intent)
			{
				if (intent.getAction().equals("android.location.PROVIDERS_CHANGED") | intent.getAction().equals(Intent.ACTION_AIRPLANE_MODE_CHANGED))
				{
					Log.i("Check", "PROVIDERS_CHANGED");
					checkMainSwitchs();
				}
			}
		};
		registerReceiver(mReceiver, filter);
	}

	public void checkMainSwitchs()
	{
		if (!Utils.isAirplaneModeOn(getApplicationContext()))
		{
			signalSituationView.setSituation(SituationLinearLightView.SITUATION_GOOD);
		}
		else
		{
			signalSituationView.setSituation(SituationLinearLightView.SITUATION_BAD);
		}

		if (Utils.isGpsOn(getApplicationContext()))
		{
			gpsSituationView.setSituation(SituationLinearLightView.SITUATION_GOOD);
		}
		else
		{
			gpsSituationView.setSituation(SituationLinearLightView.SITUATION_BAD);
		}
	}

	public void setAlarmText()
	{
		alarmTimeTv.setText(new SimpleDateFormat("HH:mm").format(new Date(alarm.getTimeMillis())));
	}
	
	public void getLocationHistoryData()
	{
		Tasks.executeInBackground(this, new BackgroundWork<String>() {
				@Override
				public String doInBackground() throws Exception {
					//final int DELAY = 3;
					//Thread.sleep(TimeUnit.SECONDS.toMillis(DELAY));
					Log.d("task", "working");
					lhData = memory.getLocationHistoryList(3);
					markers = new HashMap<Integer, MapMarker>();
					for (int i=0; i < lhData.size(); i++)
					{
						markers.put(i, memory.findMapMarkerById(lhData.get(i).getMapMarkerId()));
					}
					Log.d("task", "work ok");
					return null;
				}
			}, new Completion<String>() {
				@Override
				public void onSuccess(Context context, String result) {
					lhAdapter = new LocationHistoryAdapter(lhData, MainActivity.this, utils, markers);
					lhRecyclerView.setAdapter(lhAdapter);
					sv.setEnabled(true);
					Log.d("task", "done");
				}

				@Override
				public void onError(Context context, Exception e) {
					Log.d("task", "error");
					Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();

				}
			});
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
		unregisterReceiver(mReceiver);
		unbindService(mConnection);
	}
	
	public void findMe()
	{
		mService.addWork( System.currentTimeMillis(), Work.WorkName.FIND_ME );
	}
	
	public void checkCrashes ( )
	{
		badgeView.setValue("checking crashes...");

		File directory = new File(Utils.CRASH_FOLDER);
		if (!directory.exists())
			directory.mkdirs();
		File[] files = directory.listFiles();
		Log.d("Files", "Size: "+ files.length);
		crashCount = 0;
		for (int i = 0; i < files.length; i++) {
			Log.d("Files", "FileName:" + files[i].getName());
			String crash = utils.readFile(files[i].getPath());
			if(crash.indexOf("check_ok")<0) {
				crashCount++;
			}
		}
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run () {
				badgeView.setValue(String.valueOf(crashCount));
			}
		}, 1000);
	}
	



}
