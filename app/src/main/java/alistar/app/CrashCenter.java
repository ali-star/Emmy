package alistar.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;
import java.io.File;
import android.graphics.Color;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.readystatesoftware.notificationlog.Log;
import java.util.ArrayList;
import su.levenetc.android.badgeview.BadgeView;
import android.webkit.WebView;
import me.next.slidebottompanel.SlideBottomPanel;
import android.graphics.drawable.ColorDrawable;

public class CrashCenter extends Activity
{

	private RecyclerView rv;
	private CrashCenterAdapter cca;
	private List<File> data;
	private Utils utils;
	private BadgeView badgeView;
	private WebView webview;
	private SlideBottomPanel spl;

	@Override
	protected void onCreate ( Bundle savedInstanceState )
	{
		super.onCreate ( savedInstanceState );
		setContentView(R.layout.crash_center);

		rv = findViewById ( R.id.recycler_view );
		spl = findViewById(R.id.sbv);
		badgeView = findViewById( R.id.badge );
		utils = Utils.getInstance ( this );
		rv.setHasFixedSize ( true );
		LinearLayoutManager mLayoutManager = new LinearLayoutManager ( this );
		rv.setLayoutManager ( mLayoutManager );
		getData ( );
	}

	private void getData()
	{
		badgeView.setValue("Checking Crashes...");
		AsyncTask.execute(new Runnable() {
			@Override
			public void run() {
				data = new ArrayList<>();
				File directory = new File(Utils.CRASH_FOLDER);
				final File[] files = directory.listFiles();
				Log.d("Files", "Size: "+ files.length);
				for (File file : files) {
					Log.d("Files", "FileName:" + file.getName());
					data.add(0, file);
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						badgeView.setValue("we have " + String.valueOf(files.length) + " crashes!");
						cca = new CrashCenterAdapter(data, CrashCenter.this);
						rv.setAdapter(cca);
					}
				});
			}
		});
	}

	public class CrashCenterAdapter extends RecyclerView.Adapter<CrashCenterAdapter.MyViewHolder>
	{
		private List<File> mDataset;
		private Context context;

		// Provide a reference to the views for each data item
		// Complex data items may need more than one view per item, and
		// you provide access to all the views for a data item in a view holder
		public class MyViewHolder extends RecyclerView.ViewHolder
		{
			/*public TextView place, date;
			 public View line;*/
			public TextView text;
			public View color, clickBase;

			public MyViewHolder ( View v )
			{
				super ( v );
				text = (TextView) v.findViewById ( R.id.text );
				color = v.findViewById(R.id.color);
				clickBase = v.findViewById(R.id.click_base);
			}
		}

		// Provide a suitable constructor (depends on the kind of dataset)
		public CrashCenterAdapter ( List<File> myDataset, Context ctx)
		{
			mDataset = myDataset;
			context = ctx;
		}

		// Create new views (invoked by the layout manager)
		@Override
		public CrashCenterAdapter.MyViewHolder onCreateViewHolder ( ViewGroup parent,
																	int viewType )
		{
			// create a new view
			View v = LayoutInflater.from ( parent.getContext ( ) )
					.inflate ( R.layout.crash_item, parent, false );
			// set the view's size, margins, paddings and layout parameters
			MyViewHolder vh = new MyViewHolder ( v );
			return vh;
		}

		@Override
		public void onBindViewHolder ( CrashCenterAdapter.MyViewHolder holder, int position )
		{
			Log.i("cca", "work");
			File crashFile = mDataset.get ( position );
			final String crash = utils.readFile(crashFile.getPath());
			holder.text.setText(crash);
			if(crash.indexOf("crash_ok")<0)
			{
				holder.color.setBackgroundColor(Color.parseColor("#FF6C94"));
			}
			else
			{
				holder.color.setBackgroundColor(Color.parseColor("#69FFDE"));
			}
			holder.clickBase.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick ( View p1 )
				{
					CrashLogDialog cld = new CrashLogDialog (CrashCenter.this);
					cld.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
					cld.show();
					cld.setCrashLog(crash);
					//Log.d("click", "click ok");
					//spl.displayPanel();
				}

			});
		}

		@Override
		public int getItemCount ( )
		{
			return mDataset.size ( );
		}
	}

}
