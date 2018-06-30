package com.ctg.fitgram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class SelectActivity extends AppCompatActivity
{
private Integer rewards;
private Integer achievements;
private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        rewards = getIntent().getExtras().getInt("rewards");
        achievements =getIntent().getExtras().getInt("achievements");
        email =getIntent().getExtras().getString("email");
    }

    public void startActivityRecognition(View view)
    {
        Intent intent = new Intent(this, ActivityRecognitionActivity.class);
        startActivity(intent);
    }
    public void startGoogleMapActivity(View view)
    {
        Intent intent = new Intent(this, GoogleMapActivity.class);
        intent.putExtra("email",email);
        startActivity(intent);
    }

    public void startFitnessActivity(View view)
    {
        Intent intent = new Intent(this, FitActivity.class);
        startActivity(intent);
    }
    public void startARActivity(View view)
    {
        Intent intent = new Intent(this, ARActivity.class);
        intent.putExtra("rewards",rewards);
        intent.putExtra("achievements",achievements);
        intent.putExtra("email",email);
        startActivity(intent);
    }

}
