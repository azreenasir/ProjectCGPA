package com.projectcgpa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.projectcgpa.Adapter.CourseAdapter;
import com.projectcgpa.DatabaseOperation.CourseOperation;
import com.projectcgpa.DatabaseOperation.DBHelper;
import com.projectcgpa.DatabaseOperation.SemesterOperation;
import com.projectcgpa.calculation.CgpaCalculator;
import com.projectcgpa.entities.Course;
import com.projectcgpa.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class CourseListActivity extends AppCompatActivity implements DialogActivity.ClickListenerForCourse {
    DrawerLayout drawerLayout;
    DBHelper dbHelper;
    TextView cgpa_display, result;
    List<Course> courseList;
    SemesterOperation semesterOperation;
    CourseOperation courseOperation;
    RecyclerView recyclerView_course_list;
    LinearLayoutManager linearLayoutManager;
    Long mId;
    CourseAdapter courseAdapter;
    CgpaCalculator cgpaCalculator;
    DialogActivity dialogActivity;
    Button btn_addCourse;
    CardView input_module;
    String cgpa_total;
    Vector<Double> values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        getSupportActionBar().hide();
        drawerLayout = findViewById(R.id.drawer_layout1);
        TextView clickSem = (TextView) findViewById(R.id.clickSemTV);
        TextView clickHome = (TextView) findViewById(R.id.clickHome);

        dbHelper = new DBHelper(this);

        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");

        mId = intent.getLongExtra("mId", -1);
        initialize();

        // navigation drawer method
        clickSem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseListActivity.this,RegisterSemester.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });

        clickHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseListActivity.this,MainActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });

    }

    private void initialize() {
        cgpa_display = findViewById(R.id.cgpaResultCourseTV);
        btn_addCourse = findViewById(R.id.dialog_btnAdd);
        input_module = findViewById(R.id.cardView_inputModule);
        courseList = new ArrayList<>();
        courseOperation = new CourseOperation(this);
        semesterOperation = new SemesterOperation(this);

        cgpaCalculator = new CgpaCalculator();
        String res = "Result : " + cgpaCalculator.getDeanList(courseOperation.getCoursesBySemesterId(mId));
        result = findViewById(R.id.resultCourseTV);
        result.setText(res);

        recyclerView_course_list = findViewById(R.id.recyclerview_course_list);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_course_list.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_course_list.setHasFixedSize(true);
        courseList = courseOperation.getCoursesBySemesterId(mId);
        courseAdapter = new CourseAdapter(this, courseList);
        recyclerView_course_list.setAdapter(courseAdapter);
        dialogActivity = new DialogActivity(CourseListActivity.this);
        DialogActivity.setClickListenerForCourse(CourseListActivity.this);
        setTotalCGPA();
        setResult();
    }

    @Override
    public void addNewCourse(Course course) {
        courseList.add(course);
        Toast.makeText(CourseListActivity.this, "Course added at the bottom", Toast.LENGTH_SHORT).show();
        courseAdapter.notifyDataSetChanged();
        Vector<Double> values = cgpaCalculator.calculation(courseList);
        double calculated_cgpa = values.elementAt(0);
        double total_credit = values.elementAt(1);
        setTotalCGPA();
        setResult();
        semesterOperation.updateSemester(mId, calculated_cgpa,total_credit,courseList.size());
    }

    @Override
    public void updateList(int position, String courseName, double credit, double gpa) {
        courseList.set(position, new Course(courseName, credit, gpa));
        courseAdapter.notifyItemChanged(position);
        Vector<Double> values = cgpaCalculator.calculation(courseList);
        double calculated_cgpa = values.elementAt(0);
        double total_credit = values.elementAt(1);
        setTotalCGPA();
        setResult();
        semesterOperation.updateSemester(mId, calculated_cgpa,total_credit,courseList.size());
    }
//
    @Override
    public void removeItem(int position) {
        try {
            courseList.remove(position);
            courseAdapter.notifyItemRemoved(position);
            setTotalCGPA();
            setResult();
            Vector<Double> values = cgpaCalculator.calculation(courseList);
            double calculated_cgpa = values.elementAt(0);
            double total_credit = values.elementAt(1);
            setTotalCGPA();
            if(!courseList.isEmpty())
                semesterOperation.updateSemester(mId, calculated_cgpa,total_credit,courseList.size());
            else
                semesterOperation.updateSemester(mId, 0,0,0);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void addNewCourseFromActivity(View view) {
        dialogActivity.showDialogForAddNewSemesterCourse(mId);
    }

    public void setTotalCGPA(){
        cgpa_total = cgpaCalculator.calculatedCGPA(courseList);
        cgpa_display.setText(cgpa_total);
    }

    public void setResult(){
        String res = "Result : " + cgpaCalculator.getDeanList(courseOperation.getCoursesBySemesterId(mId));
        result = findViewById(R.id.resultCourseTV);
        result.setText(res);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            this.finish();
        }
        return true;
    }

    // nav drawer
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

    public void clickMenuHome(View view) {
        startActivity(new Intent(CourseListActivity.this, MainActivity.class));
    }

    public void clickLogout(View view) {
        MainActivity.logout(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
}