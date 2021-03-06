package com.example.apple.newsingit_project.view.view_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.AlarmData;

/**
 * Created by Tacademy on 2016-08-24.
 */
public class AlarmListViewHolder extends RecyclerView.ViewHolder {

    public TextView contentView, dateView;
    AlarmData alarmData;
    ImageView profileView;

    public AlarmListViewHolder(View itemView) {
        super(itemView);

        contentView = (TextView)itemView.findViewById(R.id.text_alarm_content);
        dateView = (TextView)itemView.findViewById(R.id.text_alarm_date);
        profileView = (ImageView)itemView.findViewById(R.id.img_alarm_profile);
    }

    public void setAlarmData(AlarmData alarmData, Context context) {
        this.alarmData = alarmData;

        contentView.setText(alarmData.getAlarm_message());
        dateView.setText(alarmData.getAlarm_date());
    }
}
