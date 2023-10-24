package com.fiction.me.ui.fragments.purchase

import androidx.lifecycle.viewModelScope
import com.fiction.core.ActionResult
import com.fiction.domain.interactors.*
import com.fiction.domain.model.TariffsItem
import com.fiction.me.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CoinShopViewModel(
    private val getAvailableTariffsUseCase: GetAvailableTariffsUseCase,
    private val getBalanceUseCase: GetBalanceUseCase,
    private val buyTariffUseCase: BuyTariffUseCase,
    val billing: Billing
) : BaseViewModel() {

    private val _tariffsItem = MutableStateFlow<List<TariffsItem>?>(null)
    val tariffsItem = _tariffsItem.asStateFlow()

    private val _userBalance = MutableStateFlow<Int?>(null)
    val userBalance = _userBalance.asStateFlow()

    private val _buyTariff = MutableSharedFlow<Unit>()
    val buyTariff = _buyTariff.asSharedFlow()

    private val _openGooglePay = MutableSharedFlow<String?>()
    val openGooglePay = _openGooglePay.asSharedFlow()

    var isCanCloseScreen = true

    private val _purchaseResponse = MutableSharedFlow<String>()
    val purchaseResponse = _purchaseResponse.asSharedFlow()
    var isButtonVisible = false
    var selectedTariffsItem: TariffsItem? = null

    fun getBalance() {
        viewModelScope.launch {
            when (val result = getBalanceUseCase()) {
                is ActionResult.Success -> {
                    _userBalance.emit(result.result)
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

    fun getTariffsList() {
        viewModelScope.launch {
            when (val result = getAvailableTariffsUseCase()) {
                is ActionResult.Success -> {
                    if (result.result.isNotEmpty()) {
                        _tariffsItem.emit(result.result)
                        _openGooglePay.emit(selectedTariffsItem?.item)
                        getAvailableTariffsUseCase(true)
                        initBilling(result.result)
                    }
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

    private fun initBilling(tariffs: List<TariffsItem>){
        val productIds = tariffs.map { it.item }
        billing.instantiateAndConnectToPlayBillingService(productIds)
        billing.onProductPriceListener = { productPriceFromStore ->
            val products = tariffs.map {
                val price = productPriceFromStore[it.coinsCount.replace(" ","")]
                if (price != null) it.copy(price = price) else it
            }
            setTariffs(products)
        }
        billing.onPurchaseResponseListener = {
            viewModelScope.launch {
                _purchaseResponse.emit(it)
            }
        }
    }

    private fun setTariffs(tariffs: List<TariffsItem>){
        viewModelScope.launch {
            _tariffsItem.emit(tariffs)
            getAvailableTariffsUseCase(true)
        }
    }
    fun updateTariffsItemSelectionState(selectedItemId: Long) {

        val oldList = tariffsItem.value
        val newList = oldList?.map {
            if (it.id == selectedItemId)
                selectedTariffsItem = it
            it.copy(isSelected = it.id == selectedItemId)
        }
        viewModelScope.launch {
            isButtonVisible = true
            _tariffsItem.emit(newList)
            _openGooglePay.emit(selectedTariffsItem?.item)
        }
    }

    fun buyCoin(tariff: String, token: String) {
        viewModelScope.launch {
            when (val result = buyTariffUseCase(tariff, token)) {
                is ActionResult.Success -> {
                    getBalance()
                    _buyTariff.emit(Unit)
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

}
