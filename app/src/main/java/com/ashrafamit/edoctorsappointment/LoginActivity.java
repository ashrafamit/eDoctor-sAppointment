package com.ashrafamit.edoctorsappointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText userEmail, userPasswrod;
    private TextView needNewAccountLink,forgotPasswordLink;

    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();
        InitializeFields();

        needNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToRegisterActivity();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlowUserToLogin();
            }
        });

        forgotPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPass();
            }
        });
    }


    private void InitializeFields() {
        loginButton=(Button)findViewById(R.id.login_button);
        userEmail=(EditText)findViewById(R.id.login_email);
        userPasswrod=(EditText)findViewById(R.id.login_password);
        needNewAccountLink=(TextView)findViewById(R.id.need_new_account_link);
        forgotPasswordLink=(TextView)findViewById(R.id.forgot_password_link);

        loadingBar= new ProgressDialog(this);
    }

    private void AlowUserToLogin() {
        String email=userEmail.getText().toString();
        String password=userPasswrod.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter an email...",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter your Password...",Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Signing in");
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        GoToMainActivity();
                        Toast.makeText(LoginActivity.this,"Login Successfull...",Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }else
                    {
                        String message= task.getException().toString();
                        Toast.makeText(LoginActivity.this,"Error : "+message,Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }

                }
            });
        }
    }
    private void forgotPass() {
        AlertDialog.Builder builder= new AlertDialog.Builder(LoginActivity.this,R.style.AlertDialog);
        builder.setTitle("Enter your valid Email to get reset password link..");

        final EditText setUserEmail=new EditText(LoginActivity.this);
        setUserEmail.setHint("e.g abc123@gmail.com");
        builder.setView(setUserEmail);

        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userEmailToResetPw=setUserEmail.getText().toString();
                if (TextUtils.isEmpty(userEmailToResetPw)){
                    Toast.makeText(LoginActivity.this,"Please write your valid Email...",Toast.LENGTH_SHORT).show();
                }else {
                    mAuth.sendPasswordResetEmail(userEmailToResetPw).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(LoginActivity.this,"Please check your Email...",Toast.LENGTH_SHORT).show();
                            }else{
                                String message=task.getException().getMessage();
                                Toast.makeText(LoginActivity.this,"Error Occured :"+message,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    private void GoToMainActivity() {
        Intent mainIntent= new Intent(LoginActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
    private void GoToRegisterActivity() {
        Intent registerIntent= new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(registerIntent);
    }
}
