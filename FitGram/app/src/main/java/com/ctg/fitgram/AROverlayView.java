package com.ctg.fitgram;

/**
 * Created by syeds on 3/5/2018.
 */
import android.*;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.opengl.Matrix;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;


import com.ctg.fitgram.helper.LocationHelper;
import com.ctg.fitgram.model.ARPoint;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;


public class AROverlayView extends View {

    Context context;
    private float[] rotatedProjectionMatrix = new float[16];
    private Location currentLocation;
    private List<ARPoint> arPoints;
    private Drawable d;
    private PlaceDetectionClient mPlaceDetectionClient;
    private final static String TAG = AROverlayView.class.getSimpleName();
    private String placeName="xyz";


    public AROverlayView(Context context) {
        super(context);

        this.context = context;
        mPlaceDetectionClient = Places.getPlaceDetectionClient(context, null);
        getPlaces();
        //Demo points
        arPoints = new ArrayList<ARPoint>() {{
            //add(new ARPoint("", 3.1850, 101.6868, 0));
           // add(new ARPoint("", 3.1579, 101.7116, 0));
            add(new ARPoint("Alfred", 8.6693622, 49.8828732, 0));
            add(new ARPoint("Piloty", 8.6523885, 49.8774965, 0));
        }};
    }

    public void updateRotatedProjectionMatrix(float[] rotatedProjectionMatrix) {
        this.rotatedProjectionMatrix = rotatedProjectionMatrix;
        this.invalidate();
    }

    public void updateCurrentLocation(Location currentLocation){
        this.currentLocation = currentLocation;
        this.invalidate();
    }
//get places
public void getPlaces()
{
    //for detecting the place
    if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        return;
    }
    Task<PlaceLikelihoodBufferResponse> placeResult = mPlaceDetectionClient.getCurrentPlace(null);
    placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
        @Override
        public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
            PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
            for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                Log.i(TAG, String.format("Place '%s' has likelihood: %g",
                        placeLikelihood.getPlace().getName(),
                        placeLikelihood.getLikelihood()));
                if (placeLikelihood.getLikelihood() > 0.5)
                {

                    if(placeLikelihood.getPlace().getName().toString().contains("Alfred")  || placeLikelihood.getPlace().getName().toString().contains("Piloty") )
                    {
                        placeName =placeLikelihood.getPlace().getName().toString();
                    }
                }
            }

            likelyPlaces.release();
        }
    });

}


    public void removeOverlay()
    {
        currentLocation = null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (currentLocation == null) {
            return;
        }

        final int radius = 30;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setTextSize(60);

        for (int i = 0; i < arPoints.size(); i ++) {
            float[] currentLocationInECEF = LocationHelper.WSG84toECEF(currentLocation);
            float[] pointInECEF = LocationHelper.WSG84toECEF(arPoints.get(i).getLocation());
            //float[] pointInECEF = LocationHelper.WSG84toECEF(currentLocation);
            float[] pointInENU = LocationHelper.ECEFtoENU(currentLocation, currentLocationInECEF, pointInECEF);

            float[] cameraCoordinateVector = new float[4];
            Matrix.multiplyMV(cameraCoordinateVector, 0, rotatedProjectionMatrix, 0, pointInENU, 0);

            // cameraCoordinateVector[2] is z, that always less than 0 to display on right position
            // if z > 0, the point will display on the opposite
            if (cameraCoordinateVector[2] < 0) {
                float x  = (0.5f + cameraCoordinateVector[0]/cameraCoordinateVector[3]) * canvas.getWidth();
                float y = (0.5f - cameraCoordinateVector[1]/cameraCoordinateVector[3]) * canvas.getHeight();

                 d = null;

                if(arPoints.get(i).getName().equals("Alfred"))
               // if( placeName.contains("") || placeName.contains("Alfred") )
                {
                    d = ContextCompat.getDrawable(context, R.drawable.athena);
                }
                else
                {
                    // d = ContextCompat.getDrawable(context, R.drawable.quote);
                }

                if(d!=null)
                {
                    Bitmap myBitmap = ((BitmapDrawable)d).getBitmap();


                    canvas.drawBitmap(myBitmap, x - (30 * arPoints.get(i).getName().length() / 2), y - 80, paint);
                    //canvas.drawCircle(x, y, radius, paint);
                    canvas.drawText(arPoints.get(i).getName(), x - (30 * arPoints.get(i).getName().length() / 2), y - 80, paint);

                }


            }
        }
    }
}
