package com.enigma.myapplication.di

import android.content.Context
import com.enigma.myapplication.MyApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(SingletonComponent::class)
object AppLevelObjects {

    @Provides
    fun scope(@ApplicationContext application: Context): CoroutineScope {
        return (application as MyApplication).applicationScope
    }

}