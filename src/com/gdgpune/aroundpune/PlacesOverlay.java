package com.gdgpune.aroundpune;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

/***
 * This is custom Overlay class which stores all map overlay points
 * 
 * @author Rohit Lagu
 * @version 1.0
 * @see <a href="http://jgilfelt.github.com/android-mapviewballoons/">Balloon
 *      Overlay library</a>
 */
public class PlacesOverlay extends ItemizedOverlay<OverlayItem> {

	private final ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

	public PlacesOverlay(Drawable defaultMarker, MapView mapview) {
		super(boundCenter(defaultMarker));
	}

	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return mOverlays.size();
	}

	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

}
