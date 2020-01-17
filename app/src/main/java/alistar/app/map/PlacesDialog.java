package alistar.app.map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.nanotasks.Tasks;

import java.util.List;

import alistar.app.R;
import alistar.app.Utils;
import alistar.app.brain.Memory;

public class PlacesDialog extends Dialog
{

	private Activity a;
	private Utils utils;
	private List<MapMarker> placesList;
	private RecyclerView rv;
	private PlacesListeAdapter adapter;

	public PlacesDialog(Activity a)
	{
		super(a);
		this.a = a;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.places_dialog);
		utils = Utils.getInstance(a);
		rv = (RecyclerView) findViewById ( R.id.recycler_view );
		rv.setHasFixedSize ( true );
		LinearLayoutManager mLayoutManager = new LinearLayoutManager ( a );
		//mLayoutManager.setReverseLayout ( true );
		//mLayoutManager.setStackFromEnd(true);
		rv.setLayoutManager ( mLayoutManager );
		getData ( );

	}
	
	private void getData()
	{
		Tasks.executeInBackground ( a, new BackgroundWork<List<MapMarker>>( ) {
				@Override
				public List<MapMarker> doInBackground ( ) throws Exception
				{
					//final int DELAY = 3;
					//Thread.sleep(TimeUnit.SECONDS.toMillis(DELAY));
					return new Memory(a).getPlaces();
				}
			}, new Completion<List<MapMarker>>( ) {
				@Override
				public void onSuccess (Context context, List<MapMarker> result )
				{
					placesList = result;
					adapter = new PlacesListeAdapter ( a, utils );
					rv.setAdapter ( adapter );
				}

				@Override
				public void onError ( Context context, Exception e )
				{
					Toast.makeText ( context, e.getMessage ( ), Toast.LENGTH_LONG ).show ( );

				}
			} );
			}
			
	public class PlacesListeAdapter extends RecyclerView.Adapter<PlacesListeAdapter.MyViewHolder>
	{
		private Context context;
		private Utils utils;

		// Provide a reference to the views for each data item
		// Complex data items may need more than one view per item, and
		// you provide access to all the views for a data item in a view holder
		public class MyViewHolder extends RecyclerView.ViewHolder
		{
			/*public TextView place, date;
			 public View line;*/
			public TextView placeName;
			
			public MyViewHolder ( View v )
			{
				super ( v );
				placeName = (TextView) v.findViewById(R.id.place_name);
				/*place = (TextView) v.findViewById(R.id.place);
				 date = (TextView) v.findViewById(R.id.date);
				 line = v.findViewById(R.id.line);*/
			}
		}

		// Provide a suitable constructor (depends on the kind of dataset)
		public PlacesListeAdapter ( Context ctx, Utils u )
		{
			context = ctx;
			this.utils = u;
		}

		// Create new views (invoked by the layout manager)
		@Override
		public PlacesListeAdapter.MyViewHolder onCreateViewHolder ( ViewGroup parent,
																int viewType )
		{
			// create a new view
			View v = LayoutInflater.from ( parent.getContext ( ) )
				.inflate ( R.layout.places_list_item, parent, false );
			// set the view's size, margins, paddings and layout parameters
			MyViewHolder vh = new MyViewHolder ( v );
			return vh;
		}

		@Override
		public void onBindViewHolder ( PlacesListeAdapter.MyViewHolder holder, final int position )
		{
			holder.placeName.setText(placesList.get(position).getName());
			holder.placeName.setOnClickListener(new View.OnClickListener()
				{

					@Override
					public void onClick ( View p1 )
					{
						Toast.makeText(a, placesList.get(position).getName(), Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(a, SaraMap.class);
						intent.putExtra("command", "zoom_marker");
						intent.putExtra("marker_id", placesList.get(position).getId());
						a.startActivity(intent);
					}
					
				
			});
		}

		@Override
		public int getItemCount ( )
		{
			return placesList.size ( );
		}
	}

}
