package alistar.app.alarm;
import android.content.*;

public class Alarm
{
	public static final String TYPE_WAKEUP = "Ali, Time to wake up";
	public static final String TYPE_WORK = "It's work time Ali";
	public static final String TYPE_SLEEP = "Sleep time";
	public static final String TYPE_FLAG = "Bring down the flag";
	public long timeMillis;
	public String type;
	public boolean active;
	public SharedPreferences sets;
	
	public Alarm(Context context)
	{
		sets = context.getSharedPreferences("sets", 0);
		timeMillis = sets.getLong("alarm_time", 0);
		type = sets.getString("alarm_type", TYPE_WAKEUP);
		active = sets.getBoolean("alarm_active", false);
	}
	
	public long getTimeMillis()
	{
		return this.timeMillis;
	}
	
	public String getType()
	{
		return this.type;
	}
	
	public boolean isAlarmActive()
	{
		return this.active;
	}
	
	public void setTimeMillis(long l)
	{
		sets.edit().putLong("alarm_time", l).commit();
		this.timeMillis = l;
	}
	
	public void setType(String t)
	{
		sets.edit().putString("alarm_type", t).commit();
		this.type = t;
	}
	
	public void setActive(boolean a)
	{
		sets.edit().putBoolean("alarm_active", a).commit();
		this.active = a;
	}
	
	
}
