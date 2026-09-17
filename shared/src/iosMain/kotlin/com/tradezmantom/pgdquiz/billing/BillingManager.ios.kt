package com.tradezmantom.pgdquiz.billing

import androidx.compose.runtime.Composable
import com.tradezmantom.pgdquiz.ui.data.SubscriptionTier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import platform.StoreKit.*

actual class PlatformContext
actual class PurchaseLauncher

private const val APPRENTICE_PRODUCT_ID = "apprentice_weekly"
private const val TRADESMAN_PRODUCT_ID = "tradesman_lifetime"

actual class BillingManager actual constructor(
    platformContext: PlatformContext,
    private val onTierUpdated: (SubscriptionTier) -> Unit
) {
    private val scope = CoroutineScope(Dispatchers.Main)

    actual fun initialize() {

        scope.launch {
            for (update in Transaction.updates) {
                handleTransactionResult(update)
            }
        }
        // Check current entitlements on launch
        scope.launch {
            for (entitlement in Transaction.currentEntitlements) {
                handleTransactionResult(entitlement)
            }
        }
    }

    actual fun launchApprenticeFlow(launcher: PurchaseLauncher) {
        purchase(APPRENTICE_PRODUCT_ID)
    }

    actual fun launchTradesmanFlow(launcher: PurchaseLauncher) {
        purchase(TRADESMAN_PRODUCT_ID)
    }

    private fun purchase(productId: String) {
        scope.launch {
            try {
                val products = Product.products(listOf(productId))
                val product = products.firstOrNull() ?: return@launch
                val result = product.purchase()
                handlePurchaseResult(result)
            } catch (e: Exception) {

            }
        }
    }

    private fun handlePurchaseResult(result: Product.PurchaseResult) {

    }

    private fun handleTransactionResult(transaction: Transaction) {
        val tier = when (transaction.productID) {
            TRADESMAN_PRODUCT_ID -> SubscriptionTier.TRADESMAN
            APPRENTICE_PRODUCT_ID -> SubscriptionTier.APPRENTICE
            else -> return
        }
        onTierUpdated(tier)
    }

    actual fun destroy() {
    }
}

@Composable
actual fun rememberPurchaseLauncher(): PurchaseLauncher = PurchaseLauncher()