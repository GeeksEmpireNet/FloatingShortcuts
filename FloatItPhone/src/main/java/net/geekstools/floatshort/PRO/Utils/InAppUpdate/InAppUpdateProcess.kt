/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 4/27/20 4:33 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geekstools.floatshort.PRO.Utils.InAppUpdate

import android.app.ActivityOptions
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.tasks.Task
import net.geekstools.floatshort.PRO.R
import net.geekstools.floatshort.PRO.Utils.Functions.FunctionsClass
import net.geekstools.floatshort.PRO.Utils.Functions.FunctionsClassDebug
import net.geekstools.floatshort.PRO.Utils.Functions.PublicVariable
import net.geekstools.floatshort.PRO.databinding.InAppUpdateViewBinding
import java.util.*

class InAppUpdateProcess : AppCompatActivity() {

    private val functionsClass: FunctionsClass by lazy {
        FunctionsClass(applicationContext)
    }

    private lateinit var appUpdateManager: AppUpdateManager
    private lateinit var installStateUpdatedListener: InstallStateUpdatedListener

    companion object {
        private const val IN_APP_UPDATE_REQUEST = 333
    }

    private lateinit var inAppUpdateViewBinding: InAppUpdateViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inAppUpdateViewBinding = InAppUpdateViewBinding.inflate(layoutInflater)
        setContentView(inAppUpdateViewBinding.root)

        window.statusBarColor = functionsClass.setColorAlpha(PublicVariable.primaryColor, 77f)
        window.navigationBarColor = functionsClass.setColorAlpha(PublicVariable.primaryColor, 77f)

        inAppUpdateViewBinding.fullEmptyView.setBackgroundColor(functionsClass.setColorAlpha(PublicVariable.primaryColor, 77f))
        inAppUpdateViewBinding.inAppUpdateWaiting.setColor(PublicVariable.primaryColorOpposite)

