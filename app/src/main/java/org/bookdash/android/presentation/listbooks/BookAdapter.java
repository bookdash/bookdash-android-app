package org.bookdash.android.presentation.listbooks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import org.bookdash.android.R;
import org.bookdash.android.domain.pojo.BookDetail;

import java.util.List;

/**
 * @author Rebecca Franks
 * @since 2015/07/16 2:06 PM
 */
public class BookAdapter extends RecyclerView.Adapter<BookViewHolder> {
    private final Context context;
    private List<BookDetail> bookDetails;
    private View.OnClickListener onClickListener;


    public BookAdapter(List<BookDetail> bookDetails, Context context, View.OnClickListener onClickListener) {
        this.bookDetails = bookDetails;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_book, parent, false);
        return new BookViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        BookDetail bookDetail = bookDetails.get(position);
        holder.bookTitle.setText(bookDetail.getBookTitle());
        Glide.with(context).load(bookDetail.getBookCoverUrl()).placeholder(R.drawable.bookdash_placeholder).error(R.drawable.bookdash_placeholder).into(holder.bookCover);
        holder.bookDetail = bookDetail;
        holder.downloadedIcon.setVisibility(bookDetail.isDownloadedAlready() ? View.VISIBLE : View.INVISIBLE);
        holder.cardContainer.setTag(holder);
        holder.cardContainer.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return bookDetails.size();
    }
}