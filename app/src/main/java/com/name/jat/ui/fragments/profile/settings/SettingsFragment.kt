package com.name.jat.ui.fragments.profile.settings

import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.name.jat.BuildConfig
import com.name.jat.R
import com.name.jat.appbase.FragmentBaseNCMVVM
import com.name.jat.appbase.utils.viewBinding
import com.name.jat.databinding.FragmentSettingsBinding
import com.name.jat.ui.fragments.search.mainsearch.QuestionDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : FragmentBaseNCMVVM<SettingsViewModel, FragmentSettingsBinding>() {
    override val viewModel: SettingsViewModel by viewModel()
    override val binding: FragmentSettingsBinding by viewBinding()
    private val args: SettingsFragmentArgs by navArgs()
    override fun onView() {
        with(binding) {
            if (args.isNotRegistered) {
                signOutOrIn.text = resources.getString(R.string.sign_in)
                signOutOrIn.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_sign_in,
                    0,
                    R.drawable.ic_arrow_stroke_gray,
                    0
                )
            } else {
                signOutOrIn.text = resources.getString(R.string.sign_out)
                signOutOrIn.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_sign_out,
                    0,
                    R.drawable.ic_arrow_stroke_gray,
                    0
                )
            }

            switchBtn.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    Log.i("switchState", "Checked")
                } else {
                    Log.i("switchState", "Unchecked")
                }
            }
            version.text =
                context?.resources?.getString(R.string.version)?.format(BuildConfig.VERSION_NAME)
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
            signOutOrIn.setOnClickListener {
                val questionDialog =
                    QuestionDialog(onClearClick = {
                        Toast.makeText(context, "Sign Out", Toast.LENGTH_SHORT).show()
                    })
                questionDialog.setQuestionText(resources.getString(R.string.are_you_shure_you_want_to_sign_out))
                questionDialog.setPositiveBtnText(resources.getString(R.string.sign_out))
                questionDialog.show(
                    childFragmentManager,
                    context?.resources?.getString(R.string.clear_dialog)
                )
            }
        }
    }

}