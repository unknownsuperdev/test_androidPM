package ru.tripster.guide.ui.fragments.notifications

import android.Manifest
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.tripster.guide.appbase.FragmentBaseNCMVVM
import ru.tripster.guide.appbase.utils.viewBinding
import ru.tripster.guide.databinding.FragmentNotificationsBinding
import ru.tripster.guide.enums.DeepLinkPrefix

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class NotificationsFragment :
    FragmentBaseNCMVVM<NotificationsViewModel, FragmentNotificationsBinding>() {

    override val viewModel: NotificationsViewModel by viewModel()
    override val binding: FragmentNotificationsBinding by viewBinding()
    private val args: NotificationsFragmentArgs by navArgs()

    override fun onViewClick() {
        with(binding) {
            turnOnNotifications.setOnClickListener {
                askNotificationPermission()
            }

            remindLater.setOnClickListener {
                viewModel.setRemindLaterTime()
                navigationLogic()
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) {
        viewModel.setNotificationState()
        navigationLogic()
    }

    private fun navigationLogic() {
        if (!args.isLoggedIn) navigateFragment(
            NotificationsFragmentDirections.actionNotificationsFragmentToLoginFragment(
                args.deepLinkOrderId,
                "",
                false
            ).setDeeplinkPrefix(args.deeplinkPrefix)
        ) else navigation(args.deeplinkPrefix, args.deepLinkOrderId)
    }

    private fun askNotificationPermission() {
        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    private fun navigation(deeplinkPrefix: String, orderId: Int) {

        when (deeplinkPrefix) {
            DeepLinkPrefix.EXPERIENCE_ORDER.prefix ->
                navigateFragment(
                    NotificationsFragmentDirections.actionNotificationsFragmentToMainFragment()
                        .setDeepLinkOrderId(orderId)
                )

            DeepLinkPrefix.DELETE_ACCOUNT.prefix ->
                navigateFragment(
                    NotificationsFragmentDirections.actionNotificationsFragmentToProfileFragment()
                )

            else -> navigateFragment(
                NotificationsFragmentDirections.actionNotificationsFragmentToMainFragment()
            )
        }
    }

}