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
public class TABLE_EXAM_SCHEDULE {
    public static String NAME = "tbl_exam_shedule",LOG_TAG="tbl_exam_shedule";

    public static String
            COL_ID = "id",
            COL_EXAM_TITLE = "exam_title",
            COL_COURSE_ID = "course_id",
            COL_CEC_MEMBER = "cec_member",
            COL_EXAM_DATE = "exam_date",
            COL_EXAM_TIME = "exam_time",
            COL_EXAM_DAY = "exam_day",
            COL_VENUE = "venue",
            COL_IS_SEEN = "is_seen";

    public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME + " ("
            + COL_ID + " INTEGER,"
            + COL_EXAM_TITLE + " TEXT,"
            + COL_COURSE_ID + " TEXT,"
            + COL_CEC_MEMBER + " TEXT,"
            + COL_EXAM_DATE + " TEXT,"
            + COL_EXAM_TIME + " TEXT,"
            + COL_EXAM_DAY + " TEXT,"
            + COL_VENUE + " TEXT,"
            + COL_IS_SEEN + " TEXT)";

    public static String QUERY_INSERT = "INSERT INTO " + NAME + " (" + COL_ID + "," + COL_EXAM_TITLE + "," + COL_COURSE_ID + "," + COL_CEC_MEMBER + ","
            + COL_EXAM_DATE + "," + COL_EXAM_TIME + "," + COL_EXAM_DAY + "," + COL_VENUE + "," + COL_IS_SEEN
            + ") VALUES (?,?,?,?,?,?,?,?,?);";


    public static void InsertExamShedule(String exam_id, String exam_title, String course_id, String exam_date, String exam_day,
                                         String exam_time, String venue, String cec_member, String is_seen) {

        IISERApp.log("TABLE_EXAMSCHEDULE", "In InsertExamShedule");
        Log.d("", "exam_id--->: "+exam_id);
        IISERApp.log("TABLE_EXAMSCHEDULE", "enter in InsertExamShedule function");
        SQLiteDatabase db = IISERApp.db.getWritableDatabase();
        String sql = "SELECT * FROM " + NAME + " where " + COL_ID + " =?" + " ;";
        Cursor c = db.rawQuery(sql, new String[]{exam_id});



        int num = c.getCount();
        Log.d("", "InsertExamShedule_count: "+c);
        ContentValues initialValues = new ContentValues();
        initialValues.put(COL_ID, exam_id.toString());
        initialValues.put(COL_EXAM_TITLE, exam_title.toString());
        initialValues.put(COL_COURSE_ID, course_id.toString());
        initialValues.put(COL_EXAM_DATE, exam_date.toString());
        initialValues.put(COL_EXAM_DAY, exam_day.toString());
        initialValues.put(COL_EXAM_TIME, exam_time.toString());
        initialValues.put(COL_VENUE, venue.toString());
        initialValues.put(COL_CEC_MEMBER, cec_member.toString());
        initialValues.put(COL_IS_SEEN, is_seen.toString());

        if (num > 0) {
            int numRows = db.update(NAME, initialValues, COL_ID + " =? ",
                    new String[]{exam_id});
            Log.d("UPDTABLE_EXMSCHEDULE:" + numRows, exam_id);
        } else {
            long rowId = db.insert(NAME, null, initialValues);
            Log.d("INSRTABLE_EXMSCHEDULE:" + rowId, exam_id);
        }
        c.close();

    }

    public static List<LinkedHashMap<String, String>> get_exam_schedule_list() {
        SQLiteDatabase db = IISERApp.db.getReadableDatabase();
        List<LinkedHashMap<String, String>> menuItems = new ArrayList();
        String str1 = "select * from " + NAME + " order by " + COL_EXAM_DATE + " ;";
        Cursor c = db.rawQuery(str1, null);
        IISERApp.log(LOG_TAG,"Count is :"+c.getCount());
        if(c.getCount()>0) {
            if (c.moveToFirst()) {
                do {
                    LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                    map.put("exam_id",
                            c.getString(c.getColumnIndexOrThrow(COL_ID)));
                    map.put("exam_title",
                            c.getString(c.getColumnIndexOrThrow(COL_EXAM_TITLE)));
                    map.put("course_id",
                            c.getString(c.getColumnIndexOrThrow(COL_COURSE_ID)));
                    map.put("cec_member",
                            c.getString(c.getColumnIndexOrThrow(COL_CEC_MEMBER)));
                    map.put("exam_date",
                            c.getString(c.getColumnIndexOrThrow(COL_EXAM_DATE)));
                    map.put("exam_time",
                            c.getString(c.getColumnIndexOrThrow(COL_EXAM_TIME)));
                    map.put("exam_day",
                            c.getString(c.getColumnIndexOrThrow(COL_EXAM_DAY)));
                    map.put("venue",
                            c.getString(c.getColumnIndexOrThrow(COL_VENUE)));
                    menuItems.add(map);
                } while (c.moveToNext());
            }
        }
        else
        {
            IISERApp.log(LOG_TAG,"N Records");

        }
        c.close();
        return menuItems;
    }

    public static void delete_tbl_exam_shedule_sync()
    {
        SQLiteDatabase db = IISERApp.db.getWritableDatabase();
        int numRows = db.delete(NAME, null, null);
        IISERApp.log(LOG_TAG,"In delete_tbl_exam_shedule_sync");
        IISERApp.log(LOG_TAG,"DeletedRows:->" + numRows);
    }

}
