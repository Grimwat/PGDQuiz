package com.tradezmantom.pgdquiz.billing

import androidx.compose.runtime.Composable
import com.tradezmantom.pgdquiz.ui.data.SubscriptionTier
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSError
import platform.StoreKit.SKPayment
import platform.StoreKit.SKPaymentQueue
import platform.StoreKit.SKPaymentTransaction
import platform.StoreKit.SKPaymentTransactionObserverProtocol
import platform.StoreKit.SKPaymentTransactionState
import platform.StoreKit.SKProduct
import platform.StoreKit.SKProductsRequest
import platform.StoreKit.SKProductsRequestDelegateProtocol
import platform.StoreKit.SKProductsResponse
import platform.StoreKit.SKRequest
import platform.darwin.NSObject

actual class PlatformContext
actual class PurchaseLauncher

private const val APPRENTICE_PRODUCT_ID = "apprentice_weekly"
private const val TRADESMAN_PRODUCT_ID = "tradesman_lifetime"

// StoreKit 2 (Transaction/Product async APIs) is Swift-only and not visible to Kotlin/Native
// cinterop, so purchases go through the Objective-C-bridged StoreKit 1 APIs instead.
@OptIn(ExperimentalForeignApi::class)
actual class BillingManager actual constructor(
    platformContext: PlatformContext,
    private val onTierUpdated: (SubscriptionTier) -> Unit
) {
    private var pendingProductRequest: ProductRequestHandler? = null

    private val transactionObserver = object : NSObject(), SKPaymentTransactionObserverProtocol {
        override fun paymentQueue(queue: SKPaymentQueue, updatedTransactions: List<*>) {
            @Suppress("UNCHECKED_CAST")
            for (transaction in updatedTransactions as List<SKPaymentTransaction>) {
                handleTransaction(transaction, queue)
            }
        }
    }

    actual fun initialize() {
        SKPaymentQueue.defaultQueue().addTransactionObserver(transactionObserver)
    }

    actual fun launchApprenticeFlow(launcher: PurchaseLauncher) {
        purchase(APPRENTICE_PRODUCT_ID)
    }

    actual fun launchTradesmanFlow(launcher: PurchaseLauncher) {
        purchase(TRADESMAN_PRODUCT_ID)
    }

    private fun purchase(productId: String) {
        val handler = ProductRequestHandler(productId) { product ->
            pendingProductRequest = null
            if (product != null) {
                SKPaymentQueue.defaultQueue().addPayment(SKPayment.paymentWithProduct(product))
            }
        }
        pendingProductRequest = handler
        handler.start()
    }

    private fun handleTransaction(transaction: SKPaymentTransaction, queue: SKPaymentQueue) {
        when (transaction.transactionState) {
            SKPaymentTransactionState.SKPaymentTransactionStatePurchased,
            SKPaymentTransactionState.SKPaymentTransactionStateRestored -> {
                val tier = when (transaction.payment.productIdentifier) {
                    TRADESMAN_PRODUCT_ID -> SubscriptionTier.TRADESMAN
                    APPRENTICE_PRODUCT_ID -> SubscriptionTier.APPRENTICE
                    else -> null
                }
                if (tier != null) onTierUpdated(tier)
                queue.finishTransaction(transaction)
            }
            else -> Unit
        }
    }

    actual fun destroy() {
        SKPaymentQueue.defaultQueue().removeTransactionObserver(transactionObserver)
    }
}

@OptIn(ExperimentalForeignApi::class)
private class ProductRequestHandler(
    productId: String,
    private val onResult: (SKProduct?) -> Unit
) : NSObject(), SKProductsRequestDelegateProtocol {
    private val request = SKProductsRequest(productIdentifiers = setOf(productId))

    init {
        request.delegate = this
    }

    fun start() {
        request.start()
    }

    override fun productsRequest(request: SKProductsRequest, didReceiveResponse: SKProductsResponse) {
        @Suppress("UNCHECKED_CAST")
        val products = didReceiveResponse.products as List<SKProduct>
        onResult(products.firstOrNull())
    }

    override fun request(request: SKRequest, didFailWithError: NSError) {
        onResult(null)
    }
}

@Composable
actual fun rememberPurchaseLauncher(): PurchaseLauncher = PurchaseLauncher()
