package org.bookdash.android.presentation.readbook;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.bookdash.android.R;
import org.bookdash.android.databinding.FragmentPageBinding;
import org.bookdash.android.domain.model.gson.Page;

import java.io.File;


public class PageFragment extends Fragment {

    private static final String PAGE_ARG = "page_arg";
    private static final String ARG_BOOK_LOCATION = "arg_book_location";
    private static final String PAGE_2_ARG = "arg_page2";
    private Page page;
    private static String bookLocation;
    private Page page2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentPageBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_page, container, false);
        binding.setPage(page);
        binding.setPage2(page2);

        return binding.getRoot();

    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String url) {
        Glide.with(view.getContext()).load(bookLocation + File.separator + url).into(view);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getParcelable(PAGE_ARG);
        page2 = getArguments().getParcelable(PAGE_2_ARG);
        bookLocation = getArguments().getString(ARG_BOOK_LOCATION);
    }

    public static Fragment newInstance(Page page, Page page2, String rootFileLocation) {
        Bundle b = new Bundle();
        b.putParcelable(PAGE_ARG, page);
        b.putParcelable(PAGE_2_ARG, page2);
        b.putString(ARG_BOOK_LOCATION, rootFileLocation);
        PageFragment pageFragment = new PageFragment();
        pageFragment.setArguments(b);
        return pageFragment;
    }

}
