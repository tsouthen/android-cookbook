package org.jsharkey.grouphome;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AppDatabase extends SQLiteOpenHelper {
	
	public static final String TAG = AppDatabase.class.toString();
	
	public final static String DB_NAME = "apps";
	public final static int DB_VERSION = 1;
	
	public final static String TABLE_APP = "app";
	public final static String FIELD_APP_CATEGORY = "category";
	public final static String FIELD_APP_PACKAGE = "package";
	public final static String FIELD_APP_DESCRIP = "descrip";
	
	public AppDatabase(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_APP
				+ " (_id INTEGER PRIMARY KEY, "
				+ FIELD_APP_CATEGORY + " TEXT, "
				+ FIELD_APP_PACKAGE + " TEXT, "
				+ FIELD_APP_DESCRIP + ")");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		int currentVersion = oldVersion;
		
		if(currentVersion < 1) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP);
			onCreate(db);
			currentVersion = DB_VERSION;
		}
	}
	
	private Object mappingLock = new Object();
	private boolean validCache = false;
	private Map<String,String> packageMapping = new HashMap<String,String>();
	
	/**
	 * Make sure our in-memory cache is loaded. Callers should wrap this call in
	 * a synchronized block.
	 */
	private void assertCache() throws Exception {
		// skip if already cached, otherwise create and fill
		if(validCache) return;
		
		Log.d(TAG, "assertCache() is building in-memory cache");
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.query(TABLE_APP, new String[] { FIELD_APP_PACKAGE,
				FIELD_APP_CATEGORY }, null, null, null, null, null);
		if(c == null)
			throw new Exception("Couldn't load application-to-category mapping database table");

		int COL_PACKAGE = c.getColumnIndexOrThrow(FIELD_APP_PACKAGE),
			COL_CATEGORY = c.getColumnIndexOrThrow(FIELD_APP_CATEGORY);
		
		while(c.moveToNext()) {
			String packageName = c.getString(COL_PACKAGE),
				categoryName = c.getString(COL_CATEGORY);
			packageMapping.put(packageName, categoryName);
		}
		
		c.close();
		validCache = true;
	}
	
	/**
	 * Invalidate any in-memory cache, probably after resolving a
	 * newly-categorized app. Callers should wrap this call in a synchronized
	 * block.
	 */
	private void invalidateCache() {
		Log.d(TAG, "invalidateCache() is removing in-memory cache");
		packageMapping.clear();
		validCache = false;
	}
	
	/**
	 * Externally visible method to clear any internal cache.
	 */
	public void clearCache() {
		synchronized(mappingLock) {
			invalidateCache();
		}
	}
	
	/**
	 * Find the category for the given package name, which may return null if we
	 * don't have it cached.
	 */
	public String getCategory(String packageName) throws Exception {
		String result = null;
		synchronized(mappingLock) {
			assertCache();
			result = packageMapping.get(packageName);
		}
		return result;
	}
	
	/**
	 * Insert a known mapping into database. This doesn't check for duplicates,
	 * and will invalidate any in-memory cache.
	 */
	public void addMapping(String packageName, String categoryName, String descrip) {
		ContentValues values = new ContentValues();
		values.put(FIELD_APP_PACKAGE, packageName);
		values.put(FIELD_APP_CATEGORY, categoryName);
		values.put(FIELD_APP_DESCRIP, descrip);
		
		SQLiteDatabase db = this.getWritableDatabase();
		db.insert(TABLE_APP, null, values);
		
		synchronized(mappingLock) {
			invalidateCache();
		}
		
	}
	
	/**
	 * Remove all known mappings from database. Probably used when performing an
	 * entire refresh and re-categorization. Will obviously invalidate any
	 * in-memory cache.
	 */
	public void deleteAllMappings() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_APP, null, null);
		
		synchronized(mappingLock) {
			invalidateCache();
		}

	}
	
}
