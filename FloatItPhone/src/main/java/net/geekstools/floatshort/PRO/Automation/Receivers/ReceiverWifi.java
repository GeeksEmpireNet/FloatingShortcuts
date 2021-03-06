/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 8/29/20 3:58 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geekstools.floatshort.PRO.Automation.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import net.geekstools.floatshort.PRO.Automation.RecoveryServices.RecoveryWifi;
import net.geekstools.floatshort.PRO.Folders.FloatingServices.FloatingFoldersForWifi;
import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Shortcuts.FloatingServices.FloatingShortcutsForWifi;
import net.geekstools.floatshort.PRO.Utils.Functions.Debug;
import net.geekstools.floatshort.PRO.Utils.Functions.FunctionsClassLegacy;
import net.geekstools.floatshort.PRO.Utils.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Utils.UI.CustomIconManager.LoadCustomIcons;

public class ReceiverWifi extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            FunctionsClassLegacy functionsClassLegacy = new FunctionsClassLegacy(context);

            if (functionsClassLegacy.customIconsEnable()) {
                LoadCustomIcons loadCustomIcons = new LoadCustomIcons(context, functionsClassLegacy.customIconPackageName());
                loadCustomIcons.load();
                Debug.Companion.PrintDebug("*** Total Custom Icon ::: " + loadCustomIcons.getTotalIconsNumber());
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
                    Intent w = new Intent(context, FloatingShortcutsForWifi.class);
                    w.putExtra(context.getString(R.string.remove_all_floatings), context.getString(R.string.remove_all_floatings));
                    w.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startService(w);

                    Intent c = new Intent(context, FloatingFoldersForWifi.class);
                    c.putExtra(context.getString(R.string.remove_all_floatings), context.getString(R.string.remove_all_floatings));
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
