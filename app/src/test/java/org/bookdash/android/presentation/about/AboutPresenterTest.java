package org.bookdash.android.presentation.about;

import android.app.UiModeManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Environment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;

import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * @author rebeccafranks
 * @since 15/11/07.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({Environment.class, File.class})
public class AboutPresenterTest {

    @Mock
    private AboutContract.View aboutView;

    @Mock
    private Context context;
    @Mock
    private UiModeManager uiModeManager;
    @Mock
    private File directory;

    @InjectMocks
    private AboutPresenter aboutPresenter = new AboutPresenter(aboutView);

    @Test
    public void clickOnSomethingRequiresContext() {
        when(context.getSystemService(Context.UI_MODE_SERVICE)).thenReturn(uiModeManager);
        when(uiModeManager.getCurrentModeType()).thenReturn(Configuration.UI_MODE_TYPE_NORMAL);

        mockStatic(Environment.class, File.class);

        when(Environment.getExternalStorageDirectory()).thenReturn(directory);

        //Do actual test code
    }
}
