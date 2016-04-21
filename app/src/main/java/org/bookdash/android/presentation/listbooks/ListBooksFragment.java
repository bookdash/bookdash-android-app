package org.bookdash.android.presentation.listbooks;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.bookdash.android.Injection;
import org.bookdash.android.R;
import org.bookdash.android.domain.pojo.BookDetail;
import org.bookdash.android.presentation.bookinfo.BookInfoActivity;
import org.bookdash.android.presentation.main.MainActivity;
import org.bookdash.android.presentation.view.AutofitRecyclerView;

import java.util.List;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;


public class ListBooksFragment extends Fragment implements ListBooksContract.View {

    private static final String TAG = ListBooksFragment.class.getCanonicalName();
    private ListBooksContract.UserActionsListener actionsListener;
    private Button buttonRetry;
    private AutofitRecyclerView mRecyclerView;
    private CircularProgressBar circularProgressBar;
    private LinearLayout linearLayoutErrorScreen;
    private TextView textViewErrorMessage;
    private Toolbar toolbar;

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

        circularProgressBar = (CircularProgressBar) view.findViewById(R.id.activity_loading_books);
        linearLayoutErrorScreen = (LinearLayout) view.findViewById(R.id.linear_layout_error);
        buttonRetry = (Button) view.findViewById(R.id.button_retry);
        textViewErrorMessage = (TextView) view.findViewById(R.id.text_view_error_screen);
        mRecyclerView = (AutofitRecyclerView) view.findViewById(R.id.recycler_view_books);

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
                actionsListener.loadBooksForLanguagePreference();
            }
        });
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        final ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionsListener.loadLanguages();
        actionsListener.loadBooksForLanguagePreference();
    }


    private View.OnClickListener bookClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openBookDetails(v);
        }
    };


    public void openBookDetails(View v) {
        Intent intent = new Intent(getActivity(), BookInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        BookViewHolder viewHolder = (BookViewHolder) v.getTag();
        BookDetail bookDetailResult = viewHolder.bookDetail;
        intent.putExtra(BookInfoActivity.BOOK_PARCEL, bookDetailResult.toBookParcelable());
        startActivity(intent);

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
            showErrorScreen(true, getString(R.string.no_books_available), true);
        }
        RecyclerView.Adapter mAdapter = new BookAdapter(bookDetailList, ListBooksFragment.this.getActivity(), bookClickListener);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.scheduleLayoutAnimation();
    }

    @Override
    public void showSnackBarError(int message) {
        Snackbar.make(mRecyclerView, message, Snackbar.LENGTH_LONG).show();
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
    public void showLanguagePopover(String[] languages, int selected) {
        AlertDialog alertDialogLanguages = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.language_selection_heading))
                .setSingleChoiceItems(languages, selected, languageClickListener).create();
        alertDialogLanguages.show();
    }


}
