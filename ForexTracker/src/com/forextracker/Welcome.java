package com.forextracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;

public class Welcome extends Activity

{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.welcome);
		
		new CountDownTimer(5000, 5000) {

			public void onTick(long millisUntilFinished) {
			}

			public void onFinish() {
				finish();
				Intent i = new Intent(getApplicationContext(),
						ForexTracker.class);
				startActivity(i);
			}
		}.start();
	}
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{     
		if(keyCode == KeyEvent.KEYCODE_BACK) 
		{                            
			finish();           
			return true;   
			}     
		return false;
	}
}
