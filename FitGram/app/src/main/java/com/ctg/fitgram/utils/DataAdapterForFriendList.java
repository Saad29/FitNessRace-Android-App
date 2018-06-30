package com.ctg.fitgram.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ctg.fitgram.R;
import com.ctg.fitgram.model.Response;
import com.ctg.fitgram.model.User;
import com.ctg.fitgram.network.RetrofitInterface;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by syeds on 3/3/2018.
 */

public class DataAdapterForFriendList extends RecyclerView.Adapter<DataAdapterForFriendList.ViewHolder>
{
    private ArrayList<User> data;
    private Context context;
    private User user;
    public static final String URL = "https://fitnessrace.herokuapp.com/";
    private SharedPreferences mSharedPreferences;
    private String mToken;
    private String mEmail;

   // public DataAdapterForFriendList(ArrayList<User> data)
   public DataAdapterForFriendList(User user,String mEmail)
    {
        this.context = context;
        //this.data= data;
        this.user= user;
        this.mEmail = mEmail;
    }

    @Override
    public DataAdapterForFriendList.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup, false);
        return new DataAdapterForFriendList.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(DataAdapterForFriendList.ViewHolder viewHolder, int i) {

       if(user!=null)
       { viewHolder.tv_name.setText(user.getName());
           Picasso.with(this.context).load("https://fitnessrace.herokuapp.com/images/" + user.getEmail() + ".jpg").resize(120, 60).into(viewHolder.img_android);
           viewHolder.linkBtn.setText("Add Friend");
       }

        viewHolder.linkBtn.setOnClickListener(view ->
        {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
            Call<Response> call = retrofitInterface.makeFriend(mEmail,user.getEmail());
            //
            call.enqueue(new Callback<Response>()
            {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
                {

                    if (response.isSuccessful())
                    {
                        //
                        Response responseBody = response.body();

                        Log.d("success", response.toString());
                        Log.d("success2", responseBody.toString());
                        viewHolder.linkBtn.setText("Friend Added!");


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
                public void onFailure(Call<Response> call, Throwable t) {

                    //mProgressBar.setVisibility(View.GONE);
                    Log.d("FAILURE", "onFailure: "+t.getLocalizedMessage());
                }
            });
            //



        });

    }




    @Override
    public int getItemCount()
    {
        return 1;

    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name,tv_email,btntext;
        ImageView img_android;
        Button linkBtn;
        public ViewHolder(View view) {
            super(view);

            tv_name = (TextView)view.findViewById(R.id.tv_name);
            img_android = (ImageView)view.findViewById(R.id.img_android);
            linkBtn= (Button)view.findViewById(R.id.linkBtn);

        }
    }
}

