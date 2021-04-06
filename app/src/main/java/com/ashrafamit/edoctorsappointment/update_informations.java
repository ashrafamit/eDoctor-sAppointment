package com.ashrafamit.edoctorsappointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class update_informations extends AppCompatActivity {
    private Spinner doctorsInfoList;
    private EditText info;
    private Button update;
    public String text,currentUserId,UserStatus,category;
    private DatabaseReference ref,doctorRef,doctorReff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_informations);

        initialization();



        doctorsInfoList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String data=info.getText().toString();
                    if (text.equals("Select") || TextUtils.isEmpty(data)) {
                        Toast.makeText(update_informations.this, "Please complete every section...", Toast.LENGTH_SHORT).show();
                    } else {
                        doctorRef.child(category).child(currentUserId).child(text).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    doctorReff.child(text).setValue(data);
                                    GoToMainActivity();
                                    Toast.makeText(update_informations.this, "Profile Updated Successfully...", Toast.LENGTH_SHORT).show();
                                } else {
                                    String message = task.getException().toString();
                                    Toast.makeText(update_informations.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    
            }
        });


    }

    private void GoToMainActivity() {
    }

    private void initialization() {
        info=(EditText)findViewById(R.id.etInfo);
        doctorsInfoList=(Spinner) findViewById(R.id.updateInfoSpinner);
        update=(Button)findViewById(R.id.btnUpdateInfo);
        currentUserId=getIntent().getExtras().getString("uid");

        doctorRef=FirebaseDatabase.getInstance().getReference().child("Doctor");
        doctorReff=FirebaseDatabase.getInstance().getReference().child("User").child(currentUserId);

        ref= FirebaseDatabase.getInstance().getReference().child("User").child(currentUserId);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserStatus=dataSnapshot.child("status").getValue().toString();
                    category=dataSnapshot.child("category").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.update,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doctorsInfoList.setAdapter(adapter);
    }
}
