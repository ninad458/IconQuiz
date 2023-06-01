package com.enigma.myapplication.extensions

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("app:url")
fun ImageView.setUrl(url: String?) {
    Log.d("xzxzxz", "setUrl: $url")
    Glide.with(context).load(url).into(this)
}
