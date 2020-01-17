package alistar.app;
import android.app.Dialog;
import android.os.Bundle;
import android.content.Context;
import android.app.Activity;
import android.webkit.WebView;

public class CrashLogDialog extends Dialog
{
	
	private Activity activity;
	private WebView webview;
	
	public CrashLogDialog (Activity a)
	{
		super (a);
		activity = a;
	}

	@Override
	protected void onCreate ( Bundle savedInstanceState )
	{
		// TODO: Implement this method
		super.onCreate ( savedInstanceState );
		setContentView(R.layout.crash_log_dialog);
		webview = (WebView) findViewById(R.id.webview);
		webview.getSettings().setJavaScriptEnabled(true);
	}
	
	public void setCrashLog (String log)
	{
		webview.loadDataWithBaseURL("", log, "text/html", "UTF-8", "");
	}
	
}
