package com.fiction.me.ui.fragments.profile.settings

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.fiction.domain.model.enums.AuthType
import com.fiction.me.BuildConfig
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentSettingsBinding
import com.fiction.me.ui.MainActivity
import com.fiction.me.ui.fragments.reader.custom_compose_dialog.ProgressDialog
import com.fiction.me.utils.Constants.Companion.PRIVACY_POLICY
import com.fiction.me.utils.Constants.Companion.TERMS_OF_USE
import com.fiction.me.utils.Events
import com.fiction.me.utils.Events.Companion.NOTIFICATION_SWITCHER_TOGGLED
import com.fiction.me.utils.Events.Companion.PRIVACY_POLICY_CLICKED
import com.fiction.me.utils.Events.Companion.SETTING_SCREEN_SHOWN
import com.fiction.me.utils.Events.Companion.TERMS_OF_USE_CLICKED
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : FragmentBaseNCMVVM<SettingsViewModel, FragmentSettingsBinding>() {
    override val viewModel: SettingsViewModel by viewModel()
    override val binding: FragmentSettingsBinding by viewBinding()
    private var clickCount = 0

    override fun onView() {
        with(binding) {
            viewModel.trackEvents(SETTING_SCREEN_SHOWN)
            mainToolbar.setBackBtnIcon(R.drawable.ic_back)
            switchBtn.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    viewModel.trackEvents(
                        NOTIFICATION_SWITCHER_TOGGLED,
                        hashMapOf(Events.STATE to Events.ON)
                    )
                } else {
                    viewModel.trackEvents(
                        NOTIFICATION_SWITCHER_TOGGLED,
                        hashMapOf(Events.STATE to Events.OFF)
                    )
                }

            }
            version.text =
                context?.resources?.getString(R.string.version)?.format(BuildConfig.VERSION_NAME)

            binding.progressDialog.setContent {
                ProgressDialog(viewModel.progressState)
            }
            binding.baseDialog.setContent {
                BaseDialog(viewModel.isSignOutDialogShow) { isConfirm ->
                    if (isConfirm) viewModel.signOut()
                    viewModel.isSignOutDialogShow.value = false
                }
            }
        }
    }

    override fun onViewClick() {
        with(binding) {
            mainToolbar.setOnBackBtnClickListener {
                popBackStack()
            }
            dataStorage.setOnClickListener {
                val directions =
                    SettingsFragmentDirections.actionSettingsFragmentToDataStorageFragment()
                navigateFragment(directions)
            }
            readingMode.setOnClickListener {
                val directions =
                    SettingsFragmentDirections.actionSettingsFragmentToReadingModeFragment()
                navigateFragment(directions)
            }
            chaptersAutoUnlock.setOnClickListener {
                val directions =
                    SettingsFragmentDirections.actionSettingsFragmentToAutoUnlockFragment()
                navigateFragment(directions)
            }
            termsOfUse.setOnClickListener {
                viewModel.trackEvents(TERMS_OF_USE_CLICKED)
                with(Intent(Intent.ACTION_VIEW)) {
                    data = Uri.parse(TERMS_OF_USE)
                    startActivity(this)
                }
            }
            privacyPolicy.setOnClickListener {
                viewModel.trackEvents(PRIVACY_POLICY_CLICKED)
                with(Intent(Intent.ACTION_VIEW)) {
                    data = Uri.parse(PRIVACY_POLICY)
                    startActivity(this)
                }
            }
            version.setOnClickListener {
                clickCount++
                when (clickCount) {
                    3 -> {
                        //viewModel.setTimer()
                        val clipboardManager =
                            context?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                        val clipData = ClipData.newPlainText("udid", viewModel.getUdid())
                        clipboardManager.setPrimaryClip(clipData)
                        Toast.makeText(
                            context,
                            "Your user id copied to clipboard",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                    /* 10 -> {
                         viewModel.setDataRestored()
                         navigateFragment(R.id.nav_onboarding)
                         clickCount = 0
                     }*/
                }
            }
            signOut.setOnClickListener {
                viewModel.isSignOutDialogShow.value = true
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.endedTime) {
            if (clickCount == 3) {
                val clipboardManager =
                    context?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("udid", it)
                clipboardManager.setPrimaryClip(clipData)
                Toast.makeText(context, "Your user id copied to clipboard", Toast.LENGTH_LONG)
                    .show()
                clickCount = 0
            } else clickCount++
        }
        onEach(viewModel.authType) {
            Log.i("AuthType", "onEach: ${it}")
            binding.signOut.isVisible = when (it) {
                AuthType.GUEST -> false
                else -> true
            }
        }
        onEach(viewModel.logout) {
            (activity as? MainActivity)?.setIsClearBackStackValue(true)
            findNavController().popBackStack(R.id.profileFragment, true)
        }
    }
}
