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
public class TABLE_NOTICE {

    public static String NAME = "tbl_notice";
    public static String LOG_TAG = "TABLE_NOTICE";

    public static String
            COL_ID = "id",
            COL_NOTICE_TITLE = "notice_title",
            COL_DESCRIPTION = "description",
            COL_PDF_LINK = "pdf_link",
            COL_EXPIRY_DATE = "expiry_date",
            COL_IS_SEEN = "is_seen";

    public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME + " ("
            + COL_ID + " INTEGER PRIMARY KEY,"
            + COL_NOTICE_TITLE + " TEXT,"
            + COL_DESCRIPTION + " TEXT,"
            + COL_PDF_LINK + " TEXT,"
            + COL_EXPIRY_DATE + " TEXT,"
            + COL_IS_SEEN + " TEXT)";

    public static String QUERY_INSERT = "INSERT INTO " + NAME + " ("+ COL_ID + "," + COL_NOTICE_TITLE + ","+ COL_DESCRIPTION
            + "," + COL_PDF_LINK + "," + COL_EXPIRY_DATE + "," + COL_IS_SEEN + ") VALUES (?,?,?,?,?,?);";

    public static void InsertNotice(String id, String assign_title,String description,
                                        String pdf_link,String expiry_date,String is_seen) {
        IISERApp.log(LOG_TAG, "enter in InsertAssignment function");
        SQLiteDatabase db = IISERApp.db.getWritableDatabase();
///db.beginTransaction();
        String sql = "SELECT * FROM " + NAME + " where " + COL_ID + " =?" + " ;";
        //String sql =  " SELECT * FROM " + NAME  + " ORDER BY " + COL_DATE + " DESC ";
        Cursor c = db.rawQuery(sql, new String[]{id});
        int num = c.getCount();
        ContentValues initialValues = new ContentValues();
        initialValues.put(COL_ID, id.toString());
        initialValues.put(COL_NOTICE_TITLE, assign_title.toString());
        initialValues.put(COL_DESCRIPTION, description.toString());
        initialValues.put(COL_PDF_LINK, pdf_link.toString());
        initialValues.put(COL_EXPIRY_DATE, expiry_date.toString());
        initialValues.put(COL_IS_SEEN, is_seen.toString());

        if (num > 0) {
            int numRows = db.update(NAME, initialValues, COL_ID + " =? ",
                    new String[]{id});
            Log.d(LOG_TAG + "Update" + numRows, id);
        } else {
            long rowId = db.insert(NAME, null, initialValues);
            Log.d(LOG_TAG+"Insrt" + rowId, id);
        }
    //    db.setTransactionSuccessful();
    //    db.endTransaction();
        c.close();

    }
    public static List<LinkedHashMap<String, String>> get_notice_list() {
        SQLiteDatabase db = IISERApp.db.getReadableDatabase();
        List<LinkedHashMap<String, String>> menuItems = new ArrayList();
        //String str1 = "select * from " + NAME + " order by "+COL_EXPIRY_DATE+ " ;";
        String str1 = " SELECT * FROM " + NAME  + " ORDER BY " + COL_EXPIRY_DATE  + " ASC ";
        Cursor c = db.rawQuery(str1, null);

        if (c.moveToFirst()) {
            do {
                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                map.put("notice_id",
                        c.getString(c.getColumnIndexOrThrow(COL_ID)));
                map.put("notice_title",
                        c.getString(c.getColumnIndexOrThrow(COL_NOTICE_TITLE)));
                map.put("notice_description",
                        c.getString(c.getColumnIndexOrThrow(COL_DESCRIPTION)));
                map.put("pdf_link",
                        c.getString(c.getColumnIndexOrThrow(COL_PDF_LINK)));
                map.put("expiry_date",
                        c.getString(c.getColumnIndexOrThrow(COL_EXPIRY_DATE)));

                menuItems.add(map);
                IISERApp.log(LOG_TAG,"notice is : "+map);
                IISERApp.log(LOG_TAG,"notice is 1: "+menuItems);
            } while (c.moveToNext());

        }
        c.close();
        return menuItems;
    }

    public static void delete_notice() {
        SQLiteDatabase db = IISERApp.db.getWritableDatabase();
        int numRows = db.delete(NAME, null, null);
        IISERApp.log(LOG_TAG,"In delete_tbl_notice");
        IISERApp.log(LOG_TAG, "DeletedRows:->" + numRows);
    }
}
