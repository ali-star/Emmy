package alistar.app;
import com.google.android.gms.maps.model.*;

public class EventInfo
{
	private LatLng latLng;
	private String info;
	
	public EventInfo(LatLng latLng, String info)
	{
		this.latLng = latLng;
		this.info = info;
	}
	
	public LatLng getLatLng(){
		return this.latLng;
	}
	
	public String getInfo()
	{
		return this.info;
	}
}
