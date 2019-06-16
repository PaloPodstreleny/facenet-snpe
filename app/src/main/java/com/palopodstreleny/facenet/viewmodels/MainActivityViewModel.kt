package com.palopodstreleny.facenet.viewmodels

import androidx.lifecycle.*
import com.palopodstreleny.facenet.AppExecutor
import com.palopodstreleny.facenet.model.ModelFileLoader

/**
 *
 * MainActivityViewModel load faceNet model on background thread
 * and don't allow user to progress further till model is not loaded
 *
 */
class MainActivityViewModel(private val lazyModelLoader: Lazy<ModelFileLoader>) : ViewModel(){

    private val modelLoader: ModelFileLoader by lazy { lazyModelLoader.value }


    private val _faceNetModel = MutableLiveData<Boolean>()

    init {
        _faceNetModel.value = true
    }

    val faceNetModelAvailable: LiveData<Boolean> = Transformations.switchMap(_faceNetModel){
        val data = MutableLiveData<Boolean>()

        //load model in background
        AppExecutor.backgroundIO().execute{
            modelLoader.modelFile
            data.postValue(true)
        }

        data
    }

}