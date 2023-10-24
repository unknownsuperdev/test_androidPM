package com.fiction.me.ui.fragments.profile

import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import com.fiction.domain.model.enums.AuthType
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentProfileBinding
import com.fiction.me.utils.Constants.Companion.HELP_AND_SUPPORT_MAIL
import com.fiction.me.utils.Events.Companion.HELP_AND_SUPPORT_CLICKED
import com.fiction.me.utils.Events.Companion.PROFILE_SCREEN_SHOWN
import com.fiction.me.utils.Events.Companion.TOP_UP_BUTTON_CLICKED
import com.fiction.me.utils.InternalDeepLink
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : FragmentBaseNCMVVM<ProfileViewModel, FragmentProfileBinding>() {
    override val viewModel: ProfileViewModel by viewModel()
    override val binding: FragmentProfileBinding by viewBinding()

    override fun onView() {

        viewModel.run {
            trackEvents(PROFILE_SCREEN_SHOWN)
            getProfileInfo()
            cacheTariffs()
        }
    }

    override fun onEach() {
        onEach(viewModel.profileInfo) { profileInfo ->
            with(binding) {
                balance.text = profileInfo?.balance.toString()
            }
        }

        onEach(viewModel.authType) {
            binding.signInOrEdit.text = when (it) {
                AuthType.GUEST -> resources.getString(R.string.sign_in)
                else -> ""//resources.getString(R.string.edit)
            }
        }
    }

    override fun onViewClick() {
        with(binding) {
            settings.setOnClickListener {
                val directions = ProfileFragmentDirections.actionProfileFragmentToSettingsFragment()
                navigateFragment(directions)
            }
            /* purchaseHistory.setOnClickListener {
                 val directions =
                     ProfileFragmentDirections.actionProfileFragmentToPurchaseHistoryFragment()
                 navigateFragment(directions)
             }*/
            helpAndSupport.setOnClickListener {
                viewModel.trackEvents(HELP_AND_SUPPORT_CLICKED)
                sendEmail()
            }
            topUp.setOnClickListener {
                viewModel.trackEvents(TOP_UP_BUTTON_CLICKED)
                val deeplink = InternalDeepLink.makeCustomDeepLinkForStore(true).toUri()
                navigateFragment(deeplink)
            }
            signInOrEdit.setOnClickListener {
                if (viewModel.authType.value == AuthType.GUEST)
                    navigateFragment(R.id.nav_auth)
            }
        }
    }

    private fun sendEmail() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto: $HELP_AND_SUPPORT_MAIL")
        startActivity(intent)
    }
}