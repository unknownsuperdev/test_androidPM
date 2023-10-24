package com.name.jat.ui.fragments.profile.settings

import android.util.Log
import com.name.jat.R
import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.databinding.FragmentAutoUnlockBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AutoUnlockFragment : FragmentBaseNCMVVM<AutoUnlockViewModel, FragmentAutoUnlockBinding>() {
    override val viewModel: AutoUnlockViewModel by viewModel()
    override val binding: FragmentAutoUnlockBinding by viewBinding()

    override fun onView() {
        with(binding) {
            viewModel.getLockState()
            chaptersUnlockRadioGroup.setOnCheckedChangeListener { group, checkedId ->
                chaptersUnlockRadioGroup.jumpDrawablesToCurrentState()
                when (checkedId) {
                    R.id.turnOn -> viewModel.changeAutoUnlockMode(true)
                    R.id.turnOffAutoUnlock -> viewModel.changeAutoUnlockMode(false)
                }
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.isTurnOnAutoUnlock) {
            it?.let {
                if (it)
                    binding.turnOn.isChecked = it
                else binding.turnOffAutoUnlock.isChecked = !it
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