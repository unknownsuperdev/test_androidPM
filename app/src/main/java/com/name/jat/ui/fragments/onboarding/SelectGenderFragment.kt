package com.name.jat.ui.fragments.onboarding

import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.databinding.FragmentSelectGenderBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectGenderFragment :
    FragmentBaseNCMVVM<SelectGenderViewModel, FragmentSelectGenderBinding>() {
    override val binding: FragmentSelectGenderBinding by viewBinding()
    override val viewModel: SelectGenderViewModel by viewModel()

    override fun onViewClick() {
        with(binding) {
            male.setOnClickListener {
                selectGenre(Genre.MALE)
            }
            female.setOnClickListener {
                selectGenre(Genre.FEMALE)
            }
            other.setOnClickListener {
                selectGenre(Genre.OTHER)
            }
            continueBtn.setOnClickListener {
                val directions =
                    SelectGenderFragmentDirections.actionSelectGenderFragmentToPickReadingTimeFragment()
                navigateFragment(directions)
            }
        }
    }

    private fun selectGenre(genre: Genre) {
        when (genre) {
            Genre.MALE -> {
                changeGenderSelection(isMale = true)
            }
            Genre.FEMALE -> {
                changeGenderSelection(isFemale = true)
            }
            Genre.OTHER -> {
                changeGenderSelection(isOther = true)
            }
        }
    }

    private fun changeGenderSelection(
        isMale: Boolean = false,
        isFemale: Boolean = false,
        isOther: Boolean = false
    ) {
        with(binding) {
            male.isSelected = isMale
            female.isSelected = isFemale
            other.isSelected = isOther

            if (!continueBtn.isEnabled) {
                continueBtn.isEnabled = true
            }
        }
    }
}