package com.fiction.me.ui.fragments.purchase

import android.content.Intent
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.appbase.viewmodel.BaseViewModel
import com.fiction.me.databinding.FragmentWriteToSupportBinding
import com.fiction.me.extensions.onTextChanged
import com.fiction.me.utils.Constants
import com.fiction.me.utils.Events.Companion.WRITE_TO_SUPPORT_SENT
import org.koin.androidx.viewmodel.ext.android.viewModel

class WriteToSupportFragment :
    FragmentBaseNCMVVM<WriteToSupportViewModel, FragmentWriteToSupportBinding>() {

    override val viewModel: WriteToSupportViewModel by viewModel()
    override val binding: FragmentWriteToSupportBinding by viewBinding()
    private var isMailInvalid = false
    override fun onView() {
        with(binding) {
            toMail.text = Constants.HELP_AND_SUPPORT_MAIL
            message.onTextChanged {
                changeStateSendButton()
            }
            subject.onTextChanged {
                changeStateSendButton()
            }
        }
    }

    override fun onViewClick() {
        with(binding) {
            closeBtn.setOnClickListener {
                popBackStack()
            }
            send.setOnClickListener {
                val to: String = toMail.text.toString().trim()
                val subject: String = subject.text.toString()
                val message: String = message.text.toString()

                val email = Intent(Intent.ACTION_SEND)
                email.run {
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
                    putExtra(Intent.EXTRA_SUBJECT, subject)
                    putExtra(Intent.EXTRA_TEXT, message)
                    type = "text/plain"
                }
                viewModel.trackEvents(WRITE_TO_SUPPORT_SENT)
                startActivity(Intent.createChooser(email, "Choose an Email client :"))
            }
        }
    }

    private fun changeStateSendButton() {
        with(binding) {
            send.isEnabled =
                subject.text?.isNotEmpty() == true &&
                        message.text?.isNotEmpty() == true &&
                        toMail.text?.isNotEmpty() == true && !isMailInvalid
        }
    }

}

