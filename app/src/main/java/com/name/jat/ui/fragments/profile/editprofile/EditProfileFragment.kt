package com.name.jat.ui.fragments.profile.editprofile

import android.view.WindowManager
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImageView
import com.name.jat.R
import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.databinding.FragmentEditProfileBinding
import com.name.jat.ui.fragments.profile.editprofile.CropImageFragment.Companion.CROPPED_IMAGE_URI
import com.name.jat.ui.fragments.profile.editprofile.CropImageFragment.Companion.FROM_CROP_IMAGE_FRAGMENT
import com.name.jat.ui.fragments.profile.editprofile.EditProfilePhotoBottomDialog.Companion.IMAGE_URI
import com.name.jat.ui.fragments.profile.editprofile.EditProfilePhotoBottomDialog.Companion.REMOVE_IMAGE
import com.name.jat.ui.fragments.profile.editprofile.EditProfilePhotoBottomDialog.Companion.START_CROP_IMAGE
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditProfileFragment :
    FragmentBaseNCMVVM<EditProfileViewModel, FragmentEditProfileBinding>(),
    CropImageView.OnCropImageCompleteListener {
    override val viewModel: EditProfileViewModel by viewModel()
    override val binding: FragmentEditProfileBinding by viewBinding()

    override fun onView() {
        getFragmentResultListener()
        with(binding) {
            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            viewModel.getProfileInfo()
            editUserName.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus)
                    editUserName.hint = viewModel.userName
                else editUserName.hint = resources.getString(R.string.username)
            }
            getFragmentResult()
        }
    }

    override fun onEach() {
        onEach(viewModel.profileInfo) { profileInfo ->
            with(binding) {
                editUserName.hint = profileInfo?.name
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

        onEach(viewModel.editedUserName) {
            popBackStack()
        }

        onEach(viewModel.updateProfile) {
            popBackStack()
        }

        onEach(viewModel.avatarRemoved) {
            context?.let {
                Glide.with(it)
                    .load(R.drawable.ic_guest_user)
                    .circleCrop()
                    .into(binding.userImage)
            }
        }
    }

    override fun onViewClick() {
        with(binding) {
            toolbar.setNavigationOnClickListener {
                popBackStack()
            }
            save.setOnClickListener {
                viewModel.updateProfile()
            }
            userImage.setOnClickListener {
                viewModel.changeUserNameIfEdited(editUserName.editText?.text.toString())
                EditProfilePhotoBottomDialog().show(
                    childFragmentManager,
                    EditProfilePhotoBottomDialog::class.java.simpleName
                )
            }
        }
    }

    private fun getFragmentResultListener() {
        activity?.supportFragmentManager?.setFragmentResultListener(
            START_CROP_IMAGE,
            viewLifecycleOwner
        ) { _, result ->
            val uri = result.getString(IMAGE_URI)
            val directions =
                EditProfileFragmentDirections.actionEditProfileFragmentToCropImageFragment(
                    uri ?: ""
                )
            navigateFragment(directions)
        }

        activity?.supportFragmentManager?.setFragmentResultListener(
            REMOVE_IMAGE,
            viewLifecycleOwner
        ) { _, result ->
            viewModel.removeAvatar()
        }
    }

    override fun onCropImageComplete(view: CropImageView?, result: CropImageView.CropResult?) {
        context?.let {
            Glide.with(it)
                .load(result?.uri ?: R.drawable.ic_guest_user)
                .circleCrop()
                .into(binding.userImage)
        }
    }

    private fun getFragmentResult() {
        activity?.supportFragmentManager?.setFragmentResultListener(
            FROM_CROP_IMAGE_FRAGMENT,
            viewLifecycleOwner
        ) { _, result ->
            val uri = result.getString(CROPPED_IMAGE_URI)
            uri?.let { viewModel.imageUri = it }
            context?.let {
                Glide.with(it)
                    .load(uri ?: R.drawable.ic_guest_user)
                    .circleCrop()
                    .into(binding.userImage)
            }
        }
    }

}