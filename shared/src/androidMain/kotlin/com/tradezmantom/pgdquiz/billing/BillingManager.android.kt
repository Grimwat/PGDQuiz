package com.tradezmantom.pgdquiz.billing

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.tradezmantom.pgdquiz.ui.data.SubscriptionTier

actual class PlatformContext(val context: Context)
actual class PurchaseLauncher(val activity: Activity)

actual class BillingManager actual constructor(
    private val platformContext: PlatformContext,
    private val onTierUpdated: (SubscriptionTier) -> Unit
) {
    companion object {
        const val APPRENTICE_PRODUCT_ID = "apprentice_weekly"
        const val TRADESMAN_PRODUCT_ID = "tradesman_lifetime"
    }

    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        }
    }

    private val billingClient = BillingClient.newBuilder(platformContext.context)
        .setListener(purchasesUpdatedListener)
        .enablePendingPurchases(
            PendingPurchasesParams.newBuilder()
                .enableOneTimeProducts()
                .build()
        )
        .build()

    actual fun initialize() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                Log.d("BILLING", "onBillingSetupFinished code=${result.responseCode} msg=${result.debugMessage}")
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    checkExistingPurchases()
                }
            }
            override fun onBillingServiceDisconnected() {
                Log.w("BILLING", "Billing service disconnected")
            }
        })
    }

    actual fun launchApprenticeFlow(launcher: PurchaseLauncher) {
        launchSubscriptionFlow(launcher.activity, APPRENTICE_PRODUCT_ID)
    }

    actual fun launchTradesmanFlow(launcher: PurchaseLauncher) {
        launchOneTimePurchaseFlow(launcher.activity, TRADESMAN_PRODUCT_ID)
    }

    private fun launchSubscriptionFlow(activity: Activity, productId: String) {
        Log.d("BILLING", "launchSubscriptionFlow called for $productId")
        val product = QueryProductDetailsParams.Product.newBuilder()
            .setProductId(productId)
            .setProductType(BillingClient.ProductType.SUBS)
            .build()

        val productList = java.util.ArrayList<QueryProductDetailsParams.Product>()
        productList.add(product)

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient.queryProductDetailsAsync(params) { result, queryResult ->
            Log.d(
                "BILLING",
                "queryProductDetailsAsync result code=${result.responseCode} msg=${result.debugMessage}"
            )
            if (result.responseCode != BillingClient.BillingResponseCode.OK) return@queryProductDetailsAsync

            val list = queryResult.productDetailsList
            Log.d("BILLING", "product list size=${list.size}")
            if (list.isEmpty()) {
                Log.w("BILLING", "product list EMPTY, bailing"); return@queryProductDetailsAsync
            }

            val productDetails = list[0]
            val subOfferDetails = productDetails.subscriptionOfferDetails
            Log.d("BILLING", "subscriptionOfferDetails=${subOfferDetails?.size ?: "null"}")
            if (subOfferDetails == null || subOfferDetails.isEmpty()) {
                Log.w("BILLING", "no offer details, bailing"); return@queryProductDetailsAsync
            }

            val offerToken = subOfferDetails[0].offerToken

            val pdParams = BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .setOfferToken(offerToken)
                .build()
            val flowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(listOf(pdParams))
                .build()
            val billingResult = billingClient.launchBillingFlow(activity, flowParams)
            Log.d(
                "BILLING",
                "launchBillingFlow result code=${billingResult.responseCode} msg=${billingResult.debugMessage}"
            )
        }
    }

    private fun launchOneTimePurchaseFlow(activity: Activity, productId: String) {
        val product = QueryProductDetailsParams.Product.newBuilder()
            .setProductId(productId)
            .setProductType(BillingClient.ProductType.INAPP)
            .build()

        val productList = java.util.ArrayList<QueryProductDetailsParams.Product>()
        productList.add(product)

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient.queryProductDetailsAsync(params) { result, queryResult ->
            if (result.responseCode != BillingClient.BillingResponseCode.OK) return@queryProductDetailsAsync
            val list = queryResult.productDetailsList
            if (list.isEmpty()) return@queryProductDetailsAsync
            val productDetails = list[0]

            val pdParams = BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .build()
            val flowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(listOf(pdParams))
                .build()
            billingClient.launchBillingFlow(activity, flowParams)
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

    actual fun destroy() {
        billingClient.endConnection()
    }
}

@Composable
actual fun rememberPurchaseLauncher(): PurchaseLauncher {
    return PurchaseLauncher(androidx.compose.ui.platform.LocalContext.current.findActivity())
}

private fun Context.findActivity(): Activity {
    var ctx = this
    while (ctx is android.content.ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    throw IllegalStateException("No Activity found from Context")
}