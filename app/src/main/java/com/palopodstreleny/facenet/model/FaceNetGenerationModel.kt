package com.palopodstreleny.facenet.model

import android.graphics.Bitmap
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.palopodstreleny.facenet.db.dao.EmbeddingDao
import com.palopodstreleny.facenet.db.dao.UserDao
import com.palopodstreleny.facenet.db.entity.Embedding
import com.palopodstreleny.facenet.inference.EmbeddingCreation
import com.qualcomm.qti.snpe.FloatTensor
import java.util.concurrent.Executor

/**
 *
 * FaceNetModel for model embeddings generation
 *
 *
 * @param userDao UserDao for user database access
 * @param embeddingDao EmbeddingDao for embeddings database access
 * @param executor Executor for background processing
 * @param network FaceNet network used for generation
 *
 * (FaceNetGenerationModel and FaceNetInferenceModel used same neural network)
 *
 */
class FaceNetGenerationModel constructor(val userDao: UserDao,
                                         val embeddingDao: EmbeddingDao,
                                         private val executor: Executor,
                                         val network: Network): FaceNetModel(network) {

    private val inputsMap: HashMap<String, FloatTensor> = HashMap()

    @Synchronized
    fun generateEmbeddings(input: Bitmap,user: String): LiveData<EmbeddingCreation> {
        val mutableEmbeddings = MutableLiveData<EmbeddingCreation>()

        executor.execute {

            //Resize bitmap to match input size for network
            val resizedBitmap = Bitmap.createScaledBitmap(input, imageWidth, imageHeight, false)

            //Process bitmap get value of rgb from every pixel
            processBitmapARGB8888(image = resizedBitmap, tensor = tensor)

            inputsMap[inputLayerName] = tensor

            //Save starting time of network measurement
            val totalNetworkTime = SystemClock.elapsedRealtime()
            val outputMap = network.neuralNetwork.execute(inputsMap)
            val endTimeTotal: Long = SystemClock.elapsedRealtime()

            if (outputMap != null) {
                for (output in outputMap.entries) {

                    val localTensor = output.value


                    val values = FloatArray(localTensor.size)

                    //Put inferred values into values
                    localTensor.read(values, 0, values.size)


                    val embedding = createEmbedding(values, user)

                    //Save embedings to the database
                    embeddingDao.insertEmbedding(embedding)

                    //Update livedata
                    mutableEmbeddings.postValue(EmbeddingCreation(user,endTimeTotal - totalNetworkTime))

                }
            }

        }
        return mutableEmbeddings
    }


    /**
     *
     * Helper function for embedding creation
     *
     * @param values: FloatArray of values that represents embedding
     * @param user: String name of the user embedding will be associated with
     *
     * @return Embedding
     *
     */
    @Synchronized
    private fun createEmbedding(values: FloatArray, user: String): Embedding {
        return Embedding(
            userName = user,
            e0 = values[0],
            e1 = values[1],
            e2 = values[2],
            e3 = values[3],
            e4 = values[4],
            e5 = values[5],
            e6 = values[6],
            e7 = values[7],
            e8 = values[8],
            e9 = values[9],

            e10 = values[10],
            e11 = values[11],
            e12 = values[12],
            e13 = values[13],
            e14 = values[14],
            e15 = values[15],
            e16 = values[16],
            e17 = values[17],
            e18 = values[18],
            e19 = values[19],

            e20 = values[20],
            e21 = values[21],
            e22 = values[22],
            e23 = values[23],
            e24 = values[24],
            e25 = values[25],
            e26 = values[26],
            e27 = values[27],
            e28 = values[28],
            e29 = values[29],


            e30 = values[30],
            e31 = values[31],
            e32 = values[32],
            e33 = values[33],
            e34 = values[34],
            e35 = values[35],
            e36 = values[36],
            e37 = values[37],
            e38 = values[38],
            e39 = values[39],


            e40 = values[40],
            e41 = values[41],
            e42 = values[42],
            e43 = values[43],
            e44 = values[44],
            e45 = values[45],
            e46 = values[46],
            e47 = values[47],
            e48 = values[48],
            e49 = values[49],

            e50 = values[50],
            e51 = values[51],
            e52 = values[52],
            e53 = values[53],
            e54 = values[54],
            e55 = values[55],
            e56 = values[56],
            e57 = values[57],
            e58 = values[58],
            e59 = values[59],

            e60 = values[60],
            e61 = values[61],
            e62 = values[62],
            e63 = values[63],
            e64 = values[64],
            e65 = values[65],
            e66 = values[66],
            e67 = values[67],
            e68 = values[68],
            e69 = values[69],

            e70 = values[70],
            e71 = values[71],
            e72 = values[72],
            e73 = values[73],
            e74 = values[74],
            e75 = values[75],
            e76 = values[76],
            e77 = values[77],
            e78 = values[78],
            e79 = values[79],

            e80 = values[80],
            e81 = values[81],
            e82 = values[82],
            e83 = values[83],
            e84 = values[84],
            e85 = values[85],
            e86 = values[86],
            e87 = values[87],
            e88 = values[88],
            e89 = values[89],

            e90 = values[90],
            e91 = values[91],
            e92 = values[92],
            e93 = values[93],
            e94 = values[94],
            e95 = values[95],
            e96 = values[96],
            e97 = values[97],
            e98 = values[98],
            e99 = values[99],

            e100 = values[100],
            e101 = values[101],
            e102 = values[102],
            e103 = values[103],
            e104 = values[104],
            e105 = values[105],
            e106 = values[106],
            e107 = values[107],
            e108 = values[108],
            e109 = values[109],

            e110 = values[110],
            e111 = values[111],
            e112 = values[112],
            e113 = values[113],
            e114 = values[114],
            e115 = values[115],
            e116 = values[116],
            e117 = values[117],
            e118 = values[118],
            e119 = values[119],

            e120 = values[120],
            e121 = values[121],
            e122 = values[122],
            e123 = values[123],
            e124 = values[124],
            e125 = values[125],
            e126 = values[126],
            e127 = values[127]
        )
    }

}