package org.bookdash.android.presentation.readbook;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import org.bookdash.android.R;
import org.bookdash.android.domain.model.gson.BookPages;
import org.bookdash.android.presentation.activity.BaseAppCompatActivity;


public class BookDetailActivity extends BaseAppCompatActivity {
    public static final String BOOK_PAGES = "book_pages";
    public static final String LOCATION_BOOK = "book_location";
    public static final String BOOK_ARG = "book_obj";
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        viewPager = findViewById(R.id.view_pager);
        viewPager.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                viewPager.getViewTreeObserver().removeOnPreDrawListener(this);
                viewPager.setTranslationX(viewPager.getMeasuredWidth());
                viewPager.animate().translationXBy(-viewPager.getMeasuredWidth()).setDuration(1000);
                return true;
            }
        });
        String book = getIntent().getStringExtra(BOOK_ARG);
        BookPages bookPages = getIntent().getParcelableExtra(BOOK_PAGES);
        bookPages.getPages().add(0, null);
        String bookLocation = getIntent().getStringExtra(LOCATION_BOOK);
        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager(), bookPages.getPages(),
                bookLocation);

        viewPager.setAdapter(pageAdapter);
        viewPager.setOffscreenPageLimit(1);


    }

    @Override
    protected String getScreenName() {
        return "BookDetailScreen";
    }


}
