package com.tradezmantom.pgdquiz.billing

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.tradezmantom.pgdquiz.ui.data.SubscriptionTier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BillingManager(
    private val context: Context,
    private val onTierUpdated: (SubscriptionTier) -> Unit
) {
    companion object {
        const val APPRENTICE_PRODUCT_ID = "apprentice_weekly"
        const val TRADESMAN_PRODUCT_ID = "tradesman_lifetime"
    }


    private var apprenticeProductDetails: ProductDetails? = null
    private var tradesmanProductDetails: ProductDetails? = null

    private val _apprenticePriceText = MutableStateFlow<String?>(null)
    val apprenticePriceText: StateFlow<String?> = _apprenticePriceText.asStateFlow()

    private val _tradesmanPriceText = MutableStateFlow<String?>(null)
    val tradesmanPriceText: StateFlow<String?> = _tradesmanPriceText.asStateFlow()

    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        }
    }

    private val billingClient = BillingClient.newBuilder(context)
        .setListener(purchasesUpdatedListener)
        .enablePendingPurchases(
            PendingPurchasesParams.newBuilder()
                .enableOneTimeProducts()
                .build()
        )
        .build()

    fun initialize() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                Log.d("BILLING", "onBillingSetupFinished code=${result.responseCode} msg=${result.debugMessage}")
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    checkExistingPurchases()
                    queryApprenticeDetails()
                    queryTradesmanDetails()
                }
            }
            override fun onBillingServiceDisconnected() {
                Log.w("BILLING", "Billing service disconnected")
            }
        })
    }


    private fun queryApprenticeDetails() {
        val product = QueryProductDetailsParams.Product.newBuilder()
            .setProductId(APPRENTICE_PRODUCT_ID)
            .setProductType(BillingClient.ProductType.SUBS)
            .build()

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(listOf(product))
            .build()

        billingClient.queryProductDetailsAsync(params) { result, queryResult ->
            if (result.responseCode != BillingClient.BillingResponseCode.OK) return@queryProductDetailsAsync
            val productDetails = queryResult.productDetailsList.firstOrNull() ?: return@queryProductDetailsAsync
            apprenticeProductDetails = productDetails

            val phase = productDetails.subscriptionOfferDetails
                ?.firstOrNull()
                ?.pricingPhases
                ?.pricingPhaseList
                ?.firstOrNull()

            _apprenticePriceText.value = phase?.formattedPrice
            Log.d("BILLING", "Apprentice price resolved: ${phase?.formattedPrice}")
        }
    }

    private fun queryTradesmanDetails() {
        val product = QueryProductDetailsParams.Product.newBuilder()
            .setProductId(TRADESMAN_PRODUCT_ID)
            .setProductType(BillingClient.ProductType.INAPP)
            .build()

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(listOf(product))
            .build()

        billingClient.queryProductDetailsAsync(params) { result, queryResult ->
            if (result.responseCode != BillingClient.BillingResponseCode.OK) return@queryProductDetailsAsync
            val productDetails = queryResult.productDetailsList.firstOrNull() ?: return@queryProductDetailsAsync
            tradesmanProductDetails = productDetails

            val formattedPrice = productDetails.oneTimePurchaseOfferDetails?.formattedPrice
            _tradesmanPriceText.value = formattedPrice
            Log.d("BILLING", "Tradesman price resolved: $formattedPrice")
        }
    }


    fun launchApprenticeFlow(activity: Activity) {
        val cached = apprenticeProductDetails
        if (cached != null) {
            launchSubscriptionPurchase(activity, cached)
        } else {
            launchSubscriptionFlow(activity, APPRENTICE_PRODUCT_ID)
        }
    }

    fun launchTradesmanFlow(activity: Activity) {
        val cached = tradesmanProductDetails
        if (cached != null) {
            launchOneTimePurchase(activity, cached)
        } else {
            launchOneTimePurchaseFlow(activity, TRADESMAN_PRODUCT_ID)
        }
    }

    private fun launchSubscriptionPurchase(activity: Activity, productDetails: ProductDetails) {
        val offerToken = productDetails.subscriptionOfferDetails?.firstOrNull()?.offerToken
        if (offerToken == null) {
            Log.w("BILLING", "No offer token on cached apprentice details, bailing")
            return
        }

        val pdParams = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(productDetails)
            .setOfferToken(offerToken)
            .build()
        val flowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(pdParams))
            .build()
        billingClient.launchBillingFlow(activity, flowParams)
    }

    private fun launchOneTimePurchase(activity: Activity, productDetails: ProductDetails) {
        val pdParams = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(productDetails)
            .build()
        val flowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(pdParams))
            .build()
        billingClient.launchBillingFlow(activity, flowParams)
    }


    private fun launchSubscriptionFlow(activity: Activity, productId: String) {
        Log.d("BILLING", "launchSubscriptionFlow (fallback query) called for $productId")
        val product = QueryProductDetailsParams.Product.newBuilder()
            .setProductId(productId)
            .setProductType(BillingClient.ProductType.SUBS)
            .build()

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(listOf(product))
            .build()

        billingClient.queryProductDetailsAsync(params) { result, queryResult ->
            if (result.responseCode != BillingClient.BillingResponseCode.OK) return@queryProductDetailsAsync
            val productDetails = queryResult.productDetailsList.firstOrNull() ?: return@queryProductDetailsAsync
            apprenticeProductDetails = productDetails
            launchSubscriptionPurchase(activity, productDetails)
        }
    }

    private fun launchOneTimePurchaseFlow(activity: Activity, productId: String) {
        val product = QueryProductDetailsParams.Product.newBuilder()
            .setProductId(productId)
            .setProductType(BillingClient.ProductType.INAPP)
            .build()

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(listOf(product))
            .build()

        billingClient.queryProductDetailsAsync(params) { result, queryResult ->
            if (result.responseCode != BillingClient.BillingResponseCode.OK) return@queryProductDetailsAsync
            val productDetails = queryResult.productDetailsList.firstOrNull() ?: return@queryProductDetailsAsync
            tradesmanProductDetails = productDetails
            launchOneTimePurchase(activity, productDetails)
        }
    }

    private fun checkExistingPurchases() {
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        ) { _, purchases ->
            if (purchases != null) {
                for (purchase in purchases) handlePurchase(purchase)
            }
        }

        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        ) { _, purchases ->
            if (purchases != null) {
                for (purchase in purchases) handlePurchase(purchase)
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState != Purchase.PurchaseState.PURCHASED) return

        val tier = when {
            purchase.products.contains(TRADESMAN_PRODUCT_ID) -> SubscriptionTier.TRADESMAN
            purchase.products.contains(APPRENTICE_PRODUCT_ID) -> SubscriptionTier.APPRENTICE
            else -> return
        }

        if (!purchase.isAcknowledged) {
            val ackParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
            billingClient.acknowledgePurchase(ackParams) { }
        }

        onTierUpdated(tier)
    }

    fun destroy() {
        billingClient.endConnection()
    }
}