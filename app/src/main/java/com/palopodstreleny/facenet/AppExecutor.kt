package com.palopodstreleny.facenet

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors


object AppExecutor {

    //The current SDK INeuralNetwork class instances are meant to be accessed from a single thread.
    //Developers must make sure that is enforced within the application or unexpected errors may occur.
    private val backgroundIO = Executors.newFixedThreadPool(50)

    private val faceNet = Executors.newSingleThreadExecutor()
    private val faceNetAdd = Executors.newSingleThreadExecutor()

    private val mainThread = MainThreadExecutor()


    fun backgroundIO(): Executor {
        return backgroundIO
    }

    fun getFaceNetExecutor(): Executor{
        return faceNet
    }

    fun getEmbeddingAddExecutor(): Executor{
        return faceNetAdd
    }


    fun mainThread(): Executor {
        return mainThread
    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }


}