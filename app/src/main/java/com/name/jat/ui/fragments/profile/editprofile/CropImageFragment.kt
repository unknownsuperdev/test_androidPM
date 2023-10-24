package com.name.jat.ui.fragments.profile.editprofile

import android.view.WindowManager
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.fragment.navArgs
import com.canhub.cropper.CropImageView
import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.appbase.viewmodel.BaseViewModel
import com.name.jat.databinding.FragmentCropImageBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CropImageFragment :
    FragmentBaseNCMVVM<BaseViewModel, FragmentCropImageBinding>() {
    override val viewModel: BaseViewModel by viewModel()
    override val binding: FragmentCropImageBinding by viewBinding()
    private val args: CropImageFragmentArgs by navArgs()

    override fun onView() {
        with(binding) {
            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            cropImageView.run {
                cropShape = CropImageView.CropShape.OVAL
                setAspectRatio(1, 1)
                guidelines = CropImageView.Guidelines.OFF
                setImageUriAsync(args.imageUri.toUri())
            }
            cropImageView.setOnCropImageCompleteListener { view, result ->
                if (result.isSuccessful) {
                    setFragmentResult(result.uri.toString())
                }
                popBackStack()
            }
        }
    }

    override fun onViewClick() {
        with(binding) {
            toolbar.setNavigationOnClickListener {
                popBackStack()
            }
            save.setOnClickListener {
                cropImageView.saveCroppedImageAsync(args.imageUri.toUri())
            }
        }
    }

    private fun setFragmentResult(uri: String) {
        activity?.supportFragmentManager?.setFragmentResult(
            FROM_CROP_IMAGE_FRAGMENT,
            bundleOf(CROPPED_IMAGE_URI to uri)
        )
    }

    companion object {
        const val CROPPED_IMAGE_URI = "CROPPED_IMAGE_URI"
        const val FROM_CROP_IMAGE_FRAGMENT = "FROM_CROP_IMAGE_FRAGMENT"
    }
}