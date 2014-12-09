package com.example.helloandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class PurgeRecords extends Activity {
	private int records;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DBAdapter db = new DBAdapter(getApplicationContext());
		db.open();
		records = db.purge();
		db.close();
		Toast.makeText(getApplicationContext(), records + " record(s) deleted!",

		Toast.LENGTH_LONG).show();

		Intent i = new Intent(getApplicationContext(), HelloAndroid.class);
		startActivity(i);

	}
}
