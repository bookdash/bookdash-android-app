package org.bookdash.android.presentation.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;



public abstract class BaseAppCompatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected abstract String getScreenName();


}
