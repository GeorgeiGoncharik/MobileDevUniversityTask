package xyz.goshanchik.prodavayka.view

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import xyz.goshanchik.prodavayka.R
import xyz.goshanchik.prodavayka.WORK_OFFER_EMPTY_OUTPUT
import xyz.goshanchik.prodavayka.WORK_OFFER_OUTPUT
import xyz.goshanchik.prodavayka.adapter.ProductListAdapter
import xyz.goshanchik.prodavayka.adapter.ProductListener
import xyz.goshanchik.prodavayka.databinding.FragmentCategoryBinding
import xyz.goshanchik.prodavayka.util.dpToPx
import xyz.goshanchik.prodavayka.viewmodel.SharedCategoryProductViewModel


class CategoryFragment : Fragment() {

    private lateinit var sharedViewModel: SharedCategoryProductViewModel
    private lateinit var binding: FragmentCategoryBinding
    private lateinit var productListAdapter: ProductListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedCategoryProductViewModel::class.java)

        // move callback to data binding
        productListAdapter = ProductListAdapter(ProductListener { productId ->
            sharedViewModel.onNavigateToProductItemDetail(productId)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.d("OnCreateView called.")
        binding = FragmentCategoryBinding.inflate(inflater, container, false)

        binding.swiperefresh.setOnRefreshListener {
            Timber.i("onRefresh called from SwipeRefreshLayout")
            sharedViewModel.refreshDataFromRepository()
            binding.swiperefresh.isRefreshing = false
        }

        val touchHelperCallback: ItemTouchHelper.SimpleCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                private val background = ColorDrawable(resources.getColor(R.color.colorPrimary))
                private val icon: Drawable = requireContext().getDrawable(R.drawable.ic_baseline_add_shopping_cart_24)!!
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val item = productListAdapter.getProductAt(viewHolder.position)
                    Timber.d("add to cart: id - ${item.id} name - ${item.name}")
                    sharedViewModel.onProductItemAddToCart(item)
                    productListAdapter.notifyItemChanged(viewHolder.position)
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                    val itemView = viewHolder.itemView
                    val backgroundCornerOffset = dpToPx(requireContext(), 4)

                    val iconMargin = (itemView.height - icon.intrinsicHeight) / 2
                    val iconTop =
                        itemView.top + (itemView.height - icon.intrinsicHeight) / 2
                    val iconBottom = iconTop + icon.intrinsicHeight

                    when {
                        dX < 0 -> { // Swiping to the left
                            val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
                            val iconRight = itemView.right - iconMargin
                            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                            background.setBounds(
                                itemView.right + dX.toInt() - backgroundCornerOffset,
                                itemView.top, itemView.right, itemView.bottom
                            )
                        }
                        else -> { // view is unSwiped
                            background.setBounds(0, 0, 0, 0)
                        }
                    }

                    background.draw(c)
                    icon.draw(c)
                }
            }

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = productListAdapter
        }

        val touchHelper = ItemTouchHelper(touchHelperCallback)
        touchHelper.attachToRecyclerView(binding.recycler)

        sharedViewModel.navigateDetailPage.observe(viewLifecycleOwner) {
            if(it){
                this.findNavController().navigate(CategoryFragmentDirections.actionCategoryFragmentToProductFragment())
                sharedViewModel.onNavigateToProductItemDetailDone()
            }
        }

        sharedViewModel.categoryWithProducts.observe(viewLifecycleOwner) {
            requireActivity().title = it.category.name
            productListAdapter.submitList(it.products)
        }

        sharedViewModel.showSnackBar.observe(viewLifecycleOwner){
            if(!it.isNullOrBlank()){
                Snackbar.make(
                    binding.root,
                    it,
                    Snackbar.LENGTH_SHORT
                ).show()

                sharedViewModel.resetShowSnackBar()
            }
        }
        return binding.root
    }


}