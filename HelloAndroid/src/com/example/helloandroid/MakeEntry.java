package com.example.helloandroid;

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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MakeEntry extends Activity {
	private Button makeEntry;
	private String date;
	private String day;
	private String inout;
	private String item;
	private String name;
	private String amount;
	private String status;

	private int mYear;
	private int mMonth;
	private int mDay;
	private int mDow;
	private EditText mDate;
	private Button mPickDate;
	private Button newName;

	static final int DATE_DIALOG_ID = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add);

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

		Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
				getApplicationContext(), R.array.status_array,
				android.R.layout.simple_spinner_item);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(adapter1);
		spinner1.setOnItemSelectedListener(new MyOnItemSelectedListener2());

		Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
				getApplicationContext(), R.array.payRecev_array,
				android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(adapter2);
		spinner2.setOnItemSelectedListener(new MyOnItemSelectedListener3());

		newName = (Button) findViewById(R.id.pickName);
		newName.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				setContentView(R.layout.add1);

				Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
				ArrayAdapter<CharSequence> adapter1 = ArrayAdapter
						.createFromResource(getApplicationContext(),
								R.array.status_array,
								android.R.layout.simple_spinner_item);
				adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner1.setAdapter(adapter1);
				spinner1.setOnItemSelectedListener(new MyOnItemSelectedListener2());

				Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
				ArrayAdapter<CharSequence> adapter2 = ArrayAdapter
						.createFromResource(getApplicationContext(),
								R.array.payRecev_array,
								android.R.layout.simple_spinner_item);
				adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner2.setAdapter(adapter2);
				spinner2.setOnItemSelectedListener(new MyOnItemSelectedListener3());

				// add a click listener to the button
				makeEntry = (Button) findViewById(R.id.submit);
				makeEntry.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {

						EditText editText = (EditText) findViewById(R.id.label4);
						date = editText.getText().toString();

						day = getDayOfWeek(mDow);

						editText = (EditText) findViewById(R.id.item);
						item = editText.getText().toString();

						editText = (EditText) findViewById(R.id.amount);
						amount = editText.getText().toString();

						editText = (EditText) findViewById(R.id.name);
						name = editText.getText().toString();

						boolean flag = true;

						if (flag == true)
							if (name == null || name.trim().equals("")) {
								Toast.makeText(getApplicationContext(),
										"Enter the name!",

										Toast.LENGTH_LONG).show();
								flag = false;
							}

						if (flag == true)
							if (item == null || item.trim().equals("")) {
								Toast.makeText(getApplicationContext(),
										"Enter name of the item!",

										Toast.LENGTH_LONG).show();

								flag = false;
							}

						if (flag == true)
							if (amount == null || amount.trim().equals("")) {
								Toast.makeText(getApplicationContext(),
										"Enter the amount!",

										Toast.LENGTH_LONG).show();
								flag = false;
							}
						if (flag == true)
							if (!isNumber(amount)) {
								Toast.makeText(getApplicationContext(),
										"Enter valid amount!",

										Toast.LENGTH_LONG).show();
								flag = false;
							}

						if (flag == true)
							if (amount.trim().equals(".")) {
								Toast.makeText(getApplicationContext(),
										"Enter valid amount!",

										Toast.LENGTH_LONG).show();
								flag = false;
							}
						if (flag == true)
							if (Double.parseDouble(amount)== 0.0) {
								Toast.makeText(getApplicationContext(),
										"Amount cannot be zero!",

										Toast.LENGTH_LONG).show();
								flag = false;
							}
						if (flag == true)
							if (amount.trim().charAt(amount.length() - 1) == '.') {
								Toast.makeText(getApplicationContext(),
										"Enter valid amount!",

										Toast.LENGTH_LONG).show();
								flag = false;
							}

						if (flag == true) {

							DBAdapter db = new DBAdapter(
									getApplicationContext());

							// ---add 2 titles---
							db.open();
							db.insertRecord(date, day, inout, item, name,
									amount, status);
							db.close();
							Toast.makeText(getApplicationContext(),
									"Entry made successfully!",

									Toast.LENGTH_LONG).show();

							Intent i = new Intent(getApplicationContext(),
									HelloAndroid.class);
							startActivity(i);

						}
					}
				});

				// capture our View elements

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

			}
		});

		// add a click listener to the button
		makeEntry = (Button) findViewById(R.id.submit);
		makeEntry.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				EditText editText = (EditText) findViewById(R.id.label4);
				date = editText.getText().toString();

				day = getDayOfWeek(mDow);

				editText = (EditText) findViewById(R.id.item);
				item = editText.getText().toString();

				boolean flag = true;

				if (name == null || name.trim().equals("")) {
					Toast.makeText(getApplicationContext(), "Enter the name!",

					Toast.LENGTH_LONG).show();
					flag = false;
				}

				if (flag == true)
					if (item == null || item.trim().equals("")) {
						Toast.makeText(getApplicationContext(),
								"Enter name of the item!",

								Toast.LENGTH_LONG).show();

						flag = false;
					}

				editText = (EditText) findViewById(R.id.amount);
				amount = editText.getText().toString();

				if (flag == true)
					if (amount == null || amount.trim().equals("")) {
						Toast.makeText(getApplicationContext(),
								"Enter the amount!",

								Toast.LENGTH_LONG).show();
						flag = false;
					}
				if (flag == true)
					if (!isNumber(amount)) {
						Toast.makeText(getApplicationContext(),
								"Enter valid amount!",

								Toast.LENGTH_LONG).show();
						flag = false;
					}
				
				if (flag == true)
					if (Double.parseDouble(amount)== 0.0) {
						Toast.makeText(getApplicationContext(),
								"Amount cannot be zero!",

								Toast.LENGTH_LONG).show();
						flag = false;
					}
				
				if (flag == true)
					if (amount.trim().equals(".")) {
						Toast.makeText(getApplicationContext(),
								"Enter valid amount!",

								Toast.LENGTH_LONG).show();
						flag = false;
					}

				if (flag == true)
					if (amount.trim().charAt(amount.length() - 1) == '.') {
						Toast.makeText(getApplicationContext(),
								"Enter valid amount!",

								Toast.LENGTH_LONG).show();
						flag = false;
					}

				if (flag == true) {
					DBAdapter db = new DBAdapter(getApplicationContext());

					// ---add 2 titles---
					db.open();
					db.insertRecord(date, day, inout, item, name, amount,
							status);
					db.close();

					Toast.makeText(getApplicationContext(),
							"Entry made successfully!",

							Toast.LENGTH_LONG).show();
					Intent i = new Intent(getApplicationContext(),
							HelloAndroid.class);
					startActivity(i);
				}
			}
		});

		// capture our View elements

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
		mDow = c.get(Calendar.DAY_OF_WEEK);
		// display the current date (this method is below)
		updateDisplay();
	}

	// updates the date in the TextView
	private void updateDisplay() {
		mDate.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mMonth + 1).append("-").append(mDay).append("-")
				.append(mYear).append(" "));
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

	public class MyOnItemSelectedListener2 implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {

			status = parent.getItemAtPosition(pos).toString();

		}

		public void onNothingSelected(AdapterView<?> parent) {
			// Do nothing.
		}
	}

	public class MyOnItemSelectedListener3 implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {

			inout = parent.getItemAtPosition(pos).toString();

		}

		public void onNothingSelected(AdapterView<?> parent) {
			// Do nothing.
		}
	}

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;

			Calendar cal = Calendar.getInstance();
			cal.set(mYear, mMonth, mDay);
			mDow = cal.get(Calendar.DAY_OF_WEEK);

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
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent Act2Intent = new Intent(MakeEntry.this, HelloAndroid.class);
			startActivity(Act2Intent);
			finish();
			return true;
		}
		return false;
	}

	public String getDayOfWeek(int n) {
		switch (n) {
		case 1:
			return "SUN";
		case 2:
			return "MON";
		case 3:
			return "TUE";
		case 4:
			return "WED";
		case 5:
			return "THU";
		case 6:
			return "FRI";
		case 7:
			return "SAT";
		}
		return "TODO1";
	}

	public boolean isNumber(String s) {
		for (int i = 0, j = 0; i < s.trim().length();) {
			if ((s.charAt(i) >= '0' && s.charAt(i) <= '9')
					|| s.charAt(i) == '.')
				i++;
			else
				return false;
			if (s.charAt(i - 1) == '.')
				j++;
			if (j > 1)
				return false;

		}
		return true;
	}

}
