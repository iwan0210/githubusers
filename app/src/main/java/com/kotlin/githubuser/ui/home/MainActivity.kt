package com.kotlin.githubuser.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.MenuItem.OnActionExpandListener
import android.view.View
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlin.githubuser.R
import com.kotlin.githubuser.adapter.GithubAdapter
import com.kotlin.githubuser.data.Result
import com.kotlin.githubuser.databinding.ActivityMainBinding
import com.kotlin.githubuser.helper.ViewModelFactory
import com.kotlin.githubuser.ui.detail.DetailActivity
import com.kotlin.githubuser.ui.favorite.FavoriteActivity
import com.kotlin.githubuser.ui.setting.SettingActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val mainViewModel: MainViewModel by viewModels {
            factory
        }

        mainViewModel.getTheme().observe(this) { isDark ->
            if (isDark) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val adapter = GithubAdapter(onclick = {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_USERNAME, it.login)
            startActivity(intent)
        })

        binding.appBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btn_favorite -> {
                    startActivity(Intent(this, FavoriteActivity::class.java))
                    true
                }

                R.id.btn_setting -> {
                    startActivity(Intent(this, SettingActivity::class.java))
                    true
                }

                else -> false
            }
        }

        val searchItem = binding.appBar.menu.findItem(R.id.btn_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = resources.getString(R.string.search)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mainViewModel.searchUser(query ?: "")
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        searchItem.setOnActionExpandListener(object : OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                mainViewModel.getUsers()
                return true
            }
        })

        if (Build.VERSION.SDK_INT >= 33) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT
            ) {
                if (searchView.isIconified) {
                    exitDialog()
                } else {
                    mainViewModel.getUsers()
                    searchItem.collapseActionView()
                }
            }
        } else {
            onBackPressedDispatcher.addCallback(
                this,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if (searchView.isIconified) {
                            exitDialog()
                        } else {
                            mainViewModel.getUsers()
                            searchItem.collapseActionView()
                        }
                    }
                }
            )
        }

        binding.userList.layoutManager = LinearLayoutManager(this)
        binding.userList.adapter = adapter

        mainViewModel.result.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                    showError(false)
                }

                is Result.Error -> {
                    showLoading(false)
                    showError(true)
                }

                is Result.Success -> {
                    showLoading(false)
                    showError(false)
                    if (result.data.isEmpty()) {
                        showError(true)
                    } else {
                        adapter.submitList(result.data)
                    }
                }
            }
        }
    }

    private fun exitDialog() {
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setMessage(resources.getString(R.string.exitMessage))
            .setCancelable(true)
            .setPositiveButton(resources.getString(R.string.close)) { _, _ -> finish() }
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }

        val theme = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }

        val alert = dialogBuilder.create()
        alert.setTitle(resources.getString(R.string.exit))
        alert.show()
        alert.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(this, if (theme) R.color.white else R.color.charcoal))
        alert.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(this, R.color.red))
    }

    private fun showError(error: Boolean) {
        binding.error.visibility = if (error) View.VISIBLE else View.GONE
        binding.userList.visibility = if (error) View.GONE else View.VISIBLE
    }

    private fun showLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        binding.userList.visibility = if (loading) View.GONE else View.VISIBLE
    }
}