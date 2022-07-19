package com.projectcgpa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.projectcgpa.DatabaseOperation.DBHelper;
import com.projectcgpa.entities.User;

public class Login extends AppCompatActivity {

    EditText emailET, passwordET;
    TextView btnLogin;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        emailET = (EditText) findViewById(R.id.emailET);
        passwordET = (EditText) findViewById(R.id.passwordET);
        btnLogin = (TextView) findViewById(R.id.btnLogin);

        dbHelper = new DBHelper(this);

        TextView signupBtn = (TextView) findViewById(R.id.Signup);

        signupBtn.setText(fromHtml( "New? Create account" + "<u>here</u>" ));

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Signup.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();

                Boolean result = dbHelper.checkUser(email, password);
                if (result == true) {
                    User user = dbHelper.login(email, password);
                    Toast.makeText(Login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this,MainActivity.class);
                    intent.putExtra("user",user);
                    startActivity(intent);
                }else {
                    Toast.makeText(Login.this, "Email or Password are wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private static Spanned fromHtml(String html) {
        Spanned result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        }else {
            result = Html.fromHtml(html);
        }
        return result;
    }


}