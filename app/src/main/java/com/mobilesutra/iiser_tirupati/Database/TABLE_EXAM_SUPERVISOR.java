package com.mobilesutra.iiser_tirupati.Database;

import android.database.sqlite.SQLiteDatabase;

import com.mobilesutra.iiser_tirupati.Config.IISERApp;

/**
 * Created by kalyani on 03/05/2016.
 */
public class TABLE_EXAM_SUPERVISOR {
    public static String NAME = "tbl_exam_supervisor",LOG_TAG="TABLE_SUPER";



    public static String
            COL_ID = "id",
            COL_EXAM_ID = "exam_id",
            COL_SUPERVISOR_ID = "supervisor_id";

    public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_EXAM_ID + " INTEGER,"
            + COL_SUPERVISOR_ID + "INTEGER)";


    public static String QUERY_INSERT = "INSERT INTO " + NAME + " (" + COL_EXAM_ID + "," + COL_SUPERVISOR_ID+ ") VALUES (?,?);";

    public static void delete_tbl_exam_supervisor()
    {
        SQLiteDatabase db = IISERApp.db.getWritableDatabase();
        int numRows = db.delete(NAME, null, null);
        IISERApp.log(LOG_TAG,"In delete_tblcal");
        IISERApp.log(LOG_TAG, "DeletedRows:->" + numRows);
    }
}
