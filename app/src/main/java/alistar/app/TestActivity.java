package alistar.app;
import android.app.*;
import android.os.*;
import ru.rambler.libs.swipe_layout.*;
import android.widget.*;

public class TestActivity extends Activity
{

	SwipeLayout swipeLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_activity);
		//ImageView iv =(ImageView) findViewById(R.id.iv);
		//iv.setImageBitmap(Utils.getInstance(this).getEmoji("crying-1.png"));
	}
	
}
