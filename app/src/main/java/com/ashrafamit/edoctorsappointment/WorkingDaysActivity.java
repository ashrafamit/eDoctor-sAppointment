package com.ashrafamit.edoctorsappointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class WorkingDaysActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private Toolbar mToolbar;
    private TextView viewDate;
    private ListView viewDateList;
    private Button selectDate,uploadDate;

    private DatabaseReference ref,reff;

    private ArrayAdapter<String>arrayAdapter;
    private ArrayList<String> list_of_groups=new ArrayList<>();

    public String currentUserId,category,date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working_days);

        Initialization();
        mToolbar=(Toolbar)findViewById(R.id.workingdays_activity_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Working Days");

        ref= FirebaseDatabase.getInstance().getReference();
        reff=FirebaseDatabase.getInstance().getReference().child("Doctor").child(category).child(currentUserId).child("WorkingDay");

        RetriveAndDisplayGroups();

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        viewDateList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currentGroutName=parent.getItemAtPosition(position).toString();
            }
        });
    }

    private void Initialization() {
        viewDate=(TextView)findViewById(R.id.tvdateView);
        viewDateList=(ListView)findViewById(R.id.list_view_Date);
        selectDate=(Button)findViewById(R.id.btnselectdate);
        uploadDate=(Button)findViewById(R.id.btnuploaddate);

        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_of_groups);
        viewDateList.setAdapter(arrayAdapter);

        category=getIntent().getExtras().getString("category");
        currentUserId=getIntent().getExtras().getString("uid");
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c=Calendar.getInstance();

        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        date = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        viewDate.setText(date);


        uploadDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child("Doctor").child(category).child(currentUserId).child("WorkingDay").child(date).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(WorkingDaysActivity.this,date+" is Uploaded",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(WorkingDaysActivity.this,date+" is not Uploaded",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void RetriveAndDisplayGroups() {
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Set<String> set=new HashSet<>();
                Iterator iterator=dataSnapshot.getChildren().iterator();

               while (iterator.hasNext()){

                    set.add(((DataSnapshot)iterator.next()).getKey());
                }

                list_of_groups.clear();
                list_of_groups.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
