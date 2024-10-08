package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ScoreActivity extends AppCompatActivity {
TextView score,prize;
Button restartBtn,toHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_score);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        score=findViewById(R.id.score);
        prize=findViewById(R.id.prize);
        restartBtn=findViewById(R.id.restartBtn);
        toHome=findViewById(R.id.toHome);


        Intent fromMain=getIntent();
        int scoreCount=fromMain.getIntExtra("Count",0);
        int flag=fromMain.getIntExtra("Category",0);
        String difficulty=fromMain.getStringExtra("difficulty");
        int prizeMoney=0;
        if(scoreCount<=5)
        {
            prizeMoney=scoreCount*1000;
        }
        else if(scoreCount>5 && scoreCount<=7)
        {
            prizeMoney=scoreCount*2000;
        }
        else prizeMoney=scoreCount*3000;
        score.setText(String.format("Correct Answers : %s",scoreCount));
        prize.setText(String.format("Prize Money : %s",prizeMoney));

        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iRestart=new Intent(ScoreActivity.this,MainActivity.class);
                iRestart.putExtra("Flag",flag);
                iRestart.putExtra("difficulty",difficulty);
                startActivity(iRestart);
                finish();
            }
        });
        toHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScoreActivity.this,OpeningActivity.class));
                finish();
            }
        });
    }
}