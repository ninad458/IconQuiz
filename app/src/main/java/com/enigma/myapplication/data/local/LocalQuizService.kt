package com.enigma.myapplication.data.local

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Type
import javax.inject.Inject


class LocalQuizService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val scope: CoroutineScope
) {

    private var listQuizData: List<QuizDto> = emptyList()

    private var dataActive: MutableMap<String, QuizDto> = mutableMapOf()

    private val mutex = Mutex()

    init {
        scope.launch {
            initialiseData()
        }
    }

    suspend fun getNextQuiz(): QuizDto {
        initialiseData()
        mutex.withLock {
            if (dataActive.isEmpty()) {
                repopulateData()
            }
            val entry: MutableMap.MutableEntry<String, QuizDto> = dataActive.entries.firstOrNull()
                ?: throw IllegalStateException("Something went wrong. Data probably not present")
            return entry.value
        }
    }

    private suspend fun initialiseData() {
        mutex.withLock(Dispatchers.IO) {
            if (listQuizData.isNotEmpty()) return@withLock
            val quizData = loadData("quiz.json")
            val typeQuizDto: Type = object : TypeToken<List<QuizDto>>() {}.type
            listQuizData = Gson().fromJson(quizData, typeQuizDto)
            dataActive = listQuizData.associateBy { it.name }.toMutableMap()
        }
    }

    private fun repopulateData() {
        dataActive += listQuizData.associateBy { it.name }.toMutableMap()
    }

    suspend fun submit(name: String, value: String) {
        mutex.withLock {
            val quiz =
                dataActive.get(name)
                    ?: throw IllegalStateException("Quiz with this name doesn't exist")
            if (quiz.name != value) {
                throw IllegalStateException("Value doesn't match the name")
            }
            dataActive.remove(name)
        }
    }

    private fun loadData(inFile: String): String {
        var tContents: String = ""
        try {
            val stream: InputStream = context.assets.open(inFile)
            val size: Int = stream.available()
            val buffer = ByteArray(size)
            stream.read(buffer)
            stream.close()
            tContents = String(buffer)
        } catch (e: IOException) {
            // Handle exceptions here
        }
        return tContents
    }
}