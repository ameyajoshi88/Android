package com.example.helloandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

public class Welcome extends Activity

{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		new CountDownTimer(5000, 300) {
			public void onTick(long millisUntilFinished) { // called every 300
															// milliseconds,
															// which could be
															// used to
				// display some crude animation
				setContentView(R.layout.welcome);
			}

			public void onFinish() {
				// After 60000 milliseconds (60 sec) finish current
				// activity and open next activity
				Intent i = new Intent(getApplicationContext(),
						HelloAndroid.class);
				startActivity(i);
			}
		}.start();

	}
}
