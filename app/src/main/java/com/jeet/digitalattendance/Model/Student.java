package com.jeet.digitalattendance.Model;

public class Student {
    private String name;
    private String student_id;
    private String date_of_birth;
    private String section;
    private String batch;

    public Student() {
    }

    public Student(String name, String student_id, String date_of_birth, String section, String batch) {
        this.name = name;
        this.student_id = student_id;
        this.date_of_birth = date_of_birth;
        this.section = section;
        this.batch = batch;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }
}
