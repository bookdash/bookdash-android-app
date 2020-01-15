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

package za.co.riggaroo.materialhelptutorial;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

/**
 * @author rebeccafranks
 * @since 2015/10/15.
 */
public class MaterialTutorialFragment extends Fragment {
    private static final String ARG_TUTORIAL_ITEM = "arg_tut_item";
    private static final String TAG = "MaterialTutFragment";
    private static final String ARG_PAGE = "arg_page";

    public static MaterialTutorialFragment newInstance(TutorialItem tutorialItem, int page) {
        MaterialTutorialFragment helpTutorialImageFragment = new MaterialTutorialFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_TUTORIAL_ITEM, tutorialItem);
        args.putInt(ARG_PAGE, page);
        helpTutorialImageFragment.setArguments(args);
        return helpTutorialImageFragment;
    }

    private TutorialItem tutorialItem;
    int page;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        tutorialItem = b.getParcelable(ARG_TUTORIAL_ITEM);
        page = b.getInt(ARG_PAGE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_help_tutorial_image, container, false);
        v.setTag(page);

        ImageView imageViewFront = (ImageView) v.findViewById(R.id.fragment_help_tutorial_imageview);
        ImageView imageViewBack = (ImageView) v.findViewById(R.id.fragment_help_tutorial_imageview_background);
        TextView textViewSubTitle = (TextView) v.findViewById(R.id.fragment_help_tutorial_subtitle_text);

        TextView textView = (TextView) v.findViewById(R.id.fragment_help_tutorial_text);
        if (!TextUtils.isEmpty(tutorialItem.getTitleText())) {
            textView.setText(tutorialItem.getTitleText());
        } else if (tutorialItem.getTitleTextRes() != -1) {
            textView.setText(tutorialItem.getTitleTextRes());
        }
        if (!TextUtils.isEmpty(tutorialItem.getSubTitleText())) {
            textViewSubTitle.setText(tutorialItem.getSubTitleText());
        } else if (tutorialItem.getSubTitleTextRes() != -1) {
            textViewSubTitle.setText(tutorialItem.getSubTitleTextRes());
        }
        if (tutorialItem.getBackgroundImageRes() != -1) {
            Glide.with(this).load(tutorialItem.getBackgroundImageRes()).into(imageViewBack);
        }
        if (tutorialItem.getForegroundImageRes() != -1 && !tutorialItem.isGif()) {
            Glide.with(this).load(tutorialItem.getForegroundImageRes()).into(imageViewFront);
        }
        if (tutorialItem.getForegroundImageRes() != -1 && tutorialItem.isGif()) {
            Glide.with(this)
                    .asGif()
                    .load(tutorialItem.getForegroundImageRes())
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA))
                    .into(imageViewFront);
        }
        return v;
    }

  /*  public void onTranform(int pageNumber, float transformation) {
        Log.d(TAG, "onTransform:" + transformation);
        if (!isAdded()) {
            return;
        }
        if (transformation == 0){
            imageViewBack.setTranslationX(0);
            return;
        }
        int pageWidth = getView().getWidth();
        Log.d(TAG, "onTransform Added page Width:" + pageWidth);

        imageViewBack.setTranslationX(-transformation * (pageWidth / 2)); //Half the normal speed

    }*/
}

