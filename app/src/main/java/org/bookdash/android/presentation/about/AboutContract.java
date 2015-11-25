package org.bookdash.android.presentation.about;

/**
 * @author rebeccafranks
 * @since 15/11/07.
 */
public interface AboutContract {

    interface View {
        void showLearnMorePage(String url);
    }

    interface UserActions {
        void clickLearnMore();
    }
}
