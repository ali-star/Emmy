package alistar.app.screen_lock.service;

import alistar.app.*;
import alistar.app.screen_lock.*;
import alistar.app.utils.*;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.readystatesoftware.notificationlog.*;
import alistar.app.timeline.*;
import alistar.app.ui.*;
import android.speech.SpeechRecognizer;
import android.speech.RecognizerIntent;
import android.speech.RecognitionListener;
import java.util.ArrayList;
//import com.readystatesoftware.notificationlog.*;


/**
 * Created by mugku on 15. 5. 20..
 */
public class LockscreenViewService extends Service
{
    private final int LOCK_OPEN_OFFSET_VALUE = 50;
    private Context mContext = null;
    private LayoutInflater mInflater = null;
    private View mLockscreenView = null;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;
    //private ShimmerTextView mShimmerTextView = null;
    private boolean mIsLockEnable = false;
    private boolean mIsSoftkeyEnable = false;
    private int mDeviceWidth = 0;
    private int mDevideDeviceWidth = 0;
    private float mLastLayoutX = 0;
    private int mServiceStartId = 0;
    private SendMassgeHandler mMainHandler = null;
	private String[] dataToggleSteps = LockScreenUtils.dataToggleSteps;
	private String[] unlockSteps = LockScreenUtils.unlockSteps;
	private int stepCounter = -1;
	private Handler handler = new Handler();
	private Utils utils;
	private ColoredImageView dataView , missedCallView , newMessageView , musicStart, musicPlayPause, musicNext, musicPrevious;
	private NetworkChangeReceiver ncrReceiver = null;
	private String[] momentSteps = LockScreenUtils.momentSteps;
	private View unlock;
	private SaraView sara;
	private View musicControlBase;
	private BroadcastReceiver mTimeReceiver;
	private boolean saraUnlockAnimationSet = false;
	private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
	private MyApplication app;
	private CircularBattryLevel batttyLevel;
	private long startTime = 0;
	private View ready;
	private BackgroundEffect backgroundEffect;
//    private boolean sIsSoftKeyEnable = false;

    private class SendMassgeHandler extends android.os.Handler
	{
        @Override
        public void handleMessage(Message msg)
		{
            super.handleMessage(msg);
            //changeBackGroundLockView(mLastLayoutX);
        }
    }
    @Override
    public IBinder onBind(Intent intent)
	{
        return null;
    }

	public class NetworkChangeReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(final Context context,final Intent intent)
		{

