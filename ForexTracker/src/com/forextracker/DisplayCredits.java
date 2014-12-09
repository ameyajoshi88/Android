package com.forextracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
//Display the credits page
public class DisplayCredits extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.credits);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent Act2Intent = new Intent(DisplayCredits.this,
					ForexTracker.class);
			startActivity(Act2Intent);
			finish();
			return true;
		}
		return false;
	}
}
