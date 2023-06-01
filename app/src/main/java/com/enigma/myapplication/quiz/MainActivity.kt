package com.enigma.myapplication.quiz

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.enigma.myapplication.databinding.ActivityMainBinding
import com.enigma.myapplication.extensions.setUrl
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.dataStore = viewModel

        viewModel.quizLogo.observe(this) {
            binding.imgIcon.setUrl(it)
        }

        val keysList = binding.listKeys

        val keysAdapter = KeysAdapter(viewModel)
        keysList.adapter = keysAdapter

        viewModel.quizAnsAttempt.observe(this) {
            binding.txtQuizInputAns.text = it
        }

        viewModel.allowSubmit.observe(this) {
            binding.btnSubmit.isEnabled = it
        }

        viewModel.options.observe(this) { options ->
            keysAdapter.setData(options)
        }

        binding.btnSubmit.setOnClickListener {
            viewModel.submit()
        }
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