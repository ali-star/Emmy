package alistar.app.alarm;
import android.content.*;
import com.readystatesoftware.notificationlog.*;
import android.os.*;

import androidx.legacy.content.WakefulBroadcastReceiver;

public class WakefulBroadcast extends WakefulBroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
	{
        // This is the Intent to deliver to our service.
		if(intent.getExtras() != null)
		{
			Intent serviceIntent = new Intent(context, WakefulService.class).putExtra("data", intent.getExtras().getString("data"));
			startWakefulService(context, serviceIntent);
		}
        

        // Start the service, keeping the device awake while it is launching.
        Log.i("SimpleWakefulReceiver", "Starting service @ " + SystemClock.elapsedRealtime());
    }
}
