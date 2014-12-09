package com.forextracker;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class EditEntry extends Activity {
	private String date;
	private String day;
	private String rate;
	private String currency;
	private String amount;
	private String count;
	private String boughtSold;
	private int mYear;
	private int mMonth;
	private int mDay;
	private int mDow;
	private EditText mDate;
	private Button mPickDate;
	private int id;
	private EditText mRate;
	private EditText mCount;
	private RadioGroup mBoughtSoldRadioGroup;

	static final int DATE_DIALOG_ID = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit);

		Intent i = getIntent();
		id = i.getIntExtra("id_key", -1);

		DBAdapter db = new DBAdapter(getApplicationContext());

		db.open();
		Cursor c = db.getRecord(id);
		db.close();

		if (c.moveToFirst()) {
			date = c.getString(1);
			day = c.getString(2);
			currency = c.getString(3);

			boughtSold = c.getString(4);
			count = c.getString(5);
			rate = c.getString(6);
			amount = c.getString(7);
		}

		EditText et = (EditText) findViewById(R.id.currency);
		et.setText(currency);

		et = (EditText) findViewById(R.id.rate);
		et.setText(rate);

		et = (EditText) findViewById(R.id.label4);
		et.setText(date);

		et = (EditText) findViewById(R.id.amount);
		et.setText(amount);

		et = (EditText) findViewById(R.id.count);
		et.setText(count);

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

		if (boughtSold.equalsIgnoreCase("Bought")) {
			RadioButton rb = (RadioButton) findViewById(R.id.radio_bought);
			rb.setChecked(true);
		} else if (boughtSold.equalsIgnoreCase("Sold")) {
			RadioButton rb = (RadioButton) findViewById(R.id.radio_sold);
			rb.setChecked(true);
		}

		Button makeChanges = (Button) findViewById(R.id.submit);
		makeChanges.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				EditText editText = (EditText) findViewById(R.id.label4);
				date = editText.getText().toString();

				day = getDayOfWeek(mDow);

				editText = (EditText) findViewById(R.id.rate);
				rate = editText.getText().toString();

				editText = (EditText) findViewById(R.id.amount);
				amount = editText.getText().toString();

				editText = (EditText) findViewById(R.id.currency);
				currency = editText.getText().toString();

				editText = (EditText) findViewById(R.id.count);
				count = editText.getText().toString();

				if (isValidEntry() == true) {

					makeEntry();

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

		String dateArray[] = new String[3];
		dateArray = date.split("-");

		mYear = Integer.parseInt(dateArray[2].substring(0, 4));
		mMonth = Integer.parseInt(dateArray[0]) - 1;
		mDay = Integer.parseInt(dateArray[1]);
		mDow = getDayOfWeekINT(day);

		// display the current date (this method is below)
		updateDisplay();

	}

	public void makeEntry() {
		DBAdapter db = new DBAdapter(getApplicationContext());

		db.open();
		db.updateRecord(id, date, day, boughtSold, rate, currency, count, amount);
		db.close();
		Toast.makeText(getApplicationContext(), "Entry made successfully!",

		Toast.LENGTH_LONG).show();

		Intent i = new Intent(getApplicationContext(), ForexTracker.class);
		startActivity(i);
	}

	// updates the date in the TextView
	private void updateDisplay() {
		mDate.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mMonth + 1).append("-").append(mDay).append("-")
				.append(mYear).append(" "));
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

			Intent i = new Intent(getApplicationContext(), ForexTracker.class);
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

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent Act2Intent = new Intent(EditEntry.this, ForexTracker.class);
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

}
