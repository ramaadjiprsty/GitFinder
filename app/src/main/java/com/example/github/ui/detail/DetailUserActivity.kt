package com.example.github.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.github.R
import com.example.github.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var detailUserViewModel: DetailUserViewModel

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_AVATAR = "extra_avatar"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers_tab,
            R.string.following_tab,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatarUrl = intent.getStringExtra(EXTRA_AVATAR)

        detailUserViewModel = ViewModelProvider(this).get(DetailUserViewModel::class.java)

        detailUserViewModel.isLoading.observe(this){
            showLoading(it)
        }

        if (username != null) {
            detailUserViewModel.userDetail(username)
            detailUserViewModel.user.observe(this){
                binding.apply {
                    tvUserId.text = it.login
                    tvUsername.text = it.name
                    tvFollowers.text = "${it.followers} Followers"
                    tvFollowing.text = "${it.following} Following"
                    Glide.with(this@DetailUserActivity)
                        .load(it.avatarUrl)
                        .into(ivDetailAvatar)
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            val count = detailUserViewModel.isUserFavorite(id)
            withContext(Dispatchers.Main) {
                _isChecked = count != null && count > 0
                updateFabIcon()
            }
        }

        binding.fabFavorite.setOnClickListener {
            _isChecked = !_isChecked
            if (_isChecked) {
                detailUserViewModel.addFavorite(id, username!!, avatarUrl!!)
                Toast.makeText(this, "Added to favorite", Toast.LENGTH_SHORT).show()
            } else {
                detailUserViewModel.removeFavorite(id)
                Toast.makeText(this, "Removed from favorite", Toast.LENGTH_SHORT).show()
            }
            updateFabIcon()
        }

        val sectionPagerAdapter = SectionPagerAdapter(this)
        sectionPagerAdapter.username = username!!

        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        viewPager.adapter = sectionPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

    }

    var _isChecked = false
    private fun updateFabIcon() {
        val drawable = if (_isChecked) {
            // Replace with your drawable resource for "favorite" state
            resources.getDrawable(R.drawable.baseline_favorite_24)
        } else {
            // Replace with your drawable resource for "not favorite" state
            resources.getDrawable(R.drawable.baseline_favorite_border_24)
        }
        binding.fabFavorite.setImageDrawable(drawable)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}