package com.example.scitmaster.scitquizking;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class rankingActivity extends Activity{

    TextView rScoreText;
    TextView hScoreText;
    String id;
    String rsText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        hScoreText = findViewById(R.id.hscore);
        rScoreText = findViewById(R.id.rscore);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        SharedPreferences pref = getSharedPreferences("SaveState",MODE_PRIVATE);


        String hScore = pref.getString("new_score","");
        String hScoreName = pref.getString("new_name","없음");
        rsText = pref.getString("rstext","");
        hScoreText.setText(hScoreName + " : " + hScore + "개");
        rScoreText.setText(rsText);
    }

    public void backM(View view){
        Intent intent = new Intent(rankingActivity.this,StartActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
        finish();
    }
}
