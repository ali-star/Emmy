package alistar.app.ui;
import alistar.app.*;
import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.graphics.*;

public class BubbleLayout extends RelativeLayout
{
	//LayoutInflater mInflater;
	private int width, height, widthCenter, heightCenter;
	private Paint paint = new Paint();
	private Path path = new Path();
	
    public BubbleLayout(Context context) {
        super(context);
		//mInflater = LayoutInflater.from(context);
		init(); 

    }
    public BubbleLayout(Context context, AttributeSet attrs, int defStyle)
    {
		super(context, attrs, defStyle);
		//mInflater = LayoutInflater.from(context);
		init(); 
    }
    public BubbleLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		//mInflater = LayoutInflater.from(context);
		init(); 
    }
	public void init()
	{
		setWillNotDraw(false);
		setPadding(15,0,15,0);
		CornerPathEffect cpe = new CornerPathEffect(7);
		paint.setAntiAlias(true);
		paint.setColor(Color.parseColor("#D8DAE8"));
		paint.setPathEffect(cpe);
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
		path = new Path();
		path.moveTo(0, heightCenter);
		path.lineTo(10, heightCenter - 10);
		path.lineTo(10, 0);
		path.lineTo(width, 0);
		path.lineTo(width, height);
		path.lineTo(10, height);
		path.lineTo(10, heightCenter + 10);
		path.lineTo(0, heightCenter);
		path.close();
		canvas.drawPath(path, paint);
		//canvas.drawRoundRect(rectf,5,5,paint);
	}
	
}
