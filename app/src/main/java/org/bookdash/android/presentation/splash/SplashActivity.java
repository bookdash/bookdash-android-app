package org.bookdash.android.presentation.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import org.bookdash.android.Injection;
import org.bookdash.android.R;
import org.bookdash.android.presentation.listbooks.ListBooksActivity;

import za.co.riggaroo.materialhelptutorial.tutorial.MaterialTutorialActivity;

/**
 * @author Rebecca Franks
 * @since 2015/07/16 3:16 PM
 */
public class SplashActivity extends AppCompatActivity implements SplashContract.View {
    private static final int SPLASH_SCREEN_REQUEST_CODE = 1;
    private SplashContract.UserActionsListener splashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashPresenter = new SplashPresenter(this, Injection.provideSettingsRepo(this));
        showSplashAfterDelay();
    }

    private void showSplashAfterDelay(){
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                splashPresenter.loadSplash();
            }
        }, 1000);

    }
    @Override
    public void loadTutorial() {
        Intent mainAct = new Intent(SplashActivity.this, MaterialTutorialActivity.class);
        mainAct.putParcelableArrayListExtra(MaterialTutorialActivity.MATERIAL_TUTORIAL_ARG_TUTORIAL_ITEMS, splashPresenter.getTutorialItems(this));
        startActivityForResult(mainAct, SPLASH_SCREEN_REQUEST_CODE);
    }



    @Override
    public void loadMainScreen() {
        Intent mainAct = new Intent(SplashActivity.this, ListBooksActivity.class);
        startActivity(mainAct);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //    super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SPLASH_SCREEN_REQUEST_CODE){
            splashPresenter.finishedTutorial();

        }
    }
}
