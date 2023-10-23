package ru.tripster.guide.ui.fragments.splashScreen

import android.annotation.SuppressLint
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.animation.AlphaAnimation
import androidx.core.content.ContextCompat
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.tripster.guide.R
import ru.tripster.guide.enums.DeepLinkPrefix
import ru.tripster.guide.appbase.FragmentBaseNCMVVM
import ru.tripster.guide.appbase.utils.viewBinding
import ru.tripster.guide.databinding.FragmentSplashScreenBinding
import ru.tripster.guide.extensions.setStatusBarColor
import ru.tripster.guide.extensions.setStatusBarWhiteText
import ru.tripster.guide.ui.MainActivity
import ru.tripster.guide.extensions.check24Hours
import ru.tripster.guide.ui.MainActivity.Companion.DEEP_LINK_ORDER_ID
import ru.tripster.guide.ui.MainActivity.Companion.DEEP_LINK_PREFIX

class SplashScreenFragment :
    FragmentBaseNCMVVM<SplashScreenViewModel, FragmentSplashScreenBinding>() {
    override val viewModel: SplashScreenViewModel by viewModel()
    override val binding: FragmentSplashScreenBinding by viewBinding()

    override fun onAttach() {
        super.onAttach()
        (activity as MainActivity).setStatusBarWhiteText()

        context?.let { context ->
            (activity as MainActivity).setStatusBarColor(
                ContextCompat.getColor(
                    context,
                    R.color.gray_20
                )
            )
        }
    }

    @SuppressLint("HardwareIds")
    override fun onView() {
        val deviceId =
            Settings.Secure.getString(context?.contentResolver, Settings.Secure.ANDROID_ID)
        Handler(Looper.getMainLooper()).postDelayed({
            context?.let { viewModel.dataStore(deviceId, it) }
        }, 2000)
    }

    override fun onEach() {
        onEach(viewModel.token) {
            val deeplinkPrefix = arguments?.getString(DEEP_LINK_PREFIX) ?: ""
            val id = arguments?.getInt(DEEP_LINK_ORDER_ID) ?: 0

            if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) && (viewModel.lastRemindLaterTime.check24Hours() || viewModel.lastRemindLaterTime.isEmpty()) && !viewModel.notificationStateIsSelected) {
                navigateFragment(
                    SplashScreenFragmentDirections.actionSplashScreenFragmentToNotificationsFragment(
                        !it.isNullOrEmpty()
                    ).setDeeplinkPrefix(deeplinkPrefix)
                )
            } else {
                if (!it.isNullOrEmpty()) {
                    navigation(deeplinkPrefix, id)
                } else {

                    navigateFragment(
                        SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginFragment(
                            id,
                            "",
                            false
                        ).setDeeplinkPrefix(deeplinkPrefix)
                    )
                }
            }
        }
    }

    private fun splashAnim() {
        with(binding.root) {
            val animation = AlphaAnimation(1f, 0.0f)
            animation.duration = 200
            startAnimation(animation)
        }
    }

    override fun onPause() {
        super.onPause()
        splashAnim()
    }

    private fun navigation(deeplinkPrefix: String, orderId: Int) {

        when (deeplinkPrefix) {
            DeepLinkPrefix.EXPERIENCE_ORDER.prefix ->
                navigateFragment(
                    SplashScreenFragmentDirections.actionSplashScreenFragmentToMainFragment()
                        .setDeepLinkOrderId(orderId)
                )

            DeepLinkPrefix.DELETE_ACCOUNT.prefix ->
                navigateFragment(
                    SplashScreenFragmentDirections.actionSplashScreenFragmentToProfileFragment()
                )

            else -> navigateFragment(
                SplashScreenFragmentDirections.actionSplashScreenFragmentToMainFragment()
            )
        }
    }
}