package com.example.findroidsettings;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class MainActivity extends Activity implements OnCheckedChangeListener {

	private EditText et;
	static final String TAG = "MainActivity";
	static final int ACTIVATION_REQUEST = 47; // identifies our request id
	DevicePolicyManager devicePolicyManager;
	ComponentName demoDeviceAdmin;
	ToggleButton toggleButton;
	CheckBox chlock, chwipe;
	String m;
	int ci, position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		toggleButton = (ToggleButton) super.findViewById(R.id.widget33);
		toggleButton.setOnCheckedChangeListener(this);

		chlock = (CheckBox) findViewById(R.id.checkBox1);
		chwipe = (CheckBox) findViewById(R.id.checkBox2);

		et = (EditText) findViewById(R.id.editText1);

		// Initialize Device Policy Manager service and our receiver class
		devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		demoDeviceAdmin = new ComponentName(this, DemoDeviceAdminReceiver.class);

		SharedPreferences settings = getSharedPreferences("MYPREFS", 0);
		et.setText(settings.getString("tvalue", "1234"));

		chlock.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (chlock.isChecked()) {


					SharedPreferences lock = getSharedPreferences("MYPREFS", 0);
					SharedPreferences.Editor editor = lock.edit();
					editor.putString("locking", "1");

					chlock.setText("lock enabled");
				} else {
					SharedPreferences lock = getSharedPreferences("MYPREFS", 0);
					SharedPreferences.Editor editor = lock.edit();
					editor.putString("locking", "0");
					chlock.setText("lock disabled");
				}
			}
		});

		chwipe.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (chwipe.isChecked()) {

					SharedPreferences wipe = getSharedPreferences("MYPREFS", 0);
					SharedPreferences.Editor editor = wipe.edit();
					editor.putString("wiping", "1");

					chwipe.setText("wipe enabled");
				} else {
					SharedPreferences wipe = getSharedPreferences("MYPREFS", 0);
					SharedPreferences.Editor editor = wipe.edit();
					editor.putString("wiping", "0");
					chwipe.setText("wipe disabled");
				}
			}
		});

	}

	public void onClickSave(View v) {

		SharedPreferences settings = getSharedPreferences("MYPREFS",
				Context.MODE_WORLD_READABLE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("tvalue", et.getText().toString());
		editor.commit();

		Context con = null;
		try {
			con = createPackageContext("com.example.findroid",
					MODE_WORLD_WRITEABLE);
			SharedPreferences settingss = con.getSharedPreferences("MYPREFS",
					Context.MODE_WORLD_WRITEABLE);
			SharedPreferences.Editor editorr = settingss.edit();
			editorr.putString("tvalue", et.getText().toString());
			editorr.commit();
		} catch (NameNotFoundException e) {
			Log.e("Not data shared", e.toString());
		}
		Toast.makeText(getBaseContext(), settings.getString("tvalue", ""),
				Toast.LENGTH_SHORT).show();

		// ---display file saved message---
		Toast.makeText(getBaseContext(), "result saved successfully!",
				Toast.LENGTH_SHORT).show();

	}

	public void onClickMoreInfo(View v) {

		Toast.makeText(this, "Now you can download the base app \"FINDROID\"",
				Toast.LENGTH_LONG).show();

		Uri uriinbox = Uri.parse("content://sms/inbox");
		ContentResolver cr = getContentResolver();
		Cursor c = cr.query(uriinbox, null, null, null, null);
		c.moveToFirst();

		do {
			m = "";
			ci = 0;

			for (int idx = 0; idx < c.getColumnCount(); idx++) {
				m += " " + c.getColumnName(idx) + ":" + c.getString(idx);

			}

			if (m.contains("Latitude") == true) {

				Intent i = new Intent(this, ShowMap.class);

				i.putExtra("msg", m);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
				
			}
		} while (c.moveToNext());
		c.close();
	}

	public void onClickExit(View v) {
		finish();
		moveTaskToBack(true);
	}

	public void onCheckedChanged(CompoundButton button, boolean isChecked) {
		if (isChecked) {
			// Activate device administration
			Intent intent = new Intent(
					DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
					demoDeviceAdmin);
			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
					"You will be able to do these things");
			startActivityForResult(intent, ACTIVATION_REQUEST);
		}
		Log.d(TAG, "onCheckedChanged to: " + isChecked);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ACTIVATION_REQUEST:
			if (resultCode == Activity.RESULT_OK) {
				Log.i(TAG, "Administration enabled!");
				toggleButton.setChecked(true);
			} else {
				Log.i(TAG, "Administration enable FAILED!");
				toggleButton.setChecked(false);
			}
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
