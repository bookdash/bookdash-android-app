package org.bookdash.android.presentation.main;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;

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
    private GoogleApiClient googleApiClient;
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
        checkIfComingFromInvite();
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

    private void checkIfComingFromInvite() {
        googleApiClient = new GoogleApiClient.Builder(this).addApi(AppInvite.API)
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        Log.d(TAG, "onConnectionFailed: onResult:" + connectionResult.toString());

                    }
                }).build();
        if (googleApiClient != null) {
            googleApiClient.connect();

            AppInvite.AppInviteApi.getInvitation(googleApiClient, this, true)
                    .setResultCallback(new ResultCallback<AppInviteInvitationResult>() {
                        @Override
                        public void onResult(AppInviteInvitationResult result) {
                            Log.d(TAG, "getInvitation:onResult:" + result.getStatus());
                        }
                    });
        }
    }

    private void showAllBooks() {
        mainPresenter.clickViewAllBooks();
    }

    public void showSettingsScreen() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment settingsFragment = new SettingsFragment();
        ft.replace(R.id.fragment_content,settingsFragment,"SETTINGS");
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
            startActivity(
                    new Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_PLAY_STORE_URL + BuildConfig.APPLICATION_ID)));
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
            Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                    .setMessage(getString(R.string.invitation_message))
                    .setCallToActionText(getString(R.string.invitation_cta))
                    // .setDeepLink(Uri.parse("http://bookdash.org/books/dK5BJWxPIf"))
                    .build();
            startActivityForResult(intent, INVITE_REQUEST_CODE);
        } catch (ActivityNotFoundException ac) {
            Snackbar.make(navigationView, R.string.common_google_play_services_unsupported_text, Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public String getScreenName() {
        return "MainActivity";
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient != null) {

            googleApiClient.disconnect();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == INVITE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Check how many invitations were sent and log a message
                // The ids array contains the unique invitation ids for each invitation sent
                // (one for each contact select by the user). You can use these for analytics
                // as the ID will be consistent on the sending and receiving devices.
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                Log.d(TAG, getString(R.string.sent_invitations_fmt, ids.length));
            } else {
                // Sending failed or it was canceled, show failure message to the user
                Log.d(TAG, "invite send failed:" + requestCode + ",resultCode:" + resultCode);
            }
        }
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