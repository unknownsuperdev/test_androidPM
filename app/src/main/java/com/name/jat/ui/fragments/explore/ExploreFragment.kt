package com.name.jat.ui.fragments.explore

import android.provider.Settings
import androidx.recyclerview.widget.LinearLayoutManager
import com.name.jat.R
import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.databinding.FragmentExploreBinding
import com.name.jat.extensions.startHeaderTextAnimation
import com.name.jat.ui.fragments.explore.adapters.ExploreListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExploreFragment : FragmentBaseNCMVVM<ExploreViewModel, FragmentExploreBinding>() {

    override val binding: FragmentExploreBinding by viewBinding()
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
            }, { directions ->
                navigateFragment(directions)
            }
        )
    override val viewModel: ExploreViewModel by viewModel()

    override fun onView() {
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
        }
        val uuid = Settings.Secure.getString(context?.contentResolver, Settings.Secure.ANDROID_ID)
        viewModel.getGuestToken(uuid)
    }

    override fun onEach() {
        onEach(viewModel.exploreDataList) {
            exploreListAdapter.submitList(it)
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
            customAppBar.setOnLastItemClickListener {
                navigateFragment(R.id.action_exploreFragment_to_libraryFragment)
            }
        }
    }

}