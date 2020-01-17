package alistar.app;

import android.widget.*;
import com.readystatesoftware.notificationlog.*;
import android.content.*;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Application;

public class MyApplication extends Application
{

	@Override
	public void onCreate ( )
	{
		// TODO: Implement this method
		super.onCreate ( );
		Log.initialize ( this );
		Log.setNotificationsEnabled ( true );
		startService ( new Intent ( this, MyService.class ) );
		
		/*Thread.setDefaultUncaughtExceptionHandler(
            new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException (Thread thread, Throwable e) {
                    handleUncaughtException (thread, e);
                }
            });*/
		
		// Initializing backtory
       /* BacktoryClient.Android.init ( Config.newBuilder ( ).
									 // Enabling User Services
									 initAuth ( "5955add3e4b03f9a23c5deb8", "5955add3e4b0613bbed270b4" ).
									 // Finilizing sdk
									 build ( ), this );*/
	}
	
	private void handleUncaughtException (Thread thread, Throwable e) {

        // The following shows what I'd like, though it won't work like this.
        //Intent intent = new Intent (getApplicationContext(),DrawView.class);
        StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
		File crashFolder = new File(Utils.CRASH_FOLDER);
		if(!crashFolder.exists())
			crashFolder.mkdirs();
		Utils.getInstance(getApplicationContext()).writeStringToFile(stackTrace.toString(), new File(Utils.CRASH_FOLDER+"crash_"+sdf.format(new Date())+".txt"));
        System.err.println(stackTrace);// You can use LogCat too
		System.exit(0);
		getApplicationContext().startActivity(new Intent(getApplicationContext(), CrashReportActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("crash", stackTrace.toString()));

        // Add some code logic if needed based on your requirement
    }

}
