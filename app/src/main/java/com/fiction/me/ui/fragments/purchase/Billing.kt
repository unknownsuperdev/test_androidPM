package com.fiction.me.ui.fragments.purchase

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingFlowParams.ProductDetailsParams
import com.android.billingclient.api.QueryProductDetailsParams.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class Billing(private val context: Context) {
    private lateinit var billingClient: BillingClient
    private val productsList: MutableList<ProductDetails> = mutableListOf()
    var onPurchaseResponseListener: (String) -> Unit = {}
    var onProductPriceListener: (Map<String, String?>) -> Unit = {}
    private var productIds = emptyList<String>()

    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            when (billingResult.responseCode) {
                BillingClient.BillingResponseCode.OK -> {
                    purchases?.forEach {
                        verifyPurchase(it)
                    }
                }
                BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                    // item already owned? call queryPurchasesAsync to verify and process all such items
                    queryPurchasesAsync()
                }
                BillingClient.BillingResponseCode.SERVICE_DISCONNECTED -> {
                    connectToPlayBilling()
                }
                else -> {
                    onPurchaseResponseListener("")
                }
            }
        }

    fun instantiateAndConnectToPlayBillingService(productIds: List<String>) {
        this.productIds = productIds
        billingClient = BillingClient.newBuilder(context)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()
        connectToPlayBilling()
    }

    private fun connectToPlayBilling() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    showProducts()
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.i("Billing", "Error: on Billing Service Disconnected")
                //connectToPlayBilling()
            }
        })
    }

    private fun getProductList(productIDs: List<String>) =
        productIDs.map {
            Product.newBuilder()
                .setProductId(it)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        }

    fun showProducts() {

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(getProductList(productIds))
            .build()

        billingClient.queryProductDetailsAsync(params) { billingResult: BillingResult?, list: List<ProductDetails> ->
            Log.i("Billing", "showProducts: ${billingResult?.responseCode}")
            productsList.clear()
            when (billingResult?.responseCode) {
                BillingClient.BillingResponseCode.OK -> {
                    if (list.isNotEmpty()) {
                        list.forEach {
                            CoroutineScope(Dispatchers.Main).launch {
                                productsList.add(it)
                                Log.i(
                                    "BillingProduct",
                                    "querySkuDetailsitems title = ${it.title} productId =  ${it}, price = ${it.oneTimePurchaseOfferDetails?.formattedPrice}"
                                )
                                getProductsPrices()
                            }
                        }
                    }
                }
            }
        }
    }

    fun launchPurchaseFlow(activity: Activity, showingProductId: String) {
        var productDetails: ProductDetails? = null
        productsList.forEach {
            if (showingProductId == it.productId)
                productDetails = it
        }

        productDetails?.let {
            val productDetailsParamsList: List<ProductDetailsParams> =
                listOf(
                    ProductDetailsParams.newBuilder()
                        .setProductDetails(it)
                        .build()
                )
            val billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build()
            billingClient.launchBillingFlow(activity, billingFlowParams)
        }
    }

    private fun getProductsPrices() {
        val productCoinsPrice = mutableMapOf<String, String?>()
        productsList.forEach {
            val coins = it.name.substringBefore(" ")
            val price = it.oneTimePurchaseOfferDetails?.formattedPrice
            productCoinsPrice[coins] = price
        }
        onProductPriceListener(productCoinsPrice)
    }

    private fun queryPurchasesAsync() {
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP)
                .build()
        ) { billingResult: BillingResult, list: List<Purchase> ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                for (purchase in list) {
                    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
                        verifyPurchase(purchase)
                    }
                }
            }
        }
    }

    private fun verifyPurchase(purchase: Purchase) {
        val consumeParams = ConsumeParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()
        val listener =
            ConsumeResponseListener { billingResult: BillingResult, s: String? ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    onPurchaseResponseListener(purchase.purchaseToken)
                    Log.i("Billing", "SUCCEFULY PURCHASED ${purchase.purchaseToken}")
                }
            }
        billingClient.consumeAsync(consumeParams, listener)
    }
}