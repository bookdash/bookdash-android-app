package org.bookdash.android.presentation.downloads;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.bookdash.android.R;
import org.bookdash.android.domain.model.firebase.FireBookDetails;

public class DownloadsViewHolder extends RecyclerView.ViewHolder {
    TextView downloadTitleTextView;
    TextView downloadProgressTextView;
    ImageView downloadImageTextView;
    ImageButton downloadActionButtonView;
    FireBookDetails book;

    public DownloadsViewHolder(View itemView) {
        super(itemView);
        downloadActionButtonView = itemView.findViewById(R.id.image_button_delete_book);
        downloadImageTextView = itemView.findViewById(R.id.image_view_download_book_cover);
        downloadProgressTextView = itemView.findViewById(R.id.download_progress);
        downloadTitleTextView = itemView.findViewById(R.id.text_view_book_title_download);
    }
}
