package alistar.app.map;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.*;
import alistar.app.map.*;
import alistar.app.R;

public class MainMapFragment extends MapFragment
 {
	 
	 private GoogleMap map;

	public void setMap(GoogleMap m)
	{
		map = m;
	}

	public Marker placeMarker(MapMarker mm) {
		
		Marker m  = getMap().addMarker(new MarkerOptions()

									   .position(mm.getLatLng())

									   .title(mm.getName()));



		return m;

	}
	
	public void setOnMapClickListener(OnMapClickListener omcl)
	{
		getMap().setOnMapClickListener(omcl);
	}
	
	public void setOnMapLongClickListener(OnMapLongClickListener omlcl)
	{
		getMap().setOnMapLongClickListener(omlcl);
	}
	
	public void showLocation(boolean b)
	{
		getMap().setMyLocationEnabled(b);
	}
	
	public GoogleMap getMap()
	{
		return map;
	}

}
