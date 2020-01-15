/*
MIT License

Copyright (c) 2015 Rebecca Franks

Permission is hereby granted, free of charge, to any person obtaining a copy of this software
and associated documentation files (the "Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject
to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or
substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package za.co.riggaroo.materialhelptutorial.tutorial;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.view.View;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import za.co.riggaroo.materialhelptutorial.MaterialTutorialFragment;
import za.co.riggaroo.materialhelptutorial.TutorialItem;

/**
 * @author rebeccafranks
 * @since 15/11/09.
 */
public class MaterialTutorialPresenter implements MaterialTutorialContract.UserActionsListener {
    private final Context context;
    private MaterialTutorialContract.View tutorialView;
    private List<MaterialTutorialFragment> fragments;
    private List<TutorialItem> tutorialItems;

    public MaterialTutorialPresenter(Context context, MaterialTutorialContract.View tutorialView) {
        this.tutorialView = tutorialView;
        this.context = context;
    }


    @Override
    public void loadViewPagerFragments(List<TutorialItem> tutorialItems) {
        fragments = new ArrayList<>();
        this.tutorialItems = tutorialItems;
        for (int i = 0; i < tutorialItems.size(); i++) {
            MaterialTutorialFragment helpTutorialImageFragment;
            helpTutorialImageFragment = MaterialTutorialFragment.newInstance(tutorialItems.get(i), i);
            fragments.add(helpTutorialImageFragment);
        }
        tutorialView.setViewPagerFragments(fragments);

        if (tutorialItems.size() == 1) {
            tutorialView.showDoneButton();
        } else {
            tutorialView.showSkipButton();
        }
    }

    @Override
    public void doneOrSkipClick() {
        tutorialView.showEndTutorial();
    }

    @Override
    public void nextClick() {
        tutorialView.showNextTutorial();
    }

    @Override
    public void onPageSelected(int pageNo) {
        if (pageNo >= fragments.size() - 1) {
            tutorialView.showDoneButton();
        } else {
            tutorialView.showSkipButton();
        }
    }

    @Override
    public void transformPage(View page, float position) {
        int pagePosition = (int) page.getTag();
        if (position <= -1.0f || position >= 1.0f) {

        } else if (position == 0.0f) {
            tutorialView.setBackgroundColor(ContextCompat.getColor(context, tutorialItems.get(pagePosition).getBackgroundColor()));
        } else {
            fadeNewColorIn(pagePosition, position);
        }
    }

    @Override
    public int getNumberOfTutorials() {
        if (tutorialItems != null) {
            return tutorialItems.size();
        }
        return 0;
    }

    private void fadeNewColorIn(int index, float multiplier) {
        if (multiplier < 0) {

            int colorStart = ContextCompat.getColor(context, tutorialItems.get(index).getBackgroundColor());
            if (index + 1 == fragments.size()) {
                tutorialView.setBackgroundColor(colorStart);
                return;
            }
            int colorEnd = ContextCompat.getColor(context, tutorialItems.get(index + 1).getBackgroundColor());
            int colorToSet = (int) (new ArgbEvaluator().evaluate(Math.abs(multiplier), colorStart, colorEnd));
            tutorialView.setBackgroundColor(colorToSet);
        }

    }

}
