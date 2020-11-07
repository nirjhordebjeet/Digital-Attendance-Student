package com.jeet.digitalattendance.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jeet.digitalattendance.Database.FirebaseTable;
import com.jeet.digitalattendance.Model.Auth;
import com.jeet.digitalattendance.Model.Ids;
import com.jeet.digitalattendance.Model.Student;
import com.jeet.digitalattendance.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText name_edit, student_id_edit, birth_date_edit, password_edit;
    private Button btn_register;

    private DatabaseReference AUTH_TABLE, STUDENT_TABLE, STUDENT_LIST_TABLE;

    private ProgressDialog progressDialog;

    private boolean isFirstSelect = true;
    private boolean isSecondSelect = true;

    private String batchString, sectionString;

    private Spinner batchSpinner, sectionSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        AUTH_TABLE = FirebaseDatabase.getInstance().getReference(FirebaseTable.AUTH_TABLE);
        STUDENT_TABLE = FirebaseDatabase.getInstance().getReference(FirebaseTable.STUDENT_TABLE);
        STUDENT_LIST_TABLE = FirebaseDatabase.getInstance().getReference(FirebaseTable.STUDENT_LIST_TABLE);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);

        name_edit = (EditText) findViewById(R.id.name_edit);
        student_id_edit = (EditText) findViewById(R.id.student_id_edit);
        birth_date_edit = (EditText) findViewById(R.id.birth_date_edit);
        password_edit = (EditText) findViewById(R.id.password_edit);

        batchSpinner = (Spinner) findViewById(R.id.batchSpinner);
        sectionSpinner = (Spinner) findViewById(R.id.sectionSpinner);

        batchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (isFirstSelect) {
                    isFirstSelect = false;
                } else {
                    batchString = adapterView.getItemAtPosition(i).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (isSecondSelect) {
                    isSecondSelect = false;
                } else {
                    sectionString = adapterView.getItemAtPosition(i).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_register = (Button) findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String full_name = name_edit.getText().toString().trim();
                String stn_id = student_id_edit.getText().toString().trim();
                String birthDate = birth_date_edit.getText().toString().trim();
                String password = password_edit.getText().toString().trim();

                if (stn_id.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Student is empty", Toast.LENGTH_SHORT).show();
                    return;
                } else if (birthDate.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Birth date is empty", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "password is empty", Toast.LENGTH_SHORT).show();
                    return;
                } else if (full_name.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Name is empty", Toast.LENGTH_SHORT).show();
                    return;
                } else if (batchString == null) {
                    Toast.makeText(RegisterActivity.this, "Select batch", Toast.LENGTH_SHORT).show();
                    return;
                } else if (sectionString == null) {
                    Toast.makeText(RegisterActivity.this, "Select section", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    tryToRegister(full_name, stn_id, birthDate, password, batchString, sectionString);
                }
            }
        });
    }

    private void tryToRegister(final String full_name, final String stn_id, final String birthDate, final String password, final String batchString, final String sectionString) {
        progressDialog.show();
        AUTH_TABLE.child(stn_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Student is already has an account", Toast.LENGTH_SHORT).show();
                } else {
                    Auth auth = new Auth(stn_id, password);
                    AUTH_TABLE.child(stn_id).setValue(auth)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Student student = new Student(full_name, stn_id, birthDate, sectionString, batchString);
                                        STUDENT_TABLE.child(stn_id).setValue(student)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Ids ids = new Ids(stn_id);
                                                        STUDENT_LIST_TABLE.child(batchString)
                                                                .child(sectionString).push().setValue(ids).isSuccessful();

                                                        progressDialog.dismiss();
                                                        Toast.makeText(RegisterActivity.this, "Register successful", Toast.LENGTH_SHORT).show();
                                                        onBackPressed();
                                                    }
                                                });
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }
}
