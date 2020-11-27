package xyz.goshanchik.prodavayka.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import xyz.goshanchik.prodavayka.data.Repository
import xyz.goshanchik.prodavayka.data.database.CommerceRoomDatabase
import xyz.goshanchik.prodavayka.data.domain.CartItem
import xyz.goshanchik.prodavayka.util.ListState
import xyz.goshanchik.prodavayka.util.NetManager

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val repository =
        Repository(CommerceRoomDatabase.getDatabase(application), NetManager(application))

    val cartItems: LiveData<List<CartItem>> = repository.allCartItems

    val totalCartItems = Transformations.map(cartItems){ it.sumOf { s -> s.quantity } }

    val totalPrice = Transformations.map(cartItems){it.sumOf { s -> s.product.fullPrice.toDouble() * s.quantity } }

    val listState = Transformations.map(cartItems){if(it.isNotEmpty()) ListState.OK else ListState.EMPTY}

    fun deleteItem(idCartItem: Long){
        viewModelScope.launch {
            repository.deleteCartItem(idCartItem)
        }
    }

    fun increaseQuantity(idCartItem: Long){
        viewModelScope.launch {
            repository.updateCartItem(idCartItem, 1)
        }
    }

    fun decreaseQuantity(idCartItem: Long){
        viewModelScope.launch {
            repository.updateCartItem(idCartItem, -1)
        }
    }
}