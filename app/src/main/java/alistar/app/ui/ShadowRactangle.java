package alistar.app.ui;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import android.widget.*;

public class ShadowRactangle extends RelativeLayout
{
	private Paint paint = new Paint();

	public ShadowRactangle(Context context)
	{
		super(context);
		init();
	}

	public ShadowRactangle(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	private void init()
	{
		setWillNotDraw(false);
		setLayerType(LAYER_TYPE_SOFTWARE, null);
		//CornerPathEffect cpe = new CornerPathEffect(7);
		//paint.setPathEffect(cpe);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.parseColor("#242536"));
		paint.setShadowLayer(15, 0, 15, Color.parseColor("#70000000"));
		setPadding(15, 0, 15, 40);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		// TODO: Implement this method
		super.onDraw(canvas);
		canvas.drawRect(15, 0, getWidth() - 15, getHeight() - 40, paint);
	}
}
