package alistar.app;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class CrashReportActivity extends Activity
{
	
	private WebView webview;

	@Override
	protected void onCreate ( Bundle savedInstanceState )
	{
		super.onCreate ( savedInstanceState );
		setContentView(R.layout.crash_report_activity);
		webview = (WebView) findViewById(R.id.webview);
		webview.getSettings().setJavaScriptEnabled(true);
		Bundle bundle = getIntent().getExtras();
		if(bundle != null)
		{
			if(bundle.getString("crash") != null)
			{
				webview.loadDataWithBaseURL("", bundle.getString("crash"), "text/html", "UTF-8", "");
			}
			else
			{
				webview.loadDataWithBaseURL("", "No Data", "text/html", "UTF-8", "");
			}
		}
	}
	
}
