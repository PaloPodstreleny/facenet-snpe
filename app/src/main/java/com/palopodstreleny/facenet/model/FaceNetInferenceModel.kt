package com.palopodstreleny.facenet.model

import android.graphics.Bitmap
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.palopodstreleny.facenet.db.dao.EmbeddingDao
import com.palopodstreleny.facenet.inference.FacePrediction
import com.qualcomm.qti.snpe.FloatTensor
import org.nd4j.linalg.factory.Nd4j
import java.util.concurrent.Executor

/**
 *
 * FaceNetModel for model inference (prediction)
 *
 * @param dao EmbeddingDao for embeddings loading
 * @param executor Executor for background processing
 * @param network FaceNet network used for inference
 *
 */
class FaceNetInferenceModel(
    private val dao: EmbeddingDao,
    private val executor: Executor,
    val network: Network
) : FaceNetModel(network) {


    private val inputsMap: HashMap<String, FloatTensor> = HashMap()


    /**
     *
     * Method transform bitmap to tensor and run it through the faceNet network
     * Result contains predicted face and network inference time
     *
     * @param input Bitmap
     * @return LiveData<FacePrediction>
     *
     */
    @Synchronized
    fun predict(input: Bitmap): LiveData<FacePrediction> {
        val faceDetectionName = MutableLiveData<FacePrediction>()

        executor.execute {

            //Resize bitmap to match input size for network
            val resizedBitmap = Bitmap.createScaledBitmap(input, imageWidth, imageHeight, true)

            //Process bitmap get value of rgb from every pixel
            processBitmapARGB8888(image = resizedBitmap, tensor = tensor)

            //Add input layer to inputs map
            inputsMap[inputLayerName] = tensor

            //Save starting time of network measurement
            val totalNetworkTime = SystemClock.elapsedRealtime()
            //put image through the network
            val outputMap = network.neuralNetwork.execute(inputsMap)
            val endTimeTotal: Long = SystemClock.elapsedRealtime()

            //Iterate through output layers (in FaceNet example one layer)
            for (output in outputMap.entries) {

                val localTensor = output.value
                val values = FloatArray(localTensor.size)

                //Put inferred values into values variable
                localTensor.read(values, 0, values.size)


                val vector = Nd4j.create(values)

                //Load the data from database ? -> change it maybe, dont load every infer
                val dbData = dao.getAllEmbeddings()


                val allEmbeddingsDistance = arrayListOf<Double>()

                dbData.forEach {
                    allEmbeddingsDistance.add(it.vectorRepresentation.distance2(vector))
                }

                //Find index of smallest distance
                val index = allEmbeddingsDistance.withIndex().minBy { it.value }?.index


                if (allEmbeddingsDistance.isNotEmpty() && index != null) {
                    //Update data
                    faceDetectionName.postValue(
                        FacePrediction(
                            dbData[index].userName, endTimeTotal - totalNetworkTime
                        )
                    )
                }
            }

            inputsMap.clear()
            outputMap.clear()

        }

        return faceDetectionName

    }


}