package org.bookdash.android.presentation.bookinfo;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.graphics.Palette;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.bookdash.android.R;
import org.bookdash.android.data.books.BookDetailRepository;
import org.bookdash.android.domain.pojo.firebase.FireBookDetails;

/**
 * @author rebeccafranks
 * @since 15/11/03.
 */
public class BookInfoPresenter implements BookInfoContract.UserActionsListener {

    private final BookInfoContract.View booksView;
    private final BookDetailRepository bookDetailRepository;

    private final Context context;

    public BookInfoPresenter(Context context, @NonNull BookInfoContract.View booksView, @NonNull BookDetailRepository bookDetailRepository) {

        this.booksView = booksView;
        this.bookDetailRepository = bookDetailRepository;
        this.context = context;
    }

    @Override
    public void loadBookInformation(String bookDetailId) {
        booksView.showProgress(true);
        bookDetailRepository.getBookDetail(bookDetailId, new BookDetailRepository.GetBookDetailCallback() {
            @Override
            public void onBookDetailLoaded(FireBookDetails bookDetail) {
                showBookDetail(bookDetail);
            }

            @Override
            public void onBookDetailLoadError(Exception e) {

                booksView.showError(e.getMessage());
            }
        });
    }

    private void showBookDetail(FireBookDetails bookDetail) {

        booksView.setBookInfoBinding(bookDetail);

        booksView.setToolbarTitle(bookDetail.getBookTitle());
        if (bookDetail.isDownloadedAlready()) {
            booksView.showDownloadFinished();
        }
        booksView.showBookDetailView();
       /* bookDetailRepository.getContributorsForBook(bookDetail.getBook(), new BookDetailRepository.GetContributorsCallback() {
            @Override
            public void onContributorsLoaded(List<BookContributor> contributors) {
                booksView.showContributors(contributors);
            }

            @Override
            public void onContributorsLoadError(Exception e) {

            }
        });*/ //TODO

    }


    @Override
    public void downloadBook(final FireBookDetails bookInfo) {
       /* if (bookInfo == null || bookInfo.getBookFile() == null || bookInfo.getBookFile().getUrl() == null) {
            booksView.showSnackBarMessage(R.string.book_not_available);
            return;
        }
        if (bookInfo.isDownloading()) {
            booksView.showSnackBarMessage(R.string.book_is_downloading);
            return;
        }
        bookInfo.setIsDownloading(true);


        bookDetailRepository.downloadBook(bookInfo, new BookDetailRepository.GetBookPagesCallback() {
            @Override
            public void onBookPagesLoaded(BookPages bookPages) {
                if (bookPages == null) {
                    booksView.showSnackBarMessage(R.string.failed_to_open_book);
                    return;
                }
                bookInfo.setIsDownloading(false);
                booksView.openBook(bookInfo, bookPages, bookInfo.getFolderLocation());
            }

            @Override
            public void onBookPagesLoadError(Exception e) {
                bookInfo.setIsDownloading(false);
                if (e != null) {
                    booksView.showSnackBarMessage(R.string.failed_to_download_book);
                }
            }

            @Override
            public void onBookPagesDownloadProgressUpdate(int progress) {
                booksView.showDownloadProgress(progress);
            }
        });*/

    }

    @Override
    public void loadImage(String url) {
        Glide.with(context).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                booksView.onImageLoaded(resource);
                extractPaletteColors(resource);
            }
        });
    }

    @Override
    public void shareBookClicked(FireBookDetails bookInfo) {
        if (bookInfo == null) {
            booksView.showError(context.getString(R.string.book_info_still_loading));
            return;
        }
        booksView.sendShareEvent(bookInfo.getBookTitle());
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

                int toolbarColor = toolbarSwatch != null ? toolbarSwatch.getRgb() : ContextCompat.getColor(context.getApplicationContext(), R.color.colorPrimary);
                int accentColor = palette.getVibrantColor(ContextCompat.getColor(context.getApplicationContext(), R.color.colorAccent));

                booksView.setToolbarColor(toolbarColor);
                booksView.setAccentColor(accentColor);

                if (toolbarSwatch != null) {
                    float[] darkerShade = toolbarSwatch.getHsl();
                    darkerShade[2] = darkerShade[2] * 0.8f; //Make it a darker shade for the status bar
                    booksView.setStatusBarColor(ColorUtils.HSLToColor(darkerShade));

                } else {

                    booksView.setStatusBarColor(ContextCompat.getColor(context.getApplicationContext(), R.color.colorPrimaryDark));

                }
            }
        });
    }
}


