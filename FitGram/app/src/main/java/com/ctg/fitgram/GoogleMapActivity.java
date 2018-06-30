package com.ctg.fitgram;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;


import com.ctg.fitgram.model.Data;
import com.ctg.fitgram.model.Response;
import com.ctg.fitgram.network.RetrofitInterface;
import com.ctg.fitgram.services.ActivityRecognizedService;
import com.ctg.fitgram.services.TrackingService;
import com.ctg.fitgram.utils.LocationTracker;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback,SensorEventListener {

    private GoogleMap map;
    private Marker yourMarker;
    private Marker ARObjectMarker;
    ArrayList<LatLng> latLngList;
    PolylineOptions polylineOptions;
    private Location mPreviousLocation;
    private static Data data;
    double dist;
    double dist2;
    double speed;
    double distance;
    private Integer steps ;
    private Integer stepTxt;
    //float [] results = new float[5];
    private final static String TAG = GoogleMapActivity.class.getSimpleName();
    private Context context;
    private SensorManager sManager;
    private Sensor stepSensor;
    private double step;
    private Integer walkingtime;
    private Integer runningtime;
    private Integer cyclingtime;
    private Integer walkingtstep;
    private Integer runningdistance;
    private Integer cyclingdistance;
    private Integer date;
    public static final String URL = "https://fitnessrace.herokuapp.com/";
    //private Integer






    /**
     * The entry point for interacting with activity recognition.
     */
    private ActivityRecognitionClient mActivityRecognitionClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private BroadcastReceiver mReceiver;
    private String msg_for_me;
    private TextView activityText;
    private TextView placeText;
    private TextView walkingDistanceTxt;
    private TextView runningDistanceTxt;
    private TextView cyclingDistanceTxt;
    private TextView stepCounter;
    private Chronometer chronometerWalking;
    private Chronometer chronometerRunning;
    private Chronometer chronometerCycling;
    private Integer elapsedMillis;
    private Integer elapsedMillisWalking;
    private Integer elapsedMillisRunning;
    private Integer elapsedMillisCycling;
    private Double walkingDistance = 0.0;
    private Double runningDistance = 0.0;
    private Double cyclingDistance = 0.0;
    private  String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_google_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        data = new Data();
        activityText = findViewById(R.id.activityText);
        activityText.setVisibility(View.VISIBLE);
        placeText = findViewById(R.id.placeTextView);
        stepCounter = findViewById(R.id.stepCountTextView);
        runningDistanceTxt = findViewById(R.id.textViewRunningDistance);
        walkingDistanceTxt = findViewById(R.id.textViewWalkingDistance);
        cyclingDistanceTxt = findViewById(R.id.textViewCyclingDistance);
        chronometerWalking = findViewById(R.id.chronometerWalking);
        chronometerRunning = findViewById(R.id.chronometerRunning);
        chronometerCycling = findViewById(R.id.chronometerCycling);
        chronometerWalking.setBase(SystemClock.elapsedRealtime());
        chronometerRunning.setBase(SystemClock.elapsedRealtime());
        chronometerCycling.setBase(SystemClock.elapsedRealtime());
        email = getIntent().getExtras().getString("email");


        sManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);


    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions().position(new LatLng(49.8728277, 8.6490228)).title(getString(R.string.markerAnchor)));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(49.8728277, 8.6490228), 17));
        this.map = map;
        ARObjectMarker = map.addMarker(
                new MarkerOptions()
                        .position(new LatLng(49.8828732, 8.6693622))
                        .title(getString(R.string.markerARPosition))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ar)));


        map.addMarker(
                new MarkerOptions()
                        .position(new LatLng(49.8774965, 8.6523885))
                        .title(getString(R.string.markerARPosition))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ar)));

    }

    // updating marker on map
    private void updateMap(Location location) {
        if (yourMarker == null)

        {
            yourMarker = map.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(location.getLatitude(), location.getLongitude()))
                            .title(getString(R.string.markerYourPosition))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_position)));
        } else {
            yourMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
        }

        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
    }

    //function for drawing path on map
    private void drawPath() {
        if (map != null) {
            polylineOptions = new PolylineOptions();
            Log.d("demo", "latlonlist" + latLngList.toString());
            for (int i = 0; i < latLngList.size(); i++) {
                polylineOptions.add(latLngList.get(i));
                if (i == latLngList.size() - 1) {
                    if (yourMarker != null) {
                        yourMarker.remove();
                    }
                    yourMarker = map.addMarker(new MarkerOptions()
                            .position(latLngList.get(i))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                            .title("END"));
                }
            }
            float zoomlevel = 16.0f;
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngList.get(latLngList.size() - 1), zoomlevel));
            polylineOptions.width(10).color(Color.BLUE).geodesic(true);
            map.addPolyline(polylineOptions);

        }
    }

    //for stopping service
    public void stopTracking(View view)

    {
        Intent mIntent = new Intent(this, TrackingService.class);
        stopService(mIntent); // stop tracking service

//activity recogniton stuff
        Intent intent = new Intent(this, ActivityRecognizedService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        ActivityRecognitionClient activityRecognitionClient = ActivityRecognition.getClient(this);
        Task task = activityRecognitionClient.removeActivityUpdates(pendingIntent);
//showing distance and time

        chronometerWalking.stop();
        chronometerCycling.stop();
        chronometerRunning.stop();
        activityText.setVisibility(View.INVISIBLE);
        // off-topic -> ignore this
       // EventBus.getDefault().unregister(this);
        this.unregisterReceiver(mReceiver);
        sManager.unregisterListener(this, stepSensor);
        sendValues();

    }

    //for getting current place
    public void getCurrentPlace() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                    if (placeLikelihood.getLikelihood() > 0.5) {
                        placeText.setText(placeLikelihood.getPlace().getName());
                    }
                }

                likelyPlaces.release();
            }
        });


    }


    //
    public static Data getData() {
        return data;
    }
    //================== OFF_TOPIC FOR ADVANCED: EVENTBUS STUFF =================

    @Override
    protected void onStart() {
        super.onStart();
        sManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        Intent mIntent = new Intent(this, TrackingService.class);
        startService(mIntent); // start tracking service


        Toast.makeText(this, R.string.toast_servicestarted, Toast.LENGTH_SHORT).show();
        latLngList = new ArrayList<>();
        // off-topic -> ignore this
        EventBus.getDefault().register(this);
        //activity recognition stuff
        Intent intent = new Intent(this, ActivityRecognizedService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        ActivityRecognitionClient activityRecognitionClient = ActivityRecognition.getClient(this);
        Task task = activityRecognitionClient.requestActivityUpdates(3000, pendingIntent);
        //
        IntentFilter intentFilter = new IntentFilter(
                "msg");

        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                //extract our message from intent
                msg_for_me = intent.getStringExtra("msg");

                String tag = intent.getAction();
                //log our message value
                Log.i("msgs", tag);
                activityText.setText(msg_for_me);
                if (msg_for_me.contains("walking")) {
                    chronometerWalking.start();
                    chronometerCycling.stop();
                    chronometerRunning.stop();

                } else if (msg_for_me.contains("running")) {

                    chronometerWalking.stop();
                    chronometerCycling.stop();
                    chronometerRunning.start();

                } else if (msg_for_me.contains("cycling")) {
                    chronometerWalking.stop();
                    chronometerCycling.start();
                    chronometerRunning.stop();
                } else {
                    chronometerWalking.stop();
                    chronometerCycling.stop();
                    chronometerRunning.stop();

                }


            }
        };
        this.registerReceiver(mReceiver, intentFilter);
        //
        //


    }

    //  protected void onStop()
    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);

       /* Intent mIntent = new Intent(this, TrackingService.class);
        stopService(mIntent); // stop tracking service

//activity recogniton stuff
        Intent intent = new Intent( this, ActivityRecognizedService.class );
        PendingIntent pendingIntent = PendingIntent.getService( this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );
        ActivityRecognitionClient activityRecognitionClient = ActivityRecognition.getClient(this);
        Task task = activityRecognitionClient.removeActivityUpdates( pendingIntent);
//


        // off-topic -> ignore this
        EventBus.getDefault().unregister(this);*/
    }

    /**
     * Listen for new database entries from background service
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LocationTracker.LocationEvent event) {
        Location location = event.location;
        float[] results = new float[5];
        updateMap(location);
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        latLngList.add(latLng);
        Log.i("RawLocations", latLngList.toString());
        drawPath();
        getCurrentPlace();


        if (mPreviousLocation != null) {
            distance = location.distanceTo(mPreviousLocation);

            Location.distanceBetween(location.getLatitude(), location.getLongitude(), mPreviousLocation.getLatitude(),
                    mPreviousLocation.getLongitude(), results);
            dist += distance;
            //for distance calculation
            if (msg_for_me.contains("walking")) {

                walkingDistance += (distance / 1000);
                walkingDistanceTxt.setText(walkingDistance.toString());

            } else if (msg_for_me.contains("running")) {

                runningDistance += (distance / 1000);
                runningDistanceTxt.setText(runningDistance.toString());

            } else if (msg_for_me.contains("cycling")) {
                cyclingDistance += (distance / 1000);
                cyclingDistanceTxt.setText(cyclingDistance.toString());
            }
        }

        // speed = location.getSpeed();
        //  speed = data.getCurSpeed();
        //dist = data.getDistance();
        Log.i(TAG, " distance is " + dist);
        mPreviousLocation = location;
        //Toast.makeText(this, "New location: ("+location.getLatitude()+","+location.getLongitude()+")", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Distance is: " + dist + " meters", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        // sManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);

        super.onResume();


    }

    @Override
    protected void onStop() {
        super.onStop();
         // sManager.unregisterListener(this, stepSensor);
        EventBus.getDefault().unregister(this);
       // this.unregisterReceiver(mReceiver);
    }

    //for steps detection
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        float[] values = event.values;
        int value = -1;

        if (values.length > 0) {
            value = (int) values[0];
        }


        if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            steps++;
            stepTxt = steps;
            stepCounter.setText(stepTxt.toString());
            Toast.makeText(this, "step" + steps, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    //for sending data to server
    public void sendValues()
    {

       /* JSONArray pointsinjsonarray = new JSONArray();
        for (int i = 0; i < latLngList.size(); i++) {
            try {
                //put(int index, long value)
                pointsinjsonarray.put(i, latLngList.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        */
       // Log.i("msgs", pointsinjsonarray.toString());

        JSONObject locations = new JSONObject();

        for (int i = 0; i < latLngList.size(); i++) {

            try {
                locations.put("lat", latLngList.get(i).latitude);
                locations.put("long", latLngList.get(i).longitude);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

       /* ArrayList<LatLng>  latLngList2 = new ArrayList<>();
        JSONArray results = locations.getJSONArray();
        for(int i=0;i<locations.length();i++)
        {
            try
            {
                latLngList2.add();

            }
            catch (Exception e)
            {}
        }*/

        String substr=chronometerRunning.getText().toString().substring(0,2);
        runningtime= Integer.parseInt(substr);

        String substr1=chronometerWalking.getText().toString().substring(0,2);
        walkingtime= Integer.parseInt(substr1);

        String substr2=chronometerCycling.getText().toString().substring(0,2);
        cyclingtime= Integer.parseInt(substr2);

        //the following are not used anymore
        elapsedMillisWalking = (int) (SystemClock.elapsedRealtime() -chronometerWalking.getBase());
        elapsedMillisRunning = (int) (SystemClock.elapsedRealtime() -chronometerRunning.getBase());
        elapsedMillisCycling = (int) (SystemClock.elapsedRealtime() -chronometerCycling.getBase());
        elapsedMillis = elapsedMillisWalking + elapsedMillisRunning+elapsedMillisCycling;
        Log.i("TIME", elapsedMillis.toString());
        //till here
        step = ((1/0.00078)) * walkingDistance;

       // date=  (int) (SystemClock.elapsedRealtime());
        Date date1 = Calendar.getInstance().getTime();
        long millis = date1.getTime();
        date=  (int)millis;
        walkingtstep = (int) step;
        runningdistance = runningDistance.intValue();
        cyclingdistance =cyclingDistance.intValue();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

        Call<Response> call = retrofitInterface.updateUserActivity(email, date, walkingtstep , walkingDistance.intValue(), runningtime, runningdistance, cyclingtime,  cyclingdistance, locations);
        //
        call.enqueue(new Callback<Response>()
        {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {

                if (response.isSuccessful())
                {

                    Response responseBody = response.body();
                    Log.d("success", response.toString());
                    Log.d("success2", responseBody.toString());
                }
                else
                {

                    ResponseBody errorBody = response.errorBody();

                    // Gson gson = new Gson();
                    Gson gson = new GsonBuilder().create();

                    try
                    {
                        Log.d("error1", errorBody.toString());
                        //Response response1 = gson.fromJson(errorBody);

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        Log.d("error2", e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                Log.d(TAG, "onFailure: "+t.getLocalizedMessage());
            }
        });
        //





    }


}