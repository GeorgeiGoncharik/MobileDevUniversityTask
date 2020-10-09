package xyz.goshanchik.prodavayka.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import xyz.goshanchik.prodavayka.data.CategoryRepository
import xyz.goshanchik.prodavayka.data.CommerceRoomDatabase
import xyz.goshanchik.prodavayka.data.dao.CategoryDao
import xyz.goshanchik.prodavayka.model.Category

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val categoryDao: CategoryDao
    private val categoryRepository: CategoryRepository
    val categories: LiveData<List<Category>>

    init {
        categoryDao = CommerceRoomDatabase.getDatabase(application, viewModelScope).categoryDao()
        categoryRepository = CategoryRepository(categoryDao)
        categories = categoryRepository.allCategories
    }
}