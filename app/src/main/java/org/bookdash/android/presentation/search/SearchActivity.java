package org.bookdash.android.presentation.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.bookdash.android.Injection;
import org.bookdash.android.R;
import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.presentation.activity.BaseAppCompatActivity;
import org.bookdash.android.presentation.bookinfo.BookInfoActivity;
import org.bookdash.android.presentation.listbooks.BookAdapter;
import org.bookdash.android.presentation.listbooks.BookViewHolder;

import java.util.List;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author rebeccafranks
 * @since 2016/12/11
 */

public class SearchActivity extends BaseAppCompatActivity implements SearchContract.View {

    SearchContract.Presenter searchPresenter;
    private Toolbar toolbar;
    private String searchQuery;
    private RecyclerView recyclerViewBooks;
    private BookAdapter bookAdapter;
    private CircularProgressBar circularProgressBar;
    private View.OnClickListener bookClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            BookViewHolder viewHolder = (BookViewHolder) view.getTag();
            FireBookDetails bookDetail = viewHolder.bookDetail;
            BookInfoActivity.startBookInfo(SearchActivity.this, bookDetail);
        }
    };
    private View errorLayout;
    private TextView errorText;
    private Button errorRetryButton;

    public static void start(final Activity activity) {
        Intent intent = new Intent(activity, SearchActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.search_query_hint));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        ActionBar actionBar = getSupportActionBar();

        setSupportActionBar(toolbar);

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setDisplayShowHomeEnabled(true);

        }
        searchPresenter = new SearchPresenter(Injection.provideBookService(), Injection.provideAnalytics(),
                AndroidSchedulers.mainThread(), Schedulers.io());
        searchPresenter.attachView(this);

        recyclerViewBooks = (RecyclerView) findViewById(R.id.recycler_view_books);
        recyclerViewBooks.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.book_span)));
        circularProgressBar = (CircularProgressBar) findViewById(R.id.progress_bar_search);
        errorLayout = findViewById(R.id.linear_layout_error);
        errorText = (TextView) findViewById(R.id.text_view_error_screen);

        errorRetryButton = (Button) findViewById(R.id.button_retry);
        errorRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                searchPresenter.search(searchQuery);
            }
        });
        hideLoading();
    }

    @Override
    protected String getScreenName() {
        return "SearchActivity";
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        final MenuItem item = menu.findItem(R.id.action_menu_search);
        final SearchView searchView = (SearchView) item.getActionView();

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.expandActionView();
                searchView.setQuery(searchQuery, false);
            }
        });

        if (searchView != null) {
            item.expandActionView();
            if (searchQuery != null) {
                toolbar.setTitle(searchQuery);
                searchView.setQuery(searchQuery, false);

            }

            searchView.setQueryHint(getString(R.string.search_query_hint));
            searchView.setIconified(false);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    item.collapseActionView();
                    searchPresenter.search(query);
                    toolbar.setTitle(query);

                    return true;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    Timber.d("Search query:" + query);
                    searchPresenter.search(query);
                    return false;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showSearchResults(final List<FireBookDetails> bookList) {
        recyclerViewBooks.setVisibility(View.VISIBLE);
        bookAdapter = new BookAdapter(bookList, this, bookClickListener);
        recyclerViewBooks.setAdapter(bookAdapter);
    }

    @Override
    public void showErrorMessage(final String errorMsg) {
        recyclerViewBooks.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
        errorText.setText(errorMsg);
    }

    @Override
    public void showLoading() {
        circularProgressBar.setVisibility(View.VISIBLE);
        recyclerViewBooks.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        circularProgressBar.setVisibility(View.GONE);

    }

    @Override
    public void showNoInternetMessage() {
        recyclerViewBooks.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
        errorText.setText(getText(R.string.search_no_internet_text));
    }

    @Override
    public void showNoResultsMessage() {
        recyclerViewBooks.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
        errorText.setText(getText(R.string.search_no_results_found));
    }

    @Override
    public void showRetryButton() {
        errorRetryButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRetryButton() {
        errorRetryButton.setVisibility(View.INVISIBLE);
    }
}
