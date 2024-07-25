package com.example.github.ui.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github.R
import com.example.github.data.local.entity.FavoriteUser
import com.example.github.data.remote.response.UserItem
import com.example.github.databinding.ActivityFavoriteBinding
import com.example.github.ui.main.UserAdapter
import com.google.android.material.appbar.MaterialToolbar

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val topAppBar = findViewById<MaterialToolbar>(R.id.favorite_AppBar)
        topAppBar.setNavigationOnClickListener {
            finish()
        }

        val adapter = UserAdapter()

        favoriteViewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]

        binding.apply {
            rvFavoriteUser.setHasFixedSize(true)
            rvFavoriteUser.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvFavoriteUser.adapter = adapter
        }

        favoriteViewModel.getFavorite()?.observe(this) {
            if (it != null) {
                val list = mapList(it)
                adapter.submitList(list)
            }
        }
    }
    private fun mapList(users: List<FavoriteUser>): ArrayList<UserItem> {
        val listUser = ArrayList<UserItem>()
        for (user in users) {
            val userMapped = UserItem(
                user.username,
                user.avatarUrl,
                user.id
            )
            listUser.add(userMapped)
        }
        return listUser
    }
}