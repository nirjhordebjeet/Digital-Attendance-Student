package com.jeet.digitalattendance.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jeet.digitalattendance.Common.Common;
import com.jeet.digitalattendance.Database.FirebaseTable;
import com.jeet.digitalattendance.Database.MySharedPreferences;
import com.jeet.digitalattendance.Model.Student;
import com.jeet.digitalattendance.R;

public class SplashActivity extends AppCompatActivity {

    private DatabaseReference student_table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        student_table = FirebaseDatabase.getInstance().getReference(FirebaseTable.STUDENT_TABLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                autoStart();
            }
        }, 2000);
    }

    private void autoStart() {
        String uid = MySharedPreferences.readUid(SplashActivity.this);
        if (uid == null) {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        } else {
            readInfo(uid);
        }
    }

    private void readInfo(String uid) {
        student_table.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Common.currentStudent = dataSnapshot.getValue(Student.class);
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SplashActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
