package org.bookdash.android.presentation.settings;

import org.bookdash.android.R;
import org.bookdash.android.data.tutorial.TutorialsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Locale;

import za.co.riggaroo.materialhelptutorial.TutorialItem;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by michel.onwordi on 14/03/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class SettingsPresenterTest {

    public static final String TUTORIAL_TEST_ITEM_FORMAT = "%s_%d";
    @Mock
    private TutorialsRepository mockTutorialsRepository;
    @Mock
    private SettingsContract.View mockSettingsView;
    @Captor
    ArgumentCaptor<ArrayList<TutorialItem>> argumentCaptor;

    private SettingsPresenter presenter;

    @Before
    public void setup() {
        presenter = new SettingsPresenter(mockTutorialsRepository);
        presenter.attachView(mockSettingsView);
    }

    @Test
    public void openTutorialScreen() throws Exception {
        ArrayList<TutorialItem> testData = generateTestData(3);
        when(mockTutorialsRepository.getTutorialItems()).thenReturn(testData);

        presenter.openTutorialScreen();

        verify(mockSettingsView).showTutorialScreen(argumentCaptor.capture());
        assertEquals(testData, argumentCaptor.getValue());
    }

    private ArrayList<TutorialItem> generateTestData(int count) {
        ArrayList<TutorialItem> testTutorialItems = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            testTutorialItems.add(new TutorialItem(String.format(Locale.getDefault(),
                TUTORIAL_TEST_ITEM_FORMAT, "tutorial_item_test", count),
                String.format(Locale.getDefault(), TUTORIAL_TEST_ITEM_FORMAT, "tutorial_item_test_subtitle_text", count),
                R.color.slide_3, R.drawable.tut_page_3_foreground, R.drawable.tut_page_3_background));
        }
        return testTutorialItems;
    }
}