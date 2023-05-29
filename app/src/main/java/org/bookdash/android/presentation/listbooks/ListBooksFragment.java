package org.bookdash.android.presentation.listbooks;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.bookdash.android.Injection;
import org.bookdash.android.R;
import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.presentation.bookinfo.BookInfoActivity;
import org.bookdash.android.presentation.main.NavDrawerInterface;
import org.bookdash.android.presentation.search.SearchActivity;

import java.util.List;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ListBooksFragment extends Fragment implements ListBooksContract.View {

    private static final String TAG = ListBooksFragment.class.getCanonicalName();
    private static final int BOOK_DETAIL_REQUEST_CODE = 43;
    private ListBooksContract.Presenter listBooksPresenter;
    private Button buttonRetry;
    private RecyclerView recyclerViewBooks;
    private CircularProgressBar circularProgressBar;
    private LinearLayout linearLayoutErrorScreen;
    private TextView textViewErrorMessage;
    private LinearLayout linearLayoutContainerLanguage;
    private TextView textViewCurrentLanguage;
    private NavDrawerInterface navDrawerInterface;
    private BookAdapter bookAdapter;
    private View.OnClickListener bookClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BookViewHolder viewHolder = (BookViewHolder) v.getTag();
            FireBookDetails bookDetailResult = viewHolder.bookDetail;
            BookInfoActivity.startBookInfo(ListBooksFragment.this.getActivity(), bookDetailResult);
        }
    };
    private DialogInterface.OnClickListener languageClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (dialog != null) {
                dialog.dismiss();
            }

            listBooksPresenter.saveSelectedLanguage(which);

        }
    };

    public static Fragment newInstance() {
        return new ListBooksFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,
                "onActivityResult() called with: " + "requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");

        if (requestCode == BOOK_DETAIL_REQUEST_CODE) {
            if (bookAdapter != null) {
                bookAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NavDrawerInterface) {
            navDrawerInterface = (NavDrawerInterface) context;

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_books, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listBooksPresenter = new ListBooksPresenter(Injection.provideSettingsRepo(getActivity()),
                Injection.provideBookService(), Injection.provideAnalytics(), Schedulers.io(),
                AndroidSchedulers.mainThread());
        listBooksPresenter.attachView(this);
        circularProgressBar = view.findViewById(R.id.activity_loading_books);
        linearLayoutErrorScreen = view.findViewById(R.id.linear_layout_error);
        buttonRetry = view.findViewById(R.id.button_retry);
        textViewErrorMessage = view.findViewById(R.id.text_view_error_screen);
        linearLayoutContainerLanguage = view.findViewById(R.id.container_language);
        textViewCurrentLanguage = view.findViewById(R.id.text_current_language);
        recyclerViewBooks = view.findViewById(R.id.recycler_view_books);
        recyclerViewBooks.setLayoutManager(
                new GridLayoutManager(getActivity(), getContext().getResources().getInteger(R.integer.book_span)));
        buttonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Retry button clicked");
                listBooksPresenter.loadBooksForLanguagePreference();
            }
        });
        linearLayoutContainerLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listBooksPresenter.clickOpenLanguagePopover();
            }
        });
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        if (navDrawerInterface != null) {
            navDrawerInterface.setToolbar(toolbar);
        }
        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();


        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.book_dash));
        }
        listBooksPresenter.loadLanguages();
        listBooksPresenter.loadBooksForLanguagePreference();
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listBooksPresenter.detachView();
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
        if (item.getItemId() == R.id.action_search_books) {
            listBooksPresenter.openSearchScreen();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void runUiThread(Runnable runnable) {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(runnable);
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
    public void showBooks(final List<FireBookDetails> bookDetailList) {
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
    public void startSearchActivity() {
        SearchActivity.start(getActivity());
    }

    @Override
    public void onSelectedLanguageChanged(final String selectedLanguage) {
        runUiThread(new Runnable() {
            @Override
            public void run() {
                textViewCurrentLanguage.setText(selectedLanguage);
            }
        });
    }
}
