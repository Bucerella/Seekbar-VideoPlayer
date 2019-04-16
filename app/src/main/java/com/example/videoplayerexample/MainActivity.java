package com.example.videoplayerexample;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnSeekBarChangeListener, MediaPlayer.OnCompletionListener{

    private VideoView videoView;
    private Button button, stopBtn, playingButton;
    private MediaController mediaController;
    MediaPlayer mp;
    private SeekBar seekBar,seekBarChange;
    private AudioManager audioManager;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.playBtn);
        videoView = findViewById(R.id.videoView);
        playingButton = findViewById(R.id.playingBtn);
        stopBtn = findViewById(R.id.pauseBtn);
        mp = MediaPlayer.create(this,R.raw.gun);
        seekBar = findViewById(R.id.seekBar);
        seekBarChange = findViewById(R.id.changePosition);
        seekBarChange.setOnSeekBarChangeListener(MainActivity.this);
        seekBarChange.setMax(mp.getDuration());
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mp.setOnCompletionListener(this);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        seekBar.setMax(maxVolume);
        seekBar.setProgress(currentVolume);

        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Toast.makeText(MainActivity.this,progress+"",Toast.LENGTH_LONG).show();
            if(fromUser){
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
            }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mediaController = new MediaController(MainActivity.this);
        playingButton.setOnClickListener(MainActivity.this);
        stopBtn.setOnClickListener(MainActivity.this);
        button.setOnClickListener(MainActivity.this);

    }

    @Override
    public void onClick(View buttonView) {

        switch (buttonView.getId()){
            case R.id.playBtn:
            Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.pexel);
            videoView.setVideoURI(uri);
            videoView.setMediaController(mediaController);
            videoView.start();
            break;

            case R.id.playingBtn:
                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        seekBarChange.setProgress(mp.getCurrentPosition());
                    }
                },0,1000);
                mp.start();
                break;

            case  R.id.pauseBtn:
                timer.cancel();
                mp.pause();
                break;
        }



    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser){
            //Toast.makeText(this,progress+"",Toast.LENGTH_LONG).show();
            mp.seekTo(progress);

        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mp.pause();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mp.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        timer.cancel();
        Toast.makeText(this,"Şarkı bitti",Toast.LENGTH_SHORT).show();
    }
}
