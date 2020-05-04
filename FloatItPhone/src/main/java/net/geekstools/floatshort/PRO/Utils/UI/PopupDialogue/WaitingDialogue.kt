/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel
 * Last modified 5/4/20 7:37 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geekstools.floatshort.PRO.Utils.UI.PopupDialogue

import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.waiting_dialogue.*
import net.geekstools.floatshort.PRO.R
import net.geekstools.floatshort.PRO.Utils.Functions.PublicVariable

class WaitingDialogueLiveData() : ViewModel() {
    val dialogueTitle: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val dialogueMessage: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}

class WaitingDialogue {

    fun initShow(initActivity: AppCompatActivity) : Dialog {
        val waitingDialogueLiveData = ViewModelProvider(initActivity).get(WaitingDialogueLiveData::class.java)

        val layoutParams = WindowManager.LayoutParams()
        val dialogueWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 363f, initActivity.resources.displayMetrics).toInt()
        val dialogueHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100f, initActivity.resources.displayMetrics).toInt()

        layoutParams.width = dialogueWidth
        layoutParams.height = dialogueHeight
        layoutParams.windowAnimations = android.R.style.Animation_Dialog
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.57f

        val dialog = Dialog(initActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.waiting_dialogue)
        dialog.window?.attributes = layoutParams
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
        dialog.setCancelable(true)

        dialog.dialogueView.backgroundTintList = ColorStateList.valueOf(PublicVariable.colorLightDark)
        dialog.waitLoading.setColor(PublicVariable.primaryColor)

        dialog.waitTextTitle.setTextColor(PublicVariable.colorLightDarkOpposite)
        dialog.waitTextMessage.setTextColor(PublicVariable.colorLightDarkOpposite)

        waitingDialogueLiveData.dialogueTitle.observe(initActivity,
                Observer<String> { dialogueTitle ->
                    dialog.waitTextTitle.text = dialogueTitle
                })

        waitingDialogueLiveData.dialogueMessage.observe(initActivity,
                Observer<String> { dialogueMessage ->
                    dialog.waitTextMessage.text = dialogueMessage
                })

        dialog.setOnDismissListener {

            dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }

        if (!initActivity.isFinishing) {
            dialog.show()
        }

        return dialog
    }
}