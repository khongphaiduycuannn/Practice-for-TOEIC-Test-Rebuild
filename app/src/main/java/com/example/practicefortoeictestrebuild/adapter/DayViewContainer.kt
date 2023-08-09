package com.example.practicefortoeictestrebuild.adapter

import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.example.practicefortoeictestrebuild.R
import com.kizitonwose.calendar.view.ViewContainer

class DayViewContainer(view: View) : ViewContainer(view) {
    val flDayLayout = view.findViewById<FrameLayout>(R.id.fl_day_layout)
    val txtDay = view.findViewById<TextView>(R.id.txt_day)
    val txtCountQuestion = view.findViewById<TextView>(R.id.txt_count_question)
}