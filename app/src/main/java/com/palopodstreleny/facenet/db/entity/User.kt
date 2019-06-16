package com.palopodstreleny.facenet.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 *
 * User entity represents database table
 *
 * @param userName Primary key of user table
 *
 */
@Entity
data class User(@PrimaryKey val userName: String){
    override fun toString(): String {
        return userName
    }
}