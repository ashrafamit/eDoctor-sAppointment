package com.ashrafamit.edoctorsappointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class DoctorsDetails extends AppCompatActivity {

    private ListView doctorsDetails;
    private Button getAppointment;
    private EditText patientHistry,patientsName,age,gender,patientsPhn;
    private Spinner wokingDays;

    ValueEventListener listener;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinnerDataList;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    ArrayList<String> list2;
    ArrayAdapter<String>adapter2;

    private DatabaseReference reference,ref,patientsRef,PatientNameRef,PatientMessageKeyRef;;

    public String docotrsCategory,doctorsUid,date,currentUserId,currentDate,currentTime,docname,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_details);

        Innitializations();
        retriveData();
        retriveDataForDoctorDetails();

        wokingDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                date = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });

    }


    private void Innitializations() {
        doctorsDetails=(ListView)findViewById(R.id.listView_doctorsDetails);
        getAppointment=(Button)findViewById(R.id.btnAppoint);
        patientHistry=(EditText)findViewById(R.id.etpatientsHistry);
        wokingDays=(Spinner)findViewById(R.id.spWorkingDays);
        age=(EditText)findViewById(R.id.etPatientsAge);
        patientsPhn=(EditText)findViewById(R.id.etPatientsPhnNumber);
        gender=(EditText)findViewById(R.id.etPatientsGender);
        patientsName=(EditText)findViewById(R.id.etPatientsName);

        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        currentUserId=mAuth.getCurrentUser().getUid();
        PatientNameRef=FirebaseDatabase.getInstance().getReference().child("User").child(currentUserId).child("notifications");

        docotrsCategory=getIntent().getExtras().getString("category");
        doctorsUid=getIntent().getExtras().getString("uid");

        patientsRef= FirebaseDatabase.getInstance().getReference("Doctor").child(docotrsCategory).child(doctorsUid).child("WorkingDay");
        reference= FirebaseDatabase.getInstance().getReference("Doctor").child(docotrsCategory).child(doctorsUid);
        list2=new ArrayList<>();
        adapter2=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list2);

        ref= FirebaseDatabase.getInstance().getReference("Doctor").child(docotrsCategory).child(doctorsUid).child("WorkingDay");

        spinnerDataList=new ArrayList<>();
        adapter=new ArrayAdapter<String>(DoctorsDetails.this,android.R.layout.simple_spinner_dropdown_item,spinnerDataList);
        wokingDays.setAdapter(adapter);

    }


    private void retriveData() {
        listener=ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    spinnerDataList.add(ds.getKey().toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void retriveDataForDoctorDetails() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                docname=dataSnapshot.child("name").getValue().toString();
                String degree=dataSnapshot.child("degree").getValue().toString();
                String designation=dataSnapshot.child("designation").getValue().toString();
                String wokingHour=dataSnapshot.child("workingHour").getValue().toString();
                phone=dataSnapshot.child("phone").getValue().toString();
                String chamber=dataSnapshot.child("chamber").getValue().toString();
                String category=dataSnapshot.child("category").getValue().toString();
                list2.add("Name : "+docname+"\nDegrees : "+degree+"\nSpecialist As : "+category+"\nDesignation : "+designation+"\nChamber Location : "+chamber+"\nVisiting Hours : "+wokingHour+"\nPhone No : "+phone);

                doctorsDetails.setAdapter(adapter2);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void uploadData() {
        String n=patientsName.getText().toString();
        String a=age.getText().toString();
        String p=patientsPhn.getText().toString();
        String g=gender.getText().toString();
        String h=patientHistry.getText().toString();
        if(TextUtils.isEmpty(n)||TextUtils.isEmpty(a)||TextUtils.isEmpty(p)||TextUtils.isEmpty(g)||TextUtils.isEmpty(h)){
            Toast.makeText(DoctorsDetails.this,"Please complete every section...",Toast.LENGTH_SHORT).show();
        }else {
            final HashMap<String, String> profileMap = new HashMap<>();

            profileMap.put("name", n);
            profileMap.put("age", a);
            profileMap.put("phone", p);
            profileMap.put("gender", g);
            profileMap.put("history", h);

            patientsRef.child(date).child(n).setValue(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    String messageKey=PatientNameRef.push().getKey();
                    if (task.isSuccessful()) {
                        Toast.makeText(DoctorsDetails.this, "You Got an appointment ...", Toast.LENGTH_SHORT).show();

                        Calendar calfordate = Calendar.getInstance();
                        SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd,yyyy");
                        currentDate = currentDateFormat.format(calfordate.getTime());

                        Calendar calforTime = Calendar.getInstance();
                        SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
                        currentTime = currentTimeFormat.format(calforTime.getTime());


                        HashMap<String, Object> groupMessageKey = new HashMap<>();
                        PatientNameRef.updateChildren(groupMessageKey);

                        PatientMessageKeyRef = PatientNameRef.child(messageKey);

                        HashMap<String, Object> messageInfoMap = new HashMap<>();
                        messageInfoMap.put("name", patientsName.getText().toString());
                        messageInfoMap.put("message", "You get an appointment to "+docname+" at "+date+"\nContact us:"+phone);
                        messageInfoMap.put("date", currentDate);
                        messageInfoMap.put("time", currentTime);

                        PatientMessageKeyRef.updateChildren(messageInfoMap);
                        GotoMainActivityPage();
                    } else {
                        String message = task.getException().toString();
                        Toast.makeText(DoctorsDetails.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }


    private void GotoMainActivityPage() {
        Intent mainIntent= new Intent(DoctorsDetails.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

}
