package org.bookdash.android.presentation.bookinfo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import org.bookdash.android.R;
import org.bookdash.android.domain.model.firebase.FireContributor;

import java.util.List;

public class ContributorAdapter extends RecyclerView.Adapter<ContributorViewHolder> {
    private final Context context;
    private List<FireContributor> items;

    public ContributorAdapter(List<FireContributor> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ContributorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_contributor, parent, false);
        return new ContributorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ContributorViewHolder holder, int position) {
        FireContributor item = items.get(position);

        holder.textViewContributor.setText(item.getName());
        holder.textViewRole.setText(item.getActualRolesFormatted());
        Glide.with(context).load(item.getAvatar()).into(holder.imageViewContributorAvatar);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}