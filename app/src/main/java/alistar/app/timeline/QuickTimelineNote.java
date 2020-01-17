package alistar.app.timeline;
import android.app.*;
import android.os.*;
import android.widget.*;
import ru.rambler.libs.swipe_layout.*;
import alistar.app.R;

import androidx.appcompat.widget.*;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.*;
import android.content.*;
import java.util.*;

import android.graphics.*;
import alistar.app.*;
import com.nanotasks.*;
import com.readystatesoftware.notificationlog.*;

public class QuickTimelineNote extends Activity
{
	
	private ImageView emoji;
	private EditText note;
	private SwipeLayout sw;
	private RecyclerView rv;
	private Utils utils;
	private QTNAdapter adapter;
	private String emojiName;
	private View emojiBase;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
							 WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
							 WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
							 WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		setContentView(R.layout.quick_timeline_note_activity);
		emoji = (ImageView) findViewById(R.id.emoji_iv);
		note = (EditText) findViewById(R.id.note_et);
		sw = (SwipeLayout) findViewById(R.id.sl);
		rv = (RecyclerView) findViewById(R.id.recycler_view);
		emojiBase = findViewById(R.id.emoji_base);
		utils = Utils.getInstance(this);
        rv.setHasFixedSize(true);
		rv.setLayoutManager(new GridLayoutManager(this, 4));
		getData();
		emoji.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					showEmojiPicker();
				}
				
			
		});
		sw.setSwipeEnabled(false);
		sw.setOnSwipeListener(new SwipeLayout.OnSwipeListener() {
                @Override
                public void onBeginSwipe(SwipeLayout swipeLayout, boolean moveToRight) {
                }

                @Override
                public void onSwipeClampReached(SwipeLayout swipeLayout, boolean moveToRight) {
                    /*Toast.makeText(swipeLayout.getContext(),
								   (moveToRight ? "cancelled" : "Note Saved"),
								   Toast.LENGTH_SHORT)
						.show();*/
					if(moveToRight)
					{
						//save note
						if(!note.getText().toString().equals(""))
						{
							utils.saveMoment(emojiName,note.getText().toString());
						}
					}
					
					finish();

                    //viewHolder.textViewPos.setText("TADA!");
                }

                @Override
                public void onLeftStickyEdge(SwipeLayout swipeLayout, boolean moveToRight) {
                }

                @Override
                public void onRightStickyEdge(SwipeLayout swipeLayout, boolean moveToRight) {
                }
            });
	}
	
	private void getData()
	{
		Tasks.executeInBackground(this, new BackgroundWork<String>() {
				@Override
				public String doInBackground() throws Exception {
					//final int DELAY = 3;
					//Thread.sleep(TimeUnit.SECONDS.toMillis(DELAY));
					Log.d("task", "loding emotions list");
					String data = utils.readFile(utils.EMOJI_FOLDER + "list.txt");
					List<Bitmap> list = new ArrayList<Bitmap>();
					List<String> names = new ArrayList<String>();
					String[] stringSet = data.split("\n");
					for(int i=0; i<stringSet.length; i++)
					{
						if(stringSet[i].indexOf(".png") >0)
							list.add(utils.getEmoji(stringSet[i]));
							names.add(stringSet[i]);
					}
					Log.d("list", String.valueOf(list.size()));
					adapter = new QTNAdapter(names, list, QuickTimelineNote.this);
					Log.d("task", "done");
					return null;
				}
			}, new Completion<String>() {
				@Override
				public void onSuccess(Context context, String result) {
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
	
	public class QTNAdapter extends RecyclerView.Adapter<QTNAdapter.AViewHolder>
	{
		
		private Context context;
		private List<Bitmap> list;
		private List<String> names;
		private Utils utils;
		
		public QTNAdapter(List<String> n, List<Bitmap> l, Context ctx)
		{
			list = l;
			names = n;
			context = ctx;
			utils.getInstance(context);
		}

		@Override
		public QuickTimelineNote.QTNAdapter.AViewHolder onCreateViewHolder(ViewGroup p1, int p2)
		{
			// TODO: Implement this method
			View v = LayoutInflater.from(p1.getContext()).inflate(R.layout.qtna_item, p1, false);
			AViewHolder avh = new AViewHolder(v);
			return avh;
		}

		@Override
		public void onBindViewHolder(QuickTimelineNote.QTNAdapter.AViewHolder holder, final int pos)
		{
			// TODO: Implement this method
			//Log.d("list item", list.get(pos));
			/*if(utils.getEmoji(list.get(pos)) == null)
			{
				Log.d("bitmap", "null");
			}
			else
			{
				holder.icon.setImageBitmap(utils.getEmoji(list.get(pos)));
			}*/
			holder.icon.setImageBitmap(list.get(pos));
			holder.icon.setOnClickListener(new View.OnClickListener()
				{

					@Override
					public void onClick(View p1)
					{
						// TODO: Implement this method
						emoji.setImageBitmap(list.get(pos));
						emojiName = names.get(pos);
						hideEmojiPicker();
						sw.setSwipeEnabled(true);
					}
				
			});
		}

		@Override
		public int getItemCount()
		{
			// TODO: Implement this method
			return list.size();
		}
		
		public class AViewHolder extends RecyclerView.ViewHolder
		{
			
			public ImageView icon;
			
			public AViewHolder(View v)
			{
				super(v);
				icon = (ImageView) v.findViewById(R.id.icon);
			}
		}
	}
	
	private void showEmojiPicker()
	{
		emojiBase.setVisibility(View.VISIBLE);
	}
	
	private void hideEmojiPicker()
	{
		emojiBase.setVisibility(View.INVISIBLE);
	}
	
}
