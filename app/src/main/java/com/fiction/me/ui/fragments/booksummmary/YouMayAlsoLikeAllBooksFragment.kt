package com.fiction.me.ui.fragments.booksummmary

import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentYouMayAlsoLikeAllBooksBinding
import com.fiction.me.extensions.startHeaderTextAnimation
import com.fiction.me.extensions.toJson
import com.fiction.me.ui.fragments.explore.SuggestBookSeeAllFragmentArgs
import com.fiction.me.utils.InternalDeepLink
import com.fiction.me.utils.cacheImages
import org.koin.androidx.viewmodel.ext.android.viewModel

class YouMayAlsoLikeAllBooksFragment :
    FragmentBaseNCMVVM<YouMayAlsoLikeAllBooksViewModel, FragmentYouMayAlsoLikeAllBooksBinding>() {

    override val binding: FragmentYouMayAlsoLikeAllBooksBinding by viewBinding()
    override val viewModel: YouMayAlsoLikeAllBooksViewModel by viewModel()
    private val args: SuggestBookSeeAllFragmentArgs by navArgs()
    override fun onView() {
        viewModel.getBooks()
        with(binding) {
            customAppBar.setFirstItemDrawable(R.drawable.ic_back)
            customAppBar.setTitle(resources.getString(R.string.you_may_also_like))
        }
    }

    override fun onEach() {
        onEach(viewModel.bookInfoList) { books ->
            books?.let {
                with(binding) {
                    it.forEach { context?.let { it1 -> cacheImages(it1, it.imageCover) } }
                    booksInfoView.submitList(it)
                    loadingContainer.isVisible = false
                }
            }
        }
        onEach(viewModel.printMessageAddedLib) {
            binding.customSnackBar.startHeaderTextAnimation(R.string.added_to_library)
        }
        onEach(viewModel.printMessageRemoveLib) {
            binding.customSnackBar.startHeaderTextAnimation(R.string.remove_from_library)
        }
    }

    override fun onViewClick() {
        with(binding) {
            customAppBar.setOnNavigationClickListener {
                popBackStack()
            }

            booksInfoView.itemSaveClickListener = { id, isSaved ->
                viewModel.updateBookItemData(id, isSaved)
            }

            booksInfoView.onReadMoreClickListener = { id, title ->
                viewModel.sendEvent(args.feedType, id)
                val book = viewModel.bookInfoList.value?.find { it.id == id }
                /* val directions =
                     YouMayAlsoLikeAllBooksFragmentDirections.actionYouMayAlsoLikeAllBooksFragmentToBookSummaryFragment(
                         id,
                         bookInfo = book?.bookInfo
                     )
                 navigateFragment(directions)*/
                val deepLink = InternalDeepLink.makeCustomDeepLinkToBookSummary(id,book?.bookInfo?.toJson() ?: "").toUri()
                navigateFragment(deepLink)
            }
        }
    }
}
