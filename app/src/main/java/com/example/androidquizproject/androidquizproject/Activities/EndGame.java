package com.example.androidquizproject.androidquizproject.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androidquizproject.androidquizproject.Classes.QuestionSet;
import com.example.androidquizproject.androidquizproject.Classes.scoreToFire;
import com.example.androidquizproject.androidquizproject.R;
import com.example.androidquizproject.androidquizproject.Classes.record_adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EndGame extends AppCompatActivity {
    String userName;
    TextView scoreTv;
    View dialogView;
    DatabaseReference ref;
    scoreToFire sToFire;
    Button restart,menuBtn,rankingTable;
    ImageView gameoverIV;
    FirebaseDatabase database;
    DatabaseReference scoretab;
    int score2;
    ListView recordListView;
    ArrayList<scoreToFire> scoreList;
    boolean connected=false;
    scoreToFire scoreFire;
    record_adapter adapter1;
    AlertDialog.Builder builder;

    @Override
    public void onBackPressed() {                                                                   //כאשר לחצן האחורה נלחץ, נודיע למשתמש שהוא
        AlertDialog.Builder builder;                                                                //הולך להתנתק מהמשתמש שלו ולעבור למסך ההתחלה
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle(getResources().getString(R.string.quitAlert))
                .setMessage(R.string.quit)                     //נשאל את המשתמש האם הוא בטוח שזה מה שרוצה לעשות
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(EndGame.this, MainActivity.class); //מעבר למסך הראשי במידה וכן
                        startActivity(intent);
                        ActivityCompat.finishAffinity(EndGame.this);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) { }                      //אל תעשה כלום אם מחליט שלא לחזור
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);
        userName= getIntent().getStringExtra("userName");
        score2 = getIntent().getIntExtra("score",0);
        scoreTv=findViewById(R.id.ScoreNumberTV);
        restart=findViewById(R.id.restart);
        rankingTable=findViewById(R.id.RankingTable);
        menuBtn=findViewById(R.id.menuBtn);
        gameoverIV=findViewById(R.id.gameoverIV);
        scoreFire = new scoreToFire(userName, score2);

        scoreTv.setText(Integer.toString(score2));
        scoreTv.invalidate();

        Animation anim=AnimationUtils.loadAnimation(this,R.anim.game_over_anim);
        gameoverIV.startAnimation(anim);                                                            //הפעלת אנימציה של סוף המשחק


        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                                          //בלחיצה על הפעל מחדש, נערבב שוב את השאלות
                Collections.shuffle(QuestionSet.questionList);
                Intent intent = new Intent(EndGame.this, Playing.class);
                intent.putExtra("userName",userName);
                startActivity(intent);
            }
        });
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                                          //מעבר למסך הראשי (התנתקות)
                Intent intent = new Intent(EndGame.this, MainActivity.class);
                intent.putExtra("userName",userName);
                startActivity(intent);
            }
        });
        rankingTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                                     //מעבר לטבלת השיאים
                scoreList=new ArrayList<>();
                adapter1=new record_adapter(scoreList,EndGame.this);
                builder=new AlertDialog.Builder(EndGame.this);
                dialogView=getLayoutInflater().inflate(R.layout.record_layout,null);
                recordListView=dialogView.findViewById(R.id.listView);
                builder.setView(dialogView).setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
                database=FirebaseDatabase.getInstance();
                ref=database.getReference("RankingTable");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {                  //הוספת השיאים ממסד הנתונים לרשימה
                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                            sToFire=ds.getValue(scoreToFire.class);
                            scoreList.add(sToFire);
                        }

                        Collections.sort(scoreList, new Comparator<scoreToFire>() {
                            @Override
                            public int compare(scoreToFire lhs, scoreToFire rhs) {                     // מיון תוצאות השיאים מהגדול לקטן
                                return lhs.getScore() > rhs.getScore() ? -1 : (lhs.getScore() < rhs.getScore()) ? 1 : 0;
                            }
                        });
                        recordListView.setAdapter(adapter1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                                                                                                    //בודק האם אנחנו מחוברים לאינטרנט
                                                                                                    //או על וייפיי או על רשת סלולרית
            connected = true;
        }
        else
            connected = false;

        if(connected) {                                                                             //אם יש חיבור הצג רשימת שיאים
            database = FirebaseDatabase.getInstance();
            scoretab = database.getReference("RankingTable");
            if (scoreFire == null)
                return;
            else if (scoreFire.getUserName().length() == 0)
                return;
            else scoretab.child(userName).setValue(scoreFire);
        }
    }

}
