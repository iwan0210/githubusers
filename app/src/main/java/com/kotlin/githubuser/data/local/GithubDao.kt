package com.kotlin.githubuser.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kotlin.githubuser.data.model.Github
import kotlinx.coroutines.flow.Flow

@Dao
interface GithubDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(github: Github)

    @Delete
    suspend fun delete(github: Github)

    @Query("SElECT * FROM github")
    fun getAllFavorite(): LiveData<List<Github>>

    @Query("SELECT * FROM github WHERE login LIKE '%' || :search || '%'")
    fun searchFavorite(search: String): LiveData<List<Github>>

    @Query("SELECT EXISTS(SELECT * FROM github WHERE login = :login)")
    fun checkFavorite(login: String): Flow<Boolean>
}