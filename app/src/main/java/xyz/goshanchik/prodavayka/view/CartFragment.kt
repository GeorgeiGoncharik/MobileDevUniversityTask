package xyz.goshanchik.prodavayka.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import timber.log.Timber
import xyz.goshanchik.prodavayka.R
import xyz.goshanchik.prodavayka.adapter.CartItemListAdapter
import xyz.goshanchik.prodavayka.adapter.CartItemListener
import xyz.goshanchik.prodavayka.databinding.FragmentCartBinding
import xyz.goshanchik.prodavayka.util.ListState
import xyz.goshanchik.prodavayka.viewmodel.CartViewModel

class CartFragment : Fragment() {

    private lateinit var cartViewModel: CartViewModel
    private lateinit var cartItemListAdapter: CartItemListAdapter
    private lateinit var binding: FragmentCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cartViewModel = ViewModelProvider(requireActivity()).get(CartViewModel::class.java)
        cartItemListAdapter = CartItemListAdapter(
            increaseButtonListener = CartItemListener{ id ->
                Timber.d("increaseButtonListener")
                cartViewModel.increaseQuantity(id) },
            decreaseButtonListener = CartItemListener{ id ->
                Timber.d("decreaseButtonListener")
                cartViewModel.decreaseQuantity(id) },
            deleteButtonListener = CartItemListener{ id ->
                Timber.d("deleteButtonListener")
                cartViewModel.deleteItem(id) }
        )
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        binding.apply {
            lifecycleOwner = this@CartFragment
            viewModel = cartViewModel
        }
        binding.cartRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartItemListAdapter
        }

        cartViewModel.cartItems.observe(viewLifecycleOwner){
            cartItemListAdapter.submitList(it)
        }

        cartViewModel.listState.observe(viewLifecycleOwner){
            when(it){
                ListState.OK -> {
                    binding.cartRecycler.visibility = View.VISIBLE
                    binding.emptyCartBanner.visibility = View.GONE
                }
                ListState.EMPTY -> {
                    binding.cartRecycler.visibility = View.GONE
                    binding.emptyCartBanner.visibility = View.VISIBLE
                }
                ListState.LOADING -> {

                }
            }
        }

        return binding.root
    }

}