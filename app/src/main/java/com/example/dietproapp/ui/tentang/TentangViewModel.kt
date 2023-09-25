package com.example.dietproapp.ui.tentang

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TentangViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is order Fragment"
    }
    val text: LiveData<String> = _text
}