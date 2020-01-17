package alistar.app.ear;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import org.w3c.dom.Text;
import alistar.app.R;
import alistar.app.*;
import java.text.*;
import java.util.*;
import android.os.*;
import com.readystatesoftware.notificationlog.*;
import java.io.*;
import android.widget.*;
import android.graphics.*;
import android.media.*;

public class RecordingActivity extends Activity {

    private RecMicToMp3 mRecMicToMp3 = null;
    private boolean isRecording = false;
    private TextView status;
	private SaraView sara;
	private String currentFileName;
	private EditText recordingName;
	private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);

		sara = (SaraView) findViewById(R.id.sara);
        status = (TextView) findViewById(R.id.status);
		recordingName = (EditText) findViewById(R.id.recording_name);
		
		sara.setRedius(7);
		sara.setColor(Color.parseColor("#D8DAE8"));
		
		sara.setTextView(status);
		
		new Handler().postDelayed(new Runnable()
			{

				@Override
				public void run ( )
				{
					sara.say("tap to start");
				}


			}, 500);
			
		/*sara.setOnTouchListener(new View.OnTouchListener()
			{

				@Override
				public boolean onTouch ( View p1, MotionEvent p2 )
				{
					if ( p2 .getAction() == MotionEvent.ACTION_DOWN )
					{
						if ( isRecording )
							return true;
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
						//Log.i("date", sdf.format(new Date()));
						currentFileName = Environment.getExternalStorageDirectory() + "/sara/recordings/" + sdf.format(new Date()) + ".mp3";
						mRecMicToMp3 = new RecMicToMp3(currentFileName, 44100);
						mRecMicToMp3.start();
						//handler.post(updater);
						sara.say("recording...");
						isRecording = true;
					}
					
					if ( p2.getAction() == MotionEvent.ACTION_UP )
					{
						mRecMicToMp3.stop();
                        isRecording = false;
                        sara.say("saved");
						new Handler().postDelayed(new Runnable()
							{

								@Override
								public void run ( )
								{
									sara.say("tap to start");
								}


							}, 1000);
						File file = new File(currentFileName);
						Log.i("file", "file"+file.exists());
						if (!file.exists())
							return true;
						if (!recordingName.getText().toString().equals(""))
							file.renameTo(new File(Environment.getExternalStorageDirectory() + "/sara/recordings/" + recordingName.getText().toString() + ".mp3"));
						
					}
					return true;
				}
				
			
		});*/
		
		sara.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick ( View p1 )
				{
					if(!isRecording){
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
						//Log.i("date", sdf.format(new Date()));
						currentFileName = Environment.getExternalStorageDirectory() + "/sara/recordings/" + sdf.format(new Date()) + ".mp3";
						mRecMicToMp3 = new RecMicToMp3(currentFileName, 44100);
						mRecMicToMp3.start();
						//handler.post(updater);
						sara.say("recording...");
						isRecording = true;
					}else{
						//handler.removeCallbacks(updater);
						mRecMicToMp3.stop();
                        isRecording = false;
                        sara.say("saved");
						new Handler().postDelayed(new Runnable()
							{

								@Override
								public void run ( )
								{
									sara.say("tap to start");
								}


							}, 1000);
						File file = new File(currentFileName);
						Log.i("file", "file"+file.exists());
						if (!file.exists())
							return;
						if (!recordingName.getText().toString().equals(""))
							file.renameTo(new File(Environment.getExternalStorageDirectory() + "/sara/recordings/" + recordingName.getText().toString() + ".mp3"));
						
					}
					
				}
				
			
		});

        /*findViewById(R.id.start).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if(!isRecording){
                            mRecMicToMp3.start();
                            status.setText("recording...");
                        }
                        isRecording = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        mRecMicToMp3.stop();
                        isRecording = false;
                        status.setText("save");
                        break;
                }
                return false;
            }
        });*/

    }
	
	private void createList ( )
	{
		
	}
	
}
