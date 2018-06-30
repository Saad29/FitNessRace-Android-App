package com.ctg.fitgram.utils;

/**
 * Created by syeds on 3/1/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctg.fitgram.FriendProfileActivity;
import com.ctg.fitgram.R;
import com.ctg.fitgram.RewardActivity;
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

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder>
{
    private ArrayList<User> data;
    private Context context;

    public DataAdapter(ArrayList<User> data)
    {
        this.context = context;
        this.data= data;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, int i) {

        viewHolder.tv_name.setText(data.get(i).getName());
        Picasso.with(this.context).load("https://fitnessrace.herokuapp.com/images/" + data.get(i).getEmail() + ".jpg").resize(120, 60).into(viewHolder.img_android);
        viewHolder.linkBtn.setText("See profile");


        viewHolder.linkBtn.setOnClickListener(view ->
        {

           // Intent intent = new Intent(this.context, FriendProfileActivity.class)

        });

    }

    @Override
    public int getItemCount()
    {
        return data.size();
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
