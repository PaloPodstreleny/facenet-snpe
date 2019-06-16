package com.palopodstreleny.facenet.inference

/**
 *
 * EmbeddingCreation data holder class
 *
 * @param networkInference inference time of neural network
 * @param name Name of the user for who new embedding was generated
 */
data class EmbeddingCreation(val name: String, val networkInference: Long)