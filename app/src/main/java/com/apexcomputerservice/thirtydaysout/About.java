package com.apexcomputerservice.thirtydaysout;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Chris on 8/29/2016.
 */
public class About extends AppCompatActivity {

    TextView versionText;
    String versionUpdateText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Context mContext = getApplicationContext();

        versionText = (TextView) findViewById (R.id.tvVersion);

        versionUpdateText = "Version "+ getPackageVersionNum(mContext);
        versionText.setText(versionUpdateText);

    }

    public static String getPackageVersionNum(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = null;
        int versionNum = 0;
        String versionName = "";
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionNum = pi.versionCode;
            versionName = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}