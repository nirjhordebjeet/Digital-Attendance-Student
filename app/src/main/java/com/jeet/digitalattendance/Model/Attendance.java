package com.jeet.digitalattendance.Model;

public class Attendance {
    private String student_id;
    private String student_name;
    private String code;
    private String teacher_id;
    private String teacher_name;
    private String time;

    public Attendance() {
    }

    public Attendance(String student_id, String student_name, String code, String teacher_id, String teacher_name, String time) {
        this.student_id = student_id;
        this.student_name = student_name;
        this.code = code;
        this.teacher_id = teacher_id;
        this.teacher_name = teacher_name;
        this.time = time;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
