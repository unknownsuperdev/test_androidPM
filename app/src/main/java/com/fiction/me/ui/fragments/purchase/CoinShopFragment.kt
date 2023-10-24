package com.fiction.me.ui.fragments.purchase

import android.util.Log
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.analytics.api.Events.USER_PROPERTY__USER_TYPE
import com.analytics.api.Events.USER_PROPERTY__USER_TYPE_PREMIUM
import com.fiction.me.R
import com.fiction.me.appbase.FragmentBaseNCMVVM
import com.fiction.me.appbase.utils.viewBinding
import com.fiction.me.databinding.FragmentCoinShopBinding
import com.fiction.me.ui.fragments.purchase.maindialog.MainDialogFragment
import com.fiction.me.utils.Events.Companion.BOOK_ID_PROPERTY
import com.fiction.me.utils.Events.Companion.COINS
import com.fiction.me.utils.Events.Companion.COINSHOP_SCREEN_CLOSED
import com.fiction.me.utils.Events.Companion.COINSHOP_SCREEN_SHOWN
import com.fiction.me.utils.Events.Companion.ERROR_SCREEN_SHOWN
import com.fiction.me.utils.Events.Companion.GET_COINS_BUTTON_CLICKED
import com.fiction.me.utils.Events.Companion.PACKAGE
import com.fiction.me.utils.Events.Companion.PRICE
import com.fiction.me.utils.Events.Companion.READER
import com.fiction.me.utils.Events.Companion.REFERRER
import com.fiction.me.utils.Events.Companion.USER_PROFILE
import com.fiction.me.utils.Events.Companion.WELL_DONE_SCREEN_SHOWN
import com.fiction.me.utils.Events.Companion.WRITE_TO_SUPPORT_CLICKED
import org.koin.androidx.viewmodel.ext.android.viewModel

