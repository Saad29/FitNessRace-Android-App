package com.ctg.fitgram;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ctg.fitgram.model.Activity;
import com.ctg.fitgram.model.Response;
import com.ctg.fitgram.model.ResponseTimeline;
import com.ctg.fitgram.model.User;
import com.ctg.fitgram.network.NetworkUtil;
import com.ctg.fitgram.network.RetrofitInterface;
import com.ctg.fitgram.utils.Constants;
import com.ctg.fitgram.utils.DataAdapter;
import com.ctg.fitgram.utils.DataAdapterForTimeline;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class TimelineActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener

{
//
    private TextView timeLineName;
    private SharedPreferences mSharedPreferences;
    private String mToken;
    private String mEmail;
    private String email;
    private CompositeSubscription mSubscriptions;
    private  Integer rewards;
    private  Integer achievements;
    private RecyclerView recyclerView;
    private ArrayList<Activity> data;
    private DataAdapterForTimeline adapter;
    public static final String URL = "https://fitnessrace.herokuapp.com/";
    public static final String TAG = TimelineActivity.class.getSimpleName();
    private ImageView imageview;
//

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        mSubscriptions = new CompositeSubscription();
        initViews();
        initSharedPreferences();
        loadProfile();
        showProfile();
        //



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
//
    private void initViews()
    {
        timeLineName = (TextView) findViewById(R.id.nameText);

        recyclerView = (RecyclerView)findViewById(R.id.card_recycler_viewT);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        imageview= (ImageView)findViewById(R.id.imageview);

      //  mBtChangePassword.setOnClickListener(view -> showDialog());
       // mBtLogout.setOnClickListener(view -> logout());
    }

    private void initSharedPreferences()
    {

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = mSharedPreferences.getString(Constants.TOKEN,"");
        mEmail = mSharedPreferences.getString(Constants.EMAIL,"");
    }

    private void loadProfile() {

        mSubscriptions.add(NetworkUtil.getRetrofit(mToken).getProfile(mEmail)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));


    }

    private void handleResponse(User user)
    {


        timeLineName.setText("Welcome " + user.getName());
       // mTvEmail.setText(user.getEmail());
       // mTvDate.setText(user.getCreated_at());
        rewards =user.getRewards();
        achievements =user.getAchievements();
        email =user.getEmail();

    }


    private void handleError(Throwable error)
    {
  /*
        if (error instanceof HttpException) {

            Gson gson = new GsonBuilder().create();

            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                Response response = gson.fromJson(errorBody,Response.class);
                showSnackBarMessage(response.getMessage());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            showSnackBarMessage("Network Error !");
        }
        */
    }




    public  void showProfile()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

        Call<ResponseTimeline> call = retrofitInterface. getUserActivity(mEmail);
        //
        call.enqueue(new Callback<ResponseTimeline>()
        {
            @Override
            public void onResponse(Call<ResponseTimeline> call, retrofit2.Response<ResponseTimeline> response)
            {

                //mProgressBar.setVisibility(View.GONE);

                if (response.isSuccessful())
                {

                    ResponseTimeline responseBody = response.body();
                    data = new ArrayList<>(Arrays.asList(responseBody.getData()));
                    adapter = new DataAdapterForTimeline(data);
                    recyclerView.setAdapter(adapter);

                    Log.d("success", response.toString());
                    Log.d("success2", responseBody.toString());
                }
                else
                {

                    ResponseBody errorBody = response.errorBody();

                    Gson gson = new Gson();

                    try
                    {
                        Log.d("error1", response.toString());
                        //Response errorResponse = gson.fromJson(errorBody.string(), Response.class);
                        //  Snackbar.make(findViewById(R.id.content), errorResponse.getMessage(),Snackbar.LENGTH_SHORT).show();

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        Log.d("error2", e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseTimeline> call, Throwable t) {

                //mProgressBar.setVisibility(View.GONE);
                Log.d(TAG, "onFailure: "+t.getLocalizedMessage());
            }
        });
        //
    }

    private void logout()
    {

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.EMAIL,"");
        editor.putString(Constants.TOKEN,"");
        editor.apply();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
       // finish();
    }
    //
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile)
        {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_friends)
        {
            Intent intent = new Intent(this, FriendActivity.class);
            intent.putExtra("email",mEmail);
            startActivity(intent);
        }
        else if (id == R.id.nav_statistics)
        {
            Intent intent = new Intent(this, RewardActivity.class);
            intent.putExtra("email",mEmail);
            startActivity(intent);
        }



        else if (id == R.id.nav_logout)
        {
            logout();
        }



        else if (id == R.id.nav_activities)
        {
            Intent intent = new Intent(this, SelectActivity.class);
            intent.putExtra("rewards",rewards);
            intent.putExtra("achievements",achievements);
            intent.putExtra("email",email);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
