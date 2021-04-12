package com.mobilesutra.iiser_tirupati.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Model.DTO_Stud_Attendence;

import java.util.ArrayList;

/**
 * Created by Bhavesh Chaudhari on 16-Jan-20.
 */
public class TABLE_STUDENT_ATTENDENCE {

    public static String NAME = "tbl_stud_attendence";
    public static String LOG_TAG = "TABLE_STUDENT_ATTENDENCE";

    public static String
            COL_DATE = "date",
            COL_COURSE_CODE = "course_code",
            COL_STUDENT_PRESENT = "student_present",
            COL_LECTURE_PRESENT = "lecture_present";

    public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME + " ("
            + COL_DATE + " TEXT,"
            + COL_COURSE_CODE + " TEXT,"
            + COL_STUDENT_PRESENT + " TEXT,"
            + COL_LECTURE_PRESENT + " INTEGER)";

    public static void insertStudentAttendence(String date, String course_code, String student_present,String lecture_present) {
        SQLiteDatabase db = IISERApp.db.getReadableDatabase();
        Log.d("version", String.valueOf(db.getVersion()));
        ContentValues cv = new ContentValues();
        cv.put(COL_DATE, date);
        cv.put(COL_COURSE_CODE, course_code);
        cv.put(COL_STUDENT_PRESENT, student_present);
        cv.put(COL_LECTURE_PRESENT,lecture_present);



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


    public static  ArrayList<DTO_Stud_Attendence> getPresentDatesForStud(String couse_code){
        IISERApp.log(LOG_TAG, "in updateDailyMilk date = ");

        SQLiteDatabase db = IISERApp.db.getWritableDatabase();

        ArrayList<DTO_Stud_Attendence> studAttendeceArray = new ArrayList<>();

        String str1 = "select * from " + NAME +" where "+COL_COURSE_CODE+"=?";
        Cursor c = db.rawQuery(str1, new String[]{couse_code});

        IISERApp.log(LOG_TAG, "in updateDailyMilk date = c.getCount() ------>" + c.getCount());
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                studAttendeceArray.add(new DTO_Stud_Attendence(c.getString(c.getColumnIndexOrThrow(COL_DATE)),
                        c.getString(c.getColumnIndexOrThrow(COL_COURSE_CODE)),
                        c.getString(c.getColumnIndexOrThrow(COL_STUDENT_PRESENT)),
                        c.getString(c.getColumnIndexOrThrow(COL_LECTURE_PRESENT))));
            } while (c.moveToNext());

        }

      //  c.close();
        IISERApp.log(LOG_TAG, " dto is +" + studAttendeceArray.toString());
        return studAttendeceArray;
    }

    public static void deleteAllRecord() {
        SQLiteDatabase db = IISERApp.db.getReadableDatabase();
        db.delete(NAME, null, null);
    }

}


