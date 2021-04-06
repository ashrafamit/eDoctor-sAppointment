package com.ashrafamit.edoctorsappointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PatientDetails extends AppCompatActivity {
    private ListView patientsDetails;
    public String currentUserId,category,date,patName;
    private DatabaseReference ref;


    ArrayList<String> list;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);

        Initialization();

        retriveData();

    }

    private void retriveData() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name=dataSnapshot.child("name").getValue().toString();
                String age=dataSnapshot.child("age").getValue().toString();
                String phone=dataSnapshot.child("phone").getValue().toString();
                String gender=dataSnapshot.child("gender").getValue().toString();
                String history=dataSnapshot.child("history").getValue().toString();
                list.add("Name : "+name+"\nAge : "+age+"\nGender : "+gender+"\nHistory : "+history+"\nPhone : "+phone);

                patientsDetails.setAdapter(adapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Initialization() {
        patientsDetails=(ListView)findViewById(R.id.listView_PatientDetails);

        category=getIntent().getExtras().getString("category");
        currentUserId=getIntent().getExtras().getString("uid");
        date=getIntent().getExtras().getString("date");
        patName=getIntent().getExtras().getString("patName");

        ref= FirebaseDatabase.getInstance().getReference().child("Doctor").child(category).child(currentUserId).child("WorkingDay").child(date).child(patName);
        list=new ArrayList<>();
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);

    }
}
