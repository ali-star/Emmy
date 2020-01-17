package alistar.app.ui;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;

public class CircleBackground extends View
{
	private Paint paint = new Paint();

	public CircleBackground(Context context)
	{
		super(context);
		init();
	}

	public CircleBackground(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	private void init()
	{
		setLayerType(LAYER_TYPE_SOFTWARE, null);
		//CornerPathEffect cpe = new CornerPathEffect(7);
		//paint.setPathEffect(cpe);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.parseColor("#263238"));
		paint.setShadowLayer(5, 0, 3, Color.parseColor("#40000000"));
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		// TODO: Implement this method
		super.onDraw(canvas);
		canvas.drawCircle(getWidth()/2, getHeight()/2, (getHeight()/2) - 7, paint);
	}
}
