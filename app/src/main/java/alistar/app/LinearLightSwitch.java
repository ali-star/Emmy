package alistar.app;
import android.view.View;
import android.graphics.*;
import android.content.Context;
import android.util.*;

public class LinearLightSwitch extends View
{

	private Paint redPaint = new Paint();
	private Paint yellowPaint = new Paint();
	private Paint greenPaint = new Paint();
	private Paint darkPaint = new Paint();
	public static int SITUATION_GOOD = 2;
	public static int SITUATION_ALART = 1;
	public static int SITUATION_BAD = 0;
	private int situation = -1;
	private boolean isOn = true;
	private SwitchListenear switchListenear;

	public LinearLightSwitch(Context context)
	{
		super(context);
		init();
	}

	public LinearLightSwitch(Context context, AttributeSet attrs)
    {
        super(context, attrs);
		init();
    }

	protected void init()
	{
		setLayerType(View.LAYER_TYPE_SOFTWARE, null);

		greenPaint.setColor(Color.parseColor("#69FFDE"));
		greenPaint.setShadowLayer(5, 0, 0, Color.parseColor("#5069FFDE"));
		greenPaint.setAntiAlias(true);
		greenPaint.setStrokeWidth(2);

		yellowPaint.setColor(Color.parseColor("#FFD869"));
		yellowPaint.setShadowLayer(5, 0, 0, Color.parseColor("#50FFD869"));
		yellowPaint.setAntiAlias(true);
		yellowPaint.setStrokeWidth(2);

		redPaint.setColor(Color.parseColor("#FF6C94"));
		redPaint.setShadowLayer(5, 0, 0, Color.parseColor("#50FF6C94"));
		redPaint.setAntiAlias(true);
		redPaint.setStrokeWidth(2);

		darkPaint.setColor(Color.parseColor("#1D1F2E"));
		darkPaint.setAntiAlias(true);
		darkPaint.setStrokeWidth(2);
		
		setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if(isOn)
				{
					isOn = false;
					if(switchListenear != null)
						switchListenear.onSwitched(false);
					invalidate();
				}
				else
				{
					isOn = true;
					if(switchListenear != null)
						switchListenear.onSwitched(true);
					invalidate();
				}
			}
		});
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		// TODO: Implement this method
		super.onDraw(canvas);
		int halfHeight = getHeight() / 2;
		if(isOn)
		{
			canvas.drawLine(10, halfHeight / 2, getWidth() - 10, halfHeight / 2, greenPaint);
			canvas.drawLine(10, getHeight() - halfHeight, getWidth() - 10, getHeight() - halfHeight, darkPaint);
		}
		else
		{
			canvas.drawLine(10, getHeight() - halfHeight, getWidth() - 10, getHeight() - halfHeight, redPaint);
			canvas.drawLine(10, halfHeight / 2, getWidth() - 10, halfHeight / 2, darkPaint);
		}
		
		
		/*switch(situation){
			case 2:
				canvas.drawLine(10, getHeight() / 2, getWidth() - 10, getHeight() / 2, greenPaint);
				break;
			case 1:
				canvas.drawLine(10, getHeight() / 2, getWidth() - 10, getHeight() / 2, yellowPaint);
				break;
			case 0:
				canvas.drawLine(10, getHeight() / 2, getWidth() - 10, getHeight() / 2, redPaint);
			    break;
			case -1:
				canvas.drawLine(10, getHeight() / 2, getWidth() - 10, getHeight() / 2, darkPaint);
				break;
		}*/
	}

	public void setSituation(int situation)
	{
		this.situation = situation;
		invalidate();
	}
	
	public interface SwitchListenear{
		void onSwitched(boolean switched);
	}
	
	public void setOnSwitchListenear(SwitchListenear sl)
	{
		switchListenear = sl;
	}
	
	public void setOn(boolean on)
	{
		isOn = on;
		invalidate();
	}

}
