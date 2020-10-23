package xyz.goshanchik.prodavayka.view

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import xyz.goshanchik.prodavayka.R
import xyz.goshanchik.prodavayka.databinding.FragmentProductBinding
import xyz.goshanchik.prodavayka.viewmodel.SharedCategoryProductViewModel
import kotlin.math.max
import kotlin.math.min


class ProductFragment : Fragment() {
    private lateinit var binding: FragmentProductBinding
    private lateinit var sharedViewModel: SharedCategoryProductViewModel
    private lateinit var mScaleGestureDetector: ScaleGestureDetector
    private var mScaleFactor = 1.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedCategoryProductViewModel::class.java)


        mScaleGestureDetector = ScaleGestureDetector(requireActivity(), ScaleListener())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product, container, false)
        binding.apply {
            lifecycleOwner = this@ProductFragment
            viewmodel = sharedViewModel
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

        binding.banner.setOnTouchListener { v, event ->
            mScaleGestureDetector.onTouchEvent(event)
            true
        }

        return binding.root
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        // when a scale gesture is detected, use it to resize the image
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            mScaleFactor *= scaleGestureDetector.scaleFactor;
            mScaleFactor = max(0.5f, min(mScaleFactor, 2.0f))
            Timber.d("onScale called. ScaleFactor: $mScaleFactor")
            binding.banner.scaleX = mScaleFactor
            binding.banner.scaleY = mScaleFactor
            return true
        }
    }

}