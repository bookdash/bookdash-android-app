package org.bookdash.android.presentation.downloads;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.bookdash.android.Injection;
import org.bookdash.android.R;
import org.bookdash.android.domain.pojo.BookDetail;

import java.util.List;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;


public class DownloadsFragment extends Fragment implements DownloadsContract.View {

    private RecyclerView listDownloadsRecyclerView;
    private DownloadsContract.UserActions downloadsPresenter;
    private DownloadsAdapter downloadsAdapter;
    private LinearLayout linearLayoutErrorScreen;
    private Button buttonRetry;
    private TextView textViewErrorMessage;
    private CircularProgressBar circularProgressBar;

    public static DownloadsFragment newInstance() {
        return new DownloadsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_downloads, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listDownloadsRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_list_downloads);
        downloadsPresenter = new DownloadsPresenter(Injection.provideBookRepo(), this);
        downloadsAdapter = new DownloadsAdapter(null, getActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadsViewHolder downloadsViewHolder = (DownloadsViewHolder) v.getTag();
                if (downloadsViewHolder.book.isDownloadedAlready()) {
                    showDeleteDialog(downloadsViewHolder.book);
                }
            }
        });
        listDownloadsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        listDownloadsRecyclerView.setAdapter(downloadsAdapter);

        linearLayoutErrorScreen = (LinearLayout) view.findViewById(R.id.linear_layout_error);
        buttonRetry = (Button) view.findViewById(R.id.button_retry);
        textViewErrorMessage = (TextView) view.findViewById(R.id.text_view_error_screen);
        circularProgressBar = (CircularProgressBar) view.findViewById(R.id.fragment_loading_downloads);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.fragment_title_downloads));
        }
        downloadsPresenter.loadListDownloads();


    }

    private void showDeleteDialog(final BookDetail bookToDelete) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
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

    @Override
    public void showDownloadedBooks(List<BookDetail> books) {
        downloadsAdapter.setBooks(books);
        downloadsAdapter.notifyDataSetChanged();
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
        listDownloadsRecyclerView.setVisibility(visible ? View.GONE : View.VISIBLE);

    }

    @Override
    public void showSnackBarError(int message) {
        Snackbar.make(listDownloadsRecyclerView, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showSnackBarError(String message) {
        Snackbar.make(listDownloadsRecyclerView, message, Snackbar.LENGTH_LONG).show();

    }

}
