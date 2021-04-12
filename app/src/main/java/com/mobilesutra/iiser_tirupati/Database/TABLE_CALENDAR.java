package com.mobilesutra.iiser_tirupati.Database;

import android.database.sqlite.SQLiteDatabase;

import com.mobilesutra.iiser_tirupati.Config.IISERApp;

/**
 * Created by kalyani on 03/05/2016.
 */
public class TABLE_CALENDAR {

    public static String NAME = "tbl_calendar",LOG_TAG="TABLE_COURSE";

    public static String
            COL_ID = "id",
            COL_CALENDAR_DATE = "calendar_date",
            COL_CALENDAR_DAY = "calendar_day",
            COL_CALENDAR_TITLE= "calendar_title";

    public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_CALENDAR_DATE + " TEXT,"
            + COL_CALENDAR_DAY + " TEXT,"
            + COL_CALENDAR_TITLE + " TEXT)";


    public static String QUERY_INSERT = "INSERT INTO " + NAME + " (" + COL_CALENDAR_DATE + "," + COL_CALENDAR_DAY
            + "," + COL_CALENDAR_TITLE + ") VALUES (?,?,?);";

    public static void delete_tbl_cal()
    {
        SQLiteDatabase db = IISERApp.db.getWritableDatabase();
        int numRows = db.delete(NAME, null, null);
        IISERApp.log(LOG_TAG,"In delete_tblcal");
        IISERApp.log(LOG_TAG, "DeletedRows:->" + numRows);
    }
}
