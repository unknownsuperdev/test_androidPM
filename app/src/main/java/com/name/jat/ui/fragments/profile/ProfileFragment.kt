package com.name.jat.ui.fragments.profile

import android.content.Intent
import android.net.Uri
import com.bumptech.glide.Glide
import com.name.jat.R
import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.databinding.FragmentProfileBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : FragmentBaseNCMVVM<ProfileViewModel, FragmentProfileBinding>() {
    override val viewModel: ProfileViewModel by viewModel()
    override val binding: FragmentProfileBinding by viewBinding()

    override fun onView() {
        with(binding) {
            collapseToolbar.run {
                title = resources.getString(R.string.profile)
                setExpandedTitleTextAppearance(R.style.Headings_h1)
                setCollapsedTitleTextAppearance(R.style.Title_Medium_primary_white_18)
            }
            viewModel.run {
                getUserToken()
                getProfileInfo()
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.userToken) {
            binding.lastItem.text =
                if (it.isNullOrEmpty()) resources.getString(R.string.sign_in) else resources.getString(
                    R.string.edit
                )
        }

        onEach(viewModel.profileInfo) { profileInfo ->
            with(binding) {
                userName.text = profileInfo?.name
                balance.text = profileInfo?.balance.toString()
                context?.let { context ->
                    profileInfo?.avatar.let {
                        Glide.with(context)
                            .load(if (it.isNullOrEmpty()) R.drawable.ic_guest_user else it)
                            .circleCrop()
                            .into(userImage)
                    }
                }
            }
        }
    }

    override fun onViewClick() {
        with(binding) {
            val isNotRegistered = viewModel.userToken.value.isNullOrEmpty()
            settings.setOnClickListener {
                val directions = ProfileFragmentDirections.actionProfileFragmentToSettingsFragment(
                    isNotRegistered
                )
                navigateFragment(directions)
            }
            purchaseHistory.setOnClickListener {
                val directions =
                    ProfileFragmentDirections.actionProfileFragmentToPurchaseHistoryFragment()
                navigateFragment(directions)
            }
            helpAndSupport.setOnClickListener {
                sendEmail()
            }
            lastItem.setOnClickListener {
                val directions =
                    ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment()
                navigateFragment(directions)
            }
        }
    }

    private fun sendEmail() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto: ")
        startActivity(intent)
    }

}