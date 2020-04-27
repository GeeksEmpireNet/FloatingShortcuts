/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 4/27/20 6:52 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geekstools.floatshort.PRO.Utils.AdapterItemsData

import android.graphics.Color
import android.graphics.drawable.Drawable

data class AdapterItemsApplications(var AppName: String,
                                    var PackageName: String,
                                    var ClassName: String,
                                    var AppIcon: Drawable,
                                    var AppIconDominantColor: Int = Color.BLUE)