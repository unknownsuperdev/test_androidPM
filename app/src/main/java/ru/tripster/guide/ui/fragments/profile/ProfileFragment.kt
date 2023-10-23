package ru.tripster.guide.ui.fragments.profile

import android.app.AlertDialog
import android.view.View
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.tripster.guide.R
import ru.tripster.guide.Screen
import ru.tripster.guide.appbase.FragmentBaseNCMVVM
import ru.tripster.guide.appbase.utils.bottomNavBarVisibility
import ru.tripster.guide.appbase.utils.viewBinding
import ru.tripster.guide.databinding.FragmentProfileBinding
import ru.tripster.guide.extensions.*
import ru.tripster.guide.ui.MainActivity

class ProfileFragment : FragmentBaseNCMVVM<ProfileViewModel, FragmentProfileBinding>() {

    override val viewModel: ProfileViewModel by viewModel()
    override val binding: FragmentProfileBinding by viewBinding()

    override fun onView() {
        activity?.setLightStatusBar()
        activity?.bottomNavBarVisibility(true)
        if (context?.isOnline() == true) viewModel.getUserInfo() else offlineError()
    }

    override fun onViewClick() {
        with(binding) {
            issue.update.setOnClickListener {
                when {
                    context?.isOnline() == true -> {
                        viewModel.getUserInfo()
                        profileNameTv.visibility = View.VISIBLE
                        profileMailTv.visibility = View.VISIBLE
                        profileIv.visibility = View.VISIBLE
                        supportTv.visibility = View.VISIBLE
                        logoutTv.visibility = View.VISIBLE
                        errorContainer.makeVisibleGone()
                    }

                    else -> {
                        offlineError()
                    }
                }
            }

            supportTv.setOnClickListener {
                context?.let { context ->
                    this@ProfileFragment.contactSupportBottomSheet(
                        context, 0, isFromProfile = true, onNavigated = {
                            lifecycleScope.launch {
                                delay(1500)
                                it()
                            }
                        }
                    ) {}
                }
            }

            logoutTv.setOnClickListener {
                showLogOutDialog()
            }

            deleteAccount.setOnClickListener {
                navigateFragment(
                    ProfileFragmentDirections.actionProfileFragmentToDeleteAccountFragment()
                )
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.profileInfo) {
            viewModel.details = it

            context?.let { context -> viewModel.menuItemClicked(context) }

            with(binding) {
                profileNameTv.text = getString(R.string.user_name, it.firstName, it.lastName)
                profileMailTv.text = it.email
                Glide.with(requireContext()).load(it.avatarImage).centerCrop()
                    .placeholder(R.drawable.ic_account).into(profileIv)
            }
        }
        onEach(viewModel.errorUserInfo) {
            callError()
        }
        onEach(viewModel.userInfo) { userInfo ->
            userInfo?.let {
                (activity as MainActivity).navController.navigate(
                    Screen.Login.show(
                        0, it, false
                    )
                )
//                navigateFragmentDeepLink(
//                    Screen.Login.show(
//                        0, it, false
//                        )
//                )
//                navigateFragment(
//                    ProfileFragmentDirections.actionProfileFragmentToLoginFragment(
//                        0,
//                        it, false
//                    )
//                )
            }
        }
    }

    private fun callError() {
        with(binding) {
            with(binding) {
                profileNameTv.visibility = View.GONE
                profileMailTv.visibility = View.GONE
                profileIv.visibility = View.GONE
                supportTv.visibility = View.GONE
                logoutTv.visibility = View.GONE
                errorContainer.makeVisible()
            }
            with(issue) {
                containerIssue.setPadding(0, 0, 0, 0)
                title.text = context?.getStringRes(R.string.call_error_title)
                message.text = context?.getStringRes(R.string.call_error_message)
            }
        }
    }

    private fun offlineError() {
        with(binding) {
            profileNameTv.visibility = View.GONE
            profileMailTv.visibility = View.GONE
            profileIv.visibility = View.GONE
            supportTv.visibility = View.GONE
            logoutTv.visibility = View.GONE
            with(issue) {
                containerIssue.setPadding(0, 0, 0, 0)
                errorContainer.makeVisible()
                title.text = context?.getStringRes(R.string.no_internet_title)
                message.text = context?.getStringRes(R.string.no_internet_message)
            }
        }
    }

    private fun showLogOutDialog() {
        val builder = AlertDialog.Builder(context, R.style.Background_transparent).create()
        val view = layoutInflater.inflate(R.layout.dialog_log_out, null)
        val positiveBtn = view.findViewById(R.id.positive) as TextView
        val negativeBtn = view.findViewById(R.id.negative) as TextView
        with(builder) {
            setView(view)
            positiveBtn.setOnClickListener {
                viewModel.logOut()
                dismiss()
            }
            negativeBtn.setOnClickListener {
                dismiss()
            }
            setCanceledOnTouchOutside(false)
            show()
        }
    }
}