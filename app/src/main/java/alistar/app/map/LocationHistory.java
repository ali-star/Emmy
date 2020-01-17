package alistar.app.map;

public class LocationHistory
{
	private int id, mapMarkerId = -1;
	private double latitude, longtude;
	private long date;
	
	public LocationHistory(int id, int mapMarkerId, double latitude, double longtude, long date)
	{
		this.id = id;
		this.mapMarkerId = mapMarkerId;
		this.latitude = latitude;
		this.longtude = longtude;
		this.date = date;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public void setMarkerId(int mapMarkerId)
	{
		this.mapMarkerId = mapMarkerId;
	}
	
	public int getMapMarkerId()
	{
		return this.mapMarkerId;
	}
	
	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}
	
	public double getLatitude()
	{
		return this.latitude;
	}
	
	public void setLongtude(double longtude)
	{
		this.longtude = longtude;
	}
	
	public double getLongtude()
	{
		return this.longtude;
	}
	
	public void setDate(long date)
	{
		this.date = date;
	}
	
	public long getDate()
	{
		return this.date;
	}
}
