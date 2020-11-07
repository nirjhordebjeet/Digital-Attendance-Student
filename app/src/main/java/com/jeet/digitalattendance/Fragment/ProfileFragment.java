package com.jeet.digitalattendance.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jeet.digitalattendance.Common.Common;
import com.jeet.digitalattendance.R;

public class ProfileFragment extends Fragment {

    private View view;

    private TextView name_tv, student_id_tv, birth_date_tv, batch_tv, section_tv;

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment, container, false);

        name_tv = (TextView) view.findViewById(R.id.name_tv);
        student_id_tv = (TextView) view.findViewById(R.id.student_id_tv);
        birth_date_tv = (TextView) view.findViewById(R.id.birth_date_tv);
        batch_tv = (TextView) view.findViewById(R.id.batch_tv);
        section_tv = (TextView) view.findViewById(R.id.section_tv);

        if (Common.currentStudent != null) {
            if (Common.currentStudent.getName() != null) {
                name_tv.setText(Common.currentStudent.getName());
            }
            if (Common.currentStudent.getStudent_id() != null) {
                student_id_tv.setText(Common.currentStudent.getStudent_id());
            }
            if (Common.currentStudent.getDate_of_birth() != null) {
                birth_date_tv.setText(Common.currentStudent.getDate_of_birth());
            }
            if (Common.currentStudent.getBatch() != null) {
                batch_tv.setText(Common.currentStudent.getBatch());
            }
            if (Common.currentStudent.getSection() != null) {
                section_tv.setText(Common.currentStudent.getSection());
            }
        }

        return view;
    }
}
