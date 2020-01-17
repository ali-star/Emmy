package alistar.app.alarm;

import android.app.*;
import android.content.*;
import com.readystatesoftware.notificationlog.*;
import android.os.*;
import alistar.app.*;
import alistar.app.brain.*;

public class WakefulService extends IntentService {
	
    public WakefulService() {
        super("SimpleWakefulService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // At this point SimpleWakefulReceiver is still holding a wake lock
        // for us.  We can do whatever we need to here and then tell it that
        // it can release the wakelock.  This sample just does some slow work,
        // but more complicated implementations could take their own wake
        // lock here before releasing the receiver's.
        //
        // Note that when using this approach you should be aware that if your
        // service gets killed and restarted while in the middle of such work
        // (so the Intent gets re-delivered to perform the work again), it will
        // at that point no longer be holding a wake lock since we are depending
        // on SimpleWakefulReceiver to that for us.  If this is a concern, you can
        // acquire a separate wake lock here.
		if(intent.getExtras() != null)
		{
			Bundle bundle = intent.getExtras();
			if(bundle.getString("data").equals(Utils.WAKEFUL_ALARM))
			{
				new Alarm(getApplicationContext()).setActive(false);
				startActivity(new Intent(getApplicationContext(), AlarmActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
			}
			else if(bundle.getString("data").equals(Utils.WAKEFUL_SEARCH_NEARBY_PLACE))
			{
				startService(new Intent(getApplicationContext(), WorkQueue.class).putExtra("command", bundle.getString("data")));
			}
		}
		Log.i("SimpleWakefulReceiver", "Completed service @ " + SystemClock.elapsedRealtime());
		WakefulBroadcast.completeWakefulIntent(intent);
    }
	
	
}
