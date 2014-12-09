package com.example.helloandroid;

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
	public static final String KEY_INOUT = "inout";
	public static final String KEY_ITEM = "item";
	public static final String KEY_NAME = "name";
	public static final String KEY_AMOUNT = "amount";
	public static final String KEY_STATUS = "status";
	private static final String TAG = "DBAdapter";

	private static final String DATABASE_NAME = "account_tracker";
	private static final String DATABASE_TABLE = "account_details";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE = "create table account_details (_id integer primary key autoincrement, "
			+ "date text not null, day text not null, "
			+ "inout text not null, "
			+ "item text not null, "
			+ "name text not null, "
			+ "amount text not null, "
			+ "status text not null);";

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
	public long insertRecord(String date, String day, String inout,
			String item, String name, String amount, String status) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_DATE, date);
		initialValues.put(KEY_DAY, day);
		initialValues.put(KEY_INOUT, inout);
		initialValues.put(KEY_ITEM, item);
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_AMOUNT, amount);
		initialValues.put(KEY_STATUS, status);

		return db.insert(DATABASE_TABLE, null, initialValues);
	}

	// ---deletes a particular title---
	public boolean deleteRecord(long rowId) {
		return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	public int purge() {
		return db.delete(DATABASE_TABLE, KEY_STATUS + "='Cleared'", null);
	}

	// ---retrieves all the titles---
	public Cursor getAllRecords() {
		return db.query(DATABASE_TABLE,
				new String[] { KEY_ROWID, KEY_DATE, KEY_DAY, KEY_INOUT,
						KEY_ITEM, KEY_NAME, KEY_AMOUNT, KEY_STATUS }, null,
				null, null, null, null);
	}

	// ---retrieves a particular title---
	public Cursor getRecord(long rowId) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID, KEY_DATE, KEY_DAY, KEY_INOUT, KEY_ITEM, KEY_NAME,
				KEY_AMOUNT, KEY_STATUS }, KEY_ROWID + "=" + rowId, null, null,
				null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	public Cursor getRecordByName(String name) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID, KEY_DATE, KEY_DAY, KEY_INOUT, KEY_ITEM, KEY_NAME,
				KEY_AMOUNT, KEY_STATUS }, KEY_NAME + "='" + name+"'", null, null,
				null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public boolean settleAccount(String name) throws SQLException {
		ContentValues args = new ContentValues();
		args.put(KEY_STATUS, "Cleared");
		return db.update(DATABASE_TABLE, args, KEY_NAME + "='" + name+"'"+" AND "+ KEY_STATUS + "='Pending'",null) > 0;
	}
	
	public Cursor getAllNames() throws SQLException {
		Cursor mCursor = db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME}, null, null, null, null, null );

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	// ---retrieves a particular title---
	public Cursor getRecordnew(String name) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID, KEY_DATE, KEY_DAY, KEY_INOUT, KEY_ITEM, KEY_NAME,
				KEY_AMOUNT, KEY_STATUS }, KEY_NAME+ "=" + name, null, null,
				null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	// ---updates a title---
	public boolean updateRecord(long rowId, String date, String day,
			String inout, String item, String name, String amount, String status) {
		ContentValues args = new ContentValues();
		args.put(KEY_DATE, date);
		args.put(KEY_DAY, day);
		args.put(KEY_INOUT, inout);
		args.put(KEY_ITEM, item);
		args.put(KEY_NAME, name);
		args.put(KEY_AMOUNT, amount);
		args.put(KEY_STATUS, status);
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
