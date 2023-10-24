package com.name.jat.ui.fragments.onboarding

import com.google.android.flexbox.FlexboxLayoutManager
import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.databinding.FragmentChooseFavoriteThemeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChooseFavoriteThemeFragment :
    FragmentBaseNCMVVM<ChooseFavoriteThemeViewMode, FragmentChooseFavoriteThemeBinding>() {
    override val binding: FragmentChooseFavoriteThemeBinding by viewBinding()
    override val viewModel: ChooseFavoriteThemeViewMode by viewModel()
    private val themeAdapter = FavoriteThemeAdapter { id, isSelected ->
        viewModel.updateThemeSelectionState(id, isSelected)
    }

    override fun onView() {
        with(binding) {
            themesRV.apply {
                context?.let {
                    layoutManager = FlexboxLayoutManager(it)
                }
                itemAnimator = null
                adapter = themeAdapter

            }
            viewModel.getTheme()
        }
    }

    override fun onViewClick() {
        binding.continueBtn.setOnClickListener {
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