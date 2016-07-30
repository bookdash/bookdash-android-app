package org.bookdash.android.presentation.listbooks;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import org.bookdash.android.Injection;
import org.bookdash.android.R;
import org.bookdash.android.data.utils.Keyboard;
import org.bookdash.android.domain.pojo.BookDetail;
import org.bookdash.android.presentation.bookinfo.BookInfoActivity;
import org.bookdash.android.presentation.main.NavDrawerInterface;

import java.util.List;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;


public class ListBooksFragment extends Fragment implements ListBooksContract.View {

    private static final String TAG = ListBooksFragment.class.getCanonicalName();
    private static final int BOOK_DETAIL_REQUEST_CODE = 43;
    private ListBooksContract.UserActionsListener actionsListener;
    private ViewSwitcher toolbarViewSwitcher;
    private Button buttonRetry;
    private RecyclerView recyclerViewBooks;
    private CircularProgressBar circularProgressBar;
    private LinearLayout linearLayoutErrorScreen;
    private TextView textViewErrorMessage;
    private NavDrawerInterface navDrawerInterface;
    private BookAdapter bookAdapter;
    private EditText searchEditText;
    private SearchWatcher searchWatcher;

    public static Fragment newInstance() {
        return new ListBooksFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_books, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        actionsListener = new ListBooksPresenter(this, Injection.provideBookRepo(), Injection.provideSettingsRepo(getActivity()));

        toolbarViewSwitcher = (ViewSwitcher) view.findViewById(R.id.view_switcher_toolbar);
        searchEditText = (EditText) view.findViewById(R.id.edit_text_search);
        searchEditText.addTextChangedListener(searchWatcher = new SearchWatcher());
        circularProgressBar = (CircularProgressBar) view.findViewById(R.id.activity_loading_books);
        linearLayoutErrorScreen = (LinearLayout) view.findViewById(R.id.linear_layout_error);
        buttonRetry = (Button) view.findViewById(R.id.button_retry);
        textViewErrorMessage = (TextView) view.findViewById(R.id.text_view_error_screen);
        recyclerViewBooks = (RecyclerView) view.findViewById(R.id.recycler_view_books);
        recyclerViewBooks.setLayoutManager(new GridLayoutManager(getActivity(), getContext().getResources().getInteger(R.integer.book_span)));
        buttonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Retry button clicked");
                if (toolbarViewSwitcher.getDisplayedChild() == 0)
                    actionsListener.loadBooksForLanguagePreference();
                else actionsListener.searchBooksForLanguage(searchEditText.getText().toString());
            }
        });
        view.findViewById(R.id.image_view_search_back).setOnClickListener(searchBackClickListener);
        searchEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) Keyboard.showKeyboard(v);
                else Keyboard.hideKeyboard(v);
            }
        });
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        if (navDrawerInterface != null) {
            navDrawerInterface.setToolbar(toolbar);
        }
        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();


        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.book_dash));
        }
        actionsListener.loadLanguages();
        actionsListener.loadBooksForLanguagePreference();
        setHasOptionsMenu(true);
    }


    private View.OnClickListener bookClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openBookDetails(v);
        }
    };

    private View.OnClickListener searchBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            navDrawerInterface.unlockNavDrawer();
            toolbarViewSwitcher.setDisplayedChild(0);
        }
    };


    public void openBookDetails(View v) {
        Intent intent = new Intent(getActivity(), BookInfoActivity.class);
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        BookViewHolder viewHolder = (BookViewHolder) v.getTag();
        BookDetail bookDetailResult = viewHolder.bookDetail;
        intent.putExtra(BookInfoActivity.BOOK_PARCEL, bookDetailResult.toBookParcelable());
        startActivityForResult(intent, BOOK_DETAIL_REQUEST_CODE);

    }

    private void runUiThread(Runnable runnable) {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(runnable);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult() called with: " + "requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");

        if (requestCode == BOOK_DETAIL_REQUEST_CODE) {
            if (bookAdapter != null) {
                bookAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void showErrorScreen(final boolean show, final String errorMessage, final boolean showRetryButton) {
        runUiThread(new Runnable() {
            @Override
            public void run() {
                if (show) {
                    linearLayoutErrorScreen.setVisibility(View.VISIBLE);
                    recyclerViewBooks.setVisibility(View.GONE);
                } else {
                    linearLayoutErrorScreen.setVisibility(View.GONE);
                    recyclerViewBooks.setVisibility(View.VISIBLE);
                }
                buttonRetry.setVisibility(showRetryButton ? View.VISIBLE : View.GONE);
                textViewErrorMessage.setText(errorMessage);
            }
        });


    }

    @Override
    public void showLoading(final boolean visible) {
        runUiThread(new Runnable() {
            @Override
            public void run() {
                if (visible) {
                    linearLayoutErrorScreen.setVisibility(View.GONE);
                    buttonRetry.setVisibility(View.GONE);
                    recyclerViewBooks.setVisibility(View.GONE);
                    circularProgressBar.setVisibility(View.VISIBLE);
                } else {
                    linearLayoutErrorScreen.setVisibility(View.GONE);
                    buttonRetry.setVisibility(View.GONE);
                    circularProgressBar.setVisibility(View.GONE);
                    recyclerViewBooks.setVisibility(View.GONE);
                }

            }
        });

    }

    @Override
    public void showBooks(final List<BookDetail> bookDetailList) {
        searchWatcher.setRequested(false);
        runUiThread(new Runnable() {
            @Override
            public void run() {
                if (bookDetailList.isEmpty()) {
                    showErrorScreen(true, getString(R.string.no_books_available), true);
                }
                bookAdapter = new BookAdapter(bookDetailList, ListBooksFragment.this.getActivity(), bookClickListener);
                recyclerViewBooks.setAdapter(bookAdapter);
            }
        });


    }

    @Override
    public void showSnackBarError(final int message) {
        runUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(recyclerViewBooks, message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private DialogInterface.OnClickListener languageClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (dialog != null) {
                dialog.dismiss();
            }

            actionsListener.saveSelectedLanguage(which);

        }
    };

    @Override
    public void showLanguagePopover(final String[] languages, final int selected) {
        runUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog alertDialogLanguages = new AlertDialog.Builder(getActivity())
                        .setTitle(getString(R.string.language_selection_heading))
                        .setSingleChoiceItems(languages, selected, languageClickListener).create();
                alertDialogLanguages.show();
            }
        });

    }

    @Override
    public void showSearch() {
        runUiThread(new Runnable() {
            @Override
            public void run() {
                linearLayoutErrorScreen.setVisibility(View.GONE);
                navDrawerInterface.lockNavDrawer();
                toolbarViewSwitcher.setDisplayedChild(1);
                searchEditText.requestFocus();
            }
        });
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search_books:
                actionsListener.clickDisplaySearch();
                return true;
            case R.id.action_language_choice:
                actionsListener.clickOpenLanguagePopover();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class SearchWatcher implements TextWatcher, Runnable {

        private static final int DURATION_THRESHOLD = 300;

        long prevTimestamp = 0;
        boolean requested;

        android.os.Handler watchRequested;
        Runnable watchRequestedRunnable;

        protected SearchWatcher() {
            watchRequested = new android.os.Handler();
            watchRequestedRunnable = this;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            long timestamp = SystemClock.currentThreadTimeMillis();
            if (timestamp - prevTimestamp > DURATION_THRESHOLD) watchRequested();
            prevTimestamp = timestamp;
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        private void watchRequested() {
            watchRequested.removeCallbacksAndMessages(null);
            watchRequested.postDelayed(watchRequestedRunnable, DURATION_THRESHOLD);
        }

        @Override
        public void run() {
            if (requested) return;
            if (toolbarViewSwitcher.getDisplayedChild() != 0) {
                requested = true;
                actionsListener.searchBooksForLanguage(searchEditText.getText().toString());
            }
        }

        public void setRequested(boolean requested) {
            this.requested = requested;
        }
    }
}
