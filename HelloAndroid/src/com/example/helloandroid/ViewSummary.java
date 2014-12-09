package com.example.helloandroid;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewSummary extends Activity {
	private String name;
	private double balanceAmt = 0;
	private static int j = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//setContentView(R.layout.atwidgetlayout);
	
		/* Find Tablelayout defined in main.xml */
		LinearLayout tl = (LinearLayout) findViewById(R.id.widLinLayout);
		
		DBAdapter db = new DBAdapter(getApplicationContext());

		db.open();
		
		List<String> listOfNames = getlist();
		System.out.println("SIZE::"+listOfNames.size());
		for(int i=0;i<listOfNames.size();i++)
		{
			
		balanceAmt = 0;

		name = listOfNames.get(i);
		
		Cursor c = db.getRecordByName(name);
		if (c.moveToFirst()) {
			do {			
				if (c.getString(3).equalsIgnoreCase("Pay") && c.getString(7).equalsIgnoreCase("PENDING")) {
					balanceAmt += Double.parseDouble(c.getString(6));
				} 
				else if(c.getString(3).equalsIgnoreCase("Receive") && c.getString(7).equalsIgnoreCase("PENDING"))
				{
					balanceAmt -= Double.parseDouble(c.getString(6));
				}
			} while (c.moveToNext());
	

			TextView tv1 = new TextView(getApplicationContext());
			TextView tv2 = new TextView(getApplicationContext());
			TextView tv3 = new TextView(getApplicationContext());
			
			int idTv1 = j++;
			tv1.setId(idTv1);
			tv1.setText(name);
			int idTv2 = j++;
			tv2.setId(idTv2);
			tv2.setText("                    ");
			int idTv3 = j++;
			tv3.setId(idTv3);
			System.out.println("HERE");
			tv3.setText(Double.toString(balanceAmt));
			
			RelativeLayout rl = new RelativeLayout(getApplicationContext());
			RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT); 
			rl.setLayoutParams(relativeParams);
			rl.setGravity(Gravity.CENTER_HORIZONTAL);
			
			rl.addView(tv1);
			
			RelativeLayout.LayoutParams relativeParams1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT); 
			relativeParams1.addRule(RelativeLayout.RIGHT_OF, tv1.getId());
			
			tv2.setLayoutParams(relativeParams1);
			rl.addView(tv2);
			
			relativeParams1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT); 
			relativeParams1.addRule(RelativeLayout.RIGHT_OF, tv2.getId());
			
			tv3.setLayoutParams(relativeParams1);
			rl.addView(tv3);
			
			tl.addView(rl); 
				
		}
		}

		db.close();

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

}
