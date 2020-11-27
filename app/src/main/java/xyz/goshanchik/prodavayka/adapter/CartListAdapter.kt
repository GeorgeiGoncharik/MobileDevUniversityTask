package xyz.goshanchik.prodavayka.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import xyz.goshanchik.prodavayka.data.domain.CartItem
import xyz.goshanchik.prodavayka.databinding.ItemCartBinding

class CartItemListAdapter(
    val increaseButtonListener: CartItemListener,
    val decreaseButtonListener: CartItemListener,
    val deleteButtonListener: CartItemListener): ListAdapter<CartItem, CartItemListAdapter.ViewHolder>(CartItemDiffCallback()) {

    inner class ViewHolder (private val binding: ItemCartBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(cartItem: CartItem){
            binding.apply {
                item = cartItem
                increaseListener  = increaseButtonListener
                decreaseListener = decreaseButtonListener
                deleteListener = deleteButtonListener
                executePendingBindings()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCartBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}


class CartItemDiffCallback: DiffUtil.ItemCallback<CartItem>(){
    override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return newItem.id == oldItem.id
    }

    override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return newItem == oldItem
    }

}

class CartItemListener(val clickListener: (Id: Long) -> Unit) {
    fun onClick(item: CartItem) = clickListener(item.id)
}