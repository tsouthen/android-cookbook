/*
	Copyright (C) 2008 Jeffrey Sharkey, http://jsharkey.org/
	
	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.
	
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.jsharkey.grouphome;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

public class LauncherActivity extends ExpandableListActivity implements OnCreateContextMenuListener, OnClickListener {
	
	public static final String TAG = LauncherActivity.class.toString();
	
	//public static final String JSON_GROUP = "{'Apps: Communication': ['com.android.contacts','com.android.mms','com.android.browser','com.android.im','com.android.email','com.google.android.gm'],'Apps: Multimedia': ['com.android.camera','com.android.music','com.amazon.mp3','com.google.android.youtube'],'Apps: Travel': ['com.google.android.apps.maps'],'Apps: Tools': ['com.android.alarmclock','com.android.calculator2','com.android.voicedialer','com.android.vending','com.android.settings'],'Apps: Productivity':['com.android.calendar','com.android.todo','com.android.notepad'],'Apps: Demo': ['com.example.android.apis','com.android.development']}";
	public static final String JSON_GROUP = "{'Apps: Lifestyle': ['us.cirion.wheeler', 'com.csi.android.MyMedBox', 'com.android.xmas_tree', 'com.android.pedometer', 'com.speedymarks.android.calories', 'net.lifeaware', 'com.android.drums', 'ulzii.android.todoremember', 'com.android.lt.caloriescalculator', 'info.androidz.horoscope', 'com.android.demo.notepad2', 'frogarmy.pray', 'com.ricket.doug.nightclock', 'com.android.FatCalculator10', 'com.roundhill.androidWP', 'org.icount.beer', 'com.android.bartender', 'com.teedroid.caddy.android', 'net.jeremye.android.mrliver', 'com.digitalbias.android', 'Unyverse.android', 'com.navee.android.freefamilywatch', 'anvillar.rt', 'com.skycoders.quote', 'android.GolfTracks', 'leanCalc.leanCalc', 'com.google.android.noisealert', 'com.boorah.android', 'com.fatsecret.android', 'com.gasolin.android.gbmi', 'com.gasolin.android.abmi', 'com.wsl.CardioTrainer', 'com.highwaynorth.jogtracker', 'jerboid.eco2go', 'com.smartcapsules.cooking.android.taster', 'com.myclo.android.mycloset'], 'Apps: News & Weather': ['com.atlantistech.android.tideapp', 'com.pureinnovation.purerss', 'com.grss.ui', 'net.yesiltas.ascore', 'thinlet.moon', 'com.aws.android', 'thinlet.forecast', 'com.accuweather.android', 'com.codeshogun.android.nprstationnearyou', 'com.androidforums.election', 'com.ebessette.android.greadernotifier', 'com.weathertopconsulting.handwx', 'com.android.weather', 'org.anddev.andweather.apk', 'com.android.tmg', 'com.weather.Weather'], 'Games: Cards & Casino': ['com.fawepark.android.barcodebeasties', 'com.kmagic.solitaire', 'com.tyler.th', 'jp.hudson.android.reversi', 'jp.hudson.android.klondike', 'jp.hudson.android.blackjack'], 'Games: Casual': ['com.androidcan.connect4', 'com.FindIt', 'com.es.android', 'com.magmasoftware', 'com.kb.andriod.bubblewrap', 'com.jamoes.txtspeed', 'com.diota.android.taptick', 'com.eanixlive.speed', 'de.joergjahnke.c64.android', 'com.odesys.bgmon', 'com.google.android.divideandconquer', 'de.joergjahnke.gameboy.android', 'com.silvermoon.client', 'com.maplekeycompany.games.poppoppopcorn', 'com.applimobile.android.rotomem', 'com.gameloft.bubblebash', 'com.zelfi.android.joyity', 'com.example.amazed', 'com.mattwach.trap2', 'com.alfray.asqare', 'com.glu.android.bonsai'], 'Games: Brain & Puzzle': ['com.vlm.client', 'com.androidcan.asudoku', 'com.g1port.PreTetris', 'tx.android.biolines', 'posimotion.Tic_Tac_Toe', 'com.google.android.checkers', 'com.demo.FourNumGuess', 'brad.android.TileGame', 'com.applimobile.powervocab', 'org.censorednet.android.MathPractice', 'org.hermit.netscramble', 'se.jompe.gaming', 'net.healeys.lexic', 'com.android.lt.tictactoe', 'com.google.code.twisty', 'com.google.android.reversi', 'com.google.android.chess', 'com.android.cocc', 'artfulbits.aiMinesweeper', 'com.bytescape.tetroid', 'com.hanoi', 'com.r2.MemCuad', 'com.android.miniMatcher', 'de.joergjahnke.mobilesudoku', 'com.mark.andguess', 'com.dynamix.mobile.SmartTacToe', 'com.icenta.sudoku.ui', 'com.sporksoft.slidepuzzle', 'com.google.android.sokoban', 'name.boyle.chris.sgtpuzzles', 'girlsskip.app', 'com.stelluxstudios.Android.Sudoku', 'com.nitoware.mahjongg', 'net.vgart.sokodroid', 'com.mgm.jumpy', 'mobi.dreamware.chessclock', 'com.google.marvin.androidsays', 'com.tapjoy.coloroid', 'org.anddev.andsudoku.apk', 'com.jamoes.lightsout', 'com.tapjoy.mismismatch', 'com.glu.android.bgd'], 'Apps: Finance': ['com.visa.android.GUI', 'com.speedymarks.android.rates', 'com.quirkconsulting.ticker', 'com.johnlauricella.mymoney1', 'jg.finance.tipjar', 'com.shareprice.app.android', 'com.android.gTip', 'net.gumbercules.loot', 'com.wsol.android.app', 'com.gbizapps.calc', 'com.threeaspen.merchant', 'com.highrf.stockapp', 'com.rawthought.herenow', 'com.tf', 'com.mayosmith.InflationMaster', 'org.fogproject.andriod.AndBankBook', 'com.twofuse.stocker', 'com.calculator.mortgage', 'com.blau.android.gmoney', 'app.money.firewallet', 'com.evancharlton.mileage', 'com.android.budget', 'net.thauvin.erik.android.tiproid', 'com.a0soft.gphone.aCurrency', 'jg.finance.moneydroid', 'com.quirkconsulting', 'org.anddev.andtip.apk', 'com.mpagano.geztip', 'com.techmethods.mileage', 'masterofmuppets.tipcalc', 'com.google.android.bistromath', 'com.savingadvice.tip_calculator', 'com.infonow.bofa'], 'Apps: Productivity': ['com.android.calendar','com.android.todo','com.android.notepad','com.connvision.mobileaccessor.android', 'com.android.ussdbal', 'thinlet.dof', 'net.ser1.timetracker', 'org.openintents.filemanager', 'e2m.android', 'org.mmin.handycalc', 'org.openintents.timesheet', 'com.android.lt.todolist', 'com.jdm.qSearch', 'com.android123.aRecorder', 'com.westtek.pdfviewer', 'com.android123.aBook', 'thinlet.worldclock', 'com.widgetop.android', 'thinlet.salat', 'org.openintents.countdown', 'com.google.android.wikinotes', 'com.omniture.android.dasboard.viewer', 'Unyverse.pro', 'com.akproduction.notepad', 'com.junobe.android.smsbook', 'com.maplekeycompany.apps.bgcal', 'com.fognl.android.ringcontrol', 'thinlet.twilight', 'net.thauvin.erik.android.googsms', 'com.splashid', 'com.twofuse.droidrecord', 'com.poidio.pTextEdit', 'com.nextmobileweb.dialzero', 'com.votereport.android', 'androidin.aReader', 'org.thismetalsky.calCOOLator', 'org.openintents.notepad', 'com.android.todo', 'org.openintents.news', 'com.iambic.android.tipper', 'com.iambic.android.googhelper', 'com.tom.medical.obdating', 'ch.tea.android.cardslight', 'com.funambol.android', 'com.google.android.quicklist'], 'Apps: Shopping': ['com.metaworldsolutions.android', 'com.bonfiremedia.android_ebay', 'com.glam.glammobile', 'com.ap.WootChecker', 'com.codeshogun.dealsdroid', 'com.google.code.p.localspinner', 'com.navee.android.isearch', 'com.biggu.shopsavvy', 'com.afarine.android.ashopper', 'be.jameswilliams.android.pleasebrowseme', 'org.openintents.shopping', 'com.yellowbook.android2', 'com.google.android.housing', 'com.google.zxing.client.android', 'com.compareeverywhere'], 'Apps: Communication': ['com.android.contacts','com.android.mms','com.android.browser','com.android.im','com.android.email','com.google.android.gm','com.starobject.android.starcontact', 'com.danapple.email', 'com.metakall.android.wallet', 'com.here_test.android', 'com.indiabolbol.hookup', 'com.cabildo.callingcard', 'com.blau.android.away', 'com.kolbysoft.steel', 'com.here.android', 'com.fsck.k9', 'com.hullomail.android.messaging', 'com.p1.chompsms', 'com.dattasmoon.aTweeter', 'com.dattasmoon.andFBChat', 'com.evancharlton.g1central', 'org.microemu.android.Browser', 'com.phonefusion.voicemailplus.android', 'net.locationmessenger.android', 'com.acode', 'com.phonalyzr', 'org.dubmenow.dub.activities', 'org.dotphone.android7.a7email', 'android.gpsmail', 'com.byarger.exchangeit', 'org.dotphone.android7', 'com.blau.android.quickcut', 'com.meebo', 'com.voxofon', 'com.blau.android.screenon', 'com.android', 'com.twitli.android.twitter', 'com.iskoot.android', 'android.mailchat', 'com.twidroid', 'com.multiplefacets.messenger', 'org.connectbot', 'de.shapeservices.implus'], 'Apps: Travel': ['com.google.android.apps.maps','org.kevinslist.android.caltrain', 'com.android.droidgap', 'com.fawepark.android.gridreference', 'org.andnav', 'com.speedymarks.android.translator', 'letufindme.com', 'au.com.jtribe.pinpoint', 'com.accutracking', 'com.clinkybot.parkmark', 'com.android.gastrip', 'com.xirgonium.android', 'com.wikitude', 'com.android.mission4u.iTubeRide', 'com.a0soft.gphone.aDialCode', 'au.com.jtribe.firepin', 'com.cab4me.android', 'com.deafcode.android.Orienteer', 'com.langtolang', 'com.lpontier.marvin', 'com.breadcrumbz'], 'Apps: Entertainment': ['com.plusmo.collegebasketballscores', 'com.bbm.android.cowbell', 'com.plusmo.soccerscores', 'eu.spvsoft.android.snowglobe', 'com.adrink', 'com.ambrosoft.nytflix', 'com.textonphone.activity', 'com.justnutz', 'com.android.basiccamera1', 'thefrenchpoet.android', 'com.android.wallswitch1', 'com.softwareforme.Life', 'com.speedymarks.android.hockey', 'org.pmix.ui', 'com.speedymarks.android.bball', 'com.plusmo.probasketballscores', 'com.speedymarks.android.football', 'com.zweder.sunlight', 'mobi.androidtunes', 'com.mobilefootie.fotmob', 'com.consumerdevices.magicball.android', 'com.android.leet.noise', 'com.stylem.wallpapers', 'com.bpb', 'com.plusmo.profootball', 'com.google.code.p.slideunlocker', 'mobi.androidwallpapers.wallpaperoid', 'com.google.code.p.mariosimulator', 'com.magic8ball', 'com.stylem.movies', 'com.netmite.andme', 'com.capaci.android.flashlight', 'com.android.krystleii', 'com.nextmobileweb.phoneflix', 'com.gamelion.globalfactbook', 'com.android.geobrowse', 'com.plusmo.collegefootball', 'com.stylem.greetings', 'com.truecountry', 'com.altitude', 'com.hho', 'com.esmusica', 'com.android.lolcat'], 'Apps: Software libraries': ['com.google.tts', 'org.openintents.convertcsv', 'org.openintents.voicenotes', 'jp.android_group.artoolkit', 'com.google.android.radar'], 'Apps: Reference': ['com.starobject.android.stardico', 'com.waterflea.chordchart', 'com.speedymarks.android.thesaurus', 'com.simonjudge.aspell', 'com.darkdesign.constitution', 'com.google.android.stardroid', 'com.simpletools.android.mc.main', 'com.cadreworks.bible', 'com.agilemedicine.BloodGas', 'com.simonjudge.wordnet', 'com.traxel.wethepeople', 'com.acrodesign.acrobiblelite', 'com.nextmobileweb.quickpedia', 'net.thauvin.erik.android.spellit', 'net.veveo.ui.android', 'com.agilemedicine.medsearch', 'com.Idealab.android.WikiDici', 'org.freedictionary', 'com.webascender.callerid', 'hongbo.wordmate', 'com.bonfiremedia.android_wikimobile'], 'Apps: Tools': ['com.android.alarmclock','com.android.calculator2','com.android.voicedialer','com.android.vending','com.android.settings','com.r2d2.QWifi', 'com.google.android.diskusage', 'com.alexis.portknocking', 'com.schwimmer.android.togglegps', 'com.codetastrophe.cellfinder', 'de.anno.android.missedCall', 'androidin.androidWhere', 'org.paulmach.textedit', 'com.samir.compactreader', 'com.schwimmer.android.togglebluetooth', 'my.flipclock', 'com.apksoftware.compass', 'com.schwimmer.android.togglewifi', 'com.noamwolf.android.androidfound', 'com.brilaps.android.txtract', 'com.speedymarks.android.speedometer', 'com.eyeonweb.acount', 'net.jaqpot.netcounter', 'com.iq_mobile.iqlight', 'droidsans.android.DroidSansTweakToolsLite', 'com.ap.DroidFtp', 'com.waterflea.wifiscan', 'com.android.MPGCalc', 'com.zicam', 'com.msync.gui', 'com.demo.YesNo', 'com.agilus.pachube', 'koushikdutta.screenshot', 'com.google.android.netmeter', 'com.tokasiki.android.phonerecorder', 'droidsans.android.DroidSansTweakTools', 'com.rerware.android.MyBackup', 'net.laserbunny.android.otp', 'com.android123.aSettings', 'koushikdutta.superuser', 'com.adamrocker.android.input.simeji', 'com.zmj.sms', 'com.example.android', 'com.clinkybot.geodroid', 'org.texteasy', 'com.codethought.android.androidconvert', 'com.timers', 'com.eyeonweb.pacer', 'com.noamwolf.android', 'org.greenmileage', 'com.handcent.sender', 'jp.chai.android.JSandBox', 'com.droidWake.app', 'com.mathinpublic.lengthConveter1001', 'com.archanet.serverup', 'com.android.apkinstaller', 'com.rcreations.dscalarmmonitor', 'com.acme.android.powermanager', 'com.zweder', 'com.ap.StopTimer', 'lysesoft.bucketupload', 'us.lindanrandy.cidrcalculator', 'org.openintents.updatechecker', 'com.android.crypt.barada', 'com.rerware.android.MyBookmarks', 'com.mshare.chinese', 'letufindme.OscanO.UI', 'com.google.android.polyglotz', 'com.sphericbox.syb', 'com.swwomm.ringtoggle', 'net.gasbot', 'com.lindaandny.lindamanager', 'org.dotphone.android.softkey', 'com.android.cm3', 'simplecode.fft.test.no.mic.one.dot.zero', 'com.johnlauricella.mytrends', 'com.android.flashlight', 'koushikdutta.klaxon', 'com.factory_h.owner', 'com.boundaryremainder.android', 'com.tokasiki.android.voicerecorder', 'com.bmsstopwatch', 'koushikdutta.telnet', 'android.closedid', 'org.efalk.rpncalc', 'net.everythingandroid.timer', 'com.skwid.systemmonitor', 'com.speedymarks.android.start', 'com.android.term', 'com.android.stopwatch', 'com.android.bacc', 'com.rcreations.ipcamviewer', 'com.metago.bender', 'bz.ktk.bubble', 'com.xtremelabs.android.speedtest', 'edu.mit.locale', 'com.mmg.appin', 'com.wrike.contactssync', 'com.androidnerds.utilities.Glance', 'com.bitsetters.android.passwordsafe', 'com.traxel.calcstra', 'com.cooolmagic.android.toggle', 'com.example.android.notepad', 'com.beust.android.translate', 'org.moellers.lucas', 'org.openintents.flashlight', 'org.ravelin.android', 'com.and.Calc', 'com.mshare.gui', 'com.android.utcclock', 'com.milk.realprog', 'com.teneke.flashlight', 'com.xcr.android.inetwork', 'com.poidio.ServiceViewer', 'com.tokasiki.android.dialcall', 'com.instamapper.gpstracker', 'org.dyndns.devesh.flashlight', 'com.antbs.android', 'com.mobeegal.android', 'com.android.taskswitcher', 'com.angryredplanet.android.rings_extended', 'org.curiouscreature.android.shutterspeed', 'com.tmobile.wifi', 'com.appdroid.anycut'], 'Apps: Multimedia': ['com.android.camera','com.android.music','com.amazon.mp3','com.google.android.youtube','com.mp1.livo', 'com.streamfurious.android.free', 'com.sass.andrum', 'net.jjc1138.android.scrobbler', 'com.tokasiki.android.shuffleplay', 'com.antbs.antplayer', 'com.android123.aPlayer', 'com.giantrabbit.nagare', 'com.rcptones.downloader', 'souvey.musical', 'lamp.lime.sand', 'org.iii.ro.iiivpa', 'com.multiplefacets.pictorial', 'com.snoggdoggler.android.doggcatcher', 'com.deafcode.android.Cinema', 'mypack.intents', 'larry.zou.colorfullife', 'hsware.HSTempo', 'com.ringermobile.app', 'vOICe.vOICe', 'com.pixelpipe.android', 'com.ajaxie.lastfm', 'com.fingerpainters.fingerpaint', 'com.mmg.playsvideo', 'com.ewebcomputing', 'org.tunescontrol', 'marcone.toddlerlock', 'org.gmote.client.android', 'com.ringdroid', 'com.appdroid.videoplayer', 'org.openintents.splashplay', 'com.google.android.panoramio', 'com.google.android.photostream', 'com.imeem.gynoid', 'com.shinycore.picsayfree', 'com.shazam.android', 'com.tunewiki.lyricplayer.android'], 'Apps: Demo': ['com.example.android.apis','com.android.development','com.qualcomm.qx.neocore', 'com.android.level', 'org.example.translate', 'org.jnikfarjam.gameoflife', 'com.concretesoftware.caloriecounter', 'com.alfray.mandelbrot'], 'Games: Arcade & Action': ['com.zingball', 'com.gamevil.bs08', 'com.gamevil.pow', 'pbf.android.animation.first', 'org.hermit.tiltlander', 'com.piggybank.tntmaniac', 'de.joergjahnke.mobileinvaders', 'android.com.abb', 'org.allbinary', 'org.two11me.apps.ZombiePoliticians.Debug', 'com.punchometer', 'org.allbinary.game.minispacewar.vector', 'net.peterd.zombierun', 'org.allbinary.game.santasworldwar', 'org.allbinary.game.zeptoracer', 'org.allbinary.game.minispacewar', 'net.rbgrn.lightracer', 'com.example.android.lunarlander', 'com.example.android.snake', 'com.google.clickin2dabeat', 'com.NamcoNetworks.PacMan'], 'Apps: Social': ['au.com.jtribe.shoutr', 'com.google.android.nextfolder', 'com.loopt', 'net.jjc1138.android.twitter', 'com.ubikod.android.ubikim.buddymob', 'com.abwaters.pingdroid', 'letufindme.com.UI', 'com.globalmotion.wherester', 'com.nextmobileweb.fbook', 'com.tomgibara.android.pintail', 'com.myspace.android', 'com.googlecode.statusinator', 'com.dytara.eljay', 'com.wertago', 'com.eventr', 'com.placelnk']}";
	
	class EntryInfo {
		ResolveInfo resolveInfo;
		CharSequence title;
		Drawable thumb;
	}
	
	// try resolving group for given package name
	// we should try caching this information for later speed-up
	protected String resolveGroup(JSONObject groupMap, String packageName) {
		try {
			for(Iterator keys = groupMap.keys(); keys.hasNext(); ) {
				String groupName = (String)keys.next();
				JSONArray packages = groupMap.getJSONArray(groupName);
				for(int i = 0; i < packages.length(); i++) {
					if(packageName.equals(packages.getString(i)))
						return groupName;
				}
			}
		} catch(Exception e) {
			Log.e(TAG, "Problem while trying to resolve group", e);
		}
		return GROUP_UNKNOWN;
	}
	
	private LayoutInflater inflater = null;
	private PackageManager pm = null;
	private AppDatabase appdb = null;
	
	// TODO: move uncat group name into strings.xml
	public String GROUP_UNKNOWN = "Uncategorized";
	private int iconSize = -1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_launch);
		
		this.inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		// remember open/closed status when coming back later
		// try latching onto new package events
		
		pm = getPackageManager();
		appdb = new AppDatabase(LauncherActivity.this);
		
		// allow focus inside of rows to select children
		getExpandableListView().setItemsCanFocus(true);

		iconSize = (int)getResources().getDimension(android.R.dimen.app_icon_size);
		
	}
	
	public void onStart() {
		super.onStart();
		new ProcessTask().execute();
	}
	
	public void onStop() {
		super.onStop();
		this.setListAdapter(null);
	}
	
	public void onDestroy() {
		super.onDestroy();
		appdb.close();
	}
	
	private MenuItem force = null;
	
	private final static int STATE_UNKNOWN = 1,
		STATE_ALL_EXPAND = 2,
		STATE_ALL_COLLAP = 3;
	
	private int expandState = STATE_UNKNOWN;
	
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		Intent homeIntent = new Intent();
		homeIntent.setClassName("com.android.launcher", "com.android.launcher.Launcher");
		menu.add("Default home")
			.setIcon(R.drawable.ic_menu_home)
			.setIntent(homeIntent);
		
		menu.add("Search")
			.setIcon(android.R.drawable.ic_menu_search);
		
		force = menu.add("Expand all")
			.setIcon(android.R.drawable.ic_menu_share)
			.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				public boolean onMenuItemClick(MenuItem item) {
					ExpandableListView listView = LauncherActivity.this.getExpandableListView();
					ExpandableListAdapter adapter = LauncherActivity.this.getExpandableListAdapter();
					switch(expandState) {
					case STATE_UNKNOWN:
					case STATE_ALL_COLLAP:
						// when unknown or collapsed, we force all open
						for(int i = 0; i < adapter.getGroupCount(); i++)
							listView.expandGroup(i);
						expandState = STATE_ALL_EXPAND;
						break;
					case STATE_ALL_EXPAND:
						// when expanded, we force all closed
						for(int i = 0; i < adapter.getGroupCount(); i++)
							listView.collapseGroup(i);
						expandState = STATE_ALL_COLLAP;
						break;
					}
					
					return true;
				}
			});
		
		menu.add("Refresh")
			.setIcon(R.drawable.ic_menu_refresh)
			.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				public boolean onMenuItemClick(MenuItem item) {
					// clear any database mappings and local json cache
					// TODO: clear local json cache (when implemented)
					appdb.deleteAllMappings();
					setListAdapter(null);
					new ProcessTask().execute();
					return true;
				}
			});
		
		Intent settingsIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
		settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		menu.add("Settings")
			.setIcon(android.R.drawable.ic_menu_preferences)
			.setIntent(settingsIntent);

		return true;
	}
	
	public boolean onPrepareOptionsMenu(Menu menu) {
		switch(expandState) {
		case STATE_UNKNOWN:
		case STATE_ALL_COLLAP:
			force.setTitle("Expand all");
			break;
		case STATE_ALL_EXPAND:
			force.setTitle("Collapse all");
			break;
		}
		return true;
	}

	/**
	 * Helper function to correctly add categorized apps to entryMap. Will
	 * create internal ArrayList if doesn't exist for given category.
	 */
	private void addMappingHelper(Map<String,List<EntryInfo>> entryMap, EntryInfo entry, String categoryName) {
		if(!entryMap.containsKey(categoryName))
			entryMap.put(categoryName, new ArrayList<EntryInfo>());
		entryMap.get(categoryName).add(entry);
	}
	

	/**
	 * Task that reads all applications, sorting into categories as needed.
	 */
    private class ProcessTask extends UserTask<Void, Void, GroupAdapter> {
		public GroupAdapter doInBackground(Void... params) {
			
			// final map used to store category mappings
			Map<String,List<EntryInfo>> entryMap = new HashMap<String,List<EntryInfo>>();
			
			// search for all launchable apps
			Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
			mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			
			List<ResolveInfo> apps = pm.queryIntentActivities(mainIntent, 0);
			List<EntryInfo> passone = new LinkedList<EntryInfo>(),
				passtwo = new LinkedList<EntryInfo>();
			
			for(ResolveInfo info : apps) {
				EntryInfo entry = new EntryInfo();
				
				// load details about this app
				entry.resolveInfo = info;
				entry.title = info.loadLabel(pm);
				if(entry.title == null)
					entry.title = info.activityInfo.name;
				
				passone.add(entry);
			}

			Log.d(TAG, String.format("entering first pass with %d unresolved", passone.size()));
			
			for(EntryInfo entry : passone) {
				// try resolving category using internal database
				String packageName = entry.resolveInfo.activityInfo.packageName;
				try {
					String categoryName = appdb.getCategory(packageName);
					if(categoryName != null) {
						// found category for this app, so record it
						addMappingHelper(entryMap, entry, categoryName);
						
						Log.d(TAG, String.format("found categoryName=%s for packageName=%s", categoryName, packageName));
						
					} else {
						// otherwise keep around for later resolving
						passtwo.add(entry);
					}
				} catch(Exception e) {
					Log.e(TAG, "Problem while trying to categorize app", e);
				}

			}
			
			Log.d(TAG, String.format("entering second pass with %d unresolved", passtwo.size()));
			
			// second pass tries resolving unknown apps
			if(passtwo.size() > 0) {
				JSONObject groupMap = new JSONObject();
				try {
					groupMap = new JSONObject(JSON_GROUP);
				} catch(Exception e) {
					Log.e(TAG, "Problem parsing category JSON", e);
				}
				
				for(EntryInfo entry : passtwo) {
					String packageName = entry.resolveInfo.activityInfo.packageName;
					String categoryName = resolveGroup(groupMap, packageName);
					CharSequence descrip = entry.resolveInfo.activityInfo.applicationInfo.loadDescription(pm);
					if(descrip == null) descrip = "";
					
					appdb.addMapping(packageName, categoryName, descrip.toString());
					addMappingHelper(entryMap, entry, categoryName);
					
					Log.d(TAG, String.format("found new mapping groupName=%s for packageName=%s", categoryName, packageName));
					
				}
			}
			
			// sort each category of apps 
			final Collator collator = Collator.getInstance();
			for(String key : entryMap.keySet()) {
				Collections.sort(entryMap.get(key), new Comparator<EntryInfo>() {
					public int compare(EntryInfo object1, EntryInfo object2) {
						return collator.compare(object1.title, object2.title);
					}
				});
			}
			
			// free any cache memory
			appdb.clearCache();
			
			// now that app tree is built, pass along to adapter
			return new GroupAdapter(entryMap);

		}

		@Override
		public void end(GroupAdapter result) {
			updateColumns(result, getResources().getConfiguration());
			setListAdapter(result);
			
			// request focus to activate dpad
			getExpandableListView().requestFocus();
		}

	}
    

    /**
     * Task for creating application thumbnails as needed.
     */
    private class ThumbTask extends UserTask<Object, Void, Object[]> {
		public Object[] doInBackground(Object... params) {
			EntryInfo info = (EntryInfo)params[0];
			
			// create actual thumbnail and pass along to gui thread
			Drawable icon = info.resolveInfo.loadIcon(pm);
			info.thumb = Utilities.createIconThumbnail(icon, iconSize);
			return params;
		}

		@Override
		public void end(Object... params) {
			EntryInfo info = (EntryInfo)params[0];
			TextView textView = (TextView)params[1];

			// dont bother updating if target has been recycled
			if(!info.equals(textView.getTag())) return;
			textView.setCompoundDrawablesWithIntrinsicBounds(null, info.thumb, null, null);
		}
	}
	
	/**
	 * Force columns shown in adapter based on orientation.
	 */
	private void updateColumns(GroupAdapter adapter, Configuration config) {
		adapter.setColumns((config.orientation == Configuration.ORIENTATION_PORTRAIT) ? 4 : 6);
	}
	
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		this.updateColumns((GroupAdapter)this.getExpandableListAdapter(), newConfig);
	}
	
	/**
	 * Special adapter to help provide application lists using expandable
	 * categories. Specifically, it folds child items into grid-like columns
	 * based on setColumns(), which is a hack. While it correctly recycles rows
	 * when possible, this could be written much better.
	 */
	public class GroupAdapter extends BaseExpandableListAdapter {

		private Map<String,List<EntryInfo>> entryMap;
		private String[] groupNames;
		
		private int columns = -1;
		
		public GroupAdapter(Map<String,List<EntryInfo>> entryMap) {
			this.entryMap = entryMap;
			this.groupNames = entryMap.keySet().toArray(new String[] {});
			Arrays.sort(this.groupNames);
		}

		/**
		 * Force the number of columns to use when wrapping child elements.
		 * Inflated children should have static widths.
		 */
		public void setColumns(int columns) {
			this.columns = columns;
			this.notifyDataSetInvalidated();
		}
		
		public Object getGroup(int groupPosition) {
			return this.groupNames[groupPosition];
		}

		public int getGroupCount() {
			return this.groupNames.length;
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			if (convertView == null)
				convertView = inflater.inflate(R.layout.item_header, parent, false);

			String group = (String) this.getGroup(groupPosition);
			((TextView) convertView.findViewById(android.R.id.text1)).setText(group);

			return convertView;
		}

		public Object getChild(int groupPosition, int childPosition) {
			// no good value when a row is actually multiple children
			return null;
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public int getChildrenCount(int groupPosition) {
			// wrap children items into rows using column count
			int actualCount = entryMap.get(groupNames[groupPosition]).size();
			return (actualCount + (columns - 1)) / columns;
		}

		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			
			if(convertView == null)
				convertView = inflater.inflate(R.layout.item_row, parent, false);

			final ViewGroup viewGroup = (ViewGroup)convertView;
			
			// rebuild this row if columns changed
			if(viewGroup.getChildCount() != columns) {
				viewGroup.removeAllViews();
				for(int i = 0; i < columns; i++) {
					View view = inflater.inflate(R.layout.item_entry, parent, false);
					view.setOnClickListener(LauncherActivity.this);
					view.setOnCreateContextMenuListener(LauncherActivity.this);
					viewGroup.addView(view);
				}
			}
			
			List<EntryInfo> actualChildren = entryMap.get(groupNames[groupPosition]);
			int start = childPosition * columns, end = (childPosition + 1) * columns;
			
			for(int i = start; i < end; i++) {
				final TextView textView = (TextView)viewGroup.getChildAt(i - start);
				
				if(i < actualChildren.size()) {
					// fill with actual child info if available
					final EntryInfo info = actualChildren.get(i);
					textView.setText(info.title);
					textView.setTag(info);
					textView.setVisibility(View.VISIBLE);
					
					// generate thumbnail in usertask if not already cached
					if(info.thumb != null) {
						textView.setCompoundDrawablesWithIntrinsicBounds(null, info.thumb, null, null);
					} else {
						textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
						new ThumbTask().execute(info, textView);
					}

				} else {
					textView.setVisibility(View.INVISIBLE);
				}
				
			}
			
			return convertView;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		public boolean hasStableIds() {
			return true;
		}

	}
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		if(!(v.getTag() instanceof EntryInfo)) return;
		EntryInfo info = (EntryInfo)v.getTag();
		
		final String packageName = info.resolveInfo.activityInfo.applicationInfo.packageName;

		menu.setHeaderTitle(info.title);
		
		Intent detailsIntent = new Intent();
		detailsIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
		detailsIntent.putExtra("com.android.settings.ApplicationPkgName", packageName);
		menu.add("App details")
			.setIntent(detailsIntent);
		
		Intent deleteIntent = new Intent(Intent.ACTION_DELETE);
		deleteIntent.setData(Uri.parse("package:" + packageName));
		menu.add("Uninstall")
			.setIntent(deleteIntent);
		
	}

	public void onClick(View v) {
		if(!(v.getTag() instanceof EntryInfo)) return;
		EntryInfo info = (EntryInfo)v.getTag();
		
		// build actual intent for launching app
		Intent launch = new Intent(Intent.ACTION_MAIN);
		launch.addCategory(Intent.CATEGORY_LAUNCHER);
		launch.setComponent(new ComponentName(info.resolveInfo.activityInfo.applicationInfo.packageName, info.resolveInfo.activityInfo.name));
		launch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		
		try {
			this.startActivity(launch);
		} catch(Exception e) {
			Toast.makeText(this, "Problem trying to launch application", Toast.LENGTH_SHORT).show();
			Log.e(TAG, "Problem trying to launch application", e);
		}
		
	}


}