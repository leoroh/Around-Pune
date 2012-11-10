package com.gdgpune.aroundpune;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

/***
 * This is AsyncTask Which send request to google places server. Json Response
 * is converted to java objects by google gson library
 * 
 * @author Rohit Lagu
 * @version 1.0
 */
public class PlacesSearchAsyncTask extends AsyncTask<String, Void, Places> {

	private final String TAG = "PlacesSearchAsyncTasks";
	private final String URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=18.5236,73.8478&radius=20000&types=%s&sensor=false&key=AIzaSyAsD-x2yDdaI_dBGu-2wPc3wyXeE8Gz4HU";

	private Context ctx = null;
	private ProgressDialog progressDialog = null;

	public PlacesSearchAsyncTask(Context context) {
		ctx = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = new ProgressDialog(ctx);
		progressDialog.setMessage("Seraching Places...");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
	}

	@Override
	protected Places doInBackground(String... params) {
		Places result = null;

		try {
			if (null == params[0]) {
				throw new IllegalArgumentException("parameter is req");
			}

			URL url = new URL(String.format(URL, params[0]));
			Log.d(TAG, url.toString());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			result = readStream(con.getInputStream());
		} catch (Exception e) {
			Log.d(TAG, "In doInBackground " + e.toString());
		}

		return result;
	}

	@Override
	protected void onPostExecute(Places result) {
		super.onPostExecute(result);
		if (progressDialog.isShowing()) {
			if (result != null) {
				if (result.getStatus() != null
						&& result.getStatus().equalsIgnoreCase("ok")) {
					Intent intent = new Intent(ctx, MapPlaces.class);
					intent.putExtra("Places", result);
					ctx.startActivity(intent);
				}
			}
			progressDialog.dismiss();
		}
	}

	/**
	 * Read input stream from response & create Java Object
	 * 
	 * @param in
	 *            InputStream
	 * @return Places Object containing parsed data from google API
	 */
	private Places readStream(InputStream in) {
		BufferedReader reader = null;
		Places places = null;

		try {

			reader = new BufferedReader(new InputStreamReader(in));
			final Gson gson = new Gson();
			places = gson.fromJson(reader, Places.class);

		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					Log.d(TAG, "In readStream " + e.toString());
				}
			}
		}
		return places;
	}

}
