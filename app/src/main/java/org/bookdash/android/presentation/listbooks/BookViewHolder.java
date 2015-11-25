package org.bookdash.android.presentation.listbooks;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.bookdash.android.R;
import org.bookdash.android.domain.pojo.*;

/**
 * @author rebeccafranks
 * @since 15/11/11.
 */
public class BookViewHolder extends RecyclerView.ViewHolder {
    public TextView mTextViewBookAuthor;
    public TextView mTextView;
    public ImageView mImageView;
    public CardView mCardView;
    public BookDetail bookDetail;
    public BookViewHolder(View v) {
        super(v);
        mCardView = (CardView) v.findViewById(R.id.card_view);
        mTextView = (TextView) v.findViewById(R.id.textViewBookName);
        mImageView = (ImageView) v.findViewById(R.id.imageViewBookCover);
        mTextViewBookAuthor = (TextView) v.findViewById(R.id.textViewAuthor);
    }
}
