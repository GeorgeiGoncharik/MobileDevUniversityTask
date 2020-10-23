package xyz.goshanchik.prodavayka.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import xyz.goshanchik.prodavayka.R
import xyz.goshanchik.prodavayka.databinding.FragmentCartBinding
import xyz.goshanchik.prodavayka.viewmodel.CartViewModel

class CartFragment : Fragment() {

    private lateinit var cartViewModel: CartViewModel
    private lateinit var binding: FragmentCartBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        cartViewModel = ViewModelProvider(requireActivity()).get(CartViewModel::class.java)


    }
}