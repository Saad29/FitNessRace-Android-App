package com.ctg.fitgram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ctg.fitgram.model.Data;
import com.ctg.fitgram.model.DataUser;
import com.ctg.fitgram.model.Response;
import com.ctg.fitgram.model.User;
import com.ctg.fitgram.model.UserResponseModel;
import com.ctg.fitgram.network.NetworkUtil;
import com.ctg.fitgram.network.RetrofitInterface;
import com.ctg.fitgram.utils.DataAdapterForFriendList;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class FriendFindingActivity extends AppCompatActivity
{
    private TextView searchTxtView;
    private String mEmail;
    private String fMail;
    public static final String URL = "https://fitnessrace.herokuapp.com/";
    public static final String TAG = FriendFindingActivity.class.getSimpleName();
    private User user;
    private ArrayList<User> data;
    private DataAdapterForFriendList adapter;
    private RecyclerView recyclerView;
    private CompositeSubscription mSubscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_finding);
        mEmail = getIntent().getExtras().getString("email");
        initViews();
        mSubscriptions = new CompositeSubscription();
    }
    private void initViews()
    {

        searchTxtView = findViewById(R.id.searchTextView);
        recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

    }

    public void searchFunction(View view)
    {


        fMail= searchTxtView.getText().toString();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

        Call<UserResponseModel> call = retrofitInterface.findFriends(fMail);
        //Call<User> call = retrofitInterface.findFriends(fMail);

        call.enqueue(new Callback<UserResponseModel>()
        {
            @Override
            public void onResponse(Call<UserResponseModel> call, retrofit2.Response<UserResponseModel> response)
            {

                if (response.isSuccessful())
                {
         //
                    UserResponseModel responseBody = response.body();

                    //data = new ArrayList<>(Arrays.asList(responseBody.getData()));
                    user =new User();
                    user = responseBody.getUser();
                    adapter = new DataAdapterForFriendList(user,mEmail);
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
           // public void onFailure(Call<User> call, Throwable t) {
            public void onFailure(Call<UserResponseModel> call, Throwable t) {

                //mProgressBar.setVisibility(View.GONE);
                Log.d(TAG, "onFailure: "+t.getLocalizedMessage());
            }
        });
        //



    }


    }



