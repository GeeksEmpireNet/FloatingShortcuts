/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 3/24/20 1:15 PM
 * Last modified 3/24/20 10:35 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geekstools.floatshort.PRO.Widgets.RoomDatabase

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import net.geekstools.floatshort.PRO.Utils.Functions.PublicVariable

@Entity(tableName = PublicVariable.WIDGET_DATA_DATABASE_NAME)
data class WidgetDataModel(
        @NonNull @PrimaryKey var WidgetNumber: Long,

        @NonNull var WidgetId: Int,

        @NonNull @ColumnInfo(name = "PackageName") var PackageName: String,
        @NonNull @ColumnInfo(name = "ClassNameProvider") var ClassNameProvider: String,
        @Nullable @ColumnInfo(name = "ConfigClassName") var ConfigClassName: String?,

        @NonNull @ColumnInfo(name = "AppName") var AppName: String,
        @ColumnInfo(name = "WidgetLabel") var WidgetLabel: String?,

        @ColumnInfo(name = "Recovery") var Recovery: Boolean?
)