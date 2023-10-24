package com.fiction.me.ui.fragments.search.defaultpage

import androidx.core.os.bundleOf
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentSearchDefaultPageBinding
import com.fiction.me.ui.fragments.search.mainsearch.SearchFragment
import com.fiction.me.utils.Events.Companion.BOOK_CLICKED
import com.fiction.me.utils.Events.Companion.BOOK_ID_PROPERTY
import com.fiction.me.utils.Events.Companion.BOOK_SUMMARY_SHOWN
import com.fiction.me.utils.Events.Companion.PLACEMENT
import com.fiction.me.utils.Events.Companion.REFERRER
import com.fiction.me.utils.Events.Companion.SEARCH_SCREEN
import com.fiction.me.utils.Events.Companion.TAG_CLICKED
import com.fiction.me.utils.Events.Companion.TAG_NAME
import com.fiction.me.utils.cacheImages
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchDefaultPageFragment :
    FragmentBaseNCMVVM<SearchDefaultPageViewModel, FragmentSearchDefaultPageBinding>() {
    override val viewModel: SearchDefaultPageViewModel by viewModel()
    override val binding: FragmentSearchDefaultPageBinding by viewBinding()
    override fun onView() {
        viewModel.run {
            getPopularTagsList()
            getMostPopularBooks()
        }
        with(binding) {
            popularTagsView.run {
                setTitle(resources.getString(R.string.popular_tags))
            }
            bookInfoView.run {
                makeSeeAllVisible()
                makeTitleVisible()
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.popularBooksList) { books ->
            books?.let {
                binding.bookInfoView.submitList(it)
                books.forEach {
                    context?.let { it1 -> cacheImages(it1, it.imageCover) }
                }
            }
        }

        onEach(viewModel.popularTagsList) { popularTagList ->
            popularTagList?.let {
                binding.popularTagsView.submitList(it)
            }
        }
        onEach(viewModel.updateBooks) {
            activity?.supportFragmentManager?.setFragmentResult(
                FROM_DEFAULT_PAGE,
                bundleOf(POP_UP_BOOK_STATE to it)
            )
        }
    }

    override fun onViewClick() {
        with(binding) {
            popularTagsView.itemClickListener = { _ -> }

            popularTagsView.seeAllClickListener = {
                activity?.supportFragmentManager?.setFragmentResult(
                    NAVIGATE_POPULAR_TAGS,
                    bundleOf(IS_NAVIGATE to true)
                )
            }
            with(bookInfoView) {
                itemSaveClickListener = { id, isSaved ->
                    viewModel.updateBookItemData(id, isSaved)
                }
                onReadMoreClickListener = { id, title ->
                    sendEvent(id)
                    val book = viewModel.popularBooksList.value?.find { it.id == id }
                    activity?.supportFragmentManager?.setFragmentResult(
                        SearchFragment.NAVIGATE_TO_BOOK_SUMMARY,
                        bundleOf(
                            SearchFragment.BOOK to book?.bookInfo,
                            SearchFragment.ID_OF_BOOK to id
                        )
                    )
                }
                setSeeAllListener {
                    activity?.supportFragmentManager?.setFragmentResult(
                        NAVIGATE_MOST_POPULAR,
                        bundleOf(NAVIGATE_MOST_POPULAR to true)
                    )
                }
            }

            popularTagsView.itemCLick = { tagId, title ->
                viewModel.trackEvents(
                    TAG_CLICKED,
                    hashMapOf(TAG_NAME to title, PLACEMENT to SEARCH_SCREEN)
                )
                activity?.supportFragmentManager?.setFragmentResult(
                    FROM_DEFAULT_PAGE_OPEN_TAG,
                    bundleOf(POPULAR_TAGS to tagId)
                )
            }
        }
    }

    fun sendEvent(id: Long) {
        viewModel.run {
            trackEvents(
                BOOK_SUMMARY_SHOWN, hashMapOf(
                    REFERRER to SEARCH_SCREEN,
                    /*Events.SECTION to Events.CURRENT_READS,*/
                    BOOK_ID_PROPERTY to id
                )
            )
            trackEvents(
                BOOK_CLICKED,
                hashMapOf(BOOK_ID_PROPERTY to id, PLACEMENT to SEARCH_SCREEN)
            )
        }
    }

    companion object {
        const val NAVIGATE_POPULAR_TAGS = "NAVIGATE_POPULAR_TAGS"
        const val IS_NAVIGATE = "IS_NAVIGATE"
        const val POP_UP_BOOK_STATE = "POP_UP_BOOK_STATE"
        const val FROM_DEFAULT_PAGE = "POP_UP_BOOK_STATE"
        const val POPULAR_TAGS = "POPULAR_TAGS"
        const val NAVIGATE_MOST_POPULAR = "NAVIGATE_MOST_POPULAR"
        const val FROM_DEFAULT_PAGE_OPEN_TAG = "FROM_DEFAULT_PAGE_OPEN_TAG"
    }
}