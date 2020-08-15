/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 8/15/20 5:41 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geekstools.floatshort.PRO.Utils.InAppStore.DigitalAssets.Items.OneTimePurchase.Extensions

import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.SkuDetails
import net.geekstools.floatshort.PRO.Utils.InAppStore.DigitalAssets.InitializeInAppBilling
import net.geekstools.floatshort.PRO.Utils.InAppStore.DigitalAssets.Items.OneTimePurchase.OneTimePurchase

fun OneTimePurchase.oneTimePurchaseFlow(skuDetails: SkuDetails) {

    inAppBillingOneTimePurchaseViewBinding.centerPurchaseButton.root.setOnClickListener {

        purchaseFlowCommand(skuDetails)
    }

    inAppBillingOneTimePurchaseViewBinding.bottomPurchaseButton.root.setOnClickListener {

        purchaseFlowCommand(skuDetails)
    }
}

private fun OneTimePurchase.purchaseFlowCommand(skuDetails: SkuDetails) {
    val billingFlowParams = BillingFlowParams.newBuilder()
            .setSkuDetails(skuDetails)
            .build()

    val billingResult = billingClient.launchBillingFlow(requireActivity() as InitializeInAppBilling, billingFlowParams)
    purchaseFlowController?.purchaseFlowInitial(billingResult)
}