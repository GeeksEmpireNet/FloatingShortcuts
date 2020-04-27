/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 4/27/20 4:03 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geekstools.floatshort.PRO.Utils.InAppStore.DigitalAssets.Items.SubscriptionPurchase

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.android.billingclient.api.*
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.button.MaterialButton
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.storage.FirebaseStorage
import net.geekstools.floatshort.PRO.R
import net.geekstools.floatshort.PRO.Utils.Functions.FunctionsClassDebug
import net.geekstools.floatshort.PRO.Utils.InAppStore.DigitalAssets.Extensions.*
import net.geekstools.floatshort.PRO.Utils.InAppStore.DigitalAssets.InitializeInAppBilling
import net.geekstools.floatshort.PRO.Utils.InAppStore.DigitalAssets.Items.InAppBillingData
import net.geekstools.floatshort.PRO.Utils.InAppStore.DigitalAssets.Items.SubscriptionPurchase.Extensions.setScreenshots
import net.geekstools.floatshort.PRO.Utils.InAppStore.DigitalAssets.Items.SubscriptionPurchase.Extensions.setupOneTimePurchaseUI
import net.geekstools.floatshort.PRO.Utils.InAppStore.DigitalAssets.Items.SubscriptionPurchase.Extensions.subscriptionPurchaseFlow
import net.geekstools.floatshort.PRO.Utils.InAppStore.DigitalAssets.Utils.PurchaseFlowController
import net.geekstools.floatshort.PRO.databinding.InAppBillingSubscriptionPurchaseViewBinding
import java.util.*
import kotlin.collections.ArrayList

class SubscriptionPurchase : Fragment(), View.OnClickListener, PurchasesUpdatedListener {

    lateinit var billingClient: BillingClient

    private val billingClientBuilder: BillingClient.Builder by lazy {
        BillingClient.newBuilder(requireActivity())//.build()
    }

    lateinit var purchaseFlowController: PurchaseFlowController
    lateinit var inAppBillingData: InAppBillingData

    private val requestManager: RequestManager by lazy {
        Glide.with(requireContext())
    }

    private val listOfItems: ArrayList<String> = ArrayList<String>()

    val mapIndexDrawable = TreeMap<Int, Drawable>()
    val mapIndexURI = TreeMap<Int, Uri>()

    var screenshotsNumber: Int = 6
    var glideLoadCounter: Int = 0

    lateinit var inAppBillingSubscriptionPurchaseViewBinding: InAppBillingSubscriptionPurchaseViewBinding

