package org.bookdash.android.presentation.bookinfo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.bookdash.android.BookDashApplication;
import org.bookdash.android.Injection;
import org.bookdash.android.R;
import org.bookdash.android.databinding.ActivityBookInformationBinding;
import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.domain.model.firebase.FireContributor;
import org.bookdash.android.domain.model.gson.BookPages;
import org.bookdash.android.presentation.activity.BaseAppCompatActivity;
import org.bookdash.android.presentation.readbook.BookDetailActivity;

import java.util.List;

import mbanje.kurt.fabbutton.FabButton;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;


public class BookInfoActivity extends BaseAppCompatActivity implements BookInfoContract.View {
    private static final String TAG = "BookInfoActivity";

    public static final String BOOK_PARCEL = "book_parcel";
    /**
     * Presenter object
     */
    private BookInfoContract.Presenter bookInfoPresenter;

    /**
     * Views
     */
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FabButton floatingActionButton;
    private View gradientBackground;
    private RecyclerView contributorRecyclerView;
    private View scrollView;
    private ImageView imageViewBook;
    private AppBarLayout appBarLayout;
    private CoordinatorLayout coordinatorLayout;
    private View errorLayout;
    private TextView errorText;
    private ActivityBookInformationBinding binding;
    private ProgressBar loadingProgressBar;
    private CardView contributorCard, mainBookCard;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private FireBookDetails bookInfo;
    private Button errorRetryButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_information);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getSharedElementReturnTransition().addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {
                    floatingActionButton.animate().scaleY(0).scaleX(0).setInterpolator(new AccelerateInterpolator()).setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime)).start();

                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    enterAnimation();
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
            postponeEnterTransition();
        }
        contributorRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_contributors);
        contributorCard = (CardView) findViewById(R.id.contributor_card);
        imageViewBook = (ImageView) findViewById(R.id.image_view_book_cover);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mainBookCard = (CardView) findViewById(R.id.card_view_main_book_info);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout_content);
        errorLayout = findViewById(R.id.linear_layout_error);
        errorText = (TextView) findViewById(R.id.text_view_error_screen);

        errorRetryButton = (Button) findViewById(R.id.button_retry);
        loadingProgressBar = (ProgressBar) findViewById(R.id.activity_loading_book_info);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        scrollView = findViewById(R.id.scrollViewBookInfo);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        Typeface tp = TypefaceUtils.load(getAssets(), BookDashApplication.DEFAULT_FONT_LOCATION);
        collapsingToolbarLayout.setExpandedTitleTypeface(tp);
        collapsingToolbarLayout.setCollapsedTitleTypeface(tp);
        gradientBackground = findViewById(R.id.toolbar_background_gradient);
        floatingActionButton = (FabButton) findViewById(R.id.fab_download);
        floatingActionButton.setScaleX(0);
        floatingActionButton.setScaleY(0);
        binding.setDownloadClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookInfo == null) {
                    showSnackBarMessage(R.string.book_not_available);
                    return;
                }
                if (!bookInfo.isDownloadedAlready()) {
                    floatingActionButton.resetIcon();
                    floatingActionButton.showProgress(true);
                    floatingActionButton.setProgress(0, false);
                }
                bookInfoPresenter.downloadBook(bookInfo);
            }
        });


        bookInfoPresenter = new BookInfoPresenter(
                Injection.provideBookService(), Injection.provideDownloadService(), Schedulers.io(), AndroidSchedulers.mainThread());
        bookInfoPresenter.attachView(this);
        calculateLayoutHeight();
        imageViewBook.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver
                .OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                imageViewBook.getViewTreeObserver().removeOnPreDrawListener(this);

                enterAnimation();
                return true;
            }
        });
        final FireBookDetails bookDetailParcelable = getIntent().getParcelableExtra(BOOK_PARCEL);

        if (bookDetailParcelable != null) {
            setBookInfoBinding(bookDetailParcelable);
            startLoadingBook(bookDetailParcelable);
            showBookDetailView();
        } else {
            onNewIntent(getIntent());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_share_book) {
            bookInfoPresenter.shareBookClicked(bookInfo);
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void loadImage(String url) {
        Glide.with(this).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                onImageLoaded(resource);
                extractPaletteColors(resource);
            }
        });
    }

    private void startLoadingBook(final FireBookDetails bookDetailId) {
        errorRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookInfoPresenter.loadContributorInformation(bookDetailId);
            }
        });

        bookInfoPresenter.loadContributorInformation(bookDetailId);
        showBookDetailView();
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String action = intent.getAction();
        String data = intent.getDataString();
        Log.d(TAG, "onNewIntent() called: action" + action);

        if (Intent.ACTION_VIEW.equals(action) && data != null) {
            Uri uri = Uri.parse(data);
            String bookId = uri.getLastPathSegment();
            String invitationId = uri.getQueryParameter("invitation_id");
            Log.d(TAG, "Action View: book id:" + bookId + ". Full URL:" + uri.toString() + ". InvitationId:" + invitationId);
            //  startLoadingBook(bookId);
        }
    }

    private void enterAnimation() {

        floatingActionButton.setScaleX(0);
        floatingActionButton.setScaleY(0);
        floatingActionButton.animate().setStartDelay(500).scaleY(1).scaleX(1).setInterpolator(new OvershootInterpolator()).setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime)).start();
    }


    @SuppressWarnings("SuspiciousNameCombination")
    private void calculateLayoutHeight() {
        if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            Log.d(TAG, "Setting image height");
            DisplayMetrics dMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
            CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams) imageViewBook.getLayoutParams();
            lp.height = dMetrics.widthPixels;
            imageViewBook.setLayoutParams(lp);

            CoordinatorLayout.LayoutParams lp2 = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
            lp2.height = dMetrics.widthPixels;
            appBarLayout.setLayoutParams(lp2);
        }
    }

    private void extractPaletteColors(Bitmap resource) {
        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                if (null == palette) {
                    return;
                }
                Log.d("onGenerated", palette.toString());
                Palette.Swatch toolbarSwatch = palette.getMutedSwatch();

                int toolbarColor = toolbarSwatch != null ? toolbarSwatch.getRgb() : ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
                int accentColor = palette.getVibrantColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));

                setToolbarColor(toolbarColor);
                setAccentColor(accentColor);

                if (toolbarSwatch != null) {
                    float[] darkerShade = toolbarSwatch.getHsl();
                    darkerShade[2] = darkerShade[2] * 0.8f; //Make it a darker shade for the status bar
                    setStatusBarColor(ColorUtils.HSLToColor(darkerShade));
                } else {
                    setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
                }
            }
        });
    }

    @Override
    protected String getScreenName() {
        return "BookInfoActivity";
    }

    @Override
    public void showBookDetailView() {
        mainBookCard.setVisibility(View.VISIBLE);
        loadingProgressBar.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
        coordinatorLayout.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.VISIBLE);
        floatingActionButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(String errorMessage) {
        loadingProgressBar.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
        errorText.setText(errorMessage);
    }

    @Override
    public void showSnackBarMessage(int message) {
        Snackbar.make(scrollView, message, Snackbar.LENGTH_LONG).show();

    }

    private int progress = 0;

    @Override
    public void showDownloadProgress(final int downloadProgress) {
        if (progress == downloadProgress) {
            return;
        }
        progress = downloadProgress;
        Log.d(TAG, "Download progress:" + downloadProgress);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                floatingActionButton.setProgress(downloadProgress, false);
            }
        });

    }

    @Override
    public void showDownloadFinished() {
        Log.d(TAG, "Download finished");
        floatingActionButton.resetIcon();
        floatingActionButton.setProgress(100, false);
    }

    @Override
    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
        if (collapsingToolbarLayout != null) {
            collapsingToolbarLayout.setTitle(title);
        }
    }

    @Override
    public void setBookInfoBinding(FireBookDetails bookInfo) {
        this.bookInfo = bookInfo;
        binding.setBookInfo(bookInfo);
        loadImage(bookInfo.getBookCoverUrl());
    }

    @Override
    public void openBook(FireBookDetails bookDetail, BookPages bookPages, String location) {
        if (isFinishing()) {
            return;
        }
        Intent intent = new Intent(BookInfoActivity.this, BookDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(BookDetailActivity.BOOK_PAGES, bookPages);
        intent.putExtra(BookDetailActivity.BOOK_ARG, bookDetail.getBookTitle());
        intent.putExtra(BookDetailActivity.LOCATION_BOOK, location);
        startActivity(intent);
    }

    @Override
    public void showContributors(List<FireContributor> list) {
        if (list == null || list.size() == 0) {
            contributorCard.setVisibility(View.GONE);
            return;
        }
        contributorCard.setVisibility(View.VISIBLE);
        ContributorAdapter contributorAdapter = new ContributorAdapter(list, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        contributorRecyclerView.setLayoutManager(linearLayoutManager);
        contributorRecyclerView.setAdapter(contributorAdapter);
    }


    private void onImageLoaded(Bitmap bitmap) {
        imageViewBook.setImageBitmap(bitmap);

    }

    private void setStatusBarColor(int color) {
        if (isFinishing()) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(color);
        }
    }


    private void setAccentColor(int accentColor) {
        floatingActionButton.setColor(accentColor);

    }

    private void setToolbarColor(int color) {
        if (collapsingToolbarLayout != null) {
            collapsingToolbarLayout.setStatusBarScrimColor(color);
            collapsingToolbarLayout.setContentScrimColor(color);
        } else {
            actionBar.setBackgroundDrawable(new ColorDrawable(color));
        }
        floatingActionButton.setRingProgressColor(color);

        if (gradientBackground != null) {
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(color);
            gradientDrawable.setAlpha(140);
            gradientBackground.setBackground(gradientDrawable);
        }
    }

    @Override
    public void sendShareEvent(String bookTitle) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.sharing_book_title, bookTitle));
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bookInfoPresenter.detachView();
    }
}
