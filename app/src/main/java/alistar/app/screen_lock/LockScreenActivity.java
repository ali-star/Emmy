package alistar.app.screen_lock;

import alistar.app.*;
import alistar.app.screen_lock.service.*;
import alistar.app.utils.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.telephony.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.readystatesoftware.notificationlog.*;
import alistar.app.timeline.*;

/**
 * Created by mugku on 15. 3. 16..
 */
public class LockScreenActivity extends Activity
{
    private static Context sLockscreenActivityContext = null;
    public static SendMassgeHandler mMainHandler = null;
	private Utils utils;

    public PhoneStateListener phoneStateListener = new PhoneStateListener() {
        public void onCallStateChanged(int state, String incomingNumber)
		{

            switch (state)
			{
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    break;
                default:
                    break;
            }
        }

        ;
    };

	@Override
	protected void onStop ( )
	{
		overridePendingTransition(0, 0);
		super.onStop ( );
	}
	
	


    @Override
    protected void onCreate(Bundle arg0)
	{
		overridePendingTransition(0, 0);
        super.onCreate(arg0);
        sLockscreenActivityContext = this;
        mMainHandler = new SendMassgeHandler();
//        getWindow().setType(2004);
//        getWindow().addFlags(524288);
//        getWindow().addFlags(4194304);
        ///
        getWindow().setType(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

		utils = utils.getInstance(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
		else
		{
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        manager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
		{
			if (bundle.getBoolean("test", false))
			{
				//do nothing
				//Log.d("tag", "doNothing");
			}
		}
		else
		{
			setLockGuard();
			Log.d("tag", "setLockGuard");
		}

    }

    private class SendMassgeHandler extends android.os.Handler
	{
        @Override
        public void handleMessage(Message msg)
		{
            super.handleMessage(msg);
            finishLockscreenAct();
        }
    }

    private void finishLockscreenAct()
	{
        finish();
    }
    private void setLockGuard()
	{
        boolean isLockEnable = false;
        if (!LockScreenUtils.getInstance(sLockscreenActivityContext).isStandardKeyguardState())
		{
            isLockEnable = false;
        }
		else
		{
            isLockEnable = true;
        }

        Intent startLockscreenIntent = new Intent(this, LockscreenViewService.class);
        startService(startLockscreenIntent);

        boolean isSoftkeyEnable = LockScreenUtils.getInstance(sLockscreenActivityContext).isSoftKeyAvail(this);
        SharedPreferencesUtil.setBoolean(Lockscreen.ISSOFTKEY, isSoftkeyEnable);
        if (!isSoftkeyEnable)
		{
            mMainHandler.sendEmptyMessage(0);
        }
		else if (isSoftkeyEnable)
		{
            if (isLockEnable)
			{
                mMainHandler.sendEmptyMessage(0);
            }
        }
    }

}

