package org.bookdash.android.presentation.bookinfo;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.bookdash.android.R;


public class ContributorViewHolder extends RecyclerView.ViewHolder {

    public final TextView textViewContributor;
    public final TextView textViewRole;
    public final ImageView imageViewContributorAvatar;

    public ContributorViewHolder(View v) {
        super(v);
        textViewContributor = (TextView) v.findViewById(R.id.textViewContributorName);
        textViewRole = (TextView) v.findViewById(R.id.textViewRole);
        imageViewContributorAvatar = (ImageView) v.findViewById(R.id.imageViewContributorAvatar);

    }
}
