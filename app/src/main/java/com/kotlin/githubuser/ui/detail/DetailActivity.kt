package com.kotlin.githubuser.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kotlin.githubuser.R
import com.kotlin.githubuser.adapter.SectionsPagerAdapter
import com.kotlin.githubuser.data.Result
import com.kotlin.githubuser.data.model.Github
import com.kotlin.githubuser.data.model.GithubUser
import com.kotlin.githubuser.databinding.ActivityDetailBinding
import com.kotlin.githubuser.helper.ViewModelFactory
import com.kotlin.githubuser.ui.favorite.FavoriteActivity
import com.kotlin.githubuser.ui.setting.SettingActivity

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var github: Github

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val detailViewModel: DetailViewModel by viewModels {
            factory
        }

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

        binding.appBar.setNavigationOnClickListener { finish() }

        val username = intent.getStringExtra(EXTRA_USERNAME).toString()
        detailViewModel.getUserByUsername(username)

        detailViewModel.getUserByUsername(username).observe(this) { result ->
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
                    showData(result.data)
                    github = Github(result.data.login, result.data.avatar_url)
                }
            }
        }

        detailViewModel.isFavorite(username).observe(this) { isFav ->
            binding.fabFav.imageTintList = if (isFav) resources.getColorStateList(
                R.color.red,
                null
            ) else resources.getColorStateList(R.color.white, null)

            binding.fabFav.setOnClickListener {
                if (isFav) detailViewModel.deleteFavorite(github) else detailViewModel.insertFavorite(
                    github
                )
            }
        }

        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, bundle)
        val viewpager: ViewPager2 = binding.viewPager
        viewpager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewpager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun showData(user: GithubUser) {
        binding.apply {
            userName.text = user.name
            userUsername.text = user.login
            userRepo.text =
                resources.getQuantityString(R.plurals.repo, user.public_repos, user.public_repos)
            userFollower.text =
                resources.getQuantityString(R.plurals.followers, user.followers, user.followers)
            userFollowing.text = resources.getString(R.string.follow, user.following)
            Glide.with(applicationContext)
                .load(user.avatar_url)
                .into(userAvatar)
            fabFav.show()
        }
    }

    private fun showError(error: Boolean) {
        if (error) {
            binding.apply {
                val errorText = resources.getString(R.string.error)
                userName.text = errorText
                userUsername.text = errorText
                userRepo.text = errorText
                userFollower.text = errorText
                userFollowing.text = errorText
                Glide.with(applicationContext)
                    .load(R.drawable.user)
                    .into(userAvatar)
                fabFav.hide()
            }
        }
    }

    private fun showLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        if (loading) binding.fabFav.hide()
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }
}