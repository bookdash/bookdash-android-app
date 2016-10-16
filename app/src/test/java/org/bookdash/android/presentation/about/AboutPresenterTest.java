package org.bookdash.android.presentation.about;

import android.app.UiModeManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Environment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * @author rebeccafranks
 * @since 15/11/07.
 */

public class AboutPresenterTest {

    @Mock
    private AboutContract.View aboutView;

    @Mock
    private Context context;

    private AboutPresenter aboutPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        aboutPresenter = new AboutPresenter(aboutView);

    }

    @Test
    public void clickLearnMore_ShowsMorePage(){
        aboutPresenter.clickLearnMore();

        verify(aboutView).showLearnMorePage("http://bookdash.org");
    }
}
