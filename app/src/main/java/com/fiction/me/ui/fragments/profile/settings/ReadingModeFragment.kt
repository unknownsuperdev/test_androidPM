package com.fiction.me.ui.fragments.profile.settings

import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentReadingModeBinding
import com.fiction.me.utils.Events.Companion.ADULT
import com.fiction.me.utils.Events.Companion.MODE
import com.fiction.me.utils.Events.Companion.READING_MODE_SCREEN_SHOWN
import com.fiction.me.utils.Events.Companion.TEENAGER
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReadingModeFragment : FragmentBaseNCMVVM<ReadingModeViewModel, FragmentReadingModeBinding>() {
    override val viewModel: ReadingModeViewModel by viewModel()
    override val binding: FragmentReadingModeBinding by viewBinding()

    override fun onView() {
        with(binding) {
            viewModel.getReadingMode()
            mainToolbar.setBackBtnIcon(R.drawable.ic_back)
            readingModeRadioGroup.setOnCheckedChangeListener { group, checkedId ->
                readingModeRadioGroup.jumpDrawablesToCurrentState()
                when (checkedId) {
                    R.id.teenager -> viewModel.changeReadingMode(false)
                    R.id.adult -> viewModel.changeReadingMode(true)
                }
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.isAdult) {
            it?.let {
                if (it) {
                    binding.adult.isChecked = it
                    viewModel.trackEvents(
                        READING_MODE_SCREEN_SHOWN,
                        hashMapOf(MODE to ADULT)
                    )
                } else {
                    binding.teenager.isChecked = !it
                    viewModel.trackEvents(
                        READING_MODE_SCREEN_SHOWN,
                        hashMapOf(MODE to TEENAGER)
                    )
                }
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