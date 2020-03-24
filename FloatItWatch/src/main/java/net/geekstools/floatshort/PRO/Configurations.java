/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 3/24/20 1:15 PM
 * Last modified 3/24/20 10:35 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geekstools.floatshort.PRO;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.wearable.activity.WearableActivity;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;

import net.geekstools.floatshort.PRO.Shortcuts.ListApplicationsView;
import net.geekstools.floatshort.PRO.Utils.Functions.FunctionsClass;

import io.fabric.sdk.android.Fabric;

public class Configurations extends WearableActivity {

    FunctionsClass functionsClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(getApplicationContext());

        CrashlyticsCore crashlyticsCore = new CrashlyticsCore.Builder()
                .disabled(BuildConfig.DEBUG)
                .build();
        Fabric.with(this, new Crashlytics.Builder().core(crashlyticsCore).build());

        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());

        setAmbientEnabled();
        functionsClass = new FunctionsClass(getApplicationContext(), Configurations.this);
        functionsClass.loadSavedColor();

        try {
            if (!BuildConfig.DEBUG) {
                functionsClass.savePreference(".UserInformation", "isBetaTester", functionsClass.appVersionName(getPackageName()).equals("[BETA]") ? true : false);
                functionsClass.savePreference(".UserInformation", "installedVersionCode", functionsClass.appVersionCode(getPackageName()));
                functionsClass.savePreference(".UserInformation", "installedVersionName", functionsClass.appVersionName(getPackageName()));
                functionsClass.savePreference(".UserInformation", "deviceModel", functionsClass.getDeviceName());
                functionsClass.savePreference(".UserInformation", "userRegion", functionsClass.getCountryIso());

                if (functionsClass.appVersionName(getPackageName()).equals("[BETA]")) {
                    functionsClass.saveDefaultPreference("JoinedBetaProgrammer", true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] Permissions = {
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.INTERNET,
                Manifest.permission.VIBRATE
        };
        requestPermissions(Permissions, 666);
        if (!Settings.canDrawOverlays(getApplicationContext())) {
            startActivity(new Intent(getApplicationContext(), PermissionDialogue.class));
        } else {
            Intent listViewOff = new Intent(getApplicationContext(), ListApplicationsView.class);
            listViewOff.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(listViewOff);
        }
        functionsClass.updateRecoverShortcuts();
        finish();
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        if (isAmbient()) {
        } else {
        }
    }
}
