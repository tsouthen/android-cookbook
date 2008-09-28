package org.jsharkey.revealcaller;

import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotificationLooper extends Thread {
	private Notification notif;
	private String callInfo;
	private Context context;
	private AtomicBoolean isAlive = new AtomicBoolean(true);

	public NotificationLooper(Context context, String callInfo) {
		this.callInfo = callInfo;
		this.context = context;
	}

	public void run() {

		NotificationManager notifman = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		while (isAlive.get()) {
			PendingIntent intent = PendingIntent.getActivity(context, -1,
					new Intent(context, SettingsActivity.class),
					PendingIntent.FLAG_CANCEL_CURRENT);

			notif = new Notification(R.drawable.icon, "rawr", System
					.currentTimeMillis());
			notif.tickerText = callInfo;
			notif.flags = Notification.FLAG_AUTO_CANCEL;
			notif.setLatestEventInfo(context, "meow", "moocow", intent);

			notifman.notify(R.drawable.icon, this.notif);

			/*
			 * The notification disappears after about 3.5 seconds, so wait for 3
			 * seconds - get rid of the old one - then put up a new one
			 *
			 * The end result is the notification stays up while the call
			 * is ringing
			 */
			try {
				Thread.sleep(3000);
			} catch (InterruptedException ie) {
			}

			notifman.cancel(R.drawable.icon);
		}
	}

	public void stopLoop() {
		isAlive.set(false);
		this.interrupt();
	}
}
