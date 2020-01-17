package alistar.app.timeline;

public class Moment
{
	
	private int id;
	private String note, emoji;
	private long date;
	
	public Moment(int id, String note, String emoji, long date)
	{
		this.id = id;
		this.note = note;
		this.emoji = emoji;
		this.date = date;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public int getId()
	{
		return id;
	}

	public void setNote(String note)
	{
		this.note = note;
	}
	
	public String getNote()
	{
		return note;
	}
	
	public void setEmoji(String emoji)
	{
		this.emoji = emoji;
	}
	
	public String getEmoji()
	{
		return this.emoji;
	}
	
	public void setDate(long date)
	{
		this.date = date;
	}
	
	public long getDate()
	{
		return date;
	}
}
