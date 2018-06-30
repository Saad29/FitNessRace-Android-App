package com.ctg.fitgram;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ctg.fitgram.fragments.LoginFragment;
import com.ctg.fitgram.fragments.ResetPasswordDialog;
import com.ctg.fitgram.utils.Constants;


public class MainActivity extends AppCompatActivity
        implements ResetPasswordDialog.Listener {


    public static final String TAG = MainActivity.class.getSimpleName();

    private LoginFragment mLoginFragment;
    private ResetPasswordDialog mResetPasswordDialog;
    private SharedPreferences mSharedPreferences;
    private String mToken;
    private String mEmail;
    public  SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSharedPreferences();
        if (savedInstanceState == null)
        {

            loadFragment();
        }
    }

    private void initSharedPreferences()
    {

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = mSharedPreferences.getString(Constants.TOKEN,"");
        mEmail = mSharedPreferences.getString(Constants.EMAIL,"");
    }

    private void loadFragment()

    {
        // mLoginFragment = new LoginFragment();

        // getFragmentManager().beginTransaction().replace(R.id.fragmentFrame,mLoginFragment,LoginFragment.TAG).commit();

        if(mToken == ""  || mEmail == "")

        {
            if (mLoginFragment == null)
            {
                mLoginFragment = new LoginFragment();
            }
            getFragmentManager().beginTransaction().replace(R.id.fragmentFrame,mLoginFragment,LoginFragment.TAG).commit();
        }

        else
        {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        //String data = intent.getData().getLastPathSegment();
//        Log.d(TAG, "onNewIntent: "+data);

        mResetPasswordDialog = (ResetPasswordDialog) getFragmentManager().findFragmentByTag(ResetPasswordDialog.TAG);

       // if (mResetPasswordDialog != null)
           // mResetPasswordDialog.setToken(data);
    }

    @Override
    public void onPasswordReset(String message) {

        showSnackBarMessage(message);
    }
    public void continueWithoutLogin(View view)
    {
        Intent intent = new Intent(this, TimelineActivity.class);
        startActivity(intent);
    }

    private void showSnackBarMessage(String message) {

        Snackbar.make(findViewById(R.id.activity_main),message,Snackbar.LENGTH_SHORT).show();

    }

/*
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {


        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
    */
}