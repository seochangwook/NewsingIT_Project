package com.example.apple.newsingit_project.data.view_data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2016-08-24.
 */
public class AlarmData {
    public int alarm_message_type;
    public String alarm_message;
    public String alarm_date;
    public int alarm_data;



    public List<AlarmData> alarmDataList = new ArrayList<>();

    public String getAlarm_message() {
        return this.alarm_message;
    }

    public void setAlarm_message(String alarm_message) {
        this.alarm_message = alarm_message;
    }

    public String getAlarm_date() {
        return this.alarm_date;
    }

    public void setAlarm_date(String alarm_date) {
        this.alarm_date = alarm_date;
    }

    public int get_alarm_message_type() {
        return this.alarm_message_type;
    }

    public void set_alarm_message_type(int alarm_message_type) {
        this.alarm_message_type = alarm_message_type;
    }

    public int get_alarm_data() {
        return this.alarm_data;
    }

    public void set_alarm_data(int alarm_data) {
        this.alarm_data = alarm_data;
    }
}