    /**
     * Callback After Purchase Dialogue Flow Get Closed
     **/
    override fun onPurchasesUpdated(billingResult: BillingResult?, purchasesList: List<Purchase>?) {
        Log.d(this@SubscriptionPurchase.javaClass.simpleName, "Purchases Updated: ${billingResult?.debugMessage}")

        billingResult?.let {
            if (!purchasesList.isNullOrEmpty()) {

                when (billingResult.responseCode) {
                    BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {

                        purchaseFlowController.purchaseFlowPaid(billingClient, purchasesList[0])
                    }
                    BillingClient.BillingResponseCode.OK -> {

                        purchaseFlowController.purchaseFlowPaid(billingClient, purchasesList[0])
                    }
                    else -> {

                        purchaseFlowController.purchaseFlowDisrupted(billingResult.debugMessage)
                    }
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listOfItems.add(arguments?.getString(InitializeInAppBilling.Entry.ItemToPurchase) ?: InAppBillingData.SKU.InAppItemDonation)
    }

    override fun onCreateView(layoutInflater: LayoutInflater, viewGroup: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(layoutInflater, viewGroup, savedInstanceState)
        inAppBillingSubscriptionPurchaseViewBinding = InAppBillingSubscriptionPurchaseViewBinding.inflate(layoutInflater)

        return inAppBillingSubscriptionPurchaseViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOneTimePurchaseUI()

        billingClient = billingClientBuilder.setListener(this@SubscriptionPurchase).enablePendingPurchases().build()
        billingClient.startConnection(object : BillingClientStateListener {

            override fun onBillingServiceDisconnected() {

                purchaseFlowController.purchaseFlowDisrupted(null)
            }

            override fun onBillingSetupFinished(billingResult: BillingResult?) {

                val skuDetailsParams = SkuDetailsParams.newBuilder()
                        .setSkusList(listOfItems)
                        .setType(BillingClient.SkuType.SUBS)
                        .build()

                billingClient.querySkuDetailsAsync(skuDetailsParams) { queryBillingResult, skuDetailsListInApp ->
                    FunctionsClassDebug.PrintDebug("Billing Result: ${queryBillingResult.debugMessage} | Sku Details List In App Purchase: $skuDetailsListInApp")

                    when (queryBillingResult.responseCode) {
                        BillingClient.BillingResponseCode.ERROR -> {

                            purchaseFlowController.purchaseFlowDisrupted(queryBillingResult.debugMessage)
                        }
                        BillingClient.BillingResponseCode.USER_CANCELED -> {

                            purchaseFlowController.purchaseFlowDisrupted(queryBillingResult.debugMessage)
                        }
                        BillingClient.BillingResponseCode.BILLING_UNAVAILABLE -> {

                            purchaseFlowController.purchaseFlowDisrupted(queryBillingResult.debugMessage)
                        }
                        BillingClient.BillingResponseCode.SERVICE_TIMEOUT -> {

                            purchaseFlowController.purchaseFlowDisrupted(queryBillingResult.debugMessage)
                        }
                        BillingClient.BillingResponseCode.SERVICE_DISCONNECTED -> {

                            purchaseFlowController.purchaseFlowDisrupted(queryBillingResult.debugMessage)
                        }
                        BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE -> {

                            purchaseFlowController.purchaseFlowDisrupted(queryBillingResult.debugMessage)
                        }
                        BillingClient.BillingResponseCode.ITEM_UNAVAILABLE -> {

                            purchaseFlowController.purchaseFlowDisrupted(queryBillingResult.debugMessage)
                        }
                        BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {

                            if (skuDetailsListInApp.isNotEmpty()) {

                                purchaseFlowController.purchaseFlowPaid(skuDetails = skuDetailsListInApp[0])
                            }
                        }
                        BillingClient.BillingResponseCode.OK -> {

                            if (skuDetailsListInApp.isNotEmpty()) {

                                purchaseFlowController.purchaseFlowSucceeded(skuDetails = skuDetailsListInApp[0])

                                subscriptionPurchaseFlow(skuDetailsListInApp[0])

                                if (listOfItems[0] == InAppBillingData.SKU.InAppItemDonation) {

                                    inAppBillingSubscriptionPurchaseViewBinding.itemTitleView.visibility = View.GONE
                                    inAppBillingSubscriptionPurchaseViewBinding.itemDescriptionView.text =
                                            Html.fromHtml("<br/>" +
                                                    "<big>${skuDetailsListInApp[0].title}</big>" +
                                                    "<br/>" +
                                                    "<br/>" +
                                                    "${skuDetailsListInApp[0].description}" +
                                                    "<br/>")

                                    (inAppBillingSubscriptionPurchaseViewBinding
                                            .centerPurchaseButton.root as MaterialButton).text = getString(R.string.donate)
                                    (inAppBillingSubscriptionPurchaseViewBinding
                                            .bottomPurchaseButton.root as MaterialButton).visibility = View.INVISIBLE

                                    inAppBillingSubscriptionPurchaseViewBinding.itemScreenshotsView.visibility = View.GONE

                                } else {

                                    inAppBillingSubscriptionPurchaseViewBinding.itemTitleView.text = (listOfItems[0].convertToItemTitle())

                                    val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
                                    firebaseRemoteConfig.setConfigSettingsAsync(FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(0).build())
                                    firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_default)
                                    firebaseRemoteConfig.fetchAndActivate().addOnSuccessListener {

                                        inAppBillingSubscriptionPurchaseViewBinding
                                                .itemDescriptionView.text = Html.fromHtml(firebaseRemoteConfig.getString(listOfItems[0].convertToRemoteConfigDescriptionKey()))

                                        (inAppBillingSubscriptionPurchaseViewBinding
                                                .centerPurchaseButton.root as MaterialButton).text = firebaseRemoteConfig.getString(listOfItems[0].convertToRemoteConfigPriceInformation())
                                        (inAppBillingSubscriptionPurchaseViewBinding
                                                .bottomPurchaseButton.root as MaterialButton).text = firebaseRemoteConfig.getString(listOfItems[0].convertToRemoteConfigPriceInformation())

                                        screenshotsNumber = firebaseRemoteConfig.getLong(listOfItems[0].convertToRemoteConfigScreenshotNumberKey()).toInt()

                                        for (i in 1..screenshotsNumber) {
                                            val firebaseStorage = FirebaseStorage.getInstance()
                                            val firebaseStorageReference = firebaseStorage.reference
                                            val storageReference = firebaseStorageReference
                                                    .child("Assets/Images/Screenshots/${listOfItems[0].convertToStorageScreenshotsDirectory()}/IAP.Demo/${listOfItems[0].convertToStorageScreenshotsFileName(i)}")
                                            storageReference.downloadUrl.addOnSuccessListener { screenshotLink ->

                                                requestManager
                                                        .load(screenshotLink)
                                                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                                                        .addListener(object : RequestListener<Drawable> {
                                                            override fun onLoadFailed(glideException: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {

                                                                return false
                                                            }

                                                            override fun onResourceReady(resource: Drawable, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                                                glideLoadCounter++

                                                                val beforeToken: String = screenshotLink.toString().split("?alt=media&token=")[0]
                                                                val drawableIndex = beforeToken[beforeToken.length - 5].toString().toInt()

                                                                mapIndexDrawable[drawableIndex] = resource
                                                                mapIndexURI[drawableIndex] = screenshotLink

                                                                if (glideLoadCounter == screenshotsNumber) {

                                                                    setScreenshots()
                                                                }

                                                                return false
                                                            }

                                                        }).submit()
                                            }
                                        }

                                    }.addOnFailureListener {

                                    }
                                }
                            }
                        }
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()

        if (requestManager.isPaused) {
            requestManager.resumeRequests()
        }
    }

    override fun onPause() {
        super.onPause()

        requestManager.pauseAllRequests()
    }

    override fun onDetach() {
        super.onDetach()

        listOfItems.clear()
    }

    override fun onClick(view: View?) {

        when(view) {
            is ImageView -> {
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(view.getTag().toString())
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    requireContext().startActivity(this@apply)
                }
            }
        }
    }
}