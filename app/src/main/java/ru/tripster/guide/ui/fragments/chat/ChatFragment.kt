package ru.tripster.guide.ui.fragments.chat

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.tripster.domain.model.chat.OrderCommentGetModel
import ru.tripster.domain.model.chat.OrderCommentsHeader
import ru.tripster.domain.model.chat.OrderPostComment
import ru.tripster.domain.model.chat.UserRoleChat
import ru.tripster.domain.model.events.OrderDetailsForChat
import ru.tripster.domain.model.events.StatisticsData
import ru.tripster.domain.model.events.Status
import ru.tripster.guide.Constants.CHAT_HEADER_ID
import ru.tripster.guide.R
import ru.tripster.guide.Screen
import ru.tripster.guide.appbase.FragmentBaseNCMVVM
import ru.tripster.guide.appbase.PaginationScrollListener
import ru.tripster.guide.appbase.utils.bottomNavBarVisibility
import ru.tripster.guide.appbase.utils.viewBinding
import ru.tripster.guide.databinding.FragmentOrderConfirmChatBinding
import ru.tripster.guide.extensions.*
import ru.tripster.guide.ui.MainFragment
import kotlin.coroutines.coroutineContext

class ChatFragment : FragmentBaseNCMVVM<ChatViewModel, FragmentOrderConfirmChatBinding>() {
    override val viewModel: ChatViewModel by viewModel()
    override val binding: FragmentOrderConfirmChatBinding by viewBinding()
    private val args: ChatFragmentArgs by navArgs()
    private lateinit var order: OrderDetailsForChat
    private lateinit var statisticData: StatisticsData

