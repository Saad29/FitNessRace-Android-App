package com.ctg.fitgram.utils;

/**
 * Created by syeds on 3/6/2018.
 */
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctg.fitgram.FriendActivity;
import com.ctg.fitgram.R;
import com.ctg.fitgram.TimelineActivity;
import com.ctg.fitgram.model.Activity;
import com.ctg.fitgram.model.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class DataAdapterForTimeline extends RecyclerView.Adapter<DataAdapterForTimeline.ViewHolder> implements OnMapReadyCallback
{
    private ArrayList<Activity> data;
    private Context context;
    FragmentManager fragmentManager;
    private GoogleMap map;
    ArrayList<LatLng> latLngList;
    PolylineOptions polylineOptions;
    private Marker yourMarker;
    private JSONArray locs;
    private JSONObject jsonObj;
    private String lat;
    private String lon;
   // public DataAdapterForTimeline(ArrayList<Activity> data,FragmentManager fragmentManager)
   public DataAdapterForTimeline(ArrayList<Activity> data)
    {
        this.context = context;
        this.data= data;
        this.fragmentManager = fragmentManager;

    }

    @Override
    public DataAdapterForTimeline.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cdrow, viewGroup, false);
        return new DataAdapterForTimeline.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapterForTimeline.ViewHolder viewHolder, int i) {

        // viewHolder.tv_name.setText(data.get(i).getName());
        //Picasso.with(context).load("https://fitnessrace.herokuapp.com/images/" + data.get(i).getEmail() + ".jpg").resize(120, 60).into(viewHolder.img_android);
        //viewHolder.linkBtn.setText("See profile");
        viewHolder.mapView.getMapAsync(this);
        if (data.get(i).getWalking().getSteps() != null)
            viewHolder.stepTxt.setText(data.get(i).getWalking().getSteps().toString());
        if (data.get(i).getWalking().getDistance() != null)
            viewHolder.walkingTime.setText(data.get(i).getWalking().getDistance().toString());
        if (data.get(i).getRunning().getDistance() != null)
            viewHolder.runningDist.setText(data.get(i).getRunning().getDistance().toString());
        if (data.get(i).getBiking().getDiistance() != null)
            viewHolder.cyclingDist.setText(data.get(i).getBiking().getDiistance().toString());
        if (data.get(i).getWalking().getTime() != null)
            viewHolder.walkingTime.setText(data.get(i).getWalking().getTime().toString());
        if (data.get(i).getRunning().getTime() != null)
            viewHolder.runningTime.setText(data.get(i).getRunning().getTime().toString());
        if (data.get(i).getBiking().getTime() != null)
            viewHolder.bikingTime.setText(data.get(i).getBiking().getTime().toString());


        if (data.get(i).getDate().toString() != null)
        {
           Integer d=   Integer.parseInt(data.get(i).getDate().toString());
           String date= getDate(d, "dd/MM/yyyy hh:mm:ss.SSS");
            viewHolder.dateTxt.setText(date);
        }




        if(data.get(i).getLocation()!=null)

        {

            try
            {

                jsonObj = new JSONObject(data.get(i).getLocation().toString());
                lat  = jsonObj.getString("lat");
                lon  = jsonObj.getString("long");
                LatLng locationx = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
                latLngList.add( locationx);
                drawPath();

            }
            catch (Exception e)
            {

            }
        }





    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView stepTxt,runningDist,walkingTime,runningTime,bikingTime,dateTxt;
        private TextView cyclingDist;
        private MapView mapView;
        public ViewHolder(View view) {
            super(view);

            stepTxt = (TextView)view.findViewById(R.id.stepTxt);
            runningDist = (TextView)view.findViewById(R.id.runningDist);
            cyclingDist = (TextView)view.findViewById(R.id.cyclingDist);
            walkingTime = (TextView)view.findViewById(R.id.walkingTime);
            runningTime = (TextView)view.findViewById(R.id.runningTime);
            bikingTime = (TextView)view.findViewById(R.id.bikingTime);
            dateTxt= (TextView)view.findViewById(R.id.dateTxt);
            mapView =(MapView)view.findViewById(R.id.mapView);
            //android.support.v4.app.Fragment mapFragment = new android.support.v4.app.Fragment();
             //fragmentManager.beginTransaction().replace(R.id.map,  mapFragment).commit();

//            itemView.setOnClickListener(this);
                mapView.onCreate(null);
                //mapView.getMapAsync(context);

            }




        }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
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

    @Override
    public void onMapReady(GoogleMap map) {
       // this.map = map;
        map.addMarker(new MarkerOptions().position(new LatLng(49.8728277, 8.6490228)));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(49.8728277, 8.6490228), 17));
        this.map = map;

    }

    // updating marker on map
    private void updateMap(Location location) {
        if (yourMarker == null)

        {
            yourMarker = map.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(location.getLatitude(), location.getLongitude()))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_position)));
        } else {
            yourMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
        }

        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
    }






}
