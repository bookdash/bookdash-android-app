package org.bookdash.android.presentation.main;

import androidx.appcompat.widget.Toolbar;


public interface NavDrawerInterface {
    void openNavDrawer();

    void closeNavDrawer();

    void setToolbar(Toolbar toolbar);
}
