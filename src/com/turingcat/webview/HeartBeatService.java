package com.turingcat.webview;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class HeartBeatService extends Service {

	private static final int heart_length = 120000;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private Handler handler_heartbeat = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			try {
				ActivityManager aManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
				ComponentName cName = aManager.getRunningTasks(1).get(0).topActivity;

				// 如果不是当前应用
				Context context = getApplicationContext();
				if (!context.getPackageName().equals(cName.getPackageName())) {
					Intent intent = new Intent();
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setClass(context, MainActivity.class);
					context.startActivity(intent);

					Log.i("test", "startActivity");

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			handler_heartbeat.sendEmptyMessageDelayed(0, heart_length);

			Log.i("test", "handler_heartbeat send Message");

		};
	};

	@Override
	public void onCreate() {

		Log.i("test", "HeartBeatService onCreate");

		handler_heartbeat.sendEmptyMessageDelayed(0, heart_length);

	};

	@Override
	public void onDestroy() {
		super.onDestroy();

		Log.i("test", "HeartBeatService onDestroy");

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("test", "HeartBeatService onStartCommand");
		return START_STICKY; // service被kill掉后自动
	}

}
