package com.palopodstreleny.facenet.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.palopodstreleny.facenet.AppExecutor
import com.palopodstreleny.facenet.EmbeddedUser
import com.palopodstreleny.facenet.db.UserEmbedding
import com.palopodstreleny.facenet.db.entity.User
import com.palopodstreleny.facenet.inference.EmbeddingCreation
import com.palopodstreleny.facenet.model.FaceNetGenerationModel
import com.qualcomm.qti.snpe.NeuralNetwork

class FaceNetEmbeddingViewModel(private val model: FaceNetGenerationModel) : ViewModel() {


    private val _croppedImage = MutableLiveData<EmbeddedUser>()
    val cropedImage: LiveData<EmbeddedUser> = _croppedImage
    val embeddingCreated: LiveData<EmbeddingCreation> = Transformations.switchMap(_croppedImage){
            input -> model.generateEmbeddings(input.bitmap,input.name)
    }

    private val _mutableRuntime = MutableLiveData<NeuralNetwork.Runtime>()
    val runtime: LiveData<NeuralNetwork.Runtime> = Transformations.switchMap(_mutableRuntime){
            input -> model.network.changeRuntime(input)
    }


    fun setEmbeddedUser(user: EmbeddedUser){
        _croppedImage.value = user
    }

    fun getAllUsers(): LiveData<List<User>> {
        return model.userDao.getAllUsers()
    }

    fun getUserEmbeddings(): LiveData<List<UserEmbedding>> {
        return model.embeddingDao.getUserEmbeddingsCount()
    }

    fun saveUser(user: User){
        AppExecutor.backgroundIO().execute {
            model.userDao.insertUser(user)
        }
    }

    fun changeRuntime(runtime: NeuralNetwork.Runtime): Boolean{
        if(model.network.runtime != runtime){
            _mutableRuntime.value = runtime
            return true
        }
        return false
    }

    fun deleteAllEmbeddings(){
        AppExecutor.backgroundIO().execute{
            model.userDao.deleteAllUsers()
            model.embeddingDao.deleteAllEmbeddings()
        }
    }


}