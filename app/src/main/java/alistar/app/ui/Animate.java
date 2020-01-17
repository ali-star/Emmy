package alistar.app.ui;

import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import static com.nineoldandroids.view.ViewPropertyAnimator.animate;
import android.animation.ObjectAnimator;
import cimi.com.easeinterpolator.EaseCircularOutInterpolator;
import com.github.florent37.viewanimator.AnimationListener;
import android.animation.Animator;
import cimi.com.easeinterpolator.EaseCircularInOutInterpolator;
import android.view.animation.*;

public class Animate
{
	public static void alpha ( View v, float f, long d, com.nineoldandroids.animation.Animator.AnimatorListener l )
	{
		animate ( v ).alpha ( f ).setDuration ( d ).setListener ( l ).start ( );
	}

	public static void alpha ( View v, float f, long d, long a, com.nineoldandroids.animation.Animator.AnimatorListener l )
	{
		animate ( v ).alpha ( f ).setDuration ( d ).setStartDelay ( a ).setListener ( l ).start ( );
	}

	public static void alphaFrom ( View v, float fs, float fe, long d, com.nineoldandroids.animation.Animator.AnimatorListener l )
	{
		v.setAlpha ( fs );
		animate ( v ).alpha ( fe ).setDuration ( d ).setListener ( l ).start ( );
	}

	public static void softIn ( View v, long d, com.nineoldandroids.animation.Animator.AnimatorListener l )
	{
		animate ( v ).alpha ( 1 ).scaleX ( 1 ).scaleY ( 1 ).setDuration ( d ).setListener ( l ).start ( );
	}

	public static void softOut ( View v, long d, com.nineoldandroids.animation.Animator.AnimatorListener l )
	{
		animate ( v ).alpha ( 0 ).scaleX ( 0 ).scaleY ( 0 ).setDuration ( d ).setListener ( l ).start ( );
	}

	public static void slideUp ( View v, float vh, int d, Interpolator i, Animator.AnimatorListener l )
	{
		ObjectAnimator animator = new ObjectAnimator ( );
		animator.setTarget ( v );
		animator.setPropertyName ( "translationY" );
		animator.setFloatValues ( vh, 0f );
		animator.setDuration ( d );
		animator.setInterpolator ( i );
		if ( l != null )
			animator.addListener ( l );
		animator.start ( );
	}

	public static void slideDown ( View v, float vh, int d, Interpolator i, Animator.AnimatorListener l )
	{
		ObjectAnimator animator = new ObjectAnimator ( );
		animator.setTarget ( v );
		animator.setPropertyName ( "translationY" );
		animator.setFloatValues ( 0f, vh );
		animator.setDuration ( d );
		animator.setInterpolator ( i );
		if ( l != null )
			animator.addListener ( l );
		animator.start ( );
	}
}
