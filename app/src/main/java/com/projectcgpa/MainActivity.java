package com.projectcgpa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
        TextView fullname = (TextView) findViewById(R.id.fullnameTV);
        TextView clickHome = (TextView) findViewById(R.id.clickHome);


        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");

        fullname.setText(user.getFullname());

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


}