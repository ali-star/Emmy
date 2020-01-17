package alistar.app;
import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.View.*;
import android.view.*;
import com.readystatesoftware.notificationlog.*;
import android.content.*;
import alistar.app.utils.*;
import alistar.app.timeline.*;

public class AskFeelingActivity extends Activity
{
	
	private ImageView vUp, vDown;
	private TextView tvValue;
	private EmotionsSeekBar esb;
	private int value = 0;
	private BroadcastReceiver screenOffReceiver;
	private Utils utils;
	private boolean saveOnFinish = false;
	private EditText noteEt;
	private String emoji = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ask_feeling_activity);
		vUp = (ImageView) findViewById(R.id.up);
		vDown = (ImageView) findViewById(R.id.down);
		tvValue = (TextView) findViewById(R.id.value);
		esb = (EmotionsSeekBar) findViewById(R.id.emotionsSeekBar);
		noteEt = (EditText) findViewById(R.id.note_et);
		utils = Utils.getInstance(this);
		
		/*findViewById(R.id.save_base).setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					Utils.getInstance(AskFeelingActivity.this).saveAliFeeling(value, null);
					finish();
				}
				
			
		});*/
		
		vUp.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					if(value <3)
					{
						value ++;
						tvValue.setText(String.valueOf(value));
						if(value > 0)
							tvValue.setText("+"+String.valueOf(value));
						esb.setValue(value);
					}
				}
				
			
		});
		
		vDown.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					if(value > -3)
					{
						value --;
						tvValue.setText(String.valueOf(value));
						if(value > 0)
							tvValue.setText("+"+String.valueOf(value));
						esb.setValue(value);
					}
				}


			});
			
		new SwipeDetector(findViewById(R.id.save_base)).setOnSwipeListener(new SwipeDetector.onSwipeEvent() {
				@Override
				public void SwipeEventDetected(View v, SwipeDetector.SwipeTypeEnum swipeType)
				{
					if (swipeType == SwipeDetector.SwipeTypeEnum.BOTTOM_TO_TOP)
					{
						//swipeUp();
						Utils.getInstance(AskFeelingActivity.this).saveAliFeeling(value, null, null);
						finish();
						startActivity(new Intent(AskFeelingActivity.this, QuickTimelineNote.class));
					}
					if (swipeType == SwipeDetector.SwipeTypeEnum.TOP_TO_BOTTOM)
					{
						//swipeDown();
					}
					if (swipeType == SwipeDetector.SwipeTypeEnum.RIGHT_TO_LEFT)
					{
						//swipeLeft();
					}
					if (swipeType == SwipeDetector.SwipeTypeEnum.LEFT_TO_RIGHT)
					{
						//swipeRight();
					}
					if (swipeType == SwipeDetector.SwipeTypeEnum.CLICK)
					{
						//click();
						Utils.getInstance(AskFeelingActivity.this).saveAliFeeling(value, null, null);
						finish();
					}

				}
			});
			
		IntentFilter screenStateFilter = new IntentFilter();
		screenStateFilter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(screenOffReceiver = new BroadcastReceiver()
						 {

							 @Override
							 public void onReceive(Context p1, Intent p2)
							 {
								 Utils.getInstance(AskFeelingActivity.this).saveAliFeeling(value, null, null);
								 finish();
							 }
							 
			
		}, screenStateFilter);
		
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this metho
		unregisterReceiver(screenOffReceiver);
		if(saveOnFinish)
		{
			Utils.getInstance(AskFeelingActivity.this).saveAliFeeling(value, noteEt.getText().toString().equals("") ? null : noteEt.getText().toString(), emoji == null ? null : emoji);
		}
		super.onDestroy();
	}
	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){

		if (keyCode == KeyEvent.KEYCODE_VOLUME_UP){
			if(value <3)
			{
				value ++;
				tvValue.setText(String.valueOf(value));
				if(value > 0)
					tvValue.setText("+"+String.valueOf(value));
				esb.setValue(value);
				if(value != 0)
				{
					utils.Vibrate(60);
				}
				else
				{
					utils.Vibrate(200);
				}
				
			}
			return true;
		}

		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
			if(value > -3)
			{
				value --;
				tvValue.setText(String.valueOf(value));
				if(value > 0)
					tvValue.setText("+"+String.valueOf(value));
				esb.setValue(value);
				if(value != 0)
				{
					utils.Vibrate(60);
				}
				else
				{
					utils.Vibrate(200);
				}
			}
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
}
