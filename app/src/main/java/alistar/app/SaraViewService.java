package alistar.app;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.media.MediaPlayer;
import android.os.*;
import android.view.*;
import android.view.WindowManager.*;
import android.widget.*;
import alistar.app.utils.*;
import alistar.app.ui.*;
import alistar.app.timeline.*;
import com.readystatesoftware.notificationlog.*;
import android.provider.*;
import java.util.*;
import alistar.app.utils.SwipeDetector.SwipeTypeEnum;
import android.view.animation.LinearInterpolator;
import com.github.hujiaweibujidao.yava.EasingFunction;
import android.animation.ObjectAnimator;
import com.github.hujiaweibujidao.yava.Functions;
import com.github.hujiaweibujidao.yava.IFunction;
import cimi.com.easeinterpolator.EaseSineInInterpolator;
import cimi.com.easeinterpolator.EaseCircularOutInterpolator;
import android.animation.Animator;
import alistar.app.brain.Work;
import alistar.app.brain.*;
import cimi.com.easeinterpolator.*;
import alistar.app.saraviewservice.utils.*;

public class SaraViewService extends Service

{

	public class MyBinder extends Binder
	{

		public SaraViewService getService ( )
		{
			return SaraViewService.this;
		}

	}

	protected ServiceConnection mServerConn = new ServiceConnection ( ) {
		@Override
		public void onServiceConnected ( ComponentName name, IBinder binder )
		{
			Log.d ( "tag", "onServiceConnected" );
			workQueue = ( (WorkQueue.ServiceBinder) binder ).getService ( );
		}

		@Override
		public void onServiceDisconnected ( ComponentName name )
		{
			Log.d ( "tag", "onServiceDisconnected" );
		}
	};

	public void start ( )
	{
		// mContext is defined upper in code, I think it is not necessary to explain what is it
		Intent intent = new Intent ( getApplicationContext ( ), WorkQueue.class );
		bindService ( intent, mServerConn, Context.BIND_AUTO_CREATE );
		startService ( intent );
	}

	public void stop ( )
	{
		//mContext.stopService(new Intent(mContext, ServiceRemote.class));
		unbindService ( mServerConn );
	}

	@Override
	public IBinder onBind ( Intent p1 )
	{
		// TODO: Implement this method
		return serviceBinder;
	}


	private WindowManager mWindowManager;
	private LayoutInflater mInflater = null;
	private SaraView sara = null;
	private TextView saraTv = null;
	private View saraBase = null;
	private View listBase = null;
	private LinearLayout listFrame = null;
	private ViewGroup saveEmothionBase = null;
	private View saveEmotionFrame = null;
	private DiamondButton findMeBtn, saveFeelingBtn, saveMomentBtn, dialerBtn, massagingBtn, lockBtn;
	private Utils utils = null;
	private LayoutParams slideAreaParams, saraBaseParams, pageParams;
	private MyBinder serviceBinder = new MyBinder ( );
	public WorkQueue workQueue = null;
	public boolean isSaraVisible = false;
	private View saraFrame = null;
	public int STATE_HIDE = 0, STATE_STANDBY = 1, STATE_TEXT = 2, STATE_LIST = 3, STATE_WORK = 4;
	private int STATE = STATE_TEXT;
	private View lock = null;
	private View slideArea = null;
	public boolean hold = false;
	public boolean isListVisible = false;
	public boolean isSaveEmotionPageVisible = false;
	private AliEmotionCircle aliEmotionView;
	private OnEmotionSaved onEmotionSavedListener;

	public void onCreate ( )
	{
		super.onCreate ( );
		initState ( );
		initView ( );
		addViews ( );
		start ( );
	}

	@Override
	public void onDestroy ( )
	{
		super.onDestroy ( );
		stop ( );
	}

	private void initState ( )
	{
		if ( mWindowManager == null )
		{
			mWindowManager = (WindowManager)getSystemService ( WINDOW_SERVICE );
		}

		if ( utils == null )
		{
			utils = Utils.getInstance ( getApplicationContext ( ) );
		}
	}

