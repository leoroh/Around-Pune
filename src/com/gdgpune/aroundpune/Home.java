package com.gdgpune.aroundpune;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class Home extends Activity {

	/**
	 * GridView for creating UI
	 */
	private GridView gridview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_home);

		gridview = (GridView) findViewById(R.id.gridView);

		gridview.setAdapter(new GridAdapter());

		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (isNeworkAvaliable()) {
					new PlacesSearchAsyncTask(Home.this)
							.execute(((GridAdapter) (arg0.getAdapter())).value[arg2]);
				} else {
					showNoCoverageDialog();
				}

			}
		});

	}

	/***
	 * This class works as adapter to populate data. Image & Textview are added
	 * to Grid View
	 * 
	 * @author Rohit Lagu
	 * 
	 * @version 1.0
	 * 
	 * @see <a
	 *      href="http://developer.android.com/guide/topics/ui/layout/gridview.html">GridView
	 *      Adapter</a>
	 * 
	 * 
	 * 
	 */
	private class GridAdapter extends BaseAdapter {

		/**
		 * Array of item images
		 */
		private final Integer[] mImage = { R.drawable.bank, R.drawable.movie,
				R.drawable.coffee, R.drawable.beer };
		/**
		 * Array of item name
		 */
		public final String[] name = { "Bank ATM", "Theater", "Cafe",
				"Club/Bar" };
		/**
		 * Array of values need to pass in AsyncTask
		 */
		public final String[] value = { "bank|atm", "movie_theater", "cafe",
				"night_club|bar" };

		@Override
		public int getCount() {
			return mImage.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View gridItem;

			LayoutInflater inflater = (LayoutInflater) Home.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			gridItem = inflater.inflate(R.layout.griditem, null);

			((TextView) gridItem.findViewById(R.id.textView1))
					.setText(name[position]);

			((ImageView) gridItem.findViewById(R.id.imageView1))
					.setImageResource(mImage[position]);

			return gridItem;
		}

	}

	/**
	 * Check Network Connectivity is available. This method require android
	 * permission in maifest.permission.ACCESS_NETWORK_STATE
	 */
	private boolean isNeworkAvaliable() {

		boolean connected = false;

		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) this
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo networkInfo = connectivityManager
					.getActiveNetworkInfo();
			connected = (networkInfo != null && networkInfo.isAvailable() && networkInfo
					.isConnected());
			return connected;

		} catch (Exception e) {
			Log.d("Class", e.toString());
		}
		return connected;
	}

	/**
	 * If Connection is not available show dialog with two option. Option 1: go
	 * to Setting & enable Network Connection Option 2: close the progress
	 * Dialog.
	 */
	private void showNoCoverageDialog() {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Connection Failure");
		alertDialog
				.setMessage("You need a network connection to use this application. Please turn on mobile network or Wi-Fi in settings.");
		alertDialog.setIcon(android.R.drawable.stat_sys_warning);
		alertDialog.setCancelable(false);
		alertDialog.setButton2("Setting",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						((Activity) Home.this).startActivityForResult(
								new Intent(Settings.ACTION_WIRELESS_SETTINGS),
								1);
					}
				});
		alertDialog.setButton("cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		alertDialog.show();
	}
}
