package org.jsharkey.revealcaller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;

public class NumberDatabase {

	public final static String TAG = NumberDatabase.class.toString();

	public final static int ENTRYLEN = 11;

	protected final Context context;
	protected Map<String,String> states = new HashMap<String,String>();


	public NumberDatabase(Context context) {
		this.context = context;
		this.fillStates();

	}

	public String searchNumber(String number) {

		// try searching for nice string for incoming call number
		number = number.replaceAll("[^0-9]", "");
		if (number.length() == 11) {
			//eg 1 555 555 5555, we want to get rid of the 1
			number = number.substring(0,number.length());
		}
		if (number.length() != 10) {
			Log.w(TAG, "the incoming number is not valid");
			return null;
		}


		int prefix = Integer.parseInt(number.substring(0, 3)),
			exchange = Integer.parseInt(number.substring(3, 6));

		String niceState = this.tryState(prefix);
		String niceDetailed = this.tryDetailed(prefix, exchange);

		return (niceDetailed != null) ? niceDetailed : niceState;

	}

	protected String tryState(int prefix) {

		// try finding this prefix in our rough state-level database
		String sprefix = Integer.toString(prefix);
		if(!this.states.containsKey(sprefix)) return null;

		return String.format("Call from %s", this.states.get(sprefix));

	}

