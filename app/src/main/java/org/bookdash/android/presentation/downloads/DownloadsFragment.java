package org.bookdash.android.presentation.downloads;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.bookdash.android.Injection;
import org.bookdash.android.R;
import org.bookdash.android.domain.model.firebase.FireBookDetails;
import org.bookdash.android.presentation.bookinfo.BookInfoActivity;
import org.bookdash.android.presentation.main.NavDrawerInterface;

import java.util.List;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class DownloadsFragment extends Fragment implements DownloadsContract.View {

    private RecyclerView listDownloadsRecyclerView;
    private DownloadsContract.Presenter downloadsPresenter;
    private DownloadsAdapter downloadsAdapter;
    private LinearLayout linearLayoutErrorScreen;
    private Button buttonRetry;
    private TextView textViewErrorMessage;
    private CircularProgressBar circularProgressBar;
    private NavDrawerInterface navDrawerInterface;


    public static DownloadsFragment newInstance() {
        return new DownloadsFragment();
    }

    @Override
    public void showDownloadedBooks(List<FireBookDetails> books) {
        downloadsAdapter.setBooks(books);
        downloadsAdapter.notifyDataSetChanged();
        listDownloadsRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading(final boolean visible) {
        circularProgressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
        listDownloadsRecyclerView.setVisibility(visible ? View.GONE : View.VISIBLE);
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
    public void showSnackBarError(final int message) {
        Snackbar.make(listDownloadsRecyclerView, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showSnackBarError(final String message) {
        Snackbar.make(listDownloadsRecyclerView, message, Snackbar.LENGTH_LONG).show();


    }

    @Override
    public void showNoBooksDownloadedMessage() {
        showErrorScreen(true, getString(R.string.no_books_downloaded), false);
        listDownloadsRecyclerView.setVisibility(View.GONE);


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
        return inflater.inflate(R.layout.fragment_downloads, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listDownloadsRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_list_downloads);
        downloadsPresenter = new DownloadsPresenter(Injection.provideBookService(), Injection.provideDownloadService(),
                Injection.provideAnalytics(), Schedulers.io(), AndroidSchedulers.mainThread(),
                Schedulers.computation());
        downloadsPresenter.attachView(this);
        downloadsAdapter = new DownloadsAdapter(null, getActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadsViewHolder downloadsViewHolder = (DownloadsViewHolder) v.getTag();
                showDeleteDialog(downloadsViewHolder.book);

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadsViewHolder downloadsViewHolder = (DownloadsViewHolder) v.getTag();
                showBookDetails(downloadsViewHolder.book);
            }
        });
        listDownloadsRecyclerView
                .setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        listDownloadsRecyclerView.setAdapter(downloadsAdapter);

        linearLayoutErrorScreen =  view.findViewById(R.id.linear_layout_error);
        buttonRetry =  view.findViewById(R.id.button_retry);
        textViewErrorMessage =  view.findViewById(R.id.text_view_error_screen);
        circularProgressBar =  view.findViewById(R.id.fragment_loading_downloads);
        Toolbar toolbar =  view.findViewById(R.id.toolbar);
        if (navDrawerInterface != null) {
            navDrawerInterface.setToolbar(toolbar);

        }

        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.fragment_title_downloads));
        }
        downloadsPresenter.loadListDownloads();

        setHasOptionsMenu(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        downloadsPresenter.detachView();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        navDrawerInterface = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

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

    private void showDeleteDialog(final FireBookDetails bookToDelete) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
        builder.setTitle(getString(R.string.delete_book_confirmation));
        builder.setMessage(getString(R.string.downloads_are_you_sure, bookToDelete.getBookTitle()));
        builder.setPositiveButton(getString(R.string.delete_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downloadsPresenter.deleteDownload(bookToDelete);
            }
        });
        builder.setNegativeButton(getString(R.string.delete_cancel), null);
        builder.show();
    }

    private void showBookDetails(FireBookDetails bookDetail) {
        Intent intent = new Intent(getActivity(), BookInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(BookInfoActivity.BOOK_PARCEL, bookDetail);
        startActivity(intent);
    }


}
