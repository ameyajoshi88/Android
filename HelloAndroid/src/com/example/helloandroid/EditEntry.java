package com.example.helloandroid;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class EditEntry extends Activity {
	private String date;
	private String day;
	private String item;
	private String name;
	private String amount;
	private String status;
	private String payRecv;

	private int mYear;
	private int mMonth;
	private int mDay;
	private int mDow;
	private EditText mDate;
	private Button mPickDate;
	private int id;

	static final int DATE_DIALOG_ID = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit);

		Intent i = getIntent();
		id = i.getIntExtra("id_key", -1);

		DBAdapter db = new DBAdapter(getApplicationContext());

		// ---add 2 titles---
		db.open();
		Cursor c = db.getRecord(id);
		db.close();

		if (c.moveToFirst()) {
			date = c.getString(1);
			day = c.getString(2);
			payRecv = c.getString(3);

			item = c.getString(4);
			name = c.getString(5);
			amount = c.getString(6);
			status = c.getString(7);
		}

		// v = findViewById(R.id.pickDate);
		EditText et = (EditText) findViewById(R.id.name);
		et.setText(name);

		et = (EditText) findViewById(R.id.item);
		et.setText(item);

		et = (EditText) findViewById(R.id.label4);
		et.setText(date);

		et = (EditText) findViewById(R.id.amount);
		et.setText(amount);

		Spinner s = (Spinner) findViewById(R.id.spinner2);

		int index = 0;

		s = (Spinner) findViewById(R.id.spinner1);
		index = 0;
		if (status.equalsIgnoreCase("Cleared"))
			index = 1;

		s.setSelection(index);

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

		Button makeChanges = (Button) findViewById(R.id.submit);
		makeChanges.setOnClickListener(new View.OnClickListener() {
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
				/*if (amount == null || amount.equals("")) {
					Toast.makeText(getApplicationContext(),
							"Enter the amount!",

							Toast.LENGTH_LONG).show();
					flag = false;
				}
				if (name == null || name.equals("")) {
					Toast.makeText(getApplicationContext(), "Enter the name!",

					Toast.LENGTH_LONG).show();
					flag = false;
				}*/
				
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
					db.updateRecord(id, date, day, payRecv, item, name, amount,
							status);
					db.close();
					Toast.makeText(getApplicationContext(),
							"Changes saved successfully!",

							Toast.LENGTH_LONG).show();

					Intent i = new Intent(getApplicationContext(),
							HelloAndroid.class);
					startActivity(i);

				}
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
		/*
		 * final Calendar cal = Calendar.getInstance(); mYear =
		 * cal.get(Calendar.YEAR); mMonth = cal.get(Calendar.MONTH); mDay =
		 * cal.get(Calendar.DAY_OF_MONTH); mDow = cal.get(Calendar.DAY_OF_WEEK);
		 */

		String dateArray[] = new String[3];
		dateArray = date.split("-");

		mYear = Integer.parseInt(dateArray[2].substring(0, 4));
		mMonth = Integer.parseInt(dateArray[0]) - 1;
		mDay = Integer.parseInt(dateArray[1]);
		mDow = getDayOfWeekINT(day);

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

			payRecv = parent.getItemAtPosition(pos).toString();

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

	public int getDayOfWeekINT(String dow) {

		if (dow.equals("SUN"))
			return 1;
		if (dow.equals("MON"))
			return 2;
		if (dow.equals("TUE"))
			return 3;
		if (dow.equals("WED"))
			return 4;
		if (dow.equals("THU"))
			return 5;
		if (dow.equals("FRI"))
			return 6;
		if (dow.equals("SAT"))
			return 7;

		return -1;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent Act2Intent = new Intent(EditEntry.this, ViewAllRecords.class);
			startActivity(Act2Intent);
			finish();
			return true;
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.delete:
			DBAdapter db = new DBAdapter(getApplicationContext());
			db.open();
			db.deleteRecord(id);
			db.close();
			Toast.makeText(getApplicationContext(),
					"Entry deleted successfully!",

					Toast.LENGTH_LONG).show();

			Intent i = new Intent(getApplicationContext(), HelloAndroid.class);
			startActivity(i);

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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
