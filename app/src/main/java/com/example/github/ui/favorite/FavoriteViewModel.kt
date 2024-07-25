package com.example.github.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.github.data.local.entity.FavoriteUser
import com.example.github.data.local.room.FavoriteDao
import com.example.github.data.local.room.FavoriteDatabase

class FavoriteViewModel(application: Application): AndroidViewModel(application) {

    private var userDao: FavoriteDao?
    private var userDb: FavoriteDatabase?

    init {
        userDb = FavoriteDatabase.getDatabase(application)
        userDao = userDb?.favoriteDao()
    }

    fun getFavorite(): LiveData<List<FavoriteUser>>? {
        return userDao?.getFavorite()
    }
}