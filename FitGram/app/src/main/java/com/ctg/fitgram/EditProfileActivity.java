package com.ctg.fitgram;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ctg.fitgram.model.Response;
import com.ctg.fitgram.model.User;
import com.ctg.fitgram.network.NetworkUtil;
import com.ctg.fitgram.network.RetrofitInterface;
import com.ctg.fitgram.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import android.view.View;
import android.widget.TextView;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class EditProfileActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private static final int INTENT_REQUEST_CODE = 100;

    public static final String URL = "https://fitnessrace.herokuapp.com/";

    private SharedPreferences mSharedPreferences;
    private String mEmail;
    private String mToken;
    private String mName;
    private String mCity;
    private Integer mAge;
    private ImageView ppViewer;
    private TextView nameText;
    private TextView ageText;
    private TextView cityText;
    private CompositeSubscription mSubscriptions;
    private User muser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSubscriptions = new CompositeSubscription();
        initSharedPreferences();
        mName = getIntent().getExtras().getString("name");
        mCity = getIntent().getExtras().getString("city");
        mAge = ((getIntent().getExtras().getInt("age")));

        //  muser =  getIntent().getExtras().getParcelable("profile");
        initViews();

        // mName = getIntent().getExtras().getString("name");
        //mCity = getIntent().getExtras().getString("city");
        //mAge = getIntent().getExtras().getInt("age");
        loadProfile();


    }


    private void initSharedPreferences() {

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEmail = mSharedPreferences.getString(Constants.EMAIL, "");
        mToken = mSharedPreferences.getString(Constants.TOKEN, "");
    }

    private void initViews() {

        ppViewer = findViewById(R.id.ppViewer);
        nameText = findViewById(R.id.nameTxt);
        cityText = findViewById(R.id.cityText);
        ageText = findViewById(R.id.ageText);
        profilePicLoad();

    }

    private void loadProfile() {

        mSubscriptions.add(NetworkUtil.getRetrofit(mToken).getProfile(mEmail)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
        nameText.setText(mName);
    }


    private void profilePicLoad() {
        nameText.setText(mName);
        cityText.setText(mCity);
        ageText.setText(mAge.toString());
        //loading profile picture
        Picasso.with(this)
                .load("https://fitnessrace.herokuapp.com/images/" + mEmail + ".jpg")
                .resize(50, 50)
                .placeholder(R.drawable.ic_menu_gallery)
                .centerCrop()
                .into(ppViewer);
        //
    }
    //for profile view

    private void handleResponse(User user) {
        muser = user;
        nameText.setText(user.getName());
        ageText.setText(user.getAge());
        cityText.setText(user.getCity());
    }

    private void handleError(Throwable error) {


        if (error instanceof HttpException) {

            Gson gson = new GsonBuilder().create();

            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                Response response = gson.fromJson(errorBody, Response.class);
                showSnackBarMessage(response.getMessage());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            showSnackBarMessage("Network Error !");
        }
    }

    private void showSnackBarMessage(String message) {

        //Snackbar.make(findViewById(layout.activity_edit_profile),message,Snackbar.LENGTH_SHORT).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }

    //
//profile updating
    public void updateProfileClick(View view) {
        //  updateProfile(User user);
        //User user = new User();

        muser.setName(nameText.getText().toString());
        muser.setCity(cityText.getText().toString());
        muser.setAge(Integer.parseInt(ageText.getText().toString()));
        updateProfile(muser);
       /* User muser = new User();
        muser.setName(nameText.getText().toString());
        muser.setAge(Integer.parseInt(ageText.getText().toString()));
        muser.setCity(cityText.getText().toString());*/

        // user.setEmail(mEmail);
        //updateProfile(muser);
    }

    public void updateProfile(User user) {




        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

        Call<Response> call = retrofitInterface.updateUser(user.getName(),user.getAge(),user.getCity(),mEmail);
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


        //


        //


    }


//





    //for image upload
    public void uploadImageHandler(View view)
    {


        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");

        try {
            startActivityForResult(intent, INTENT_REQUEST_CODE);

        }
        catch (ActivityNotFoundException e)
        {

            e.printStackTrace();
            Log.d("imageError", e.toString());
        }
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == INTENT_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                try {

                    InputStream is = getContentResolver().openInputStream(data.getData());

                    uploadImage(getBytes(is));

                } catch (IOException e)
                {
                    Log.d("errorOnStart", e.toString());
                    e.printStackTrace();
                }
            }
        }
    }

    public byte[] getBytes(InputStream is) throws IOException
    {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff.toByteArray();
    }

    private void uploadImage(byte[] imageBytes)
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);

        MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
        Call<Response> call = retrofitInterface.uploadImage(body,mEmail);
        // mProgressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

                //mProgressBar.setVisibility(View.GONE);

                if (response.isSuccessful())
                {

                    Response responseBody = response.body();
                    Snackbar.make(findViewById(R.id.content), responseBody.getMessage(),Snackbar.LENGTH_SHORT).show();
                    Log.d("success", response.toString());
                    profilePicLoad();
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
    }
}