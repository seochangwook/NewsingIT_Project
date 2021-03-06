package com.example.apple.newsingit_project.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.AlarmData;
import com.example.apple.newsingit_project.manager.fontmanager.FontManager;
import com.example.apple.newsingit_project.view.view_list.AlarmListViewHolder;

/**
 * Created by Tacademy on 2016-08-24.
 */
public class AlarmListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    AlarmData alarmData;
    Context context;
    FontManager fontManager;

    public AlarmListAdapter(Context context) {
        this.context = context;
        alarmData = new AlarmData();
        fontManager = new FontManager(context);
    }


    public void setAlarmDataLIist(AlarmData alarmData) {
        if (this.alarmData != alarmData) {
            this.alarmData = alarmData;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        if (alarmData == null) {
            return 0;
        }
        return alarmData.alarmDataList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_alarm_list, parent, false);
        AlarmListViewHolder holder = new AlarmListViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (alarmData.alarmDataList.size() > 0) {
            if (position < alarmData.alarmDataList.size()) {
                AlarmListViewHolder alarmListViewHolder = (AlarmListViewHolder) holder;
                alarmListViewHolder.setAlarmData(alarmData.alarmDataList.get(position), context);
                alarmListViewHolder.contentView.setTypeface(fontManager.getTypefaceRegularInstance());
                alarmListViewHolder.dateView.setTypeface(fontManager.getTypefaceRegularInstance());

                return;
            }
            position -= alarmData.alarmDataList.size();
        }

        throw new IllegalArgumentException("invalid position");

    }
}
