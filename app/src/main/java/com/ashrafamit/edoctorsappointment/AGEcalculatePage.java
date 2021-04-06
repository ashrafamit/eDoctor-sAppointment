package com.ashrafamit.edoctorsappointment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AGEcalculatePage extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView today,birthday,result;
    private Button calculate;
    DatePickerDialog.OnDateSetListener dateSetListerner1,dateSetListener2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agecalculate_page);

        mToolbar=(Toolbar)findViewById(R.id.age_activity_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Age Calculator");
        innitializations();


        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                final int year=calendar.get(Calendar.YEAR);
                final int month=calendar.get(Calendar.MONTH);
                final int day=calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog =new DatePickerDialog(AGEcalculatePage.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListerner1,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();

            }
        });
        dateSetListerner1=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                String date= dayOfMonth+"/"+month+"/"+year;
                today.setText(date);
            }
        };


        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                final int year=calendar.get(Calendar.YEAR);
                final int month=calendar.get(Calendar.MONTH);
                final int day=calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog =new DatePickerDialog(AGEcalculatePage.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener2,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        dateSetListener2=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                String date= dayOfMonth+"/"+month+"/"+year;
                birthday.setText(date);
            }
        };

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sDate=birthday.getText().toString();
                String eDate=today.getText().toString();

                if(sDate.equals("Your Birthday")){
                    Toast.makeText(AGEcalculatePage.this,"Please, Set Your Birthday..",Toast.LENGTH_SHORT).show();
                }else{
                    SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("dd/MM/yyyy");

                    try {
                        Date date1=simpleDateFormat1.parse(sDate);
                        Date date2=simpleDateFormat1.parse(eDate);

                        long startDate=date1.getTime();
                        long endDate=date2.getTime();

                        if(startDate<=endDate){
                            Period period=new Period(startDate,endDate, PeriodType.yearMonthDay());

                            int year=period.getYears();
                            int month=period.getMonths();
                            int day=period.getDays();

                            result.setText("Your Age : "+year+" Years |"+month+" Months |"+day+" Days");
                        }else{
                            Toast.makeText(getApplicationContext(),"Birthdate should not be larger than today's date...",Toast.LENGTH_SHORT).show();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void innitializations() {
        today=(TextView)findViewById(R.id.tvTodaysDate);
        birthday=(TextView)findViewById(R.id.tvBirthday);
        result=(TextView)findViewById(R.id.tvResult);
        calculate=(Button)findViewById(R.id.btnCalculate);

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
        String date=simpleDateFormat.format(Calendar.getInstance().getTime());

        today.setText(date);
    }
}
