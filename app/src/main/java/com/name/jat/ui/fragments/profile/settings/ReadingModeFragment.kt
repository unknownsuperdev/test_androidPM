package com.name.jat.ui.fragments.profile.settings

import com.name.jat.R
import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.databinding.FragmentReadingModeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReadingModeFragment : FragmentBaseNCMVVM<ReadingModeViewModel, FragmentReadingModeBinding>() {
    override val viewModel: ReadingModeViewModel by viewModel()
    override val binding: FragmentReadingModeBinding by viewBinding()

    override fun onView() {
        with(binding) {
            viewModel.getReadingMode()
            readingModeRadioGroup.setOnCheckedChangeListener { group, checkedId ->
                readingModeRadioGroup.jumpDrawablesToCurrentState()
                when (checkedId) {
                    R.id.teenager -> viewModel.changeReadingMode(true)
                    R.id.adult -> viewModel.changeReadingMode(false)
                }
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.isTeenager) {
            it?.let {
                if (it)
                    binding.teenager.isChecked = it
                else binding.adult.isChecked = !it
            }
        }
    }

    override fun onViewClick() {
        with(binding) {
            mainToolbar.setOnBackBtnClickListener {
                popBackStack()
            }
        }
    }

}