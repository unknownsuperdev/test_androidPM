package ru.tripster.guide.extensions

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog
import ru.tripster.guide.Constants
import ru.tripster.guide.R
import ru.tripster.guide.databinding.BottomSheetSupportBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import ru.tripster.guide.BuildConfig

fun Fragment.contactSupportBottomSheet(
    context: Context,
    orderId: Int,
    isFromProfile: Boolean,
    currentProgress: Float = 1.0f,
    onNavigated: (a: () -> Unit) -> Unit,
    onCloseClicked: () -> Unit
) {
    val appVersionName = BuildConfig.VERSION_NAME
    val appVersionCode = BuildConfig.VERSION_CODE

    val dialog = BottomSheetDialog(context)

    val dialogBinding =
        BottomSheetSupportBinding.inflate(LayoutInflater.from(context))

    when (currentProgress) {
        in 0.0f..0.75f -> {
            dialog.window?.decorView?.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        in 0.75f..1.0f -> {
            dialog.setFullScreenAndLightStatusBarDialog()
        }
    }

    dialog.setOnDismissListener {
        onCloseClicked()
    }

    with(dialogBinding) {
        close.setOnClickListener {
            onCloseClicked()
            dialog.dismiss()
        }

        supportInfoTv.text = if (!isFromProfile)
            context.getString(
                R.string.support_info_text_with_order_number,
                orderId
            ) else context.getString(
            R.string.support_info_text,
            appVersionName, appVersionCode
        )

        phone.setOnClickListener {
            buttonsAreClickable(false, dialogBinding)
            onNavigated {
                buttonsAreClickable(true, dialogBinding)
            }
            context.let { context -> Constants.SUPPORT_PHONE.phoneCall(context) }
        }

        mail.setOnClickListener {
            buttonsAreClickable(false, dialogBinding)
            onNavigated{
                buttonsAreClickable(true, dialogBinding)
            }
            startActivity(Constants.SUPPORT_EMAIL.openMail())
        }

        telegram.setOnClickListener {
            buttonsAreClickable(false, dialogBinding)
            onNavigated{
                buttonsAreClickable(true, dialogBinding)
            }
            if (isPackageInstalled(Constants.TELEGRAM_PACKAGE_NAME, context.packageManager)) {
                val intent =
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("tg://resolve?domain=${Constants.SUPPORT_TELEGRAM_NICK}")
                    )
                startActivity(intent)
            } else {
                startActivity(Constants.SUPPORT_TELEGRAM_LINK.openLinkInBrowser())
            }
        }

        vkSupport.setOnClickListener {
            buttonsAreClickable(false, dialogBinding)
            onNavigated {
                buttonsAreClickable(true, dialogBinding)
            }
            if (isPackageInstalled(Constants.VK_PACKAGE_NAME, context.packageManager)) {
                val intent =
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("vkontakte://profile/-51519887")
                    )
                startActivity(intent)
            } else {
                startActivity(Constants.SUPPORT_VK.openLinkInBrowser())
            }
        }
    }

    dialog.let {
        it.setContentView(dialogBinding.root)
        it.show()
    }

}

private fun buttonsAreClickable(
    isClickable: Boolean,
    binding: BottomSheetSupportBinding
) {
    with(binding) {
        mail.isClickable = isClickable
        telegram.isClickable = isClickable
        vkSupport.isClickable = isClickable
    }
}

private fun isPackageInstalled(packageName: String, packageManager: PackageManager?): Boolean {
    return try {
        packageManager?.getPackageInfo(packageName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}
