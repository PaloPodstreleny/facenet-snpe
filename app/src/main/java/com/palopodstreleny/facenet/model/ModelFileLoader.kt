package com.palopodstreleny.facenet.model
import android.app.Application
import android.content.Context
import java.io.File


/**
 *
 * ModelFileLoader loads the DLC model, the instance of this class <b>should</b> be created on Worker Thread
 *
 * @param modelName String value of model in assets folder
 * @param context Application context
 *
 */
class ModelFileLoader constructor(private val modelName: String, private val context: Application) {

    val modelFile: File

    init {

        //Check if model already exists
        if(notExists()) {
            try {

                //Open file in assets
                val startTime = System.currentTimeMillis()

                val inputStream = context.assets.open(modelName)

                //Create output stream
                val fileOutputStream = context.openFileOutput(modelName, Context.MODE_PRIVATE)
                val buffer = ByteArray(1024)

                var length = inputStream.read(buffer)

                while (length >= 0) {
                    fileOutputStream.write(buffer, 0, length)
                    length = inputStream.read(buffer)
                }

                //Close stream
                inputStream.close()
                fileOutputStream.close()

                val finishTime = System.currentTimeMillis()


            } catch (e: Exception) {
                throw IllegalStateException("ModelFileLoader was not able to load the data")
            }
        }

        val modelPath = "${context.filesDir.absolutePath}/$modelName"
        modelFile = File(modelPath)

    }

    /**
     *
     * Helper function for checking if model already exists (was already loaded)
     *
     */
    private fun notExists(): Boolean {
        return modelName !in context.fileList()
    }


}