package com.fiction.me.ui.fragments.explore

import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.fiction.domain.model.BestsellersModel
import com.fiction.domain.model.GenreModel
import com.fiction.domain.model.StoryModel
import com.fiction.domain.model.SuggestedBooksModel
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentExploreBinding
import com.fiction.me.extensions.startHeaderTextAnimation
import com.fiction.me.ui.fragments.explore.adapters.ExploreListAdapter
import com.fiction.me.utils.Events.Companion.EXPLORE
import com.fiction.me.utils.Events.Companion.PLACEMENT
import com.fiction.me.utils.Events.Companion.SECTION
import com.fiction.me.utils.cacheImages
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExploreFragment : FragmentBaseNCMVVM<ExploreViewModel, FragmentExploreBinding>() {

    override val binding: FragmentExploreBinding by viewBinding()
    override val viewModel: ExploreViewModel by viewModel()
    private var exploreListAdapter =
        ExploreListAdapter({ suggestedBookListId, suggestedBookId, isAddedLibrary, pos, offset ->
            viewModel.updateSuggestedBookItemData(
                suggestedBookListId,
                suggestedBookId,
                isAddedLibrary,
                pos,
                offset
            )
        },
            { storyListId, storyId, isAddedLibrary, pos, offset ->
                viewModel.updateStoryItemData(storyListId, storyId, isAddedLibrary, pos, offset)
            }, { directions, section, isSeeAllClick, title, bookId ->
                if (section != null) {
                    if (isSeeAllClick)
                        viewModel.sendEventSeeAllClick(section)
                    else viewModel.sendEventToOpenBookSummary(section, bookId)
                }
                val bundle = bundleOf()
                bundle.putAll(directions.arguments)
                section?.let {
                    bundle.putString(SECTION, viewModel.getSection(it))
                }
                navigateFragment(directions.actionId, bundle)
            }, { event, eventProperty ->
                val prepareEventProperty = hashMapOf<String, Any>(PLACEMENT to EXPLORE)
                prepareEventProperty.putAll(eventProperty)
                viewModel.trackEvents(
                    event,
                    prepareEventProperty
                )
            }
        )

    override fun onView() {
        viewModel.sendEventForAppLaunch()
        viewModel.getExploreDataList()
        with(binding) {
            customAppBar.run {
                setTitle(resources.getString(R.string.explore))
                setFirstItemDrawable(R.drawable.ic_logo)
                makeLastItemVisible()
            }
            exploreListRV.apply {
                context?.let {
                    layoutManager =
                        LinearLayoutManager(it)
                    adapter = exploreListAdapter
                }
                itemAnimator = null
            }
            shimmerEffect.setContent {
                ShimmerEffect()
            }
            /*earnedCoinDialog.setContent {
                EarnedCoinDialog(viewModel.earnedCoin){
                    viewModel.earnedCoin.value = false
                }
            }*/
        }

        activity?.onBackPressedDispatcher?.addCallback(this) {}

    }

    override fun onEach() {
        onEach(viewModel.exploreDataList) { data ->
            context?.let { context ->
                data?.forEach { item ->
                    when (item) {
                        is BestsellersModel -> item.bestsellersList.forEach {
                            cacheImages(
                                context,
                                it.cover
                            )
                        }
                        is StoryModel -> item.storyList.forEach { cacheImages(context, it.picture) }
                        is SuggestedBooksModel -> item.booksList.forEach {
                            cacheImages(
                                context,
                                it.image
                            )
                        }
                        is GenreModel -> item.genreList.forEach { cacheImages(context, it.icon) }
                    }
                }
            }
            binding.shimmerEffect.isVisible = false
            exploreListAdapter.submitList(data)

        }
        onEach(viewModel.printMessageAddedLib) {
            binding.customSnackBar.startHeaderTextAnimation(R.string.added_to_library)
        }
        onEach(viewModel.printMessageRemoveLib) {
            binding.customSnackBar.startHeaderTextAnimation(R.string.remove_from_library)
        }
        onEach(viewModel.giftWelcomeBox) {
            val directions = ExploreFragmentDirections.actionExploreFragmentToWelcomeGiftFragment(
                it?.body ?: "",
                it?.id ?: -1,
                it?.coinCount ?: 0
            )
            navigateFragment(directions)
        }
    }

    override fun onViewClick() {
        with(binding) {
            customAppBar.setOnLastItemClickListener {
                navigateFragment(R.id.action_exploreFragment_to_libraryFragment)
            }
        }
    }
}