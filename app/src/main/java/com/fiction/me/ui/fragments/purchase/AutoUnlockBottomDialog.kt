package com.fiction.me.ui.fragments.purchase

import android.os.Bundle
import com.analytics.api.Events.USER_PROPERTY__CHAPTERS_AUTO_UNLOCK
import com.fiction.me.R
import com.fiction.me.appbase.BottomSheetFragmentBaseMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.BottomDialogAutoUnlockBinding
import com.fiction.me.utils.Events.Companion.CHAPTERS_AUTO_UNLOCK_SWITCHED
import com.fiction.me.utils.Events.Companion.OFF
import com.fiction.me.utils.Events.Companion.ON
import com.fiction.me.utils.Events.Companion.STATE
import org.koin.androidx.viewmodel.ext.android.viewModel

class AutoUnlockBottomDialog :
    BottomSheetFragmentBaseMVVM<AutoUnlockStateViewModel, BottomDialogAutoUnlockBinding>() {
    override val viewModel: AutoUnlockStateViewModel by viewModel()
    override val binding: BottomDialogAutoUnlockBinding by viewBinding()
    var onAutoUnlockListener: (Boolean) -> Unit = {}
    var isTurnOn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onViewClick() {
        with(binding) {
            switchBtn.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    viewModel.run {
                        changeAutoUnlockMode(isChecked)
                        trackEvents(CHAPTERS_AUTO_UNLOCK_SWITCHED, hashMapOf(STATE to ON))
                        setUserPropertyEvent(mapOf(USER_PROPERTY__CHAPTERS_AUTO_UNLOCK to ON))
                    }
                }else  {
                    viewModel.trackEvents(CHAPTERS_AUTO_UNLOCK_SWITCHED, hashMapOf(STATE to OFF))
                    viewModel.setUserPropertyEvent(mapOf(USER_PROPERTY__CHAPTERS_AUTO_UNLOCK to OFF))
                }
            }
            closeBtn.setOnClickListener {
                dismiss()
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.isTurnOnAutoUnlock) {
            isTurnOn = true
            onAutoUnlockListener(isTurnOn)
            dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!isTurnOn)
            onAutoUnlockListener(isTurnOn)
    }
}
