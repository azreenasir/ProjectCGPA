package com.projectcgpa.entities;


public class Course {

    private long mId;
    private String course_name;
    private double course_credit;
    private double obtain_gpa;
    private double calculated_cgpa;
    private long semester_id;

    public Course() {

    }

    public Course(String course_name, double course_credit, double obtain_gpa) {
        this.course_name = course_name;
        this.course_credit = course_credit;
        this.obtain_gpa = obtain_gpa;
    }

    public long getmId() {
        return mId;
    }

    public void setmId(long mId) {
        this.mId = mId;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public double getCourse_credit() {
        return course_credit;
    }

    public void setCourse_credit(double course_credit) {
        this.course_credit = course_credit;
    }

    public double getObtain_gpa() {
        return obtain_gpa;
    }

    public void setObtain_gpa(double obtain_gpa) {
        this.obtain_gpa = obtain_gpa;
    }

    public long getSemester_id() {
        return semester_id;
    }

    public void setSemester_id(long semester_id) {
        this.semester_id = semester_id;
    }

    public double getCalculated_cgpa() {
        return calculated_cgpa;
    }

    public void setCalculated_cgpa(double calculated_cgpa) {
        this.calculated_cgpa = calculated_cgpa;
    }

    public String getGrade() {
        String points;
        if(getObtain_gpa() == 4.00 && getObtain_gpa()> 3.67 ){
            points = "A+/A";
        }
        else if (getObtain_gpa() == 3.67 || getObtain_gpa()> 3.33){
            points = "A-";
        }
        else if (getObtain_gpa() == 3.33 || getObtain_gpa() > 3.00 ){
            points = "B+";
        }
        else if (getObtain_gpa() == 3.00 || getObtain_gpa() > 2.67){
            points = "B";
        }
        else if (getObtain_gpa() == 2.67 || getObtain_gpa() > 2.33){
            points = "B-";
        }
        else if (getObtain_gpa() == 2.33 || getObtain_gpa() > 2.00){
            points = "C+";
        }
        else if (getObtain_gpa() == 2.00 || getObtain_gpa() > 1.67){
            points = "C";
        }
        else if (getObtain_gpa() == 1.67 || getObtain_gpa() > 1.33){
            points = "C-";
        }
        else if (getObtain_gpa() == 1.33 || getObtain_gpa() > 1.00){
            points = "D+";
        }
        else if (getObtain_gpa() == 1.00 || getObtain_gpa() > 0.67){
            points = "D";
        }
        else if (getObtain_gpa() == 0.67 || getObtain_gpa() > 0.01){
            points = "E";
        }
        else
            points = "F";

        return points;
    }
}
