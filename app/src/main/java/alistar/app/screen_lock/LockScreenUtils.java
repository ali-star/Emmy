package alistar.app.screen_lock;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import alistar.app.*;
import android.view.*;

public class LockScreenUtils {
	
	private Context mContext = null;
	public static String[] dataToggleSteps = {"SWIPE_RIGHT", "SWIPE_LEFT", "SWIPE_DOWN", "SWIPE_RIGHT", "SWIPE_DOWN"};
	public static String[] unlockSteps = {"SWIPE_RIGHT", "SWIPE_LEFT", "SWIPE_DOWN", "SWIPE_LEFT", "SWIPE_RIGHT", "DOUBLE_CLICK"};
	public static String[] momentSteps = {"CLICK", "CLICK", "CLICK", "SWIPE_DOWN"};
    private static LockScreenUtils mLockscreenUtilInstance;
    public static LockScreenUtils getInstance(Context context) {
        if (mLockscreenUtilInstance == null) {
            if (null != context) {
                mLockscreenUtilInstance = new LockScreenUtils(context);
            }
            else {
                mLockscreenUtilInstance = new LockScreenUtils();
            }
        }
        return mLockscreenUtilInstance;
    }

    private LockScreenUtils(Context context) {
        mContext = context;
    }
    public boolean isStandardKeyguardState() {
        boolean isStandardKeyguqrd = false;
        KeyguardManager keyManager =(KeyguardManager) mContext.getSystemService(mContext.KEYGUARD_SERVICE);
        if (null != keyManager) {
            isStandardKeyguqrd = keyManager.isKeyguardSecure();
        }

        return isStandardKeyguqrd;
    }

    public boolean isSoftKeyAvail(Context context) {
        final boolean[] isSoftkey = {false};
        final View activityRootView = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					int rootViewHeight = activityRootView.getRootView().getHeight();
					int viewHeight = activityRootView.getHeight();
					int heightDiff = rootViewHeight - viewHeight;
					if (heightDiff > 100) { // 99% of the time the height diff will be due to a keyboard.
						isSoftkey[0] = true;
					}
				}
			});
        return isSoftkey[0];
    }

    public int getStatusBarHeight(){
        int result=0;
        int resourceId= mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if(resourceId >0)
            result = mContext.getResources().getDimensionPixelSize(resourceId);

        return result;
    }
	

	// Member variables
	private OverlayDialog mOverlayDialog;
	private OnLockStatusChangedListener mLockStatusChangedListener;

	// Interface to communicate with owner activity
	public interface OnLockStatusChangedListener
	{
		public void onLockStatusChanged(boolean isLocked);
	}

	// Reset the variables
	public LockScreenUtils() {
		reset();
	}

	// Display overlay dialog with a view to prevent home button click
	public void lock(Activity activity) {
		if (mOverlayDialog == null) {
			mOverlayDialog = new OverlayDialog(activity);
			mOverlayDialog.show();
			mLockStatusChangedListener = (OnLockStatusChangedListener) activity;
		}
	}

	// Reset variables
	public void reset() {
		if (mOverlayDialog != null) {
			mOverlayDialog.dismiss();
			mOverlayDialog = null;
		}
	}

	// Unlock the home button and give callback to unlock the screen
	public void unlock() {
		if (mOverlayDialog != null) {
			mOverlayDialog.dismiss();
			mOverlayDialog = null;
			if(mLockStatusChangedListener!=null)
			{
				mLockStatusChangedListener.onLockStatusChanged(false);
			}
		}
	}

	// Create overlay dialog for lockedscreen to disable hardware buttons
	private static class OverlayDialog extends AlertDialog {

		public OverlayDialog(Activity activity) {
			super(activity, R.style.OverlayDialog);
			WindowManager.LayoutParams params = getWindow().getAttributes();
			params.type = LayoutParams.TYPE_SYSTEM_ERROR;
			params.dimAmount = 0.0F;
			params.width = 0;
			params.height = 0;
			params.gravity = Gravity.BOTTOM;
			getWindow().setAttributes(params);
			getWindow().setFlags(LayoutParams.FLAG_SHOW_WHEN_LOCKED | LayoutParams.FLAG_NOT_TOUCH_MODAL,
								 0xffffff);
			setOwnerActivity(activity);
			setCancelable(false);
		}

		// consume touch events
		public final boolean dispatchTouchEvent(MotionEvent motionevent) {
			return true;
		}

	}
}


