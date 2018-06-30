package com.ctg.fitgram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ctg.fitgram.model.Response;
import com.ctg.fitgram.model.User;
import com.ctg.fitgram.network.RetrofitInterface;
import com.ctg.fitgram.utils.DataAdapter;
import com.ctg.fitgram.utils.DataAdapterForReward;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RewardActivity extends AppCompatActivity {

    private String mEmail;
    public static final String URL = "https://fitnessrace.herokuapp.com/";
    public static final String TAG = FriendActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private ArrayList<User> data;
    private DataAdapterForReward adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);
        mEmail = getIntent().getExtras().getString("email");
        initViews();

    }


    private void initViews(){
        recyclerView = (RecyclerView)findViewById(R.id.card_recycler_viewr);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        myFriendFunction();
    }
    public void myFriendFunction()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

        Call<Response> call = retrofitInterface. find();
        //
        call.enqueue(new Callback<Response>()
        {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {

                //mProgressBar.setVisibility(View.GONE);

                if (response.isSuccessful())
                {

                    Response responseBody = response.body();
                    data = new ArrayList<>(Arrays.asList(responseBody.getData()));
                    adapter = new DataAdapterForReward(data);
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
            public void onFailure(Call<Response> call, Throwable t) {

                //mProgressBar.setVisibility(View.GONE);
                Log.d(TAG, "onFailure: "+t.getLocalizedMessage());
            }
        });
        //
    }


}
