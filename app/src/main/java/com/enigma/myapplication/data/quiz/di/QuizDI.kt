package com.enigma.myapplication.data.quiz.di

import android.content.Context
import com.enigma.myapplication.data.quiz.QuizDataRepository
import com.enigma.myapplication.data.quiz.local.LocalQuizService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(ViewModelComponent::class)
object QuizDI {

    @Provides
    fun quizLocalService(
        @ApplicationContext context: Context,
        scope: CoroutineScope
    ): LocalQuizService {
        return LocalQuizService.getInstance(context, scope)
    }

    @Provides
    fun quizDataRepository(localQuizService: LocalQuizService): QuizDataRepository {
        return QuizDataRepository.getInstance(localQuizService)
    }
}