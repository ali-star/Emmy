package alistar.app.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Canvas;

public class CircularBattryLevel extends View
{

	private Paint circlePaintFull = new Paint();
	private Paint circlePaintLow = new Paint();
	private int width, height, widthCenter, heightCenter, circleRadius = 3;
	private int level = 0;

	public CircularBattryLevel(Context context)
	{
		super(context);
		init();
	}

	public CircularBattryLevel(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	private void init()
	{
		circlePaintFull.setAntiAlias(true);
		circlePaintFull.setStyle(Paint.Style.FILL);
		circlePaintFull.setColor(Color.parseColor("#FFFFFF"));

		circlePaintLow.setAntiAlias(true);
		circlePaintLow.setStyle(Paint.Style.FILL);
		circlePaintLow.setColor(Color.parseColor("#30FFFFFF"));
	}
	
	public void setLevel(int l)
	{
		level = l;
		invalidate();
	}

	@Override
	protected void onDraw ( Canvas canvas )
	{
		super.onDraw ( canvas );
		width = getWidth();
		height = getHeight();
		widthCenter = width / 2;
		heightCenter = height / 2;

		for(int i=0; i<10; i++)
		{
			if((100-(level+1))>=(i*10))
			{
				canvas.drawCircle(getWidth()/2, ((circleRadius*3)*i) + 12, circleRadius, circlePaintLow);
			}
			else
			{
				canvas.drawCircle(getWidth()/2, ((circleRadius*3)*i) + 12, circleRadius, circlePaintFull);
			}

		}
	}

}
