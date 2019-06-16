package com.palopodstreleny.facenet.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.palopodstreleny.facenet.db.entity.User

/**
 *
 * DAO User interface provides basic methods to work with user database table
 *
 */
@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAllUsers(): LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(users: User)

    @Query("DELETE FROM user")
    fun deleteAllUsers()


}
