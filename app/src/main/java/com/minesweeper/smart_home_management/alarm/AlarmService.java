package com.minesweeper.smart_home_management.alarm;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.google.firebase.database.DatabaseReference;


public class AlarmService extends Service {

    Alarm alarm ;

    public AlarmService(){
        alarm=new Alarm();
    }

    public AlarmService(String userId){
    }

    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        /*Bundle extras = intent.getExtras();
        alarm.setUserID(extras.get("userID").toString());
        */
        alarm.setAlarm(this,intent);
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        Bundle extras = intent.getExtras();
        if(extras.get("userID").toString()!= null){
            alarm=new Alarm(extras.get("userId").toString());
        }
        alarm.setAlarm(this,intent);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
