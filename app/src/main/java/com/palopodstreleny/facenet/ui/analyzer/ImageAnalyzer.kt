package com.palopodstreleny.facenet.ui.analyzer

import android.graphics.Bitmap
import android.renderscript.RenderScript
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy

class ImageAnalyzer(private val renderScript: RenderScript) : BaseAnalyzer(), ImageAnalysis.Analyzer {

    private var lastAnalyzedTimestamp = 0L
    var listener : ImageAnalyzedListener? = null

    override fun analyze(image: ImageProxy, rotationDegrees: Int) {


        val currentTimestamp = System.currentTimeMillis()


        //Show prediction no more than every seconds
        if (currentTimestamp - lastAnalyzedTimestamp >= 300) {
            if (image.image != null) {

                val extractedData = extractByteArrayFromUYV(image)
                val data = rotateYUV420Degree270(extractedData,image.width,image.height)
                val rescaledData = halveYUV420(data,imageHeight = image.height,imageWidth = image.width)


                val bitmap = convertUYVtoRGB(rescaledData, image.width / 2, image.height / 2, renderScript)

                //val rescaledBitmap = Bitmap.createScaledBitmap(bitmap,320,320,true)
                listener?.onAnalyze(bitmap, rescaledData)

                //viewmodel.setFaceDetectorBitmap(rescaledBitmap)

                lastAnalyzedTimestamp = System.currentTimeMillis()


            }
        }

    }



}

interface ImageAnalyzedListener{
    fun onAnalyze(bitmap: Bitmap, rescaledData: ByteArray)
}
