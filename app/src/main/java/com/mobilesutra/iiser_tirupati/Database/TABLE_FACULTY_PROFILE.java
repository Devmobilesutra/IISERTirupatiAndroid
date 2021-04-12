package com.mobilesutra.iiser_tirupati.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Model.DTO_Faculty_Profile;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by KALYANI on 02/05/2016.
 */
public class TABLE_FACULTY_PROFILE {
    public static String NAME = "tbl_faculty_profile";
    public static String LOG_TAG = "TABLE_FACULTY_PROFILE";
    public static String
            COL_ID = "id",
            COL_NAME = "name",
            COL_DEGREE = "degree",
            COL_EMAIL_ID = "email_id",
            COL_MOBILE_NO = "mobile_no",
            COL_DESIGNATION = "designation",
            COL_SUBJECT = "subject",
            COL_RESEARCH = "research",
            COL_PHOTO_URL = "photo_url",
            COL_PERSONAL_PAGE = "personal_page",
            COL_USER_TYPE = "user_type",
            COL_IS_SYNC = "is_sync";

    public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME + " ("
            + COL_ID + " INTEGER PRIMARY KEY,"
            + COL_NAME + " TEXT,"
            + COL_DEGREE + " TEXT,"
            + COL_EMAIL_ID + " TEXT,"
            + COL_MOBILE_NO + " TEXT,"
            + COL_DESIGNATION + " TEXT,"
            + COL_SUBJECT + " TEXT,"
            + COL_RESEARCH + " TEXT,"
            + COL_PHOTO_URL + " TEXT,"
            + COL_PERSONAL_PAGE + " TEXT,"
            + COL_USER_TYPE + " TEXT,"
            + COL_IS_SYNC + " TEXT)";

    public static String QUERY_INSERT = "INSERT INTO " + NAME + " (" + COL_ID + ","
            + COL_NAME + ","
            + COL_DEGREE + ","
            + COL_EMAIL_ID + ","
            + COL_MOBILE_NO + ","
            + COL_DESIGNATION + ","
            + COL_SUBJECT + ","
            + COL_RESEARCH + ","
            + COL_PHOTO_URL + ","
            + COL_PERSONAL_PAGE + ","
            + COL_USER_TYPE + ","
            + COL_IS_SYNC + ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";


    public static List<LinkedHashMap<String, String>> get_faculty_details() {
        SQLiteDatabase db = IISERApp.db.getReadableDatabase();
        List<LinkedHashMap<String, String>> menuItems = new ArrayList();
        String str1 = "select * from " + NAME + " ;";
        Cursor c = db.rawQuery(str1, null);

        if (c.moveToFirst()) {
            do {
                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                map.put("user_id",
                        c.getString(c.getColumnIndexOrThrow(COL_ID)));
                map.put("faculty_name",
                        c.getString(c.getColumnIndexOrThrow(COL_NAME)));
                map.put("degree",
                        c.getString(c.getColumnIndexOrThrow(COL_DEGREE)));
                map.put("designation",
                        c.getString(c.getColumnIndexOrThrow(COL_DESIGNATION)));
                map.put("research",
                        c.getString(c.getColumnIndexOrThrow(COL_RESEARCH)));
                map.put("email_id",
                        c.getString(c.getColumnIndexOrThrow(COL_EMAIL_ID)));
                map.put("mobile_no",
                        c.getString(c.getColumnIndexOrThrow(COL_MOBILE_NO)));
                map.put("photo_url",
                        c.getString(c.getColumnIndexOrThrow(COL_PHOTO_URL)));
                map.put("personal_page",
                        c.getString(c.getColumnIndexOrThrow(COL_PERSONAL_PAGE)));
                menuItems.add(map);
            } while (c.moveToNext());
        }
        c.close();
        return menuItems;
    }

    public static List<DTO_Faculty_Profile> get_faculty_details_with_Discipline(String str_discipline) {
        SQLiteDatabase db = IISERApp.db.getReadableDatabase();
        List<DTO_Faculty_Profile> menuItems = new ArrayList();
        String str1 = " SELECT * FROM " + NAME + " where " + COL_SUBJECT + "='" + str_discipline + "'";
        // String str1 = " SELECT * FROM " + NAME;
        IISERApp.log(LOG_TAG, "get_faculty_details_with_Discipline(), QUERY->" + str1);
        Cursor c = db.rawQuery(str1, null);
        int count = c.getCount();
        IISERApp.log(LOG_TAG, "get_faculty_details_with_Discipline(), count->" + count);
        if (count > 0) {
            if (c.moveToFirst()) {
                do {
                    LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                    String faculty_id = c.getString(c.getColumnIndexOrThrow(COL_ID));
                    String faculty_name = c.getString(c.getColumnIndexOrThrow(COL_NAME));
                    String faculty_degree = c.getString(c.getColumnIndexOrThrow(COL_DEGREE));
                    String faculty_designation = c.getString(c.getColumnIndexOrThrow(COL_DESIGNATION));
                    String faculty_research = c.getString(c.getColumnIndexOrThrow(COL_RESEARCH));
                    String faculty_email_id = c.getString(c.getColumnIndexOrThrow(COL_EMAIL_ID));
                    String faculty_mobile_no = c.getString(c.getColumnIndexOrThrow(COL_MOBILE_NO));
                    String faculty_photo_url = c.getString(c.getColumnIndexOrThrow(COL_PHOTO_URL));
                    String faculty_personal_page = c.getString(c.getColumnIndexOrThrow(COL_PERSONAL_PAGE));

                    DTO_Faculty_Profile DTOFacultyProfile = new DTO_Faculty_Profile(faculty_id, faculty_name, faculty_degree
                            , faculty_designation, faculty_research, faculty_email_id, faculty_mobile_no, faculty_photo_url,
                            faculty_personal_page);
                    menuItems.add(DTOFacultyProfile);
                } while (c.moveToNext());
            }
            c.close();
            return menuItems;
        } else {
            return menuItems;
        }
    }

    public static void delete_tbl_faculty_profile() {
        SQLiteDatabase db = IISERApp.db.getWritableDatabase();
        int numRows = db.delete(NAME, null, null);
        IISERApp.log(LOG_TAG,"delete_tbl_faculty_profile(),  delete_tblcal");
        IISERApp.log(LOG_TAG,"delete_tbl_faculty_profile(), DeletedRows:->" + numRows);
    }
}
