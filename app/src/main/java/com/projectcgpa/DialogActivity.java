package com.projectcgpa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.projectcgpa.DatabaseOperation.SemesterOperation;
import com.projectcgpa.calculation.CgpaCalculator;
import com.projectcgpa.entities.Semester;
import com.projectcgpa.entities.User;

import java.util.List;
import java.util.Vector;

public class DialogActivity {
    private Context context;
    private EditText courseNameDialog, creditDialog, gpaDialog, saveSemester;
    private String mCourseName;
    private double mCredit, mGpa;
    private Button btn_add_dialog;
    private int itemPosition;
    private SemesterOperation semesterOperation;
    private static clickListenerForSemester clickListenerForSemester;

    public DialogActivity(Context context) {
        this.context = context;
        semesterOperation = new SemesterOperation(context);
    }



    //------------------------------DialogForSemesterActivity-----------------------------------//
    public void showDialogSaveSemester(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.addnewsem_dialog,null);
        builder.setView(view);
        saveSemester = view.findViewById(R.id.semNameET);
        CgpaCalculator cgpaCalculator = new CgpaCalculator();
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

    //------------------------------DialogForCourseActivity-----------------------------------//




    public interface clickListenerForSemester {
        void saveNewSemester(Semester semester);
        void refreshListAfterDeleteSemester(Semester semester, int position);
    }


    //setListener
    public static  void setclickListenerForSemester(clickListenerForSemester clickListener){
        clickListenerForSemester = clickListener;
    }
}

