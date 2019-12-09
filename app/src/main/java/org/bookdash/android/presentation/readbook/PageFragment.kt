package org.bookdash.android.presentation.readbook

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import org.bookdash.android.databinding.FragmentPageBinding
import org.bookdash.android.domain.model.gson.Page
import java.io.File


class PageFragment : Fragment() {
    private var page: Page? = null
    private var page2: Page? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = FragmentPageBinding.inflate(inflater, container, false)
        binding.page = page
        binding.page2 = page2

        val readBookViewModel = ViewModelProviders.of(activity!!).get<ReadBookViewModel>(ReadBookViewModel::class.java)

        binding.nextClickListener = View.OnClickListener {
            Log.d(TAG, "Next Clicked")
            readBookViewModel.pageForwardEventTrigger.call()
        }
        binding.backClickListener = View.OnClickListener {
            Log.d(TAG, "Back Clicked")
            readBookViewModel.pageBackEventTrigger.call()
        }
        return binding.root

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        page = arguments?.getParcelable(PAGE_ARG)
        page2 = arguments?.getParcelable(PAGE_2_ARG)
        bookLocation = arguments?.getString(ARG_BOOK_LOCATION)
    }

    companion object {

        private val PAGE_ARG = "page_arg"
        private val ARG_BOOK_LOCATION = "arg_book_location"
        private val PAGE_2_ARG = "arg_page2"
        private var bookLocation: String? = null
        private val TAG = "PageFragment"

        @BindingAdapter("bind:imageUrl")
        @JvmStatic
        fun loadImage(view: ImageView, url: String?) {
            Glide.with(view.context).load(bookLocation + File.separator + url).into(view)
        }

        fun newInstance(page: Page?, page2: Page?, rootFileLocation: String): Fragment {
            val b = Bundle()
            b.putParcelable(PAGE_ARG, page)
            b.putParcelable(PAGE_2_ARG, page2)
            b.putString(ARG_BOOK_LOCATION, rootFileLocation)
            val pageFragment = PageFragment()
            pageFragment.arguments = b
            return pageFragment
        }
    }

}
