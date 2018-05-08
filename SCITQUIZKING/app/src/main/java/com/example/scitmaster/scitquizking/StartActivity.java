package com.example.scitmaster.scitquizking;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class StartActivity extends Activity {
    AlertDialog start;
    AudioManager audioManager;
    SeekBar vol;
    Intent getIntent;
    String id;
    TextView welcome;
    public static MediaPlayer BGM;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        getIntent = getIntent();
        id = getIntent.getStringExtra("id");

        welcome = findViewById(R.id.welcome);
        welcome.setText(id+"님 어서오세요");

        BGM = MediaPlayer.create(this,R.raw.bgm);
        BGM.setLooping(true);
        BGM.start();
    }
    public void btnStart(View view){
        switch(view.getId()){
            case R.id.gameStart :
                LayoutInflater inflater = LayoutInflater.from(this);
                View viewInDialog = inflater.inflate(R.layout.level_select, null);
                start = new AlertDialog.Builder(this).setView(viewInDialog).create();
                start.setButton(start.BUTTON_POSITIVE,"취소", (DialogInterface.OnClickListener) null);
                start.setTitle("난이도 선택");
                start.setCancelable(false);
                start.show();
                break;
            case R.id.gameOption :
                inflater = LayoutInflater.from(this);
                viewInDialog = inflater.inflate(R.layout.option, null);
                start = new AlertDialog.Builder(this).setView(viewInDialog).create();
                start.setButton(start.BUTTON_POSITIVE,"취소", (DialogInterface.OnClickListener) null);

                //음성 조절
                vol = (SeekBar) viewInDialog.findViewById(R.id.volumSeekBar);
                audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int nCurrentVolumn = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                int nMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                vol.setProgress(nMax);
                vol.setProgress(nCurrentVolumn);
                vol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                                progress, 0);
                    }
                });
                start.setTitle("옵션 조절");
                start.setCancelable(false);
                start.show();
                break;

            case R.id.gameRanking:
                Intent intent = new Intent(StartActivity.this,rankingActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);

            case R.id.gameOff :
                ActivityCompat.finishAffinity(this);
                System.exit(0);

        }
    }

    public void btnLevel (View view){
        Intent intent = new Intent(StartActivity.this,playActivity.class);
        String playLevel = ((Button)view).getText().toString();
        intent.putExtra("level", playLevel);
        intent.putExtra("id",id);
        startActivity(intent);
        start.dismiss();
    }
    public void btnVibe(View view){
        if (view.getId() == R.id.vibeOff){
            playActivity.vibOn = false;
        }else{
            playActivity.vibOn = true;
        }
    }
}
