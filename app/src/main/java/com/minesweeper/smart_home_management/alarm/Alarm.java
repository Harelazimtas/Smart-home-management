package com.minesweeper.smart_home_management.alarm;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.widget.Toast;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minesweeper.smart_home_management.MissionAdd;
import com.minesweeper.smart_home_management.R;
import com.minesweeper.smart_home_management.model.Mission;

import java.util.Date;

public class Alarm extends BroadcastReceiver
{
    //we can store user id in preference and get them

   // private static String userID;
    private final int SHOW_MESSAGE_PER_MINUTE= 1;

    public Alarm(String userID){
        //this.userID=userID;

    }

    public Alarm(){

    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();
        Bundle extras = intent.getExtras();
        if(extras.get("userID")!= null &&extras.get("userID").toString()!= null){
            Toast.makeText(context, "Please complete the last mission "+extras.get("nameNextMission").toString(), Toast.LENGTH_LONG).show();
        }
        wl.release();
    }

    public void setAlarm(Context context, Intent intent)
    {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent i = new Intent(context, Alarm.class);
        Bundle extras = intent.getExtras();
        if(extras.get("userID").toString() != null ){
            i.putExtra("userID", extras.get("userID").toString());
            i.putExtra("nameNextMission", extras.get("nameNextMission").toString());
        }
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 6*this.SHOW_MESSAGE_PER_MINUTE , pi); // Millisec * Second * Minute
    }

    public void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

}
