package com.example.notespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class CreatAccoiuntActivity extends AppCompatActivity {
    EditText emailEditText,passwordEditText,confirmPasswordEdittext;
    Button creatAccountbtn;
    ProgressBar progressBar;
    TextView loginBtnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_accoiunt);
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        confirmPasswordEdittext = findViewById(R.id.confirm_password_edit_text);
        creatAccountbtn = findViewById(R.id.create_account_btn);


        progressBar  = findViewById(R.id.progress_bar);
        loginBtnTextView = findViewById(R.id.login_text_view_btn);

        
        creatAccountbtn.setOnClickListener(v->createAccount());
        loginBtnTextView.setOnClickListener(v -> startActivity(new Intent(CreatAccoiuntActivity.this,loginActivity.class))); //papu@gmail.com
    }

    
    private void createAccount() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmpassword = confirmPasswordEdittext.getText().toString();

        boolean isValidate  = validateData(email,password,confirmpassword);

        if(!isValidate){
            return;
        }
        createAccountInFirebase(email,password);

    }

    void createAccountInFirebase(String email,String password){

        changeInProgres(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreatAccoiuntActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgres(false);
                      if(task.isSuccessful()){
                          Toast.makeText(CreatAccoiuntActivity.this, "Succesfully create account ,check email to verify", Toast.LENGTH_SHORT).show();
                          firebaseAuth.getCurrentUser().sendEmailVerification();
                          firebaseAuth.signOut();
                          finish();
                      }else {
                          Toast.makeText(CreatAccoiuntActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                          Log.v("Error",task.getException().toString());

                      }
                    }

                });



    }


    void changeInProgres(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            creatAccountbtn.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            creatAccountbtn.setVisibility(View.VISIBLE);
        }

    }

    boolean validateData(String email,String password , String confirmpassword){

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is invalid");
            return  false;
        }
        if(password.length()<6){
            passwordEditText.setError("Password length is invalid");
            return false;
        }
        if(!password.equals(confirmpassword)){
            confirmPasswordEdittext.setError("Password not matched");
            return  false;

        }
          return true;
    }

}
