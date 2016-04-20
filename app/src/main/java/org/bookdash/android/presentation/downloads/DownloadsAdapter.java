package org.bookdash.android.presentation.downloads;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import org.bookdash.android.R;
import org.bookdash.android.domain.pojo.BookDetail;

import java.util.List;

public class DownloadsAdapter extends RecyclerView.Adapter<DownloadsViewHolder> {

    private List<BookDetail> bookList;
    private Context context;

    public DownloadsAdapter(List<BookDetail> bookList, Context context) {
        this.bookList = bookList;
        this.context = context;
    }

    @Override
    public DownloadsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_download, parent, false);

        return new DownloadsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DownloadsViewHolder holder, int position) {
        BookDetail book = bookList.get(position);
        holder.downloadTitleTextView.setText(book.getBookTitle());
        Glide.with(context).load(book.getBookCoverUrl()).into(holder.downloadImageTextView);

    }

    @Override
    public int getItemCount() {
        if (bookList == null) {
            return 0;
        }
        return bookList.size();
    }

    public void setBooks(List<BookDetail> books) {
        this.bookList = books;
    }
}
