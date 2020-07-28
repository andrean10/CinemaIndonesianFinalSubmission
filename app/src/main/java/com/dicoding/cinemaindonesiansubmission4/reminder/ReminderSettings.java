package com.dicoding.cinemaindonesiansubmission4.reminder;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.dicoding.cinemaindonesiansubmission4.R;

import static com.dicoding.cinemaindonesiansubmission4.reminder.Reminder.ID_DAILY_REMIND;
import static com.dicoding.cinemaindonesiansubmission4.reminder.Reminder.ID_RELEASE_REMIND;

public class ReminderSettings extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private String DAILYREMIND;
    private String RELEASEREMIND;
    private SwitchPreference dailyPreferences;
    private SwitchPreference releasePreferences;

    private Reminder reminder;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.reminder_settings);
        reminder = new Reminder();

        init();
        setSummaries();
    }

    private void init() {
        DAILYREMIND = getResources().getString(R.string.key_daily_remind);
        RELEASEREMIND = getResources().getString(R.string.key_release_remind);

        dailyPreferences = findPreference(DAILYREMIND);
        releasePreferences = findPreference(RELEASEREMIND);
    }

    private void setSummaries() {
        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        dailyPreferences.setChecked(sharedPreferences.getBoolean(DAILYREMIND, false));
        releasePreferences.setChecked(sharedPreferences.getBoolean(RELEASEREMIND, false));
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(DAILYREMIND)) {
            if (dailyPreferences.isChecked()) {
                reminder.setDailyRemind(getActivity());
                Toast.makeText(getContext(), getString(R.string.activated_dailyremind), Toast.LENGTH_SHORT).show();
            } else {
                reminder.cancelReminder(getActivity(), ID_DAILY_REMIND);
                Toast.makeText(getContext(), getString(R.string.unactivated_dailyremind), Toast.LENGTH_SHORT).show();
            }
        }

        if (key.equals(RELEASEREMIND)) {
            if (releasePreferences.isChecked()) {
                reminder.setRelaseReminder(getActivity());
                Toast.makeText(getContext(), getString(R.string.activated_releaseremind), Toast.LENGTH_SHORT).show();
            } else {
                reminder.cancelReminder(getActivity(), ID_RELEASE_REMIND);
                Toast.makeText(getContext(), getString(R.string.unactivated_releaseremind), Toast.LENGTH_SHORT).show();
            }
        }
    }
}