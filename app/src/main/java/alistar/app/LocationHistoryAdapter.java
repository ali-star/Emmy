package alistar.app;
import alistar.app.map.*;
import android.content.*;
import androidx.appcompat.widget.*;
import androidx.recyclerview.widget.RecyclerView;

import android.view.*;
import android.widget.*;
import java.util.*;

public class LocationHistoryAdapter extends RecyclerView.Adapter<LocationHistoryAdapter.MyViewHolder>
{
	private List<LocationHistory> mDataset;
	private HashMap<Integer, MapMarker> markers;
	private Context context;
	private MapMarker mapMarker;
	private Utils utils;

	// Provide a reference to the views for each data item
	// Complex data items may need more than one view per item, and
	// you provide access to all the views for a data item in a view holder
	public class MyViewHolder extends RecyclerView.ViewHolder
	{
		public TextView place, date;
		public View line;
		
		public MyViewHolder(View v)
		{
			super(v);
			place = (TextView) v.findViewById(R.id.place);
			date = (TextView) v.findViewById(R.id.date);
			line = v.findViewById(R.id.line);
		}
	}

	// Provide a suitable constructor (depends on the kind of dataset)
	public LocationHistoryAdapter(List<LocationHistory> myDataset, Context ctx, Utils u, HashMap<Integer, MapMarker> markers)
	{
		mDataset = myDataset;
		context = ctx;
		this.utils = u;
		this.markers = markers;
	}

	// Create new views (invoked by the layout manager)
	@Override
	public LocationHistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
													 int viewType)
	{
		// create a new view
		View v = LayoutInflater.from(parent.getContext())
			.inflate(R.layout.location_history_item, parent, false);
		// set the view's size, margins, paddings and layout parameters
		MyViewHolder vh = new MyViewHolder(v);
		return vh;
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position)
	{

		holder.date.setText(utils.convertLongToTimeString(mDataset.get(position).getDate()));
		mapMarker = markers.get(position);
		if(mapMarker != null)
		{
			holder.place.setText(mapMarker.getName());
		}
		else
		{
			holder.place.setText("unknow place");
		}
		
		if(position == mDataset.size() -1)
		{
			holder.line.setVisibility(View.INVISIBLE);
		}
		else
		{
			holder.line.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public int getItemCount()
	{
		return mDataset.size();
	}
}
