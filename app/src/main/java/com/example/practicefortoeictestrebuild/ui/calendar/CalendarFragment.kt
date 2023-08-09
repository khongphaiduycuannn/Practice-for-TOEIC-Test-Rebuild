package com.example.practicefortoeictestrebuild.ui.calendar

import android.view.View
import android.widget.TextView
import androidx.core.view.children
import com.example.practicefortoeictestrebuild.MyApplication
import com.example.practicefortoeictestrebuild.R
import com.example.practicefortoeictestrebuild.adapter.DayViewContainer
import com.example.practicefortoeictestrebuild.adapter.MonthViewContainer
import com.example.practicefortoeictestrebuild.api.ApiHelper
import com.example.practicefortoeictestrebuild.api.ApiResponse
import com.example.practicefortoeictestrebuild.api.ApiService
import com.example.practicefortoeictestrebuild.base.BaseFragment
import com.example.practicefortoeictestrebuild.databinding.FragmentCalendarBinding
import com.example.practicefortoeictestrebuild.model.QuestionCardDaily
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

class CalendarFragment : BaseFragment<FragmentCalendarBinding>(FragmentCalendarBinding::inflate) {

    val calendarView by lazy { binding.calendarView }

    override fun initData() {

    }

    override fun handleEvent() {
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)
        val endMonth = currentMonth.plusMonths(100)
        val daysOfWeek = daysOfWeek(DayOfWeek.SUNDAY)
        val apiService = ApiHelper.getInstance().create(ApiService::class.java)

        calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)

            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.txtDay.text = data.date.dayOfMonth.toString()
                if (data.position != DayPosition.MonthDate) {
                    container.flDayLayout.alpha = 0.0F
                    return
                }

                if (data.date <= LocalDate.now()) {
                    container.txtCountQuestion.alpha = 0.0F
                    apiService.getDailyQuestionCard(
                        MyApplication.getToken(),
                        data.date.year.toString(),
                        data.date.month.value.toString(),
                        data.date.dayOfMonth.toString()
                    ).enqueue(object : Callback<ApiResponse<MutableList<QuestionCardDaily>>> {
                        override fun onResponse(
                            call: Call<ApiResponse<MutableList<QuestionCardDaily>>>,
                            response: Response<ApiResponse<MutableList<QuestionCardDaily>>>
                        ) {
                            var count = 0
                            if (response.isSuccessful && response.body()?.data != null) {
                                val data = response.body()?.data!!
                                if (data.isNotEmpty())
                                    count = data.first().cards.size
                            }
                            if (count > 0) {

                                container.txtCountQuestion.text = "$count"
                                container.txtCountQuestion.alpha = 1.0F
                            }
                        }

                        override fun onFailure(
                            call: Call<ApiResponse<MutableList<QuestionCardDaily>>>,
                            t: Throwable
                        ) {

                        }
                    })
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

        calendarView.setup(startMonth, endMonth, daysOfWeek.first())
        calendarView.scrollToMonth(currentMonth)
        calendarView.setOnTouchListener { _, _ -> true }
    }

    override fun bindData() {

    }
}