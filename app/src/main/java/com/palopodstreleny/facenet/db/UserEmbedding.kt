package com.palopodstreleny.facenet.db

/**
 *
 * Data holder class for UserEmbeddings
 *
 * @param name Name of the user
 * @param numberOfEmbeddings Number of embeddings give user has
 *
 */
data class UserEmbedding(val name: String, val numberOfEmbeddings: Int) {

    override fun toString(): String {
        return "User $name has $numberOfEmbeddings embeddings"
    }
}