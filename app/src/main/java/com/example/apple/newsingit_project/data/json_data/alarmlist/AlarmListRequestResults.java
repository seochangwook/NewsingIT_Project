package com.example.apple.newsingit_project.data.json_data.alarmlist;

public class AlarmListRequestResults {
    private int data_pk;
    private String dtime;
    private String message;

    public int getData_pk() {
        return this.data_pk;
    }

    public void setData_pk(int data_pk) {
        this.data_pk = data_pk;
    }

    public String getDtime() {
        return this.dtime;
    }

    public void setDtime(String dtime) {
        this.dtime = dtime;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
