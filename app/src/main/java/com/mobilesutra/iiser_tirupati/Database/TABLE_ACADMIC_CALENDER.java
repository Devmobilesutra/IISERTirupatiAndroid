package com.mobilesutra.iiser_tirupati.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Model.DTO_AcademicCalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by teJ on 2016-12-01.
 */
public class TABLE_ACADMIC_CALENDER {

    public static String NAME = "tbl_acadmic_calender";
    public static String LOG_TAG = "TABLE_ACADMIC_CALENDER";
/* "id": "1",
            "event_name": "Reporting of students after summer vacation",
            "date": "2016-07-31",
            "year": "2016-17"*/
    public static String
            COL_ID = "id",
            COL_EVENT_TITLE = "event_title",
            COL_DATE= "event_date",
            COL_YEAR= "year",
            COL_HOLIDAYS = "holiday";

    public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME + " ("
            + COL_ID + " INTEGER PRIMARY KEY,"
            + COL_EVENT_TITLE + " TEXT,"
            + COL_DATE + " TEXT,"
            + COL_YEAR + " TEXT,"
            + COL_HOLIDAYS + " TEXT)";

    public static String QUERY_INSERT= "INSERT INTO "+ NAME+"(" + COL_ID + "," + COL_EVENT_TITLE +"," + COL_DATE + "," +COL_YEAR + "," +COL_HOLIDAYS + ")" +
 " VALUES (?,?,?,?,?);";

    //public static String QUERY_UPDATE = "UPDATE " + NAME + " SET " + COL_IS_SELECTED + "='Y' " + " WHERE " + COL_ID + "=? ";

   /* public static String QUERY_INSERT = "INSERT INTO " + NAME + " (" + COL_ID + "," + COL_COURSE_NAME + ","
            + COL_SEMESTER_NAME + "," + COL_SUBJECT_NAME + "," + COL_CREDIT + "," + COL_IS_SELECTED + "," + COL_IS_OPTIONAL
            +","+COL_STUDENT_ID+ ")" +
            " VALUES (?,?,?,?,?,?,?,?);";*/


    public static void   parse_notice_data_and_insert(JSONArray m_jArry) {
        try
        {
            IISERApp.log(LOG_TAG, "Calender:" + m_jArry);
            SQLiteDatabase db = IISERApp.db.getWritableDatabase();


            String insert_sql = "insert into " +NAME + " ( "
                    + COL_ID + ", "
                    + COL_EVENT_TITLE + ", "
                    + COL_DATE + ", "
                    + COL_YEAR +  ","
                    +COL_HOLIDAYS +") " +
                    "VALUES(?,?,?,?,?)";


            IISERApp.log(LOG_TAG, "m_fram_details:" + m_jArry);
            db.beginTransaction();
            SQLiteStatement statement = db.compileStatement(insert_sql);

            for (int i = 0; i < m_jArry.length(); i++)
            {
                JSONObject j_obj = m_jArry.getJSONObject(i);
                String id = j_obj.getString("id");
                String event_name = j_obj.getString("event_name");
                String date = j_obj.getString("date");
                String year = j_obj.getString("year");
                String holiday=j_obj.getString("holiday");



                statement.clearBindings();
                statement.bindString(1, id);
                statement.bindString(2, event_name);
                statement.bindString(3, date);
                statement.bindString(4, year);
                statement.bindString(5, holiday);


                statement.execute();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            statement.close();

        } catch (JSONException e)
        {
            e.printStackTrace();
            IISERApp.log(LOG_TAG, "Hardcode exception is " + e.getMessage());
        }
    }
    public static List<DTO_AcademicCalendar> get_calender() {
        ArrayList<DTO_AcademicCalendar> arr_crop = new ArrayList<DTO_AcademicCalendar>();
        SQLiteDatabase db = IISERApp.db.getReadableDatabase();
        String querytogetuserdata="";

        querytogetuserdata = " SELECT * FROM " + NAME  + " ORDER BY " + COL_DATE + " ASC ";
       // querytogetuserdata = " SELECT * FROM " + NAME;
        IISERApp.log(LOG_TAG,"query->"+querytogetuserdata);
        Cursor c = db.rawQuery(querytogetuserdata,null);
        int count = c.getCount();
        IISERApp.log(LOG_TAG,"count->"+count);


        if (count > 0) {

            c.moveToFirst();
            do {

                String str_calendar_id=c.getString(c.getColumnIndexOrThrow(COL_ID));
                String str_calendar_title=c.getString(c.getColumnIndexOrThrow(COL_EVENT_TITLE));
                String str_calendar_date = c.getString(c.getColumnIndexOrThrow(COL_DATE));
                String year = c.getString(c.getColumnIndexOrThrow(COL_YEAR));
                String holiday = c.getString(c.getColumnIndexOrThrow(COL_HOLIDAYS));

                String str_calendar_weekday = IISERApp.get_weekday_from_date(str_calendar_date);
                String str_calendar_month = IISERApp.get_month_from_date(str_calendar_date);
                String str_calendar_weekdate = IISERApp.get_day_from_date(str_calendar_date);
                int day_cal = IISERApp.get_day_from_date_new(str_calendar_date);


                /*DTO_AcademicCalendar DTOAcademicCalendar = new DTO_AcademicCalendar(str_calendar_id, str_calendar_date,
                        str_calendar_month, str_calendar_weekday,
                        str_calendar_weekdate, str_calendar_title);
*/
                DTO_AcademicCalendar DTOAcademicCalendar = new DTO_AcademicCalendar(str_calendar_id, str_calendar_date,
                        str_calendar_month, str_calendar_weekday,
                        str_calendar_weekdate, str_calendar_title,holiday);

                arr_crop.add(DTOAcademicCalendar);

            } while (c.moveToNext());

        } else {
            IISERApp.log(LOG_TAG, "NO DATA");
        }

        if (c != null) {
            c.close();
        }
        return arr_crop;
    }
    public static void delete_tbl_aca_cal_sync()
    {
        SQLiteDatabase db = IISERApp.db.getWritableDatabase();
        int numRows = db.delete(NAME, null, null);
        IISERApp.log(LOG_TAG,"In delete_tbl_aca_calender");
        IISERApp.log(LOG_TAG, "DeletedRows:->" + numRows);
    }




}
