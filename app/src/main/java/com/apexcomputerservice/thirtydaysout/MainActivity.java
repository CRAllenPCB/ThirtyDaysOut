package com.apexcomputerservice.thirtydaysout;


import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;


public class MainActivity extends AppCompatActivity {

    EditText days;
    EditText startDateTxt;
    TextView display, result;
    Button addToCal;
    String strStartDate, strEndDate;
    Calendar startC, endC;
    SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
    SimpleDateFormat sdf2 = new SimpleDateFormat("EEE MMMM d, yyyy", Locale.US);
    int daysInt, passMonth, passYear, passDay;
    Date startDate;
    ShareActionProvider mShareActionProvider;

    private FirebaseAnalytics mFirebaseAnalytics;




    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            startC.set(Calendar.YEAR, year);
            startC.set(Calendar.MONTH, month);
            startC.set(Calendar.DAY_OF_MONTH, day);
            getStartDateText();
            getEndDateText();
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        days = (EditText) findViewById (R.id.etDays); // The number of days to add
        display = (TextView) findViewById (R.id.tvToday); // Display
        startDateTxt = (EditText) findViewById(R.id.etStartDate); //Start date
        result = (TextView) findViewById(R.id.tvResult); // End date
        addToCal = (Button) findViewById(R.id.bAddCal); // Add to Calendar button





        AdView mAdView = (AdView) findViewById(R.id.adView);
        // *** Use the commented line below in real app ***
         AdRequest adRequest = new AdRequest.Builder().build();
        // REMOVE TEST
       // AdRequest request = new AdRequest.Builder()
       //         .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // .addTestDevice("My MD5-hased ID for phone")
       //         .build();
        mAdView.loadAd(adRequest);

        getStartDate();
        //Get preferences
        updatePreferences();

        startDateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, date, startC.get(Calendar.YEAR),
                        startC.get(Calendar.MONTH), startC.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

       days.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               // Nothing to do here
           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               // Nothing to do here
           }

           @Override
           public void afterTextChanged(Editable s) {
               // After Text changed, start work
               getDaysInt();
               getEndDateText();
           }
       });

    }


    public void onResume(){
        super.onResume();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean prefsUpdated = pref.getBoolean("prefsChanged",false);
        if (prefsUpdated) {
            updatePreferences();
        }
    }


    public void updatePreferences() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String prefDays = sharedPrefs.getString("daysPref", "");
        days.setText(prefDays);
        getDaysInt();

        //Date format changed
        String dateFormat = sharedPrefs.getString("pref_key_date","MMMM d, yyyy");
        sdf = new SimpleDateFormat(dateFormat, Locale.US);
        sdf2 = new SimpleDateFormat("EEE " + dateFormat, Locale.US);
        getStartDateText();
        getEndDateText();
        // Intent to update widget
        Intent iw = new Intent(this,Widget.class);
        iw.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), Widget.class));
        iw.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        sendBroadcast(iw);
    }

    public void getDaysInt(){
        if (days.getText().toString().isEmpty())
            daysInt = 0;
         else
            daysInt = Integer.parseInt(days.getText().toString());
    }

    public void getStartDate() {
        // Get current date and format to string
        startC = Calendar.getInstance();
        startDate = startC.getTime();
        getStartDateText();
    }

    public void getStartDateText() {
        strStartDate = sdf.format(startC.getTime());
        startDateTxt.setText(strStartDate);
    }

    public void getEndDateText(){
        endC = (Calendar)startC.clone();
        endC.add(Calendar.DAY_OF_MONTH, daysInt);
        strEndDate = sdf2.format(endC.getTime());
        String resultString = new String ("Will be " + strEndDate);
        result.setText(resultString);
        passYear = endC.get(Calendar.YEAR);
        passMonth = endC.get(Calendar.MONTH);
        passDay =  endC.get(Calendar.DAY_OF_MONTH);
    }



    public void addToCalendar(View view){
        Intent a = new Intent(this,AddToCalendar.class);
        a.putExtra("endDate", strEndDate);
        a.putExtra("passYear", passYear);
        a.putExtra("passMonth", passMonth);
        a.putExtra("passDay", passDay);
        startActivity(a);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public Intent doShare() {
        // populate the share intent with data
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Thirty Days Out is a great date calculator.\n"+getString(R.string.play_link);
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Thirty Days Out");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        //startActivity(Intent.createChooser(sharingIntent, "Share via"));
        return sharingIntent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        mShareActionProvider.setShareIntent(doShare());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_item_share:
                mShareActionProvider.setShareIntent(doShare());
                return true;
            case R.id.preferences:
                // Preferences selected
                Intent i = new Intent(this, Prefs.class);
                startActivity(i);
                return true;
            case R.id.about:
                //About selected
                Intent a = new Intent(this, About.class);
                startActivity(a);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

