package org.bookdash.android.presentation.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import org.bookdash.android.Injection;
import org.bookdash.android.R;

import java.util.ArrayList;

import za.co.riggaroo.materialhelptutorial.TutorialItem;
import za.co.riggaroo.materialhelptutorial.tutorial.MaterialTutorialActivity;

/**
 * Created by michel.onwordi on 26/01/2017.
 */

public class SettingsFragment extends PreferenceFragmentCompat implements SettingsContract.View {
    SettingsPresenter presenter;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        presenter = new SettingsPresenter(Injection.provideTutorialRepo(getContext()));
        presenter.attachView(this);

        addPreferencesFromResource(R.xml.app_preferences);
        setupTutorialDisplayPreference();
    }

    @Override
    public void showTutorialScreen(ArrayList<TutorialItem> tutorialItems) {
        Intent mainAct = new Intent(getContext(), MaterialTutorialActivity.class);
        mainAct.putParcelableArrayListExtra(MaterialTutorialActivity.MATERIAL_TUTORIAL_ARG_TUTORIAL_ITEMS, tutorialItems);
        startActivity(mainAct);
    }

    private void setupTutorialDisplayPreference() {
        Preference tutorialPreferenceItem = findPreference("tutorial_display_key");
        tutorialPreferenceItem.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                presenter.openTutorialScreen();
                return true;
            }
        });
    }
}
