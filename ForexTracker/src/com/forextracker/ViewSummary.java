package com.forextracker;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class ViewSummary extends Activity {
	private String currency;
	private double balanceCount = 0;
	private double balanceAmount = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.viewsummary);
		/* Find Tablelayout defined in main.xml */
		TableLayout tl = (TableLayout) findViewById(R.id.myTableLayout);

		DBAdapter db = new DBAdapter(getApplicationContext());

		db.open();
		
		List<String> listOfCurrencies = getlist();
		
		for(int i=0;i<listOfCurrencies.size();i++)
		{
			
		balanceCount = 0;
		balanceAmount = 0;
		currency = listOfCurrencies.get(i);
		
		Cursor c = db.getRecordByCurrency(currency);
		if (c.moveToFirst()) {
			do {			
				if (c.getString(4).equalsIgnoreCase("Bought")) {
					balanceCount += Double.parseDouble(c.getString(5));
					balanceAmount -= Double.parseDouble(c.getString(7));
				} 
				else
				{
					balanceCount -= Double.parseDouble(c.getString(5));
					balanceAmount += Double.parseDouble(c.getString(7));
				}
			} while (c.moveToNext());
		
			
			TableRow tr = new TableRow(getApplicationContext());
			tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));

			/* Create a Button to be the row-content. */

			TextView tv1 = new TextView(getApplicationContext());
			TextView tv2 = new TextView(getApplicationContext());
			TextView tv3 = new TextView(getApplicationContext());
			TextView tv4 = new TextView(getApplicationContext());
			TextView tv5 = new TextView(getApplicationContext());
			
			tv1.setText(currency);
			tv2.setText("|");
			tv3.setText(Double.toString(balanceCount));
			tv4.setText("|");
			tv5.setText(Double.toString(balanceAmount));
			
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
			
			tr.addView(tv1);
			tr.addView(tv2);
			tr.addView(tv3);
			tr.addView(tv4);
			tr.addView(tv5);
			
			tl.addView(tr, new TableLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			
		}
		}

		db.close();

	}
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{     
		if(keyCode == KeyEvent.KEYCODE_BACK) 
		{             
			Intent Act2Intent = new Intent(ViewSummary.this, ForexTracker.class);       
			startActivity(Act2Intent);                     
			finish();           
			return true;   
			}     
		return false;
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

}
