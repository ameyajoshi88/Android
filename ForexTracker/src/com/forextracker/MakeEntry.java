package com.forextracker;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class MakeEntry extends Activity {
	private Button makeEntry;
	private String date;
	private String day;
	private String boughtSold;

	private String currency;
	private String amount;

	private String rate;
	private String count;

	private int mYear;
	private int mMonth;
	private int mDay;
	private int mDow;
	private EditText mDate;
	private Button mPickDate;
	private Button newName;
	private EditText mRate;
	private EditText mCount;
	private RadioGroup mBoughtSoldRadioGroup;

	static final int DATE_DIALOG_ID = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ArrayList<String> spinnerArray = getlist();

		if (spinnerArray.size() == 0) {
			displayNewCurrencyPage();
		} else {
			setContentView(R.layout.add);

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

			mCount = (EditText) findViewById(R.id.count);

			mCount.addTextChangedListener(new TextWatcher() {
				public void afterTextChanged(Editable s) {

				}

				public void beforeTextChanged(CharSequence s, int start,
						int iCount, int after) {
				}

				public void onTextChanged(CharSequence s, int start,
						int before, int iCount) {
					populateAmount();
				}
			});

			mRate = (EditText) findViewById(R.id.rate);

			mRate.addTextChangedListener(new TextWatcher() {
				public void afterTextChanged(Editable s) {

				}

				public void beforeTextChanged(CharSequence s, int start,
						int iCount, int after) {
				}

				public void onTextChanged(CharSequence s, int start,
						int before, int iCount) {
					populateAmount();
				}
			});

			newName = (Button) findViewById(R.id.pickName);
			newName.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {

					displayNewCurrencyPage();
				}
			});

			// add a click listener to the button
			makeEntry = (Button) findViewById(R.id.submit);
			makeEntry.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {

					EditText editText = (EditText) findViewById(R.id.label4);
					date = editText.getText().toString();

					day = getDayOfWeek(mDow);

					editText = (EditText) findViewById(R.id.count);
					count = editText.getText().toString();

					editText = (EditText) findViewById(R.id.amount);
					amount = editText.getText().toString();

					if (isValidEntry() == true) {

						makeEntry();
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

			currency = parent.getItemAtPosition(pos).toString();

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

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent Act2Intent = new Intent(MakeEntry.this, ForexTracker.class);
			startActivity(Act2Intent);
			finish();
			return true;
		}
		return false;
	}

	public void populateAmount() {
		EditText editText = (EditText) findViewById(R.id.rate);
		rate = editText.getText().toString();

		editText = (EditText) findViewById(R.id.count);
		count = editText.getText().toString();
		if ((rate.trim().length() != 0) && (count.trim().length() != 0)) {
			double amt = Integer.parseInt(count) * Double.parseDouble(rate);
			editText = (EditText) findViewById(R.id.amount);
			editText.setText(new StringBuilder().append(amt));
		} else {
			editText = (EditText) findViewById(R.id.amount);
			editText.setText(new StringBuilder().append(""));
		}
	}

	public boolean isValidEntry() {
		boolean flag = true;

		if (flag == true)
			if (currency == null || currency.trim().equals("")) {
				Toast.makeText(getApplicationContext(), "Enter the Currency!",

				Toast.LENGTH_LONG).show();
				flag = false;
			}

		if (flag == true)
			if (count == null || count.trim().equals("")) {
				Toast.makeText(getApplicationContext(), "Enter the count!",

				Toast.LENGTH_LONG).show();

				flag = false;
			}

		if (flag == true)
			if (rate == null || rate.trim().equals("")) {
				Toast.makeText(getApplicationContext(), "Enter the Rate!",

				Toast.LENGTH_LONG).show();
				flag = false;
			}
		if (flag == true)
			if (!isNumber(rate)) {
				Toast.makeText(getApplicationContext(), "Enter valid rate!",

				Toast.LENGTH_LONG).show();
				flag = false;
			}

		if (flag == true)
			if (rate.trim().equals(".")) {
				Toast.makeText(getApplicationContext(), "Enter valid rate!",

				Toast.LENGTH_LONG).show();
				flag = false;
			}
		if (flag == true)
			if (Double.parseDouble(rate) == 0.0) {
				Toast.makeText(getApplicationContext(), "Rate cannot be zero!",

				Toast.LENGTH_LONG).show();
				flag = false;
			}
		if (flag == true)
			if (rate.trim().charAt(rate.length() - 1) == '.') {
				Toast.makeText(getApplicationContext(), "Enter valid Rate!",

				Toast.LENGTH_LONG).show();
				flag = false;
			}
		if (flag == true) {
			RadioButton rb1 = (RadioButton) findViewById(R.id.radio_bought);
			RadioButton rb2 = (RadioButton) findViewById(R.id.radio_sold);
			if (rb1.isChecked() || rb2.isChecked()) {
				mBoughtSoldRadioGroup = (RadioGroup) findViewById(R.id.radio_bought_sold_group);

				// get selected radio button from radioGroup
				int selectedId = mBoughtSoldRadioGroup
						.getCheckedRadioButtonId();

				// find the radiobutton by returned id
				RadioButton boughtSoldRadioButton = (RadioButton) findViewById(selectedId);
				boughtSold = boughtSoldRadioButton.getText().toString();

				flag = true;
			} else {
				Toast.makeText(getApplicationContext(), "Select Bought/Sold!",

				Toast.LENGTH_LONG).show();

				flag = false;
			}
		}

		return flag;
	}
	
	public void makeEntry()
	{
		DBAdapter db = new DBAdapter(getApplicationContext());

		db.open();
		db.insertRecord(date, day, boughtSold, rate, currency,
				count, amount);
		db.close();
		Toast.makeText(getApplicationContext(),
				"Entry made successfully!",

				Toast.LENGTH_LONG).show();

		Intent i = new Intent(getApplicationContext(),
				ForexTracker.class);
		startActivity(i);
	}

	public void displayNewCurrencyPage() {
		setContentView(R.layout.add1);

		mCount = (EditText) findViewById(R.id.count);

		mCount.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {

			}

			public void beforeTextChanged(CharSequence s, int start,
					int iCount, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int iCount) {
				populateAmount();
			}
		});

		mRate = (EditText) findViewById(R.id.rate);

		mRate.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {

			}

			public void beforeTextChanged(CharSequence s, int start,
					int iCount, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int iCount) {
				populateAmount();
			}
		});

		// add a click listener to the button
		makeEntry = (Button) findViewById(R.id.submit);
		makeEntry.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				EditText editText = (EditText) findViewById(R.id.currency);
				currency = editText.getText().toString();

				editText = (EditText) findViewById(R.id.label4);
				date = editText.getText().toString();

				day = getDayOfWeek(mDow);

				editText = (EditText) findViewById(R.id.count);
				count = editText.getText().toString();

				editText = (EditText) findViewById(R.id.amount);
				amount = editText.getText().toString();

				if (isValidEntry() == true) {

					makeEntry();

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

}