	private void initView ( )
	{
		if ( mInflater == null )
		{
			mInflater = (LayoutInflater) getBaseContext ( ).getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
		}

		if ( slideArea == null )
		{
			slideArea = mInflater.inflate ( R.layout.slide_area, null );
		}

		if ( saraBase == null )
		{
			saraBase = mInflater.inflate ( R.layout.sara_on_screen_view, null );
			sara = (SaraView) saraBase.findViewById ( R.id.sara );
			saraTv = (TextView) saraBase.findViewById ( R.id.sara_tv );
			saraFrame = saraBase.findViewById ( R.id.sara_frame );
			sara.setRedius ( 4 );
			sara.setTextView ( saraTv );
		}

		if ( listBase == null )
		{
			listBase = mInflater.inflate ( R.layout.sara_on_screen_list, null );
			listFrame = ( LinearLayout ) listBase.findViewById ( R.id.list_frame );
			/*saveFeelingBtn = (DiamondButton) listBase.findViewById ( R.id.save_feeling_btn );
			saveMomentBtn = (DiamondButton)  listBase.findViewById ( R.id.save_moment_btn );
			findMeBtn = (DiamondButton) listBase.findViewById ( R.id.find_me_btn );
			dialerBtn = (DiamondButton) listBase.findViewById ( R.id.phone_btn );
			massagingBtn = (DiamondButton)  listBase.findViewById ( R.id.massages_btn );
			lockBtn = (DiamondButton) listBase.findViewById ( R.id.lock_btn );*/
		}
	}

