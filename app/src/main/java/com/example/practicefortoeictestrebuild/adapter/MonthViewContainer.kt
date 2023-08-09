package com.example.practicefortoeictestrebuild.adapter

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.practicefortoeictestrebuild.R
import com.kizitonwose.calendar.view.ViewContainer

class MonthViewContainer(view: View) : ViewContainer(view) {
    val dayTitlesContainer: LinearLayout = view.findViewById(R.id.txt_day)
    val monthTitleContainer: TextView = view.findViewById(R.id.txt_month)
}