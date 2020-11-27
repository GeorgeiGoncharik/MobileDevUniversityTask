package xyz.goshanchik.prodavayka.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import xyz.goshanchik.prodavayka.data.Repository
import xyz.goshanchik.prodavayka.data.database.CommerceRoomDatabase
import xyz.goshanchik.prodavayka.util.NetManager

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository =
        Repository(CommerceRoomDatabase.getDatabase(application), NetManager(application))

    val categories = repository.allCategories

    val categoriesFromFlow = repository.categoriesUsingFlow.asLiveData()

    val recents = repository.allRecents

    private val _navigate = MutableLiveData<Int?>()
    val navigate: LiveData<Int?>
        get() = _navigate

    fun navigateCategoryActivity(categoryId: Int){
        _navigate.value = categoryId
    }

    fun onNavigateCategoryActivity(){
        _navigate.value = null
    }


    fun refreshDataFromRepository() {
        viewModelScope.launch {
            repository.refreshCategories()
        }
    }
}