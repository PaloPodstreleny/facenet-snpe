package com.palopodstreleny.facenet

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.view.Surface
import android.view.TextureView
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.core.content.ContextCompat
import com.palopodstreleny.facenet.ui.analyzer.ImageAnalyzer

class Camera(
    private val faceAnalyzer: ImageAnalyzer,
    private val context: Application,
    private val previewConfig: PreviewConfig,
    private val analyzerConfig: ImageAnalysisConfig) {

    //Camera permission list
    private val requiredPermissions = arrayOf(Manifest.permission.CAMERA)


    fun startCamera(viewFinder: TextureView): CameraBag {

        previewConfig.targetResolution.width

        // Build the viewfinder use case
        val preview = Preview(previewConfig)

        // Every time the viewfinder is updated, recompute layout
        preview.setOnPreviewOutputUpdateListener {

            // To update the SurfaceTexture, we have to remove it and re-add it
            val parent = viewFinder.parent as ViewGroup
            parent.removeView(viewFinder)
            parent.addView(viewFinder, 0)

            viewFinder.surfaceTexture = it.surfaceTexture
            updateTransform(viewFinder)
        }

        //Analyzer config
        val imageAnalysis = ImageAnalysis(analyzerConfig)
        imageAnalysis.analyzer = faceAnalyzer

        return CameraBag(preview,imageAnalysis)
    }


    fun updateTransform(viewFinder: TextureView) {
        val matrix = Matrix()

        // Compute the center of the view finder
        val centerX = viewFinder.width / 2f
        val centerY = viewFinder.height / 2f

        // Correct preview output to account for display rotation
        val rotationDegrees = when (viewFinder.display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }
        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)

        // Finally, apply transformations to our TextureView
        viewFinder.setTransform(matrix)
    }

    /**
     * Check if all permission specified in the manifest have been granted
     */
    fun allPermissionsGranted(): Boolean {
        for (permission in requiredPermissions) {
            if (ContextCompat.checkSelfPermission(
                    context, permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }


}

data class CameraBag(val preview: Preview, val analysis: ImageAnalysis)
