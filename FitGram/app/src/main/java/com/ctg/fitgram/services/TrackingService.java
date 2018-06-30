package com.ctg.fitgram.services;

/**
 * Created by syeds on 1/29/2018.
 */

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import org.jetbrains.annotations.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

 import com.ctg.fitgram.GoogleMapActivity;
import com.ctg.fitgram.R;
import com.ctg.fitgram.utils.LocationTracker;

/**
 * Tracking service where we register
 * the sensor listeners
 */
public class TrackingService extends Service
{

    private final static int SERVICE_NOTIFICATION_ID = 123456;

    private LocationTracker locationTracker;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        locationTracker = new LocationTracker(this);
        locationTracker.start();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_notification_icon)
                        .setContentTitle(getString(R.string.notification_title));

        Intent resultIntent = new Intent(this, GoogleMapActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(GoogleMapActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        startForeground(SERVICE_NOTIFICATION_ID, mBuilder.build());

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if(locationTracker != null) locationTracker.stop();
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {return null;}
}
