package com.mobilesutra.iiser_tirupati.Database;

import android.database.sqlite.SQLiteDatabase;

import com.mobilesutra.iiser_tirupati.Config.IISERApp;

/**
 * Created by satish on 03/05/2016.
 */
public class TABLE_FACULTY_CALENDAR {
    public static String NAME = "tbl_faculty_calendar",LOG_TAG="TABLE_FACULTY_CAL";

    public static String
            COL_ID = "id",
            COL_CALENDAR_ID = "calendar_date",
            COL_FACULTY_ID = "calendar_day";

    public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_CALENDAR_ID + " INTEGER,"
            + COL_FACULTY_ID + " INTEGER)";


    public static String QUERY_INSERT = "INSERT INTO " + NAME + " (" + COL_CALENDAR_ID + "," + COL_FACULTY_ID
            + ") VALUES (?,?);";
    public static void delete_tbl_faculty_cal()
    {
        SQLiteDatabase db = IISERApp.db.getWritableDatabase();
        int numRows = db.delete(NAME, null, null);
        IISERApp.log(LOG_TAG,"In delete_tblcal");
        IISERApp.log(LOG_TAG, "DeletedRows:->" + numRows);
    }
}