package alistar.app;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.content.*;
import alistar.app.map.*;
import java.util.*;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import alistar.app.brain.*;
import com.nanotasks.*;
import com.readystatesoftware.notificationlog.*;

public class LocationHistoryActivity extends Activity
{

	private LocationHistoryAdapter adapter;
	private List<LocationHistory> data;
	private Memory memory;
	private Utils utils;
	private HashMap<Integer, MapMarker> markers;
	RecyclerView rv;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_history_activity);
		rv = (RecyclerView) findViewById(R.id.recycler_view);
		memory = new Memory(this);
		utils = Utils.getInstance(this);
        rv.setHasFixedSize(true);
		rv.setLayoutManager(new LinearLayoutManager(this));
		getData();
		//new MyTask().execute();
		
	}
	
	public void getData()
	{
		Tasks.executeInBackground(this, new BackgroundWork<String>() {
				@Override
				public String doInBackground() throws Exception {
					//final int DELAY = 3;
					//Thread.sleep(TimeUnit.SECONDS.toMillis(DELAY));
					Log.d("task", "working");
					data = memory.getLocationHistoryList();
					markers = new HashMap<Integer, MapMarker>();
					for (int i=0; i < data.size(); i++)
					{
						markers.put(i, memory.findMapMarkerById(data.get(i).getMapMarkerId()));
					}
					Log.d("task", "work ok");
					return null;
				}
			}, new Completion<String>() {
				@Override
				public void onSuccess(Context context, String result) {
					adapter = new LocationHistoryAdapter(data, LocationHistoryActivity.this, utils, markers);
					rv.setAdapter(adapter);
					Log.d("task", "done");
				}

				@Override
				public void onError(Context context, Exception e) {
					Log.d("task", "error");
					Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();

				}
			});
	}

	

}
