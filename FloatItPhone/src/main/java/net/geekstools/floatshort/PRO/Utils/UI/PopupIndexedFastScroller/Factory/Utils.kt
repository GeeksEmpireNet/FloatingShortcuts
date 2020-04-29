/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 4/29/20 1:53 PM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geekstools.floatshort.PRO.Utils.UI.PopupIndexedFastScroller.Factory

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

fun Float.convertToDp(context: Context) : Int {

    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            this@convertToDp,
            context.resources.displayMetrics).toInt()
}

fun calculateStatusBarHeight(resources: Resources) : Int {
    var navigationBarHeight = 0

    val resourceIdNavigationBar: Int = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    if (resourceIdNavigationBar > 0) {
        navigationBarHeight = resources.getDimensionPixelSize(resourceIdNavigationBar)
    }

    return navigationBarHeight
}


fun calculateNavigationBarHeight(resources: Resources) : Int {
    var statusBarHeight = 0

    val resourceIdStatusBar: Int = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceIdStatusBar > 0) {
        statusBarHeight = resources.getDimensionPixelSize(resourceIdStatusBar)
    }

    return statusBarHeight
}