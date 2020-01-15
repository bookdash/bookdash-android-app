package org.bookdash.android.presentation.listbooks;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.bookdash.android.R;
import org.bookdash.android.domain.model.firebase.FireBookDetails;

/**
 * @author rebeccafranks
 * @since 15/11/11.
 */
public class BookViewHolder extends RecyclerView.ViewHolder {

    public TextView bookTitle;
    public ImageView bookCover;
    public CardView cardContainer;
    public FireBookDetails bookDetail;
    public ImageView downloadedIcon;

    public BookViewHolder(View v) {
        super(v);
        cardContainer = (CardView) v.findViewById(R.id.card_view);
        bookTitle = (TextView) v.findViewById(R.id.textViewBookName);
        bookCover = (ImageView) v.findViewById(R.id.imageViewBookCover);
        downloadedIcon = (ImageView) v.findViewById(R.id.imageViewBookDownloaded);
    }
}
