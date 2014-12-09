package com.example.helloandroid;

import java.util.ArrayList;
import java.util.List;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.widget.RemoteViews;
import android.widget.Toast;

public class ATAppWidgetProvider extends AppWidgetProvider {
	private static String name;
	private static double balanceAmt = 0;
	private static String status;
	private static int size = 0;
	private static int rows = 5;
	private static int index = 0;
	private static double finalAmt = 0;
	private static ArrayList<WidgetRow> listObtained = new ArrayList<WidgetRow>();
	public static String ACTION_WIDGET_REFRESH = "ActionReceiverRefresh";
	public static String ACTION_WIDGET_NEXT = "ActionReceiverNext";
	public static String ACTION_WIDGET_PREVIOUS = "ActionReceiverPrevious";
	public static String ACTION_WIDGET_SETTINGS = "ActionReceiverSettings";

	@Override
	public void onReceive(Context ctxt, Intent intent) {

		if (intent.getAction().equals(ACTION_WIDGET_REFRESH)) {
			//Resetting the values
			listObtained.clear();
			balanceAmt = 0;
			finalAmt = 0;
			
			getData(ctxt);

			String msg = "";
			if (finalAmt < 0)
				msg = "Its payback time! Pay " + (-finalAmt);
			else if (finalAmt > 0)
				msg = "Today's ur lucky day! Receive " + finalAmt;
			else
				msg = "Fair n Square!";
			Toast.makeText(ctxt, msg, Toast.LENGTH_LONG).show();
		} else if (intent.getAction().equals(ACTION_WIDGET_NEXT)) {
			//Next Button pressed first
			if(listObtained.size() == 0)
				getData(ctxt);
			
			int lastIndex = index;
			
			index += rows;
			
			// Setting the upperlimit
			if (index >= size)
				index = lastIndex;
			
			super.onReceive(ctxt, intent);
		} else if (intent.getAction().equals(ACTION_WIDGET_PREVIOUS)) {
			
			//Next Button pressed first
			if(listObtained.size() == 0)
				getData(ctxt);
			
			index -= rows;
			
			// Setting the lowerlimit
			if (index < 0)
				index = 0;
			

			super.onReceive(ctxt, intent);
		}
		else {
			
			super.onReceive(ctxt, intent);
		}
		ctxt.startService(new Intent(ctxt, UpdateService.class));

	}
	
	@Override
	public void onUpdate(Context ctxt, AppWidgetManager mgr, int[] appWidgetIds) {
		ctxt.startService(new Intent(ctxt, UpdateService.class));
	}

	public static class UpdateService extends IntentService {

		public UpdateService() {
			super("ATAppWidgetProvider$UpdateService");
		}

		@Override
		public void onCreate() {
			super.onCreate();
		}

		@Override
		public void onHandleIntent(Intent intent) {
			ComponentName me = new ComponentName(this,
					ATAppWidgetProvider.class);
			AppWidgetManager mgr = AppWidgetManager.getInstance(this);

			mgr.updateAppWidget(me, buildUpdate(this));
		}

		private RemoteViews buildUpdate(Context context) {
			RemoteViews updateViews = new RemoteViews(context.getPackageName(),
					R.layout.atwidgetlayout);

			// Refreshing
			updateViews.removeAllViews(R.id.widParLinLayout);

			for (int j = index, count = 0; count < rows && j < size; j++, count++) {


				status = listObtained.get(j).getStatus();
				name = listObtained.get(j).getName();

				RemoteViews newView = new RemoteViews(context.getPackageName(),
						R.layout.atwidgetrow);

				newView.setTextViewText(R.id.wid3, status);
				if(status.contains("Pay"))
					newView.setTextColor(R.id.wid3, Color.RED);
				else if(status.contains("Receive"))
					newView.setTextColor(R.id.wid3, Color.GREEN);
				newView.setTextViewText(R.id.wid1, name);

				updateViews.addView(R.id.widParLinLayout, newView);
			}

			Intent i = new Intent(this, ATAppWidgetProvider.class);
			i.setAction(ACTION_WIDGET_REFRESH);
			PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);

			updateViews.setOnClickPendingIntent(R.id.refresh, pi);

			i = new Intent(this, ATAppWidgetProvider.class);
			i.setAction(ACTION_WIDGET_PREVIOUS);
			pi = PendingIntent.getBroadcast(context, 0, i, 0);

			updateViews.setOnClickPendingIntent(R.id.prevButton, pi);

			i = new Intent(this, ATAppWidgetProvider.class);
			i.setAction(ACTION_WIDGET_NEXT);
			pi = PendingIntent.getBroadcast(context, 0, i, 0);

			updateViews.setOnClickPendingIntent(R.id.nxtButton, pi);
			
			return (updateViews);
		}
	}

	private static void getData(Context context) {
		DBAdapter db = new DBAdapter(context);

		db.open();

		List<String> listOfNames = getlist(context);
		size = listOfNames.size();
		for (int i = 0; i < listOfNames.size(); i++) {

			balanceAmt = 0;

			name = listOfNames.get(i);

			Cursor c = db.getRecordByName(name);
			if (c.moveToFirst()) {
				do {
					if (c.getString(3).equalsIgnoreCase("Pay")
							&& c.getString(7).equalsIgnoreCase("PENDING")) {
						balanceAmt -= Double.parseDouble(c.getString(6));
					} else if (c.getString(3).equalsIgnoreCase("Receive")
							&& c.getString(7).equalsIgnoreCase("PENDING")) {
						balanceAmt += Double.parseDouble(c.getString(6));
					}
				} while (c.moveToNext());

				finalAmt += balanceAmt;

				if (balanceAmt < 0)
					status = "Pay " + (-balanceAmt);
				else if (balanceAmt > 0)
					status = "Receive " + balanceAmt;
				else
					status = "Account Settled!";

				WidgetRow wr = new WidgetRow();
				wr.setName(name);
				wr.setStatus(status);

				listObtained.add(wr);

			}
		}

		db.close();
	}

	public static List<String> getlist(Context c) {

		// creates a new arraylist for the results
		ArrayList<String> list = new ArrayList<String>();

		// calls the DBAdapter for the cursor (again, best way I can describe
		// it)
		DBAdapter db = new DBAdapter(c);
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