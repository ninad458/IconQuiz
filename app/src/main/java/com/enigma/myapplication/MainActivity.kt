package com.enigma.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.quizLogo.observe(this, {

        })
    }
}

/*
* Glide
* Retrofit
* Shared Preference
* View Model
* Dagger
* Data Binding
* */

/*
* add json to assets
* data layer/module
* fetch list from data
* data format?
* In-memory list to store these records
* Pop them to get the latest one, mark as done in shared preference once answer submitted and remove from in-memory list
*
*
* UI
* ViewModel will give image and keys
* Keys Generation logic at data layer
* Disable keys clicked
* Clear button
* Enter will submit
* Loader
* Get new data
* Reset state
*
* Error screens for all calls
*
*
*
* */