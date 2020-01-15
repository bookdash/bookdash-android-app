package org.bookdash.android.presentation.readbook

import androidx.lifecycle.ViewModel

import org.bookdash.android.presentation.utils.SingleLiveEvent

/**
 * @author rebeccafranks
 * @since 2017/10/27.
 */

class ReadBookViewModel : ViewModel() {
    val pageBackEventTrigger = SingleLiveEvent<Void>()
    val pageForwardEventTrigger = SingleLiveEvent<Void>()


}
