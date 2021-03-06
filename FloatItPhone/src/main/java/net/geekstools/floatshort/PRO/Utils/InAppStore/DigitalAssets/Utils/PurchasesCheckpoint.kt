/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 12/6/20 7:07 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geekstools.floatshort.PRO.Utils.InAppStore.DigitalAssets.Utils

import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import net.geekstools.floatshort.PRO.Utils.Functions.Debug.Companion.PrintDebug
import net.geekstools.floatshort.PRO.Utils.Functions.FunctionsClassLegacy
import net.geekstools.floatshort.PRO.Utils.Functions.NetworkCheckpoint
import net.geekstools.floatshort.PRO.Utils.Functions.PreferencesIO
import net.geekstools.floatshort.PRO.Utils.InAppStore.DigitalAssets.Items.InAppBillingData

class PurchasesCheckpoint(var appCompatActivity: AppCompatActivity) : PurchasesUpdatedListener {

    val functionsClassLegacy: FunctionsClassLegacy = FunctionsClassLegacy(appCompatActivity)

    val preferencesIO: PreferencesIO = PreferencesIO(appCompatActivity)

    val networkCheckpoint: NetworkCheckpoint by lazy {
        NetworkCheckpoint(appCompatActivity)
    }

    fun trigger() : BillingClient {

        val billingClient = BillingClient.newBuilder(appCompatActivity)
                .setListener(this@PurchasesCheckpoint)
                .enablePendingPurchases().build()

        //In-App Billing
        if (networkCheckpoint.networkConnection()) {

            //Restore Purchased Item
            billingClient.startConnection(object : BillingClientStateListener {

                override fun onBillingSetupFinished(billingResult: BillingResult) {

                    billingResult?.let {
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            preferencesIO.savePreference(".PurchasedItem", InAppBillingData.SKU.InAppItemFloatingWidgets, false)

                            billingClient.queryPurchases(BillingClient.SkuType.INAPP).purchasesList?.let { purchases ->

                                for (purchase in purchases) {
                                    PrintDebug("*** Purchased Item: $purchase ***")

                                    preferencesIO.savePreference(".PurchasedItem", purchase.sku, true)

                                    //Consume Donation
                                    if (purchase.sku == InAppBillingData.SKU.InAppItemDonation
                                            && functionsClassLegacy.alreadyDonated()) {

                                        val consumeResponseListener = ConsumeResponseListener { billingResult, purchaseToken ->
                                            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                                                PrintDebug("*** Consumed Item: $purchaseToken ***")

                                                preferencesIO.savePreference(".PurchasedItem", purchase.sku, false)
                                            }
                                        }
                                        val consumeParams = ConsumeParams.newBuilder()
                                        consumeParams.setPurchaseToken(purchase.purchaseToken)
                                        billingClient.consumeAsync(consumeParams.build(), consumeResponseListener)
                                    }

                                    PurchasesCheckpoint.purchaseAcknowledgeProcess(billingClient, purchase, BillingClient.SkuType.INAPP)
                                }

                            }

                        }
                    }
                }

                override fun onBillingServiceDisconnected() {

                }
            })

            //Restore Subscribed Item
            billingClient.startConnection(object : BillingClientStateListener {

                override fun onBillingSetupFinished(billingResult: BillingResult) {

                    billingResult?.let {
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            preferencesIO.savePreference(".SubscribedItem", InAppBillingData.SKU.InAppItemSecurityServices, false)
                            preferencesIO.savePreference(".SubscribedItem", InAppBillingData.SKU.InAppItemSearchEngines, false)

                            billingClient.queryPurchases(BillingClient.SkuType.SUBS).purchasesList?.let { purchases ->

                                for (purchase in purchases) {
                                    PrintDebug("*** Subscribed Item: $purchase ***")

                                    preferencesIO.savePreference(".SubscribedItem", purchase.sku, true)

                                    PurchasesCheckpoint.purchaseAcknowledgeProcess(billingClient, purchase, BillingClient.SkuType.SUBS)
                                }
                            }
                        }
                    }
                }

                override fun onBillingServiceDisconnected() {

                }
            })
        }

        return billingClient
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchasesList: MutableList<Purchase>?) {

        billingResult?.let {
            if (!purchasesList.isNullOrEmpty()) {

                when (billingResult.responseCode) {
                    BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {

                    }
                    BillingClient.BillingResponseCode.OK -> {

                    }
                    else -> {

                    }
                }
            }
        }
    }

    companion object {

        fun purchaseAcknowledgeProcess(billingClient: BillingClient, purchase: Purchase, purchaseType: String) = CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {

            if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                PrintDebug("*** ${purchase.sku} Purchase Acknowledged: ${purchase.isAcknowledged} ***")

                if (!purchase.isAcknowledged) {

                    val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                            .setPurchaseToken(purchase.purchaseToken)

                    val aPurchaseResult: BillingResult = billingClient.acknowledgePurchase(acknowledgePurchaseParams.build())

                    PrintDebug("*** Purchased Acknowledged Result: ${purchase.sku} -> ${aPurchaseResult.debugMessage} ***")
                }
            }
        }
    }
}