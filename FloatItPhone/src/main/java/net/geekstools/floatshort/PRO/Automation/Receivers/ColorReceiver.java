/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 4/25/20 5:52 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geekstools.floatshort.PRO.Automation.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import net.geekstools.floatshort.PRO.Utils.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Utils.Functions.FunctionsClassDebug;

public class ColorReceiver extends BroadcastReceiver {
    Context context;

    @Override
    public void onReceive(Context colorContext, Intent intent) {
        context = colorContext;
        FunctionsClassDebug.Companion.PrintDebug("Wallpaper Changed".toUpperCase());

        new ExtractColor().execute();
    }

    private class ExtractColor extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            FunctionsClass functionsClass = new FunctionsClass(context);
            functionsClass.extractWallpaperColor();
            return null;
        }
    }
}