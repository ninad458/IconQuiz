package com.enigma.myapplication.data.local

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Type
import javax.inject.Inject


class LocalQuizService @Inject constructor(@ApplicationContext private val context: Context) {

    private val listQuizData: List<QuizDto>

    private val dataActive: MutableMap<String, QuizDto>

    init {
        val quizData = loadData("quiz.json")
        val typeQuizDto: Type = object : TypeToken<List<QuizDto>>() {}.type
        listQuizData = Gson().fromJson(quizData, typeQuizDto)
        dataActive = listQuizData.associateBy { it.name }.toMutableMap()
    }

    fun getNextQuiz(): QuizDto {
        if (dataActive.isEmpty()) {
            repopulateData()
        }
        val entry: MutableMap.MutableEntry<String, QuizDto> = dataActive.entries.firstOrNull()
            ?: throw IllegalStateException("Something went wrong. Data probably not present")

        return entry.value
    }

    private fun repopulateData() {
        dataActive += listQuizData.associateBy { it.name }.toMutableMap()
    }

    fun submit(name: String, value: String) {
        val quiz =
            dataActive.get(name) ?: throw IllegalStateException("Quiz with this name doesn't exist")
        if (quiz.name != value) {
            throw IllegalStateException("Value doesn't match the name")
        }
        dataActive.remove(name)
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