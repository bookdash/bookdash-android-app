package org.bookdash.android.presentation.listbooks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;

import org.bookdash.android.R;
import org.bookdash.android.domain.model.firebase.FireBookDetails;

import java.util.List;

/**
 * @author Rebecca Franks
 * @since 2015/07/16 2:06 PM
 */
public class BookAdapter extends RecyclerView.Adapter<BookViewHolder> {
    private final Context context;
    private List<FireBookDetails> bookDetails;
    private View.OnClickListener onClickListener;


    public BookAdapter(List<FireBookDetails> bookDetails, Context context, View.OnClickListener onClickListener) {
        this.bookDetails = bookDetails;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_book, parent, false);
        return new BookViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        FireBookDetails bookDetail = bookDetails.get(position);
        holder.bookTitle.setText(bookDetail.getBookTitle());
        Log.d("BookAdapter", "Book url:" + bookDetail.getBookCoverPageUrl());
        Glide.with(context).using(new FirebaseImageLoader()).load(bookDetail.getFirebaseBookCoverUrl())
                .placeholder(R.drawable.bookdash_placeholder).error(R.drawable.bookdash_placeholder)
                .into(holder.bookCover);
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