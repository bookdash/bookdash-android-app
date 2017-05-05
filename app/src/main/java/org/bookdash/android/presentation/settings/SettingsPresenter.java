package org.bookdash.android.presentation.settings;

import org.bookdash.android.data.settings.SettingsRepository;
import org.bookdash.android.data.tracking.Analytics;
import org.bookdash.android.data.tutorial.TutorialsRepository;
import org.bookdash.android.presentation.base.BasePresenter;

import rx.Observer;


public class SettingsPresenter extends BasePresenter<SettingsContract.View> implements SettingsContract.Presenter {

    private TutorialsRepository repository;
    private SettingsRepository settings;

    private Analytics analytics;

    public SettingsPresenter(TutorialsRepository repository, Analytics analytics, SettingsRepository settings) {
        this.repository = repository;
        this.analytics = analytics;
        this.settings = settings;
    }

    @Override
    public void openTutorialScreen() {
        checkViewAttached();
        analytics.trackViewHelpTutorialAgain();
        getView().showTutorialScreen(repository.getTutorialItems());
    }

    @Override
    public void setNewBookNotificationSubscriptionStatus(final boolean onOff) {
        checkViewAttached();
        analytics.trackUserToggleNewBookNotifications(onOff);
        settings.setNewBookNotificationStatus(onOff).subscribe(new Observer<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(final Throwable e) {

            }

            @Override
            public void onNext(final Boolean aBoolean) {

            }
        });

    }
}
