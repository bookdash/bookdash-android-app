package org.bookdash.android.presentation.splash;

import android.content.Context;

import java.util.ArrayList;

import za.co.riggaroo.materialhelptutorial.TutorialItem;

/**
 * @author rebeccafranks
 * @since 15/11/05.
 */
public interface SplashContract {
    interface View {
        void loadTutorial();

        void loadMainScreen();
    }

    interface UserActionsListener {
        void loadSplash();

        ArrayList<TutorialItem> getTutorialItems(Context context);

        void finishedTutorial();
    }
}
