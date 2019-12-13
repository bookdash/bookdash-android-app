package org.bookdash.android.presentation.main;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.bookdash.android.BuildConfig;
import org.bookdash.android.Injection;
import org.bookdash.android.R;
import org.bookdash.android.presentation.about.AboutFragment;
import org.bookdash.android.presentation.activity.BaseAppCompatActivity;
import org.bookdash.android.presentation.downloads.DownloadsFragment;
import org.bookdash.android.presentation.listbooks.ListBooksFragment;
import org.bookdash.android.presentation.settings.SettingsFragment;


public class MainActivity extends BaseAppCompatActivity implements MainContract.MainView, NavDrawerInterface {

    private static final int INVITE_REQUEST_CODE = 1;
    private static final String TAG = "MainActivity";
    private static final String GOOGLE_PLAY_STORE_URL = "http://play.google.com/store/apps/details?id=";
    private static final String GOOGLE_PLAY_MARKET_URL = "market://details?id=";
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MainContract.MainUserActions mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        mainPresenter = new MainPresenter(this, Injection.provideAnalytics());
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setUpNavDrawer();
        showAllBooks();
    }

    private void setUpNavDrawer() {

        navigationView.setCheckedItem(R.id.action_all_books);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.action_all_books: {
                        mainPresenter.clickViewAllBooks();
                        break;
                    }
                    case R.id.action_downloads:
                        mainPresenter.clickViewDownloadBooks();
                        break;
                    case R.id.action_about:
                        showAboutPage();
                        break;
                    case R.id.action_settings: {
                        mainPresenter.clickShowSettings();
                        break;
                    }
                    case R.id.action_thanks: {
                        showThanksPopover();
                        break;
                    }
                    case R.id.action_invite_friends: {
                        mainPresenter.clickInvitePage();
                        break;
                    }
                    case R.id.action_rate_app: {
                        mainPresenter.clickRateApp();
                        break;
                    }
                    default:

                }
                drawerLayout.closeDrawers();
                if (menuItem.getItemId() == R.id.action_thanks || menuItem
                        .getItemId() == R.id.action_invite_friends || menuItem.getItemId() == R.id.action_rate_app) {
                    return false;
                } else {
                    return true;
                }

            }


        });
    }

    private void showAllBooks() {
        mainPresenter.clickViewAllBooks();
    }

    public void showSettingsScreen() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment settingsFragment = new SettingsFragment();
        ft.replace(R.id.fragment_content, settingsFragment, "SETTINGS");
        ft.commit();
    }

    @Override
    public void showThanksPopover() {
        AlertDialog.Builder thanksDialog = new AlertDialog.Builder(this);
        thanksDialog.setTitle(getString(R.string.contributions_to_app));
        thanksDialog.setMessage(Html.fromHtml(getString(R.string.list_of_contributors)));

        thanksDialog.setPositiveButton(android.R.string.ok, null);
        AlertDialog ad = thanksDialog.show();
        ((TextView) ad.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());

    }

    @Override
    public void showAboutPage() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment f = AboutFragment.newInstance();
        ft.replace(R.id.fragment_content, f, "ABOUT");
        ft.commit();
    }

    @Override
    public void showRatingPlayStore() {
        Uri uri = Uri.parse(GOOGLE_PLAY_MARKET_URL + BuildConfig.APPLICATION_ID);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            try {
                startActivity(
                        new Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_PLAY_STORE_URL + BuildConfig.APPLICATION_ID)));
            } catch (ActivityNotFoundException anfe) {
                Snackbar.make(navigationView, R.string.error_opening_app_rating, Snackbar.LENGTH_LONG);
            }
        }
    }

    @Override
    public void showAllBooksPage() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment f = ListBooksFragment.newInstance();
        ft.replace(R.id.fragment_content, f, "ALLBOOKS");
        ft.commit();
    }

    @Override
    public void showDownloadedBooksPage() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment f = DownloadsFragment.newInstance();
        ft.replace(R.id.fragment_content, f, "DOWNLOADED_BOOKS");
        ft.commit();
    }

    @Override
    public void inviteFriends() {
        try {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.invitation_message));
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.invitation_subject));
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent,
                    getResources().getText(R.string.invite_using)));
        } catch (ActivityNotFoundException ac) {
            Snackbar.make(navigationView, R.string.invite_error_no_apps_found, Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public String getScreenName() {
        return "MainActivity";
    }

    @Override
    public void openNavDrawer() {
        drawerLayout.openDrawer(navigationView);
    }

    @Override
    public void closeNavDrawer() {
        drawerLayout.closeDrawer(navigationView);
    }

    @Override
    public void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }
}