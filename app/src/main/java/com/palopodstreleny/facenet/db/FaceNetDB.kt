package com.palopodstreleny.facenet.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.palopodstreleny.facenet.db.dao.EmbeddingDao
import com.palopodstreleny.facenet.db.dao.UserDao
import com.palopodstreleny.facenet.db.entity.Embedding
import com.palopodstreleny.facenet.db.entity.User


@Database(entities = [User::class, Embedding::class], version = 2)
abstract class FaceNetDB : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun embeddingDao(): EmbeddingDao
}