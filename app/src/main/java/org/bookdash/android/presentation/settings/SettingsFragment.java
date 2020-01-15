package org.bookdash.android.presentation.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import org.bookdash.android.Injection;
import org.bookdash.android.R;
import org.bookdash.android.data.settings.SettingsApiImpl;
import org.bookdash.android.presentation.main.NavDrawerInterface;

import java.util.ArrayList;

import za.co.riggaroo.materialhelptutorial.TutorialItem;
import za.co.riggaroo.materialhelptutorial.tutorial.MaterialTutorialActivity;


public class SettingsFragment extends PreferenceFragmentCompat implements SettingsContract.View {
    public static final String TUTORIAL_DISPLAY_KEY = "tutorial_display_key";
    SettingsPresenter presenter;
    private NavDrawerInterface navDrawerInterface;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        presenter = new SettingsPresenter(Injection.provideTutorialRepo(getContext()), Injection.provideAnalytics(),
                Injection.provideSettingsRepo(getContext()));
        presenter.attachView(this);

        addPreferencesFromResource(R.xml.app_preferences);
        setupTutorialDisplayPreference();
        setupNewBookNotificationPreference();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NavDrawerInterface) {
            navDrawerInterface = (NavDrawerInterface) context;

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        navDrawerInterface = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                if (navDrawerInterface != null) {
                    navDrawerInterface.openNavDrawer();
                }
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar =  view.findViewById(R.id.toolbar);

        navDrawerInterface.setToolbar(toolbar);

        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();


        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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