			int status = Utils.NetworkUtil.getConnectivityStatus(context);
			//Log.e ( "Sulod sa network reciever", "Sulod sa network reciever" );
			if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction()))
			{
				if (status == Utils.NetworkUtil.NETWORK_STATUS_NOT_CONNECTED)
				{
					//new ForceExitPause(context).execute();
					//makeText("diaconnected");
					if (dataView != null)
						dataView.setVisibility(View.GONE);
				}else
				{
					//new ResumeForceExitPause(context).execute();
					//makeText("connected");
					if (dataView != null)
						dataView.setVisibility(View.VISIBLE);
				}

			}
		}
	}


    @Override
    public void onCreate()
	{
        super.onCreate();
		startTime = System.currentTimeMillis();
		app = (MyApplication) getApplication();
        mContext = this;
        SharedPreferencesUtil.init(mContext);
		utils = Utils.getInstance(mContext);
		ncrReceiver = new NetworkChangeReceiver();

		IntentFilter filter = new IntentFilter();
		filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");

		IntentFilter timeTickFilter = new IntentFilter(Intent.ACTION_TIME_TICK);
		registerReceiver(mTimeReceiver = new BroadcastReceiver()
						 {

							 @Override
							 public void onReceive(Context p1,Intent p2)
							 {
								 if (mLockscreenView != null)
								 {
									 (( TextView ) mLockscreenView.findViewById(R.id.time)).setText(utils.getClockString());
									 batttyLevel.setLevel(utils.getBatteryPercentage());
								 }
							 }


						 }, timeTickFilter);

		registerReceiver(ncrReceiver, filter);
		speech = SpeechRecognizer.createSpeechRecognizer(this);
		speech.setRecognitionListener(new RecognitionListener()
			{

				@Override
				public void onReadyForSpeech(Bundle p1)
				{
					sara.setWaitingMode(false);
				}

				@Override
				public void onBeginningOfSpeech()
				{
					// TODO: Implement this method
				}

				@Override
				public void onRmsChanged(float p1)
				{
					// TODO: Implement this method
				}

				@Override
				public void onBufferReceived(byte[] p1)
				{
					// TODO: Implement this method
				}

				@Override
				public void onEndOfSpeech()
				{
					// TODO: Implement this method
				}

				@Override
				public void onError(int p1)
				{
					// TODO: Implement this method
				}

				@Override
				public void onResults(Bundle results)
				{
					Log.i("sara", "onResults");
					ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
					String text = "";
					for (String result : matches)
						text += result + "\n";
					Log.d("text", text);
					if (matches.get(0).toLowerCase().equals("i am ali"))
					{
						dettachLockScreenView();
					}
					if (matches.get(0).toLowerCase().equals("i'm ali"))
					{
						dettachLockScreenView();
					}
					if (matches.get(0).toLowerCase().equals("hey baby"))
					{
						dettachLockScreenView();
					}
					sara.say(matches.get(0));
				}

				@Override
				public void onPartialResults(Bundle p1)
				{
					// TODO: Implement this method
				}

				@Override
				public void onEvent(int p1,Bundle p2)
				{
					// TODO: Implement this method
				}


			});
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
//        sIsSoftKeyEnable = SharedPreferencesUtil.get(Lockscreen.ISSOFTKEY);
    }


    @Override
    public int onStartCommand(Intent intent,int flags,int startId)
	{
		//Log.d("LockscreenViewService", "LockscreenViewService onStartCommand");
        mMainHandler = new SendMassgeHandler();
        if (isLockScreenAble())
		{
            if (null == mWindowManager)
			{
				initState();
				initView();
				attachLockScreenView();
				utils.vaibrate(new long[] {20, 70, 20, 70}, -1);
            }
        }
        return LockscreenViewService.START_NOT_STICKY;
    }

    @Override
    public void onDestroy()
	{
		try
		{
			if (speech != null)
			{
				speech.destroy();
				Log.i("sara", "destroy");
			}
			unregisterReceiver(mTimeReceiver);
			unregisterReceiver(ncrReceiver);
			dettachLockScreenView();
		}catch (Exception e)
		{

		}
    }


    private void initState()
	{
		try
		{
			mIsLockEnable = LockScreenUtils.getInstance(mContext).isStandardKeyguardState();
			if (mIsLockEnable)
			{
				mParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
			}else
			{
				mParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
					| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
					| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,
                    PixelFormat.TRANSLUCENT);
			}
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
			{
				if (mIsLockEnable && mIsSoftkeyEnable)
				{
					mParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
				}else
				{
					mParams.flags = WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;
				}
			}else
			{
				mParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
			}

			if (null == mWindowManager)
			{
				mWindowManager = ((WindowManager) mContext.getSystemService(WINDOW_SERVICE));
			}
		}catch ( Exception e )
		{
			e.printStackTrace();
		}
    }

    private void initView()
	{
		try
		{
			if (null == mInflater)
			{
				mInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			}

			if (null == mLockscreenView)
			{
				mLockscreenView = mInflater.inflate(R.layout.lockscreen_activity, null);
				dataView = (ColoredImageView) mLockscreenView.findViewById(R.id.data_icon);
				missedCallView = (ColoredImageView) mLockscreenView.findViewById(R.id.missed_call_icon);
				newMessageView = (ColoredImageView) mLockscreenView.findViewById(R.id.new_message_icon);
				sara = (SaraView) mLockscreenView.findViewById(R.id.sara);
				sara.setTextView((TextView)mLockscreenView.findViewById(R.id.sara_tv));
				batttyLevel = (CircularBattryLevel) mLockscreenView.findViewById(R.id.battry_level);
				ready = mLockscreenView.findViewById(R.id.ready);
				backgroundEffect = (BackgroundEffect) mLockscreenView.findViewById(R.id.bg_effect);
			}

			if (null == unlock)
			{
				unlock = mLockscreenView.findViewById(R.id.unlock);
			}

			//if ( null == musicControlBase )
			//{
			musicControlBase = mLockscreenView.findViewById(R.id.music_control_base);
			musicPlayPause = (ColoredImageView) mLockscreenView.findViewById(R.id.music_play_pause);
			musicNext = (ColoredImageView) mLockscreenView.findViewById(R.id.music_next);
			musicPrevious = (ColoredImageView) mLockscreenView.findViewById(R.id.music_previous);
			musicStart = (ColoredImageView) mLockscreenView.findViewById(R.id.music_start);
			//}
		}catch ( Exception e )
		{
			e.printStackTrace();
			Log.d("tag", e.getMessage().toString());
		}
    }

    private boolean isLockScreenAble()
	{
        /*boolean isLock = SharedPreferencesUtil.get(Lockscreen.ISLOCK);
		 if (isLock) {
		 isLock = true;
		 } else {
		 isLock = false;
		 }*/
        return true;
    }


    private void attachLockScreenView()
	{

        if (null != mWindowManager && null != mLockscreenView && null != mParams)
		{

			(( TextView ) mLockscreenView.findViewById(R.id.time)).setText(utils.getClockString());

			batttyLevel.setLevel(utils.getBatteryPercentage());

			if (utils.isMusicActive())
			{
				musicControlBase.setVisibility(View.VISIBLE);
				musicStart.setVisibility(View.INVISIBLE);
			}

			sara.setOnClickListener(new View.OnClickListener()
				{

					@Override
					public void onClick(View p1)
					{
						sara.setWaitingMode(true);
						utils.Vibrate(200);
						speech.startListening(recognizerIntent);
					}


				});

			musicStart.setOnClickListener(new View.OnClickListener()
				{

					@Override
					public void onClick(View p1)
					{
						musicControlBase.setVisibility(View.VISIBLE);
						musicStart.setVisibility(View.INVISIBLE);
						utils.Vibrate(200);
						try
						{
							utils.musicPlayPause();
						}catch ( Exception e )
						{
							e.printStackTrace();
						}
					}


				});

			musicPlayPause.setOnClickListener(new View.OnClickListener()
				{

					@Override
					public void onClick(View p1)
					{
						utils.Vibrate(200);
						try
						{
							utils.musicPlayPause();
							if (utils.isMusicActive())
							{
								musicControlBase.setVisibility(View.INVISIBLE);
								musicStart.setVisibility(View.VISIBLE);
							}else
							{

							}
						}catch ( Exception e )
						{
							e.printStackTrace();
							musicControlBase.setVisibility(View.INVISIBLE);
							musicStart.setVisibility(View.VISIBLE);
						}
					}


				});

			musicNext.setOnClickListener(new View.OnClickListener()
				{

					@Override
					public void onClick(View p1)
					{
						try
						{
							utils.musicNext();
						}catch ( Exception e )
						{
							e.printStackTrace();
						}
					}


				});

			musicPrevious.setOnClickListener(new View.OnClickListener()
				{

					@Override
					public void onClick(View p1)
					{
						try
						{
							utils.musicPrevious();
						}catch ( Exception e )
						{
							e.printStackTrace();
						}
					}


				});

			unlock.setOnClickListener(new View.OnClickListener()
				{

					@Override
					public void onClick(View p1)
					{
						// TODO: Implement this method
						dettachLockScreenView();
					}


				});
			sara.setRedius(5);
			resetStepCounter();
            mLockscreenView.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext())
				{

					@Override
					public void onDoubleClick()
					{
						super.onDoubleClick();
						// your on onDoubleClick here
					    doubleClick();
					}

					@Override
					public void onLongClick()
					{
						super.onLongClick();
						// your on onLongClick here
					}

					@Override
					public void onSwipeUp()
					{
						super.onSwipeUp();
						// your swipe up here
						swipeUp();
					}

					@Override
					public void onSwipeDown()
					{
						super.onSwipeDown();
						// your swipe down here.
						swipeDown();
					}

					@Override
					public void onSwipeLeft()
					{
						super.onSwipeLeft();
						// your swipe left here.
						swipeLeft();
					}

					@Override
					public void onSwipeRight()
					{
						super.onSwipeRight();
						// your swipe right here.
						swipeRight();
					}
				});

			/*mLockscreenView.setOnClickListener(new View.OnClickListener()
			 {

			 @Override
			 public void onClick ( View p1 )
			 {
			 // TODO: Implement this method
			 dettachLockScreenView();
			 }


			 });*/

			String notificationSTR = "";
			if (utils.getNumberOfMissedCalls() > 0)
			{
				notificationSTR += "Missed Call";
				missedCallView.setVisibility(View.VISIBLE);
			}

			if (utils.getNumberOfNewMessage() > 0)
			{
				if (!notificationSTR.equals(""))
					notificationSTR += ", New Message";
				else
					notificationSTR += "New Message";
				newMessageView.setVisibility(View.VISIBLE);
			}

			sara.say(notificationSTR);

			final int[] saraXY = new int[2];
			
			sara.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
				{

					@Override
					public void onGlobalLayout()
					{
						if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
							sara.getViewTreeObserver().removeGlobalOnLayoutListener(this);
						} else {
							sara.getViewTreeObserver().removeOnGlobalLayoutListener(this);
						} 
						sara.getLocationOnScreen(saraXY);
						backgroundEffect.setCenter(saraXY [0] + (sara.getMeasuredWidth() / 2), saraXY [1] + (sara.getMeasuredHeight() / 2));
					}

				
				});

			

            mWindowManager.addView(mLockscreenView, mParams);
			startTime = System.currentTimeMillis() - startTime;
			Log.d("ls start time", String.valueOf(startTime));
			sara.say(String.valueOf(startTime));
			//Log.d("lockView", "lockView added");
        }

    }


    private boolean dettachLockScreenView()
	{
        if (null != mWindowManager && null != mLockscreenView)
		{
			sara.say("dettachLockScreenView");
			backgroundEffect.disconnect();
            mWindowManager.removeView(mLockscreenView);
            mLockscreenView = null;
            mWindowManager = null;
            stopSelf(mServiceStartId);
            return true;
        }else
		{
            return false;
        }
    }

	private void doubleClick()
	{
		//Toast.makeText(LockScreenActivity.this, "click", Toast.LENGTH_SHORT).show();
		checkStep("DOUBLE_CLICK");
	}

	private void click()
	{
		//Toast.makeText(LockScreenActivity.this, "click", Toast.LENGTH_SHORT).show();
		checkStep("CLICK");
	}

	private void swipeLeft()
	{
		//Toast.makeText(LockScreenActivity.this, "swpie left", Toast.LENGTH_SHORT).show();
		checkStep("SWIPE_LEFT");
	}

	private void swipeRight()
	{
		//Toast.makeText(LockScreenActivity.this, "swpie right", Toast.LENGTH_SHORT).show();
		checkStep("SWIPE_RIGHT");
	}

	private void swipeUp()
	{
		//Toast.makeText(LockScreenActivity.this, "swipe up", Toast.LENGTH_SHORT).show();
		checkStep("SWIPE_UP");
	}

	private void swipeDown()
	{
		//Toast.makeText(LockScreenActivity.this, "swipe down", Toast.LENGTH_SHORT).show();
		checkStep("SWIPE_DOWN");
	}

	private Runnable run = new Runnable()
	{

		@Override
		public void run()
		{
			// TODO: Implement this method
			//Toast.makeText(getApplicationContext(), "star agine", Toast.LENGTH_SHORT).show();
			resetStepCounter();
		}


	};

	private void resetStepCounter()
	{

		unlockStepCounter = -1;
		//Animate.alpha ( sara, 1, 400, null );
		ready.setVisibility(View.VISIBLE);
		saraUnlockAnimationSet = false;
		//saraActCould();
	}

	private int unlockStepCounter = -1;
	private int toggleDataStepCounter = -1;
	private int momentStepCounter = -1;

	private void checkStep(String step)
	{
		//saraActGood();
		Log.d("Tag", step);
		try
		{
			addStep();
			if (!saraUnlockAnimationSet)
			{
				//Animate.alpha ( sara, .5f, 400, null );
				ready.setVisibility(View.INVISIBLE);
				saraUnlockAnimationSet = true;
			}
			if (unlockStepCounter < unlockSteps.length)
			{
				if (unlockSteps [unlockStepCounter].equals(step))
				{
					Log.d("unlock screen", "step" + unlockStepCounter + "/" + unlockSteps.length);
					if (unlockStepCounter == unlockSteps.length - 1)
					{
						//sara.say("unlock ok");
						handler.removeCallbacks(run);
						//finish();
						dettachLockScreenView();
						return;
					}
				}else
				{
					unlockStepCounter = -1;
				}
			}else
			{
				unlockStepCounter = -1;
			}

			/*if ( stepCounter < momentSteps.length )
			 {
			 if ( momentSteps [ momentStepCounter ].equals ( step ) )
			 {
			 Log.d ( "unlock screen", "step" + momentStepCounter + "/" + momentSteps.length );
			 if ( momentStepCounter == momentSteps.length - 1 )
			 {
			 //makeText("work");
			 //utils.toggleData();
			 dettachLockScreenView ( );
			 startActivity ( new Intent ( getApplicationContext ( ), AskFeelingActivity.class ).setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK ) );
			 return;
			 }
			 }
			 else
			 {
			 momentStepCounter = -1;
			 }
			 }
			 else
			 {
			 momentStepCounter = -1;
			 }*/
		}catch ( Exception e )
		{
			unlockStepCounter = -1;
			ready.setVisibility(View.INVISIBLE);
			//Animate.alpha ( sara, .5f, 400, null );
		}


		/*if (stepCounter < dataToggleSteps.length)
		 {
		 if (dataToggleSteps[toggleDataStepCounter].equals(step))
		 {
		 Log.d("unlock screen", "step" + toggleDataStepCounter + "/" + dataToggleSteps.length);
		 if (toggleDataStepCounter == dataToggleSteps.length - 1)
		 {
		 //makeText("work");
		 utils.toggleData();
		 //dettachLockScreenView();
		 return;
		 }
		 }
		 else
		 {
		 toggleDataStepCounter = -1;
		 }
		 }*/

		handler.removeCallbacks(run);
		handler.postDelayed(run, 1000);
	}

	private void addStep()
	{
		unlockStepCounter++;
		toggleDataStepCounter++;
		momentStepCounter++;
	}
}
