package com.fiction.me.ui.fragments.mainlibrary.finished

import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentAllReadBooksBinding
import com.fiction.me.extensions.toJson
import com.fiction.me.utils.Events.Companion.BOOK_CLICKED
import com.fiction.me.utils.Events.Companion.BOOK_ID_PROPERTY
import com.fiction.me.utils.Events.Companion.BOOK_SUMMARY_SHOWN
import com.fiction.me.utils.Events.Companion.FINISHED
import com.fiction.me.utils.Events.Companion.REFERRER
import com.fiction.me.utils.Events.Companion.USER_LIBRARY
import com.fiction.me.utils.InternalDeepLink
import org.koin.androidx.viewmodel.ext.android.viewModel

class SeeAllFinishedBooksFragment :
    FragmentBaseNCMVVM<SeeAllFinishedBooksViewModel, FragmentAllReadBooksBinding>() {
    override val viewModel: SeeAllFinishedBooksViewModel by viewModel()
    override val binding: FragmentAllReadBooksBinding by viewBinding()

    override fun onView() {
        with(binding) {
            customAppBar.run {
                setTitle(resources.getString(R.string.finished))
                setFirstItemDrawable(R.drawable.ic_back)
            }
            loadingContainer.isVisible = false
        }
    }

    override fun onEach() {
        onEach(viewModel.pagingData) {
            binding.allCurrentReadBooksView.submitData(lifecycle, it)
        }
    }

    override fun onViewClick() {
        with(binding) {
            allCurrentReadBooksView.onItemClickListener = { id, title ->
                sendEvent(id)
                val book = allCurrentReadBooksView.getList().find { it.id == id }
               /* val directions =
                    SeeAllFinishedBooksFragmentDirections.actionSeeAllFinishedBooksFragmentToBookSummaryFragment(
                        id,
                        bookInfo = book?.bookInfo
                    )
                navigateFragment(directions)*/
                val deepLink = InternalDeepLink.makeCustomDeepLinkToBookSummary(id,book?.bookInfo?.toJson() ?: "").toUri()
                navigateFragment(deepLink)
            }
            customAppBar.setOnNavigationClickListener {
                popBackStack()
            }
        }
    }

    fun sendEvent(id: Long) {
        viewModel.run {
            trackEvents(
                BOOK_SUMMARY_SHOWN, hashMapOf(
                    REFERRER to FINISHED, /*SECTION to ,*/
                    BOOK_ID_PROPERTY to id
                )
            )
            trackEvents(
                BOOK_CLICKED,
                hashMapOf(REFERRER to USER_LIBRARY, BOOK_ID_PROPERTY to id)
            )
        }
    }
}