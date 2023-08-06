package com.example.practicefortoeictestrebuild.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.practicefortoeictestrebuild.R

class PopUpNotification(
    private val context: Context
) {
    companion object {
        const val SUCCESS_COLOR = R.drawable.pop_up_success_notification
        const val ERROR_COLOR = R.drawable.pop_up_error_notification
        const val INFO_COLOR = R.drawable.pop_up_info_notification
        const val WARNING_COLOR = R.drawable.pop_up_warning_notification
    }

    private val view: View =
        LayoutInflater.from(context).inflate(R.layout.pop_up_notification, null)
    private val width = ViewGroup.LayoutParams.MATCH_PARENT
    private val height = ViewGroup.LayoutParams.WRAP_CONTENT

    var title: TextView = view.findViewById(R.id.title)
    var message: TextView = view.findViewById(R.id.message)
    var duration: Long = 1700

    fun setColor(id: Int) {
        val linearContainer: LinearLayout = view.findViewById(R.id.linear_container)
        linearContainer.background =
            ContextCompat.getDrawable(context, id)
    }

    fun show(atView: View) {
        val popupWindow = PopupWindow(view, width, height, false)
        popupWindow.animationStyle = R.style.topDownAnimation
        popupWindow.isOutsideTouchable = false
        popupWindow.showAtLocation(atView, Gravity.TOP, 0, 10)

        Handler(Looper.getMainLooper()).postDelayed(
            {
                popupWindow.dismiss()
            }, duration
        )
    }
}