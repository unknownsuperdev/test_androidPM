package ru.tripster.guide.ui.fragments.chat

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.tripster.domain.model.confirmOrEdit.EditTravelerContactsOpen
import ru.tripster.guide.R
import ru.tripster.guide.appbase.bottomSheetDialog.BottomSheetDialogFragmentBaseMVVM
import ru.tripster.guide.appbase.utils.viewBinding
import ru.tripster.guide.databinding.BottomSheetContactsViewRulesBinding
import ru.tripster.guide.extensions.phoneCall
import ru.tripster.guide.ui.MainActivity
import ru.tripster.guide.ui.MainFragment

class RulesOfViewContactBottomSheet :
    BottomSheetDialogFragmentBaseMVVM<RulesOfViewContactBottomSheetViewModel, BottomSheetContactsViewRulesBinding>() {
    override val viewModel: RulesOfViewContactBottomSheetViewModel by viewModel()
    override val binding: BottomSheetContactsViewRulesBinding by viewBinding()
    private val args: RulesOfViewContactBottomSheetArgs by navArgs()

    override fun onView() {
        hideBottomNavBar()
        dialog?.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val coordinatorLayout = binding.container
            val bottomSheetInternal = d.findViewById<View>(R.id.bottomSheet) as ConstraintLayout
            val bottomSheetBehavior = BottomSheetBehavior.from<View>(bottomSheetInternal)
            BottomSheetBehavior.from(coordinatorLayout.parent as View).peekHeight =
                bottomSheetInternal.height
            bottomSheetBehavior.peekHeight = bottomSheetInternal.height
            coordinatorLayout.parent.requestLayout()
        }
        with(binding) {
            when (args.type) {
                context?.getString(R.string.chat_type) -> {
                    title.text = resources.getString(R.string.contacts_view_rules_title)
                    contactViewRulesDesc.text =
                        resources.getString(R.string.contact_view_rules_desc)
                    viewContactBtn.text = resources.getString(R.string.view_contact)
                }
                context?.getString(R.string.private_type) -> {
                    title.text = resources.getString(R.string.contacts_before_order_payment)
                    contactViewRulesDesc.text =
                        resources.getString(R.string.conversion_over_60)
                    viewContactBtn.text = resources.getString(R.string.show_contacts)
                }
                else -> {
                    title.text = resources.getString(R.string.call_before_payment)
                    contactViewRulesDesc.text = resources.getString(R.string.conversion_over_60)
                    viewContactBtn.text = resources.getString(R.string.call)
                }
            }
        }

    }

    override fun onViewClick() {
        with(binding) {
            close.setOnClickListener {
                dismiss()
            }
            viewContactBtn.setOnClickListener {
                when (args.type) {
                    context?.getString(R.string.chat_type), context?.getString(
                        R.string.private_type
                    ) -> {
                        val editContacts =
                            EditTravelerContactsOpen(
                                true
                            )
                        viewModel.editOrder(args.orderId, editContacts)
                    }
                    else -> {
                        val editContacts =
                            EditTravelerContactsOpen(
                                true
                            )
                        viewModel.editOrder(args.orderId, editContacts)
                        context?.let { it1 -> args.phoneNumber.phoneCall(it1) }
                    }
                }
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.dismiss) {
            setFragmentResult(
                PRESS_SHOW_CONTACTS_KEY, bundleOf(
                    PRESS_SHOW_CONTACTS_BUNDLE to true
                )
            )
            if (it) dismiss()
        }
    }

    private fun hideBottomNavBar() {
        if (activity is MainActivity && args.type==context?.getString(R.string.chat_type)) {
            MainFragment().bottomNavBarVisibility(false)
//            (activity as MainActivity).bottomNavBarVisibility(false)
        }
    }

    companion object {
        const val PRESS_SHOW_CONTACTS_KEY = "PRESS_SHOW_CONTACTS_KEY"
        const val PRESS_SHOW_CONTACTS_BUNDLE = "PRESS_SHOW_CONTACTS_BUNDLE"
    }
}


