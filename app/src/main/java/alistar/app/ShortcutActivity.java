package alistar.app;
import android.app.*;
import android.os.*;
import android.content.*;

public class ShortcutActivity extends  Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		
		Intent intent = new Intent();

		Intent shortcutIntent = new Intent(getApplicationContext(), AskFeelingActivity.class);

		shortcutIntent.setAction(Intent.ACTION_MAIN);

		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "save feeling");
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher));

		setResult(RESULT_OK, intent);
		finish();
	}
	
}
