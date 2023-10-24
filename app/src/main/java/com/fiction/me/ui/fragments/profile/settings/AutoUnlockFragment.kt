package com.fiction.me.ui.fragments.profile.settings

import android.util.Log
import com.analytics.api.Events.USER_PROPERTY__CHAPTERS_AUTO_UNLOCK
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentAutoUnlockBinding
import com.fiction.me.utils.Events
import org.koin.androidx.viewmodel.ext.android.viewModel

class AutoUnlockFragment : FragmentBaseNCMVVM<AutoUnlockViewModel, FragmentAutoUnlockBinding>() {
    override val viewModel: AutoUnlockViewModel by viewModel()
    override val binding: FragmentAutoUnlockBinding by viewBinding()

    override fun onView() {
        with(binding) {
            viewModel.getLockState()
            mainToolbar.setBackBtnIcon(R.drawable.ic_back)
        }
    }

    override fun onEach() {
        onEach(viewModel.isTurnOnAutoUnlock) {
            it?.let {
                if (it) {
                    binding.turnOn.isChecked = it
                    viewModel.setUserPropertyEvent(mapOf(USER_PROPERTY__CHAPTERS_AUTO_UNLOCK to Events.ON))
                    viewModel.trackEvents(
                        Events.CHAPTERS_AUTO_UNLOCK_SCREEN_SHOWN,
                        hashMapOf(Events.STATE to Events.ON)
                    )

                } else {
                    binding.turnOffAutoUnlock.isChecked = !it
                    viewModel.setUserPropertyEvent(mapOf(USER_PROPERTY__CHAPTERS_AUTO_UNLOCK to Events.OFF))
                    viewModel.trackEvents(
                        Events.CHAPTERS_AUTO_UNLOCK_SCREEN_SHOWN,
                        hashMapOf(Events.STATE to Events.OFF)
                    )
                }
                Log.i("isTurnOn", "onEach: $it")
            }
        }
    }

    override fun onViewClick() {
        with(binding) {

            chaptersUnlockRadioGroup.setOnCheckedChangeListener { group, checkedId ->
                chaptersUnlockRadioGroup.jumpDrawablesToCurrentState()
                when (checkedId) {
                    R.id.turnOn -> viewModel.changeAutoUnlockMode(true)
                    R.id.turnOffAutoUnlock -> viewModel.changeAutoUnlockMode(false)
                }
            }

            mainToolbar.setOnBackBtnClickListener {
                popBackStack()
            }
        }
    }

}