package com.projectcgpa.calculation;

import com.projectcgpa.entities.Course;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Vector;

public class CgpaCalculator {

    private DecimalFormat precision = new DecimalFormat("0.00");

    public String calculatedCGPA(List<Course> courseList){
        double sum = 0;
        double total_credit = 0;
        double calculated_cgpa;
        for(int i=0; i<courseList.size(); i++){
            sum += courseList.get(i).getCourse_credit()*courseList.get(i).getObtain_gpa();
            total_credit += courseList.get(i).getCourse_credit();
        }
        calculated_cgpa = sum / total_credit;
        return courseList.isEmpty() ? "0.00":String.valueOf(precision.format(calculated_cgpa));
    }
    public Vector<Double> calculation(List<Course> courseList){
        Vector<Double> ret = new Vector<>();
        double sum = 0;
        double total_credit = 0;
        double calculated_cgpa;
        for(int i=0; i<courseList.size(); i++){
            sum += courseList.get(i).getCourse_credit()*courseList.get(i).getObtain_gpa();
            total_credit += courseList.get(i).getCourse_credit();
        }
        calculated_cgpa = sum / total_credit;
        ret.add(calculated_cgpa);
        ret.add(total_credit);
        return ret;
    }

    public String getDeanList(List<Course> courseList) {
        String result = null;
        double sum = 0;
        double total_credit = 0;
        double calculated_cgpa;
        for(int i=0; i<courseList.size(); i++){
            sum += courseList.get(i).getCourse_credit()*courseList.get(i).getObtain_gpa();
            total_credit += courseList.get(i).getCourse_credit();
        }
        calculated_cgpa = sum / total_credit;

        if (calculated_cgpa >= 3.50)
            result = "Dean List";
        else if (calculated_cgpa >= 2.00 || calculated_cgpa < 3.50 )
            result = "Pass";
        else if (calculated_cgpa < 2.00)
            result = "Failed";

        return  result;
    }


}
