package com.projectcgpa.DatabaseOperation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.projectcgpa.entities.Course;
import com.projectcgpa.entities.Semester;

import java.util.ArrayList;
import java.util.List;

public class SemesterOperation {
    private Context mContext;
    private DBHelper dbHelper;
    private static SQLiteDatabase sqLiteDatabase;
    private static String[] mAllColumns = {DBHelper.UID_SEMESTER,
            DBHelper.COLUMN_SEMESTER_NAME,
            DBHelper.COLUMN_SEMESTER_GPA,
            DBHelper.COLUMN_SEMESTER_TOTAL_COURSE,
            DBHelper.COLUMN_SEMESTER_TOTAL_CREDIT,
            DBHelper.COLUMN_SEMESTER_STUDENT_ID
    };

    public SemesterOperation(Context context) {
        mContext = context;
        dbHelper = new DBHelper(context);
        try {
            sqLiteDatabase = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // create
    public static Semester createSemester(String semester_name, double gpa, int total_course, double total_credit, String student_id) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_SEMESTER_NAME, semester_name);
        values.put(DBHelper.COLUMN_SEMESTER_GPA, gpa);
        values.put(DBHelper.COLUMN_SEMESTER_TOTAL_COURSE, total_course);
        values.put(DBHelper.COLUMN_SEMESTER_TOTAL_CREDIT, total_credit);
        values.put(DBHelper.COLUMN_SEMESTER_STUDENT_ID, student_id);
        long insertId = sqLiteDatabase.insert(DBHelper.TABLE_NAME_SEMESTER,null,values);
        Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_NAME_SEMESTER, mAllColumns,
                DBHelper.UID_SEMESTER + " = " + insertId, null, null,
                null, null);
        cursor.moveToFirst();
        Semester newSemester = cursorToSmester(cursor);
        cursor.close();
        return newSemester;
    }

    // get
    public List<Semester> getAllSemester(){
        List<Semester> allSemester = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_NAME_SEMESTER, mAllColumns,
                null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Semester semester = cursorToSmester(cursor);
                allSemester.add(semester);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return allSemester;
    }

    public void deleteSemester(Semester semester) {
        long id = semester.getmId();
        CourseOperation cgpaOperation = new CourseOperation(mContext);
        List<Course> listCourses = cgpaOperation.getCoursesBySemesterId(id);
        if (listCourses != null && !listCourses.isEmpty()) {
            for (Course e : listCourses) {
                cgpaOperation.deleteCourse(e);
            }
        }
        String[] args={String.valueOf(id)};
        sqLiteDatabase.delete(DBHelper.TABLE_NAME_SEMESTER, DBHelper.UID_SEMESTER
                + " = ?", args);
    }

    public void updateSemester(long semesterId, double gpa, double total_credit, int total_course){
        long id = semesterId;
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_SEMESTER_GPA,gpa);
        contentValues.put(DBHelper.COLUMN_SEMESTER_TOTAL_CREDIT,total_credit);
        contentValues.put(DBHelper.COLUMN_SEMESTER_TOTAL_COURSE,total_course);
        String[] whereArgs = {String.valueOf(id)};
        sqLiteDatabase.update(DBHelper.TABLE_NAME_SEMESTER,contentValues, DBHelper.UID_SEMESTER+" =? ",whereArgs);
    }

    public void updateSemesterName(Semester semester, String name){
        long id = semester.getmId();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_SEMESTER_NAME,name);
        String[] whereArgs = {String.valueOf(id)};
        sqLiteDatabase.update(DBHelper.TABLE_NAME_SEMESTER,contentValues, DBHelper.UID_SEMESTER+" =? ",whereArgs);

        /*String sql = " UPDATE " + DBHelper.TABLE_NAME_SEMESTER + " SET " + DBHelper.COLUMN_SEMESTER_NAME + " = " + "' name '" + " WHERE " + DBHelper.UID_SEMESTER + " = " +id;
        sqLiteDatabase.execSQL(sql);*/
    }

    private static Semester cursorToSmester(Cursor cursor) {
        Semester semester = new Semester();
        semester.setmId(cursor.getLong(0));
        semester.setSemester_name(cursor.getString(1));
        semester.setTotal_gpa(cursor.getDouble(2));
        semester.setTotal_course(cursor.getInt(3));
        semester.setTotal_credit(cursor.getDouble(4));
        semester.setStudent_id(cursor.getString(5));
        return semester;
    }


}
