package com.example.quiz;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView QuestionTxt, OptionTxt1, OptionTxt2, OptionTxt3, OptionTxt4, timer;
    ProgressBar progressbar;
    int time = 20;
    ArrayList<String> questions,ans;
    ArrayList<ArrayList<String>> options;
    boolean isRunning=true;
    int count=0;
    int flag;
    MediaPlayer correct_mp =new MediaPlayer();;
    MediaPlayer fail_mp=new MediaPlayer();
    MediaPlayer timer_mp=new MediaPlayer();
    Intent fromOpening;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidNetworking.initialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fromOpening=getIntent();
        flag=fromOpening.getIntExtra("Flag",0);
        String difficulty=fromOpening.getStringExtra("difficulty");
        //connecting ui components to java
        initVar();


        //Api url
        //Default url for random category questions
        String url = "https://opentdb.com/api.php?amount=10&type=multiple";

        //selecting category based on flag value
        if(flag>0)
        {
            url = "https://opentdb.com/api.php?amount=10&category="+flag+"&difficulty="+difficulty+"&type=multiple";
            assert difficulty != null;
            if(difficulty.equals("Any"))
            {
                url = "https://opentdb.com/api.php?amount=10&category="+flag+"&type=multiple";
            }
        }

        //calling Api call method
        ApiCall(url);
        //sets audio
        AddMedia();


    }
    //Method that initializes the audio components
    private void AddMedia() {
        correct_mp.setAudioStreamType(AudioManager.STREAM_MUSIC);

        fail_mp.setAudioStreamType(AudioManager.STREAM_MUSIC);

        timer_mp.setAudioStreamType(AudioManager.STREAM_MUSIC);

        String correct_path="android.resource://"+getPackageName()+"/raw/correct";
        String fail_path="android.resource://"+getPackageName()+"/raw/fail";
        String timer_path="android.resource://"+getPackageName()+"/raw/timer";

        Uri correct_uri= Uri.parse(correct_path);
        Uri fail_uri= Uri.parse(fail_path);
        Uri timer_uri=Uri.parse(timer_path);

        try {
            correct_mp.setDataSource(getApplicationContext(),correct_uri);
            correct_mp.prepare();
            fail_mp.setDataSource(getApplicationContext(),fail_uri);
            fail_mp.prepare();
            timer_mp.setDataSource(getApplicationContext(),timer_uri);
            timer_mp.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //initiating values of multiple variables
    private void initVar() {
        QuestionTxt = findViewById(R.id.QuestionTxt);
        OptionTxt1 = findViewById(R.id.OptionTxt1);
        OptionTxt2 = findViewById(R.id.OptionTxt2);
        OptionTxt3 = findViewById(R.id.OptionTxt3);
        OptionTxt4 = findViewById(R.id.OptionTxt4);
        timer = findViewById(R.id.timer);
        progressbar = findViewById(R.id.progressbar);

        questions =new ArrayList<>();
        ans =new ArrayList<>();
        options =new ArrayList<>();
    }

    //Method to load questions
    private void loadQuestions(int QuestionNo) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                //End the round when the questions reach the end
                if(QuestionNo>=10)Next(getApplicationContext(),ScoreActivity.class);

                //Reset timer when the new Question loads
                time=20;
                timer_mp.start();
                timer_mp.setLooping(true);
                //resume Time
                isRunning=true;
                //resting TextView Colors
                QuestionTxt.setTextColor(getColor(R.color.black));
                OptionTxt1.setTextColor(getColor(R.color.black));
                OptionTxt2.setTextColor(getColor(R.color.black));
                OptionTxt3.setTextColor(getColor(R.color.black));
                OptionTxt4.setTextColor(getColor(R.color.black));

                //Shuffle the list of option for random option every time
                Collections.shuffle(options.get(QuestionNo));

                //sets the question ans options in ui
                QuestionTxt.setText(questions.get(QuestionNo));
                OptionTxt1.setText(String.format("A. %s",options.get(QuestionNo).get(0)));
                OptionTxt2.setText(String.format("B. %s",options.get(QuestionNo).get(1)));
                OptionTxt3.setText(String.format("C. %s",options.get(QuestionNo).get(2)));
                OptionTxt4.setText(String.format("D. %s",options.get(QuestionNo).get(3)));

                //validation of ans;
                Check(OptionTxt1,String.format("A. %s",ans.get(QuestionNo)),QuestionNo);
                Check(OptionTxt2,String.format("B. %s",ans.get(QuestionNo)),QuestionNo);
                Check(OptionTxt3,String.format("C. %s",ans.get(QuestionNo)),QuestionNo);
                Check(OptionTxt4,String.format("D. %s",ans.get(QuestionNo)),QuestionNo);

            }
        },3000);
    }

    //Api Call method the calls Api url and Assign values in the list of question options and correct ans
    private void ApiCall(String url) {
        AndroidNetworking.get(url).setPriority(Priority.HIGH).build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progressbar.setVisibility(View.GONE);
//                        Log.d("Response",jsonObject.toString());
                        loadQuestions(0);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                starTimer(new Handler());
                            }
                        },3000);
                        try {
                            JSONArray arr = jsonObject.getJSONArray("results");
                            for(int i=0;i<arr.length();i++) {
                                ArrayList<String> wrong=new ArrayList<>();
                                JSONObject jsonObj = arr.getJSONObject(i);
                                String Question = parse(jsonObj.getString("question"));
                                String Option1 = parse(jsonObj.getString("correct_answer"));
                                JSONArray incorrect_arr = jsonObj.getJSONArray("incorrect_answers");
                                String Option2 = parse(incorrect_arr.getString(0));
                                String Option3 = parse(incorrect_arr.getString(1));
                                String Option4 = parse(incorrect_arr.getString(2));
                                questions.add(Question);
                                ans.add(Option1);
                                wrong.add(Option1);
                                wrong.add(Option3);
                                wrong.add(Option2);
                                wrong.add(Option4);
                                options.add(wrong);
                            }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("ERROR", anError.toString());
                    }
                });
    }
    //Method that starts the clock
    private void starTimer(Handler handler) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(isRunning) time--;
                int sec = time % 60;

                String timeStr = String.format(Locale.getDefault(), "%02d", sec);
                timer.setText(timeStr);

                if(time==0)
                {
                      Next(getApplicationContext(),ScoreActivity.class);
                      timer_mp.pause();
                      fail_mp.start();
                    finish();
                }

                handler.postDelayed(this, 1000);
            }
        });
    }
    //Method for ans Validation that takes Text View,Ans and QuestionNo as Parameters and validates
    public void Check(TextView textView,String ans,int QuestionNo)
    {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textView.getText().toString().equals(ans))
                {
                    correct_mp.start();
                    textView.setTextColor(getColor(R.color.green));
//                    Toast.makeText(MainActivity.this, "Correct", Toast.LENGTH_SHORT).show();
                    isRunning=false;
                    timer_mp.pause();
                    count++;
                    loadQuestions(QuestionNo+1);
                }
                else
                {
                    //start wrong ans sound
                    fail_mp.start();
                    //color the correct ans
                    if(OptionTxt1.getText().toString().substring(3).equals(ans.substring(3)))OptionTxt1.setTextColor(getColor(R.color.green));
                    if(OptionTxt2.getText().toString().substring(3).equals(ans.substring(3)))OptionTxt2.setTextColor(getColor(R.color.green));
                    if(OptionTxt3.getText().toString().substring(3).equals(ans.substring(3)))OptionTxt3.setTextColor(getColor(R.color.green));
                    if(OptionTxt4.getText().toString().substring(3).equals(ans.substring(3)))OptionTxt4.setTextColor(getColor(R.color.green));
//                    Toast.makeText(MainActivity.this, "Wrong!", Toast.LENGTH_SHORT).show();
                    isRunning=false;
                    timer_mp.pause();
                    textView.setTextColor(getColor(R.color.Red));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Next(MainActivity.this,ScoreActivity.class);
                            finish();
                        }
                    },3000);
                }
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });
    }
    //Removes unnecessary Symbols from The String and replace quot as symbol of quot
    private String parse(String str)
    {
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<str.length();i++)
        {
            if(!(str.charAt(i)=='@'||str.charAt(i)=='#'||str.charAt(i)=='&'||str.charAt(i)==';'))
            {
                sb.append(str.charAt(i));
            }
        }
        return sb.toString().replace("quot","").replace("039","'");
    }
    //Intent passing form this class to desired class and pass the required bundle
    private void Next(Context context,Class nextClass)
    {
        Intent iNext=new Intent(context,nextClass);
        iNext.putExtra("Count",count);
        iNext.putExtra("Category",fromOpening.getIntExtra("Flag",0));
        iNext.putExtra("difficulty",fromOpening.getStringExtra("difficulty"));
        startActivity(iNext);
    }
}
