package com.example.scitmaster.scitquizking;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class playActivity extends Activity {

    int timer;
    TextView levelText;
    Thread thread;
    ProgressBar timeBar;
    String level;
    ImageView img;
    Button startBtn;
    Button stopBtn;
    Question selectedQ;
    int score;
    MediaPlayer sure;
    MediaPlayer unsure;
    Vibrator vibrator;
    static boolean vibOn;
    String id;

    ArrayList<Question> easy;
    ArrayList<Question> normal;
    ArrayList<Question> hard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_game);

        startBtn = findViewById(R.id.btnStart);
        stopBtn = findViewById(R.id.btnStop);
        levelText = findViewById(R.id.levelText);

        Intent intent = getIntent();
        level = intent.getStringExtra("level");
        id = intent.getStringExtra("id");

        levelText.setText(level + " 난이도");

        sure = MediaPlayer.create(this, R.raw.sure);
        unsure = MediaPlayer.create(this, R.raw.unsure);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibOn = true;

        //////////////// DB 대체
        easy = new ArrayList<Question>();
        normal = new ArrayList<Question>();
        hard = new ArrayList<Question>();
        easy.add(new Question("1", "가", "초급", "e001", "설명"));
        easy.add(new Question("2", "다", "초급", "e002", "설명"));
        easy.add(new Question("3", "라", "초급", "e003", "설명"));
        normal.add(new Question("4", "다", "중급", "n001", "설명"));
        normal.add(new Question("5", "라", "중급", "n002", "설명"));
        normal.add(new Question("6", "라", "중급", "n003", "설명"));
        hard.add(new Question("7", "다", "고급", "h001", "설명"));
        hard.add(new Question("8", "가", "고급", "h002", "설명"));
        hard.add(new Question("9", "다", "고급", "h003", "설명"));
        long seed = System.nanoTime();
        Collections.shuffle(easy, new Random(seed));
        Collections.shuffle(normal, new Random(seed));
        Collections.shuffle(hard, new Random(seed));
        ///////////////
    }

    // 뒤로 가기 눌렀을때 스레드 처리
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (thread != null) {
            thread.interrupt();
        }
    }

    // 시작 버튼 눌렀을때 처리
    public void gameStart(View view) {
        score = 0;
        startBtn.setVisibility(View.GONE);
        stopBtn.setVisibility(View.VISIBLE);
        img = findViewById(R.id.question);
            gameSelect();
    }

    //종료 버튼 눌렀을때 처리
    public void gameStop(View view){
        startBtn.setVisibility(View.VISIBLE);
        stopBtn.setVisibility(View.GONE);
        if (thread != null) {
            thread.interrupt();
        }
        finish();
    }

    // 게임난이도 선택 및 문제 처리
    public void gameSelect() {
        Log.i("체킹",normal.size()+"");
        BackThread runnable = new BackThread();
        thread = new Thread(runnable);
        thread.setDaemon(true);
        timer = 100;
        thread.start();
        int resID;
        String resName;
        switch (level) {
            case "초급":
                resName = "@drawable/" + easy.get(0).getImgPath();
                selectedQ = easy.get(0);
                easy.remove(easy.get(0));
                resID = getResources().getIdentifier(resName, "drawable", getPackageName());
                img.setImageResource(resID);
                break;
            case "중급":
                resName = "@drawable/" + normal.get(0).getImgPath();
                selectedQ = normal.get(0);
                normal.remove(normal.get(0));
                resID = getResources().getIdentifier(resName, "drawable", getPackageName());
                img.setImageResource(resID);
                break;
            case "고급":
                resName = "@drawable/" + hard.get(0).getImgPath();
                selectedQ = hard.get(0);
                hard.remove(hard.get(0));
                resID = getResources().getIdentifier(resName, "drawable", getPackageName());
                img.setImageResource(resID);
                break;
        }
    }

    //틀림
    public void wrong(){
        AlertDialog.Builder wrong = new AlertDialog.Builder(this);
        wrong.setTitle("오답 입니다.");
        unsure.start();
        if (vibOn){
            vibrator.vibrate(500);
        }

        wrong.setMessage(selectedQ.getSolv());
        wrong.setPositiveButton("다음문제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (easy.size() == 0 || normal.size() == 0||hard.size() == 0) {
                    finish();
                    result();
                }else {
                    gameSelect();
                }
            }
        }).show();
    }

    //정답 선택버튼 클릭
    public void btnAnswer(View view){
        if(thread != null){
            thread.interrupt();
            thread = null;
        }
        try {
            if (((Button) view).getText().toString().equals(selectedQ.getAnswer())) {
                AlertDialog.Builder right = new AlertDialog.Builder(this);
                score++;
                right.setMessage("정답 입니다!!");
                sure.start();
                right.setPositiveButton("다음문제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (easy.size() == 0 || normal.size() == 0 || hard.size() == 0) {
                            finish();
                            result();
                        } else {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    gameSelect();
                                }
                            });
                        }
                    }
                }).show();
            } else if (!((Button) view).getText().toString().equals(selectedQ.getAnswer())) {
                wrong();
            }
        }catch (Exception e){

        }
    }
    // 결과 보러 가기
        public void result(){
            Intent intent = new Intent(playActivity.this,resultActivity.class);
            intent.putExtra("score",score);
            intent.putExtra("id",id);
            startActivity(intent);
        }




    // 백스레드 처리
    class BackThread implements Runnable {
        @Override
        public void run() {
            timeBar = findViewById(R.id.progress);
            try {
                while (!thread.isInterrupted()){
                    if (timer == 0){
                        thread.interrupt();
                        thread = null;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                wrong();
                            }
                        });
                    }
                    timer--;
                    timeBar.setProgress(timer);
//                    Log.i("timer",timer+"");
                    Thread.sleep(100);
                }
            }catch (InterruptedException e){
            }
        }
    }
    Handler handler = new Handler();{
    }
}
