package com.android.tlb.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.android.tlb.R
import com.android.tlb.databinding.ActivityHomeBinding
import com.android.tlb.utils.toast


class HomeActivity : AppCompatActivity() {

    private lateinit var searchListener: HomeActivity.SearchListener
    private lateinit var binding: ActivityHomeBinding
    private lateinit var textCartItemCount: TextView
    private var mCartItemCount: Int = 10

    interface SearchListener {
        fun onSearchClick()
    }

    fun registerSearchListener(searchListener: SearchListener) {
        this.searchListener = searchListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBindings(savedInstanceState)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_discover,
                R.id.navigation_collections,
                R.id.navigation_notification,
                R.id.navigation_profile
            )
        )
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.logo = ContextCompat.getDrawable(this, R.drawable.ic_launcher)
        binding.toolbar.title = this.resources.getString(R.string.app_name)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.blue))
       // binding.toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_launcher)
        binding.toolbar.setPadding(0, 0, 0, 0)
        binding.toolbar.setContentInsetsAbsolute(0, 0)

    }

    private fun setupBindings(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                this.toast("Search clicked")
            }
            R.id.action_wish_list -> {
                this.toast("Wish List")
            }
            R.id.action_cart -> {
                this.toast("Cart")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_options_menu, menu)
        val menuItem: MenuItem = menu!!.findItem(R.id.action_cart)
        val actionView: View = menuItem.actionView
        textCartItemCount = actionView.findViewById(R.id.cart_badge)
        setupBadge()
        actionView.setOnClickListener {
            onOptionsItemSelected(menuItem)
        }
        return true
    }

    private fun setupBadge() {
        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.visibility != View.GONE) {
                    textCartItemCount.visibility = View.GONE
                }
            } else {
                textCartItemCount.text = Math.min(mCartItemCount, 99).toString()
                if (textCartItemCount.visibility != View.VISIBLE) {
                    textCartItemCount.visibility = View.VISIBLE
                }
            }
        }
    }
}