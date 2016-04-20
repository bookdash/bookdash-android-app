package org.bookdash.android.presentation.downloads;

import org.bookdash.android.data.books.BookDetailRepository;
import org.bookdash.android.domain.pojo.BookDetail;

import java.util.List;

public class DownloadsPresenter implements DownloadsContract.UserActions{
    private final BookDetailRepository bookRepository;
    private final DownloadsContract.View view;

    public DownloadsPresenter(BookDetailRepository bookRepository, DownloadsContract.View downloadsView) {
        this.bookRepository = bookRepository;
        this.view = downloadsView;
    }

    public void loadListDownloads() {
        view.showLoading(true);
        bookRepository.getDownloadedBooks(new BookDetailRepository.GetBooksForLanguageCallback() {
            @Override
            public void onBooksLoaded(List<BookDetail> books) {
                view.showDownloadedBooks(books);
                view.showLoading(false);
            }

            @Override
            public void onBooksLoadError(Exception e) {
                view.showErrorScreen(true, e.getMessage(), true);
                view.showLoading(false);
            }
        });
    }

    @Override
    public void deleteDownload() {

    }
}
