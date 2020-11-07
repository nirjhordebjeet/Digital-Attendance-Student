package com.jeet.digitalattendance.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jeet.digitalattendance.Common.Common;
import com.jeet.digitalattendance.Database.FirebaseTable;
import com.jeet.digitalattendance.Database.MySharedPreferences;
import com.jeet.digitalattendance.Model.Attendance;
import com.jeet.digitalattendance.Model.Student;
import com.jeet.digitalattendance.R;

public class AttendanceActivity extends AppCompatActivity {

    private TextView timer_tv;
    private EditText edit_code;
    private Button done_btn, verified_btn;

    public String getVerification;
    private DatabaseReference STUDENT_ATTENDANCE_TABLE, STUDENT_TABLE;

    private String code=null, teacher_id, teacher_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);




        if (getIntent() != null) {

            code = getIntent().getStringExtra("code");
            teacher_id = getIntent().getStringExtra("teacher_id");
            teacher_name = getIntent().getStringExtra("teacher_name");
        }

        STUDENT_ATTENDANCE_TABLE = FirebaseDatabase.getInstance().getReference(FirebaseTable.STUDENT_ATTENDANCE_TABLE);
        STUDENT_TABLE = FirebaseDatabase.getInstance().getReference(FirebaseTable.STUDENT_TABLE);

        if (Common.currentStudent == null) {
            readStudentDetails(MySharedPreferences.readUid(AttendanceActivity.this));
        }

        timer_tv = (TextView) findViewById(R.id.timer_tv);
        edit_code = (EditText) findViewById(R.id.edit_code);
        done_btn = (Button) findViewById(R.id.done_btn);
        verified_btn = findViewById(R.id.verification);


        verified_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(AttendanceActivity.this, FingerScanner.class);
                startActivity(i);

            }
        });


        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                attendances();


            }
        });

        startTimer();
    }

    private void readStudentDetails(String uid) {
        STUDENT_TABLE.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Common.currentStudent = dataSnapshot.getValue(Student.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AttendanceActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void attendances() {
        String token = edit_code.getText().toString().trim();
//        Toast.makeText(this, Common.currentCode+"         Token=     "+token, Toast.LENGTH_SHORT).show();
        if (Common.currentCode== null){
            Toast.makeText(this, "Received Token Not Found", Toast.LENGTH_SHORT).show();
            return;
        }
        if( !Common.currentCode.equals(token)){
            Toast.makeText(this, "Token Number not same", Toast.LENGTH_SHORT).show();
            return;
        }

        Attendance attendance = new Attendance(Common.currentStudent.getStudent_id(), Common.currentStudent.getName(), Common.currentCode,Common.currentTeacherId, Common.currentTeacherName, String.valueOf(System.currentTimeMillis()));
            STUDENT_ATTENDANCE_TABLE.child(Common.currentStudent.getBatch())
                    .child(Common.currentStudent.getSection())
                    .push().setValue(attendance).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(), "Attendance done", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(AttendanceActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            });

//        if (!token.equals(code)) {
////            Toast.makeText(this, "Token Number not same", Toast.LENGTH_SHORT).show();
////        } else {
////            Attendance attendance = new Attendance(Common.currentStudent.getStudent_id(), Common.currentStudent.getName(), code, teacher_id, teacher_name, String.valueOf(System.currentTimeMillis()));
////            STUDENT_ATTENDANCE_TABLE.child(Common.currentStudent.getBatch())
////                    .child(Common.currentStudent.getSection())
////                    .push().setValue(attendance).addOnCompleteListener(new OnCompleteListener<Void>() {
////                @Override
////                public void onComplete(@NonNull Task<Void> task) {
////                    finish();
////                }
////            });
////
////        }
    }

    private void startTimer() {
        new CountDownTimer(120000, 1000) {
            @Override
            public void onTick(long l) {
                timer_tv.setText(String.valueOf(l / 1000));
                int seconds = (int) (l / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                timer_tv.setText("TIME : " + String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
            }

            @Override
            public void onFinish() {
                finish();
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onPause() {
        finish();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }
}
