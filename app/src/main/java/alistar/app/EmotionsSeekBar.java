package alistar.app;
import android.view.*;
import android.content.*;
import android.util.*;
import android.graphics.*;

public class EmotionsSeekBar extends View
{
	
	private Paint whiteLinePaint = new Paint();
	private Paint greenLinePaint = new Paint();
	private Paint redLinePaint = new Paint();
	private Paint circlePaint = new Paint();
	private Paint trainglePaint = new Paint();
	private float halfHeight = 0;
	private float halfWidth = 0;
	private float width = 0;
	private float height = 0;
	private float stepWidth = 0;
	private float smallCircleSize = 10;
	private float bigCircleSize = 20;
	private float leftPadding = 20;
	private float rightPadding = 20;
	private float smallLineHeight = 10;
	private float step = 3;
	
	public EmotionsSeekBar(Context context)
	{
		super(context);
		init();
	}

	public EmotionsSeekBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
		init();
    }
	
	protected void init()
	{
		setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		
		whiteLinePaint.setStyle(Paint.Style.STROKE);
		whiteLinePaint.setStrokeWidth(2);
		whiteLinePaint.setColor(Color.parseColor("#D8DAE8"));
		
		greenLinePaint.setStyle(Paint.Style.STROKE);
		greenLinePaint.setStrokeWidth(2);
		greenLinePaint.setColor(Color.parseColor("#69FFDE"));
		greenLinePaint.setShadowLayer(5, 0, 0, Color.parseColor("#5069FFDE"));
		
		redLinePaint.setStyle(Paint.Style.STROKE);
		redLinePaint.setStrokeWidth(2);
		redLinePaint.setColor(Color.parseColor("#FF6C94"));
		redLinePaint.setShadowLayer(5, 0, 0, Color.parseColor("#50FF6C94"));
		
		trainglePaint.setStyle(Paint.Style.FILL);
		trainglePaint.setAntiAlias(true);
		trainglePaint.setColor(Color.parseColor("#FFD869"));
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		// TODO: Implement this method
		super.onDraw(canvas);
		//init values
		width = getWidth();
		height = getHeight();
		halfHeight = height / 2;
		halfWidth = width / 2;
		leftPadding = getPaddingLeft();
		rightPadding = getPaddingRight();
		stepWidth = (width / 6) - (rightPadding/2) + (whiteLinePaint.getStrokeWidth()/2);
		
		//draw lines
		//canvas.drawLine(leftPadding, halfHeight, width-rightPadding-(whiteLinePaint.getStrokeWidth()/2), halfHeight, whiteLinePaint);
		canvas.drawLine(leftPadding, halfHeight, halfWidth-(smallLineHeight*2), halfHeight, redLinePaint);
		canvas.drawLine(halfWidth+(smallLineHeight*2), halfHeight, width-rightPadding-(whiteLinePaint.getStrokeWidth()/2), halfHeight, greenLinePaint);
		canvas.drawLine(halfWidth-(smallLineHeight*2), halfHeight, halfWidth+(smallLineHeight*2), halfHeight, whiteLinePaint);
		//draw step lines
		for(int i=0; i<7; i++)
		{
			if(i>3)
			{
				canvas.drawLine((i*stepWidth) + leftPadding + (greenLinePaint.getStrokeWidth()/2), halfHeight, (i*stepWidth)+leftPadding+(greenLinePaint.getStrokeWidth()/2), halfHeight-smallLineHeight, greenLinePaint);
			}
			else if(i<3)
			{
				canvas.drawLine((i*stepWidth) + leftPadding + (redLinePaint.getStrokeWidth()/2), halfHeight, (i*stepWidth)+leftPadding+(redLinePaint.getStrokeWidth()/2), halfHeight-smallLineHeight, redLinePaint);
			}
			else
			{
				canvas.drawLine((i*stepWidth) + leftPadding + (whiteLinePaint.getStrokeWidth()/2), halfHeight, (i*stepWidth)+leftPadding+(whiteLinePaint.getStrokeWidth()/2), halfHeight-smallLineHeight, whiteLinePaint);
			}
		}
		//draw triangle
		Point p1 = new Point((int) ((step * stepWidth) + leftPadding + (whiteLinePaint.getStrokeWidth())) - 10, (int) (halfHeight-30));
		Point p2 = new Point((int) ((step * stepWidth) + + leftPadding + (whiteLinePaint.getStrokeWidth())) + 10, (int) (halfHeight-30));
		Point p3 = new Point((int) ((step * stepWidth) + leftPadding + (whiteLinePaint.getStrokeWidth())) , (int) (halfHeight-20));
		Path path = new Path();
		path.setFillType(Path.FillType.EVEN_ODD);
		path.moveTo(p1.x, p1.y);
		path.lineTo(p2.x, p2.y);
		path.lineTo(p3.x, p3.y);
		path.lineTo(p1.x, p1.y);

		path.close();
		
		canvas.drawPath(path, trainglePaint);
		
	}
	
	public void setValue(int v)
	{
		step = 3 + v;
		invalidate();
	}
	
	
}
