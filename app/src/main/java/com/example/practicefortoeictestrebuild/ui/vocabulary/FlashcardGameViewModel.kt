package com.example.practicefortoeictestrebuild.ui.vocabulary

import androidx.lifecycle.MutableLiveData
import com.example.practicefortoeictestrebuild.base.BaseViewModel
import java.time.LocalDateTime

class FlashcardGameViewModel : BaseViewModel() {

    var startTime: LocalDateTime? = null

    var endTime: LocalDateTime? = null

    val totalCount = MutableLiveData<Int>().apply {
        value = 0
    }

    val correctCount = MutableLiveData<Int>().apply {
        value = 0
    }

    val incorrectCount = MutableLiveData<Int>().apply {
        value = 0
    }
}