package com.palopodstreleny.facenet.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.palopodstreleny.facenet.R
import com.palopodstreleny.facenet.viewmodels.MainActivityViewModel
import com.palopodstreleny.facenet.databinding.ActivityAppMainBinding
import org.koin.android.viewmodel.ext.android.viewModel

/**
 *
 * MainActivity, entry point of the app
 *
 */
class MainActivity : AppCompatActivity() {


    private val binding: ActivityAppMainBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_app_main) as ActivityAppMainBinding
    }

    private val viewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        //If model loaded, view become focusable so we can start new Activity, otherwise show downloading message
        binding.faceRecognitionExample.setOnClickListener{
            if(it.isFocusable){
                val intent = Intent(this,FaceNetInferenceActivity::class.java)
                startActivity(intent)
            }else{
                downloadingInProgress()
            }
        }
    }

    /**
     *
     * Helper method for showing message about model loading process
     *
     */
    private fun downloadingInProgress() {
        Snackbar.make(binding.root,getString(R.string.model_downloading_progress),Snackbar.LENGTH_LONG).show()
    }


}
