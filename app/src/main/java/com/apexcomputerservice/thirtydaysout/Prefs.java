package com.apexcomputerservice.thirtydaysout;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by Chris on 9/30/2015.
 */

public class Prefs extends PreferenceActivity {

    boolean prefsChanged = false;
    private OnDataPass mListener;
    public interface OnDataPass{public void onDataPass(boolean prefsChanged);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragment()).commit();
    }

    public static class PrefsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener{

        @Override
        public void onCreate(final Bundle savedInstance){
            super.onCreate(savedInstance);
            addPreferencesFromResource(R.xml.prefs);
            getPreferenceScreen().getEditor().putBoolean("prefsChanged", false).apply();


        }

        @Override
        public void onResume(){
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause(){
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("daysPref"))
            getPreferenceScreen().getEditor().putBoolean("prefsChanged", true).apply();
        }
    }
}
