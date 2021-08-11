package com.minesweeper.smart_home_management.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartAlarm extends BroadcastReceiver
{
    Alarm alarm = new Alarm();



    public Alarm getAlarm() {
        return alarm;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            alarm.setAlarm(context,intent);
        }
    }
}