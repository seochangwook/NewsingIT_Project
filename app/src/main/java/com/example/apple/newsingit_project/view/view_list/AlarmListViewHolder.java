package com.example.apple.newsingit_project.view.view_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.AlarmData;

/**
 * Created by Tacademy on 2016-08-24.
 */
public class AlarmListViewHolder extends RecyclerView.ViewHolder {

    AlarmData alarmData;
    TextView nameView, contentView, dateView;


    public AlarmListViewHolder(View itemView) {
        super(itemView);
        nameView = (TextView)itemView.findViewById(R.id.alarm_name);
        contentView = (TextView)itemView.findViewById(R.id.alarm_content);
        dateView = (TextView)itemView.findViewById(R.id.alarm_date);
    }

    public void setAlarmData(AlarmData alarmData) {
        this.alarmData = alarmData;
        nameView.setText(alarmData.getName());
        contentView.setText(alarmData.getContent());
        dateView.setText(alarmData.getDate());
    }
}
