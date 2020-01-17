package alistar.app.screen_lock;
import android.content.*;
import android.os.*;
import com.readystatesoftware.notificationlog.*;

public class LockScreenReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context p1, Intent p2)
	{
		// TODO: Implement this method
		Bundle bundle = p2.getExtras();
		if(bundle != null)
		{
			if(bundle.getString("data").equals("lock"))
			{
				//Log.d("tag", "lock screen receiver");
				//p1.startActivity(new Intent(p1, LockScreenActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
			}
		}
	}
	
}
