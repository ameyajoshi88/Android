package com.example.helloandroid;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class ViewAllRecords extends Activity {


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewall);

		/* Find Tablelayout defined in main.xml */
		TableLayout tl = (TableLayout) findViewById(R.id.myTableLayout);

		DBAdapter db = new DBAdapter(getApplicationContext());

		db.open();
		Cursor c = db.getAllRecords();
		
		if (c.moveToFirst()) {
			do {
				
				Button b = new Button(getApplicationContext());
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
				TextView tv14 = new TextView(getApplicationContext());
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
				tv14.setText("|");
				b = new Button(getApplicationContext());
				b.setOnClickListener(buttonListener);
				b.setText("Edit");
				b.setId(Integer.parseInt(c.getString(0)));
				
				
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
				tv14.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT));

				b.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
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
				tr.addView(tv14);
				tr.addView(b);

				/* Add row to TableLayout. */
				tl.addView(tr, new TableLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

			} while (c.moveToNext());
		}

		else {

			Toast.makeText(getApplicationContext(), "No records found",

			Toast.LENGTH_LONG).show();
		}

		db.close();

		

	}
	
	private OnClickListener buttonListener = new OnClickListener() 
	{    public void onClick(View v) 
	{      
		// do something when the button is clicked    
		int id = v.getId();
		Intent i = new Intent(getApplicationContext(), EditEntry.class);
		i.putExtra("id_key", id);

		startActivity(i);
	
	}};
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent Act2Intent = new Intent(ViewAllRecords.this, HelloAndroid.class);
			startActivity(Act2Intent);
			finish();
			return true;
		}
		return false;
	}
	
	}


