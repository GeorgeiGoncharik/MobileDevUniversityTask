package xyz.goshanchik.prodavayka.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import xyz.goshanchik.prodavayka.CategoryActivity
import xyz.goshanchik.prodavayka.adapter.CategoryListAdapter
import xyz.goshanchik.prodavayka.adapter.CategoryListener
import xyz.goshanchik.prodavayka.adapter.ProductListAdapter
import xyz.goshanchik.prodavayka.adapter.ProductListener
import xyz.goshanchik.prodavayka.databinding.FragmentHomeBinding
import xyz.goshanchik.prodavayka.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var categoryAdapter: CategoryListAdapter
    private lateinit var recentsAdapter: ProductListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("OnCreate called")
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        categoryAdapter = CategoryListAdapter(requireActivity() ,CategoryListener { categoryId ->
            homeViewModel.navigateCategoryActivity(categoryId)
        })

        recentsAdapter = ProductListAdapter(ProductListener {
            Timber.d("id: $it")
        })
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.swiperefresh.setOnRefreshListener {
            Timber.i("onRefresh called from SwipeRefreshLayout")
            homeViewModel.refreshDataFromRepository()
        }

        binding.recycler.apply {

            layoutManager = LinearLayoutManager(requireActivity())

            adapter = categoryAdapter
        }

        binding.recyclerRecentlySeen.apply {
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

            adapter = recentsAdapter
        }

        homeViewModel.categories.observe(viewLifecycleOwner, {
            categoryAdapter.submitList(it)
        })

        homeViewModel.recents.observe(viewLifecycleOwner, {
            Timber.d("recents list size: ${it.size}")
            if(it.isNotEmpty()){
                binding.textView2.visibility = View.VISIBLE
                binding.recyclerRecentlySeen.visibility = View.VISIBLE

                recentsAdapter.submitList(it)
            }
            else{
                binding.textView2.visibility = View.GONE
                binding.recyclerRecentlySeen.visibility = View.GONE
            }
        })

        homeViewModel.navigate.observe(viewLifecycleOwner) { id ->
            id?.let {CategoryListAdapter.ViewHolder
                val intent = Intent(requireActivity(), CategoryActivity::class.java).apply {
                    putExtra("category_id", id)
                }
                startActivity(intent)
                homeViewModel.onNavigateCategoryActivity()
            }

        }

        homeViewModel.refreshed.observe(viewLifecycleOwner){
            binding.swiperefresh.isRefreshing = false
        }

        return binding.root
    }
}