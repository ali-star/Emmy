package alistar.app;
import android.view.*;
import android.content.*;
import android.util.AttributeSet;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.*;

public class MainCircleView extends View
{

	private Paint circlePaint = new Paint();
	private RectF rectF = new RectF();
	private int progress = 100;
	private int max = 100;

	public MainCircleView(Context context)
	{
		super(context);
	}

	public MainCircleView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	protected void init()
	{
		circlePaint.setStrokeWidth(20);
		circlePaint.setAntiAlias(true);
		circlePaint.setStyle(Paint.Style.STROKE);
		Shader shader = new LinearGradient(0, getHeight() / 2, getWidth(), getHeight() / 2, Color.parseColor("#A56AF9"), Color.parseColor("#FF6C97"), Shader.TileMode.CLAMP);
		circlePaint.setShader(shader);
		//circlePaint.setColor(Color.parseColor("#FFFFFF"));
		/*circlePaint.setShader(new LinearGradient(0, 10, 10, 0,
												 new int[]{Color.BLACK, Color.BLACK, Color.BLUE, Color.BLUE},
												 new float[]{0,0.5f,.55f,1}, Shader.TileMode.CLAMP));*/
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		// TODO: Implement this method
		rectF.set(20, 20, MeasureSpec.getSize(widthMeasureSpec) - 20, MeasureSpec.getSize(heightMeasureSpec) - 20);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	public void setMax(int max)
	{
		this.max = max;
	}
	
	public int getMax() {
        return max;
    }
	
	public int getProgress()
	{
		return progress;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		// TODO: Implement this method
		super.onDraw(canvas);
		init();
		float yHeight = (getProgress() / (float) getMax() * getHeight());
        float radius = (getWidth() / 2f);
        float angle = (float) (Math.acos((radius - yHeight) / radius) * 180 / Math.PI);
        float startAngle = 90 + angle;
        float sweepAngle = 360 - angle * 2;
		canvas.drawArc(rectF, 270 - angle, angle * 2, false, circlePaint);
		//canvas.drawCircle(getWidth() / 2, getHeight() / 2, (getWidth() / 2) - 20, circlePaint);
	}

}
