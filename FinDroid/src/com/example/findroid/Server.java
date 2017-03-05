package com.example.findroid;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class Server extends Service {
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		Intent i = new Intent(this, FindLocation.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
		return START_STICKY; // We want this service to continue running until
								// it is explicitly stopped, so return sticky.
	}

	@Override
	// Called when the service is stopped using stopService().
	// This is where you clean up the resources used by your service.
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
	}

}
