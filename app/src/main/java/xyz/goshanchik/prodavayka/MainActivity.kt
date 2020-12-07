package xyz.goshanchik.prodavayka

import android.media.MediaPlayer
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import timber.log.Timber
import xyz.goshanchik.prodavayka.view.CartFragmentDirections
import xyz.goshanchik.prodavayka.view.HomeFragmentDirections

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mp: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate called.")

        setContentView(R.layout.activity_main)

        mp = MediaPlayer.create(this, R.raw.water_bloop)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_cart, R.id.nav_info), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_info -> {
                val navController = findNavController(R.id.nav_host_fragment)
                when(navController.currentDestination!!.id){
                    R.id.nav_home -> {
                        mp.start()
                        navController.navigate(HomeFragmentDirections.actionNavHomeToNavInfo())
                    }
                    R.id.nav_cart -> {
                        mp.start()
                        navController.navigate(CartFragmentDirections.actionNavCartToNavInfo())
                    }
                }
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
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