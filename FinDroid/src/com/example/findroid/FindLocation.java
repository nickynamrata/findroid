package com.example.findroid;

import com.example.findroid.R;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class FindLocation extends Activity implements LocationListener {
	
	private static final int ACTIVATION_REQUEST = 47;
	DevicePolicyManager devicePolicyManager;
	private LocationManager mgr;
	private TextView output1;
	private TextView output2;
	private String best;

	String m;
	String mobileno;
	int ci, position;
	TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		output1 = (TextView) findViewById(R.id.textView1);
		output2 = (TextView) findViewById(R.id.textView2);
		Criteria criteria = new Criteria();
		criteria.setCostAllowed(true);
		criteria.setAltitudeRequired(false);
		best = mgr.getBestProvider(criteria, true);
		Location location = mgr.getLastKnownLocation(best);
		mgr.requestLocationUpdates(best, 35000, 1, this);

		Uri uriinbox = Uri.parse("content://sms/inbox");
		ContentResolver cr = getContentResolver();
		Cursor c = cr.query(uriinbox, null, null, null, null);
		c.moveToFirst();

		do {
			m = "";
			ci = 0;
			tv = (TextView) findViewById(R.id.textView6);
			for (int idx = 0; idx < c.getColumnCount(); idx++) {
				m += " " + c.getColumnName(idx) + ":" + c.getString(idx);

			}

			Context con = null;
			try {
				con = createPackageContext("com.example.findroid", 0);
				SharedPreferences pref = con.getSharedPreferences("MYPREFS", 0);
				String data = pref.getString("tvalue", "No Value");
				Toast.makeText(this, data, Toast.LENGTH_LONG).show();
				

			} catch (NameNotFoundException e) {
				Log.e("Not data shared", e.toString());
			}

			if (m.contains("1234") == true) {
				// Target mobile no. is traced out....
				position = m.indexOf("address:+");
				mobileno = m.substring(position + 11, position + 21);
				tv.setText(mobileno);
				// Add here to send SMS containing latitude and longitude....
				Toast.makeText(this, "in if block", Toast.LENGTH_LONG).show();
				dumpLocation(location);
			}
			if (m.contains("Latitude") == true) {
				@SuppressWarnings("deprecation")
				SharedPreferences locinfo = getSharedPreferences("MYPREFSL",
						Context.MODE_WORLD_READABLE);
				SharedPreferences.Editor editor = locinfo.edit();
				editor.putString("lvalue", m);
				editor.commit();

				Intent i;
				PackageManager manager = getPackageManager();
				try {
					i = manager
							.getLaunchIntentForPackage("com.example.findroidsettings");
					if (i == null)
						throw new PackageManager.NameNotFoundException();
					i.addCategory(Intent.CATEGORY_LAUNCHER);
					startActivity(i);
				} catch (PackageManager.NameNotFoundException e) {

				}
			}
		} while (c.moveToNext());
		c.close();
	}

	private void dumpLocation(Location location) {

		try {

			if (location == null) {
				output1.setText("Unknown");
				output2.setText("Unknown");

			} else {
				output1.setText(String.valueOf(location.getLatitude()));
				output2.setText(String.valueOf(location.getLongitude()));
				SmsManager sms = SmsManager.getDefault();
				sms.sendTextMessage(
						mobileno,
						null,
						"Latitude:" + String.valueOf(location.getLatitude())
								+ "   Longitude:"
								+ String.valueOf(location.getLongitude()),
						null, null);
				
				
				SharedPreferences lock = getSharedPreferences("MYPREFS", MODE_PRIVATE);
				String dataReturnedlock = lock.getString("locking", "");
				Toast.makeText(this, dataReturnedlock, Toast.LENGTH_SHORT).show();
				
				SharedPreferences wipe = getSharedPreferences("MYPREFS", MODE_PRIVATE);
				String dataReturnedwipe = wipe.getString("wiping", "");
				Toast.makeText(this, dataReturnedwipe, Toast.LENGTH_SHORT).show();
				
				if(dataReturnedlock.equals("1"))
				devicePolicyManager.lockNow();
					
				
				if(dataReturnedwipe.equals("1"))
				devicePolicyManager.wipeData(ACTIVATION_REQUEST);
					
			}
		} catch (Exception e) {
			Toast.makeText(this, "Message send is failed...",
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mgr.removeUpdates(this);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mgr.requestLocationUpdates(best, 35000, 1, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

}