    private var messagingAdapter = ChatAdapter(
        { comment, key ->
            viewModel.postComment(
                OrderPostComment(
                    comment = comment,
                    order = order.orderId,
                    postContacts = false,
                    userRole = UserRoleChat.GUIDE.value,
                    viewedByGuide = true,
                ), key
            )
        },
        showContact = { goToRulesOfViewContactBottomSheet() },
        howToSeeContact = { navigateToSeeContactWarningBottomSheet() },
        rootClick = { activity?.hideSoftKeyboard(binding.inputMessage) }) {

        context?.let {
            startActivity(
                (getString(R.string.BASE_URL) + getString(
                    R.string.open_ticket_link,
                    order.orderId
                )).openLinkInBrowser()
            )
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.onBackPressedDispatcher?.addCallback(this) {
            if (args.typeOfNavigation == context.getString(R.string.date_order_type)) {
                navigateFragment(
                    ChatFragmentDirections.actionChatFragmentToOrderDetailsIndividualFragment(
                        order.orderId,
                        0,
                        context.getString(R.string.chat_type),
                        backButtonClick(),
                        args.statisticData
                    )
                )
            } else popBackStack()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onView() {
        activity?.bottomNavBarVisibility(false)

        order = Gson().fromJson(args.order, OrderDetailsForChat::class.java)
        statisticData = Gson().fromJson(args.statisticData, StatisticsData::class.java)

        messagingAdapter.orderDetail(order.status, order.awareStartDt, order.type)
        activity?.setLightStatusBar()

        if (context?.isOnline() == true) {
            viewModel.getOrderComments(
                OrderCommentGetModel(
                    order.orderId,
                    OrderCommentsHeader(
                        CHAT_HEADER_ID,
                        order.memberCountAndPrice,
                        order.avatar.replace(" ", "/"),
                        order.name
                    ), order.currency,
                    statisticData.canOpenContacts,
                    statisticData.ordersRateValue
                ), viewModel.page
            )
        } else {
            errorMessage(false)
        }

        viewModel.markAsRead()
        with(binding) {
            rootView.viewTreeObserver.addOnGlobalLayoutListener {

                if (isAdded) {
                    val rect = Rect()
                    rootView.getWindowVisibleDisplayFrame(rect)
                    val screenHeight: Int = rootView.rootView.height
                    val keypadHeight = screenHeight - rect.bottom
                    inputMessage.isCursorVisible = keypadHeight > screenHeight * 0.15
                }
            }

            if (inputMessage.text.toString().isEmpty()) {
                context?.let { context ->
                    ContextCompat.getColor(
                        context,
                        R.color.gray_80
                    )
                }
                    ?.let { btnSend.setColorFilter(it) }
            } else {
                context?.let { context ->
                    ContextCompat.getColor(
                        context,
                        R.color.gray_20
                    )
                }?.let { btnSend.setColorFilter(it) }
            }


            if (Build.VERSION.SDK_INT <= 28) {
                rVMessaging.overScrollMode = View.OVER_SCROLL_NEVER
            }
            toolbar.setTitleText(order.title)
            toolbar.setSubTitle(
                if (order.date != " ")
                    order.date else
                    resources.getString(R.string.no_date_and_time)
            )
            toolbar.setPhoneVisibility(order.status == Status.PAID.value)
            toolbar.onPhoneClick {
                context?.let { it1 -> order.phone.phoneCall(it1) }
            }

            rVMessaging.apply {
                context?.let {
                    val mLayoutManager = LinearLayoutManager(it)
                    layoutManager = mLayoutManager
                    mLayoutManager.reverseLayout = true
                    adapter = messagingAdapter
                    setOnTouchListener { v, event ->
                        activity?.hideSoftKeyboard(inputMessage)
                        v?.onTouchEvent(event) ?: true
                    }
                    addOnScrollListener(object : PaginationScrollListener(mLayoutManager) {

                        override fun loadMoreItems() {

                            if (!viewModel.isLastPage) {
                                viewModel.isLoading = true
                                binding.loadMoreProgress.makeVisible()
                                viewModel.getOrderComments(
                                    OrderCommentGetModel(
                                        order.orderId,
                                        OrderCommentsHeader(
                                            CHAT_HEADER_ID,
                                            order.memberCountAndPrice,
                                            order.avatar.replace(" ","/"),
                                            order.name
                                        ), order.currency,
                                        statisticData.canOpenContacts,
                                        statisticData.ordersRateValue
                                    ), ++viewModel.page
                                )
                            }
                        }

                        override val isLastPage: Boolean
                            get() = viewModel.isLastPage

                        override val isLoading: Boolean
                            get() = viewModel.isLoading

                    })
                }
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.errorMessage) {
            goToWarningBottomSheet()
        }

        onEach(viewModel.commentsList) {
            setFragmentResult(
                VIEWED_BY_GUIDE_KEY,
                bundleOf(VIEWED_BY_GUIDE_BUNDLE to order.viewedByGuid)
            )
            with(binding) {
                var isListSizeDifferent = false
//                viewModel.noInternetToastShown = false

                loadingIcon.makeVisibleGone()
                issue.containerIssue.makeVisibleGone()
                rVMessaging.makeVisible()
                inputMessage.makeVisible()
                connectionProgress.makeVisibleGone()
                loadMoreProgress.makeVisibleGone()

                if (it.size != messagingAdapter.itemCount) isListSizeDifferent = true

                messagingAdapter.submitList(it)

                val layoutManager = rVMessaging.layoutManager as LinearLayoutManager
                rootView.viewTreeObserver.addOnGlobalLayoutListener {
                    if (!rVMessaging.canScrollVertically(-1) && isAdded) {
                        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                        val totalItemCount = layoutManager.itemCount
                        if (lastVisibleItemPosition == totalItemCount - 1) {
                            rVMessaging.addItemDecoration(VerticalSpaceItemDecoration(80))
                        }
                    }
                }


                if (isListSizeDifferent) {
                    lifecycleScope.launch {
                        delay(50)
                        rVMessaging.scrollToPosition(0)
                    }
                }

                viewModel.isLoading = false
                toolbar.setSubTitle(
                    if (order.date != " ")
                        order.date else
                        resources.getString(R.string.no_date_and_time)
                )

            }
        }

        onEach(viewModel.commentsError) {
            binding.loadingIcon.makeVisibleGone()
            if (isOnline()) {
                context?.getString(R.string.connecting)
                    ?.let { it1 -> binding.toolbar.setSubTitle(it1) }
                context?.getStringRes(R.string.try_again_later)
                    ?.let { Toast(context).showCustomToast(it, this, false) }
//                            binding.connectionProgress.makeVisible()
            } else {
                errorMessage(true)

//                binding.toolbar.setSubTitle(
//                    if (args.order.date != " ")
//                        args.order.date else
//                        resources.getString(R.string.no_date_and_time)
//                )


//                if (!viewModel.noInternetToastShown)
//                    context?.getStringRes(R.string.no_internet_toast)
//                        ?.let { Toast(context).showCustomToast(it, this, false) }
//                viewModel.noInternetToastShown = true
            }
        }

        onEach(viewModel.orderComments) {
            if (!viewModel.isLastPage)
                viewModel.isLastPage = it?.next?.isEmpty() == true
            binding.loadMoreProgress.makeVisibleGone()
        }

        onEach(viewModel.postCommentError) {

        }
    }


    override fun onViewClick() {
        with(binding) {
            setFragmentResultListener(WarningSendContactsBottomSheet.SEND_MESSAGE_BUNDLE) { _, bundle ->
                if (bundle.getBoolean(WarningSendContactsBottomSheet.SEND_MESSAGE_REQUEST_KEY)) {
                    viewModel.booleanOfContactsSend = true
                    btnSend.performClick()
                }
            }

            error.update.setOnClickListener {
                isOnlineAgain()
            }

            btnSend.setOnClickListener {
                if (!inputMessage.text.isNullOrEmpty()) {
                    if (order.status != Status.PAID.value) {
                        if (!inputMessageLogic(inputMessage) || viewModel.booleanOfContactsSend) {
                            if (sendMessageLogic(inputMessage.text.toString())) {
                                viewModel.postComment(
                                    OrderPostComment(
                                        comment = inputMessage.text.toString(),
                                        order = order.orderId,
                                        postContacts = viewModel.booleanOfContactsSend,
                                        userRole = UserRoleChat.GUIDE.value,
                                        viewedByGuide = true
                                    )
                                )
                                inputMessage.setText("")

                            }
                        } else goToWarningBottomSheet()
                    } else {
                        if (sendMessageLogic(inputMessage.text.toString())) {
                            viewModel.postComment(
                                OrderPostComment(
                                    comment = inputMessage.text.toString(),
                                    order = order.orderId,
                                    postContacts = false,
                                    userRole = UserRoleChat.GUIDE.value,
                                    viewedByGuide = true
                                )
                            )
                            inputMessage.setText("")

                        }
                    }
                }
            }
            inputMessage.setOnClickListener {
                activity?.showSoftKeyboard(inputMessage)
            }
            rootView.setOnClickListener {
                activity?.hideSoftKeyboard(inputMessage)
            }

            inputMessage.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    if (!s.isNullOrEmpty() && sendMessageLogic(s.toString())) {
                        context?.let { context ->
                            ContextCompat.getColor(
                                context,
                                R.color.gray_20
                            )
                        }
                            ?.let { btnSend.setColorFilter(it) }
                        inputMessage.setTextAppearance(R.style.Text_17_400)
                    } else {
                        context?.let { context ->
                            ContextCompat.getColor(
                                context,
                                R.color.gray_80
                            )
                        }
                            ?.let { btnSend.setColorFilter(it) }
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            issue.update.setOnClickListener {
                viewModel.page = 0
                viewModel.getOrderComments(
                    OrderCommentGetModel(
                        order.orderId,
                        OrderCommentsHeader(
                            CHAT_HEADER_ID,
                            order.memberCountAndPrice,
                            order.avatar.replace(" ","/"),
                            order.name
                        ), order.currency,
                        statisticData.canOpenContacts,
                        statisticData.ordersRateValue
                    ), viewModel.page
                )
            }
        }
    }

    private fun isOnline(): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
        return false
    }

    private fun offlineError() {
        with(binding) {
            loadingIcon.makeVisibleGone()
            rVMessaging.makeVisibleGone()
            inputMessage.makeVisibleGone()
            with(issue) {
                containerIssue.makeVisible()
                title.text = context?.getStringRes(R.string.no_internet_title)
                message.text = context?.getStringRes(R.string.no_internet_message)
            }
        }
    }

    private fun goToRulesOfViewContactBottomSheet() {
        navigateFragment(
            ChatFragmentDirections.actionChatFragmentToRulesOfViewContactBottomSheet(
                order.orderId,
                context?.getString(R.string.chat_type) ?: "",
                ""
            )
        )
    }

    private fun goToWarningBottomSheet() {
        navigateFragment(
            ChatFragmentDirections.actionChatFragmentToSendContactsBottomSheet()
        )
    }

    private fun inputMessageLogic(editText: EditText): Boolean {
        var isValid = false
        val list = editText.text.split(" ")
        list.forEach {
            it.replace("-", "")
            isValid = (checkPhone(it) || checkEmail(it))
        }
        return isValid
    }

    private fun checkEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun checkPhone(phoneNumber: String): Boolean =
        Patterns.PHONE.matcher(phoneNumber)
            .matches() && phoneNumber.length >= 10

    private fun navigateToSeeContactWarningBottomSheet() {
        navigateFragment(
            ChatFragmentDirections.actionChatFragmentToSeeContactWarningBottomSheet(
                statisticData, context?.getString(R.string.chat_type) ?: ""
            )
        )
    }

    private fun sendMessageLogic(message: String): Boolean {
        message.forEach { char ->
            if (char == ' ') viewModel.countOfSpace++
        }
        if (viewModel.countOfSpace != message.length) {
            viewModel.countOfSpace = 0
            return true
        }

        viewModel.countOfSpace = 0
        return false

    }

    private fun callError() {
        with(binding) {
            rVMessaging.makeVisibleGone()
            inputMessage.makeVisibleGone()

            with(issue) {
                containerIssue.makeVisible()
                title.text = context?.getStringRes(R.string.call_error_title)
                message.text = context?.getStringRes(R.string.call_error_message)
            }
        }

    }

    private fun backButtonClick(): Boolean {
        return args.typeOfNavigation == context?.getString(R.string.date_order_type)
    }

    private fun errorMessage(isOnline: Boolean) {
        with(binding) {
            loadingIcon.makeVisibleGone()
            containerError.makeVisible()
            error.title.text =
                if (isOnline) context?.getStringRes(R.string.call_error_title) else context?.getString(
                    R.string.no_internet_title
                )
            error.message.text =
                if (isOnline) context?.getStringRes(R.string.call_error_message) else context?.getString(
                    R.string.no_internet_message
                )
        }
    }

    private fun isOnlineAgain() {
        with(binding) {
            if (context?.isOnline() == true) {
                containerError.makeVisibleGone()
                loadingIcon.makeVisible()
                viewModel.getOrderComments(
                    OrderCommentGetModel(
                        order.orderId,
                        OrderCommentsHeader(
                            CHAT_HEADER_ID,
                            order.memberCountAndPrice,
                            order.avatar.replace(" ","/"),
                            order.name
                        ), order.currency,
                        statisticData.canOpenContacts,
                        statisticData.ordersRateValue
                    ), viewModel.page
                )
            }
        }
    }

    companion object {
        const val VIEWED_BY_GUIDE_KEY = "VIEWED_BY_GUIDE_KEY"
        const val VIEWED_BY_GUIDE_BUNDLE = "VIEWED_BY_GUIDE_BUNDLE"
    }
}

