package alistar.app.ui;

import android.view.*;
import android.content.*;
import android.util.*;
import android.graphics.*;
import android.widget.*;
import android.animation.*;
import android.view.animation.*;
import alistar.app.*;

public class AliEmotionCircle extends View
{

	private Paint dashedCirclePaint = new Paint ( );
	private Paint seekBarPaint = new Paint ( );
	private Paint trianglePaint = new Paint ( );
	private Paint seekBarLinePaint = new Paint ( );
	private Paint leftTrianglePaint = new Paint ( );
	private Paint rightTrianglePaint = new Paint ( );
	private Paint textPaint = new Paint ( );
	private Utils utils;
	private int width, height, dashedCircleRedius;
	private SweepGradient seekBarGradient;
	private int redColor = Color.parseColor ( "#FF6C94" );
	private int whiteColor = Color.parseColor ( "#D8DAE8" );
	private int greenColor = Color.parseColor ( "#69FFDE" );
	private DashPathEffect circleDashEffect;
	private float time = 0;
	private int emotionValue = 0;
	private Path seekbarTrianglePath = new Path ( );
	private Point seekbarTrianglePoint;
	private Path leftTrianglePath = new Path ( );
	private Point leftTrianglePoint;
	private Path rightTrianglePath = new Path ( );
	private Point rightTrianglePoint;
	private float animatedValue = 90 + ( emotionValue  * ( 180 / 6 ) );
	private Paint emotionCirclePaint = new Paint ( );
	private int emotionCircleColor = whiteColor;
	private OnEmotionSelected emotionSelectedListener;

	public AliEmotionCircle ( Context context )
	{
		super ( context );
		init ( );
	}

	public AliEmotionCircle ( Context context, AttributeSet attrs )
	{
		super ( context, attrs );
		init ( );
	}

	private void init ( )
	{
		utils = Utils.getInstance( getContext() );

		dashedCircleRedius = utils.dpToTx ( 34 );
		circleDashEffect = new DashPathEffect ( new float[] {10f + emotionValue, 10f + emotionValue}, 1 );

		dashedCirclePaint.setAntiAlias ( true );
		dashedCirclePaint.setColor ( Color.WHITE );
		dashedCirclePaint.setStyle ( Paint.Style.STROKE );
		dashedCirclePaint.setStrokeWidth ( utils.dpToTx ( 1.5f ) );
		dashedCirclePaint.setPathEffect ( circleDashEffect );

		seekBarPaint.setAntiAlias ( true );
		seekBarPaint.setColor ( Color.WHITE );
		seekBarPaint.setStyle ( Paint.Style.STROKE );
		seekBarPaint.setStrokeWidth ( utils.dpToTx ( 2f ) );

		seekBarLinePaint.setAntiAlias ( true );
		seekBarLinePaint.setColor ( Color.parseColor ( "#FFFFFF" ) );
		seekBarLinePaint.setStyle ( Paint.Style.STROKE );
		seekBarLinePaint.setStrokeWidth ( utils.dpToTx ( 2f ) );

		trianglePaint.setAntiAlias ( true );
		trianglePaint.setStyle ( Paint.Style.FILL );
		trianglePaint.setColor ( Color.parseColor ( "#FFD869" ) );
		trianglePaint.setPathEffect ( new CornerPathEffect ( utils.dpToTx ( 4 ) ) );

		leftTrianglePaint.setAntiAlias ( true );
		leftTrianglePaint.setStyle ( Paint.Style.FILL );
		leftTrianglePaint.setColor ( redColor );
		leftTrianglePaint.setPathEffect ( new CornerPathEffect ( utils.dpToTx ( 4 ) ) );

		rightTrianglePaint.setAntiAlias ( true );
		rightTrianglePaint.setStyle ( Paint.Style.FILL );
		rightTrianglePaint.setColor ( greenColor );
		rightTrianglePaint.setPathEffect ( new CornerPathEffect ( utils.dpToTx ( 4 ) ) );

		textPaint.setAntiAlias ( true );
		textPaint.setStyle ( Paint.Style.FILL );
		textPaint.setColor ( Color.BLACK );
		textPaint.setTextSize ( utils.spToPx( 23 ) );
		textPaint.setTextAlign( Paint.Align.CENTER );

		emotionCirclePaint.setAntiAlias ( true );
		emotionCirclePaint.setStyle ( Paint.Style.FILL );
		emotionCirclePaint.setColor ( emotionCircleColor );
	}

