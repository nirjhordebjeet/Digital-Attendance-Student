package com.jeet.digitalattendance.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.jeet.digitalattendance.Common.Common;
import com.jeet.digitalattendance.Database.FirebaseTable;
import com.jeet.digitalattendance.Database.MySharedPreferences;
import com.jeet.digitalattendance.Fragment.ProfileFragment;
import com.jeet.digitalattendance.Model.Student;
import com.jeet.digitalattendance.Model.Token;
import com.jeet.digitalattendance.R;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigationView;
    private Fragment fragment;

    private DatabaseReference TOKEN_TABLE, STUDENT_TABLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        STUDENT_TABLE = FirebaseDatabase.getInstance().getReference(FirebaseTable.STUDENT_TABLE);
        TOKEN_TABLE = FirebaseDatabase.getInstance().getReference(FirebaseTable.TOKEN_TABLE);

        navigationView = (BottomNavigationView) findViewById(R.id.navigationView);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                /*if (menuItem.getItemId() == R.id.result) {
                    fragment = new ResultFragment();
                    setFragment(fragment);
                    setTitle("Result");
                }
                if (menuItem.getItemId() == R.id.routine) {
                    fragment = new RoutineFragment();
                    setFragment(fragment);
                    setTitle("Routine");
                }*/
                if (menuItem.getItemId() == R.id.profile) {
                    fragment = new ProfileFragment();
                    setFragment(fragment);
                    setTitle("Profile");
                }
                return true;
            }
        });

        setFragment(new ProfileFragment());
        setTitle("Profile");

        refreshedToken(MySharedPreferences.readUid(this));

        if (Common.currentStudent == null) {
            getReadData(MySharedPreferences.readUid(this));
        }
    }

    private void getReadData(String readUid) {
        STUDENT_TABLE.child(readUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Common.currentStudent = dataSnapshot.getValue(Student.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        String Sid = MySharedPreferences.readUid(this);
        if (Sid == null) {
            startActivity(new Intent(MainActivity.this, SplashActivity.class));
            finish();
        }
        super.onStart();
    }

    private void refreshedToken(final String readUid) {
        if (readUid != null) {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (task.isSuccessful()) {
                                Token token = new Token(task.getResult().getToken());
                                TOKEN_TABLE.child(readUid).setValue(token);
                            }
                        }
                    });
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
