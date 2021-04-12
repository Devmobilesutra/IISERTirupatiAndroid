package com.mobilesutra.iiser_tirupati.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.mobilesutra.iiser_tirupati.Config.IISERApp;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Bhavesh Chaudhari on 16-Jan-20.
 */
public class TABLE_ATTENDENCE_MASTER {
    public static String NAME = "tbl_attendence_master";
    public static String LOG_TAG = "TABLE_ATTENDENCE";

    public static String
            COL_ID = "id",
            COL_COURSE_CODE = "course_code",
            COL_TOT_NO_CLASSES = "tot_no_classes",
            COL_ATTENTED_CLASSES = "attended_classes",
            COL_AVERAGE_ATTENDENCE = "average_attendance";

    public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME + " ("
            + COL_ID + " INTEGER PRIMARY KEY,"
            + COL_COURSE_CODE + " TEXT,"
            + COL_TOT_NO_CLASSES + " TEXT,"
            + COL_ATTENTED_CLASSES + " TEXT,"
            + COL_AVERAGE_ATTENDENCE + " TEXT)";

    public static void insertAttendenceMaster(String course_code, String tot_no_classes,String attenred_classes, String average_attendance) {
        SQLiteDatabase db = IISERApp.db.getReadableDatabase();
        Log.d("version", String.valueOf(db.getVersion()));
        ContentValues cv = new ContentValues();
        cv.put(COL_COURSE_CODE, course_code);
        cv.put(COL_TOT_NO_CLASSES, tot_no_classes);
        cv.put(COL_ATTENTED_CLASSES, attenred_classes);
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

    public static List<LinkedHashMap<String, String>> get_attendence_percentage(String course_code) {
        SQLiteDatabase db = IISERApp.db.getReadableDatabase();
        Log.d("version", String.valueOf(db.getVersion()));
        List<LinkedHashMap<String, String>> menuItems = new ArrayList();

        IISERApp.log(LOG_TAG, "In get assignment_detail function: " + course_code);
        String str1 = "select * from " + NAME +" where "+COL_COURSE_CODE+"=?";
        Cursor c = db.rawQuery(str1, new String[]{course_code});

        if (c.moveToFirst()) {
            do {
                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                map.put("tot_no_classes", c.getString(c.getColumnIndexOrThrow(COL_TOT_NO_CLASSES)));
                map.put("attended_classes",c.getString(c.getColumnIndexOrThrow(COL_ATTENTED_CLASSES)));
                map.put("avg_percentage",c.getString(c.getColumnIndexOrThrow(COL_AVERAGE_ATTENDENCE)));
                menuItems.add(map);
            } while (c.moveToNext());
        }

        IISERApp.log(LOG_TAG,"menuitems:"+menuItems);
      //  c.close();


        return menuItems;

    }

    public static LinkedHashMap<String, String> get_Faculty_stud_courseCode() {
        SQLiteDatabase db = IISERApp.db.getReadableDatabase();
        Log.d("version", String.valueOf(db.getVersion()));
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

        String str1 = "select course_code  from " + NAME;

        Cursor c = db.rawQuery(str1, null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                IISERApp.log(LOG_TAG, "i doo s is:");
                map.put(c.getString(c.getColumnIndexOrThrow(COL_COURSE_CODE)),"");


            } while (c.moveToNext());
        }


        //  c.close();


        return map;

    }




    public static void deleteAllRecord() {
        SQLiteDatabase db = IISERApp.db.getReadableDatabase();
        db.delete(NAME, null, null);
    }

}
