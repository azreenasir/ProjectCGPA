package com.projectcgpa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.projectcgpa.DatabaseOperation.SemesterOperation;
import com.projectcgpa.entities.Semester;
import com.projectcgpa.entities.User;

import java.util.ArrayList;
import java.util.List;

public class CgpaReportChart extends AppCompatActivity {
    DrawerLayout drawerLayout;
    private PieChart pieChart;
    SemesterOperation semesterOperation;
    List<Semester> semesterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cgpa_report_chart);

        getSupportActionBar().hide();

        drawerLayout = findViewById(R.id.drawer_layout);
        TextView clickSem = (TextView) findViewById(R.id.clickSemTV);
        TextView clickHome = (TextView) findViewById(R.id.clickHome);
        TextView clickReport = (TextView) findViewById(R.id.clickReportTV);

        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");

        pieChart = findViewById(R.id.pieChart);
        setupPieChart();
        loadPieChartData();


        clickSem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CgpaReportChart.this,RegisterSemester.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });

        clickHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CgpaReportChart.this,MainActivity.class);
                intent.putExtra("user",user);
                startActivity(new Intent(intent));
            }
        });

        clickReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });

    }

    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Semester Performance");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);


    }

    private void loadPieChartData(){
        ArrayList<PieEntry> entries = new ArrayList<>();
        semesterOperation = new SemesterOperation(this);
        semesterList = semesterOperation.getAllSemester();
        for(int i = 0 ; i < semesterList.size() ; i ++) {
            double total_gpa =  semesterList.get(i).getTotal_course();
            entries.add(new PieEntry((float) total_gpa,semesterList.get(i).getSemester_name()));
        }

        ArrayList<Integer> colors = new ArrayList<>();
        for(int color: ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for(int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Semester");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.animateY(1400, Easing.EaseInOutQuad);
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

    public void clickLogout(View view) {
        MainActivity.logout(this);
    }


}