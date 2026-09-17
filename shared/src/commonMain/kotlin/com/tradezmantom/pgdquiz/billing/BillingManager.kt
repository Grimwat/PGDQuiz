package com.tradezmantom.pgdquiz.billing

import androidx.compose.runtime.Composable
import com.tradezmantom.pgdquiz.ui.data.SubscriptionTier


expect class PlatformContext


expect class PurchaseLauncher

expect class BillingManager(
    platformContext: PlatformContext,
    onTierUpdated: (SubscriptionTier) -> Unit
) {
    fun initialize()
    fun launchApprenticeFlow(launcher: PurchaseLauncher)
    fun launchTradesmanFlow(launcher: PurchaseLauncher)
    fun destroy()
}


@Composable
expect fun rememberPurchaseLauncher(): PurchaseLauncher