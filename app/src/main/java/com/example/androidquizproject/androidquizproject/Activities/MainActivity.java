package com.example.androidquizproject.androidquizproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidquizproject.androidquizproject.Classes.Question;
import com.example.androidquizproject.androidquizproject.Classes.QuestionSet;
import com.example.androidquizproject.androidquizproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference questions;
    Button GotoSignupBt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GotoSignupBt=findViewById(R.id.GotoSignupBt);                                               //זיהוי כפתור התחברות

        GotoSignupBt.setOnClickListener(new View.OnClickListener() {                                //מעבר להתחברות
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);  //מעבר לאקטיביטי הבא
                startActivity(intent);
            }
        });
        database = FirebaseDatabase.getInstance();                                                   //כתובת מסד הנתונים מהפיירבייס
        questions = database.getReference("Questions");                                        //קבלת השאלות מכתובת מסד הנתונים



        questions.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {                      //הוספת השאלות למערך השאלות בקוד
                    Question ques = postSnapshot.getValue(Question.class);
                    QuestionSet.questionList.add(ques);
                }
                Log.d("FirebaseLoaded", "Firebase Loaded ");
                Collections.shuffle(QuestionSet.questionList);                                      //ערבוב השאלות
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {  }
        });


    }

}
