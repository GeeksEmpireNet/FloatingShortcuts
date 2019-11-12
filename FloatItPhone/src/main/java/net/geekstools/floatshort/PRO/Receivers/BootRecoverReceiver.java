/*
 * Copyright © 2019 By Geeks Empire.
 *
 * Created by Elias Fazel on 11/11/19 7:18 PM
 * Last modified 11/11/19 7:16 PM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geekstools.floatshort.PRO.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;

import androidx.preference.PreferenceManager;

import net.geekstools.floatshort.PRO.BindServices;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClassDebug;
import net.geekstools.floatshort.PRO.Util.RemoteTask.RecoveryFolders;
import net.geekstools.floatshort.PRO.Util.RemoteTask.RecoveryShortcuts;
import net.geekstools.floatshort.PRO.Util.UI.CustomIconManager.LoadCustomIcons;

public class BootRecoverReceiver extends BroadcastReceiver {

    FunctionsClass functionsClass;

    @Override
    public void onReceive(final Context context, Intent intent) {
        functionsClass = new FunctionsClass(context);
        functionsClass.savePreference("WidgetsInformation", "Reallocated", false);

        try {
            if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
                if (functionsClass.ControlPanel()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService(new Intent(context, BindServices.class));
                    } else {
                        context.startService(new Intent(context, BindServices.class));
                    }
                }

                if (functionsClass.loadCustomIcons()) {
                    LoadCustomIcons loadCustomIcons = new LoadCustomIcons(context, functionsClass.customIconPackageName());
                    loadCustomIcons.load();
                    FunctionsClassDebug.Companion.PrintDebug("*** Total Custom Icon ::: " + loadCustomIcons.getTotalIcons());
                }

                SharedPreferences sharedPrefBoot = PreferenceManager.getDefaultSharedPreferences(context);
                String boot = sharedPrefBoot.getString("boot", "1");
                if (boot.equals("0")) {
                } else if (boot.equals("1")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent shortcutsRecovery = new Intent(context, RecoveryShortcuts.class);
                            shortcutsRecovery.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                context.startForegroundService(shortcutsRecovery);
                            } else {
                                context.startService(shortcutsRecovery);
                            }
                        }
                    }, 1000);
                } else if (boot.equals("2")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent categoryRecovery = new Intent(context, RecoveryFolders.class);
                            categoryRecovery.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                context.startForegroundService(categoryRecovery);
                            } else {
                                context.startService(categoryRecovery);
                            }
                        }
                    }, 3000);
                } else if (boot.equals("3")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent shortcutsRecovery = new Intent(context, RecoveryShortcuts.class);
                            shortcutsRecovery.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                context.startForegroundService(shortcutsRecovery);
                            } else {
                                context.startService(shortcutsRecovery);
                            }
                        }
                    }, 1000);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent categoryRecovery = new Intent(context, RecoveryFolders.class);
                            categoryRecovery.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                context.startForegroundService(categoryRecovery);
                            } else {
                                context.startService(categoryRecovery);
                            }
                        }
                    }, 3000);
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
