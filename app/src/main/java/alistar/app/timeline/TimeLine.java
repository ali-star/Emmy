package alistar.app.timeline;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.nanotasks.Tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alistar.app.R;
import alistar.app.SaraView;
import alistar.app.Utils;
import alistar.app.brain.Memory;
import alistar.app.ui.BubbleLayout;

public class TimeLine extends Activity
{

	private RecyclerView rv;
	private Utils utils;
	private List<Moment> data;
	private TimeLineAdapter adapter;
	private BubbleLayout bl;
	private Map<String, Bitmap> bitmaps;
	List<Bitmap> list = new ArrayList<Bitmap> ( );
	List<String> names = new ArrayList<String>( );
	private EditText textBase;
	private String emojiName;
	private View emojiBase;
	private RecyclerView emojiRv;
	private ImageView emoji;
	private QTNAdapter emojiAdapter;
	private SaraView sara;
	private OnDataLoadListener dataLoadListener;
	private int processes = 0;
	private ImageView save;

	@Override
	protected void onCreate ( Bundle savedInstanceState )
	{
		// TODO: Implement this method
		Window window = getWindow ( );
		// clear FLAG_TRANSLUCENT_STATUS flag:
		window.clearFlags ( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS );

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
		window.addFlags ( WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS );

        // finally change the color
		window.setStatusBarColor ( Color.parseColor ( "#6994FD" ) );
		super.onCreate ( savedInstanceState );
		setContentView ( R.layout.timeline_activity );

		bitmaps = new HashMap<String, Bitmap>( );
		rv = (RecyclerView) findViewById ( R.id.recycler_view );
		utils = Utils.getInstance ( this );
        rv.setHasFixedSize ( true );
		LinearLayoutManager mLayoutManager = new LinearLayoutManager ( this );
		mLayoutManager.setReverseLayout ( true );
		//mLayoutManager.setStackFromEnd(true);
		rv.setLayoutManager ( mLayoutManager );
		getData ( );
		textBase = (EditText) findViewById ( R.id.text_base );
		textBase.getBackground ( ).mutate ( ).setColorFilter ( Color.parseColor ( "#6994FD" ), PorterDuff.Mode.SRC_ATOP );
		textBase.addTextChangedListener(new TextWatcher()
			{

				@Override
				public void beforeTextChanged ( CharSequence p1, int p2, int p3, int p4 )
				{
					// TODO: Implement this method
				}

				@Override
				public void onTextChanged ( CharSequence p1, int p2, int p3, int p4 )
				{
					// TODO: Implement this method
					if(emojiName != null)
					{
						if(!textBase.getText().toString().equals(""))
						{
							save.setEnabled(true);
							save.setAlpha(1.0f);
						}
						else
						{
							save.setEnabled(false);
							save.setAlpha(.5f);
						}
					}
				}

				@Override
				public void afterTextChanged ( Editable p1 )
				{
					// TODO: Implement this method
				}
				
			
		});
		emoji = (ImageView) findViewById ( R.id.emoji_iv );
		emojiRv = (RecyclerView) findViewById ( R.id.emoji_recycler_view );
		emojiBase = findViewById ( R.id.emoji_base );
		save = (ImageView) findViewById(R.id.save);
		save.setAlpha(.5f);
		save.setEnabled(false);
		save.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick ( View p1 )
				{
					addNote();
				}
				
			
		});
		emojiRv.setHasFixedSize ( true );
		emojiRv.setLayoutManager ( new GridLayoutManager( this, 4 ) );
		getEmojiData ( );
		emoji.setOnClickListener ( new View.OnClickListener ( )
			{

				@Override
				public void onClick ( View p1 )
				{
					// TODO: Implement this method
					showEmojiPicker ( );
				}


			} );

		sara = (SaraView) findViewById ( R.id.sara );

		sara.setRedius ( 7 );
		sara.setColor ( Color.parseColor ( "#6994FD" ) );

		sara.setTextView ( (TextView)findViewById ( R.id.sara_tv ) );
		sara.say ( "Loading data..." );
	}

	private void getData ( )
	{
		Tasks.executeInBackground ( this, new BackgroundWork<String>( ) {
				@Override
				public String doInBackground ( ) throws Exception
				{
					//final int DELAY = 3;
					//Thread.sleep(TimeUnit.SECONDS.toMillis(DELAY));
					Log.d ( "task", "loading momets" );
					registerProcess();
					data = new Memory( TimeLine.this ).getAllMoments ( );
					String data = utils.readFile ( utils.EMOJI_FOLDER + "list.txt" );
					list = new ArrayList<Bitmap> ( );
					names = new ArrayList<String> ( );
					String[] stringSet = data.split ( "\n" );
					Bitmap bitmap = null;
					for ( int i=0; i < stringSet.length; i++ )
					{
						if ( stringSet [ i ].indexOf ( ".png" ) > 0 )
							bitmap = utils.getEmoji ( stringSet [ i ] );
						list.add ( bitmap );
						names.add ( stringSet [ i ] );
						bitmaps.put ( stringSet [ i ], bitmap );
						bitmap = null;
					}
					Log.d ( "task", "done" );
					return null;
				}
			}, new Completion<String>( ) {
				@Override
				public void onSuccess (Context context, String result )
				{
					adapter = new TimeLineAdapter ( data, TimeLine.this, utils );
					rv.setAdapter ( adapter );
					dataLoadListener.onDataLoadComplite();
					if ( emojiAdapter != null )
						sara.say ( "done" );
					Log.d ( "task", "done" );
				}

				@Override
				public void onError ( Context context, Exception e )
				{
					Log.d ( "task", "error" );
					Toast.makeText ( context, e.getMessage ( ), Toast.LENGTH_LONG ).show ( );

				}
			} );

	}

	public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.MyViewHolder>
	{
		private List<Moment> mDataset;
		private Context context;
		private Utils utils;

		// Provide a reference to the views for each data item
		// Complex data items may need more than one view per item, and
		// you provide access to all the views for a data item in a view holder
		public class MyViewHolder extends RecyclerView.ViewHolder
		{
			/*public TextView place, date;
			 public View line;*/
			public TextView note;
			public ImageView emoji;
			public View topLine, bottomLine;

			public MyViewHolder ( View v )
			{
				super ( v );
				note = (TextView) v.findViewById ( R.id.note );
				emoji = (ImageView) v.findViewById ( R.id.emoji );
				topLine = v.findViewById ( R.id.top_line );
				bottomLine = v.findViewById ( R.id.bottom_line );
				bl = (BubbleLayout) v.findViewById ( R.id.bubble_layout );
				/*place = (TextView) v.findViewById(R.id.place);
				 date = (TextView) v.findViewById(R.id.date);
				 line = v.findViewById(R.id.line);*/
			}
		}

		// Provide a suitable constructor (depends on the kind of dataset)
		public TimeLineAdapter ( List<Moment> myDataset, Context ctx, Utils u )
		{
			mDataset = myDataset;
			context = ctx;
			this.utils = u;
		}

		// Create new views (invoked by the layout manager)
		@Override
		public TimeLineAdapter.MyViewHolder onCreateViewHolder ( ViewGroup parent,
																int viewType )
		{
			// create a new view
			View v = LayoutInflater.from ( parent.getContext ( ) )
				.inflate ( R.layout.timeline_item, parent, false );
			// set the view's size, margins, paddings and layout parameters
			MyViewHolder vh = new MyViewHolder ( v );
			return vh;
		}

		@Override
		public void onBindViewHolder ( TimeLineAdapter.MyViewHolder holder, int position )
		{
			Moment moment = mDataset.get ( position );
			//bl.setVisibility(View.GONE);
			//holder.note.setText(null);
			//if(emotion.getNote() != null)
			//{
			//holder.note.setText("");
			holder.note.setText ( moment.getNote ( ) );
			//bl.setVisibility(View.GONE);
			//}
			holder.emoji.setImageBitmap ( bitmaps.get ( moment.getEmoji ( ) ) );
			/*if ( position == 0 )
			{
				holder.topLine.setVisibility ( View.INVISIBLE );
			}
			else
			{
				holder.topLine.setVisibility ( View.VISIBLE );
			}
			if ( position != mDataset.size ( ) - 1 )
			{
				holder.bottomLine.setVisibility ( View.VISIBLE );
			}
			else
			{
				holder.bottomLine.setVisibility ( View.INVISIBLE );
			}*/

			/*if(holder.note.getText().equals(""))
			 {
			 bl.setVisibility(View.INVISIBLE);
			 }
			 else
			 {
			 bl.setVisibility(View.VISIBLE);
			 }*/

			/*holder.date.setText(utils.convertLongToTimeString(mDataset.get(position).getDate()));
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
			 }*/

		}

		@Override
		public int getItemCount ( )
		{
			return mDataset.size ( );
		}
	}

	private void getEmojiData ( )
	{
		Tasks.executeInBackground ( this, new BackgroundWork<String> ( ) {
				@Override
				public String doInBackground ( ) throws Exception
				{
					//final int DELAY = 3;
					//Thread.sleep(TimeUnit.SECONDS.toMillis(DELAY));
					Log.d ( "task", "loding emotions list" );
					registerProcess();
					String data = utils.readFile ( utils.EMOJI_FOLDER + "list.txt" );
					List<Bitmap> list = new ArrayList<Bitmap> ( );
					List<String> names = new ArrayList<String> ( );
					String[] stringSet = data.split ( "\n" );
					for ( int i=0; i < stringSet.length; i++ )
					{
						if ( stringSet [ i ].indexOf ( ".png" ) > 0 )
							list.add ( utils.getEmoji ( stringSet [ i ] ) );
						names.add ( stringSet [ i ] );
					}
					Log.d ( "list", String.valueOf ( list.size ( ) ) );
					emojiAdapter = new QTNAdapter ( names, list, TimeLine.this );
					Log.d ( "task", "done" );
					return null;
				}
			}, new Completion<String> ( ) {
				@Override
				public void onSuccess ( Context context, String result )
				{
					emojiRv.setAdapter ( emojiAdapter );
					dataLoadListener.onDataLoadComplite();
					Log.d ( "task", "done" );
				}

				@Override
				public void onError ( Context context, Exception e )
				{
					Log.d ( "task", "error" );
					Toast.makeText ( context, e.getMessage ( ), Toast.LENGTH_LONG ).show ( );

				}
			} );
	}

	public class QTNAdapter extends RecyclerView.Adapter<QTNAdapter.AViewHolder>
	{

		private Context context;
		private List<Bitmap> list;
		private List<String> names;
		private Utils utils;

		public QTNAdapter ( List<String> n, List<Bitmap> l, Context ctx )
		{
			list = l;
			names = n;
			context = ctx;
			utils.getInstance ( context );
		}

		@Override
		public TimeLine.QTNAdapter.AViewHolder onCreateViewHolder ( ViewGroup p1, int p2 )
		{
			// TODO: Implement this method
			View v = LayoutInflater.from ( p1.getContext ( ) ).inflate ( R.layout.qtna_item, p1, false );
			AViewHolder avh = new AViewHolder ( v );
			return avh;
		}

		@Override
		public void onBindViewHolder ( TimeLine.QTNAdapter.AViewHolder holder, final int pos )
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
			holder.icon.setImageBitmap ( list.get ( pos ) );
			holder.icon.setOnClickListener ( new View.OnClickListener ( )
				{

					@Override
					public void onClick ( View p1 )
					{
						// TODO: Implement this method
						emoji.setImageBitmap ( list.get ( pos ) );
						emojiName = names.get ( pos );
						if(!textBase.getText().toString().equals(""))
						{
							save.setEnabled(true);
							save.setAlpha(1.0f);
						}
						hideEmojiPicker ( );
					}

				} );
		}

		@Override
		public int getItemCount ( )
		{
			// TODO: Implement this method
			return list.size ( );
		}

		public class AViewHolder extends RecyclerView.ViewHolder
		{

			public ImageView icon;

			public AViewHolder ( View v )
			{
				super ( v );
				icon = (ImageView) v.findViewById ( R.id.icon );
			}
		}
	}

	private void showEmojiPicker ( )
	{
		emojiBase.setVisibility ( View.VISIBLE );
	}

	private void hideEmojiPicker ( )
	{
		emojiBase.setVisibility ( View.INVISIBLE );
	}

	private void registerProcess ( )
	{
		processes++;
		if ( dataLoadListener == null )
		{
			dataLoadListener = new OnDataLoadListener ( )
			{

				@Override
				public void onDataLoadComplite ( )
				{
					processes--;
					if ( processes == 0 )
					{
						sara.say ( "Data load complite" );
					}
				}


			};
		}
	}

	private interface OnDataLoadListener
	{
		public void onDataLoadComplite ();
	}
	private void addNote()
	{
		utils.saveMoment(emojiName, textBase.getText().toString());
		emojiName = null;
		textBase.setText("");
		emoji.setImageResource(android.R.drawable.ic_delete);
		data.add(0, new Memory(TimeLine.this).getLastSavedMoment());
		adapter.notifyItemInserted(0);
		rv.scrollToPosition(0);
		save.setEnabled(false);
		save.setAlpha(.5f);
		sara.say("Note saved");
		sara.say("type something...", 2000);
	}

}
