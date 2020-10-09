package xyz.goshanchik.prodavayka.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import xyz.goshanchik.prodavayka.databinding.CategoryItemBinding
import xyz.goshanchik.prodavayka.model.Category
import xyz.goshanchik.prodavayka.util.GlideApp

class CategoryListAdapter(private val context: Context, private val listener: CategoryListener) : ListAdapter<Category, CategoryListAdapter.ViewHolder>(CategoryDiffCallback()) {

    class ViewHolder private constructor (private val binding: CategoryItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(listener: CategoryListener, item: Category, context: Context) {
            itemView.setOnClickListener { listener.onClick(item) }
            binding.title.text = item.name
            GlideApp
                .with(binding.banner.context)
                .load(item.pictureUrl)
                .into(binding.banner)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CategoryItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listener ,getItem(position), context)
    }

    fun getCategoryAt(position: Int): Category {
        return getItem(position)
    }

}

class CategoryDiffCallback: DiffUtil.ItemCallback<Category>(){
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return newItem.id == oldItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return newItem == oldItem
    }

}

class CategoryListener(val clickListener: (Id: Int) -> Unit) {
    fun onClick(category: Category) = clickListener(category.id)
}