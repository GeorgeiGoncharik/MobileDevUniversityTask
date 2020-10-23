package xyz.goshanchik.prodavayka.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import xyz.goshanchik.prodavayka.databinding.ItemProductBinding
import xyz.goshanchik.prodavayka.data.domain.Product

class ProductListAdapter(val listener: ProductListener): ListAdapter<Product, ProductListAdapter.ViewHolder>(ProductDiffCallback()) {

    inner class ViewHolder(private val binding: ItemProductBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(listener: ProductListener, product: Product) {
            binding.product = product
            binding.listener = listener
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listener, getItem(position))
    }

    fun getProductAt(position: Int): Product {
        return getItem(position)
    }
}

class ProductDiffCallback: DiffUtil.ItemCallback<Product>(){
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return newItem.id == oldItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return newItem == oldItem
    }

}

class ProductListener(val clickListener: (Id: Long) -> Unit) {
    fun onClick(product: Product) = clickListener(product.id)
}