package com.forextracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class ForexTracker extends Activity {
	private Button viewAll;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.home);

		viewAll = (Button) findViewById(R.id.view);
		viewAll.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Intent i = new Intent(getApplicationContext(),
						ViewAllRecords.class);
				startActivity(i);

			}
		});

		final Button button = (Button) findViewById(R.id.make);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Intent i = new Intent(getApplicationContext(), MakeEntry.class);
				startActivity(i);

			}
		});
		
		final Button button1 = (Button) findViewById(R.id.search);
		button1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Intent i = new Intent(getApplicationContext(), SearchByName.class);
				startActivity(i);

			}
		});
		
		final Button button2 = (Button) findViewById(R.id.settle);
		button2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Intent i = new Intent(getApplicationContext(), SettleAccount.class);
				startActivity(i);

			}
		});
		
		final Button button5 = (Button) findViewById(R.id.summary);
		button5.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Intent i = new Intent(getApplicationContext(), ViewSummary.class);
				startActivity(i);

			}
		});
		
		
		final Button button4 = (Button) findViewById(R.id.credits);
		button4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Intent i = new Intent(getApplicationContext(), DisplayCredits.class);
				startActivity(i);

			}
		});
		
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{     
		if(keyCode == KeyEvent.KEYCODE_BACK) 
		{                              
			finish();
			
			Intent intent = new Intent(Intent.ACTION_MAIN);     
			intent.addCategory(Intent.CATEGORY_HOME);     
			startActivity(intent);
			return true;   
		}     
		return false;
	}
}