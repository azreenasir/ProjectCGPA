package com.projectcgpa.entities;

import java.io.Serializable;

public class User implements Serializable {

    private static long Student_Id;
    private String fullname;
    private String email;
    private String password;

    public User() {
    }

    public static long getStudent_Id() {
        return Student_Id;
    }

    public long getStudent_id() {
        return Student_Id;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setStudent_Id(long student_Id) {
        Student_Id = student_Id;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
