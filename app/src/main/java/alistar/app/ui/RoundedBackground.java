package alistar.app.ui;

import alistar.app.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.util.*;
import android.view.*;

public class RoundedBackground extends View
{

	private Paint paint = new Paint();
	private Path path = new Path();
	private int color = Color.parseColor("#303537");

	public RoundedBackground(Context context)
	{
		super(context);
		init();
	}

	public RoundedBackground(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.coloredImageView);
		color = attributes.getColor(R.styleable.coloredImageView_setColor, Color.parseColor("#303537"));
		attributes.recycle();
		init();
	}
	
	public RoundedBackground(Context context, AttributeSet attrs, int defStyle)
	{
        super(context, attrs, defStyle);
		TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.coloredImageView);
		color = attributes.getColor(R.styleable.coloredImageView_setColor, Color.parseColor("#303537"));
		attributes.recycle();
		init();
	}

	private void init()
	{
		setLayerType(LAYER_TYPE_SOFTWARE, null);
		CornerPathEffect cpe = new CornerPathEffect(7);
		paint.setPathEffect(cpe);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(color);
		paint.setShadowLayer(5, 0, 3, Color.parseColor("#40000000"));
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		// TODO: Implement this method
		super.onDraw(canvas);
		path.moveTo(15, 15);
		path.lineTo(getWidth() - 15, 15);
		path.lineTo(getWidth() - 15, getHeight() - 15);
		path.lineTo(15, getHeight() - 15);
		path.lineTo(15, 15);
		path.close();
		canvas.drawPath(path, paint);
	}

}
