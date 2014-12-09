package com.forextracker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;

public class ShowEntriesForDate extends Activity
{
	private String date;
	private double balanceAmount = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent i = getIntent();
		date = i.getStringExtra("date_key");

		setContentView(R.layout.viewforname);
		/* Find Tablelayout defined in main.xml */
		TableLayout tl = (TableLayout) findViewById(R.id.myTableLayout);

		DBAdapter db = new DBAdapter(getApplicationContext());

		db.open();
		Cursor c = db.getRecordByDate(date);
		if (c.moveToFirst()) {
			do {
				/* Create a new row to be added. */
				TableRow tr = new TableRow(getApplicationContext());
				tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT));

				/* Create a Button to be the row-content. */

				TextView tv1 = new TextView(getApplicationContext());
				TextView tv2 = new TextView(getApplicationContext());
				TextView tv3 = new TextView(getApplicationContext());
				TextView tv4 = new TextView(getApplicationContext());
				TextView tv5 = new TextView(getApplicationContext());
				TextView tv6 = new TextView(getApplicationContext());
				TextView tv7 = new TextView(getApplicationContext());
				TextView tv8 = new TextView(getApplicationContext());
				TextView tv9 = new TextView(getApplicationContext());
				TextView tv10 = new TextView(getApplicationContext());
				TextView tv11 = new TextView(getApplicationContext());
				TextView tv12 = new TextView(getApplicationContext());
				TextView tv13 = new TextView(getApplicationContext());
				tv1.setText(c.getString(1));
				tv2.setText("|");
				tv3.setText(c.getString(2));
				tv4.setText("|");
				tv5.setText(c.getString(3));
				tv6.setText("|");
				tv7.setText(c.getString(4));
				tv8.setText("|");
				tv9.setText(c.getString(5));
				tv10.setText("|");
				tv11.setText(c.getString(6));
				tv12.setText("|");
				tv13.setText(c.getString(7));

			

				tv1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT));
				tv2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT));
				tv3.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT));
				tv4.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT));
				tv5.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT));
				tv6.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT));
				tv7.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT));
				tv8.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT));
				tv9.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT));
				tv10.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT));
				tv11.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT));
				tv12.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT));
				tv13.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT));

				/* Add Button to row. */

				tr.addView(tv1);
				tr.addView(tv2);
				tr.addView(tv3);
				tr.addView(tv4);
				tr.addView(tv5);
				tr.addView(tv6);
				tr.addView(tv7);
				tr.addView(tv8);
				tr.addView(tv9);
				tr.addView(tv10);
				tr.addView(tv11);
				tr.addView(tv12);
				tr.addView(tv13);

				/* Add row to TableLayout. */
				tl.addView(tr, new TableLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				
				if (c.getString(4).equalsIgnoreCase("Bought")) {
					balanceAmount -= Double.parseDouble(c.getString(7));
				} 
				else
				{
					balanceAmount += Double.parseDouble(c.getString(7));
				}
			} while (c.moveToNext());

			Toast.makeText(getApplicationContext(), "Amount = "+balanceAmount,

					Toast.LENGTH_LONG).show();
		}

		else {

			Toast.makeText(getApplicationContext(), "No records found",

			Toast.LENGTH_LONG).show();
		}

		db.close();

	}
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{     
		if(keyCode == KeyEvent.KEYCODE_BACK) 
		{             
			Intent Act2Intent = new Intent(ShowEntriesForDate.this, SearchByName.class);       
			startActivity(Act2Intent);                     
			finish();           
			return true;   
			}     
		return false;
	}

}
