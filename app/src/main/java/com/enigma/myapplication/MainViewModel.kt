package com.enigma.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.enigma.myapplication.data.QuizData
import com.enigma.myapplication.data.QuizDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val quizDataRepository: QuizDataRepository
) : ViewModel() {

    private val _refreshData = MutableLiveData<Unit>(Unit)

    private val _quizObj: LiveData<QuizData> = Transformations.map(_refreshData) {
        quizDataRepository.fetchNewData()
    }

    val options = Transformations.map(_quizObj) {
        it.options
    }

    val quizLogo: LiveData<String> = Transformations.map(_quizObj) {
        it.imageUrl
    }

    private val _quizAnsAttempt = MutableLiveData<String>()
    val quizAnsAttempt: LiveData<String> get() = _quizAnsAttempt

    val allowSubmit: LiveData<Boolean> = Transformations.map(_quizAnsAttempt) {
        val quizAttemptAns = _quizAnsAttempt.value
        if (quizAttemptAns.isNullOrEmpty()) {
            return@map false
        }

        return@map quizAttemptAns.length == _quizObj.value?.answer.orEmpty().length
    }


    fun addEntry(c: Char) {
        _quizAnsAttempt.postValue(quizAnsAttempt.value + c)
    }

    fun clear() {
        _quizAnsAttempt.postValue("")
    }

    fun submit() {
        try {
            // show Progress
            quizDataRepository.submit(_quizObj.value ?: return, _quizAnsAttempt.value ?: return)
            _refreshData.postValue(Unit)
        } catch (e: Exception) {
            // show Error
        } finally {
            // hide progress
        }
    }
}