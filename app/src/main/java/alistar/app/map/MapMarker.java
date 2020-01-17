package alistar.app.map;
import com.google.android.gms.maps.model.*;

public class MapMarker
{
	private int id = -1;
	private String name, decription;
	private LatLng latLng;
	private double distance, accuracy = -1;
	private boolean alart, star;
	
	public MapMarker()
	{
		
	}
	
	public MapMarker(int id, String name, LatLng p1, String description, double distance, boolean alart, boolean star)
	{
		this.id = id;
		this.name = name;
		this.latLng = p1;
		this.distance = distance;
		this.decription = description;
		this.alart = alart;
		this.star = star;
	}
	
	public void update(String name, LatLng p1, String description, double distance, boolean alart, boolean star)
	{
		this.name = name;
		this.latLng = p1;
		this.distance = distance;
		this.decription = description;
		this.alart = alart;
		this.star = star;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setLatLng(LatLng l)
	{
		this.latLng = l;
	}
	
	public LatLng getLatLng()
	{
		return this.latLng;
	}
	
	public void setDistance(double distance)
	{
		this.distance = distance;
	}
	
	public double getDistance()
	{
		return this.distance;
	}
	
	public void setDescription(String d)
	{
		this.decription = d;
	}
	
	public String getDescription()
	{
		return this.decription;
	}
	
	public void setAlart(boolean alart)
	{
		this.alart = alart;
	}
	
	public boolean getAlart()
	{
		return this.alart;
	}
	
	public void setStar(boolean star)
	{
		this.star = star;
	}
	
	public boolean isStared()
	{
		return this.star;
	}
	
	public void setAccuracy(double accuracy)
	{
		this.accuracy = accuracy;
	}
	
	public double getAccuracy()
	{
		return this.accuracy;
	}
}
