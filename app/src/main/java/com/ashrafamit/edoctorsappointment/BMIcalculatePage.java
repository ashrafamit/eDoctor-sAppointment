package com.ashrafamit.edoctorsappointment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class BMIcalculatePage extends AppCompatActivity {
    private Toolbar mToolbar;
    private EditText height,weight;
    private Button btnCal;
    private TextView result,status;
    public float bmi,h,w;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmicalculate_page);

        mToolbar=(Toolbar)findViewById(R.id.BMI_activity_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("BMI Calculator");
        
        innitializatios();

        btnCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h=Float.parseFloat(height.getText().toString())/100;
                w=Float.parseFloat(weight.getText().toString());
                bmi=w/(h*h);
                status.setText("Status : " +interpreteBMI(bmi));
                result.setText("Your BMI : "+bmi);
            }
        });

    }

    private void innitializatios() {
        height=(EditText)findViewById(R.id.etHeight);
        weight=(EditText)findViewById(R.id.etWeight);
        btnCal=(Button) findViewById(R.id.btnCalculateBMI);
        result=(TextView)findViewById(R.id.tvBMIresult);
        status=(TextView)findViewById(R.id.tvBMIStatus);
    }

    public String interpreteBMI(float bmiValue){
        if(bmiValue<16){
            return "Servely Underweight";
        }
        else if(bmiValue<18.5){
            return "Underweight";
        }
        else if(bmiValue<25){
            return "Normal";
        }
        else if(bmiValue<30){
            return "Over Weight";
        }
        else
            return "Obese";
    }
}
