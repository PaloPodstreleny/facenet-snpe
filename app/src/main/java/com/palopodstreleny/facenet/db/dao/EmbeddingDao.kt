package com.palopodstreleny.facenet.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.palopodstreleny.facenet.db.UserEmbedding
import com.palopodstreleny.facenet.db.entity.Embedding

/**
 *
 * DAO Embeddings interface provides basic methods to work with embedding database table
 *
 */
@Dao
interface EmbeddingDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertEmbedding(embedding: Embedding)

    @Query("DELETE FROM embedding")
    fun deleteAllEmbeddings()

    @Query("SELECT user_name as name, COUNT(e0) as numberOfEmbeddings FROM embedding GROUP BY user_name")
    fun getUserEmbeddingsCount(): LiveData<List<UserEmbedding>>

    @Query("SELECT * FROM embedding")
    fun getAllEmbeddings(): List<Embedding>
    
}




