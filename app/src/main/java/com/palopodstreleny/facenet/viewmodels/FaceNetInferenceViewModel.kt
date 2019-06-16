package com.palopodstreleny.facenet.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.palopodstreleny.facenet.inference.FacePrediction
import com.palopodstreleny.facenet.model.FaceNetInferenceModel
import com.qualcomm.qti.snpe.NeuralNetwork

class FaceNetInferenceViewModel(private val model: FaceNetInferenceModel) : ViewModel() {


    private val _inputBitmap = MutableLiveData<Bitmap>()
    val croppedFace = _inputBitmap
    val facePrediction: LiveData<FacePrediction> = Transformations.switchMap(_inputBitmap){
        data -> model.predict(data)
    }

    fun setInputBitmap(bitmap: Bitmap){
        _inputBitmap.postValue(bitmap)
    }

    private val _mutableRuntime = MutableLiveData<NeuralNetwork.Runtime>()
    val runtime: LiveData<NeuralNetwork.Runtime> = Transformations.switchMap(_mutableRuntime){
        input -> model.network.changeRuntime(input)
    }

    fun changeRuntime(runtime: NeuralNetwork.Runtime): Boolean{
        if(model.network.runtime != runtime){
            _mutableRuntime.value = runtime
            return true
        }
        return false
    }



}