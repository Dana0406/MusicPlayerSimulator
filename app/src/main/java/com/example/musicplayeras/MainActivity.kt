package com.example.musicplayeras

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Bundle
import android.os.Handler
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.musicplayeras.services.Constants
import com.example.musicplayeras.databinding.ActivityMainBinding
import com.example.musicplayeras.services.RunningService
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var runnable: Runnable
    private lateinit var mediaPlayer: MediaPlayer

    private var songs =  arrayListOf<Int>()
    private var handler = Handler()
    private var currentPosition = 0

    companion object {
        fun performAction(context: Context, action: String) {
            val intent = Intent(context, MainActivity::class.java)
            intent.action = action
            context.sendBroadcast(intent)
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val startIntent = Intent(this@MainActivity, RunningService::class.java)
        startIntent.action = Constants.ACTION_STARTFOREGROUND
        startService(startIntent)

        addTracks()

        songNames()
        mediaPlayer = MediaPlayer.create(this, songs[currentPosition])

        with(binding){
            seekBar.max = mediaPlayer.duration
            seekBar.progress = 0

            playButton.setOnClickListener {
                playOrStopTrack()
            }

            playPrevious.setOnClickListener {
                setPreviousTrack()
            }

            playNext.setOnClickListener {
                setNextTrack()
            }

            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, position: Int, changed: Boolean) {
                    if (changed) {
                        mediaPlayer.seekTo(position)
                    }
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }

                override fun onStopTrackingTouch(p0: SeekBar?) {

                }
            })
        }

        mediaPlayer.setOnCompletionListener(OnCompletionListener {
            setNextTrack()
        })

        runnable = Runnable {
            binding.seekBar.progress = mediaPlayer.currentPosition
            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
    }

    private fun addTracks() {
        songs.add(0, R.raw.stuff)
        songs.add(1, R.raw.nothing_say_less)
        songs.add(2, R.raw.skillet_victorious)
        songs.add(3, R.raw.imagine_dragons_monday)
        songs.add(4, R.raw.the_1975_people)
    }

    private fun songNames() {
        when (currentPosition) {
            0 -> {
                binding.songName.text = "Alarm melody"
                binding.songAuthor.text = "Without perfomer"
            }
            1 -> {
                binding.songName.text = "Say Less"
                binding.songAuthor.text = "Nothing"
            }
            2 -> {
                binding.songName.text = "Victorious"
                binding.songAuthor.text = "Skillet"
            }
            3 -> {
                binding.songName.text = "Monday"
                binding.songAuthor.text = "Imagine dragons"
            }
            4 -> {
                binding.songName.text = "People"
                binding.songAuthor.text = "The 1975"
            }
        }
    }

    private fun setNextTrack() {
        if (currentPosition < songs.size - 1) {
            currentPosition++
        } else {
            currentPosition = 0
        }

        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }

        binding.seekBar.progress = 0
        songNames()

        mediaPlayer.stop()
        mediaPlayer.release()
        mediaPlayer = MediaPlayer.create(applicationContext, songs[currentPosition])
        binding.seekBar.max = mediaPlayer.duration
        mediaPlayer.start()

        mediaPlayer.setOnCompletionListener(OnCompletionListener {
            setNextTrack()
        })
    }

    private fun setPreviousTrack() {
        if (mediaPlayer != null) {
            binding.playButton.setImageResource(R.drawable.baseline_pause_24)
        }
        if (currentPosition > 0) {
            currentPosition--
        } else {
            currentPosition = songs.size - 1
        }
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        songNames()

        mediaPlayer = MediaPlayer.create(applicationContext, songs[currentPosition])

        binding.seekBar.max = mediaPlayer.duration
        mediaPlayer.start()
    }

    private fun playOrStopTrack() {
        songNames()
        if (mediaPlayer != null && !mediaPlayer.isPlaying) {
            binding.seekBar.max = mediaPlayer.duration
            mediaPlayer.start()
            binding.playButton.setImageResource(R.drawable.baseline_pause_24)
        } else {
            mediaPlayer.stop()
            binding.playButton.setImageResource(R.drawable.baseline_play_arrow_24)
        }
    }

    override fun onDestroy() {
        val stopIntent = Intent(this, RunningService::class.java)
        stopIntent.action = Constants.ACTION_STOPFOREGROUND
        startService(stopIntent)
        mediaPlayer.stop()
        super.onDestroy()
    }
}

