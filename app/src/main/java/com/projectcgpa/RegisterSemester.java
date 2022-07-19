package com.projectcgpa;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.projectcgpa.Adapter.SemesterAdapter;
import com.projectcgpa.DatabaseOperation.CourseOperation;
import com.projectcgpa.DatabaseOperation.DBHelper;
import com.projectcgpa.DatabaseOperation.SemesterOperation;
import com.projectcgpa.calculation.CgpaCalculator;
import com.projectcgpa.entities.Course;
import com.projectcgpa.entities.Semester;
import com.projectcgpa.entities.User;

import java.util.ArrayList;
import java.util.List;

public class RegisterSemester extends AppCompatActivity implements DialogActivity.clickListenerForSemester {
    DrawerLayout drawerLayout;
    DBHelper dbHelper;
    DialogActivity dialogActivity;
    List<Semester> semesterList;
    List<Course> courseList;
    TextView cgpa_header;
    SemesterOperation semesterOperation;
    CourseOperation courseOperation;
    CgpaCalculator cgpaCalculator;
    RecyclerView recyclerView_semester_list;
    LinearLayoutManager linearLayoutManager;
    SemesterAdapter semesterAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_semester);

        getSupportActionBar().hide();
        drawerLayout = findViewById(R.id.drawer_layout);

        dbHelper = new DBHelper(this);

        TextView fullnameTV = (TextView) findViewById(R.id.usernameTV);
        TextView studentIdTV = (TextView) findViewById(R.id.studentIdTV);
        TextView clickSem = (TextView) findViewById(R.id.clickSemTV);
        TextView AddNewSem = (TextView) findViewById(R.id.btnAddsem);
        TextView clickHome = (TextView) findViewById(R.id.clickHome);

        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");

        fullnameTV.setText(user.getFullname());
        long id = user.getStudent_Id();
        String studId = Long.toString(id);
        studentIdTV.setText(studId);

        initialize_view();
        if(semesterList.isEmpty())
        {
            Toast.makeText(this, "Empty List", Toast.LENGTH_LONG).show();
        }

        // navigation drawer method
        clickSem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });

        clickHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterSemester.this,MainActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });
    }

    private void initialize_view() {
        semesterList = new ArrayList<>();
        cgpa_header = findViewById(R.id.cgpaResultTV);
        semesterOperation = new SemesterOperation(this);
        cgpaCalculator = new CgpaCalculator();
        //String cgpa_total = cgpaCalculator.calculatedCGPA(courseList);
        //cgpa_header.setText(cgpa_total);
        semesterList = semesterOperation.getAllSemester();
        recyclerView_semester_list = findViewById(R.id.recyclerview_semester);
        linearLayoutManager = new LinearLayoutManager(RegisterSemester.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_semester_list.setLayoutManager(linearLayoutManager);
        recyclerView_semester_list.setHasFixedSize(true);
        semesterAdapter = new SemesterAdapter(RegisterSemester.this, semesterList);
        recyclerView_semester_list.setAdapter(semesterAdapter);
        dialogActivity = new DialogActivity(RegisterSemester.this);
        DialogActivity.setclickListenerForSemester(this);
    }

    public void addNewSemester(View view) {
        dialogActivity.showDialogSaveSemester();
    }


    public void clickMenu(View view) {
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

    @Override
    public void saveNewSemester(Semester semester) {
        semesterList.add(semester);
        semesterAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshListAfterDeleteSemester(Semester semester, int position) {
        semesterList.remove(semester);
        semesterAdapter.notifyItemRemoved(position);
        courseList.clear();
        courseList = courseOperation.getAllCourses();
        String cgpa_total = cgpaCalculator.calculatedCGPA(courseList);
        cgpa_header.setText(cgpa_total);
        Toast.makeText(this,"Delete",Toast.LENGTH_SHORT).show();
        Log.d("Anik", "Data deleted");
    }
}