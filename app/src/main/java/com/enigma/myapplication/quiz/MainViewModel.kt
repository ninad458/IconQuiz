package com.enigma.myapplication.quiz

import androidx.lifecycle.*
import com.enigma.myapplication.data.QuizData
import com.enigma.myapplication.data.QuizDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val quizDataRepository: QuizDataRepository
) : ViewModel(), KeyClickListener {

    private val _refreshData = MutableLiveData<Unit>(Unit)

    private val _quizObj: LiveData<QuizData> = _refreshData.switchMap {
        return@switchMap liveData {
            try {
                val quizData = quizDataRepository.fetchNewData()
                emit(quizData)
            } catch (e: Exception) {
            }
        }
    }

    private val _disabledKeys = MutableLiveData<Set<Int>>(emptySet())

    val options: LiveData<List<KeyModel>> = MediatorLiveData<List<KeyModel>>().apply {
        fun handleData() {
            val quizData = _quizObj.value ?: return
            postValue(quizData.options.mapIndexed { i, ch ->
                KeyModel(
                    i, ch,
                    _disabledKeys.value.orEmpty().contains(i)
                )
            })
        }
        addSource(_disabledKeys) {
            handleData()
        }

        addSource(_quizObj) {
            handleData()
        }
    }

    val quizLogo: LiveData<String> = _quizObj.map {
        it.imageUrl
    }

    private val _quizAnsAttempt = MutableLiveData<String>()
    val quizAnsAttempt: LiveData<String> get() = _quizAnsAttempt

    val allowSubmit: LiveData<Boolean> = (_quizAnsAttempt).map {
        val quizAttemptAns = _quizAnsAttempt.value
        if (quizAttemptAns.isNullOrEmpty()) {
            return@map false
        }

        return@map quizAttemptAns.length == _quizObj.value?.answer.orEmpty().length
    }


    override fun addEntry(c: KeyModel) {
        _quizAnsAttempt.postValue(quizAnsAttempt.value.orEmpty() + c.text)
        _disabledKeys.postValue(_disabledKeys.value.orEmpty() + c.index)
    }

    fun clear() {
        _quizAnsAttempt.postValue("")
        _disabledKeys.postValue(emptySet())
    }

    fun submit() {
        viewModelScope.launch {
            try {
                // show Progress
                quizDataRepository.submit(
                    _quizObj.value ?: return@launch,
                    _quizAnsAttempt.value ?: return@launch
                )
                _refreshData.postValue(Unit)
                _disabledKeys.postValue(emptySet())
                _quizAnsAttempt.postValue("")
            } catch (e: Exception) {
                // show Error
            } finally {
                // hide progress
            }
        }
    }
}