        inAppUpdateViewBinding.textInputChangeLog.boxBackgroundColor = functionsClass.setColorAlpha(PublicVariable.primaryColor, 77f)
        inAppUpdateViewBinding.textInputChangeLog.hintTextColor = ColorStateList.valueOf(getColor(R.color.lighter))
        inAppUpdateViewBinding.textInputChangeLog.hint = "${getString(R.string.inAppUpdateAvailable)} ${intent.getStringExtra("UPDATE_VERSION")}"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            inAppUpdateViewBinding.changeLog.setText(Html.fromHtml(intent.getStringExtra("UPDATE_CHANGE_LOG"), Html.FROM_HTML_MODE_LEGACY))
        } else {
            inAppUpdateViewBinding.changeLog.setText(Html.fromHtml(intent.getStringExtra("UPDATE_CHANGE_LOG")))
        }

        installStateUpdatedListener = InstallStateUpdatedListener {
            when (it.installStatus()) {
                InstallStatus.REQUIRES_UI_INTENT -> {
                    FunctionsClassDebug.PrintDebug("*** UPDATE Requires UI Intent ***")
                }
                InstallStatus.DOWNLOADING -> {
                    FunctionsClassDebug.PrintDebug("*** UPDATE Downloading ***")
                }
                InstallStatus.DOWNLOADED -> {
                    FunctionsClassDebug.PrintDebug("*** UPDATE Downloaded ***")

                    showCompleteConfirmation()
                }
                InstallStatus.INSTALLING -> {
                    FunctionsClassDebug.PrintDebug("*** UPDATE Installing ***")

                }
                InstallStatus.INSTALLED -> {
                    FunctionsClassDebug.PrintDebug("*** UPDATE Installed ***")

                    appUpdateManager.unregisterListener(installStateUpdatedListener)
                }
                InstallStatus.CANCELED -> {
                    FunctionsClassDebug.PrintDebug("*** UPDATE Canceled ***")

                    this@InAppUpdateProcess.finish()
                }
                InstallStatus.FAILED -> {
                    FunctionsClassDebug.PrintDebug("*** UPDATE Failed ***")

                    val inAppUpdateTriggeredTime: Int = "${Calendar.getInstance().get(Calendar.YEAR)}${Calendar.getInstance().get(Calendar.MONTH)}${Calendar.getInstance().get(Calendar.DATE)}".toInt()
                    functionsClass.savePreference("InAppUpdate", "TriggeredDate", inAppUpdateTriggeredTime)
                }
            }
        }
        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        appUpdateManager.registerListener(installStateUpdatedListener)

        val appUpdateInfo: Task<AppUpdateInfo> = appUpdateManager.appUpdateInfo
        appUpdateInfo.addOnSuccessListener { updateInfo ->
            FunctionsClassDebug.PrintDebug("*** Update Availability == ${updateInfo.updateAvailability()} ||| Available Version Code == ${updateInfo.availableVersionCode()} ***")

            if (updateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && updateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {

                appUpdateManager.startUpdateFlowForResult(
                        updateInfo,
                        AppUpdateType.FLEXIBLE,
                        this@InAppUpdateProcess,
                        IN_APP_UPDATE_REQUEST
                )
            } else {
                val inAppUpdateTriggeredTime: Int = "${Calendar.getInstance().get(Calendar.YEAR)}${Calendar.getInstance().get(Calendar.MONTH)}${Calendar.getInstance().get(Calendar.DATE)}".toInt()
                functionsClass.savePreference("InAppUpdate", "TriggeredDate", inAppUpdateTriggeredTime)

                this@InAppUpdateProcess.finish()
            }

        }.addOnFailureListener {
            FunctionsClassDebug.PrintDebug("*** Exception Error ${it} ***")

            val inAppUpdateTriggeredTime: Int = "${Calendar.getInstance().get(Calendar.YEAR)}${Calendar.getInstance().get(Calendar.MONTH)}${Calendar.getInstance().get(Calendar.DATE)}".toInt()
            functionsClass.savePreference("InAppUpdate", "TriggeredDate", inAppUpdateTriggeredTime)
        }

        appUpdateManager.unregisterListener {
            FunctionsClassDebug.PrintDebug("*** Unregister Listener ${it} ***")

            this@InAppUpdateProcess.finish()
        }
    }

    override fun onStart() {
        super.onStart()

        inAppUpdateViewBinding.rateFloatIt.setOnClickListener {

            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.play_store_link) + packageName)),
                    ActivityOptions.makeCustomAnimation(applicationContext, android.R.anim.fade_in, android.R.anim.fade_out).toBundle())
        }

        inAppUpdateViewBinding.pageFloatIt.setOnClickListener {

            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.link_facebook_app))),
                    ActivityOptions.makeCustomAnimation(applicationContext, android.R.anim.fade_in, android.R.anim.fade_out).toBundle())
        }
    }

    override fun onResume() {
        super.onResume()

        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability()
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {

                appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.FLEXIBLE,
                        this@InAppUpdateProcess,
                        IN_APP_UPDATE_REQUEST)
            }

            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                showCompleteConfirmation()
            }
        }
    }

    override fun onPause() {
        super.onPause()

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onBackPressed() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == IN_APP_UPDATE_REQUEST) {
            when (resultCode) {
                RESULT_CANCELED -> {
                    FunctionsClassDebug.PrintDebug("*** RESULT CANCELED ***")

                    val inAppUpdateTriggeredTime: Int = "${Calendar.getInstance().get(Calendar.YEAR)}${Calendar.getInstance().get(Calendar.MONTH)}${Calendar.getInstance().get(Calendar.DATE)}".toInt()
                    functionsClass.savePreference("InAppUpdate", "TriggeredDate", inAppUpdateTriggeredTime)

                    appUpdateManager.unregisterListener(installStateUpdatedListener)
                    this@InAppUpdateProcess.finish()

                    PublicVariable.updateCancelByUser = true
                }
                RESULT_OK -> {
                    FunctionsClassDebug.PrintDebug("*** RESULT OK ***")

                }
                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
                    FunctionsClassDebug.PrintDebug("*** RESULT IN APP UPDATE FAILED ***")

                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun showCompleteConfirmation() {
        FunctionsClassDebug.PrintDebug("*** Complete Confirmation ***")

        val snackbar = Snackbar.make(findViewById<RelativeLayout>(R.id.fullEmptyView),
                getString(R.string.inAppUpdateDescription),
                Snackbar.LENGTH_INDEFINITE)
        snackbar.setBackgroundTint(PublicVariable.colorLightDark)
        snackbar.setTextColor(PublicVariable.colorLightDarkOpposite)
        snackbar.setActionTextColor(PublicVariable.primaryColor)
        snackbar.setAction(Html.fromHtml(getString(R.string.inAppUpdateAction))) { view ->
            appUpdateManager.completeUpdate().addOnSuccessListener {
                FunctionsClassDebug.PrintDebug("*** Complete Update Success Listener ***")

            }.addOnFailureListener {
                FunctionsClassDebug.PrintDebug("*** Complete Update Failure Listener | ${it} ***")

                val inAppUpdateTriggeredTime: Int = "${Calendar.getInstance().get(Calendar.YEAR)}${Calendar.getInstance().get(Calendar.MONTH)}${Calendar.getInstance().get(Calendar.DATE)}".toInt()
                functionsClass.savePreference("InAppUpdate", "TriggeredDate", inAppUpdateTriggeredTime)
            }
        }

        val view = snackbar.view
        val layoutParams = view.layoutParams as FrameLayout.LayoutParams
        layoutParams.gravity = Gravity.BOTTOM
        view.layoutParams = layoutParams

        snackbar.show()
    }
}