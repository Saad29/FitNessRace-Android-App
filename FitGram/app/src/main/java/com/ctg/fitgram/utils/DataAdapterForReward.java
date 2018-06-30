package com.ctg.fitgram.utils;

/**
 * Created by syeds on 3/6/2018.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctg.fitgram.R;
import com.ctg.fitgram.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DataAdapterForReward extends RecyclerView.Adapter<DataAdapterForReward.ViewHolder>
{
    private ArrayList<User> data;
    private Context context;

    public DataAdapterForReward(ArrayList<User> data)
    {
        this.context = context;
        this.data= data;
    }

    @Override
    public DataAdapterForReward.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cdrow1, viewGroup, false);
        return new DataAdapterForReward.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapterForReward.ViewHolder viewHolder, int i) {

        viewHolder.tv_name.setText(data.get(i).getName());
        if(data.get(i).getRewards()!=null)
        viewHolder. textViewReward.setText(data.get(i).getRewards().toString());

    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name,textViewReward;

        public ViewHolder(View view) {
            super(view);

            tv_name = (TextView)view.findViewById(R.id.tv_name);
            textViewReward = (TextView)view.findViewById(R.id.textViewReward);


        }
    }
}
