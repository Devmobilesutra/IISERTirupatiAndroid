package com.mobilesutra.iiser_tirupati.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Model.DTO_Faculty_Attendence;
import com.mobilesutra.iiser_tirupati.Model.DTO_Stud_Attendence;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Bhavesh Chaudhari on 23-Jan-20.
 */
public class TABLE_FACULTY_ATTENDENCE {
    public static String NAME = "tbl_faculty_attendence";
    public static String LOG_TAG = "TABLE_FACULTY_ATTENDENCE";

    public static String
            COL_ID = "id",
            COL_COURSE_CODE = "course_code",
            COL_ROLL_NUMBER = "roll_number",
            COL_BATCH="batch",
            COL_AVERAGE_ATTENDENCE = "average_attendance";

    public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME + " ("
            + COL_ID + " INTEGER PRIMARY KEY,"
            + COL_COURSE_CODE + " TEXT,"
            + COL_ROLL_NUMBER + " TEXT,"
            + COL_BATCH + " TEXT,"
            + COL_AVERAGE_ATTENDENCE + " TEXT)";

    public static void insertFacultyAttendenceMaster(String course_code, String roll_number,String batch, String average_attendance) {
        SQLiteDatabase db = IISERApp.db.getReadableDatabase();
        Log.d("version", String.valueOf(db.getVersion()));
        ContentValues cv = new ContentValues();
        cv.put(COL_COURSE_CODE, course_code);
        cv.put(COL_ROLL_NUMBER, roll_number);
        cv.put(COL_BATCH, batch);
        cv.put(COL_AVERAGE_ATTENDENCE,average_attendance);
        try {
            long insertId = db.insert(NAME, null, cv);
            IISERApp.log(LOG_TAG, "insertItemMaster is " + insertId);

            if (insertId == -1) {

            }
        } catch (SQLiteException e) {
            IISERApp.log(LOG_TAG, "insertItemMaster Exception is " + e.getMessage());
        }
      //  db.close();

    }

    public static  ArrayList<DTO_Faculty_Attendence> getrollNumberFaculty(String couse_code){
        IISERApp.log(LOG_TAG, "in updateDailyMilk date = ");

        SQLiteDatabase db = IISERApp.db.getWritableDatabase();

        ArrayList<DTO_Faculty_Attendence> facultyAttendeceArray = new ArrayList<>();

        String str1 = "select * from " + NAME +" where "+COL_COURSE_CODE+"=? and  tbl_faculty_attendence.roll_number !=''";
        Cursor c = db.rawQuery(str1, new String[]{couse_code});

        IISERApp.log(LOG_TAG, "in updateDailyMilk date = c.getCount() ------>" + c.getCount());
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                facultyAttendeceArray.add(new DTO_Faculty_Attendence(c.getString(c.getColumnIndexOrThrow(COL_ROLL_NUMBER)),
                        c.getString(c.getColumnIndexOrThrow(COL_AVERAGE_ATTENDENCE))));
            } while (c.moveToNext());

        }

      //  c.close();
        IISERApp.log(LOG_TAG, " dto is +" + facultyAttendeceArray.toString());
        return facultyAttendeceArray;
    }

    public static void deleteAllRecord() {
        SQLiteDatabase db = IISERApp.db.getReadableDatabase();
        db.delete(NAME, null, null);
    }
}