	@Override
	protected void onDraw ( Canvas canvas )
	{
		super.onDraw ( canvas );

		time ++;

		width = getWidth ( );
		height = getHeight ( ) + utils.dpToTx( 36 );
		//seekBarGradient = new SweepGradient ( width / 2, height / 2, new int[] {Color.parseColor("#FF6C94"), Color.WHITE, Color.parseColor("#69FFDE")}, new float[] {.65f,.75f,.85f} );
		//seekBarPaint.setShader( seekBarGradient );
		//* dashed circle *//
		canvas.save ( );
		canvas.rotate ( time / 3, width / 2, height / 2 );
		canvas.drawCircle ( width / 2, height / 2, dashedCircleRedius, dashedCirclePaint );
		canvas.restore ( );
		//* value seekbar *//
		int x1 = ( width / 2 ) - dashedCircleRedius - utils.dpToTx ( 12 );
		int y1 = ( height / 2 ) - dashedCircleRedius - utils.dpToTx ( 12 );
		int x2 = ( width / 2 ) + dashedCircleRedius + utils.dpToTx ( 12 );
		int y2 = ( height / 2 ) + dashedCircleRedius + utils.dpToTx ( 12 );
		RectF rect = new RectF ( x1, y1, x2, y2 );
		seekBarPaint.setColor ( greenColor );
		canvas.drawArc ( rect, 0, -80, false, seekBarPaint );
		seekBarPaint.setColor ( whiteColor );
		canvas.drawArc ( rect, -80, -20, false, seekBarPaint );
		seekBarPaint.setColor ( redColor );
		canvas.drawArc ( rect, -100, -80, false, seekBarPaint );
		//* seekbar lines *//
		for ( int i=0; i < 11; i++ )
		{
			canvas.save ( );
			canvas.rotate ( i * 180 / 10, width / 2, height / 2 );
			if ( i > 5 )
				seekBarLinePaint.setColor ( greenColor );
			if ( i == 5 )
				seekBarLinePaint.setColor ( whiteColor );
			if ( i < 5 )
				seekBarLinePaint.setColor ( redColor );
			canvas.drawLine ( x1 - utils.dpToTx ( 6 ), height / 2, x1 + ( seekBarPaint.getStrokeWidth ( ) / 2 ), height / 2, seekBarLinePaint );
			//canvas.drawCircle( x1 - utils.dpToTx ( 6 ), height / 2, utils.dpToTx ( 1 ), seekBarLinePaint );
			canvas.restore ( );
		}
		//* seekbar triangle *//
		if ( seekbarTrianglePoint == null )
		{
			seekbarTrianglePoint = new Point ( x1 - utils.dpToTx ( 12 ), height / 2 );
			seekbarTrianglePath.moveTo ( seekbarTrianglePoint.x, seekbarTrianglePoint.y );
			seekbarTrianglePath.lineTo ( seekbarTrianglePoint.x - utils.dpToTx ( 6 ), seekbarTrianglePoint.y + utils.dpToTx ( 8 ) );
			seekbarTrianglePath.lineTo ( seekbarTrianglePoint.x - utils.dpToTx ( 6 ), seekbarTrianglePoint.y - utils.dpToTx ( 8 ) );
			seekbarTrianglePath.close ( );
		}
		canvas.save ( );
		canvas.rotate (animatedValue, width / 2, height / 2 );
		canvas.drawPath ( seekbarTrianglePath, trianglePaint );
		canvas.restore ( );

		//* left triangle *//
		if ( leftTrianglePoint == null )
		{
			leftTrianglePoint = new Point ( ( width / 2 ) - utils.dpToTx ( 130 ), height / 2 );
			leftTrianglePath.moveTo ( leftTrianglePoint.x, leftTrianglePoint.y );
			leftTrianglePath.lineTo ( leftTrianglePoint.x + utils.dpToTx ( 12 ), leftTrianglePoint.y - utils.dpToTx ( 12 ) );
			leftTrianglePath.lineTo ( leftTrianglePoint.x + utils.dpToTx ( 12 ), leftTrianglePoint.y + utils.dpToTx ( 12 ) );
			leftTrianglePath.close ( );
		}
		canvas.drawPath ( leftTrianglePath, leftTrianglePaint );

		//* right triangle *//
		if ( rightTrianglePoint == null )
		{
			rightTrianglePoint = new Point ( ( width / 2 ) + utils.dpToTx ( 130 ), height / 2 );
			rightTrianglePath.moveTo ( rightTrianglePoint.x, rightTrianglePoint.y );
			rightTrianglePath.lineTo ( rightTrianglePoint.x - utils.dpToTx ( 12 ), rightTrianglePoint.y - utils.dpToTx ( 12 ) );
			rightTrianglePath.lineTo ( rightTrianglePoint.x - utils.dpToTx ( 12 ), rightTrianglePoint.y + utils.dpToTx ( 12 ) );
			rightTrianglePath.close ( );
		}
		canvas.drawPath ( rightTrianglePath, rightTrianglePaint );

		//* emotion circle *//
		canvas.drawCircle( width / 2, height / 2, utils.dpToTx( 28 ), emotionCirclePaint );

		//* text *//
		int xPos = (width / 2);
		int yPos = (int) ((height / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));
		canvas.drawText( emotionValue > 0 ? "+" + String.valueOf( emotionValue ) : String.valueOf( emotionValue ), xPos, yPos, textPaint);

		invalidate ( );
	}

