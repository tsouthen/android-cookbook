package org.jsharkey.revealcaller;

import java.util.HashMap;
import java.util.Map;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Contacts;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;


public class RevealService extends Service {
	
	public final static String TAG = RevealService.class.toString();
	
	protected Map states = new HashMap();
	
	protected NumberDatabase numberdb;

	protected TelephonyManager teleman;
	protected NotificationManager notifman;
	
	protected PhoneStateListener listener = new PhoneStateListener() {
		
		public void onCallStateChanged(int state, String incomingNumber) {
			
			Log.d(TAG, String.format("onCallStateChanged(state=%d, incomingNumber=%s)", state, incomingNumber));

			switch(state) {
			case TelephonyManager.CALL_STATE_RINGING:
				startRinging(incomingNumber);
				break;
			
			case TelephonyManager.CALL_STATE_IDLE:
			case TelephonyManager.CALL_STATE_OFFHOOK:
				stopRinging();
				break;
			
			}
			
		}
		
	};
	
	public void startRinging(String incoming) {
		// create a notification to help tell user about who is calling
		// first perform lookup against database
		
		String nice = this.numberdb.searchNumber(incoming);
		
		// show our information several times
		for(int i = 0; i < 8; i++) {
			
			PendingIntent intent = PendingIntent.getActivity(this, -1, new Intent(this, SettingsActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);
	
			this.notif = new Notification(R.drawable.icon, "rawr", System.currentTimeMillis());
			this.notif.tickerText = nice;
			this.notif.flags = Notification.FLAG_AUTO_CANCEL;
			this.notif.setLatestEventInfo(this, "meow", "moocow", intent);
	
			this.notifman.notify(i, this.notif);
			
		}

	}
	
	
	public void stopRinging() {
		
		// hide any shown notifications
		for(int i = 0; i < 8; i++) {
			this.notifman.cancel(i);
		}
		
		
	}
	
	protected static final int NOTIF_ID = 42;
	protected Notification notif;
	
	@Override
	public void onCreate() {
		Log.d(TAG, "creating RevealService");
		
		// open number database
		this.numberdb = new NumberDatabase(this);
		
		this.teleman = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		this.notifman = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
		
        teleman.listen(this.listener, PhoneStateListener.LISTEN_CALL_STATE);
        

		
	}
	
	
	

	@Override
	public void onDestroy() {
		
		// unsubscribe from telephone updates
		this.teleman.listen(listener, PhoneStateListener.LISTEN_NONE);
		
	}
	

	public class RevealBinder extends Binder {
		RevealService getService() {
			return RevealService.this;
		}
	}

	private final IBinder binder = new RevealBinder();

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}


	

}
