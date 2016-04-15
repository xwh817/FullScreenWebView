package com.turingcat.webview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootBroadcastReceiver extends BroadcastReceiver {
	static final String action_boot = "android.intent.action.BOOT_COMPLETED";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(action_boot)){
			/*Intent ootStartIntent=new Intent(context, MainActivity.class);
			ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(ootStartIntent);*/
			

			Log.i("test", action_boot);
			
			Intent service = new Intent(context, HeartBeatService.class);
			context.startService(service);
	        
		}
	}
}