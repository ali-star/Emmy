package alistar.app.map;

import alistar.app.R;
import alistar.app.Utils;
import alistar.app.brain.Memory;
import alistar.app.map.mapnavigator.Navigator;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.readystatesoftware.notificationlog.Log;
import java.util.HashMap;
import java.util.List;


public class SaraMap extends FragmentActivity implements OnMapLongClickListener, LocationListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks
{

	@Override
	public void onLocationChanged ( Location p1 )
	{
		// TODO: Implement this method
		if(firstLocation == null)
			firstLocation = new LatLng(p1.getLatitude(), p1.getLongitude());
		if(lockLocation)
			updateCameraBearing(map, myLocation.getBearing(), p1);
		myLocation = p1;
		if(selectedMarker != null & firstLocation != null)
		{
			Navigator nav = new Navigator(map,selectedMarker.getLatLng(), firstLocation);
			nav.setPathColor(Color.parseColor("#6994FD"), Color.GREEN, Color.RED);
			nav.setPathBorderColor(Color.parseColor("#000000"), Color.parseColor("#000000"), Color.parseColor("#000000"));
			nav.setPathLineWidth(6);
			nav.findDirections(true);
		}
		/*Log.i("Accuracy", "Accuracy: " + p1.getAccuracy());
		 MapMarker nearbyMarker = utils.getNearbyPlace(places, new LatLng(p1.getLatitude(), p1.getLongitude()));
		 if(nearbyMarker != null)
		 {
		 Log.d("nearby", "nearby place:" + nearbyMarker.getName());
		 }
		 else
		 Log.d("nearby", "no nearby place");*/
	}
	
	
	GoogleMap map;
	LocationRequest mLocationRequest;
	GoogleApiClient mGoogleApiClient;

	@Override
	public void onMapReady(GoogleMap p1)
	{
		// TODO: Implement this method
		map = p1;
		initMap();
	}

