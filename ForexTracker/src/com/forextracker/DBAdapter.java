package com.forextracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	public static final String KEY_ROWID = "_id";
	public static final String KEY_DATE = "date";
	public static final String KEY_DAY = "day";
	public static final String KEY_BOUGHTSOLD = "boughtSold";
	public static final String KEY_COUNT = "count";
	public static final String KEY_RATE = "rate";
	public static final String KEY_CURRENCY = "currency";
	public static final String KEY_AMOUNT = "amount";
	private static final String TAG = "DBAdapter";

	private static final String DATABASE_NAME = "forex_tracker";
	private static final String DATABASE_TABLE = "transaction_details";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE = "create table transaction_details (_id integer primary key autoincrement, "
			+ "date text not null, day text not null, "
			+ "boughtSold text not null, "
			+ "count text not null, "
			+ "rate text not null, "
			+ "currency text not null, "
			+ "amount text not null);";

	private final Context context;

	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	// ---opens the database---
	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	// ---closes the database---
	public void close() {
		DBHelper.close();
	}

	// ---insert a title into the database---
	public long insertRecord(String date, String day, String boughtSold,
			String rate, String currency, String count, String amount) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_DATE, date);
		initialValues.put(KEY_DAY, day);
		initialValues.put(KEY_BOUGHTSOLD, boughtSold);
		initialValues.put(KEY_COUNT, count);
		initialValues.put(KEY_RATE, rate);
		initialValues.put(KEY_CURRENCY, currency);
		initialValues.put(KEY_AMOUNT, amount);

		return db.insert(DATABASE_TABLE, null, initialValues);
	}

	// ---deletes a particular record---
	public boolean deleteRecord(long rowId) {
		return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public int settle(String currency) {
		return db.delete(DATABASE_TABLE, KEY_CURRENCY + "='" + currency + "'",
				null);
	}

	// ---retrieves all the records---
	public Cursor getAllRecords() {
		return db.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_DATE,
				KEY_DAY, KEY_CURRENCY, KEY_BOUGHTSOLD, KEY_COUNT, KEY_RATE,
				KEY_AMOUNT }, null, null, null, null, null);
	}

	// ---retrieves a particular record based on rowId---
	public Cursor getRecord(long rowId) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID, KEY_DATE, KEY_DAY, KEY_CURRENCY, KEY_BOUGHTSOLD,
				KEY_COUNT, KEY_RATE, KEY_AMOUNT }, KEY_ROWID + "=" + rowId,
				null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	// ---retrieves records of a particular currency---
	public Cursor getRecordByCurrency(String currency) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID, KEY_DATE, KEY_DAY, KEY_CURRENCY, KEY_BOUGHTSOLD,
				KEY_COUNT, KEY_RATE, KEY_AMOUNT }, KEY_CURRENCY + "='"
				+ currency + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	
	// ---retrieves records of a particular date---
	public Cursor getRecordByDate(String date) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID, KEY_DATE, KEY_DAY, KEY_CURRENCY, KEY_BOUGHTSOLD,
				KEY_COUNT, KEY_RATE, KEY_AMOUNT }, KEY_DATE + "='"
				+ date + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	// ---retrieves all currency names---
	public Cursor getAllCurrencies() throws SQLException {
		Cursor mCursor = db.query(DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_CURRENCY }, null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getRecordnew(String name) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID, KEY_DATE, KEY_DAY, KEY_CURRENCY, KEY_BOUGHTSOLD,
				KEY_COUNT, KEY_RATE, KEY_AMOUNT }, KEY_CURRENCY + "=" + name,
				null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	// ---updates a record---
	public boolean updateRecord(long rowId, String date, String day,
			String boughtSold, String rate, String currency, String count,
			String amount) {
		ContentValues args = new ContentValues();
		args.put(KEY_CURRENCY, currency);
		args.put(KEY_DATE, date);
		args.put(KEY_DAY, day);
		args.put(KEY_BOUGHTSOLD, boughtSold);
		args.put(KEY_COUNT, count);
		args.put(KEY_RATE, rate);
		args.put(KEY_AMOUNT, amount);
		return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS titles");
			onCreate(db);
		}
	}
}
