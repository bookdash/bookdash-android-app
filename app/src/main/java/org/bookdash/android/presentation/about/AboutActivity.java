package org.bookdash.android.presentation.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.bookdash.android.BookDashApplication;
import org.bookdash.android.R;
import org.bookdash.android.presentation.activity.BaseAppCompatActivity;

public class AboutActivity extends BaseAppCompatActivity implements AboutContract.View {

    private AboutPresenter aboutPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        aboutPresenter = new AboutPresenter(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.about_heading);
        }

        TextView textViewWhyBookDash = (TextView) findViewById(R.id.text_why_bookdash);
        textViewWhyBookDash.setText(Html.fromHtml(getString(R.string.why_bookdash)));
        Linkify.addLinks(textViewWhyBookDash, Linkify.ALL);

        TextView textViewHeading = (TextView) findViewById(R.id.text_view_about);
        textViewHeading.setText(Html.fromHtml(getString(R.string.heading_about)));
        Linkify.addLinks(textViewHeading, Linkify.ALL);

        Button learnMoreBookDash = (Button) findViewById(R.id.button_learn_more);
        learnMoreBookDash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutPresenter.clickLearnMore();
            }
        });


        textViewHeading.findFocus();
    }

    @Override
    protected String getScreenName() {
        return "About Screen";
    }


    @Override
    public void showLearnMorePage(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
