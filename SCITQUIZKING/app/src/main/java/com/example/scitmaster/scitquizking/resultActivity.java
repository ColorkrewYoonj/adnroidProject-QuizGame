package com.example.scitmaster.scitquizking;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class resultActivity extends Activity{
    TextView cPoint;
    TextView comment;
    ImageView img;
    Button goMain;
    int score;
    String id;
    String rs;
    String rsText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        cPoint = findViewById(R.id.score);
        comment = findViewById(R.id.comment);
        img = findViewById(R.id.resultImg);

        Intent intent = getIntent();
        score =  intent.getIntExtra("score",1);
        id = intent.getStringExtra("id");
        rs = "\n";
        rs = id + " : " + score +"개"+"\n";
        cPoint.setText(score + " / 3");
        if (score < 2){
            comment.setText("좀더 노력합시다.");
            img.setImageResource(R.drawable.gp);
        }else if(score > 1){
            comment.setText("참 잘했어요!");
            img.setImageResource(R.drawable.pika);
        }

        goMain = findViewById(R.id.goMain);
        goMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences pref = getSharedPreferences("SaveState",MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        String score_dumy2 = pref.getString("new_score","0");
        String name_dumy2 = pref.getString("new_name","0");
        rsText = pref.getString("rstext","");
        rsText += rs;
        edit.putString("rstext",rsText);
        if (score >  Integer.parseInt(score_dumy2)){
            edit.putString("new_score",String.valueOf(score));
            edit.putString("new_name",id);
            edit.putString("recent_play",rs);
        }
        edit.commit();
    }
}
