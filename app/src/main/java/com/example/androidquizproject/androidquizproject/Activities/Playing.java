package com.example.androidquizproject.androidquizproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.androidquizproject.androidquizproject.Classes.QuestionSet;
import com.example.androidquizproject.androidquizproject.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Playing extends AppCompatActivity implements View.OnClickListener{

    int index = 0, score = 0, thisQuestion = 1, totalQuestion=22;
    FirebaseDatabase database;
    DatabaseReference questions;
    Button btnA, btnB, btnC, btnD;
    TextView textViewClock,scoreTxt;
    ImageView ImgView;
    String userName;
    CountDownTimer cTimer = null;
    long millisPause;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        userName= getIntent().getStringExtra("userName");                                       //קבלת שם המשתמש ממסך קודם

        database = FirebaseDatabase.getInstance();
        questions = database.getReference("Questions");
        ImgView = findViewById(R.id.imageQuestion);

        btnA = findViewById(R.id.Answer1);
        btnB = findViewById(R.id.Answer2);
        btnC = findViewById(R.id.Answer3);
        btnD = findViewById(R.id.Answer4);
        scoreTxt = findViewById(R.id.scoreNumber);

        btnA.setOnClickListener(this);                                                              //הפעלת פונקציות הכפתורים בהתאם לתשובה הנכונה
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);
        textViewClock = findViewById(R.id.time);
        showQuestion(index);
    }

    private void showQuestion(int index){
        if (index+1<totalQuestion) {                                                                //עבור על כל השאלות שבמסד הנתונים
            scoreTxt.setText("" + score);
            Glide.with(getApplicationContext()).load(QuestionSet.questionList.get(index).getPictureAddress()).into(ImgView);
            btnA.setText(QuestionSet.questionList.get(index).getAnswerA());
            btnB.setText(QuestionSet.questionList.get(index).getAnswerB());
            btnC.setText(QuestionSet.questionList.get(index).getAnswerC());
            btnD.setText(QuestionSet.questionList.get(index).getAnswerD());
            startTimer(8000);                                                                   //טיימר לכל שאלה 8 שניות
        }

        else
        {
            Intent intent = new Intent(Playing.this,EndGame.class);                     //מעבר לסוף המשחק והעברת נתוני השחקן ותוצאתו
            intent.putExtra("userName",userName);
            intent.putExtra("score",score);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        Button clickedButton = (Button) v;
        cancelTimer();
        String CorrectAnswer = QuestionSet.questionList.get(index).getCorrectAnswer();
        switch (clickedButton.getId()) {                                                            //בדיקה מול מסד הנתונים אם התשובה שנלחצה נכונה
            case R.id.Answer1:
                if (CorrectAnswer.equals("AnswerA")) {
                    score++;}
                break;
            case R.id.Answer2:
                if (CorrectAnswer.equals("AnswerB")) {score++;}
                break;
            case R.id.Answer3:
                if (CorrectAnswer.equals("AnswerC")) {score++;}
                break;
            case R.id.Answer4:
                if (CorrectAnswer.equals("AnswerD")) {score++;}
                break;
        }
            thisQuestion++;
            showQuestion(++index);
    }

    //start timer function
    void startTimer(long millis) {                                                                  //פונ' טיימר
        cTimer = new CountDownTimer(millis, 1000) {
            public void onTick(long millisUntilFinished) {
                millisPause=millisUntilFinished;
                textViewClock.setText("" + millisUntilFinished / 1000);
            }
            public void onFinish() {                                                                //כאשר מסתיים הזמן, עבור לשאלה הבאה
                showQuestion(++index);
            }
        };
        cTimer.start();                                                                             //הפעל טיימר
    }
    //cancel timer
    void cancelTimer() {
        if(cTimer!=null)
            cTimer.cancel();
    }
}
