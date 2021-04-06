package com.ashrafamit.edoctorsappointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PatientList extends AppCompatActivity {
    private Toolbar mToolbar;
    private ListView viewDateList;
    public String currentUserId,category,date;

    private DatabaseReference ref;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_groups=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        Initialization();
        mToolbar=(Toolbar)findViewById(R.id.patientsList_activity_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Working Days");

        retriveDateView();

        viewDateList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                date=parent.getItemAtPosition(position).toString();
                GotoPatientListActivity();
            }
        });
    }


    private void Initialization() {
        viewDateList=(ListView)findViewById(R.id.list_view_PatientsListDate);
        category=getIntent().getExtras().getString("category");
        currentUserId=getIntent().getExtras().getString("uid");

        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_of_groups);
        viewDateList.setAdapter(arrayAdapter);

        ref= FirebaseDatabase.getInstance().getReference().child("Doctor").child(category).child(currentUserId).child("WorkingDay");
    }


    private void retriveDateView() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Set<String> set=new HashSet<>();
                Iterator iterator=dataSnapshot.getChildren().iterator();

                while (iterator.hasNext()){

                    set.add(((DataSnapshot)iterator.next()).getKey());
                }

                list_of_groups.addAll(set);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void GotoPatientListActivity() {
        Intent Intent= new Intent(PatientList.this,PatientListView.class);
        Intent.putExtra("category",category);
        Intent.putExtra("uid",currentUserId);
        Intent.putExtra("date",date);
        startActivity(Intent);
    }

}