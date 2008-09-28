package org.jsharkey.revealcaller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SettingsActivity extends Activity {

	public final static String TAG = SettingsActivity.class.toString();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// TODO: this should be a SettingsActivity similar to the behavior of task switcher setup app
		// TODO: look at amarokremote for example use of settingsactivity

		// the revealservice might also perform a simple curl+regex against reverse phonebook page:
		// http://www.google.com/search?hl=en&pb=r&q=2188795293
		// disclaimer: i have no clue who this random person is rofl

		// TODO: CITE DATA SOURCES
		// http://www.wcisd.hpc.mil/~phil/npanxx/
		// http://www.bennetyee.org/ucsd-pages/area.html



		NumberDatabase numberdb = new NumberDatabase(this);

		// answers should be cloquet, denver, seattle
		for(String number : new String[] { "2188790000", "3035736236", "2062858646" }) {
			Log.d(TAG, String.format("%s=%s", number, numberdb.searchNumber(number)));
		}


		// start the background service nicely
		startService(new Intent(SettingsActivity.this, RevealService.class));

	}


}
