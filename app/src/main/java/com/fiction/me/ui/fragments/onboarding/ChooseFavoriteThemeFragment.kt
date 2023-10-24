package com.fiction.me.ui.fragments.onboarding

import androidx.activity.addCallback
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentChooseFavoriteThemeBinding
import com.fiction.me.utils.Events.Companion.FAVOURITE_THEME_SCREEN_CONTINUE_BUTTON_CLICKED
import com.fiction.me.utils.Events.Companion.FAVOURITE_THEME_SCREEN_SHOWN
import com.fiction.me.utils.Events.Companion.ONBOARDING
import com.fiction.me.utils.Events.Companion.PLACEMENT
import com.fiction.me.utils.Events.Companion.TAG_CLICKED
import com.fiction.me.utils.Events.Companion.TAG_NAME
import com.google.android.flexbox.FlexboxLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChooseFavoriteThemeFragment :
    FragmentBaseNCMVVM<ChooseFavoriteThemeViewModel, FragmentChooseFavoriteThemeBinding>() {
    override val binding: FragmentChooseFavoriteThemeBinding by viewBinding()
    override val viewModel: ChooseFavoriteThemeViewModel by viewModel()
    private val themeAdapter = FavoriteThemeAdapter { title, id, isSelected ->
        viewModel.updateThemeSelectionState(id, isSelected)
        if (isSelected)
            viewModel.trackEvents(
                TAG_CLICKED,
                hashMapOf(TAG_NAME to title, PLACEMENT to ONBOARDING)
            )
    }

    override fun onView() {
        viewModel.trackEvents(FAVOURITE_THEME_SCREEN_SHOWN)
        with(binding) {
            themesRV.apply {
                context?.let {
                    layoutManager = FlexboxLayoutManager(it)
                }
                itemAnimator = null
                adapter = themeAdapter

            }
            viewModel.getThemes()
        }
        activity?.onBackPressedDispatcher?.addCallback(this) {
            //activity?.finish()
        }
    }

    override fun onViewClick() {
        binding.continueBtn.setOnClickListener {
            viewModel.run {
                updateFavoriteTagsSelection()
                deleteOnBoardingSetting()
            }
            val directions =
                ChooseFavoriteThemeFragmentDirections.actionChooseFavoriteThemeFragmentToAnalyzeNovelFragment()
            navigateFragment(directions)
        }
    }

    override fun onEach() {
        onEach(viewModel.themeList) {
            themeAdapter.submitList(it)
            viewModel.getFilteredListById()?.let {
                binding.continueBtn.run {
                    isEnabled = it.isNotEmpty()
                }
            }
        }
    }
}