/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 3/24/20 1:15 PM
 * Last modified 3/24/20 10:39 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geekstools.floatshort.PRO.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import net.geekstools.floatshort.PRO.Automation.RecoveryWifi;
import net.geekstools.floatshort.PRO.Folders.FloatingServices.Folder_Unlimited_Wifi;
import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Shortcuts.FloatingServices.App_Unlimited_Wifi;
import net.geekstools.floatshort.PRO.Utils.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Utils.Functions.FunctionsClassDebug;
import net.geekstools.floatshort.PRO.Utils.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Utils.UI.CustomIconManager.LoadCustomIcons;

public class ReceiverWifi extends BroadcastReceiver {

    FunctionsClass functionsClass;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            functionsClass = new FunctionsClass(context);

            if (functionsClass.loadCustomIcons()) {
                LoadCustomIcons loadCustomIcons = new LoadCustomIcons(context, functionsClass.customIconPackageName());
                loadCustomIcons.load();
                FunctionsClassDebug.Companion.PrintDebug("*** Total Custom Icon ::: " + loadCustomIcons.getTotalIconsNumber());
            }

            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifiManager.isWifiEnabled() == true && PublicVariable.receiveWiFi == false) {
                if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
                    Intent wifi = new Intent(context, RecoveryWifi.class);
                    wifi.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startService(wifi);
                    PublicVariable.receiveWiFi = true;
                }
            } else if (wifiManager.isWifiEnabled() == false) {
                if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED) {
                    Intent w = new Intent(context, App_Unlimited_Wifi.class);
                    w.putExtra("pack", context.getString(R.string.remove_all_floatings));
                    w.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startService(w);

                    Intent c = new Intent(context, Folder_Unlimited_Wifi.class);
                    c.putExtra("categoryName", context.getString(R.string.remove_all_floatings));
                    c.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startService(c);

                    PublicVariable.receiveWiFi = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
