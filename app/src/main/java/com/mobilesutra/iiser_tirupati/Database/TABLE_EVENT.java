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
 * Created by kalyani on 02/05/2016.
 */
public class TABLE_EVENT {
    public static String NAME = "tbl_event";

    public static String
            COL_ID = "id",
            COL_EVENT_TITLE = "event_title",
            COL_EVENT_DESCRIPTION = "event_description",
            COL_EVENT_DATE= "event_date",
            COL_PDF_LINK = "pdf_link",
            COL_VENUE = "venue",
            COL_IS_SEEN = "is_seen";

    public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME + " ("
            + COL_ID + " INTEGER PRIMARY KEY,"
            + COL_EVENT_TITLE + " TEXT,"
            + COL_EVENT_DESCRIPTION + " TEXT,"
            + COL_EVENT_DATE + " TEXT,"
            + COL_PDF_LINK + " TEXT,"
            + COL_VENUE + " TEXT,"
            + COL_IS_SEEN + " TEXT)";

    public static String QUERY_INSERT = "INSERT INTO " + NAME + " ("+ COL_ID + "," + COL_EVENT_TITLE + "," + COL_EVENT_DESCRIPTION
            + "," + COL_EVENT_DATE + ","+ COL_PDF_LINK + "," + COL_VENUE + "," + COL_IS_SEEN + ") VALUES (?,?,?,?,?,?,?);";


    public static void InsertEventData(String event_id, String event_title, String event_discription,
                                       String event_date,String pdf_link,String venue,String is_seen) {
        IISERApp.log("InsertEventData", "enter in InsertEventData function");
        SQLiteDatabase db = IISERApp.db.getWritableDatabase();

        String sql = "SELECT * FROM " + NAME + " where " + COL_ID + "=? " + " ;";
        Cursor c = db.rawQuery(sql, new String[]{event_id});
        int num = c.getCount();
        ContentValues initialValues = new ContentValues();
        initialValues.put(COL_ID, event_id.toString());
        initialValues.put(COL_EVENT_TITLE, event_title.toString());
        initialValues.put(COL_EVENT_DESCRIPTION, event_discription.toString());
        initialValues.put(COL_EVENT_DATE, event_date.toString());
        initialValues.put(COL_PDF_LINK, pdf_link.toString());
        initialValues.put(COL_VENUE, venue.toString());
        initialValues.put(COL_IS_SEEN, is_seen.toString());

        if (num > 0) {
            int numRows = db.update(NAME, initialValues, COL_ID + " =? ",
                    new String[]{event_id});
            Log.d("UPDTABLE_EXMSCHEDULE:" + numRows, event_id);
        } else {
            long rowId = db.insert(NAME, null, initialValues);
            Log.d("INSRTABLE_EXMSCHEDULE:" + rowId, event_id);
        }
        c.close();

    }

    public static List<LinkedHashMap<String, String>> get_event_list() {
        SQLiteDatabase db = IISERApp.db.getReadableDatabase();
        List<LinkedHashMap<String, String>> menuItems = new ArrayList();
        String str1 = "select * from " + NAME + " order by "+COL_EVENT_DATE+ " ;";
        Cursor c = db.rawQuery(str1, null);

        if (c.moveToFirst()) {
            do {
                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                map.put("event_id",
                        c.getString(c.getColumnIndexOrThrow(COL_ID)));
                map.put("event_title",
                        c.getString(c.getColumnIndexOrThrow(COL_EVENT_TITLE)));
                map.put("event_description",
                        c.getString(c.getColumnIndexOrThrow(COL_EVENT_DESCRIPTION)));
                map.put("event_date",
                        c.getString(c.getColumnIndexOrThrow(COL_EVENT_DATE)));
                map.put("pdf_link",
                        c.getString(c.getColumnIndexOrThrow(COL_PDF_LINK)));
                map.put("venue",
                        c.getString(c.getColumnIndexOrThrow(COL_VENUE)));
                menuItems.add(map);
            } while (c.moveToNext());
        }
        c.close();
        return menuItems;
    }
    public static void delete_tbl_event_sync()
    {
        SQLiteDatabase db = IISERApp.db.getWritableDatabase();
        int numRows = db.delete(NAME, null, null);
       /* IISERApp.log(LOG_TAG,"In delete_tbl_exam_shedule_sync");
        IISERApp.log(LOG_TAG, "DeletedRows:->" + numRows);*/
    }
}
