package com.palopodstreleny.facenet.ui

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.TextureView
import android.view.View
import android.widget.Toast
import androidx.camera.core.CameraX
import androidx.core.app.ActivityCompat
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector
import com.palopodstreleny.facenet.Camera
import com.palopodstreleny.facenet.R
import com.palopodstreleny.facenet.Utils
import com.palopodstreleny.facenet.databinding.ActivityFaceNetBinding
import com.palopodstreleny.facenet.hide
import com.palopodstreleny.facenet.ui.analyzer.ImageAnalyzedListener
import com.palopodstreleny.facenet.ui.analyzer.ImageAnalyzer
import com.palopodstreleny.facenet.viewmodels.FaceNetInferenceViewModel
import com.qualcomm.qti.snpe.NeuralNetwork
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

/**
 *
 * Activity responsible for showing actual inference of model
 *
 */
class FaceNetInferenceActivity : BaseActivity<ActivityFaceNetBinding>(), ImageAnalyzedListener {

    //Camera permission
    private val requiredPermissions = arrayOf(Manifest.permission.CAMERA)
    private val requestCodePermission = 10

    //Embedding menu item
    private var isEmbeddingMenuItemCreated = true
    private val embeddingMenuItemID = 12345

    //Injection of important objects
    private val viewmodel: FaceNetInferenceViewModel by viewModel()
    private val camera: Camera by inject()
    private val analyzer: ImageAnalyzer by inject()
    private val detector: FirebaseVisionFaceDetector by inject()
    private val metadata: FirebaseVisionImageMetadata by inject()

    private lateinit var viewFinder: TextureView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //ViewFinder camera view
        viewFinder = binding.viewFinder

        //Set lifecycle owner and viewmodel
        binding.lifecycleOwner = this
        binding.viewmodel = viewmodel

        //Set listener for analyzer
        analyzer.listener = this

        //Show message when runtime change
        this.viewmodel.runtime.observe(this, Observer {
            if (it != null) {
                showSnackBar(getString(R.string.runtime_changed, it.name))
            }
        })

        //Set image view of cropped face that is used as input for neural network
        this.viewmodel.croppedFace.observe(this, Observer {
            if (it != null) {
                binding.imageBitmap.setImageBitmap(it)
            }
        })


        // Every time the provided texture view changes, recompute layout
        viewFinder.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            camera.updateTransform(viewFinder)
        }

        //Check if camera permission is not granted
        if (!camera.allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this, requiredPermissions, requestCodePermission
            )
        }

        //When start button is clicked hide the button and show all details and camera view
        //Moreover check if permissions are granted and run camera
        binding.buttonStart.setOnClickListener {
            binding.cardViewHolder.visibility = View.VISIBLE
            binding.viewFinder.visibility = View.VISIBLE
            it.hide()

            //Start camera
            if (camera.allPermissionsGranted()) {
                viewFinder.post {
                    val cameraBag = camera.startCamera(viewFinder)
                    CameraX.bindToLifecycle(this, cameraBag.preview, cameraBag.analysis)
                }
            }
        }

    }


    override fun setUpBinding(): ActivityFaceNetBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_face_net)
    }

    override fun changeRuntime(runtime: NeuralNetwork.Runtime) {
        if (!this.viewmodel.changeRuntime(runtime)) {
            showSnackBar(getString(R.string.runtime_selected, runtime.name))
        }
    }

    override fun setToolbarView(): View {
        return binding.myToolbar
    }

    override fun setTitleOfToolbar(): String {
        return getString(R.string.facenet_title)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            embeddingMenuItemID -> {
                val intent = Intent(this, FaceNetRecordingEmbeddingsActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        //Add person button to appbar
        if (isEmbeddingMenuItemCreated) {
            menu.add(2000, embeddingMenuItemID, Menu.NONE, R.string.facenet_recorder).icon =
                getDrawable(R.drawable.ic_person_black_24dp)

            val item = menu[menu.size() - 1]
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            isEmbeddingMenuItemCreated = false
        }
        return super.onPrepareOptionsMenu(menu)
    }


    @Synchronized
    override fun onAnalyze(bitmap: Bitmap, rescaledData: ByteArray) {

        val image = FirebaseVisionImage.fromByteArray(rescaledData, metadata)
        detector.detectInImage(image).addOnSuccessListener { faces ->

            for ((index, face) in faces.withIndex()) {

                if (index == 0) {
                    val newBitmap = Utils.getCroppedBitmap(face, bitmap)
                    if (newBitmap != null) {
                        viewmodel.setInputBitmap(newBitmap)
                    }
                }

            }
        }

    }

    /**
     * Process result from permission request dialog box, has the request
     * been granted? If yes, start Camera. Otherwise display a Snack
     */
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == requestCodePermission) {
            if (camera.allPermissionsGranted()) {
                viewFinder.post { camera.startCamera(viewFinder) }
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }


}
