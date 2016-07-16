package org.bookdash.android.presentation.splash;

import org.bookdash.android.data.settings.SettingsRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author rebeccafranks
 * @since 15/11/05.
 */
public class SplashPresenterTest {

    @Mock
    private SettingsRepository settingsRepository;
    @Mock
    private SplashContract.View splashView;
    private SplashContract.UserActionsListener splashPresenter;

    @Before
    public void setupTests() {
        MockitoAnnotations.initMocks(this);
        splashPresenter = new SplashPresenter(splashView, settingsRepository);
    }

    @Test
    public void loadSplashFistTime() {
        when(settingsRepository.isFirstTime()).thenReturn(true);

        splashPresenter.loadSplash();

        verify(splashView).loadTutorial();

    }

    @Test
    public void loadSplashNotFirstTime() {
        when(settingsRepository.isFirstTime()).thenReturn(false);

        splashPresenter.loadSplash();

        verify(splashView).loadMainScreen();
    }

}
