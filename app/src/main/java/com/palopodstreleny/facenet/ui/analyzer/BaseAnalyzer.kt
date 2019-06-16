package com.palopodstreleny.facenet.ui.analyzer

import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicYuvToRGB
import androidx.annotation.WorkerThread
import androidx.camera.core.ImageProxy

abstract class BaseAnalyzer{

    /**
     *
     * Helper method for converting cameras YUV to RGB format
     *
     */
    @WorkerThread
    protected fun convertUYVtoRGB(data: ByteArray,width: Int, height: Int,renderScript: RenderScript): Bitmap {
        val yuvToRgbIntrinsic = ScriptIntrinsicYuvToRGB.create(renderScript, Element.YUV(renderScript))

        val aIn = Allocation.createSized(renderScript, Element.U8(renderScript), height*width*3/2)

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val aOut = Allocation.createFromBitmap(renderScript, bitmap)
        yuvToRgbIntrinsic.setInput(aIn)


        aIn.copyFrom(data)
        yuvToRgbIntrinsic.forEach(aOut)
        aOut.copyTo(bitmap)


        return bitmap
    }



    /**
     *
     * Helper method for extracting Bytes from YUV
     *
     */
    @WorkerThread
    protected fun extractByteArrayFromUYV(image: ImageProxy): ByteArray{
        //Get image planes
        val y = image.planes[0]
        val u = image.planes[1]
        val v = image.planes[2]


        val ySize = y.buffer.remaining()
        val uSize = u.buffer.remaining()
        val vSize = v.buffer.remaining()


        val data = ByteArray(ySize + uSize + vSize)

        y.buffer.get(data, 0, ySize)
        v.buffer.get(data, ySize, vSize)
        u.buffer.get(data, ySize + vSize, uSize)

        return data

    }

    /**
     *
     * Helper method for rotating YUV byteArray by 270 degrees
     *
     */
     fun rotateYUV420Degree270(data: ByteArray, imageWidth: Int, imageHeight: Int): ByteArray {
        val yuv = ByteArray(imageWidth * imageHeight * 3 / 2)
        val nWidth = 0
        val nHeight = 0
        var wh = 0
        var uvHeight = 0
        if (imageWidth != nWidth || imageHeight != nHeight) {
            wh = imageWidth * imageHeight
            uvHeight = imageHeight shr 1// uvHeight = height / 2
        }

        var k = 0
        for (i in 0 until imageWidth) {
            var nPos = 0
            for (j in 0 until imageHeight) {
                yuv[k] = data[nPos + i]
                k++
                nPos += imageWidth
            }
        }
        var i = 0
        while (i < imageWidth) {
            var nPos = wh
            for (j in 0 until uvHeight) {
                yuv[k] = data[nPos + i]
                yuv[k + 1] = data[nPos + i + 1]
                k += 2
                nPos += imageWidth
            }
            i += 2
        }
        return rotateYUV420Degree180(yuv, imageWidth, imageHeight)
    }

    /**
     *
     * Helper method for rotating YUV byteArray by 180 degrees
     *
     */
    private fun rotateYUV420Degree180(data: ByteArray, imageWidth: Int, imageHeight: Int): ByteArray {
        val yuv = ByteArray(imageWidth * imageHeight * 3 / 2)
        var i = 0
        var count = 0
        i = imageWidth * imageHeight - 1
        while (i >= 0) {
            yuv[count] = data[i]
            count++
            i--
        }
        i = imageWidth * imageHeight * 3 / 2 - 1
        i = imageWidth * imageHeight * 3 / 2 - 1
        while (i >= imageWidth * imageHeight) {
            yuv[count++] = data[i - 1]
            yuv[count++] = data[i]
            i -= 2
        }
        return yuv
    }


    /**
     *
     * Helper method for shrinking size of ByteArray by halve
     *
     */
    fun halveYUV420(data: ByteArray, imageWidth: Int, imageHeight: Int): ByteArray {
        val yuv = ByteArray(imageWidth / 2 * imageHeight / 2 * 3 / 2)
        // halve yuma
        var i = 0
        run {
            var y = 0
            while (y < imageHeight) {
                var x = 0
                while (x < imageWidth) {
                    yuv[i] = data[y * imageWidth + x]
                    i++
                    x += 2
                }
                y += 2
            }
        }
        // halve U and V color components
        var y = 0
        while (y < imageHeight / 2) {
            var x = 0
            while (x < imageWidth) {
                yuv[i] = data[imageWidth * imageHeight + y * imageWidth + x]
                i++
                yuv[i] = data[imageWidth * imageHeight + y * imageWidth + (x + 1)]
                i++
                x += 4
            }
            y += 2
        }
        return yuv
    }



}