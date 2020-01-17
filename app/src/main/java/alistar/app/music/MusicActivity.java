package alistar.app.music;
import android.app.*;
import android.os.*;
import android.content.*;
import alistar.app.*;
import android.media.audiofx.*;
import android.view.View.*;
import android.view.*;
import com.github.angads25.filepicker.model.*;
import com.github.angads25.filepicker.view.*;
import com.github.angads25.filepicker.controller.*;
import java.io.*;
import android.media.*;
import android.widget.*;

public class MusicActivity extends Activity
{
	
	final int EVENT_PLAY_OVER = 0x100;

    Thread mThread = null;
    byte[] data = null;
    Handler mHandler;

    Equalizer mEqualizer;
    boolean isPlaying = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music_activity);
		findViewById(R.id.music_icon).setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					DialogProperties properties=new DialogProperties();
					properties.selection_mode=DialogConfigs.SINGLE_MODE;
					properties.selection_type=DialogConfigs.FILE_SELECT;
					properties.root=new File(DialogConfigs.DEFAULT_DIR);
					properties.error_dir=new File(DialogConfigs.DEFAULT_DIR);
					properties.extensions=null;
					FilePickerDialog dialog = new FilePickerDialog(MusicActivity.this,properties);
					dialog.setTitle("Select a File");
					dialog.setDialogSelectionListener(new DialogSelectionListener() {
							@Override
							public void onSelectedFilePaths(String[] files) {
								//files is the array of the paths of files selected by the Application User.
								data = getPCMDataFromFile(files[0]);
								play();
								/*Intent intent = new Intent();
								intent.setClass(MusicActivity.this, MyEqualizerActivity.class);

								intent.putExtra(AppConstants.KEY_AUDIO_ID, AppConstants.audioSessionId);
								intent.putExtra(AppConstants.KEY_IS_PLAYING, isPlaying);

								startActivity(intent);*/
								
							}
						});
					dialog.show();
				}
				
			
		});
	}
	
	public byte[] getPCMDataFromFile(String filePath) {
        File file = new File(filePath);
        FileInputStream inStream;
        byte[] dataPack = null;
        try {
            inStream = new FileInputStream(file);

            if (inStream != null) {
                long size = file.length();
                dataPack = new byte[(int) size];
                inStream.read(dataPack);
            }
            inStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return dataPack;
	}
	
	public void play() {
        if (data == null) {
            Toast.makeText(this, "No File...", Toast.LENGTH_LONG).show();
            return;
        }

        if (mThread == null) {
            isPlaying = true;
            MyThread myThread = new MyThread(data, mHandler);
            myThread.setUp();
            mThread = new Thread(myThread);

            mThread.start();
            //textView.setText("Playing...");
        }

    }

    public void stop() {
        if (data == null) {
            return;
        }

        if (mThread != null) {
            isPlaying = false;
            mThread.interrupt();
            mThread = null;
           // textView.setText("Stop...");
        }
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO: Implement this method
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
}
