package com.kotlin.githubuser.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlin.githubuser.adapter.GithubAdapter
import com.kotlin.githubuser.data.Result
import com.kotlin.githubuser.databinding.FragmentFollowingBinding
import com.kotlin.githubuser.helper.ViewModelFactory

class FollowingFragment : Fragment() {

    private var binding: FragmentFollowingBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val followingViewModel: FollowingViewModel by viewModels {
            factory
        }

        val username = arguments?.getString(DetailActivity.EXTRA_USERNAME).toString()
        followingViewModel.getFollowing(username)

        binding?.followingList?.layoutManager = LinearLayoutManager(activity)
        val adapter = GithubAdapter(onclick = {
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_USERNAME, it.login)
            startActivity(intent)
        })
        binding?.followingList?.adapter = adapter

        followingViewModel.result.observe(viewLifecycleOwner) { result ->
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

    private fun showError(error: Boolean) {
        binding?.error?.visibility = if (error) View.VISIBLE else View.GONE
        binding?.followingList?.visibility = if (error) View.GONE else View.VISIBLE
    }

    private fun showLoading(loading: Boolean) {
        binding?.progressBar?.visibility = if (loading) View.VISIBLE else View.GONE
        binding?.followingList?.visibility = if (loading) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}