package xyz.goshanchik.prodavayka

import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import timber.log.Timber
import xyz.goshanchik.prodavayka.factory.SharedCategoryProductViewModelFactory
import xyz.goshanchik.prodavayka.viewmodel.SharedCategoryProductViewModel
import kotlin.math.max
import kotlin.math.min

class CategoryActivity : AppCompatActivity() {

    var categoryId = 0
    lateinit var sharedViewModel: SharedCategoryProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate called.")

        setContentView(R.layout.activity_category)

        categoryId = intent.getIntExtra("category_id", -1)

        sharedViewModel = ViewModelProvider(
            this,
            SharedCategoryProductViewModelFactory(categoryId, application)
        )
            .get(SharedCategoryProductViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        Timber.d("onStart called.")
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume called.")
    }

    override fun onPause() {
        super.onPause()
        Timber.d("onPause called.")
    }

    override fun onStop() {
        super.onStop()
        Timber.d("onStop called.")
    }

    override fun onRestart() {
        super.onRestart()
        Timber.d("onRestart called.")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("onDestroy called.")
    }

}