package com.example.findroid;

import com.example.findroid.Server;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MainActivity extends BroadcastReceiver {
	@Override
	public void onReceive(Context ctxt, Intent intent) {
		Bundle bndl = intent.getExtras();
		if (bndl != null) {
			ctxt.startService(new Intent(ctxt, Server.class));
		}
	}
}
