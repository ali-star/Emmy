package alistar.app;
import android.app.*;
import android.os.*;
import android.widget.*;
import android.graphics.*;
import android.view.*;
import com.crystal.crystalrangeseekbar.widgets.*;
import com.crystal.crystalrangeseekbar.interfaces.*;
import java.util.*;
import com.readystatesoftware.notificationlog.*;

public class SelectTimeDialog extends Dialog
{

	private Activity a;
	private CrystalSeekbar hourSeekbar, minuteSeekbar;
	public TextView timeText, ok, cancel;;
	private int minute, hour;

	public SelectTimeDialog(Activity activity){
		super(activity);
		a = activity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.select_time_dialog);
		timeText = (TextView) findViewById(R.id.time_text);
		ok = (TextView) findViewById(R.id.ok_btn);
		cancel = (TextView) findViewById(R.id.cancel_btn);
		hourSeekbar = (CrystalSeekbar) findViewById(R.id.hour_seekbar);
		minuteSeekbar = (CrystalSeekbar) findViewById(R.id.minute_seekbar);
		
		cancel.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					SelectTimeDialog.this.dismiss();
				}
				
			
		});
		
		hourSeekbar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {

				@Override
				public void valueChanged(Number p1)
				{
					// TODO: Implement this metho
					hour = p1.intValue();
					setTimeText();
				}

			});
			
		minuteSeekbar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {

				@Override
				public void valueChanged(Number p1)
				{
					// TODO: Implement this metho
					minute = p1.intValue();
					setTimeText();
				}

			});
			
		
	}
	
	private void setTimeText()
	{
		timeText.setText(String.format("%02d", hour) + ":" + String.format("%02d", minute));
	}
	
	public long getTimeMillis()
	{
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		if(c.getTimeInMillis() < System.currentTimeMillis())
		{
			Log.d("sara", Utils.getInstance(getContext()).convertLongToTimeString(c.getTimeInMillis() + Utils.DAY));
			return c.getTimeInMillis() + Utils.DAY;
		}
		Log.d("sara", Utils.getInstance(getContext()).convertLongToTimeString(c.getTimeInMillis()));
		return c.getTimeInMillis();
	}
	
	public void setTime(long l)
	{
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(l);
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);
		setTimeText();
		hourSeekbar.setMinStartValue(hour).apply();
		minuteSeekbar.setMinStartValue(minute).apply();
	}

}
