package com.projectcgpa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.projectcgpa.DatabaseOperation.DBHelper;

public class Signup extends AppCompatActivity {

    EditText studentIdET, fullnameET, emailET, passwordET,c_passwordET;
    TextView btnSignup;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().hide();

        dbHelper = new DBHelper(this);

        studentIdET = (EditText) findViewById(R.id.studentIdET);
        fullnameET = (EditText) findViewById(R.id.fullnameET);
        emailET = (EditText) findViewById(R.id.emailET);
        passwordET = (EditText) findViewById(R.id.passwordET);
        c_passwordET = (EditText) findViewById(R.id.c_passwordET);

        btnSignup = (TextView) findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String studentId = studentIdET.getText().toString().trim();
                String fullname = fullnameET.getText().toString().trim();
                String email = emailET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();
                String cPassword = c_passwordET.getText().toString().trim();

                ContentValues values = new ContentValues();

                if(!password.equals(cPassword)){
                    Toast.makeText(Signup.this, "Password not match!", Toast.LENGTH_SHORT).show();
                }
                else if(studentId.equals("") || fullname.equals("") || email.equals("") || password.equals("") || cPassword.equals("")) {
                    Toast.makeText(Signup.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                }
                else if(studentId.matches("[a-zA-Z]+") && studentId.length()>2 ) {
                    Toast.makeText(Signup.this, "Student ID must be numberic!", Toast.LENGTH_SHORT).show();
                }else {
                    values.put(DBHelper.user_id, studentId);
                    values.put(DBHelper.user_fullname, fullname);
                    values.put(DBHelper.user_email, email);
                    values.put(DBHelper.user_password, password);

                    dbHelper.registerUser(values);

                    Toast.makeText(Signup.this, "Register Successful!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}