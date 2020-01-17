package alistar.app.ui;
import alistar.app.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.nineoldandroids.animation.*;
import java.util.*;

public class DiamondButton extends FrameLayout
{
	
	private String text;
	private TextView textView;
	private Diamond diamondView;
	
	public DiamondButton(Context context) {
        super(context);
		//mInflater = LayoutInflater.from(context);
		init(); 
    }
    public DiamondButton(Context context, AttributeSet attrs, int defStyle)
    {
		super(context, attrs, defStyle);
		//mInflater = LayoutInflater.from(context);
		TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.base);
		text = attributes.getString(R.styleable.base_setText);
		attributes.recycle();
		init(); 
    }
	
    public DiamondButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		//mInflater = LayoutInflater.from(context);
		TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.base);
		text = attributes.getString(R.styleable.base_setText);
		attributes.recycle();
		init(); 
    }
	
	public void init()
	{
		LayoutInflater.from(getContext()).inflate(R.layout.diamond_button, this, true);
		textView = (TextView) findViewById(R.id.text);
		diamondView = (Diamond) findViewById(R.id.diamondbuttonDiamond1);
		textView.setText(text);
	}
	
	public void setText ( String text )
	{
		textView.setText( text );
	}
	
	public void setOnClickListener(View.OnClickListener ocl)
	{
		findViewById(R.id.click_base).setOnClickListener(ocl);
	}
	
	public void startAfter(long d)
	{
		findViewById(R.id.text).setAlpha(0);
		Timer timer = new Timer();
		timer.schedule(new TimerTask()
			{

				@Override
				public void run()
				{
					// TODO: Implement this method
					DiamondButton.this.post(new Runnable()
						{

							@Override
							public void run()
							{
								// TODO: Implement this method
								Animate.softIn(diamondView, 200, null);
								Animate.alpha(textView, 1, 200, null);
							}
							
						
					});
				}
				
			
		}, d);
	}
	
	public void hideAfter(long d)
	{
		Timer timer = new Timer();
		timer.schedule(new TimerTask()
			{

				@Override
				public void run()
				{
					// TODO: Implement this method
					DiamondButton.this.post(new Runnable()
						{

							@Override
							public void run()
							{
								// TODO: Implement this method
								Animate.softOut(diamondView, 200, null);
								Animate.alpha(textView, 0, 200, null);
							}
							
						
					});
				}
				
			
		}, d);
	}
}
