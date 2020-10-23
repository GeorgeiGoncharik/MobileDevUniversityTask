package xyz.goshanchik.prodavayka.adapter

import xyz.goshanchik.prodavayka.databinding.ItemProductBinding

//class ProductListAdapter(val context: Context, val listener: ProductListener): ListAdapter<Product, ProductListAdapter.ViewHolder>(ProductDiffCallback()) {
//
//    class ViewHolder private constructor (private val binding: ProductItemBinding): RecyclerView.ViewHolder(binding.root){
//
//        fun bind(listener: ProductListener, product: Product, context: Context) {
//            itemView.setOnClickListener { listener.onClick(product) }
//            binding.productName.text = product.name
//            binding.price.text = product.fullPrice.toString()
//            GlideApp
//                .with(binding.banner.context)
//                .load(product.pictureUrl)
//                .into(binding.banner)
//        }
//
//        companion object {
//            fun from(parent: ViewGroup): ViewHolder {
//                val layoutInflater = LayoutInflater.from(parent.context)
//                val binding = ProductItemBinding.inflate(layoutInflater, parent, false)
//                return ViewHolder(binding)
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder.from(parent)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(listener ,getItem(position), context)
//    }
//
//    fun getProductAt(position: Int): Product {
//        return getItem(position)
//    }
//}
//
//
//class ProductDiffCallback: DiffUtil.ItemCallback<Product>(){
//    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
//        return newItem.id == oldItem.id
//    }
//
//    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
//        return newItem == oldItem
//    }
//
//}
//
//class CartListener(val clickListener: (Id: Long) -> Unit) {
//    fun onClick(product: Product) = clickListener(product.id)
//}