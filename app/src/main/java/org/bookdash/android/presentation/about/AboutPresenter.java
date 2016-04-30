package org.bookdash.android.presentation.about;


/**
 * @author rebeccafranks
 * @since 15/11/07.
 */
public class AboutPresenter implements AboutContract.UserActions {
    static final String BOOKDASH_SITE_URL = "http://bookdash.org";

    private AboutContract.View aboutView;

    public AboutPresenter(AboutContract.View aboutView) {
        this.aboutView = aboutView;
    }

    @Override
    public void clickLearnMore() {
        aboutView.showLearnMorePage(BOOKDASH_SITE_URL);

    }

}