	private synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this).addApi(LocationServices.API)
            .build();
	}

	@Override
	public void onConnectionSuspended(int i) {
		Log.d("TAG", "Connection to Google API suspended");

	}

	@Override
	public void onConnected(Bundle bundle) {
		
		LocationRequest mLocationRequest = LocationRequest.create()
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
			.setInterval(2000)
			.setFastestInterval(2000);
		LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
																 mLocationRequest, (LocationListener) this);

	}
	

	private Utils utils;
	private Memory memory;
	private MainMapFragment mapFragment;
	private HashMap<Marker, MapMarker> markersMap;
	private HashMap<Marker, Circle> circlesMap;
	private List<MapMarker> places;
	private Location myLocation;
	private LatLng firstLocation;
	private MapMarker selectedMarker;
	private boolean lockLocation = false;

	@Override
	public void onMapLongClick(final LatLng p1)
	{
		// TODO: Implement this method
		final MarkerDialog md = new MarkerDialog(SaraMap.this);
		md.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		md.show();
		md.save.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					// TODO: Implement this method
					MapMarker result = utils.saveMarker(md.name.getText().toString(), p1, md.description.getText().toString(), Double.valueOf(md.distance.getText().toString()), md.alart.isChecked(), md.star.isChecked());
					if (result != null)
					{
						Marker marker = addPlace(result);
						Circle circle = addCircle(result);
						markersMap.put(marker, result);
						circlesMap.put(marker, circle);
						md.dismiss();
					}
					else
						Toast.makeText(SaraMap.this, "Select Name", Toast.LENGTH_LONG).show();



				}


			});


	}

	@Override

	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);

		setContentView(R.layout.map_activity);

		mapFragment = new MainMapFragment();
		
		FragmentTransaction ft = getFragmentManager().beginTransaction();

		ft.add(R.id.map, mapFragment);

		ft.commit();
		
		buildGoogleApiClient();

		mGoogleApiClient.connect();
		
		mapFragment.getMapAsync(this);

		utils = utils.getInstance(this);
		
		findViewById(R.id.search_btn).setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick ( View p1 )
				{
					// TODO: Implement this method
					lockLocation = true;
				}
				
			
		});

	}

	protected void initMap()
	{
		mapFragment.setMap(map);
	
		mapFragment.setOnMapClickListener(new OnMapClickListener()
			{

				@Override
				public void onMapClick ( LatLng p1 )
				{
					Log.d("tag", "distance:" + utils.getDistanceOnMap(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()), p1));
					MapMarker nearbyMarker = utils.getNearbyPlace(places, p1);
					if(nearbyMarker != null)
					{
						Log.d("nearby", "nearby place:" + nearbyMarker.getName());
					}
					else
						Log.d("nearby", "no nearby place");
				}
				
			
		});
		mapFragment.setOnMapLongClickListener(this);
		mapFragment.showLocation(true);
		mapFragment.getMap().setMapType(GoogleMap.MAP_TYPE_HYBRID);

		markersMap = new HashMap<Marker, MapMarker>();
		circlesMap = new HashMap<Marker, Circle>();

		//eventMarkerMap.put(firstMarker, firstEventInfo);

		memory = new Memory(this);
		places = memory.getPlaces();
		for (int i=0; i < places.size(); i++)
		{
			Marker marker = addPlace(places.get(i));
			markersMap.put(marker, places.get(i));
			circlesMap.put(marker, addCircle(places.get(i)));

		}

		mapFragment.getMap().setOnInfoWindowClickListener(new OnInfoWindowClickListener() {



				@Override

				public void onInfoWindowClick(final Marker marker)
				{
					final MapMarker mapMarker = markersMap.get(marker);
					//deletePlace(marker);
					final MarkerDialog md = new MarkerDialog(SaraMap.this);
					md.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
					md.show();
					md.dialogTitle.setText("Edite Marker");
					md.name.setText(mapMarker.getName());
					md.description.setText(mapMarker.getDescription());
					md.distance.setText(String.valueOf(mapMarker.getDistance()));
					md.star.setChecked(mapMarker.isStared());
					md.alart.setChecked(mapMarker.getAlart());
					md.save.setOnClickListener(new OnClickListener()
						{

							@Override
							public void onClick(View p1)
							{
								// TODO: Implement this method
								mapMarker.update(md.name.getText().toString(), mapMarker.getLatLng(), md.description.getText().toString(), Double.valueOf(md.distance.getText().toString()), md.alart.isChecked(), md.star.isChecked());
								memory.updatePlace(mapMarker);
								updatePlace(marker, mapMarker);
								md.dismiss();
							}


						});
					md.delete.setVisibility(View.VISIBLE);
					md.delete.setOnClickListener(new OnClickListener()
						{

							@Override
							public void onClick(View p1)
							{
								// TODO: Implement this method
								deletePlace(marker);
								md.dismiss();
							}


						});
				}

			});
		try
		{
		if ( !getIntent().getExtras().isEmpty() )
		{
			switch (getIntent().getExtras().getString("command"))
			{
				case "zoom_marker":
					selectedMarker = memory.findMapMarkerById(getIntent().getExtras().getInt("marker_id"));
					map.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedMarker.getLatLng(), 17));
					break;
			}
		}
		}
		catch ( Exception e )
		{}

	}

	private Marker addPlace(MapMarker marker)
	{
		return mapFragment.placeMarker(marker);
	}

	private Circle addCircle(MapMarker marker)
	{
		Circle c = mapFragment.getMap().addCircle(new CircleOptions()
												   .center(marker.getLatLng())
												   .radius(Double.valueOf(marker.getDistance()))
												   .strokeColor(Color.parseColor("#906E6FA7"))
												   .fillColor(Color.parseColor("#30448AFF"))
												   .strokeWidth(3));

		return c;
	}

	public void deletePlace(Marker marker)
	{
		marker.remove();
		circlesMap.get(marker).remove();
		memory.deletePlace(markersMap.get(marker).getId());
	}

	public void updatePlace(Marker marker, MapMarker mapMarker)
	{
		marker.remove();
		circlesMap.get(marker).remove();
		markersMap.remove(marker);
		circlesMap.remove(marker);
		Marker newMarker = addPlace(mapMarker);
		Circle circle = addCircle(mapMarker);
		markersMap.put(newMarker, mapMarker);
		circlesMap.put(newMarker, circle);
	}
	
	private void updateCameraBearing(GoogleMap googleMap, float mBearing, Location newLocation) {
		if ( googleMap == null) return;
		float bearing = newLocation.bearingTo(myLocation);
		CameraPosition cameraPosition = new CameraPosition.Builder()
            .target(new LatLng(myLocation.getLatitude(),
							   myLocation.getLongitude())).bearing(bearing + 530)
            .tilt(45).zoom(17).build();

		map.animateCamera(
            CameraUpdateFactory.newCameraPosition(cameraPosition), 1,
            null);
	}

}
