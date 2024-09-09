package com.example.multitaskar.Model;

import com.google.firebase.Timestamp;

public class ReminderModel {
    String schedule_title;
    String schedule_Content;
    String schedule_Date;
    String schedule_Time;
    String schedule_status;

    public String getSchedule_title() {
        return schedule_title;
    }

    public void setSchedule_title(String schedule_title) {
        this.schedule_title = schedule_title;
    }

    public String getSchedule_Content() {
        return schedule_Content;
    }

    public void setSchedule_Content(String schedule_Content) {
        this.schedule_Content = schedule_Content;
    }

    public String getSchedule_Date() {
        return schedule_Date;
    }

    public void setSchedule_Date(String schedule_Date) {
        this.schedule_Date = schedule_Date;
    }

    public String getSchedule_Time() {
        return schedule_Time;
    }

    public void setSchedule_Time(String schedule_Time) {
        this.schedule_Time = schedule_Time;
    }

    public String getSchedule_status() {
        return schedule_status;
    }

    public void setSchedule_status(String schedule_status) {
        this.schedule_status = schedule_status;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public ReminderModel() {
    }

    Timestamp timestamp;

}
