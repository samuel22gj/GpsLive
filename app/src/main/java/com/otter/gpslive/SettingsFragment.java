package com.otter.gpslive;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;

public class SettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = SettingsFragment.class.getSimpleName();

    public static final String KEY_UPDATE_FREQ_TIME = "prefs_update_freq_time";
    public static final String KEY_UPDATE_FREQ_DISTANCE = "prefs_update_freq_distance";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        initSummary(getPreferenceScreen());
    }

    @Override
    public void onResume() {
        super.onResume();

        getPreferenceScreen()
                .getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        getPreferenceScreen()
                .getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updatePrefsSummary(findPreference(key));
    }

    private void initSummary(Preference p) {
        if (p instanceof PreferenceGroup) {
            PreferenceGroup pGrp = (PreferenceGroup) p;
            for (int i = 0; i < pGrp.getPreferenceCount(); i++) {
                initSummary(pGrp.getPreference(i));
            }
        } else {
            updatePrefsSummary(p);
        }
    }

    private void updatePrefsSummary(Preference p) {
        if (p instanceof ListPreference) {
            ListPreference listPrefs = (ListPreference) p;
            p.setSummary(listPrefs.getEntry());
        } else if (p instanceof EditTextPreference) {
            EditTextPreference editTextPrefs = (EditTextPreference) p;
            if (p.getTitle().toString().contains("assword")) {
                p.setSummary("********");
            } else {
                p.setSummary(editTextPrefs.getText());
            }
        }
    }
}
