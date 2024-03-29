package com.mobilesutra.iiser_tirupati.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mobilesutra.iiser_tirupati.Config.IISERApp;

/**
 * Created by kalyani on 02/05/2016.
 */
public class TABLE_WEBSERVICE {
    public static String NAME = "tbl_webservice",LOG_TAG = "tbl_user_profile";

    public static String
            COL_ID = "id",
            COL_WEB_LINK = "web_link",
            COL_IS_SYNC = "is_sync";

    public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_WEB_LINK + " TEXT,"
            + COL_IS_SYNC + " TEXT)";

    public static String QUERY_INSERT = "INSERT INTO " + NAME + " (" + COL_WEB_LINK + "," + COL_IS_SYNC+") VALUES (?,?);";

    public static void delete_tbl_web_service() {
        SQLiteDatabase db = IISERApp.db.getWritableDatabase();
        String str1 = "select * from " + NAME;
        Cursor c = db.rawQuery(str1, null);
        int count= c.getCount();
        IISERApp.log(LOG_TAG, "Count"+count);
        int numRows = db.delete(NAME, null, null);
        IISERApp.log(LOG_TAG, "In delete_tbl_web_service");

        IISERApp.log(LOG_TAG, "DeletedRows:->" + numRows);
    }
}
