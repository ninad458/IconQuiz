package com.enigma.myapplication.data

import com.enigma.myapplication.data.local.LocalQuizService
import javax.inject.Inject


class QuizDataRepository @Inject constructor(private val localQuizService: LocalQuizService) {

    suspend fun fetchNewData(): QuizData {
        val nextQuiz = localQuizService.getNextQuiz()

        val options: List<Char> = generateOptions(nextQuiz.name)

        return QuizData(nextQuiz.imgUrl, nextQuiz.name, options)
    }

    private fun generateOptions(name: String): List<Char> {
        val chars = name.toCharArray().toList()
        val size = chars.size

        val charPool = ('A'..'Z')
        val random: List<Char> = buildList<Char> {
            repeat(OPTIONS_LIMIT - size) {
                add(charPool.random())
            }
        }
        return (random + chars).shuffled()
    }

    suspend fun submit(quizData: QuizData, s: String) {
        localQuizService.submit(quizData.answer, s)
    }

    companion object {
        private const val OPTIONS_LIMIT = 16
    }
}