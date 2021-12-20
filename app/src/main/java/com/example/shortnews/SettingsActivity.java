package com.example.shortnews;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class SettingsActivity extends AppCompatActivity {
    private final String TAG = SettingsActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.settingsFragmentContainer, new SettingsFragment())
                    .commit();
        }
        // TODO: something about actionBar
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat
            implements Preference.OnPreferenceChangeListener{
        private final String TAG = SettingsActivity.class.getSimpleName();

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.root_preferences);

            EditTextPreference pageSizePrefs = findPreference(getResources().getString(R.string.settings_pageSize_key));
            if(pageSizePrefs != null) {
                pageSizePrefs.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                    @Override
                    public void onBindEditText(@NonNull EditText editText) {
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    }
                });
            }
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String newStringValue = newValue.toString();
            preference.setSummary(newStringValue);
            return true;
        }
    }
}