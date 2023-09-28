package com.kotlin.githubuser.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlin.githubuser.R
import com.kotlin.githubuser.adapter.GithubAdapter
import com.kotlin.githubuser.databinding.ActivityFavoriteBinding
import com.kotlin.githubuser.helper.ViewModelFactory
import com.kotlin.githubuser.ui.detail.DetailActivity
import com.kotlin.githubuser.ui.setting.SettingActivity

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val favoriteViewModel: FavoriteViewModel by viewModels {
            factory
        }

        val searchItem = binding.appBar.menu.findItem(R.id.btn_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = resources.getString(R.string.search)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                favoriteViewModel.searchFavorite(query ?: "")
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                favoriteViewModel.getFavorites()
                return true
            }
        })

        val adapter = GithubAdapter(onclick = {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_USERNAME, it.login)
            startActivity(intent)
        })

        binding.appBar.setNavigationOnClickListener {
            finish()
        }

        binding.appBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.btn_setting -> {
                    startActivity(Intent(this, SettingActivity::class.java))
                    true
                }
                else -> false
            }
        }

        binding.userList.layoutManager = LinearLayoutManager(this)
        binding.userList.adapter = adapter

        favoriteViewModel.getFavorites()

        favoriteViewModel.result.observe(this) { result ->
            showError(result.isEmpty())
            adapter.submitList(result)
        }
    }

    private fun showError(error: Boolean) {
        binding.error.visibility = if (error) View.VISIBLE else View.GONE
        binding.userList.visibility = if (error) View.GONE else View.VISIBLE
    }
}