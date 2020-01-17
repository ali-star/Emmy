package alistar.app;

import android.content.*;

public class BootReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent p2)
	{
		// TODO: Implement this method
		context.startService(new Intent(context, MyService.class).putExtra("command", MyService.CHECK_SIM));
	}

}

