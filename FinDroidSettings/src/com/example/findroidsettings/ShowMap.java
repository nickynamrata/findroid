package com.example.findroidsettings;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.renderscript.Double2;
import android.widget.Toast;

public class ShowMap extends Activity {

	private GoogleMap googleMap;
	static String mesg;
	Double latitude;
	Double longitude;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
	
		Bundle bundle = getIntent().getExtras();
		String mesg = bundle.getString("msg");
		String lat = mesg.substring(184, 195);
		String lon = mesg.substring(207, 217);

		 Toast.makeText(this, "Latitude:"+lat, Toast.LENGTH_LONG).show();
		 Toast.makeText(this, "Longitude"+lon, Toast.LENGTH_LONG).show();

		latitude = Double.parseDouble(lat);
		longitude = Double.parseDouble(lon);

		try {
			// Loading map
			// initilizeMap();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	@SuppressLint("NewApi")
	protected void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			} else {
				// latitude and longitude
				// create marker
				MarkerOptions marker = new MarkerOptions().position(
						new LatLng(latitude, longitude)).title("Hello Maps ");

				// Changing marker icon
				// adding marker
				googleMap.addMarker(marker);

				CameraPosition cameraPosition = new CameraPosition.Builder()
						.target(new LatLng(17.385044, 78.486671)).zoom(12)
						.build();
				googleMap.animateCamera(CameraUpdateFactory
						.newCameraPosition(cameraPosition));

			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
	}

}
