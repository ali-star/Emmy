package alistar.app.alarm;
import android.app.*;
import android.os.*;
import android.widget.*;
import alistar.app.*;
import android.view.View.*;
import android.view.*;
import android.util.*;
import static com.nineoldandroids.view.ViewPropertyAnimator.animate;
import android.app.admin.*;

public class AlarmActivity extends Activity
{
	
	private TextView xTv,yTv;
	private int firstX, firstY, x, y, screenWidth, screenHeight;
	private boolean firstTouch = true;
	private int distance = 0;
	private int percent = 0;
	private int viewHeight = 0;
	private LinearLayout touchView;
	private View touchCircle;
	private SaraView sara;
	private DevicePolicyManager devicePolicyManager;
	private Vibrator vibrator;
	private TextView typeText;
	private Alarm alarm;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
		unlockScreen();
		devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
		setContentView(R.layout.alarm_activity);
		xTv = (TextView) findViewById(R.id.x_tv);
		yTv = (TextView) findViewById(R.id.y_tv);
		touchView = (LinearLayout) findViewById(R.id.touch_view);
		sara = (SaraView) findViewById(R.id.sara);
		//touchCircle = findViewById(R.id.touch_circle);
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		typeText = (TextView) findViewById(R.id.type_text);
		
		DisplayMetrics metrics = this.getResources().getDisplayMetrics();
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		
		alarm = new Alarm(this);
		
		typeText.setText(alarm.getType());
		
		long[] pattern = { 0, 340, 1000, 340, 1000, 340, 1000, 340, 1000, 340, 1000};
		vibrator.vibrate(pattern, 0);
		
		sara.setOnThreeTapsListnear(new SaraView.OnThreeTapsListenear()
			{

				@Override
				public void onThreeTaps()
				{
					// TODO: Implement this method
					//Toast.makeText(AlarmActivity.this, "onThreeTaps", Toast.LENGTH_LONG).show();
					vibrator.cancel();
					vibrator = null;
					turnOffScreen();
					new Handler().postDelayed(new Runnable()
						{

							@Override
							public void run()
							{
								// TODO: Implement this method
								finish();
							}
							
						
					}, 500);
				}
				
			
		});
		
		/*touchCircle.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					touchView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
					viewHeight = touchView.getHeight(); //height is ready
					yTv.setText("viewHeight=" + viewHeight);
				}
			});
		
		touchCircle.setOnTouchListener(new OnTouchListener()
			{

				@Override
				public boolean onTouch(View p1, MotionEvent event)
				{
					// TODO: Implement this method
					if(firstTouch)
					{
						firstX = (int)event.getX();
						firstY = (int)event.getY();
						firstTouch = false;
					}
					
					x = (int)event.getX();
					y = (int)event.getY();
					distance = y - firstY;
					if(distance < 0)
					{
						percent = -distance / 2;
					}
					else
					{
						percent = distance / 2;
					}
					if(percent > 100)
					{
						percent = 100;
					}
					xTv.setText("x=" + x + " y=" + y);
					yTv.setText("distance=" + distance + " percent=" + percent);
					
					if(event.getAction() == MotionEvent.ACTION_UP)
					{
						firstTouch = true;
						if(percent >= 100)
						{
							animate(touchCircle).scaleX(0).scaleY(0).setDuration(240).start();
						}
						else
						{
							touchCircle.getLayoutParams().width = percent;
							touchCircle.getLayoutParams().height = percent;
							touchCircle.requestLayout();
						}
						
					}
					
					
					touchCircle.getLayoutParams().width = (viewHeight /100) * percent;
                    touchCircle.getLayoutParams().height = (viewHeight /100) * percent;
                    touchCircle.requestLayout();
					
					return true;
				}
				
		});*/
	}
	
	private PowerManager mPowerManager;
	private PowerManager.WakeLock mWakeLock;

	private void unlockScreen() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

	//Suppress lint error for PROXIMITY_SCREEN_OFF_WAKE_LOCK
	public void turnOffScreen(){
		// turn off screen
		Log.v("ProximityActivity", "OFF!");
		devicePolicyManager.lockNow();
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		if(vibrator != null)
			vibrator.cancel();
		super.onDestroy();
	}
	
	
	
}
