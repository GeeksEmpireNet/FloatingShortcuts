/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 4/17/20 11:02 PM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geekstools.floatshort.PRO.Utils.InAppStore.DigitalAssets.Items

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
import net.geekstools.floatshort.PRO.Utils.InAppStore.DigitalAssets.InAppBillingData
import net.geekstools.floatshort.PRO.Utils.InAppStore.DigitalAssets.InitializeInAppBilling
import net.geekstools.floatshort.PRO.Utils.InAppStore.DigitalAssets.Items.Extensions.oneTimePurchasePurchaseFlow
import net.geekstools.floatshort.PRO.Utils.InAppStore.DigitalAssets.Items.Extensions.setScreenshots
import net.geekstools.floatshort.PRO.Utils.InAppStore.DigitalAssets.Items.Extensions.setupOneTimePurchaseUI
import net.geekstools.floatshort.PRO.Utils.InAppStore.DigitalAssets.Utils.PurchaseFlowController
import net.geekstools.floatshort.PRO.databinding.InAppBillingOneTimePurchaseViewBinding
import java.util.*
import kotlin.collections.ArrayList

class OneTimePurchase (val purchaseFlowController: PurchaseFlowController,
                       val inAppBillingData: InAppBillingData) : Fragment(), View.OnClickListener, PurchasesUpdatedListener {

    lateinit var billingClient: BillingClient

    private val billingClientBuilder: BillingClient.Builder by lazy {
        BillingClient.newBuilder(requireActivity())//.build()
    }

    private val requestManager: RequestManager by lazy {
        Glide.with(requireContext())
    }

    private val listOfItems: ArrayList<String> = ArrayList<String>()

    val mapIndexDrawable = TreeMap<Int, Drawable>()
    val mapIndexURI = TreeMap<Int, Uri>()

    var screenshotsNumber: Int = 6
    var glideLoadCounter: Int = 0

    lateinit var inAppBillingOneTimePurchaseViewBinding: InAppBillingOneTimePurchaseViewBinding

    override fun onPurchasesUpdated(billingResult: BillingResult?, purchasesList: MutableList<Purchase>?) {//ResponseCode 7 = Item Owned
        Log.d(this@OneTimePurchase.javaClass.simpleName, "PurchasesUpdated: ${billingResult?.debugMessage}")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listOfItems.add(arguments?.getString(InitializeInAppBilling.Entry.ItemToPurchase) ?: InAppBillingData.InAppItemDonation)
    }

    override fun onCreateView(layoutInflater: LayoutInflater, viewGroup: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(layoutInflater, viewGroup, savedInstanceState)
        inAppBillingOneTimePurchaseViewBinding = InAppBillingOneTimePurchaseViewBinding.inflate(layoutInflater)

        return inAppBillingOneTimePurchaseViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOneTimePurchaseUI()

        billingClient = billingClientBuilder.setListener { billingResult, mutableList ->

        }.enablePendingPurchases().build()

        billingClient.startConnection(object : BillingClientStateListener {

            override fun onBillingServiceDisconnected() {

                purchaseFlowController.purchaseFlowDisrupted(null)
            }

            override fun onBillingSetupFinished(billingResult: BillingResult?) {

                val skuDetailsParams = SkuDetailsParams.newBuilder()
                        .setSkusList(listOfItems)
                        .setType(BillingClient.SkuType.INAPP)
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

                            purchaseFlowController.purchaseFlowSucceeded(skuDetails = skuDetailsListInApp[0])

                            if (skuDetailsListInApp.isNotEmpty()) {

                                inAppBillingOneTimePurchaseViewBinding.itemTitleView.text = (listOfItems[0].convertToItemTitle())

                                val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
                                firebaseRemoteConfig.setConfigSettingsAsync(FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(0).build())
                                firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_default)
                                firebaseRemoteConfig.fetchAndActivate().addOnSuccessListener {

                                    oneTimePurchasePurchaseFlow(skuDetailsListInApp[0])

                                    inAppBillingOneTimePurchaseViewBinding
                                            .itemDescriptionView.text = Html.fromHtml(firebaseRemoteConfig.getString(listOfItems[0].convertToRemoteConfigDescriptionKey()))

                                    (inAppBillingOneTimePurchaseViewBinding
                                            .centerPurchaseButton.root as MaterialButton).text = firebaseRemoteConfig.getString(listOfItems[0].convertToRemoteConfigPriceInformation())
                                    (inAppBillingOneTimePurchaseViewBinding
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