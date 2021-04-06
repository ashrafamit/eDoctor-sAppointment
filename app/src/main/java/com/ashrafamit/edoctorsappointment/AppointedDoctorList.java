package com.ashrafamit.edoctorsappointment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AppointedDoctorList extends AppCompatActivity {
    private Toolbar mToolbar;
    private ListView mScrollView;
    private TextView mtextView;
    private DatabaseReference ref;
    public String currentUserId;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_groups=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointed_doctor_list);

        mToolbar=(Toolbar)findViewById(R.id.appointedDoctor_activity_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Notifications");

        initializations();
    }


    @Override
    protected void onStart() {
        super.onStart();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Iterator iterator=dataSnapshot.getChildren().iterator();
                Set<String> set=new HashSet<>();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    String Chatdate=ds.child("date").getValue().toString();
                    String Chatmessage=ds.child("message").getValue().toString();
                    String Chatname=ds.child("name").getValue().toString();
                    String Chattime=ds.child("time").getValue().toString();
                    set.add(Chatname+":\n\n"+Chatmessage+"\n\n"+Chattime+"      "+Chatdate);
                    //mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
                list_of_groups.addAll(set);
                mScrollView.setAdapter(arrayAdapter);            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void initializations() {
        mScrollView=(ListView) findViewById(R.id.my_scroll_view);
        //mtextView=(TextView)findViewById(R.id.notification_Display);

        currentUserId=getIntent().getExtras().getString("uid");

        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_of_groups);


        ref= FirebaseDatabase.getInstance().getReference().child("User").child(currentUserId).child("notifications");

    }
}
