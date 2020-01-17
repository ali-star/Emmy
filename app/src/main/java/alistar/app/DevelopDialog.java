package alistar.app;

import android.app.*;
import android.content.*;
import android.view.*;
import com.github.angads25.filepicker.model.*;
import java.io.*;
import com.github.angads25.filepicker.view.*;
import com.github.angads25.filepicker.controller.*;
import com.readystatesoftware.notificationlog.*;
import alistar.app.alarm.*;
import android.widget.*;

import alistar.app.ear.RecordingActivity;
import alistar.app.voice_recognizer.*;
import alistar.app.music.*;
import alistar.app.screen_lock.*;
import com.nanotasks.*;
import android.os.Bundle;
import alistar.app.eye.*;
import alistar.app.opencv.*;
import alistar.app.timeline.*;
import alistar.app.map.*;
import android.graphics.drawable.*;
import android.graphics.*;

public class DevelopDialog extends Dialog
{

	private Activity a;
	private Utils utils;

	public DevelopDialog(Activity a)
	{
		super(a);
		this.a = a;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.develop_dialog);
		utils = Utils.getInstance(a);
		findViewById(R.id.exz).setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					// TODO: Implement this method
					DialogProperties properties=new DialogProperties();
					properties.selection_mode = DialogConfigs.MULTI_MODE;
					properties.selection_type = DialogConfigs.FILE_SELECT;
					properties.root = new File(DialogConfigs.DEFAULT_DIR);
					properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
					properties.extensions = null;
					FilePickerDialog dialog = new FilePickerDialog(a, properties);
					dialog.setTitle("Select a File");
					dialog.setDialogSelectionListener(new DialogSelectionListener() {
							@Override
							public void onSelectedFilePaths(final String[] files)
							{
								//files is the array of the paths of files selected by the Application User.
								Tasks.executeInBackground(a, new BackgroundWork<String>() {
										@Override
										public String doInBackground() throws Exception {
											//final int DELAY = 3;
											//Thread.sleep(TimeUnit.SECONDS.toMillis(DELAY));
											for (int i=0; i < files.length; i++)
											{
												utils.copyIconsFromZip(files[i]);
											}
											Log.d("files", "working on files");
											return null;
										}
									}, new Completion<String>() {
										@Override
										public void onSuccess(Context context, String result) {
											
											Log.d("files", "done");
										}

										@Override
										public void onError(Context context, Exception e) {
											Log.d("files", "error");
											Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();

										}
									});
							}
						});
					dialog.show();
				}


			});

		findViewById(R.id.alarm).setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					a.startActivity(new Intent(a, AlarmActivity.class));
				}


			});

		findViewById(R.id.voice_recognizer).setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					a.startActivity(new Intent(a, SpeechRecognizerActivity.class));
				}


			});

		findViewById(R.id.open_eq).setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					//a.startActivity(new Intent(a, MusicActivity.class));
					/*Intent intent = new Intent();
					 intent.setAction("android.media.action.DISPLAY_AUDIO_EFFECT_CONTROL_PANEL");
					 if((intent.resolveActivity(a.getPackageManager()) != null)) {
					 a.startActivityForResult(intent, 1);
					 // REQUEST_EQ is an int of your choosing
					 } else {
					 // No equalizer found :(
					 }*/
					 PlacesDialog pd = new PlacesDialog(a);
					 pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
					 pd.show();
				}


			});

		findViewById(R.id.lockscreen).setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					a.startActivity(new Intent(a, LockScreenActivity.class).putExtra("test", true));
					//Utils.getInstance(a).setConnection(true, a);
					//a.startService(new Intent(a, MyService.class).putExtra("command", Utils.WAKEFUL_SEARCH_NEARBY_PLACE));
				}


			});
			
		findViewById(R.id.opencv).setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					a.startActivity(new Intent(a, FdActivity.class).putExtra("test", true));
					//Utils.getInstance(a).setConnection(true, a);
					//a.startService(new Intent(a, MyService.class).putExtra("command", Utils.WAKEFUL_SEARCH_NEARBY_PLACE));
				}


			});
			
		findViewById(R.id.find_me).setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					//a.startActivity(new Intent(a, OpenCVActivity.class).putExtra("test", true));
					//Utils.getInstance(a).setConnection(true, a);
					//a.startService(new Intent(a, MyService.class).putExtra("command", Utils.WAKEFUL_SEARCH_NEARBY_PLACE));
					((MainActivity) a).findMe();
				}


			});
			
			findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					a.startActivity(new Intent(a, QuickTimelineNote.class));
					/*String data = utils.readFile(utils.EMOJI_FOLDER + "list.txt");
					//List<String> list = new ArrayList<String>();
					String[] stringSet = data.split("\n");
					Log.d("file", data);
					for(int i=0; i<stringSet.length; i++)
					{
						//Log.d("file", stringSet[i]);
					}*/
				}
				
				
			});
		findViewById(R.id.btn_resize).setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					DialogProperties properties=new DialogProperties();
					properties.selection_mode = DialogConfigs.MULTI_MODE;
					properties.selection_type = DialogConfigs.FILE_SELECT;
					properties.root = new File(DialogConfigs.DEFAULT_DIR);
					properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
					properties.extensions = null;
					FilePickerDialog dialog = new FilePickerDialog(a, properties);
					dialog.setTitle("Select a File");
					dialog.setDialogSelectionListener(new DialogSelectionListener() {
							@Override
							public void onSelectedFilePaths(final String[] files)
							{
								//files is the array of the paths of files selected by the Application User.
								Tasks.executeInBackground(a, new BackgroundWork<String>() {
										@Override
										public String doInBackground() throws Exception {
											//final int DELAY = 3;
											//Thread.sleep(TimeUnit.SECONDS.toMillis(DELAY));
											StringBuilder sb = new StringBuilder();
											for(int i = 0; i<files.length; i++)
											{
												sb.append(utils.convertFormatName(files[i], ".png") + "\n");
											}
											File f = new File(Utils.EMOJI_FOLDER + "list.txt");
											if(!f.exists())
											{
												f.createNewFile();
											}
											utils.writeStringToFile(sb.toString(), f);
											Log.d("files", "resizing images");
											File file = new File(Utils.EMOJI_FOLDER);
											if(!file.exists())
											{
												file.mkdirs();
											}
											for (int i=0; i < files.length; i++)
											{
												try
												{
													
													utils.saveBitmap(utils.resizeBitmap(files[i], 190, 190), Utils.EMOJI_FOLDER + utils.convertFormatName(files[i], ".png"));
												}
												catch(Exception e)
												{
													e.printStackTrace();
													Log.d("files", "error in "+files[i]);
												}
												
											}
											return null;
										}
									}, new Completion<String>() {
										@Override
										public void onSuccess(Context context, String result) {

											Log.d("files", "done");
										}

										@Override
										public void onError(Context context, Exception e) {
											Log.d("files", "error");
											Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();

										}
									});
							}
						});
					dialog.show();
				}


			});
			
			findViewById(R.id.btn_timeline).setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					a.startActivity(new Intent(a, TimeLine.class));
				}
				
				
			});
			
		findViewById(R.id.btn_emotions_activity).setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					a.startActivity(new Intent(a, AliEmotionsActivity.class));
				}


			});

		findViewById(R.id.btn_recording_activity).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View p1)
			{
				// TODO: Implement this method
				a.startActivity(new Intent(a, RecordingActivity.class));
			}


		});
			
		findViewById(R.id.btn_test_ask_time).setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					Utils.getInstance(a).canAskFeeling();
				}


			});
		
		/*findViewById(R.id.btn_timeline).setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					a.stopService(new Intent(a, SaraViewService.class));
				}


			});*/
			
	}

}
