package com.palopodstreleny.facenet.model

import com.qualcomm.qti.snpe.FloatTensor

/**
 *
 * Abstract class encapsulating common faceNetModel attributes
 *
 * @param network Network used for tensor creation
 *
 */
abstract class FaceNetModel(network: Network) : RGBConverter {


    //Define input dimension values
    protected val imageHeight = 96
    protected val imageWidth = 96
    private val batchSize = 1
    private val imageDepth = 3

    //Input layer name
    protected val inputLayerName = "input_9:0"

    //Create input tensor
    protected val tensor: FloatTensor =
        network.neuralNetwork.createFloatTensor(batchSize, imageWidth, imageHeight, imageDepth)


}