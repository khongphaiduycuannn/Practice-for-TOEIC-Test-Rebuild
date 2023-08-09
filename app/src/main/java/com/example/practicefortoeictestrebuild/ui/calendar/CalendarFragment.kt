package com.example.practicefortoeictestrebuild.ui.calendar

import android.view.View
import android.widget.TextView
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import com.example.practicefortoeictestrebuild.R
import com.example.practicefortoeictestrebuild.adapter.DayViewContainer
import com.example.practicefortoeictestrebuild.adapter.MonthViewContainer
import com.example.practicefortoeictestrebuild.base.BaseFragment
import com.example.practicefortoeictestrebuild.databinding.FragmentCalendarBinding
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

class CalendarFragment : BaseFragment<FragmentCalendarBinding>(FragmentCalendarBinding::inflate) {

    private val viewModel by lazy {
        activity?.let {
            ViewModelProvider(it)[CalendarViewModel::class.java]
        }
    }

    private val calendarView by lazy { binding.calendarView }

    private val countQuestionsDaily: MutableMap<Int, Int> = mutableMapOf()

    private val daysOfWeek by lazy { daysOfWeek(DayOfWeek.SUNDAY) }

    override fun initData() {
        viewModel?.getDailyQuestionCard()
    }

    override fun handleEvent() {
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)
        val endMonth = currentMonth.plusMonths(100)

        bindDay()
        bindMonth()

        calendarView.setup(startMonth, endMonth, daysOfWeek.first())
        calendarView.scrollToMonth(currentMonth)
        calendarView.setOnTouchListener { _, _ -> true }
    }

    override fun bindData() {
        viewModel?.listQuestionCardDaily?.observe(viewLifecycleOwner) { list ->
            list.forEach {
                countQuestionsDaily[it.day] = it.cards.size
            }
            bindDay()
        }
    }

    private fun bindDay() {
        calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)

            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.txtDay.text = data.date.dayOfMonth.toString()
                if (data.position != DayPosition.MonthDate) {
                    container.flDayLayout.alpha = 0.0F
                    return
                }

                if (countQuestionsDaily[data.date.dayOfMonth] != null) {
                    container.txtCountQuestion.text = "${countQuestionsDaily[data.date.dayOfMonth]}"
                    container.txtCountQuestion.alpha = 1.0F
                }

                if (data.date == LocalDate.now()) {
                    container.txtDay.setTextColor(requireActivity().getColor(R.color.white))
                    container.txtDay.setBackgroundResource(R.drawable.shape_circle_lavender)
                    return
                }
                container.txtDay.setTextColor(requireActivity().getColor(R.color.emperor))
                container.txtDay.setBackgroundResource(R.color.white)
            }
        }
    }

    private fun bindMonth() {
        calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)

            override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                if (container.dayTitlesContainer.tag == null) {
                    container.dayTitlesContainer.tag = data.yearMonth
                    container.monthTitleContainer.text =
                        "${data.yearMonth.month.name} ${data.yearMonth.year}"
                    container.dayTitlesContainer.children.map { it as TextView }
                        .forEachIndexed { index, textView ->
                            val dayOfWeek = daysOfWeek[index]
                            val title =
                                dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                            textView.text = title
                        }
                }
            }
        }
    }
}