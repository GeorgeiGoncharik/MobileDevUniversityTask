package xyz.goshanchik.prodavayka.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import timber.log.Timber
import xyz.goshanchik.prodavayka.data.Repository
import xyz.goshanchik.prodavayka.data.database.CommerceRoomDatabase
import xyz.goshanchik.prodavayka.util.NetManager
import java.time.LocalDateTime

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository =
        Repository(CommerceRoomDatabase.getDatabase(application, viewModelScope), NetManager(application))


    private val _refreshed: MutableLiveData<LocalDateTime> = MutableLiveData(LocalDateTime.now())
    val refreshed: LiveData<LocalDateTime>
        get() = _refreshed

    init {
        //        refreshDataFromRepository()
    }

    val categories = repository.allCategories

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
            _refreshed.postValue(LocalDateTime.now())
        }
    }
}