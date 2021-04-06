package com.ashrafamit.edoctorsappointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {
    private Spinner spinnerUser,spinnerCat;
    public String text,doctorsCategory;
    private ConstraintLayout doctorsDetailsForm;
    private EditText userName,degree,designation,workingHour,phone,chamber;
    private Button updateButton;

    private FirebaseAuth mAuth;
    private String currentUserId;
    private DatabaseReference rafDoctor,rafPatient,rafDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        rafDoctor= FirebaseDatabase.getInstance().getReference();
        rafPatient=FirebaseDatabase.getInstance().getReference();
        rafDoc=FirebaseDatabase.getInstance().getReference();

        InnitializeFields();
        doctorsDetailsForm.setVisibility(View.INVISIBLE);


        spinnerUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text=parent.getItemAtPosition(position).toString();
                if(text.equals("Doctor")){
                    doctorsDetailsForm.setVisibility(View.VISIBLE);
                }else{
                    doctorsDetailsForm.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        spinnerCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                doctorsCategory=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadUserData();
            }
        });

    }

    private void uploadUserData() {
        final String setUserName=userName.getText().toString();

        if (text.equals("User Status")||TextUtils.isEmpty(setUserName)){
            Toast.makeText(SettingsActivity.this,"Please complete every section...",Toast.LENGTH_SHORT).show();
        }
        else if(text.equals("Patient")) {
                HashMap<String, String> profileMap = new HashMap<>();
                profileMap.put("uid", currentUserId);
                profileMap.put("name", setUserName);
                profileMap.put("status", text);
                rafPatient.child("User").child(currentUserId).setValue(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            GoToMainActivity();
                            Toast.makeText(SettingsActivity.this, "Profile Updated Successfully...", Toast.LENGTH_SHORT).show();
                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(SettingsActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
        else{

                String deg = degree.getText().toString();
                String des = designation.getText().toString();
                String work = workingHour.getText().toString();
                String cham = chamber.getText().toString();
                String phn = phone.getText().toString();

                if (TextUtils.isEmpty(phn)||TextUtils.isEmpty(cham)||TextUtils.isEmpty(work)||TextUtils.isEmpty(des)||TextUtils.isEmpty(deg)||doctorsCategory.equals("Specialist As")){
                    Toast.makeText(SettingsActivity.this,"Please complete every section...",Toast.LENGTH_SHORT).show();
                }

                else {

                    final HashMap<String, String> profileMap = new HashMap<>();
                    profileMap.put("uid", currentUserId);
                    profileMap.put("name", setUserName);
                    profileMap.put("status", text);
                    profileMap.put("category", doctorsCategory);
                    profileMap.put("degree", deg);
                    profileMap.put("designation", des);
                    profileMap.put("chamber", cham);
                    profileMap.put("workingHour", work);
                    profileMap.put("phone", phn);
                    rafDoctor.child("User").child(currentUserId).setValue(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                rafDoc.child("Doctor").child(doctorsCategory).child(currentUserId).setValue(profileMap);
                                GoToMainActivity();
                                Toast.makeText(SettingsActivity.this, "Profile Updated Successfully...", Toast.LENGTH_SHORT).show();
                            } else {
                                String message = task.getException().toString();
                                Toast.makeText(SettingsActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        }




    private void InnitializeFields() {
        spinnerUser=(Spinner)findViewById(R.id.spinnerUserType);
        spinnerCat=(Spinner)findViewById(R.id.spinnerDoctorCategory);
        doctorsDetailsForm=(ConstraintLayout)findViewById(R.id.doctorsDetails);
        userName=(EditText)findViewById(R.id.etUserName);
        degree=(EditText)findViewById(R.id.etDegree);
        designation=(EditText)findViewById(R.id.etdesignation);
        workingHour=(EditText)findViewById(R.id.etWorkingHour);
        phone=(EditText)findViewById(R.id.etPhnNumber);
        chamber=(EditText)findViewById(R.id.etChamberLocation);
        updateButton=(Button)findViewById(R.id.btnUpdate);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.userType,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUser.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2=ArrayAdapter.createFromResource(this,R.array.category,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCat.setAdapter(adapter2);
    }


    private void GoToMainActivity() {
        Intent mainIntent= new Intent(SettingsActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
