package com.ctg.fitgram;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.TextView;
import android.os.ResultReceiver;

import com.ctg.fitgram.services.ActivityRecognizedService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;

public class ActivityRecognitionActivity extends AppCompatActivity  implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{

    public GoogleApiClient mApiClient;

private BroadcastReceiver mReceiver;
private String activityNumber;
    private    String msg_for_me;
    Chronometer chronometerWalking;
    Chronometer chronometerRunning;
    int i=0;
    @Override
    protected void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();


    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //unregister our receiver
        this.unregisterReceiver(this.mReceiver);
    }
    //
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognition);
        chronometerWalking = findViewById(R.id.chronometerWalking);
        chronometerRunning = findViewById(R.id.chronometerRunning);

        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mApiClient.connect();

    }
    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        Intent intent = new Intent( this, ActivityRecognizedService.class );
        PendingIntent pendingIntent = PendingIntent.getService( this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates( mApiClient, 3000, pendingIntent );

        //
        IntentFilter intentFilter = new IntentFilter(
                "msg");

        mReceiver = new BroadcastReceiver()
        {

            @Override
            public void onReceive(Context context, Intent intent)
            {
                //extract our message from intent
            msg_for_me = intent.getStringExtra("msg");

                String tag = intent.getAction();
                Log.i("msgs", tag);

                if(msg_for_me.contains("walking"))
                {
                    chronometerWalking.start();
                   chronometerRunning.stop();

                }

                else
                {
                    chronometerRunning.start();
                    chronometerWalking.stop();


                }
                //log our message value
                final   TextView textView = (TextView) findViewById(R.id.ActivityText);
                Log.i("msgs", msg_for_me);
                textView.setText(msg_for_me);


            }
        };
        this.registerReceiver(mReceiver, intentFilter);
        //
    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)

    {

    }
}

