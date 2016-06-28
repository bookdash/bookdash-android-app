package org.bookdash.android.presentation.downloads;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.bookdash.android.R;
import org.bookdash.android.domain.model.firebase.FireBookDetails;

public class DownloadsViewHolder extends RecyclerView.ViewHolder {
    TextView downloadTitleTextView;
    TextView downloadProgressTextView;
    ImageView downloadImageTextView;
    ImageButton downloadActionButtonView;
    RelativeLayout downloadRelativeLayout;
    FireBookDetails book;

    public DownloadsViewHolder(View itemView) {
        super(itemView);
        downloadActionButtonView = (ImageButton) itemView.findViewById(R.id.image_button_delete_book);
        downloadImageTextView = (ImageView) itemView.findViewById(R.id.image_view_download_book_cover);
        downloadProgressTextView = (TextView) itemView.findViewById(R.id.download_progress);
        downloadTitleTextView = (TextView) itemView.findViewById(R.id.text_view_book_title_download);
        downloadRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.download_relative_layout);
    }
}
