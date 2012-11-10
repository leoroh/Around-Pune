package com.gdgpune.aroundpune;

import java.util.Iterator;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;

/***
 * This is extended from Map Activity class this class adds mapview ,overlays &
 * show current location
 * 
 * @author Rohit Lagu
 * 
 */
public class MapPlaces extends MapActivity {

	/**
	 * map controller set zoom level, types of maps
	 */
	private MapController mapController;
	/**
	 * overlay for showing blue white animated dot of current location
	 */
	private MyLocationOverlay myLocation;
	/**
	 * showing map tiles & overlay items on screen
	 */
	private MapView mapView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_places);

		// Get Mapping Controllers etc
		initMapView();

		addMyLocationOverlay();

		Places places = (Places) getIntent().getSerializableExtra("Places");
		Drawable drawable = this.getResources().getDrawable(R.drawable.pin);
		PlacesOverlay itemizedoverlay = new PlacesOverlay(drawable, mapView);

		for (@SuppressWarnings("rawtypes")
		Iterator iterator = places.getResults().iterator(); iterator.hasNext();) {
			Results results = (Results) iterator.next();
			GeoPoint point = getPoint(results.getGeometry().getLocation()
					.getLat(), results.getGeometry().getLocation().getLng());
			OverlayItem overlayitem = new OverlayItem(point, results.getName(),
					results.getVicinity());
			itemizedoverlay.addOverlay(overlayitem);
		}

		mapView.getOverlays().add(itemizedoverlay);

	}

	/**
	 * Initialize Map view by setting map view controller & zoom buttons
	 */
	private void initMapView() {

		mapView = (MapView) findViewById(R.id.map_view);
		mapController = mapView.getController();
		mapController.setZoom(15);
		mapView.setBuiltInZoomControls(true);

	}

	/**
	 * Adding current location on map view
	 */
	private void addMyLocationOverlay() {
		myLocation = new MyLocationOverlay(this, mapView);
		mapView.getOverlays().add(myLocation);

		myLocation.enableMyLocation();

		myLocation.runOnFirstFix(new Runnable() {
			@Override
			public void run() {
				mapController.animateTo(myLocation.getMyLocation());
			}
		});

	}

	/**
	 * converts point in latitude & longitude to geopoints
	 * 
	 * @param lat
	 *            latitude
	 * @param lon
	 *            longitude
	 * @return GeoPoint stored as integer numbers of microdegrees
	 */
	private GeoPoint getPoint(double lat, double lon) {
		return (new GeoPoint((int) (lat * 1000000.0), (int) (lon * 1000000.0)));
	}

	@Override
	protected void onResume() {
		super.onResume();
		myLocation.enableMyLocation();
		myLocation.enableCompass();
	}

	@Override
	protected void onPause() {
		super.onPause();
		myLocation.enableCompass();
		myLocation.disableMyLocation();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}