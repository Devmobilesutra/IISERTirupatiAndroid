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
public class TABLE_ASSIGNMENT {
    public static String NAME = "tbl_assignment";
    public static String LOG_TAG="TABLE_ASSIGNMENT";

    public static String
            COL_ID = "id",
            COL_COURSE_ID = "course_id",
            COL_TITLE = "title",
            COL_DESCRIPTION = "description",
            COL_PDF_LINK = "pdf_link",
            COL_SUBMISSION_DATE = "submission_date",
            COL_IS_SEEN = "is_seen",
            COL_IMG_URL = "img_url";

    public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME + " ("
            + COL_ID + " INTEGER PRIMARY KEY,"
            + COL_COURSE_ID + " TEXT,"
            + COL_TITLE + " TEXT,"
            + COL_DESCRIPTION + " TEXT,"
            + COL_PDF_LINK + " TEXT,"
            + COL_SUBMISSION_DATE + " TEXT,"
            + COL_IS_SEEN + " TEXT,"
           +  COL_IMG_URL + " TEXT)";

    public static String QUERY_INSERT = "INSERT INTO " + NAME + " (" + COL_ID + "," + COL_COURSE_ID + "," + COL_TITLE + ","
            + COL_DESCRIPTION + "," + COL_PDF_LINK + "," + COL_SUBMISSION_DATE + "," + COL_IS_SEEN + ","+ COL_IMG_URL +") VALUES (?,?,?,?,?,?,?.?);";

    public static void InsertAssignment(String assign_id, String assign_title, String course_id, String description,
                                        String pdf_link,String submission_date,String is_seen,String img_url) {
        IISERApp.log(LOG_TAG, "enter in InsertAssignment function");
        SQLiteDatabase db = IISERApp.db.getWritableDatabase();

        String sql = "SELECT * FROM " + NAME + " where " + COL_ID + " =?" + " ;";
        Cursor c = db.rawQuery(sql, new String[]{assign_id});
        int num = c.getCount();
        ContentValues initialValues = new ContentValues();
        initialValues.put(COL_ID, assign_id.toString());
        initialValues.put(COL_TITLE, assign_title.toString());
        initialValues.put(COL_COURSE_ID, course_id.toString());
        initialValues.put(COL_DESCRIPTION, description.toString());
        initialValues.put(COL_PDF_LINK, pdf_link.toString());
        initialValues.put(COL_SUBMISSION_DATE, submission_date.toString());
        initialValues.put(COL_IS_SEEN, is_seen.toString());
        initialValues.put(COL_IMG_URL, img_url.toString());

        if (num > 0) {
            int numRows = db.update(NAME, initialValues, COL_ID + " =? ",
                    new String[]{assign_id});
            Log.d(LOG_TAG+"Update" + numRows, assign_id);
        } else {
            long rowId = db.insert(NAME, null, initialValues);
            Log.d(LOG_TAG+"Insrt" + rowId, assign_id);
        }
        c.close();

    }

    public static List<LinkedHashMap<String, String>> get_assignment_details(String course_id) {
        SQLiteDatabase db = IISERApp.db.getReadableDatabase();
        List<LinkedHashMap<String, String>> menuItems = new ArrayList();
        IISERApp.log(LOG_TAG, "In get assignment_detail function: " + course_id);
        String str1 = "select * from " + NAME +" where "+COL_COURSE_ID+"=? "+ " order by "+COL_SUBMISSION_DATE+" ;";
        Cursor c = db.rawQuery(str1, new String[]{course_id});

        if (c.moveToFirst()) {
            do {
                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                map.put("id",
                        c.getString(c.getColumnIndexOrThrow(COL_ID)));
                map.put("course_id",
                        c.getString(c.getColumnIndexOrThrow(COL_COURSE_ID)));
                map.put("assignment_title",
                        c.getString(c.getColumnIndexOrThrow(COL_TITLE)));
                map.put("description",
                        c.getString(c.getColumnIndexOrThrow(COL_DESCRIPTION)));
                map.put("pdf_link",
                        c.getString(c.getColumnIndexOrThrow(COL_PDF_LINK)));
                map.put("submission_date",
                        c.getString(c.getColumnIndexOrThrow(COL_SUBMISSION_DATE)));
                map.put("img_url",
                        c.getString(c.getColumnIndexOrThrow(COL_IMG_URL)));
                menuItems.add(map);
            } while (c.moveToNext());
        }

        IISERApp.log(LOG_TAG,"menuitems:"+menuItems);
        c.close();
        return menuItems;
    }

    public static void delete_tbl_assignment_sync()
    {
        SQLiteDatabase db = IISERApp.db.getWritableDatabase();
        int numRows = db.delete(NAME, null, null);
     //   IISERApp.log(LOG_TAG,"In delete_tbl_exam_shedule_sync");
     //   IISERApp.log(LOG_TAG, "DeletedRows:->" + numRows);
    }
}
