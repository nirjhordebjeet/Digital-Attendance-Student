package com.jeet.digitalattendance.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jeet.digitalattendance.Common.Common;
import com.jeet.digitalattendance.Database.FirebaseTable;
import com.jeet.digitalattendance.Database.MySharedPreferences;
import com.jeet.digitalattendance.Model.Auth;
import com.jeet.digitalattendance.Model.Student;
import com.jeet.digitalattendance.R;

public class LoginActivity extends AppCompatActivity {

    private DatabaseReference auth, student_table;
    private Button btn_login;
    private TextView registerTv;
    private EditText password_edit, student_id_edit;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        auth = FirebaseDatabase.getInstance().getReference(FirebaseTable.AUTH_TABLE);
        student_table = FirebaseDatabase.getInstance().getReference(FirebaseTable.STUDENT_TABLE);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);

        student_id_edit = (EditText) findViewById(R.id.student_id_edit);
        password_edit = (EditText) findViewById(R.id.password_edit);

        btn_login = (Button) findViewById(R.id.btn_login);

        registerTv = (TextView) findViewById(R.id.registerTv);

        registerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = student_id_edit.getText().toString().trim();
                String pass = password_edit.getText().toString().trim();

                if (id.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter your student id", Toast.LENGTH_SHORT).show();
                    return;
                } else if (pass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    tryToLogin(id, pass);
                }
            }
        });
    }

    private void tryToLogin(final String id, final String pass) {
        progressDialog.show();
        auth.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Auth auth = dataSnapshot.getValue(Auth.class);
                    if (auth.getPassword().equals(pass)) {
                        student_table.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    progressDialog.dismiss();
                                    MySharedPreferences.writeUid(LoginActivity.this, id);
                                    Common.currentStudent = dataSnapshot.getValue(Student.class);
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Student is not exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
