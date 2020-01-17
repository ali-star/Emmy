package alistar.app.ui;
import android.content.*;
import android.view.*;
import android.util.*;
import android.graphics.*;
import alistar.app.*;

public class SaraListBackground extends View
{
	
	private Paint paint = new Paint();
	private Path path = new Path();
	private int width, height, widthCenter, heightCenter;
	private int viewWidth, viewHeight;
	private Utils utils;
	
	public SaraListBackground(Context context)
	{
		super(context);
		init();
	}
	
	public SaraListBackground(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}
	
	protected void init()
	{
		utils = Utils.getInstance(getContext());
		setWillNotDraw(false);
		CornerPathEffect cpe = new CornerPathEffect(7);
		paint.setPathEffect(cpe);
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);
		paint.setColor(Color.parseColor("#1D1F2E"));
		viewWidth = (int) utils.convertDpToPixel(58);
		viewHeight = (int) utils.convertDpToPixel(58);
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
		//draw path
		Point p1 = new Point(0, 0);
		Point p2 = new Point(width, 0);
		Point p3 = new Point(width, height - (viewHeight / 2));
		Point p4 = new Point(viewWidth, height - (viewHeight / 2));
		Point p5 = new Point(viewWidth, height);
		Point p6 = new Point(0, height);
		Point p7 = new Point(0, 0);
		path.moveTo(p1.x, p1.y);
		path.lineTo(p2.x, p2.y);
		path.lineTo(p3.x, p3.y);
		path.lineTo(p4.x, p4.y);
		path.lineTo(p5.x, p5.y);
		/*
		int x1 = p5.x;
		int x2 = p6.x;
		int y1 = p5.y;
		int y2 = p6.y;
		final Path path = new Path();
		int midX            = x1 + ((x2 - x1) / 2);
		int midY            = y1 + ((y2 - y1) / 2);
		float xDiff         = midX - x1;
		float yDiff         = midY - y1;
		double angle        = (Math.atan2(yDiff, xDiff) * (180 / Math.PI)) - 90;
		double angleRadians = Math.toRadians(angle);
		float pointX        = (float) (midX + 90 * Math.cos(angleRadians));
		float pointY        = (float) (midY + 90 * Math.sin(angleRadians));
		
		path.cubicTo(x1,y1,pointX, pointY, x2, y2);
		*/
		path.moveTo(p5.x, p5.y);
		//final float x2 = (p6.x + p5.x) / 3;
		final float y2 = ((p6.y + p5.y) + 130) / 3;
		path.quadTo(viewWidth / 2, y2, p6.x, p6.y);
		//path.lineTo(p6.x, p6.y);
		path.lineTo(p7.x, p7.y);
		path.close();
		canvas.drawPath(path, paint);
	}
	
	public void setViewWH(int w, int h)
	{
		viewWidth = w;
		viewHeight = h;
		invalidate();
	}
	
	
}