class CoinShopFragment :
    FragmentBaseNCMVVM<CoinShopViewModel, FragmentCoinShopBinding>() {

    override val viewModel: CoinShopViewModel by viewModel()
    override val binding: FragmentCoinShopBinding by viewBinding()
    private val tariffsAdapter = TariffsAdapter { selectedItemId ->
        viewModel.updateTariffsItemSelectionState(selectedItemId)
    }
    private val args: CoinShopFragmentArgs by navArgs()
    private val columns = 2

    override fun onView() {
        sendEvent()
        viewModel.getBalance()
        with(binding) {
            toolBar.run {
                setFirstIcon(R.drawable.ic_close_cross_black)
                setLastIcon(R.drawable.ic_one_coin)
                setTitleText(resources.getString(R.string.get_coins))
            }
            viewModel.getTariffsList()
            tariffsRV.apply {
                layoutManager =
                    object : StaggeredGridLayoutManager(columns, LinearLayoutManager.VERTICAL) {
                        override fun canScrollVertically(): Boolean {
                            return true
                        }
                    }
                itemAnimator = null
                adapter = tariffsAdapter
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(this) {
            if (viewModel.isCanCloseScreen) {
                setFragmentResult()
                popBackStack()
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.tariffsItem) {
            tariffsAdapter.submitList(it)
        }

        onEach(viewModel.openGooglePay) { item ->
            sendTariffEvent(GET_COINS_BUTTON_CLICKED)
            activity?.let {  activity ->
                item?.let {
                    viewModel.billing.launchPurchaseFlow(activity, it)
                }
            }
        }
        onEach(viewModel.userBalance) {
            binding.toolBar.setLastText(it.toString())
        }
        onEach(viewModel.purchaseResponse) {
            viewModel.isCanCloseScreen = false
            binding.progressContainer.isVisible = true
            if (it.isNotEmpty()) {
                viewModel.buyCoin(viewModel.selectedTariffsItem?.item ?: "", it)
            } else showError()
        }
        onEach(viewModel.buyTariff) {
            binding.progressContainer.isVisible = false
            viewModel.isCanCloseScreen = true
            viewModel.setUserPropertyEvent(mapOf(USER_PROPERTY__USER_TYPE to USER_PROPERTY__USER_TYPE_PREMIUM))
            showWellDone()
        }
    }

    override fun onViewClick() {
        with(binding) {
            toolBar.setOnFirstIconClickListener {
                if (viewModel.isCanCloseScreen) {
                    setFragmentResult()
                    popBackStack()
                }
            }

            writeToSupport.setOnClickListener {
                viewModel.trackEvents(WRITE_TO_SUPPORT_CLICKED)
                val directions =
                    CoinShopFragmentDirections.actionGetCoinFromStoreFragmentToWriteToSupportFragment()
                navigateFragment(directions)
            }
        }
    }

    private fun setFragmentResultListenerAndNavigate(purchaseToken: Boolean) {
        if (args.isFromProfile) return
        activity?.supportFragmentManager?.setFragmentResult(
            FROM_COIN_STORE,
            bundleOf(
                IS_SUCCEEDED_IN_GETTING_COIN to purchaseToken
            )
        )
        Log.i("GetPurchasedCoin", "setFragmentResultListenerAndNavigate: ")
        if (purchaseToken) goBackOrNavigate()
    }

    private fun goBackOrNavigate() {
        if (args.bookId == -1L && args.chapterId == -1L) {
            popBackStack()
            Log.i("GetPurchasedCoin", "goBackOrNavigate: popBackStack")
        }
        else {
            Log.i("GetPurchasedCoin", "goBackOrNavigate: args.chapterId = ${args.chapterId} args.bookId = ${args.bookId}")
            val directions =
                CoinShopFragmentDirections.actionGetCoinFromStoreFragmentToReaderFragment(
                    chapterId = args.chapterId,
                    bookId = args.bookId,
                    isChapterLocked = false
                )
            navigateFragment(directions)
        }
    }

    private fun showWellDone() {
        val mainDialogFragment = MainDialogFragment.newInstance(
            resources.getString(R.string.well_done),
            resources.getString(R.string.well_done_content)
        )
        mainDialogFragment.show(childFragmentManager, MainDialogFragment::class.java.simpleName)
        sendTariffEvent(WELL_DONE_SCREEN_SHOWN)
        mainDialogFragment.onDismiss = {
            setFragmentResult()
            setFragmentResultListenerAndNavigate(true)
        }

    }

    private fun showError() {
        Log.i("MainDialogFragment", "showError: ")
        binding.progressContainer.isVisible = false
        viewModel.isCanCloseScreen = true
        val mainDialog = MainDialogFragment.newInstance(
            resources.getString(R.string.error),
            resources.getString(R.string.error_content)
        )
        mainDialog.show(childFragmentManager, MainDialogFragment::class.java.simpleName)
        sendTariffEvent(ERROR_SCREEN_SHOWN)
        setFragmentResultListenerAndNavigate(false)
    }

    fun sendEvent() {
        val bookId = args.bookId
        if (args.isFromProfile)
            viewModel.trackEvents(
                COINSHOP_SCREEN_SHOWN,
                hashMapOf(REFERRER to USER_PROFILE)
            )
        else viewModel.trackEvents(
            COINSHOP_SCREEN_SHOWN,
            hashMapOf(REFERRER to READER, BOOK_ID_PROPERTY to bookId)
        )
    }

    private fun sendTariffEvent(eventName: String){
        val product = viewModel.selectedTariffsItem ?: return
        viewModel.trackEvents(eventName, hashMapOf(
            PACKAGE to product.item,
            PRICE to product.price,
            COINS to product.coins
        ))
    }

    private fun setFragmentResult(){
        activity?.supportFragmentManager?.setFragmentResult(
            COIN_SHOP,
            bundleOf(IS_CLOSED to true)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.trackEvents(COINSHOP_SCREEN_CLOSED)
    }

    companion object {
        const val FROM_COIN_STORE = "fromCoinStore"
        const val IS_SUCCEEDED_IN_GETTING_COIN = "succeededInGettingAcoin"
        const val COIN_SHOP = "from Coin shop"
        const val IS_CLOSED = "is closed"
    }
}