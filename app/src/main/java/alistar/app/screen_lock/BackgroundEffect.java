package alistar.app.screen_lock;
import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Paint;
import android.graphics.Canvas;
import alistar.app.Utils;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.RadialGradient;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

public class BackgroundEffect extends View
{

	private Paint circle1Paint;
	private int centerX, centerY;
	private Utils utils;
	private Context context;
	private ValueAnimator va;

	public BackgroundEffect(Context context)
	{
		super(context);
		this.context = context;
		init();
	}

	public BackgroundEffect(Context context,AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
		init();
	}

	public void setWaitingMode(boolean p0)
	{
		if (p0)
		{
			if (va == null)
			{
				va = new ValueAnimator();
				va.setIntValues(new int[] {0, 100});
				va.setRepeatMode(Animation.REVERSE);
				va.setInterpolator(new LinearInterpolator());
				va.setDuration(450);
				va.setRepeatCount(20);
				va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
					{

						@Override
						public void onAnimationUpdate(ValueAnimator p1)
						{
							int value = (int) va.getAnimatedValue();
							circle1Paint.setAlpha(value);
							invalidate();
						}


					});
				va.addListener(new Animator.AnimatorListener()
					{

						@Override
						public void onAnimationStart(Animator p1)
						{
							// TODO: Implement this method
						}

						@Override
						public void onAnimationEnd(Animator p1)
						{
							// TODO: Implement this method
							//va.reverse();
						}

						@Override
						public void onAnimationCancel(Animator p1)
						{
							// TODO: Implement this method
						}

						@Override
						public void onAnimationRepeat(Animator p1)
						{
							// TODO: Implement this method
						}


					});
				va.start();
			}
		}else
		{
			if (va != null)
			{
				va.cancel();
				va = null;
			}
		}
	}

	private void init()
	{
		utils = Utils.getInstance(context);

		circle1Paint = new Paint();
		circle1Paint.setStyle(Paint.Style.FILL);
		circle1Paint.setAntiAlias(true);
		circle1Paint.setStrokeWidth(utils.dpToTx(.5f));
		circle1Paint.setColor(Color.parseColor("#ffffff"));
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		Shader gradient = new RadialGradient(centerX, centerY, getWidth() / 2
											 , new int[] {Color.TRANSPARENT, Color.TRANSPARENT, Color.parseColor("#996994FD"), Color.TRANSPARENT}
											 , new float[] {0f, 0.09f, 0.09f, 0.8f}
											 , Shader.TileMode.CLAMP);
		circle1Paint.setShader(gradient);
		//canvas.drawCircle(centerX, centerY, utils.dpToTx(100), circle1Paint);
	}

	public void setCenter(int x,int y)
	{
		centerX = x;
		centerY = y;

	}

	public void disconnect()
	{
		if(va != null)
		{
			va.cancel();
			va = null;
		}
	}

}