	public void valueDown ( )
	{
		if ( emotionValue > -5 )
		{
			emotionValue --;
			ValueAnimator triangleAnimator = ValueAnimator.ofFloat( animatedValue + 1f,  90f + ( ( emotionValue ) * ( 180f / 10f ) ) );
			triangleAnimator.addUpdateListener( new ValueAnimator.AnimatorUpdateListener ( )
				{

					@Override
					public void onAnimationUpdate ( ValueAnimator p1 )
					{
						animatedValue = (float) p1.getAnimatedValue();
					}

				});
			triangleAnimator.setInterpolator( new LinearInterpolator ( ) );
			triangleAnimator.setDuration( 125 );
			triangleAnimator.start();
		}

		animateColor ( );

	}

	public void valueUp ( )
	{
		if ( emotionValue < 5 )
		{
			emotionValue ++;
			ValueAnimator triangleAnimator = ValueAnimator.ofFloat( animatedValue - 1f,  90f + ( ( emotionValue ) * ( 180f / 10f ) ) );
			triangleAnimator.addUpdateListener( new ValueAnimator.AnimatorUpdateListener ( )
				{

					@Override
					public void onAnimationUpdate ( ValueAnimator p1 )
					{
						animatedValue = (float) p1.getAnimatedValue();
					}

				});
			triangleAnimator.setInterpolator( new LinearInterpolator ( ) );
			triangleAnimator.setDuration( 125 );
			triangleAnimator.start();
		}

		animateColor ( );

	}

	private void animateColor ( )
	{
		int endColor = greenColor;
		if ( emotionValue > 0 )
			endColor = greenColor;
		if ( emotionValue == 0 )
			endColor = whiteColor;
		if ( emotionValue < 0 )
			endColor = redColor;

		ValueAnimator va = ValueAnimator.ofArgb( emotionCircleColor, endColor );
		va.addUpdateListener( new ValueAnimator.AnimatorUpdateListener ( )
			{

				@Override
				public void onAnimationUpdate ( ValueAnimator p1 )
				{
					emotionCircleColor = (int) p1.getAnimatedValue();
					emotionCirclePaint.setColor( emotionCircleColor );
				}


			});
		va.setInterpolator( new LinearInterpolator ( ) );
		va.setDuration( 125 );
		va.start();
	}

	@Override
	public boolean onTouchEvent ( MotionEvent event )
	{

		if ( event.getAction ( ) == MotionEvent.ACTION_UP )
		{

			if (event.getX() > ( (width / 2 ) - dashedCircleRedius ) & event.getX() < ( ( width / 2 ) + dashedCircleRedius ) )
			{
				if ( emotionSelectedListener != null )
					emotionSelectedListener.OnEmotionSelected( emotionValue );
					Log.d("aec", "emotion saved");
				return true;
			}

			if ( event.getX ( ) > width / 2 )
			{
				valueUp ( );
			}
			else
			{
				valueDown ( );
			}
		}
		return true;
	}

	public interface OnEmotionSelected
	{
		public void OnEmotionSelected (int emotion);
	}

	public void setOnEmotionSelectedListener ( OnEmotionSelected oes )
	{
		emotionSelectedListener = oes;
	}

}
