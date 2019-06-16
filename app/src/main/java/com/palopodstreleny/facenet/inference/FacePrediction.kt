package com.palopodstreleny.facenet.inference

/**
 *
 * FacePrediction data holder class
 *
 * @param networkInference inference time of neural network
 * @param name Name of the inferred user
 */
data class FacePrediction(val name: String, val networkInference:Long)