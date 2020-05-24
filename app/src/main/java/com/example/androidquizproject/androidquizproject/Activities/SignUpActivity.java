package com.example.androidquizproject.androidquizproject.Activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidquizproject.androidquizproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.example.androidquizproject.androidquizproject.Classes.User;


public class SignUpActivity extends Activity {
    MaterialEditText edtNewUser,edtNewPassword,edtNewEmail; // for sign up
    EditText edtPassword,edtUser; // for sign in
    Button btnSignUp,btnSignIn;
    DatabaseReference users;
    boolean connected=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //firebase
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        users= database.getReference("Users");
        btnSignIn=(Button)findViewById(R.id.btn_sign_in);
        btnSignUp=(Button)findViewById(R.id.btn_sign_up);
        edtUser= (EditText) findViewById(R.id.edtUser);
        edtUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {                                              //תקינות שם משתמש
                if(s.length()==0){
                    edtUser.setError(getResources().getString(R.string.enterYourName));
                    btnSignIn.setEnabled(false);
                }
                else
                    btnSignIn.setEnabled(true);
            }
        });
        edtPassword= (EditText) findViewById(R.id.edtPassword);
        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {                                               //תקינות סיסמא
                if(s.length()==0){
                    edtPassword.setError(getResources().getString(R.string.enterYourPass));
                    btnSignIn.setEnabled(false);
                }
                else
                    btnSignIn.setEnabled(true);
            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUpDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {                                   //תקינות קלטי משתמש וסיסמא
            @Override
            public void onClick(View v) {
                if(edtUser.getText().toString().length()==0){
                    btnSignIn.setEnabled(false);
                    edtUser.setError(getResources().getString(R.string.enterYourName));
                }
                else if(edtPassword.getText().toString().length()==0){
                    btnSignIn.setEnabled(false);
                    edtPassword.setError(getResources().getString(R.string.enterYourPass));
                }
                else{
                    signIn(edtUser.getText().toString(),edtPassword.getText().toString());
                }
            }
        });

                                                                                                    //בדיקה אם יש חיבור לאינטרנט
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;
        if(!connected){
            Toast.makeText(SignUpActivity.this, "please connect to the internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {                                                                   //כאשר כפתור אחורה נלחץ, חזור למסך הראשי
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    private void signIn(final String user, final String pwd) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(user).exists())                                              //בדיקה האם המשתמש המתחבר קיים במסד
                {
                    if (!user.isEmpty())
                    {
                        User login = dataSnapshot.child(user).getValue(User.class);
                        if (login.getPassword().equals(pwd)){                                           //זיהוי משתמש במסד המשתמשים
                            Intent intent=new Intent(SignUpActivity.this,Playing.class); //מתחיל את המשחק אחרי הרשמה
                            intent.putExtra("userName",user);
                            startActivity(intent);
                        }
                        else
                            edtPassword.setError(getResources().getString(R.string.WrongPassword));                                  //אימות הסיסמא המוזנת מול המסד
                    }
                    else
                    {
                        edtUser.setError(getResources().getString(R.string.enterYourName));
                    }
                }
                else
                {
                    edtUser.setError(getResources().getString(R.string.userexist));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

    }


    private void showSignUpDialog() {                                                               //מסך הרשמה
        final AlertDialog.Builder alertDailog = new AlertDialog.Builder(SignUpActivity.this);
        alertDailog.setTitle(R.string.SignUp);

        alertDailog.setMessage(R.string.fillInformation);
        LayoutInflater inflater = this.getLayoutInflater();
        View sign_up_layuot= inflater.inflate(R.layout.sign_up_layou2t,null);
        alertDailog.setView(sign_up_layuot);
        alertDailog.setNegativeButton(R.string.cancel, new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {                                //כפתור ביטול ההרשמה
                dialog.dismiss();
            }
        });
        alertDailog.setPositiveButton(R.string.Ok, new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {                                    //כפתור אישור הרשמה
                final User user = new User(edtNewUser.getText().toString(),edtNewPassword.getText().toString(),edtNewEmail.getText().toString());
                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {                           //בדיקה אם היוזר קיים במערכת בזמן הרשמה
                        if (dataSnapshot.child(user.getUserName()).exists())
                            Toast.makeText(SignUpActivity.this, R.string.Useralreadyexist, Toast.LENGTH_SHORT).show();
                        else
                        {
                            users.child(user.getUserName()).setValue(user);                         //בעת סיום ההרשמה, נעבור ישר למשחק
                            Toast.makeText(SignUpActivity.this, R.string.Userregistrationsuccess, Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(SignUpActivity.this,Playing.class);
                            intent.putExtra("userName",user.getUserName());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                dialog.dismiss();

            }
        });

        edtNewUser= (MaterialEditText) sign_up_layuot.findViewById(R.id.edtNewUserName);
        edtNewEmail= (MaterialEditText) sign_up_layuot.findViewById(R.id.edtNewEmail);
        edtNewPassword= (MaterialEditText) sign_up_layuot.findViewById(R.id.edtNewPassword);
        final AlertDialog d = alertDailog.create();

        edtNewUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {                                              //לא ניתן ללחוץ OK כל עוד שם המשתמש ריק
                final Button okButton = d.getButton(AlertDialog.BUTTON_POSITIVE);
                if(edtNewUser.getText().length() == 0 || edtNewEmail.getText().length() == 0 || edtNewPassword.getText().length() == 0) {
                    okButton.setEnabled(false);
                }
                else {
                    okButton.setEnabled(true);
                }
            }
        });


        edtNewEmail.addTextChangedListener(new TextWatcher() {                                      //לא ניתן ללחוץ OK כל עוד האימייל ריק
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                final Button okButton = d.getButton(AlertDialog.BUTTON_POSITIVE);
                if(edtNewUser.getText().length() == 0 || edtNewEmail.getText().length() == 0 || edtNewPassword.getText().length() == 0) {
                    okButton.setEnabled(false);
                } else {
                    okButton.setEnabled(true);
                }
            }
        });


        edtNewPassword.addTextChangedListener(new TextWatcher() {                                   //לא ניתן ללחוץ OK כל עוד הסיסמא ריקה
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                final Button okButton = d.getButton(AlertDialog.BUTTON_POSITIVE);
                if(edtNewUser.getText().length() == 0 || edtNewEmail.getText().length() == 0 || edtNewPassword.getText().length() == 0) {
                    okButton.setEnabled(false);
                } else {
                    okButton.setEnabled(true);
                }
            }
        });

        d.show();
        d.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);




    }


}


