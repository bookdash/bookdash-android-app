package org.bookdash.android.presentation.settings;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import org.bookdash.android.R;

/**
 * Created by michel.onwordi on 26/01/2017.
 */

public class SettingsFragment extends PreferenceFragmentCompat implements SettingsContract.View{
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.app_preferences);
    }

    @Override
    public void showTutorialScreen() {

    }
}
