package com.example.practicefortoeictestrebuild.ui.vocabulary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.practicefortoeictestrebuild.base.BaseViewModel

class VocabularyViewModel : BaseViewModel() {

    private val _topicId = MutableLiveData<String>().apply {
        value = ""
    }

    val topicId: LiveData<String> get() = _topicId

    fun setTopicId(id: String?) {
        _topicId.value = id
    }
}