package alistar.app.brain;

public class Emotion
{
	
	private int id, feeling;
	private String note, emoji;
	private long date;
	private boolean isAnomaly;
	private boolean dayFirstRecord;
	
	public Emotion(int id, int feeling, long date, String note, String emoji)
	{
		this.id = id;
		this.feeling = feeling;
		this.date = date;
		this.note = note;
		this.emoji = emoji;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public int getFeeling()
	{
		return this.feeling;
	}
	
	public long getDate()
	{
		return this.date;
	}
	
	public String getNote()
	{
		return this.note;
	}
	
	public String getEmoji()
	{
		return this.emoji;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public void setFeelinhg(int feeling)
	{
		this.feeling = feeling;
	}
	
	public void setNote(String note)
	{
		this.note = note;
	}
	
	public void setEmoji(String emoji)
	{
		this.emoji = emoji;
	}

	public boolean isAnomaly() {
		return isAnomaly;
	}

	public void setAnomaly(boolean anomaly) {
		isAnomaly = anomaly;
	}

	public boolean isDayFirstRecord() {
		return dayFirstRecord;
	}

	public void setDayFirstRecord(boolean dayFirstRecord) {
		this.dayFirstRecord = dayFirstRecord;
	}
}
