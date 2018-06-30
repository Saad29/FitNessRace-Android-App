package com.ctg.fitgram;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ctg.fitgram.fragments.ChangePasswordDialog;
import com.ctg.fitgram.model.Response;
import com.ctg.fitgram.model.User;
import com.ctg.fitgram.network.NetworkUtil;
import com.ctg.fitgram.utils.Constants;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class ProfileActivity extends AppCompatActivity implements ChangePasswordDialog.Listener
{

    public static final String TAG = ProfileActivity.class.getSimpleName();

    private TextView mTvName;
    private TextView mTvEmail;
    private TextView mTvDate;
    private Button mBtChangePassword;
    private Button mBtLogout;
    private Button mBtEditProfile;
    private ImageView ppViewer;

    private ProgressBar mProgressbar;

    //
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    //public static final String Phone = "phoneKey";
    public static final String Email = "emailKey";
    //
    private SharedPreferences mSharedPreferences;
    private String mToken;
    private String mEmail;
    private String mName;
    private String mCity;
    private Integer mAge;
    private User muser;

    private CompositeSubscription mSubscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mSubscriptions = new CompositeSubscription();
        initViews();
        initSharedPreferences();
        loadProfile();
    }

    private void initViews()
    {

        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvEmail = (TextView) findViewById(R.id.tv_email);
        mTvDate = (TextView) findViewById(R.id.tv_date);
        mBtChangePassword = (Button) findViewById(R.id.btn_change_password);
        mBtLogout = (Button) findViewById(R.id.btn_logout);
        mBtEditProfile = (Button) findViewById(R.id.btn_edit_profile);
        mProgressbar = (ProgressBar) findViewById(R.id.progress);
        ppViewer = (ImageView)findViewById(R.id.ProfilePicImageView);

        mBtChangePassword.setOnClickListener(view -> showDialog());
        mBtLogout.setOnClickListener(view -> logout());
    }

    private void initSharedPreferences()
    {

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = mSharedPreferences.getString(Constants.TOKEN,"");
        mEmail = mSharedPreferences.getString(Constants.EMAIL,"");
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

    private void showDialog()
    {

        ChangePasswordDialog fragment = new ChangePasswordDialog();

        Bundle bundle = new Bundle();
        bundle.putString(Constants.EMAIL, mEmail);
        bundle.putString(Constants.TOKEN,mToken);
        fragment.setArguments(bundle);

        fragment.show(getFragmentManager(), ChangePasswordDialog.TAG);
    }

    public void startEditProfileActivity(View view)
    {
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra("name",mName);
        intent.putExtra("age",mAge);
        intent.putExtra("city",mCity);
        //intent.putExtra("profile",(Parcelable)muser);
        startActivity(intent);
    }

    private void loadProfile()
    {


        mSubscriptions.add(NetworkUtil.getRetrofit(mToken).getProfile(mEmail)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));


    }

    private void handleResponse(User user)

    {

        mProgressbar.setVisibility(View.GONE);
        mTvName.setText(user.getName());
        mName = user.getName();
        mCity =user.getCity();
        mAge =user.getAge();
        mTvEmail.setText(user.getEmail());
        mTvDate.setText(user.getCreated_at());
        muser= user;

       // SharedPreferences.Editor editor = mSharedPreferences.edit();

       // editor.putString(Name, user.getName());
       // editor.putString(Age, ph);
        //editor.putString(Email, user.getEmail());
       // editor.commit();
        //for image
        Picasso.with(this)
                .load("https://fitnessrace.herokuapp.com/images/" + user.getEmail() + ".jpg" )
                .resize(120, 60)
                .placeholder(R.drawable.ic_menu_gallery)
                .centerCrop()
                .into(ppViewer);

        //

    }

    private void handleError(Throwable error) {

        mProgressbar.setVisibility(View.GONE);

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
            Log.i("msgs", error.toString());


        }
    }

    private void showSnackBarMessage(String message)
    {

        Snackbar.make(findViewById(R.id.activity_profile),message,Snackbar.LENGTH_SHORT).show();

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }

    @Override
    public void onPasswordChanged() {

        showSnackBarMessage("Password Changed Successfully !");
    }

    public void continueToTimeline(View view)
    {
        Intent intent = new Intent(this, TimelineActivity.class);
        startActivity(intent);
    }
}