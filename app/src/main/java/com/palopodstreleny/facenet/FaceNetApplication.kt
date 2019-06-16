package com.palopodstreleny.facenet


import android.app.Application
import com.palopodstreleny.facenet.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class FaceNetApplication : Application(){


    override fun onCreate() {
        super.onCreate()

        startKoin {

            androidLogger()

            androidContext(this@FaceNetApplication)

            // dependency injection modules
            modules(listOf(
                neuralNetworkModule,
                mainActivityModule,
                faceNetInferenceActivityModule,
                databaseModule,
                firebaseModule,
                cameraModule,
                faceNetGenerationActivity
            ))
        }

    }


}