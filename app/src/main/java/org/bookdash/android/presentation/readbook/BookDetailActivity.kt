package org.bookdash.android.presentation.readbook

import android.os.Bundle
import android.view.ViewTreeObserver
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import org.bookdash.android.R
import org.bookdash.android.domain.model.gson.BookPages
import org.bookdash.android.presentation.activity.BaseAppCompatActivity


class BookDetailActivity : BaseAppCompatActivity() {
    private lateinit var viewPager: ViewPager

    private lateinit var pageAdapter: PageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        viewPager = findViewById(R.id.view_pager)
        viewPager.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                viewPager.viewTreeObserver.removeOnPreDrawListener(this)
                viewPager.translationX = viewPager.measuredWidth.toFloat()
                viewPager.animate().translationXBy((-viewPager.measuredWidth).toFloat()).duration = 1000
                return true
            }
        })
        //val book = intent.getStringExtra(BOOK_ARG)
        val bookPages = intent.getParcelableExtra<BookPages>(BOOK_PAGES)
        bookPages.pages.add(0, null)
        val bookLocation = intent.getStringExtra(LOCATION_BOOK)
        pageAdapter = PageAdapter(supportFragmentManager, bookPages.pages,
                bookLocation)

        viewPager.adapter = pageAdapter
        viewPager.offscreenPageLimit = 1

        setupViewModelListeners()
    }

    private fun setupViewModelListeners() {
        val readBookViewModel = ViewModelProviders.of(this).get<ReadBookViewModel>(ReadBookViewModel::class.java)
        readBookViewModel.pageBackEventTrigger.observe(this, Observer {
            if (viewPager.currentItem != 0) {
                viewPager.setCurrentItem(viewPager.currentItem - 1, true)
            }
        })
        readBookViewModel.pageForwardEventTrigger.observe(this, Observer {
            if (viewPager.currentItem != viewPager.childCount -1) {
                viewPager.setCurrentItem(viewPager.currentItem + 1, true)
            }
        })
    }

    override fun getScreenName(): String {
        return "BookDetailScreen"
    }

    companion object {
        val BOOK_PAGES = "book_pages"
        val LOCATION_BOOK = "book_location"
        val BOOK_ARG = "book_obj"
    }


}
