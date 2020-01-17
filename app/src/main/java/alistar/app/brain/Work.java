package alistar.app.brain;

public class Work
{
	private long id;
	private String command, workStartText, workEndText, type;
	private long date;
	private int status = 0;
	private int priority = 0;
	private boolean confirmed = false;
	private WorkName work;
	private WorkListener workListener;
	public static final int PRIORITY_MIN = 0;
	public static final int PRIORITY_LOW = 1;
	public static final int PRIORITY_MED = 2;
	public static final int PRIORITY_HIG = 3;
	public static final int PRIORITY_MAX = 4;
	public static final int STATUS_PENDING = 0;
	public static final int STATUS_ONGOING = 1;
	public static final int STATUS_DONE = 2;
	
	public enum WorkName
	{
		OPEN_SARA_MAIN_VIEW, OPEN_LIST, FIND_ME, VOICE_RECORD, ASK_FEELING
	}
	
	public Work ( long id, WorkName work )
	{
		this.id = id;
		this.date = id;
		this.work = work;
	}
	
	public interface WorkListener
	{
		public void onStartListener();
		public void onFinishListener();
	}
	
	public void setId(long id)
	{
		this.id = id;
	}
	
	public long getId()
	{
		return this.id;
	}
	
	public void setCommand(String command)
	{
		this.command = command;
	}
	
	public String getCommand()
	{
		return this.command;
	}
	
	public void setStartText ( String text )
	{
		this.workStartText = text;
	}

	public String getStartText ( )
	{
		return this.workStartText;
	}
	
	public void setEndText ( String text )
	{
		this.workEndText = text;
	}

	public String getEndText ( )
	{
		return this.workEndText;
	}
	
	public void setType ( String type )
	{
		this.type = type;
	}
	
	public String getType ( )
	{
		return this.type;
	}
	
	public void setDate ( long date )
	{
		this.date = date;
	}
	
	public long getDate ( )
	{
		return this.date;
	}
	
	public void setStatus ( int status )
	{
		this.status = status;
		if ( workListener == null )
			return;
		if ( status == STATUS_ONGOING )
			workListener.onStartListener();
		if ( status == STATUS_DONE )
			workListener.onFinishListener();
	}
	
	public int getStatus ( )
	{
		return this.status;
	}
	
	public void setPriority ( int priority )
	{
		this.priority = priority;
	}
	
	public int getPriority ( )
	{
		return this.priority;
	}
	
	public void setWork ( WorkName work )
	{
		this.work = work;
	}
	
	public WorkName getWork ( )
	{
		return this.work;
	}
	
	public void confirmed ( )
	{
		this.confirmed = true;
	}
	
	public boolean isConfirmed ( )
	{
		return this.confirmed;
	}
	
	public void setListener ( WorkListener wl )
	{
		this.workListener = wl;
	}
}
