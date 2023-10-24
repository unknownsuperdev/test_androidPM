package com.name.jat.ui.fragments.profile.editprofile

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.name.jat.R
import com.name.jat.databinding.BottomDialogEditProfilePhotoBinding
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.Locale.getDefault

class EditProfilePhotoBottomDialog : BottomSheetDialogFragment() {
    private lateinit var binding: BottomDialogEditProfilePhotoBinding
    var fileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomDialogEditProfilePhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            removePhoto.setOnClickListener {
                activity?.supportFragmentManager?.setFragmentResult(
                    REMOVE_IMAGE,
                    bundleOf(IS_REMOVE_IMAGE to true)
                )
                dismiss()
            }
            fileUri = getOutputMediaFileUri()
            cameraTakePhoto.setOnClickListener {
                if (checkPermissionForCamera()) {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
                    resultCameraLauncher.launch(cameraIntent)
                } else requestPermissionForCamera()
            }

            galleryChoosePhoto.setOnClickListener {
                if (checkPermissionForStorageOrGallery()) {
                    launchGalleryLauncher()
                } else requestPermissionForStorageOrGallery()
            }
        }
    }

    private fun checkPermissionForStorageOrGallery() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_DENIED &&
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_DENIED

    private fun requestPermissionForStorageOrGallery() {
        requestGalleryPermission.launch(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
        )
    }

    private var requestGalleryPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var grantedPermissionCount = 0
            permissions.entries.forEach {
                val isGranted = it.value
                if (isGranted) {
                    grantedPermissionCount++
                }
            }
            if (grantedPermissionCount == 2)
                launchGalleryLauncher()
            else Toast.makeText(requireContext(), "Denied Gallery", Toast.LENGTH_SHORT).show()
        }

    private fun checkPermissionForCamera() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) != PackageManager.PERMISSION_DENIED

    private fun requestPermissionForCamera() {
        requestCameraPermission.launch(Manifest.permission.CAMERA)
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA),
            REQUEST_TAKE_PHOTO_FROM_CAMERA
        )
    }

    private var requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
                resultCameraLauncher.launch(cameraIntent)
            } else Toast.makeText(requireContext(), "Denied Camera", Toast.LENGTH_SHORT).show()
        }

    private var resultCameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                fileUri?.let { setFragmentResult(it) }
                dismiss()
            }
        }

    private fun launchGalleryLauncher() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            EXTERNAL_CONTENT_URI
        )
        resultGalleryLauncher.launch(galleryIntent)
    }

    private var resultGalleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val uriFile = result.data?.data?.let { getFileFromContentUri(it, true) }
            uriFile?.let { setFragmentResult(Uri.fromFile(it)) }
            dismiss()
        }

    private fun setFragmentResult(uri: Uri) {
        activity?.supportFragmentManager?.setFragmentResult(
            START_CROP_IMAGE,
            bundleOf(IMAGE_URI to uri.toString())
        )
    }

    private fun getFileFromContentUri(contentUri: Uri, uniqueName: Boolean): File {
        val fileExtension = getFileExtension(contentUri) ?: ""
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", getDefault()).format(Date())
        val fileName = ("temp_file_" + if (uniqueName) timeStamp else "") + ".$fileExtension"
        val tempFile = File(context?.cacheDir, fileName)
        tempFile.createNewFile()
        var oStream: FileOutputStream? = null
        var inputStream: InputStream? = null

        try {
            oStream = FileOutputStream(tempFile)
            inputStream = context?.contentResolver?.openInputStream(contentUri)
            copy(inputStream, oStream)
            oStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
            oStream?.close()
        }
        return tempFile
    }

    private fun getFileExtension(uri: Uri): String? =
        if (uri.scheme == ContentResolver.SCHEME_CONTENT)
            MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(context?.contentResolver?.getType(uri))
        else uri.path?.let {
            MimeTypeMap.getFileExtensionFromUrl(
                Uri.fromFile(File(it)).toString()
            )
        }

    @Throws(IOException::class)
    private fun copy(source: InputStream?, target: OutputStream) {
        val buf = ByteArray(8192)
        var length: Int
        source?.let {
            while (it.read(buf).also { length = it } > 0) {
                target.write(buf, 0, length)
            }
        }
    }

    private fun getOutputMediaFileUri(): Uri? {
        return getOutputMediaFile()?.let {
            FileProvider.getUriForFile(
                requireContext(),
                requireContext().applicationContext?.packageName.toString() + ".provider",
                it
            )
        }
    }

    private fun getOutputMediaFile(): File? {
        val mediaStorageDir = File(
            context?.getExternalFilesDir(null)?.absolutePath,
            IMAGE_DIRECTORY_NAME
        )
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(
                    IMAGE_DIRECTORY_NAME, ("Oops! Failed create "
                            + IMAGE_DIRECTORY_NAME) + " directory"
                )
                return null
            }
        }
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", getDefault()).format(Date())
        return File(
            mediaStorageDir.path + File.separator
                .toString() + "IMG_" + timeStamp + ".jpg"
        )
    }


    companion object {
        const val REQUEST_TAKE_PHOTO_FROM_CAMERA = 0
        const val IMAGE_DIRECTORY_NAME: String = "Image from Camera"
        const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1
        const val START_CROP_IMAGE = "CROP_IMAGE"
        const val IMAGE_URI = "IMAGE_URI"
        const val REMOVE_IMAGE = "REMOVE_IMAGE"
        const val IS_REMOVE_IMAGE = "IS_REMOVE_IMAGE"
    }
}