	private void addViews ( )
	{
		if ( mWindowManager != null )
		{
			slideAreaParams = new WindowManager.LayoutParams (
					WindowManager.LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.TYPE_PHONE,
					WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
					PixelFormat.TRANSLUCENT );

			saraBaseParams = new WindowManager.LayoutParams (
					WindowManager.LayoutParams.MATCH_PARENT,
					WindowManager.LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.TYPE_PHONE,
					WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
					PixelFormat.TRANSLUCENT );

			pageParams = new WindowManager.LayoutParams (
					WindowManager.LayoutParams.MATCH_PARENT,
					WindowManager.LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.TYPE_PHONE,
					WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
					PixelFormat.TRANSLUCENT );

			pageParams.gravity = Gravity.BOTTOM;
			pageParams.x = 0;
			pageParams.y = utils.dpToTx ( 36 );

			saraBaseParams.gravity = Gravity.BOTTOM;
			saraBaseParams.x = 0;
			saraBaseParams.y = 0;

			slideAreaParams.gravity = Gravity.BOTTOM | Gravity.LEFT;
			slideAreaParams.x = 0;
			slideAreaParams.y = 0;
			mWindowManager.addView ( slideArea, slideAreaParams );

			try
			{
				new SwipeDetector ( slideArea ).setOnSwipeListener ( new SwipeDetector.onSwipeEvent ( )
				{

					@Override
					public void SwipeEventDetected ( View v, SwipeDetector.SwipeTypeEnum SwipeType )
					{
						// TODO: Implement this method
						if ( SwipeType == SwipeDetector.SwipeTypeEnum.LEFT_TO_RIGHT )
						{
							MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.menu_open);
							mediaPlayer.start();
							showSaraBase ( );
						}
					}


				} );

				new SwipeDetector ( saraBase ).setOnSwipeListener ( new SwipeDetector.onSwipeEvent ( )
				{

					@Override
					public void SwipeEventDetected ( View v, SwipeDetector.SwipeTypeEnum SwipeType )
					{
						// TODO: Implement this method
						if ( SwipeType == SwipeDetector.SwipeTypeEnum.BOTTOM_TO_TOP )
						{
							showList ( );
						}
					}


				} );

				sara.setOnClickListener ( new View.OnClickListener ( )
				{

					@Override
					public void onClick ( View p1 )
					{
						// TODO: Implement this method
						if ( isSaveEmotionPageVisible )
							return;
						if (!pagesClosed())
							hideSaraBase();
					}


				} );
				createListButtons();
			}
			catch ( Exception e )
			{
				e.printStackTrace ( );
			}
		}
	}

	private void initSaveEmotionButtons ( )
	{
		aliEmotionView.setOnEmotionSelectedListener( new AliEmotionCircle.OnEmotionSelected ()
		{

			@Override
			public void OnEmotionSelected ( int emotion )
			{
				if ( onEmotionSavedListener != null )
				{
					onEmotionSavedListener.onEmotionSaved( emotion );
					getSharedPreferences("data", 0).edit().putLong("last_saved_feeling_time", System.currentTimeMillis()).commit();
				}
			}


		});
	}

	public void setOnEmotionSavedListener ( OnEmotionSaved oes )
	{
		onEmotionSavedListener = oes;

	}

	/*private void initListButtons ( )
	{
		saveFeelingBtn.setOnClickListener ( new View.OnClickListener ( )
			{

				@Override
				public void onClick ( View p1 )
				{
					// TODO: Implement this method
					//startActivity ( new Intent ( getApplicationContext ( ), AskFeelingActivity.class ).setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK ) );
					hideList ( );
					if ( utils.canAskFeeling() )
						workQueue.addWork( System.currentTimeMillis(), Work.WorkName.ASK_FEELING );
				}


			} );

		saveMomentBtn.setOnClickListener ( new View.OnClickListener ( )
			{

				@Override
				public void onClick ( View p1 )
				{
					// TODO: Implement this method
					startActivity ( new Intent ( getApplicationContext ( ), QuickTimelineNote.class ).setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK ) );
					hideList ( );
					workQueue.addTextToQueue ( "Moments..." );
				}


			} );

		dialerBtn.setOnClickListener ( new View.OnClickListener ( )
			{

				@Override
				public void onClick ( View p1 )
				{
					Intent intent = new Intent ( Intent.ACTION_DIAL );
					intent.setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK );
					startActivity ( intent );
					hideList ( );
					workQueue.addTextToQueue ( "here is the dialer" );
				}


			} );

		massagingBtn.setOnClickListener ( new View.OnClickListener ( )
			{

				@Override
				public void onClick ( View p1 )
				{
					Intent eventIntentMessage =getPackageManager ( )
						.getLaunchIntentForPackage ( Telephony.Sms.getDefaultSmsPackage ( getApplicationContext ( ) ) );
					startActivity ( eventIntentMessage );
					hideList ( );
					workQueue.addTextToQueue ( "here is you're massages" );
				}


			} );

		findMeBtn.setOnClickListener ( new View.OnClickListener ( )
			{

				@Override
				public void onClick ( View p1 )
				{
					workQueue.addWork ( System.currentTimeMillis ( ), Work.WorkName.FIND_ME );

				}


			} );
	}*/

	public void showSaveEmotionPage ( )
	{
		isSaveEmotionPageVisible = true;
		saveEmothionBase = (ViewGroup) mInflater.inflate ( R.layout.sara_on_screen_save_ali_emotion , null );
		saveEmotionFrame = saveEmothionBase.findViewById ( R.id.select_emotion_frame );
		aliEmotionView = ( AliEmotionCircle ) saveEmothionBase.findViewById( R.id.select_emotion_view );
		initSaveEmotionButtons();
		mWindowManager.addView ( saveEmothionBase, pageParams );
		Animate.slideUp ( saveEmotionFrame, utils.dpToTx ( 180 ), 500, new EaseCircularOutInterpolator ( ), null );
	}

	public void closeSaveEmotionPage ( )
	{
		hold = true;
		isSaveEmotionPageVisible = false;
		Animate.slideDown ( saveEmotionFrame, utils.dpToTx ( 180 ), 500, new EaseCircularOutInterpolator ( ), new Animator.AnimatorListener ( )
		{

			@Override
			public void onAnimationStart ( Animator p1 )
			{
				// TODO: Implement this method
			}

			@Override
			public void onAnimationEnd ( Animator p1 )
			{
				aliEmotionView = null;
				mWindowManager.removeView ( saveEmothionBase );
				saveEmothionBase = null;
				hold = false;
			}

			@Override
			public void onAnimationCancel ( Animator p1 )
			{
				// TODO: Implement this method
			}

			@Override
			public void onAnimationRepeat ( Animator p1 )
			{
				// TODO: Implement this method
			}


		} );
	}

	public void showList ( )
	{
		if ( isPageVisible ( ) )
			return;
		isListVisible = true;
		mWindowManager.addView ( listBase, pageParams );
		Animate.slideUp ( listFrame, utils.dpToTx ( 180 ), 500, new EaseCircularOutInterpolator ( ), null );
	}

	public void hideList ( )
	{
		hold = true;
		isListVisible = false;
		Animate.slideDown ( listFrame, utils.dpToTx ( 180 ), 500, new EaseCircularOutInterpolator ( ), new Animator.AnimatorListener ( )
		{

			@Override
			public void onAnimationStart ( Animator p1 )
			{
				// TODO: Implement this method
			}

			@Override
			public void onAnimationEnd ( Animator p1 )
			{
				if (listBase.isAttachedToWindow())
					mWindowManager.removeView ( listBase );
				hold = false;
			}

			@Override
			public void onAnimationCancel ( Animator p1 )
			{
				// TODO: Implement this method
			}

			@Override
			public void onAnimationRepeat ( Animator p1 )
			{
				// TODO: Implement this method
			}


		} );
	}

	public void showSaraBase ( )
	{
		if ( isSaraVisible )
			return;
		isSaraVisible = true;
		mWindowManager.removeView ( slideArea );
		mWindowManager.addView ( saraBase, saraBaseParams );
		saraFrame.setVisibility ( View.VISIBLE );
		Animate.slideUp ( saraFrame, utils.dpToTx ( 36 ), 400, new EaseCircularOutInterpolator ( ), null );
		say ( "Hi Ali" );
	}

	public void hideSaraBase () {
		if (!isSaraVisible | isPageVisible ())
			return;
		isSaraVisible = false;
		Animate.slideDown ( saraFrame, utils.dpToTx (36), 400, new EaseCubicInOutInterpolator (), new Animator.AnimatorListener () {

			@Override
			public void onAnimationStart (Animator p1) {}

			@Override
			public void onAnimationEnd ( Animator p1 ) {
				if (!slideArea.isAttachedToWindow())
					mWindowManager.addView(slideArea, slideAreaParams);
				if (saraBase.isAttachedToWindow())
					mWindowManager.removeView(saraBase);
			}

			@Override
			public void onAnimationCancel ( Animator p1 ) {}

			@Override
			public void onAnimationRepeat ( Animator p1 ) {}
		});

	}

	public void say ( final String text )
	{
		sara.post ( new Runnable ( )
		{
			@Override
			public void run ( )
			{
				sara.say ( text );
			}
		} );
	}

	private boolean pagesClosed ( )
	{
		if ( isListVisible )
		{
			hideList ( );
			return true;
		}


		if ( isSaveEmotionPageVisible )
		{
			closeSaveEmotionPage ( );
			return true;
		}

		return false;
	}

	public boolean isPageVisible ( )
	{
		if ( isListVisible )
			return true;
		if ( isSaveEmotionPageVisible )
			return true;
		return false;
	}

	private void createListButtons ( )
	{
		List<DiamondButton> buttons = new ButtonsList ( SaraViewService.this ).getButtonsList();
		LinearLayout table = ( LinearLayout ) listBase.findViewById( R.id.list_table );
		int counter = 0;
		LinearLayout column = new LinearLayout ( getApplicationContext() );
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams ( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT );
		column.setLayoutParams( params );
		column.setOrientation( LinearLayout.VERTICAL );
		for ( int i=0; i<buttons.size(); i++ )
		{
			if ( counter == 5 )
			{
				table.addView( column );
				column = new LinearLayout ( getApplicationContext() );
				LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams ( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT );
				params2.setMargins((int) utils.convertDpToPixel(8), 0, 0, 0);
				column.setOrientation( LinearLayout.VERTICAL );
				column.setLayoutParams( params2 );
				counter = 0;
			}
			if ( i == buttons.size() -1 & counter != 5 )
			{
				column.addView( buttons.get( i ) );
				table.addView( column );
				break;
			}
			column.addView( buttons.get( i ) );
			counter ++;
		}

		//row.addView(
	}

	public interface OnEmotionSaved
	{
		public void onEmotionSaved ( int emotion );
	}

	public WorkQueue getWorkQueueService ( )
	{
		return workQueue;
	}

}
