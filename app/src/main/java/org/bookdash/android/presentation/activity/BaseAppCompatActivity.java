package org.bookdash.android.presentation.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;



public abstract class BaseAppCompatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected abstract String getScreenName();


}
