package com.palopodstreleny.facenet;

import android.graphics.Bitmap;
import android.graphics.Rect;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceContour;

public class Utils {

    public static String showInferenceTime(Long time){
        if(time == 0) return "";
        return "Network inference " + time + " ms";
    }

    public static String showInferenceUsername(String userName){
        if(userName == null) return "";
        return "User: "+ userName;
    }



    public static Bitmap getCroppedBitmap(FirebaseVisionFace face, Bitmap bitmap){

        //Firebase returns Points that could be negative or calculated width/height could be bigger
        FirebaseVisionFaceContour faceOval = face.getContour(FirebaseVisionFaceContour.FACE);

        int left = Math.round(faceOval.getPoints().get(28).getX());
        int right = Math.round(faceOval.getPoints().get(8).getX());
        int top = Math.round(faceOval.getPoints().get(31).getY());
        int bottom = Math.round(faceOval.getPoints().get(18).getY());

        Rect rect = new Rect(left,top,right,bottom);
        int width = rect.width();
        int height = rect.height();

        if(top + width <= bitmap.getWidth() &&
                left + height <= bitmap.getHeight() &&
                height > 0 &&
                width > 0 &&
                top > 0 &&
                left > 0 &&
                right < bitmap.getWidth()
                && bottom < bitmap.getHeight()) {

            Bitmap croppedBitmap = Bitmap.createBitmap(
                    bitmap,
                    left,
                    top,
                    width,
                    height
            );
            if(croppedBitmap.getWidth() > 96 && croppedBitmap.getHeight() > 96) {
                return croppedBitmap;
            }
        }
        return null;
    }

}
