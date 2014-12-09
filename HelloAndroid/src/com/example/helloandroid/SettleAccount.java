package com.example.helloandroid;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class SettleAccount extends Activity {
	private String name;
	private Button submit;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settleaccount);

		// links the spinner adapter to the spinner (best way I can describe it)
		Spinner spinner = new Spinner(getApplicationContext());

		// shows the spinner in the layout
		spinner = (Spinner) findViewById(R.id.spinner);

		ArrayList<String> spinnerArray = getlist();

		// creates new array adapter for the spinner
		// and loads the query results into the simple_spinner_item layout
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, spinnerArray);

		// loads the selected item into the spinner view in the layout
		spinnerArrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner.setAdapter(spinnerArrayAdapter);
		spinner.setOnItemSelectedListener(new MyOnItemSelectedListener1());

		submit = (Button) findViewById(R.id.settle);
		submit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (name == null)
					Toast.makeText(getApplicationContext(),
							"No Accounts to Settle!",

							Toast.LENGTH_LONG).show();

				else {
					DBAdapter db = new DBAdapter(getApplicationContext());
					db.open();
					db.settleAccount(name);
					db.close();
					Toast.makeText(getApplicationContext(), "Account Settled!",

					Toast.LENGTH_LONG).show();
				}

			}
		});

	}

	public ArrayList<String> getlist() {

		// creates a new arraylist for the results
		ArrayList<String> list = new ArrayList<String>();

		// calls the DBAdapter for the cursor (again, best way I can describe
		// it)
		DBAdapter db = new DBAdapter(getApplicationContext());
		db.open();
		Cursor results = db.getAllNames();
		db.close();
		// loop to get all rows
		if (results != null) {
			if (results.moveToFirst()) {
				do {
					String item = results.getString(1);

					if (!(list.contains(item)))
						list.add(item);
				} while (results.moveToNext()); // move to next row
			} // if
		} // if

		// adds the results to the array
		return list;
	}

	public class MyOnItemSelectedListener1 implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {

			name = parent.getItemAtPosition(pos).toString();

		}

		public void onNothingSelected(AdapterView<?> parent) {
			// Do nothing.
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent Act2Intent = new Intent(SettleAccount.this, HelloAndroid.class);
			startActivity(Act2Intent);
			finish();
			return true;
		}
		return false;
	}

}
