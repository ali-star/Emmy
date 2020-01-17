package alistar.app;
import android.app.*;
import android.os.*;
import android.widget.*;
import android.graphics.*;
import android.view.*;

public class SelectItemDialog extends Dialog
{

	public Activity a;
	private String[] items;
	private LinearLayout addLayout;
	private TextView[] textViews;
	public int selectedInt;
	public String selectedString;

	public SelectItemDialog(Activity a)
	{
		super(a);
		this.a = a;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_item_dialog);
		addLayout = (LinearLayout) findViewById(R.id.add_base);

	}

	public void setItems(final String[] items)
	{
		this.items = items;
		textViews = new TextView[items.length];
		for (int i=0; i < items.length; i++)
		{
			textViews[i] = new TextView(a);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			textViews[i].setLayoutParams(params);
			textViews[i].setPadding(20, 20, 20, 20);
			textViews[i].setText("●  " + items[i]);
			textViews[i].setTextSize(14);
			textViews[i].setTextColor(Color.WHITE);
			final int i_f = i;
			textViews[i].setOnClickListener(new View.OnClickListener()
				{

					@Override
					public void onClick(View p1)
					{
						// TODO: Implement this method
						selectedInt = i_f;
						selectedString = items[i_f];
						SelectItemDialog.this.dismiss();
					}


				});
			addLayout.addView(textViews[i]);
		}
	}

	public void setSelectedItem(int selected)
	{
		for (int i=0; i < items.length; i++)
		{
			if (i == selected)
			{
				textViews[i].setTextColor(Color.parseColor("#448AFF"));
			}
		}
	}

	public void setSelectedItem(String selected)
	{
		for (int i=0; i < items.length; i++)
		{
			if (items[i].equals(selected))

			{
				textViews[i].setTextColor(Color.parseColor("#448AFF"));
			}
		}
	}
	
	public int getSelectedInteger()
	{
		return selectedInt;
	}
	
	public String getSelectedString()
	{
		return selectedString;
	}
}
