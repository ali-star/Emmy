package alistar.app;
import android.widget.*;
import android.content.*;
import android.util.*;
import android.content.res.*;
import android.graphics.*;
import alistar.app.*;

public class ColoredImageView extends ImageView
{
	
	private int color = Color.parseColor("#000000");
	
	public ColoredImageView(Context context)
	{
		super(context);
	}

	public ColoredImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
		TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.coloredImageView);
		color = attributes.getColor(R.styleable.coloredImageView_setColor, Color.parseColor("#000000"));
		attributes.recycle();
		init();
    }

	public ColoredImageView(Context context, AttributeSet attrs, int defStyle)
	{
        super(context, attrs, defStyle);
		TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.coloredImageView);
		color = attributes.getColor(R.styleable.coloredImageView_setColor, Color.parseColor("#000000"));
		attributes.recycle();
		init();
	}

	public void setColor(int color)
	{
		setColorFilter(color);
	}
	
	protected void init()
	{
		setColorFilter(color);
	}
}
