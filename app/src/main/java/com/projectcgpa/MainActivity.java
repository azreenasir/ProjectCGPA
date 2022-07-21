package com.projectcgpa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.projectcgpa.DatabaseOperation.DBHelper;
import com.projectcgpa.entities.User;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        getSupportActionBar().hide();

        drawerLayout = findViewById(R.id.drawer_layout);
        TextView clickSem = (TextView) findViewById(R.id.clickSemTV);
        TextView clickReport = (TextView) findViewById(R.id.clickReportTV);
        TextView fullname = (TextView) findViewById(R.id.fullnameTV);
        TextView clickHome = (TextView) findViewById(R.id.clickHome);


        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");

        fullname.setText(user.getFullname());

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            String username = savedInstanceState.getString("username");
            fullname.setText(username);
        }

        clickSem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegisterSemester.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });

        clickHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });

        clickReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CgpaReportChart.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        TextView fullname = (TextView) findViewById(R.id.fullnameTV);
        String username = (String) fullname.getText();
        outState.putString("username",username);
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
        logout(this);
    }


    public static void logout(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finishAffinity();
                System.exit(0);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

    @Override
    public void onBackPressed()
    {
        Toast.makeText(MainActivity.this,"Please logout first!", Toast.LENGTH_SHORT).show();
    }


}