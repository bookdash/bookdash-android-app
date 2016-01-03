package org.bookdash.android.presentation.listbooks;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import org.bookdash.android.presentation.about.AboutActivity;
import org.bookdash.android.presentation.activity.BaseAppCompatActivity;
import org.bookdash.android.presentation.bookinfo.BookInfoActivity;
import org.bookdash.android.domain.pojo.BookDetail;
import org.bookdash.android.presentation.view.AutofitRecyclerView;

import java.util.List;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;


public class ListBooksActivity extends BaseAppCompatActivity implements ListBooksContract.View {

    private static final int INVITE_REQUEST_CODE = 1;
    private ListBooksContract.UserActionsListener actionsListener;
    private GoogleApiClient googleApiClient;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Button buttonRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionsListener = new ListBooksPresenter(this, Injection.provideBookRepo(), Injection.provideSettingsRepo(this));
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setUpNavDrawer();
        circularProgressBar = (CircularProgressBar) findViewById(R.id.activity_loading_books);
        linearLayoutErrorScreen = (LinearLayout) findViewById(R.id.linear_layout_error);
        buttonRetry = (Button) findViewById(R.id.button_retry);
        textViewErrorMessage = (TextView) findViewById(R.id.text_view_error_screen);
        mRecyclerView = (AutofitRecyclerView) findViewById(R.id.recycler_view_books);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = 8;
                outRect.right = 8;
                outRect.left = 8;
                outRect.top = 8;
            }
        });
        buttonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Retry button clicked");
                actionsListener.loadBooksForLanguagePreference(false);
            }
        });

        actionsListener.loadLanguages();
        actionsListener.loadBooksForLanguagePreference(false);
        checkIfComingFromInvite();
    }

    private void setUpNavDrawer() {
        if (toolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }
        navigationView.setCheckedItem(R.id.action_all_books);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {

                    case R.id.action_all_books: {
                        showAllBooks();
                        break;
                    }
                    case R.id.action_downloads:
                        showDownloadedBooks();
                        break;
                    case R.id.action_about:
                        showAboutPage();
                        break;
                    case R.id.action_settings: {
                        showSettingsScreen();
                        break;
                    }
                    case R.id.action_thanks: {
                        showThanksPopover();
                        break;
                    }
                    case R.id.action_invite_friends: {
                        openInvitePage();
                        break;
                    }
                    case R.id.action_rate_app: {
                        showRatingPlayStore();
                        break;
                    }
                    default:

                }
                drawerLayout.closeDrawers();
                return true;
            }


        });
    }

    private void showDownloadedBooks() {
        downloadOnly = true;

        actionsListener.loadBooksForLanguagePreference(downloadOnly);
    }

    private void showAllBooks() {
        downloadOnly = false;
        actionsListener.loadBooksForLanguagePreference(downloadOnly);
    }

    private void showSettingsScreen() {

    }

    private void checkIfComingFromInvite() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(AppInvite.API)
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        Log.d(TAG, "onConnectionFailed: onResult:" + connectionResult.toString());

                    }
                })
               /* .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        Log.d(TAG, "onConnectionFailed: onResult:" + connectionResult.toString());

                    }
                })*/
                .build();
        if (googleApiClient != null) {
            googleApiClient.connect();

            AppInvite.AppInviteApi.getInvitation(googleApiClient, this, true)
                    .setResultCallback(
                            new ResultCallback<AppInviteInvitationResult>() {
                                @Override
                                public void onResult(AppInviteInvitationResult result) {
                                    Log.d(TAG, "getInvitation:onResult:" + result.getStatus());
                                }
                            });
        }
    }

    private static final String TAG = ListBooksActivity.class.getCanonicalName();

    private AutofitRecyclerView mRecyclerView;
    private CircularProgressBar circularProgressBar;
    private LinearLayout linearLayoutErrorScreen;
    private TextView textViewErrorMessage;

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient != null) {

            googleApiClient.disconnect();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    private View.OnClickListener bookClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //    final BookDetail bookDetail = (BookDetail) v.getTag();

            openBookDetails(v);
        }
    };

    @Override
    protected String getScreenName() {
        return "BookListingScreen";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_about) {
            showAboutPage();
            return true;
        }
        if (id == R.id.action_language_choice) {
            actionsListener.clickOpenLanguagePopover();
            return true;
        }
        if (id == R.id.action_rate_app) {
            showRatingPlayStore();
            return true;
        }
        if (id == R.id.action_thanks) {
            showThanksPopover();
            return true;
        }
        if (id == R.id.action_invite_friends) {
            openInvitePage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openInvitePage() {
        try {
            Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                    .setMessage(getString(R.string.invitation_message))
                    .setCallToActionText(getString(R.string.invitation_cta))
                    // .setDeepLink(Uri.parse("http://bookdash.org/books/dK5BJWxPIf"))
                    .build();
            startActivityForResult(intent, INVITE_REQUEST_CODE);
        } catch (ActivityNotFoundException ac) {
            Snackbar.make(mRecyclerView, R.string.common_google_play_services_api_unavailable_text, Snackbar.LENGTH_LONG).show();
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

    private boolean downloadOnly = false;
    private DialogInterface.OnClickListener languageClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (dialog != null) {
                dialog.dismiss();
            }

            actionsListener.saveSelectedLanguage(which, downloadOnly);

          /*  tracker.send(new HitBuilders.EventBuilder()
                    .setCategory("LanguageChange")
                    .setAction(languages.get(which).getLanguageName())
                    .build());*/


        }
    };

    @Override
    public void showLanguagePopover(String[] languages, int selected) {
        AlertDialog alertDialogLanguages = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.language_selection_heading))
                .setSingleChoiceItems(languages, selected, languageClickListener).create();
        alertDialogLanguages.show();
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
        Intent intent = new Intent(ListBooksActivity.this, AboutActivity.class);

        startActivity(intent);
    }


    public void openBookDetails(View v) {
      /*  if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            ImageView imageView =
                    ((BookViewHolder) v.getTag()).mImageView;
            imageView.setTransitionName(getString(R.string.transition_book));
            v.setBackgroundColor(
                    ContextCompat.getColor(ListBooksActivity.this, android.R.color.transparent));
            ActivityOptions options =
                    ActivityOptions.makeSceneTransitionAnimation(ListBooksActivity.this,
                            Pair.create((View) imageView, getString(R.string.transition_book)));


            Intent intent = new Intent(ListBooksActivity.this, BookInfoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(BookInfoActivity.BOOK_PARCEL, ((BookViewHolder) v.getTag()).bookDetail.toBookParcelable());
            startActivity(intent, options.toBundle());

        } else {*/

        Intent intent = new Intent(ListBooksActivity.this, BookInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        BookViewHolder viewHolder = (BookViewHolder) v.getTag();
        BookDetail bookDetailResult = viewHolder.bookDetail;
        intent.putExtra(BookInfoActivity.BOOK_PARCEL, bookDetailResult.toBookParcelable());
        startActivity(intent);

       /* }*/
    }

    @Override
    public void showRatingPlayStore() {
        Uri uri = Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)));
        }
    }

    @Override
    public void showErrorScreen(boolean show, String errorMessage, boolean showRetryButton) {
        if (show) {
            linearLayoutErrorScreen.setVisibility(View.VISIBLE);
        } else {
            linearLayoutErrorScreen.setVisibility(View.GONE);
        }
        buttonRetry.setVisibility(showRetryButton ? View.VISIBLE : View.GONE);
        textViewErrorMessage.setText(errorMessage);

    }

    @Override
    public void showLoading(boolean visible) {
        circularProgressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
        mRecyclerView.setVisibility(visible ? View.GONE : View.VISIBLE);

    }

    @Override
    public void showBooks(List<BookDetail> bookDetailList) {
        if (bookDetailList.isEmpty()) {
            if (downloadOnly) {
                showErrorScreen(true, getString(R.string.no_books_downloaded), false);

            } else {
                showErrorScreen(true, getString(R.string.no_books_available), true);

            }
            // return;
        }
        RecyclerView.Adapter mAdapter = new BookAdapter(bookDetailList, ListBooksActivity.this, bookClickListener);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.scheduleLayoutAnimation();
    }

    @Override
    public void showSnackBarError(int message) {
        Snackbar.make(mRecyclerView, message, Snackbar.LENGTH_LONG).show();
    }

}
