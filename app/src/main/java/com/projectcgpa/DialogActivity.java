package com.projectcgpa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.projectcgpa.DatabaseOperation.CourseOperation;
import com.projectcgpa.DatabaseOperation.SemesterOperation;
import com.projectcgpa.calculation.CgpaCalculator;
import com.projectcgpa.entities.Course;
import com.projectcgpa.entities.Semester;
import com.projectcgpa.entities.User;

import java.util.List;

public class DialogActivity {
    private Context context;
    private EditText courseNameDialog, creditDialog, gpaDialog, saveSemester;
    private String mCourseName, credit, course, semestergpa;
    private double mCredit, mGpa;
    private Button btn_add_dialog;
    private int itemPosition;
    private SemesterOperation semesterOperation;
    private CourseOperation courseOperation;
    private static clickListenerForSemester clickListenerForSemester;
    private static ClickListenerForCourse clickListenerForCourse;
    private CgpaCalculator cgpaCalculator;

    public DialogActivity(Context context) {
        this.context = context;
        semesterOperation = new SemesterOperation(context);
        courseOperation = new CourseOperation(context);
    }



    //------------------------------DialogForSemesterActivity-----------------------------------//
    public void showDialogSaveSemester(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.addnewsem_dialog,null);
        builder.setView(view);
        saveSemester = view.findViewById(R.id.semNameET);
        cgpaCalculator = new CgpaCalculator();
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String semester_name = saveSemester.getText().toString();
                if(!semester_name.isEmpty()){
                    long user = User.getStudent_Id();
                    Semester newSemester = semesterOperation.createSemester(semester_name,0.00,0,0.00, Long.toString(user));
                    clickListenerForSemester.saveNewSemester(newSemester);
                }
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showDialogDeleteSemester(final Semester semester, final int itemPosition){
        String semester_name = semester.getSemester_name();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_baseline_delete_24);
        builder.setTitle("Delete");
        builder.setMessage("Do you want to delete "+semester_name+"?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                semesterOperation.deleteSemester(semester);
                clickListenerForSemester.refreshListAfterDeleteSemester(semester, itemPosition);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showDialogUpdateForSemester(List<Semester> semesterList, final int itemPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.addnewsem_dialog,null);
        builder.setView(view);

        int total_course = semesterList.get(itemPosition).getTotal_course();
        Double total_credit = semesterList.get(itemPosition).getTotal_credit();
        Double total_gpa = semesterList.get(itemPosition).getTotal_gpa();

        saveSemester = view.findViewById(R.id.semNameET);
        saveSemester.setText(semesterList.get(itemPosition).getSemester_name());
        final Semester semester = semesterList.get(itemPosition);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String semester_name = saveSemester.getText().toString();
                if(!semester_name.isEmpty()){
                    semesterOperation.updateSemesterName(semester,semester_name);
                    clickListenerForSemester.updateListForSemester(itemPosition, semester_name, total_gpa, total_course, total_credit);
                }
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //------------------------------DialogForCourseActivity-----------------------------------//

    public void showDialogUpdateDeleteForCourse(List<Course> courseList, int position) {
        itemPosition =  position;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.addcourse_layout, null);
        builder.setView(view);

        courseNameDialog = view.findViewById(R.id.autoCompleteTextViewDialog);
        creditDialog = view.findViewById(R.id.creditDialog);
        gpaDialog = view.findViewById(R.id.gpaDialog);
        courseNameDialog.setText(courseList.get(position).getCourse_name());
        creditDialog.setText(String.valueOf(courseList.get(position).getCourse_credit()));
        gpaDialog.setText(String.valueOf(courseList.get(position).getObtain_gpa()));
        final Course course = courseList.get(position);

        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_baseline_mode_24);
        builder.setTitle("Update or Delete");
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCourseName = courseNameDialog.getText().toString();
                mCredit = Double.parseDouble(creditDialog.getText().toString());
                mGpa = Double.parseDouble(gpaDialog.getText().toString());
                courseOperation.updateCourse(course, mCourseName, mCredit, mGpa);
                clickListenerForCourse.updateList(itemPosition, mCourseName, mCredit, mGpa);
            }
        });
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                courseOperation.deleteCourse(course);
                clickListenerForCourse.removeItem(itemPosition);
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showDialogForAddNewSemesterCourse(final Long mId) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.addnewcourse, null);
        builder.setView(view);
        courseNameDialog = view.findViewById(R.id.dialog_autoCompleteTextView_courseName);
        creditDialog = view.findViewById(R.id.dialog_credit);
        gpaDialog = view.findViewById(R.id.dialog_gpa);
        btn_add_dialog = view.findViewById(R.id.dialog_btnAdd);

        builder.setTitle("Add New Course");
        builder.setIcon(R.drawable.ic_baseline_mode_24);
        final  AlertDialog dialog = builder.create();
        dialog.show();
        btn_add_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String course_name = courseNameDialog.getText().toString();
                if(course_name.isEmpty())
                    course_name = "Course Name?";
                try {
                    double credit = Double.parseDouble(creditDialog.getText().toString());
                    double gpa = Double.parseDouble(gpaDialog.getText().toString());
                    Course course = courseOperation.createCourse(course_name, credit, gpa, mId);
                    dialog.dismiss();
                    clickListenerForCourse.addNewCourse(course);
                } catch (NumberFormatException e) {
                    Toast.makeText(context,"Please fill all the fields carefully",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public interface clickListenerForSemester {
        void saveNewSemester(Semester semester);
        void refreshListAfterDeleteSemester(Semester semester, int position);
        void updateListForSemester(int itemPosition, String semester_name, Double total_gpa, int total_course, Double total_credit);
    }

    public interface ClickListenerForCourse {
        void addNewCourse(Course course);
        void updateList(int position, String courseName, double credit, double gpa);
        void removeItem(int position);
    }


    //setListener
    public static  void setclickListenerForSemester(clickListenerForSemester clickListener){
        clickListenerForSemester = clickListener;
    }

    public static  void setClickListenerForCourse(ClickListenerForCourse clickListener){
        clickListenerForCourse = clickListener;
    }
}

