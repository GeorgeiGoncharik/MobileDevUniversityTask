package xyz.goshanchik.prodavayka.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import xyz.goshanchik.prodavayka.R

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val preferences = application.getSharedPreferences(application.resources.getString(R.string.preference_cart), Context.MODE_PRIVATE)

    init {
        val productsId = preferences.all

    }
}