package org.bookdash.android.presentation.about;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.bookdash.android.R;
import org.bookdash.android.presentation.main.NavDrawerInterface;

public class AboutFragment extends Fragment implements AboutContract.View {

    private AboutPresenter aboutPresenter;
    private NavDrawerInterface navDrawerInterface;

    public static AboutFragment newInstance() {

        Bundle args = new Bundle();

        AboutFragment fragment = new AboutFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.activity_about, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        aboutPresenter = new AboutPresenter(this);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        navDrawerInterface.setToolbar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setTitle(R.string.about_heading);
        }

        TextView textViewWhyBookDash = (TextView) view.findViewById(R.id.text_why_bookdash);
        textViewWhyBookDash.setText(Html.fromHtml(getString(R.string.why_bookdash)));
        Linkify.addLinks(textViewWhyBookDash, Linkify.ALL);

        TextView textViewHeading = (TextView) view.findViewById(R.id.text_view_about);
        textViewHeading.setText(Html.fromHtml(getString(R.string.heading_about)));
        Linkify.addLinks(textViewHeading, Linkify.ALL);

        Button learnMoreBookDash = (Button) view.findViewById(R.id.button_learn_more);
        learnMoreBookDash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutPresenter.clickLearnMore();
            }
        });


        textViewHeading.findFocus();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NavDrawerInterface) {
            navDrawerInterface = (NavDrawerInterface) context;

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        navDrawerInterface = null;
    }


    @Override
    public void showLearnMorePage(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
