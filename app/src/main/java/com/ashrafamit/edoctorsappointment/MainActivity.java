package com.ashrafamit.edoctorsappointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.mbms.MbmsErrors;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    private Toolbar mToolbar;
    ActionBarDrawerToggle toggle;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference ref;
    public String UserStatus,UserName,category,currentUserId,currentUserGmail;

    private TextView patientName,doctorName,patientEmail,doctorEmail;
    private Button takeAppointment,viewAppoitment,patientlist,scheduleSetup,docBMI,docAGE,patBMI,patAGE;
    private ConstraintLayout pat,doc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Initialization();

        mAuth=FirebaseAuth.getInstance();
        ref= FirebaseDatabase.getInstance().getReference();
        currentUser=mAuth.getCurrentUser();

        mToolbar=(Toolbar)findViewById(R.id.main_activity_toolbar);
        drawerLayout= findViewById(R.id.drawer);
        navigationView=findViewById(R.id.navigationView);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("e-Doctor's Appointment");
        toggle=new ActionBarDrawerToggle(this,drawerLayout,mToolbar,R.string.drawerOpen,R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void Initialization() {


        pat=(ConstraintLayout)findViewById(R.id.patient);
        doc=(ConstraintLayout)findViewById(R.id.doctor);

        takeAppointment=(Button)findViewById(R.id.btnDoctorsList);
        viewAppoitment=(Button)findViewById(R.id.btnAppointedDoctor);
        patientName=(TextView)findViewById(R.id.tvPatientName);
        patAGE=(Button)findViewById(R.id.btnPatientAge);
        patBMI=(Button)findViewById(R.id.btnPatientBmi);
        docAGE=(Button)findViewById(R.id.btnDoctorAge);
        docBMI=(Button)findViewById(R.id.btnDoctorBmi);
        patientEmail=(TextView)findViewById(R.id.tvPatientEmail);
        doctorEmail=(TextView)findViewById(R.id.tvDoctorEmail);


        patientlist=(Button)findViewById(R.id.btnpatientsList);
        scheduleSetup=(Button)findViewById(R.id.btnUpdateSchedule);
        doctorName=(TextView)findViewById(R.id.tvdoctorName);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (currentUser==null){
            LoginActivity();
        }else {
            VerifyUserExistance();
        }
    }

    public void VerifyUserExistance() {
        currentUserId=mAuth.getCurrentUser().getUid();
        currentUserGmail=mAuth.getCurrentUser().getEmail();
        ref.child("User").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("name").exists()){
                    UserStatus=dataSnapshot.child("status").getValue().toString();
                    UserName=dataSnapshot.child("name").getValue().toString();
                    patientName.setText(UserName);
                    patientEmail.setText(currentUserGmail);
                    doctorEmail.setText(currentUserGmail);
                    doctorName.setText(UserName);
                    Toast.makeText(MainActivity.this,"Welcome",Toast.LENGTH_SHORT).show();
                    if(UserStatus.equals("Doctor")){
                        pat.setVisibility(View.INVISIBLE);
                        category=dataSnapshot.child("category").getValue().toString();
                        patientlist.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                gotoPatientListPage();
                            }
                        });
                        scheduleSetup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                gotoScheduleSetupPage();
                            }
                        });
                        docAGE.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                gotoAgecalculatePage();
                            }
                        });
                        docBMI.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                gotoBMIcalculatePage();
                            }
                        });
                    }else{
                        doc.setVisibility(View.INVISIBLE);
                        takeAppointment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                gotoDoctorsCategoryPage();
                            }
                        });
                        viewAppoitment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                gotoAppointedDoctorsListPage();
                            }
                        });
                        patAGE.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                gotoAgecalculatePage();
                            }
                        });
                        patBMI.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                gotoBMIcalculatePage();
                            }
                        });
                    }
                }else{
                    SendUserToSettingsActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void gotoAppointedDoctorsListPage() {
        Intent loginIntent= new Intent(MainActivity.this,AppointedDoctorList.class);
        loginIntent.putExtra("uid",currentUserId);
        //loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        //finish();
    }

    private void gotoDoctorsCategoryPage() {
        Intent loginIntent= new Intent(MainActivity.this,DoctorsCategoryListView.class);
        //loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        //finish();
    }

    private void gotoScheduleSetupPage() {
        Intent loginIntent= new Intent(MainActivity.this,WorkingDaysActivity.class);
        loginIntent.putExtra("category",category);
        loginIntent.putExtra("uid",currentUserId);
        //loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        //finish();
    }

    private void gotoPatientListPage() {
        Intent loginIntent= new Intent(MainActivity.this,PatientList.class);
        loginIntent.putExtra("category",category);
        loginIntent.putExtra("uid",currentUserId);
        //loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        //finish();
    }

    private void LoginActivity() {
        Intent loginIntent= new Intent(MainActivity.this,LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
    private void SendUserToSettingsActivity() {
        Intent settingsIntent= new Intent(MainActivity.this,SettingsActivity.class);
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(settingsIntent);
        finish();
    }
    private void gotoUpdateInformationPage() {
        Intent upIntent= new Intent(MainActivity.this,update_informations.class);
        upIntent.putExtra("uid",currentUserId);
        startActivity(upIntent);
    }
    private void gotoPatientUpdatePage() {
        Intent upIntent= new Intent(MainActivity.this,update_Patient_informations.class);
        upIntent.putExtra("uid",currentUserId);
        startActivity(upIntent);
    }
    private void gotoBMIcalculatePage(){
        Intent bmiIntent= new Intent(MainActivity.this,BMIcalculatePage.class);
        startActivity(bmiIntent);
    }
    private void gotoAgecalculatePage(){
        Intent ageIntent= new Intent(MainActivity.this,AGEcalculatePage.class);
        startActivity(ageIntent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.main_logout_option){
            mAuth.signOut();
            LoginActivity();
        }
        if(menuItem.getItemId() == R.id.bmi_cal){
           gotoBMIcalculatePage();
        }
        if(menuItem.getItemId() == R.id.age_cal){
            gotoAgecalculatePage();
        }
        if(menuItem.getItemId() == R.id.main_settings_option){
            if(UserStatus.equals("Doctor")){
                gotoUpdateInformationPage();
            }else{
                gotoPatientUpdatePage();
            }

        }
        return false;
    }
}
