package org.bookdash.android.presentation.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import org.bookdash.android.Injection;
import org.bookdash.android.R;
import org.bookdash.android.data.settings.SettingsApiImpl;

import java.util.ArrayList;

import za.co.riggaroo.materialhelptutorial.TutorialItem;
import za.co.riggaroo.materialhelptutorial.tutorial.MaterialTutorialActivity;


public class SettingsFragment extends PreferenceFragmentCompat implements SettingsContract.View {
    public static final String TUTORIAL_DISPLAY_KEY = "tutorial_display_key";
    SettingsPresenter presenter;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        presenter = new SettingsPresenter(Injection.provideTutorialRepo(getContext()), Injection.provideAnalytics(),
                Injection.provideSettingsRepo(getContext()));
        presenter.attachView(this);

        addPreferencesFromResource(R.xml.app_preferences);
        setupTutorialDisplayPreference();
        setupNewBookNotificationPreference();
    }

    private void setupTutorialDisplayPreference() {
        Preference tutorialPreferenceItem = findPreference(TUTORIAL_DISPLAY_KEY);
        tutorialPreferenceItem.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                presenter.openTutorialScreen();
                return true;
            }
        });
    }

    private void setupNewBookNotificationPreference() {
        Preference notificationPreference = findPreference(SettingsApiImpl.PREF_IS_SUBSCRIBED_NEW_BOOK_NOTIFICATIONS);
        notificationPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(final Preference preference, final Object newValue) {
                presenter.setNewBookNotificationSubscriptionStatus((Boolean) newValue);
                return true;
            }
        });
    }

    @Override
    public void showTutorialScreen(ArrayList<TutorialItem> tutorialItems) {
        Intent mainAct = new Intent(getContext(), MaterialTutorialActivity.class);
        mainAct.putParcelableArrayListExtra(MaterialTutorialActivity.MATERIAL_TUTORIAL_ARG_TUTORIAL_ITEMS,
                tutorialItems);
        startActivity(mainAct);
    }
}
