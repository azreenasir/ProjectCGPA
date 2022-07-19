package com.projectcgpa.DatabaseOperation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.projectcgpa.entities.Grade;
import com.projectcgpa.entities.User;

public class DBHelper extends SQLiteOpenHelper {
    public static final String database_name = "cgpa";

    // user table schema
    public static final String user_table = "user";
    public static final String user_id = "Student_Id";
    public static final String user_fullname = "Fullname";
    public static final String user_email = "Email";
    public static final String user_password = "Password";
    // create table
    private static final String TABLE_CREATE_USER = "CREATE TABLE " + user_table
            + "("
            + user_id + " TEXT, "
            + user_fullname + " TEXT, "
            + user_email + " TEXT, "
            + user_password + " TEXT)";
    // drop table
    private  static final String DROP_TABLE_USER = "DROP TABLE IF EXISTS " + user_table;


    // course table schema
    public static final String TABLE_NAME_CGPA = "TABLE_GPA";
    public static final String UID_COURSE = "_id";
    public static final String COLUMN_CGPA_COURSE_NAME = "COURSE_NAME";
    public static final String COLUMN_CGPA_COURSE_CREDIT = "COURSE_CREDIT";
    public static final String COLUMN_CGPA_COURSE_GPA = "COURSE_GPA";
    public static final String COLUMN_CGPA_SEMESTER_ID = "SEMESTER_ID";
    // create table
    private static final String TABLE_CREATE_CGPA = "CREATE TABLE "+TABLE_NAME_CGPA+" ("
            +UID_COURSE +" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +COLUMN_CGPA_COURSE_NAME+" VARCHAR(255), "
            +COLUMN_CGPA_COURSE_CREDIT+" REAL NOT NULL, "
            +COLUMN_CGPA_COURSE_GPA+" REAL NOT NULL, "
            +COLUMN_CGPA_SEMESTER_ID+" INTEGER NOT NULL "
            +");";
    // drop table
    private static final String DROP_TABLE_CGPA = "DROP TABLE IF EXISTS "+TABLE_NAME_CGPA;

    // semester table schema
    public static final String TABLE_NAME_SEMESTER = "TABLE_SEMESTER";
    public static final String UID_SEMESTER = "_id";
    public static final String COLUMN_SEMESTER_NAME = "SEMESTER_NAME";
    public static final String COLUMN_SEMESTER_GPA = "SEMESTER_GPA";
    public static final String COLUMN_SEMESTER_TOTAL_COURSE = "SEMESTER_TOTAL_COURSE";
    public static final String COLUMN_SEMESTER_TOTAL_CREDIT = "SEMESTER_TOTAL_CREDIT";
    public static final String COLUMN_SEMESTER_STUDENT_ID = "STUDENT_ID";
    // create table
    private static final String TABLE_CREATE_SEMESTER = "CREATE TABLE "+TABLE_NAME_SEMESTER+" ("
            +UID_SEMESTER+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +COLUMN_SEMESTER_NAME+" VARCHAR(255), "
            +COLUMN_SEMESTER_GPA+" REAL NOT NULL, "
            +COLUMN_SEMESTER_TOTAL_COURSE+" INTEGER NOT NULL, "
            +COLUMN_SEMESTER_TOTAL_CREDIT+" REAL NOT NULL, "
            +COLUMN_SEMESTER_STUDENT_ID+" TEXT NOT NULL "
            +");";

    private static final String DROP_TABLE_SEMESTER = "DROP TABLE IF EXISTS "+TABLE_NAME_SEMESTER;


    private final SQLiteDatabase db;

    // constructor
    public DBHelper( Context context) {
        super(context, database_name, null, 2);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_USER);
        db.execSQL(TABLE_CREATE_CGPA);
        db.execSQL(TABLE_CREATE_SEMESTER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_USER);
        db.execSQL(DROP_TABLE_CGPA);
        db.execSQL(DROP_TABLE_SEMESTER);
    }

    // Register new User
    public void registerUser(ContentValues values){
        db.insert(user_table, null, values);
    }

    // check User to Login
    public boolean checkUser(String email, String password) {
        String[] columns =  {user_id};
        SQLiteDatabase db = getReadableDatabase();
        String selection = user_email + "=?" + " and " + user_password + "=?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query(user_table, columns, selection, selectionArgs, null, null, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count > 0;
    }

    // check user after login then save the information to User Java class
    public User login(String email, String password) {
        User user = null;
        try {
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            Cursor cursor =sqLiteDatabase.rawQuery("select * from " + user_table + " where email = ? AND password = ?", new String[]{email, password});
            if (cursor.moveToFirst()) {
                user = new User();
                user.setStudent_Id(cursor.getLong(0));
                user.setFullname(cursor.getString(1));
                user.setEmail(cursor.getString(2));
                user.setPassword(cursor.getString(3));

            }
        }catch (Exception e){
            user = null;
        }
        return user;
    }

}
