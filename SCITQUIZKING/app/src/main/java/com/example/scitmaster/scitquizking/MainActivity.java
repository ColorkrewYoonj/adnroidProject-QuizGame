package com.example.scitmaster.scitquizking;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    LinearLayout main;
    EditText gameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameId = findViewById(R.id.gameID);
        main = findViewById(R.id.main);

        main.setOnTouchListener(new MainOnTouch());
    }

    class MainOnTouch implements View.OnTouchListener{
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (gameId.getText().toString().length() != 0){

                Intent intent = new Intent(MainActivity.this,StartActivity.class);
                intent.putExtra("id",gameId.getText().toString());

                startActivity(intent);
                finish();
                return true;
            }
            return false;
        }
    }
}
