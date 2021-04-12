



package com.mobilesutra.iiser_tirupati.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mobilesutra.iiser_tirupati.Config.IISERApp;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by KALYANI on 02/05/2016.
 */
public class TABLE_TIMETABLE {
    public static String NAME = "tbl_timetable", LOG_TAG = "tbl_timetable";

    public static String
            COL_ID = "id",
            COL_COURSE_ID = "course_id",
            COL_DAY = "day",
            COL_PERIOD_TIME = "period_time",
            COL_BATCH = "batch",
            COL_VENUE = "venue",
            COL_IS_SEEN = "is_seen";

    public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME + " ("
            + COL_ID + " INTEGER PRIMARY KEY,"
            + COL_COURSE_ID + " TEXT,"
            + COL_DAY + " TEXT,"
            + COL_PERIOD_TIME + " TEXT,"
            + COL_BATCH + " TEXT,"
            + COL_VENUE + " TEXT,"
            + COL_IS_SEEN + " TEXT)";

    public static String QUERY_INSERT = "INSERT INTO " + NAME + " (" + COL_ID + "," + COL_COURSE_ID + "," + COL_DAY + ","
            + COL_PERIOD_TIME + "," + COL_BATCH + "," + COL_VENUE + "," + COL_IS_SEEN + ") VALUES (?,?,?,?,?,?,?);";

    public static void InsertTimetable(String id, String course_id, String day, String period_time,
                                       String batch, String venue, String is_seen) {
        IISERApp.log("TABLE_TIMETABLE", "enter in InsertTimetable function");
        SQLiteDatabase db = IISERApp.db.getWritableDatabase();

        String sql = "SELECT * FROM " + NAME + " where " + COL_ID + " =?" + " ;";

        Cursor c = db.rawQuery(sql, new String[]{id});

        int num = c.getCount();
        ContentValues initialValues = new ContentValues();
        initialValues.put(COL_ID, id.toString());
        initialValues.put(COL_COURSE_ID, course_id.toString());
        initialValues.put(COL_DAY, day.toString());
        initialValues.put(COL_PERIOD_TIME, period_time.toString());
        initialValues.put(COL_BATCH, batch.toString());
        initialValues.put(COL_VENUE, venue.toString());
        initialValues.put(COL_IS_SEEN, is_seen.toString());

        if (num > 0) {
            int numRows = db.update(NAME, initialValues, COL_ID + " =? ",
                    new String[]{id});
            Log.d("UPDTABLE_TIMETABLE:" + numRows, id);
        } else {
            long rowId = db.insert(NAME, null, initialValues);
            Log.d("INSRTABLE_TIMETABLE:" + rowId, id);
        }
        c.close();

    }


    public static List<LinkedHashMap<String, String>> get_timetable_details(String day) {
        SQLiteDatabase db = IISERApp.db.getReadableDatabase();

/*
        if (IISERApp.selected_course_lhm.size() > 0) {

            Set<String> keys = IISERApp.selected_course_lhm.keySet();
            for (String key : keys) {
                IISERApp.selected_course_lhm.get(key);
                IISERApp.log(LOG_TAG, "select" + key + "-" + IISERApp.selected_course_lhm.get(key));
                if (IISERApp.selected_course_lhm.get(key).equalsIgnoreCase("Y")) {
                    IISERApp.log(LOG_TAG, "select" + key + "-" + IISERApp.selected_course_lhm.get(key));
                    IISERApp.log("ABC", "select" + key );

                                   }
            }
        }*/


        List<LinkedHashMap<String, String>> menuItems = new ArrayList();
        String str1 = "select * from " + NAME + " where " + COL_DAY + "=? " + " ;";
        Cursor c = db.rawQuery(str1, new String[]{day});

        if (c.moveToFirst()) {
            do {
                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                map.put("id",
                        c.getString(c.getColumnIndexOrThrow(COL_ID)));
                map.put("course_id",
                        c.getString(c.getColumnIndexOrThrow(COL_COURSE_ID)));
                map.put("day",
                        c.getString(c.getColumnIndexOrThrow(COL_DAY)));
                map.put("period_time",
                        c.getString(c.getColumnIndexOrThrow(COL_PERIOD_TIME)));
                map.put("batch",
                        c.getString(c.getColumnIndexOrThrow(COL_BATCH)));
                map.put("venue",
                        c.getString(c.getColumnIndexOrThrow(COL_VENUE)));
                map.put("is_seen",
                        c.getString(c.getColumnIndexOrThrow(COL_IS_SEEN)));
                menuItems.add(map);
            } while (c.moveToNext());
        }
        c.close();
        IISERApp.log(LOG_TAG, "day:" + day + " timetable menu:" + menuItems);
        return menuItems;
    }

    public static void delete_tbl_timetable_sync() {


      /*  SQLiteDatabase db = IISERApp.db.getWritableDatabase();
        int numRows = db.delete(NAME, null, null);*/

        SQLiteDatabase db = IISERApp.db.getWritableDatabase();
        String str1 = "select * from " + NAME;
        Cursor c = db.rawQuery(str1, null);
        int count= c.getCount();
        IISERApp.log(LOG_TAG, "Count"+count);
        int numRows = db.delete(NAME, null, null);
        IISERApp.log(LOG_TAG, "In delete_tbl_timetable_sync");

        IISERApp.log(LOG_TAG, "DeletedRows:->" + numRows);
    }

    public static void delete_record() {

        SQLiteDatabase db = IISERApp.db.getWritableDatabase();
        int numRows = db.delete(NAME, null, null);

    }


    //////////to take  from database and set to spinner
    public static List<LinkedHashMap<String, String>> get_courses_spnr() {
        IISERApp.log(LOG_TAG, "IN get_courses_spnr()");
        SQLiteDatabase db = IISERApp.db.getReadableDatabase();

        List<LinkedHashMap<String, String>> menuItems = new ArrayList();
        //String str1 = "select * from " + NAME + " where " + COL_DAY + "=? " + " ;";
        String str1 = "select distinct course_id from " + NAME;
        Cursor c = db.rawQuery(str1, null);
        IISERApp.log(LOG_TAG, "c.getCount()"+c.getCount());
        if (c.getCount() > 0) {
            IISERApp.log(LOG_TAG, "in if ");
            c.moveToFirst();
            do {
                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

                map.put("course_id",
                        c.getString(c.getColumnIndexOrThrow(COL_COURSE_ID)));
                menuItems.add(map);
            } while (c.moveToNext());

            c.close();
            IISERApp.log(LOG_TAG, "day:"  + " COURSES ARE" + menuItems);

            //  IISERApp.log(LOG_TAG, "day:" + day + " COURSES ARE" + menuItems);

        }
        return menuItems;
    }



    public static LinkedHashMap<String, String> get_faculty_course() {
        IISERApp.log(LOG_TAG,"in get_faculty_course()");
        SQLiteDatabase db = IISERApp.db.getReadableDatabase();

        String str1 = "select distinct course_id from " + NAME;
        Cursor c = db.rawQuery(str1, null);
        c.getCount();
        IISERApp.log(LOG_TAG,"count of courses is:"+c.getCount());


        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                IISERApp.log(LOG_TAG, "i doo s is:");
                map.put(c.getString(c.getColumnIndexOrThrow(COL_COURSE_ID)),"");


            } while (c.moveToNext());
        }
        IISERApp.log("database", "menuitems:" + map);
        c.close();
        return map;
    }
}

