package alistar.app.ear;
import alistar.app.*;
import androidx.recyclerview.widget.RecyclerView;

import android.view.*;

public class RecordingsAdapter extends RecyclerView.Adapter<RecordingsAdapter.Holder>
{

	@Override
	public RecordingsAdapter.Holder onCreateViewHolder ( ViewGroup p1, int p2 )
	{
		// create a new view
		View v = LayoutInflater.from(p1.getContext())
			.inflate(R.layout.recording_item, p1, false);
		// set the view's size, margins, paddings and layout parameters
		Holder vh = new Holder(v);
		return vh;
	}

	@Override
	public void onBindViewHolder ( RecordingsAdapter.Holder p1, int p2 )
	{
		// TODO: Implement this method
	}

	@Override
	public int getItemCount ( )
	{
		// TODO: Implement this method
		return 0;
	}
	
	
	public class Holder extends RecyclerView.ViewHolder
	{
		public Holder ( View v )
		{
			super(v);
		}
	}
	
}
