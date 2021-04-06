package com.ashrafamit.edoctorsappointment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class DoctorsCategoryListView extends AppCompatActivity {

    private ListView listView;
    ArrayAdapter<String> adapter;
    public String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_category_list_view);

        innitialization();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent(DoctorsCategoryListView.this,doctorsList.class);
                category=listView.getItemAtPosition(position).toString();
                intent.putExtra("category",category);
                Toast.makeText(DoctorsCategoryListView.this, listView.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }

    private void innitialization() {
        listView=(ListView)findViewById(R.id.doctorsCategoryList);

        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.doctorsCategories));
        listView.setAdapter(adapter);
    }
}
