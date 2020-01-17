package alistar.app.ui;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import android.animation.*;

public class Diamond extends View
{
	//LayoutInflater mInflater;
	private int width, height, widthCenter, heightCenter;
	private Paint paint = new Paint();
	private Path path = new Path();
	private int padding = 3;

    public Diamond(Context context) {
        super(context);
		//mInflater = LayoutInflater.from(context);
		init(); 

    }
    public Diamond(Context context, AttributeSet attrs, int defStyle)
    {
		super(context, attrs, defStyle);
		//mInflater = LayoutInflater.from(context);
		init(); 
    }
    public Diamond(Context context, AttributeSet attrs) {
		super(context, attrs);
		//mInflater = LayoutInflater.from(context);
		init(); 
    }
	public void init()
	{
		setWillNotDraw(false);
		CornerPathEffect cpe = new CornerPathEffect(2);
		paint.setAntiAlias(true);
		paint.setColor(Color.parseColor("#48DDBF"));
		paint.setPathEffect(cpe);
		//paint.setsh;
		/*View v = mInflater.inflate(R.layout.custom_view, this, true);
		 TextView tv = (TextView) v.findViewById(R.id.textView1);
		 tv.setText(" Custom RelativeLayout");*/
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		// TODO: Implement this method
		super.onDraw(canvas);
		width = getWidth();
		height = getHeight();
		widthCenter = width / 2;
		heightCenter = height / 2;
		//RectF rectf = new RectF(0, 0, getWidth(), getHeight());
		path.moveTo(widthCenter, padding);
		path.lineTo(width - padding, heightCenter);
		path.lineTo(widthCenter, height - padding);
		path.lineTo(padding, heightCenter);
		path.lineTo(widthCenter, padding);
		path.close();
		canvas.drawPath(path, paint);
		// Animates view changing x, y along path co-ordinates
		/*ValueAnimator pathAnimator = ObjectAnimator.ofFloat(Diamond.this, "x", "y", path);
		pathAnimator.setDuration(2000);
		pathAnimator.start();*/
		//canvas.drawRoundRect(rectf,5,5,paint);
	}

}
