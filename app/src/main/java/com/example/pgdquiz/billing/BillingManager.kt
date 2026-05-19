package com.example.pgdquiz.billing

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.*
import com.example.pgdquiz.ui.data.SubscriptionTier

class BillingManager(
    private val context: Context,
    private val onTierUpdated: (SubscriptionTier) -> Unit
) {
    companion object {
        const val APPRENTICE_PRODUCT_ID = "apprentice_weekly"
        const val TRADESMAN_PRODUCT_ID = "tradesman_lifetime"
    }

    private val billingClient = BillingClient.newBuilder(context)
        .setListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                purchases?.forEach { handlePurchase(it) }
            }
        }
        .enablePendingPurchases()
        .build()

    fun initialize() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    checkExistingPurchases()
                }
            }
            override fun onBillingServiceDisconnected() {
                Log.w("BILLING", "Billing service disconnected")
            }
        })
    }

    fun launchApprenticeFlow(activity: Activity) {
        launchSubscriptionFlow(activity, APPRENTICE_PRODUCT_ID)
    }

    fun launchTradesmanFlow(activity: Activity) {
        launchOneTimePurchaseFlow(activity, TRADESMAN_PRODUCT_ID)
    }

    private fun launchSubscriptionFlow(activity: Activity, productId: String) {
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(listOf(
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(productId)
                    .setProductType(BillingClient.ProductType.SUBS)
                    .build()
            )).build()

        billingClient.queryProductDetailsAsync(params) { result, productDetailsList ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK
                && productDetailsList.isNotEmpty()) {

                val product = productDetailsList.first()
                val offerToken = product.subscriptionOfferDetails
                    ?.firstOrNull()?.offerToken ?: return@queryProductDetailsAsync

                val flowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(listOf(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                            .setProductDetails(product)
                            .setOfferToken(offerToken)
                            .build()
                    )).build()

                billingClient.launchBillingFlow(activity, flowParams)
            }
        }
    }

    private fun launchOneTimePurchaseFlow(activity: Activity, productId: String) {
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(listOf(
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(productId)
                    .setProductType(BillingClient.ProductType.INAPP)
                    .build()
            )).build()

        billingClient.queryProductDetailsAsync(params) { result, productDetailsList ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK
                && productDetailsList.isNotEmpty()) {

                val flowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(listOf(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                            .setProductDetails(productDetailsList.first())
                            .build()
                    )).build()

                billingClient.launchBillingFlow(activity, flowParams)
            }
        }
    }

    private fun checkExistingPurchases() {
        // Check one-time purchases (Tradesman)
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        ) { _, purchases ->
            purchases.forEach { handlePurchase(it) }
        }

        // Check subscriptions (Apprentice)
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        ) { _, purchases ->
            purchases.forEach { handlePurchase(it) }
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