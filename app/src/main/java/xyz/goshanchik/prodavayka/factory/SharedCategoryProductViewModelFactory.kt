package xyz.goshanchik.prodavayka.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.goshanchik.prodavayka.viewmodel.SharedCategoryProductViewModel
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class SharedCategoryProductViewModelFactory(val categoryId: Int, val productId: Long, val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedCategoryProductViewModel::class.java))
            return SharedCategoryProductViewModel(categoryId, productId, application) as T
        throw IllegalArgumentException("no viewModel class found.")
    }
}