	protected String tryDetailed(int prefix, int exchange) {

		// decide which raw file to open
		int prefix1 = prefix - (prefix % 100);
		InputStream is = this.openStream(prefix1);
		if(is == null) return null;

		// try scanning into file correct amount
		int first = (prefix % 100) * 900;
		int second = exchange - 100;

		int scanin = (first + second) * ENTRYLEN;

		Log.d(TAG, String.format("first=%d, second=%d, total=%d, scanin=%d", first, second, first+second,scanin));

		String raw = null;

		try {
			byte[] entry = new byte[ENTRYLEN];
			is.skip(scanin);
			is.read(entry, 0, ENTRYLEN);
			raw = new String(entry).trim();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(raw == null) return null;
		if(raw.length() < 4) return null;

		// format LSTCity
		String type = raw.substring(0, 1);
		String state = raw.substring(1, 3);
		String city = raw.substring(3);

		return String.format("%s from %s, %s", (type == "W") ? "Cellular" : "Landline", city, state);

	}

	protected InputStream openStream(int prefix1) {
		Log.d(TAG, String.format("prefix1=%d", prefix1));
		switch(prefix1) {
		case 200: return context.getResources().openRawResource(R.raw.area200);
		case 300: return context.getResources().openRawResource(R.raw.area300);
		case 400: return context.getResources().openRawResource(R.raw.area400);
		case 500: return context.getResources().openRawResource(R.raw.area500);
		case 600: return context.getResources().openRawResource(R.raw.area600);
		case 700: return context.getResources().openRawResource(R.raw.area700);
		case 800: return context.getResources().openRawResource(R.raw.area800);
		case 900: return context.getResources().openRawResource(R.raw.area900);
		}
		return null;
	}


	protected void fillStates() {
        states.put("201", "New Jersey");
		states.put("202", "D.C.");
		states.put("203", "Connecticut");
		states.put("205", "Alabama");
		states.put("206", "Washington");
		states.put("207", "Maine");
		states.put("208", "Idaho");
		states.put("209", "California");
		states.put("210", "Texas");
		states.put("212", "New York");
		states.put("213", "California");
		states.put("214", "Texas");
		states.put("215", "Pennsylvania");
		states.put("216", "Ohio");
		states.put("217", "Illinois");
		states.put("218", "Minnesota");
		states.put("219", "Indiana");
		states.put("224", "Illinois");
		states.put("225", "Louisiana");
		states.put("228", "Mississippi");
		states.put("229", "Georgia");
		states.put("231", "Michigan");
		states.put("234", "Ohio");
		states.put("236", "Virginia");
		states.put("239", "Florida");
		states.put("240", "Maryland");
		states.put("248", "Michigan");
		states.put("251", "Alabama");
		states.put("252", "North Carolina");
		states.put("253", "Washington");
		states.put("254", "Texas");
		states.put("256", "Alabama");
		states.put("260", "Indiana");
		states.put("262", "Wisconsin");
		states.put("267", "Pennsylvania");
		states.put("269", "Michigan");
		states.put("270", "Kentucky");
		states.put("276", "Virginia");
		states.put("278", "Michigan");
		states.put("281", "Texas");
		states.put("283", "Ohio");
		states.put("301", "Maryland");
		states.put("302", "Delaware");
		states.put("303", "Colorado");
		states.put("304", "West Virginia");
		states.put("305", "Florida");
		states.put("307", "Wyoming");
		states.put("308", "Nebraska");
		states.put("309", "Illinois");
		states.put("310", "California");
		states.put("312", "Illinois");
		states.put("313", "Michigan");
		states.put("314", "Missouri");
		states.put("315", "New York");
		states.put("316", "Kansas");
		states.put("317", "Indiana");
		states.put("318", "Louisiana");
		states.put("319", "Iowa");
		states.put("320", "Minnesota");
		states.put("321", "Florida");
		states.put("323", "California");
		states.put("325", "Texas");
		states.put("330", "Ohio");
		states.put("331", "Illinois");
		states.put("334", "Alabama");
		states.put("336", "North Carolina");
		states.put("337", "Louisiana");
		states.put("339", "Massachusetts");
		states.put("341", "California");
		states.put("347", "New York");
		states.put("351", "Massachusetts");
		states.put("352", "Florida");
		states.put("360", "Washington");
		states.put("361", "Texas");
		states.put("369", "California");
		states.put("380", "Ohio");
		states.put("385", "Utah");
		states.put("386", "Florida");
		states.put("401", "Rhode Island");
		states.put("402", "Nebraska");
		states.put("404", "Georgia");
		states.put("405", "Oklahoma");
		states.put("406", "Montana");
		states.put("407", "Florida");
		states.put("408", "California");
		states.put("409", "Texas");
		states.put("410", "Maryland");
		states.put("412", "Pennsylvania");
		states.put("413", "Massachusetts");
		states.put("414", "Wisconsin");
		states.put("415", "California");
		states.put("417", "Missouri");
		states.put("419", "Ohio");
		states.put("423", "Tennessee");
		states.put("424", "California");
		states.put("425", "Washington");
		states.put("430", "Texas");
		states.put("432", "Texas");
		states.put("434", "Virginia");
		states.put("435", "Utah");
		states.put("440", "Ohio");
		states.put("442", "California");
		states.put("443", "Maryland");
		states.put("464", "Illinois");
		states.put("469", "Texas");
		states.put("470", "Georgia");
		states.put("475", "Connecticut");
		states.put("478", "Georgia");
		states.put("479", "Arkansas");
		states.put("480", "Arizona");
		states.put("484", "Pennsylvania");
		states.put("501", "Arkansas");
		states.put("502", "Kentucky");
		states.put("503", "Oregon");
		states.put("504", "Louisiana");
		states.put("505", "New Mexico");
		states.put("507", "Minnesota");
		states.put("508", "Massachusetts");
		states.put("509", "Washington");
		states.put("510", "California");
		states.put("512", "Texas");
		states.put("513", "Ohio");
		states.put("515", "Iowa");
		states.put("516", "New York");
		states.put("517", "Michigan");
		states.put("518", "New York");
		states.put("520", "Arizona");
		states.put("530", "California");
		states.put("540", "Virginia");
		states.put("541", "Oregon");
		states.put("551", "New Jersey");
		states.put("557", "Missouri");
		states.put("559", "California");
		states.put("561", "Florida");
		states.put("562", "California");
		states.put("563", "Iowa");
		states.put("564", "Washington");
		states.put("567", "Ohio");
		states.put("570", "Pennsylvania");
		states.put("571", "Virginia");
		states.put("573", "Missouri");
		states.put("574", "Indiana");
		states.put("575", "New Mexico");
		states.put("580", "Oklahoma");
		states.put("585", "New York");
		states.put("586", "Michigan");
		states.put("601", "Mississippi");
		states.put("602", "Arizona");
		states.put("603", "New Hampshire");
		states.put("605", "South Dakota");
		states.put("606", "Kentucky");
		states.put("607", "New York");
		states.put("608", "Wisconsin");
		states.put("609", "New Jersey");
		states.put("610", "Pennsylvania");
		states.put("612", "Minnesota");
		states.put("614", "Ohio");
		states.put("615", "Tennessee");
		states.put("616", "Michigan");
		states.put("617", "Massachusetts");
		states.put("618", "Illinois");
		states.put("619", "California");
		states.put("620", "Kansas");
		states.put("623", "Arizona");
		states.put("626", "California");
		states.put("627", "California");
		states.put("628", "California");
		states.put("630", "Illinois");
		states.put("631", "New York");
		states.put("636", "Missouri");
		states.put("641", "Iowa");
		states.put("646", "New York");
		states.put("650", "California");
		states.put("651", "Minnesota");
		states.put("660", "Missouri");
		states.put("661", "California");
		states.put("662", "Mississippi");
		states.put("669", "California");
		states.put("678", "Georgia");
		states.put("679", "Michigan");
		states.put("682", "Texas");
		states.put("689", "Florida");
		states.put("701", "North Dakota");
		states.put("702", "Nevada");
		states.put("703", "Virginia");
		states.put("704", "North Carolina");
		states.put("706", "Georgia");
		states.put("707", "California");
		states.put("708", "Illinois");
		states.put("712", "Iowa");
		states.put("713", "Texas");
		states.put("714", "California");
		states.put("715", "Wisconsin");
		states.put("716", "New York");
		states.put("717", "Pennsylvania");
		states.put("718", "New York");
		states.put("719", "Colorado");
		states.put("720", "Colorado");
		states.put("724", "Pennsylvania");
		states.put("727", "Florida");
		states.put("731", "Tennessee");
		states.put("732", "New Jersey");
		states.put("734", "Michigan");
		states.put("737", "Texas");
		states.put("740", "Ohio");
		states.put("747", "California");
		states.put("754", "Florida");
		states.put("757", "Virginia");
		states.put("760", "California");
		states.put("762", "Georgia");
		states.put("763", "Minnesota");
		states.put("764", "California");
		states.put("765", "Indiana");
		states.put("769", "Mississippi");
		states.put("770", "Georgia");
		states.put("772", "Florida");
		states.put("773", "Illinois");
		states.put("774", "Massachusetts");
		states.put("775", "Nevada");
		states.put("779", "Illinois");
		states.put("781", "Massachusetts");
		states.put("785", "Kansas");
		states.put("786", "Florida");
		states.put("801", "Utah");
		states.put("802", "Vermont");
		states.put("803", "South Carolina");
		states.put("804", "Virginia");
		states.put("805", "California");
		states.put("806", "Texas");
		states.put("808", "Hawaii");
		states.put("810", "Michigan");
		states.put("812", "Indiana");
		states.put("813", "Florida");
		states.put("814", "Pennsylvania");
		states.put("815", "Illinois");
		states.put("816", "Missouri");
		states.put("817", "Texas");
		states.put("818", "California");
		states.put("828", "North Carolina");
		states.put("830", "Texas");
		states.put("831", "California");
		states.put("832", "Texas");
		states.put("835", "Pennsylvania");
		states.put("843", "South Carolina");
		states.put("845", "New York");
		states.put("847", "Illinois");
		states.put("848", "New Jersey");
		states.put("850", "Florida");
		states.put("856", "New Jersey");
		states.put("857", "Massachusetts");
		states.put("858", "California");
		states.put("859", "Kentucky");
		states.put("860", "Connecticut");
		states.put("862", "New Jersey");
		states.put("863", "Florida");
		states.put("864", "South Carolina");
		states.put("865", "Tennessee");
		states.put("870", "Arkansas");
		states.put("872", "Illinois");
		states.put("878", "Pennsylvania");
		states.put("901", "Tennessee");
		states.put("903", "Texas");
		states.put("904", "Florida");
		states.put("906", "Michigan");
		states.put("907", "Alaska");
		states.put("908", "New Jersey");
		states.put("909", "California");
		states.put("910", "North Carolina");
		states.put("912", "Georgia");
		states.put("913", "Kansas");
		states.put("914", "New York");
		states.put("915", "Texas");
		states.put("916", "California");
		states.put("917", "New York");
		states.put("918", "Oklahoma");
		states.put("919", "North Carolina");
		states.put("920", "Wisconsin");
		states.put("925", "California");
		states.put("927", "Florida");
		states.put("928", "Arizona");
		states.put("931", "Tennessee");
		states.put("935", "California");
		states.put("936", "Texas");
		states.put("937", "Ohio");
		states.put("940", "Texas");
		states.put("941", "Florida");
		states.put("947", "Michigan");
		states.put("949", "California");
		states.put("951", "California");
		states.put("952", "Minnesota");
		states.put("954", "Florida");
		states.put("956", "Texas");
		states.put("957", "New Mexico");
		states.put("959", "Connecticut");
		states.put("970", "Colorado");
		states.put("971", "Oregon");
		states.put("972", "Texas");
		states.put("973", "New Jersey");
		states.put("975", "Missouri");
		states.put("978", "Massachusetts");
		states.put("979", "Texas");
		states.put("980", "North Carolina");
		states.put("984", "North Carolina");
		states.put("985", "Louisiana");
		states.put("989", "Michigan");

	}

}
