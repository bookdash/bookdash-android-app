package org.bookdash.android.presentation.readbook;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.bookdash.android.domain.model.gson.Page;

import java.util.List;


public class PageAdapter extends FragmentStatePagerAdapter {

    private final String rootFileLocation;
    private List<Page> pages;


    public PageAdapter(FragmentManager fm, List<Page> pages, String rootFileLocation) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.pages = pages;
        this.rootFileLocation = rootFileLocation;
    }

    @Override
    public Fragment getItem(int position) {
        int realPos = position * 2;
        Page page = pages.get(realPos);
        Page page2 = null;
        if (realPos + 1 < pages.size()) {
            page2 = pages.get(realPos + 1);
        }
        return PageFragment.Companion.newInstance(page, page2, rootFileLocation);
    }


    @Override
    public int getCount() {
        return (int) Math.ceil(((float) this.pages.size()) / 2.0f);
    }

}

