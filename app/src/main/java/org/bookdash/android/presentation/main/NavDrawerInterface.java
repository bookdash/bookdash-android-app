package org.bookdash.android.presentation.main;

import android.support.v7.widget.Toolbar;


public interface NavDrawerInterface {
    void openNavDrawer();

    void closeNavDrawer();

    void lockNavDrawer();

    void unlockNavDrawer();

    void setToolbar(Toolbar toolbar);
}
