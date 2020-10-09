package xyz.goshanchik.prodavayka.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import xyz.goshanchik.prodavayka.adapter.CategoryListAdapter
import xyz.goshanchik.prodavayka.adapter.CategoryListener
import xyz.goshanchik.prodavayka.databinding.FragmentHomeBinding
import xyz.goshanchik.prodavayka.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val categoryAdapter = CategoryListAdapter(requireActivity() ,CategoryListener { categoryId ->
            //TODO
        })

        binding.recycler.apply {

            layoutManager = LinearLayoutManager(requireActivity())

            adapter = categoryAdapter
        }

        homeViewModel.categories.observe(viewLifecycleOwner, {
            categoryAdapter.submitList(it)
        })
    }
}