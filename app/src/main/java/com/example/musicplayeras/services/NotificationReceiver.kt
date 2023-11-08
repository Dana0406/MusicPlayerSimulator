package com.example.musicplayeras.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.musicplayeras.MainActivity

class NotificationReceiver:BroadcastReceiver(){
    override fun onReceive(context:Context?,intent:Intent?){
        if(intent!=null&&context!=null){
            val action=intent.action
            when(action){
                Constants.ACTION_PREV->{
                    MainActivity.performAction(context, Constants.ACTION_PREV)
                }
                Constants.ACTION_PLAY->{
                    MainActivity.performAction(context, Constants.ACTION_PLAY)
                }
                Constants.ACTION_NEXT->{
                    MainActivity.performAction(context, Constants.ACTION_NEXT)
                }
            }
        }
    }
}
