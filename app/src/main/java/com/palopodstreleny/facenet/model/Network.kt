package com.palopodstreleny.facenet.model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.qualcomm.qti.snpe.NeuralNetwork
import com.qualcomm.qti.snpe.SNPE
import java.io.File
import java.util.concurrent.Executor

/**
 *
 * Network class encapsulate NeuralNetwork object creation and releasing
 *
 * @param context Application context
 * @param outputLayers String value of output layer, default = null
 * @param debugEnabled Boolean value represents if neural network is possible to debug, default = false
 * @param runtime NeuralNetwork.Runtime represents runtime neural network run on
 * @param cpuFallback Boolean
 * @param model DLC file representing neural network model
 *
 */
class Network(private val context: Application,
              private val outputLayers: String? = null,
              private val debugEnabled: Boolean? = false,
              var runtime: NeuralNetwork.Runtime = NeuralNetwork.Runtime.CPU,
              private val cpuFallback: Boolean? = true,
              private val executor: Executor,
              private val model: File) {


    private var networkReleased = false

    var neuralNetwork: NeuralNetwork

    init {
        neuralNetwork = buildNetwork(runtime)
    }


    /**
     *
     * Method changes runtime, new neural network is created and old network released
     *
     * @param runtime NeuralNetwork.Runtime that network should run on
     * @return LiveData<NeuralNetwork.Runtime>
     *
     */
    fun changeRuntime(runtime: NeuralNetwork.Runtime): LiveData<NeuralNetwork.Runtime>{
        val runtimeLiveData = MutableLiveData<NeuralNetwork.Runtime>()
        this.runtime = runtime
        //Create new network in the background
        executor.execute {
            releaseNetwork()
            neuralNetwork = buildNetwork(runtime)
            runtimeLiveData.postValue(runtime)
        }
        return runtimeLiveData
    }

    /**
     *
     * Helper method creates new neural network and returns it
     *
     * @param runtime NeuralNetwork.Runtime that neural network runs on
     * @return newly created NeuralNetwork
     *
     */
    private fun buildNetwork(runtime: NeuralNetwork.Runtime): NeuralNetwork{
        networkReleased = false

        val builder = SNPE.NeuralNetworkBuilder(context)
            .setDebugEnabled(debugEnabled!!)
            .setModel(model)
            .setCpuFallbackEnabled(cpuFallback!!)
            .setRuntimeOrder(runtime)

        if(outputLayers != null){
            builder.setOutputLayers(outputLayers)
        }

        return builder.build()

    }


    /**
     *
     * Helper method for manually releasing old neural network
     *
     */
    private fun releaseNetwork(){
        if(networkReleased) {
            neuralNetwork.release()
            networkReleased = true
        }
    }


}
