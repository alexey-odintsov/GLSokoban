package com.alekso.glsokoban;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 
 * 
 *         levels _id INT - level number 
 *         data TEXT - ###+@.. 
 *         completed INT - 0 - not completed; 1 - completed 
 *         score INT - from 1 to 3 stars
 * 
 */
public class DbAdapter {

	private static final String TABLE_LEVELS = "levels";
	private static final String KEY_LEVEL_ID = "_id";
	private static final String KEY_LEVEL_DATA = "data";
	private static final String KEY_LEVEL_BEST_TIME = "time";
	private static final String KEY_LEVEL_COMPLETED = "completed";
	private static final String KEY_LEVEL_SCORE = "score";
	private static final String CREATE_LEVELS = "CREATE TABLE " + TABLE_LEVELS
			+ "(" + KEY_LEVEL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ KEY_LEVEL_DATA + " TEXT NOT NULL," + KEY_LEVEL_BEST_TIME
			+ " INTEGER NOT NULL, " + KEY_LEVEL_COMPLETED
			+ " INTEGER NOT NULL, " + KEY_LEVEL_SCORE + " INTEGER NOT NULL)";

	private static final String DB_NAME = "sokoban_levels";
	private static final int DB_VERSION = 15;

	private final Context mContext;
	private DbHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static final String TAG = "DBAdapter";

	/**
	 * DbHelper class
	 */
	private static class DbHelper extends SQLiteOpenHelper {

		/**
		 * 
		 * @param context
		 */
		public DbHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_LEVELS);
			populateLevels(db);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEVELS);
			onCreate(db);
		}
		
		private void populateLevels(SQLiteDatabase db) {
			db.execSQL("INSERT INTO "
					+ TABLE_LEVELS
					+ "("
					+ KEY_LEVEL_ID
					+ ", "
					+ KEY_LEVEL_DATA
					+ ", "
					+ KEY_LEVEL_BEST_TIME
					+ ", "
					+ KEY_LEVEL_COMPLETED
					+ ", "
					+ KEY_LEVEL_SCORE
					+ ") VALUES(1, '##########|#        #|#        #|# #@     #|#+# *    #|##########', 30, 1, 2)");
			db.execSQL("INSERT INTO "
					+ TABLE_LEVELS
					+ "("
					+ KEY_LEVEL_ID
					+ ", "
					+ KEY_LEVEL_DATA
					+ ", "
					+ KEY_LEVEL_BEST_TIME
					+ ", "
					+ KEY_LEVEL_COMPLETED
					+ ", "
					+ KEY_LEVEL_SCORE
					+ ") VALUES(2, '##########|#   # + +#|#@  # @  #|#   ##  @#|#        #|#+  *    #|#        #|#      #@#|#     ##+#|##########', 0, 0, 0)");
			db.execSQL("INSERT INTO "
					+ TABLE_LEVELS
					+ "("
					+ KEY_LEVEL_ID
					+ ", "
					+ KEY_LEVEL_DATA
					+ ", "
					+ KEY_LEVEL_BEST_TIME
					+ ", "
					+ KEY_LEVEL_COMPLETED
					+ ", "
					+ KEY_LEVEL_SCORE
					+ ") VALUES(3, '  ##### |###   # |#+*@  # |### @+# |#+##@ # |# #   ##|#@  @ +#|#   +  #|########', 0, 0, 0)");		
		}

	}

	
	
	/**
	 * 
	 * @param context
	 */
	public DbAdapter(Context context) {
		this.mContext = context;
	}

	/**
	 * Open database connection
	 * 
	 * @return
	 */
	public DbAdapter open() {
		mDbHelper = new DbHelper(mContext);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	/**
	 * Close database connection
	 */
	public void close() {
		mDbHelper.close();
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<Level> getLevels() {
		String sql = "SELECT * FROM " + TABLE_LEVELS;
		Cursor c = mDb.rawQuery(sql, null);

		ArrayList<Level> levels = new ArrayList<Level>();

		while (c.moveToNext()) {
			Log.d("ALEKSO", "Cursor: " + c.getString(0) + " " + c.getString(1));
			levels.add(new Level(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3), c.getInt(4)));
		}

		return levels;
	}

	public void updateLevel(Level level) {
		String query = "UPDATE " + TABLE_LEVELS + " SET " + KEY_LEVEL_COMPLETED
				+ " = 1 WHERE " + KEY_LEVEL_ID + " = " + level.id;
		
		mDb.rawQuery(query, null);
	}
}
