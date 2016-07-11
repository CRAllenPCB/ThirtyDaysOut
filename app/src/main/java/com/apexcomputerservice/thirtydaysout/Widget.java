package com.apexcomputerservice.thirtydaysout;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.Html;
import android.widget.RemoteViews;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;

/**
 * Implementation of App Widget functionality.
 */
public class Widget extends AppWidgetProvider {


    public static final String ACTION_AUTO_UPDATE = "AUTO_UPDATE";


    static SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE \nMMMM d, yyyy", Locale.US);
    //String prefDays;


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String prefDays) {


        //Get current date
        Calendar startC = Calendar.getInstance();
        Date startDate = startC.getTime();

        //Change Pref days to int
        int daysInt = Integer.parseInt(prefDays);

        //Determine end date
        Calendar endC = (Calendar)startC.clone();
        endC.add(Calendar.DAY_OF_MONTH, daysInt);

        String strEndDate = sdf2.format(endC.getTime());




        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget3);


        views.setTextViewText(R.id.wTVDays, prefDays +"\nDays Out" );
        views.setTextViewText(R.id.wTVResult, strEndDate);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

        // Create a pending intent from the detail activity intent
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intent, 0);

        // When a button is clicked, open the detail activity
        views.setOnClickPendingIntent(R.id.wTVDays, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {


        //Get number of days out from preferences
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String prefDays = sharedPrefs.getString("daysPref","30");

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, prefDays);
        }


    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

        //Set Alarm to update widget once a day at 12:05 AM
        WidgetAlarm appWidgetAlarm = new WidgetAlarm(context.getApplicationContext());
        appWidgetAlarm.startAlarm();

        }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        //Stop alarm
        // stop alarm
        WidgetAlarm appWidgetAlarm = new WidgetAlarm(context.getApplicationContext());
        appWidgetAlarm.stopAlarm();
    }
}

