package alistar.app.ui;
import android.view.*;
import android.content.*;
import android.util.*;
import android.graphics.*;

public class SeekbarBackground extends View
{
	
	private Path backgroundPath = new Path();
	private Paint backgroundPaint = new Paint();
	
	public SeekbarBackground ( Context context )
	{
		super ( context );
		init();
	}
	
	public SeekbarBackground ( Context context, AttributeSet attrs )
	{
		super ( context, attrs );
		init();
	}
	
	private void init ()
	{
		backgroundPaint.setStyle( Paint.Style.FILL );
		backgroundPaint.setAntiAlias( true );
		backgroundPaint.setColor( Color.parseColor( "#6994FD" ) );
		backgroundPaint.setPathEffect( new CornerPathEffect ( 12 ) );
	}

	@Override
	protected void onDraw ( Canvas canvas )
	{
		super.onDraw ( canvas );
		Point p1 = new Point ( 0, 0 );
		Point p2 = new Point ( getWidth(), 0 );
		Point p3 = new Point ( getWidth(), getHeight() );
		Point p4 = new Point ( 0, getHeight() );
		
		backgroundPath.moveTo( p1.x, p1.y );
		backgroundPath.lineTo( p2.x, p2.y );
		backgroundPath.lineTo( p3.x, p3.y );
		backgroundPath.lineTo( p4.x, p4.y );
		backgroundPath.close();
		
		canvas.drawPath( backgroundPath, backgroundPaint );
		
	}
	
	
}
