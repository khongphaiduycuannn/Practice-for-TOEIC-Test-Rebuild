package com.example.practicefortoeictestrebuild.ui.vocabulary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.practicefortoeictestrebuild.MyApplication
import com.example.practicefortoeictestrebuild.api.ApiHelper
import com.example.practicefortoeictestrebuild.api.ApiResponse
import com.example.practicefortoeictestrebuild.api.ApiService
import com.example.practicefortoeictestrebuild.base.BaseViewModel
import com.example.practicefortoeictestrebuild.base.DataResult
import com.example.practicefortoeictestrebuild.model.DataOverview
import com.example.practicefortoeictestrebuild.model.FlashCard
import com.example.practicefortoeictestrebuild.model.TopicVocabulary
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class VocabularyViewModel : BaseViewModel() {

    private val _topicId = MutableLiveData<String>().apply {
        value = ""
    }

    private val _topicName = MutableLiveData<String>().apply {
        value = ""
    }

    private val _allCard = MutableLiveData<MutableList<FlashCard>>().apply {
        value = mutableListOf()
    }

    private val _newCard = MutableLiveData<MutableList<FlashCard>>().apply {
        value = mutableListOf()
    }

    private val _memorizedCard = MutableLiveData<MutableList<FlashCard>>().apply {
        value = mutableListOf()
    }

    private val _unmemorizedCard = MutableLiveData<MutableList<FlashCard>>().apply {
        value = mutableListOf()
    }

    private val _listCard = MutableLiveData<MutableList<FlashCard>>().apply {
        value = mutableListOf()
    }

    val allCard: LiveData<MutableList<FlashCard>> get() = _allCard

    val newCard: LiveData<MutableList<FlashCard>> get() = _newCard

    val memorizedCard: LiveData<MutableList<FlashCard>> get() = _memorizedCard

    val unmemorizedCard: LiveData<MutableList<FlashCard>> get() = _unmemorizedCard

    val listCard: LiveData<MutableList<FlashCard>> get() = _listCard

    val topicId: LiveData<String> get() = _topicId

    val topicName: LiveData<String> get() = _topicName

    fun setTopicId(id: String?) {
        _topicId.value = id
    }

    suspend fun getFlashCard(): DataResult<MutableList<FlashCard>> {
        val apiService = ApiHelper.getInstance().create(ApiService::class.java)

        val allCard = mutableListOf<FlashCard>()
        val newCard = mutableListOf<FlashCard>()
        val memorizedCard = mutableListOf<FlashCard>()
        val unmemorizedCard = mutableListOf<FlashCard>()

        val x = suspendCoroutine { continuation ->
            apiService.getFlashCard(
                MyApplication.getToken(),
                topicId.value
            ).enqueue(object : Callback<ApiResponse<TopicVocabulary>> {
                override fun onResponse(
                    call: Call<ApiResponse<TopicVocabulary>>,
                    response: Response<ApiResponse<TopicVocabulary>>
                ) {
                    if (response.isSuccessful && response.body()?.data != null) {
                        val list = response.body()?.data!!.cards
                        list.forEach {
                            allCard.add(it)
                        }
                        continuation.resume(1)
                    } else continuation.resume(0)
                }

                override fun onFailure(
                    call: Call<ApiResponse<TopicVocabulary>>,
                    t: Throwable
                ) {
                    continuation.resume(0)
                }
            })
        }

        val y = suspendCoroutine { continuation ->
            apiService.getProgressCard(
                MyApplication.getToken(),
                topicId.value
            ).enqueue(object : Callback<ApiResponse<MutableList<DataOverview>>> {
                override fun onResponse(
                    call: Call<ApiResponse<MutableList<DataOverview>>>,
                    response: Response<ApiResponse<MutableList<DataOverview>>>
                ) {
                    if (response.isSuccessful && response.body()?.data != null) {
                        val progress = response.body()?.data
                        allCard.forEach { card ->
                            var flag = false
                            progress?.forEach { data ->
                                if (card.id == data.cardId && data.status == 1) {
                                    unmemorizedCard.add(card)
                                    flag = true
                                } else if (card.id == data.cardId && data.status == 2) {
                                    memorizedCard.add(card)
                                    flag = true
                                }
                            }
                            if (!flag) newCard.add(card)
                        }
                        continuation.resume(1)
                    } else continuation.resume(0)
                }

                override fun onFailure(
                    call: Call<ApiResponse<MutableList<DataOverview>>>,
                    t: Throwable
                ) {
                    continuation.resume(0)
                }
            })
        }

        _allCard.value = allCard
        _newCard.value = newCard
        _memorizedCard.value = memorizedCard
        _unmemorizedCard.value = unmemorizedCard
        return DataResult.Success(allCard)
    }

    fun getData() {
        executeTask(
            request = {
                getFlashCard()
            },
            onSuccess = {
                _listCard.value = it
            },
            onError = {
            }
        )
    }

    fun setListCard(selectedCategory: String?) {
        when (selectedCategory) {
            "Default" -> _listCard.value = _allCard.value
            "New" -> _listCard.value = _newCard.value
            "Memorized" -> _listCard.value = _memorizedCard.value
            "Unmemorized" -> _listCard.value = _unmemorizedCard.value
        }
    }
}