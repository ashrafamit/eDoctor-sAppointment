package com.ashrafamit.edoctorsappointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class update_Patient_informations extends AppCompatActivity {
    private EditText info;
    private Button update;
    public String currentUserId;
    private DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__patient_informations);

        innitializations();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String data=info.getText().toString();

                if(TextUtils.isEmpty(data)){
                    Toast.makeText(update_Patient_informations.this, "Please complete every section...", Toast.LENGTH_SHORT).show();
                }else{
                    ref.child("name").setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                GoToMainActivity();
                                Toast.makeText(update_Patient_informations.this, "Profile Updated Successfully...", Toast.LENGTH_SHORT).show();
                            } else {
                                String message = task.getException().toString();
                                Toast.makeText(update_Patient_informations.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void GoToMainActivity() {
    }

    private void innitializations() {
        info=(EditText)findViewById(R.id.etPatientInfo);
        update=(Button)findViewById(R.id.btnUpdatePatinetInfo);
        currentUserId=getIntent().getExtras().getString("uid");
        ref= FirebaseDatabase.getInstance().getReference().child("User").child(currentUserId);
    }
}
