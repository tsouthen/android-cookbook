package org.jsharkey.revealcaller;

import java.util.HashMap;
import java.util.Map;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;


public class RevealService extends Service {
	public final static String TAG = RevealService.class.toString();

	protected Map states = new HashMap();

	protected NumberDatabase numberdb;

	protected TelephonyManager teleman;
	private NotificationLooper noteLoop;

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
		if (nice == null)
			return;

		if (noteLoop != null)
			stopRinging();
		noteLoop = new NotificationLooper(this, nice);
		noteLoop.start();
	}


	public void stopRinging() {
		if (noteLoop == null)
			return;

		noteLoop.stopLoop();
	}

	protected static final int NOTIF_ID = 42;
	protected Notification notif;

	@Override
	public void onCreate() {
		Log.d(TAG, "creating RevealService");

		// open number database
		this.numberdb = new NumberDatabase(this);

		this.teleman = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);

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
