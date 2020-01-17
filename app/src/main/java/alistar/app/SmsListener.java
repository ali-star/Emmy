package alistar.app;
import android.content.*;
import android.provider.*;
import android.telephony.*;
import android.widget.*;
import com.readystatesoftware.notificationlog.*;

public class SmsListener extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent)
	{
		// TODO: Implement this method
		if(Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())){
			for(SmsMessage smsMassage : Telephony.Sms.Intents.getMessagesFromIntent(intent)){
				String smsBody = smsMassage.getMessageBody();
				if(smsBody.indexOf(Utils.getLocationKeyWord(context)) != -1){
					Log.d("command", "detect location command");
					Log.i("phone number", smsMassage.getOriginatingAddress());
					Utils.saveReceivedSmsPhoneNumber(context, smsMassage.getOriginatingAddress());
					context.startService(new Intent(context, MyService.class).putExtra("command", MyService.DETECT_LOCATION));
					//Utils.sendSmsMessage("+989907473597", "send sms work");
				}
				if(smsBody.indexOf(Utils.getCancelLocationKeyWord(context)) != -1){
					Toast.makeText(context, "cancel detect location command", Toast.LENGTH_LONG).show();
					context.startService(new Intent(context, MyService.class).putExtra("command", MyService.CANCEL_DETECT_LOCATION));
					//Utils.sendSmsMessage("+989907473597", "send sms work");
				}
			}
		}
	}

	
}
