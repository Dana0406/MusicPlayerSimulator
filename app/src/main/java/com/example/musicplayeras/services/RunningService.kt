package com.example.musicplayeras.services

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.PackageManagerCompat
import com.example.musicplayeras.MainActivity
import com.example.musicplayeras.R

class RunningService: Service() {

    @SuppressLint("RestrictedApi")
    override fun onStartCommand(intent:Intent, flags:Int, startId:Int):Int{

        when (intent.action)
        {
            Constants.ACTION_STARTFOREGROUND ->{
                Log.i(PackageManagerCompat.LOG_TAG,"ReceivedStartForegroundIntent")

                val notificationIntent=Intent(this,MainActivity::class.java)

                notificationIntent.action= Constants.ACTION_MAIN_
                notificationIntent.flags=(Intent.FLAG_ACTIVITY_NEW_TASK
                        or Intent.FLAG_ACTIVITY_CLEAR_TASK)

                val pendingIntent=PendingIntent.getActivity(
                    this,0,notificationIntent,0)

                val previousIntent=Intent(this,RunningService::class.java)
                previousIntent.action= Constants.ACTION_PREV
                val ppreviousIntent=PendingIntent.getService(
                    this,0,previousIntent,0)

                val playIntent=Intent(this,RunningService::class.java)
                playIntent.action= Constants.ACTION_PLAY
                val pplayIntent=PendingIntent.getService(
                    this,0,
                    playIntent,0
                )

                val nextIntent=Intent(this,RunningService::class.java)
                nextIntent.action= Constants.ACTION_NEXT
                val pnextIntent=PendingIntent.getService(this,0,
                    nextIntent,0)

                val notification:Notification=NotificationCompat.Builder(this,"running_channel")
                    .setContentTitle("Music Player")
                    .setTicker("Music Player")
                    .setContentText("My Music")
                    .setSmallIcon(R.drawable.baseline_music_note_24)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .addAction(R.drawable.baseline_skip_previous_24,"Previous",ppreviousIntent)
                    .addAction(R.drawable.baseline_play_arrow_24,"Play",pplayIntent)
                    .addAction(R.drawable.baseline_skip_next_24,"Next",pnextIntent).build()

                startForeground(Constants.FOREGROUND_SERVICE,notification)
            }
            Constants.ACTION_PREV -> {
                Log.i(PackageManagerCompat.LOG_TAG,"ClickedPrevious")
            }
            Constants.ACTION_NEXT -> {
                Log.i(PackageManagerCompat.LOG_TAG,"ClickedNext")
            }
            Constants.ACTION_STOPFOREGROUND -> {
                Log.i(PackageManagerCompat.LOG_TAG,"ReceivedStopForegroundIntent")
                stopSelf()
            }
        }

        return START_STICKY
    }

    @SuppressLint("RestrictedApi")
    override fun onDestroy(){
        super.onDestroy()
    }

    override fun onBind(intent:Intent):IBinder?{
        return null
    }

}