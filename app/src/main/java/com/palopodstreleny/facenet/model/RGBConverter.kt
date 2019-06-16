package com.palopodstreleny.facenet.model

import android.graphics.Bitmap
import androidx.annotation.WorkerThread
import com.qualcomm.qti.snpe.FloatTensor

/**
 *
 * Interface responsible for converting bitmap to RGB
 *
 */
interface RGBConverter {

    /**
     *
     * Method for processing bitmap and retrieving rgb values of very pixel
     * Retrieved values are added to tensor
     *
     * @param image: Bitmap for model
     * @param tensor: FloatTensor holder for rgb values
     *
     */
    @WorkerThread
    fun processBitmapARGB8888(image: Bitmap, tensor: FloatTensor) {
        val pixels = IntArray(image.width * image.height)

        image.getPixels(
            pixels, 0, image.width, 0, 0,
            image.width, image.height
        )

        for (y in 0 until image.height) {
            for (x in 0 until image.width) {
                val pixel = pixels[y * image.width + x]

                val b = (pixel and 0xFF)
                val g = (pixel shr 8 and 0xFF)
                val r = (pixel shr 16 and 0xFF)

                val pixelFloats = floatArrayOf(r / 255f, g  / 255f,b / 255f)

                tensor.write(pixelFloats, 0, pixelFloats.size, 0, y, x)
            }
        }


    }

}