package com.enigma.myapplication.data.quiz

import com.enigma.myapplication.data.quiz.local.LocalQuizService


interface QuizDataRepository {

    suspend fun fetchNewData(): QuizData

    suspend fun submit(quizData: QuizData, s: String)

    companion object {

        fun getInstance(localQuizService: LocalQuizService): QuizDataRepository {
            return QuizDataRepositoryImpl(localQuizService)
        }

        private class QuizDataRepositoryImpl(
            private val localQuizService: LocalQuizService
        ) : QuizDataRepository {
            override suspend fun fetchNewData(): QuizData {
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

            override suspend fun submit(quizData: QuizData, s: String) {
                localQuizService.submit(quizData.answer, s)
            }
        }

        private const val OPTIONS_LIMIT = 16
    }

}