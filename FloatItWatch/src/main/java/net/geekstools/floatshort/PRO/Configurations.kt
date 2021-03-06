/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 4/27/20 7:22 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geekstools.floatshort.PRO

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.wearable.activity.WearableActivity
import android.util.TypedValue
import com.google.firebase.FirebaseApp
import net.geekstools.floatshort.PRO.Shortcuts.ApplicationsViewWatch
import net.geekstools.floatshort.PRO.Utils.Functions.FunctionsClass
import net.geekstools.floatshort.PRO.Utils.Functions.PublicVariable

class Configurations : WearableActivity() {

    private companion object {
        const val RuntimePermissionRequestCode = 666
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(applicationContext)

        val functionsClass = FunctionsClass(applicationContext)
        functionsClass.loadSavedColor()

        if (!BuildConfig.DEBUG) {
            functionsClass.savePreference(".UserInformation", "isBetaTester", if (functionsClass.appVersionName(packageName) == "[BETA]") true else false)
            functionsClass.savePreference(".UserInformation", "installedVersionCode", functionsClass.appVersionCode(packageName))
            functionsClass.savePreference(".UserInformation", "installedVersionName", functionsClass.appVersionName(packageName))
            functionsClass.savePreference(".UserInformation", "deviceModel", functionsClass.deviceName)
            functionsClass.savePreference(".UserInformation", "userRegion", functionsClass.countryIso)

            if (functionsClass.appVersionName(packageName) == "[BETA]") {
                functionsClass.saveDefaultPreference("JoinedBetaProgrammer", true)
            }
        }

        PublicVariable.floatingSizeNumber = 31
        PublicVariable.floatingViewsHW = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PublicVariable.floatingSizeNumber.toFloat(), resources.displayMetrics).toInt()

        val sharedPreferences = getSharedPreferences("theme", Context.MODE_PRIVATE)
        if (!sharedPreferences.getBoolean("hide", false)) {
            PublicVariable.transparencyEnabled = false
        } else if (sharedPreferences.getBoolean("hide", false)) {
            PublicVariable.transparencyEnabled = true
        }

        val allPermissions = arrayOf(
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.INTERNET,
                Manifest.permission.VIBRATE
        )
        requestPermissions(allPermissions, Configurations.RuntimePermissionRequestCode)

        if (!Settings.canDrawOverlays(applicationContext)) {

            startActivity(Intent(applicationContext, PermissionDialogue::class.java))

        } else {

            functionsClass.updateRecoverShortcuts()

            val applicationsViewWatch = Intent(applicationContext, ApplicationsViewWatch::class.java)
            applicationsViewWatch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(applicationsViewWatch)
        }

        finish()
    }
}