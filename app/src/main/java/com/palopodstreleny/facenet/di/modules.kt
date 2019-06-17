package com.palopodstreleny.facenet.di

import android.renderscript.RenderScript
import android.util.Rational
import android.util.Size
import androidx.camera.core.CameraX
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysisConfig
import androidx.camera.core.PreviewConfig
import androidx.room.Room
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.palopodstreleny.facenet.AppExecutor
import com.palopodstreleny.facenet.Camera
import com.palopodstreleny.facenet.db.FaceNetDB
import com.palopodstreleny.facenet.model.FaceNetGenerationModel
import com.palopodstreleny.facenet.model.FaceNetInferenceModel
import com.palopodstreleny.facenet.model.ModelFileLoader
import com.palopodstreleny.facenet.model.Network
import com.palopodstreleny.facenet.ui.analyzer.ImageAnalyzer
import com.palopodstreleny.facenet.viewmodels.FaceNetEmbeddingViewModel
import com.palopodstreleny.facenet.viewmodels.FaceNetInferenceViewModel
import com.palopodstreleny.facenet.viewmodels.MainActivityViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 *
 * Dependency injection file contains different modules
 *
 */

const val modelName = "please.dlc"
const val dbName = "facenet_db"
const val size = 320

//Neural network module declaration
val neuralNetworkModule = module {

    //Create Singleton for ModelFileLoader -> should be use as lazy parameter
    single {ModelFileLoader(modelName,get()) }

    //Singleton for Network
    single { Network(context = androidApplication(),model = get<ModelFileLoader>().modelFile,executor = AppExecutor.getEmbeddingAddExecutor()) }

}

val mainActivityModule = module{
    viewModel { MainActivityViewModel(lazy {  get<ModelFileLoader>() }) }
}

val faceNetInferenceActivityModule = module {

    //FaceNetInferenceModel
    single {FaceNetInferenceModel(get(),AppExecutor.getFaceNetExecutor(),get())}

    viewModel { FaceNetInferenceViewModel(get()) }
}

val faceNetGenerationActivity = module {

    single {FaceNetGenerationModel(get(),get(),AppExecutor.getEmbeddingAddExecutor(),get())}

    viewModel { FaceNetEmbeddingViewModel(get()) }
}

//Database module
val databaseModule = module {

    //Create singleton for database
    single { Room.databaseBuilder(androidApplication(), FaceNetDB::class.java, dbName).build() }

    //Single instance of UserDao
    single { get<FaceNetDB>().userDao()}

    //Single  instance of EmbeddingDao
    single {get<FaceNetDB>().embeddingDao()}

}


val firebaseModule = module{

    single<FirebaseVisionFaceDetectorOptions> { FirebaseVisionFaceDetectorOptions.Builder()
        .setPerformanceMode(FirebaseVisionFaceDetectorOptions.FAST)
        .setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CONTOURS)
        .build() }

    single<FirebaseVisionFaceDetector> {FirebaseVision.getInstance().getVisionFaceDetector(get())}

    single<FirebaseVisionImageMetadata> {FirebaseVisionImageMetadata.Builder().setWidth(size).setHeight(size).setFormat(
        FirebaseVisionImageMetadata.IMAGE_FORMAT_YV12).build()}

}

val cameraModule = module {

    single { ImageAnalyzer(RenderScript.create(androidContext())) }
    single { Camera(get(),androidApplication(),get(),get()) }
    single { PreviewConfig.Builder().apply {
        setTargetAspectRatio(Rational(1, 1))
        setLensFacing(CameraX.LensFacing.FRONT)
        setTargetResolution(Size(640,640)) }.build() }

    single { ImageAnalysisConfig.Builder().apply {
        setTargetAspectRatio(Rational(1, 1))
        setLensFacing(CameraX.LensFacing.FRONT)
        setTargetResolution(Size(640,640))
        setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
    }.build() }



}


