package com.smartpantry.manager;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends AppCompatActivity {

    public static final String PREFERENCES_NAME = "pantry_settings";
    public static final String KEY_COMPACT_UNITS = "compact_units";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        NavigationHelper.setup(this, R.id.navSettings);

        SwitchCompat compactUnits = findViewById(R.id.switchCompactUnits);
        boolean enabled = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE)
                .getBoolean(KEY_COMPACT_UNITS, false);
        compactUnits.setChecked(enabled);

        compactUnits.setOnCheckedChangeListener((button, checked) ->
                getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE)
                        .edit()
                        .putBoolean(KEY_COMPACT_UNITS, checked)
                        .apply());
    }
}
