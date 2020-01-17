package alistar.app.map;

import android.app.*;
import android.os.*;
import android.widget.*;
import alistar.app.*;
import android.graphics.*;

public class MarkerDialog extends Dialog
{
	
	private Activity a;
	public boolean edite = false;
	public TextView dialogTitle, save, delete;
	public EditText name, description, distance;
	public CheckBox alart, star;
	private Utils utils;
	
	public MarkerDialog(Activity a)
	{
		super(a);
		this.a = a;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.marker_dialog);
		utils = Utils.getInstance(a);
		
		dialogTitle = (TextView) findViewById(R.id.dialog_title);
		save = (TextView) findViewById(R.id.save_marker);
		delete = (TextView) findViewById(R.id.delete_marker);
		name = (EditText) findViewById(R.id.marker_name_et);
		description = (EditText) findViewById(R.id.marker_desription_et);
		distance = (EditText) findViewById(R.id.distance);
		alart = (CheckBox) findViewById(R.id.marker_alart);
		star = (CheckBox) findViewById(R.id.marker_star);
		
		distance.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
		name.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
		description.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
	}
	
	
}
