package com.forextracker;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class SearchByName extends Activity {
	private String name;
	private Button submit;
	private Button searchByDate;
	private ArrayList<String> spinnerArray;
	private EditText mDate;
	private Button mPickDate;
	private int mYear;
	private int mMonth;
	private int mDay;
	static final int DATE_DIALOG_ID = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		spinnerArray = getlist();

		int listSize = spinnerArray.size();
		if (listSize == 0) {
			Toast.makeText(getApplicationContext(), "No entries found!",
					Toast.LENGTH_LONG).show();

			Intent i = new Intent(getApplicationContext(), ForexTracker.class);
			startActivity(i);
		} else {
			setContentView(R.layout.searchbyname);

			// links the spinner adapter to the spinner (best way I can describe
			// it)
			Spinner spinner = new Spinner(getApplicationContext());

			// shows the spinner in the layout
			spinner = (Spinner) findViewById(R.id.spinner);

			// creates new array adapter for the spinner
			// and loads the query results into the simple_spinner_item layout
			ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_item, spinnerArray);

			// loads the selected item into the spinner view in the layout
			spinnerArrayAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			spinner.setAdapter(spinnerArrayAdapter);
			spinner.setOnItemSelectedListener(new MyOnItemSelectedListener1());

			submit = (Button) findViewById(R.id.search);
			submit.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(),
							ShowEntriesForName.class);
					i.putExtra("name_key", name);
					startActivity(i);

				}
			});
			

			mDate = (EditText) findViewById(R.id.label4);
			mPickDate = (Button) findViewById(R.id.pickDate);
			// add a click listener to the button
			mPickDate.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					showDialog(DATE_DIALOG_ID);
				}
			});
			// get the current date
			final Calendar c = Calendar.getInstance();
			mYear = c.get(Calendar.YEAR);
			mMonth = c.get(Calendar.MONTH);
			mDay = c.get(Calendar.DAY_OF_MONTH);
			// display the current date (this method is below)
			updateDisplay();
			
			
			searchByDate = (Button) findViewById(R.id.searchDate);
			searchByDate.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(),
							ShowEntriesForDate.class);
					i.putExtra("date_key",mDate.getText().toString());
					startActivity(i);

				}
			});

		}

	}

	public ArrayList<String> getlist() {

		// creates a new arraylist for the results
		ArrayList<String> list = new ArrayList<String>();

		// calls the DBAdapter for the cursor (again, best way I can describe
		// it)
		DBAdapter db = new DBAdapter(getApplicationContext());
		db.open();
		Cursor results = db.getAllCurrencies();
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
			Intent Act2Intent = new Intent(SearchByName.this,
					ForexTracker.class);
			startActivity(Act2Intent);
			finish();
			return true;
		}
		return false;
	}

	private void updateDisplay() {
		mDate.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mMonth + 1).append("-").append(mDay).append("-")
				.append(mYear).append(" "));
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;

			Calendar cal = Calendar.getInstance();
			cal.set(mYear, mMonth, mDay);
			updateDisplay();
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	}

}
