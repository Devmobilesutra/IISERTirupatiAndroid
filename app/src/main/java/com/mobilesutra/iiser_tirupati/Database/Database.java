package com.mobilesutra.iiser_tirupati.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mobilesutra.iiser_tirupati.Config.IISERApp;

/**
 * Created by kalyani on 30/04/2016.
 */
public class
Database extends SQLiteOpenHelper{
static Context  context1=null;
    public static String LOG_TAG = "Database";
   // static final int DATABASE_VERSION = 1;
    public Database(Context context)
    {

        super(context,TABLE.DATABASE_NAME,null,TABLE.DATABASE_VERSION);
        context1 = context;
        IISERApp.log(LOG_TAG,"In database constructor");

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        IISERApp.log(LOG_TAG, "In onCreate");
        try
        {
            db.execSQL(TABLE_COURSE.CREATE_TABLE);
            db.execSQL(TABLE_USER_PROFILE.CREATE_TABLE);
            db.execSQL(TABLE_FACULTY_PROFILE.CREATE_TABLE);
            db.execSQL(TABLE_ASSIGNMENT.CREATE_TABLE);
            db.execSQL(TABLE_EXAM_SCHEDULE.CREATE_TABLE);
            db.execSQL(TABLE_EXAM_SUPERVISOR.CREATE_TABLE);
            db.execSQL(TABLE_TIMETABLE.CREATE_TABLE);
            db.execSQL(TABLE_NOTICE.CREATE_TABLE);
            db.execSQL(TABLE_CALENDAR.CREATE_TABLE);
            db.execSQL(TABLE_FACULTY_CALENDAR.CREATE_TABLE);
            db.execSQL(TABLE_EVENT.CREATE_TABLE);
            db.execSQL(TABLE_BANNER.CREATE_TABLE);
            db.execSQL(TABLE_WEBSERVICE.CREATE_TABLE);
            db.execSQL(TABLE_ACADMIC_CALENDER.CREATE_TABLE);
            db.execSQL(TABLE_ATTENDENCE_MASTER.CREATE_TABLE);
            db.execSQL(TABLE_STUDENT_ATTENDENCE.CREATE_TABLE);
            db.execSQL(TABLE_FACULTY_ATTENDENCE.CREATE_TABLE);
            // db.execSQL(TABLE_USER_PROFILE.QUERY_INSERT1);
            // db.execSQL(TABLE_COURSE.QUERY_INSERT1);
            IISERApp.set_session(IISERApp.SESSION_USER_TYPE,"student");
            //IISERApp.set_session(IISERApp.SESSION_USER_TYPE,"faculty");

        }catch (Exception e)
        {
            IISERApp.log(LOG_TAG,"DB ERROR->"+e);
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            /*if (newVersion > oldVersion) {
                db.execSQL("ALTER TABLE "+TABLE_ASSIGNMENT.NAME+" ADD COLUMN "+TABLE_SUBJECTS.COL_IMG_URL+" TEXT NOT NULL DEFAULT '' ");
                db.execSQL(TABLE_NOTIFICATION.CREATE_TABLE);
            }*/
        }



    // -------------START Export Database Functions---------------------//
    public String[] getTableNames() {
        // TODO Auto-generated method stub
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        int tableCount = c.getCount();
        Log.i("CursorCount", tableCount + "");

        String tableNames[] = new String[tableCount];
        int i = 0;
        if (tableCount > 0) {
            c.moveToFirst();
            do {
                Log.i("TableName", c.getString(0));
                tableNames[i++] = c.getString(0);
            } while (c.moveToNext());

        }
        c.close();
        return tableNames;
    }

    public int deleteTables(String tableNames)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "delete * from "+tableNames;
          db.execSQL(sql);

       /* context1.deleteDatabase("TABLE_USER_PROFILE");
        context1.deleteDatabase("TABLE");
*/
        //String[] columnNames = dbCursor.getColumnNames();
        //dbCursor.close();
        return 1;
    }

    public String[] getTableColumnNames(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor dbCursor = db.query(tableName, null, null, null, null, null,
                null);
        String[] columnNames = dbCursor.getColumnNames();
        dbCursor.close();
        return columnNames;
    }

    public String getDataFromTable(String tableName) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor dbCursor = db.query(tableName, null, null, null, null, null,
                null);
        String data = "";
        Log.i("ColumnCount", dbCursor.getCount() + "");

        String[] columnNames = dbCursor.getColumnNames();
        int columnCount = columnNames.length;
        for (int i = 0; i < columnCount; i++) {
            Log.i("Column-" + i, columnNames[i]);
        }

        if (columnCount > 0) {
            Cursor cT = db.rawQuery("SELECT * FROM " + tableName, null);
            int recordCount = cT.getCount();
            Log.i("RecordCount", recordCount + "");

            if (recordCount > 0) {
                cT.moveToFirst();
                do {
                    for (int i = 0; i < columnCount; i++) {
                        try {
                            data += cT.getString(i).toString()
                                    .replace(",", "~+").toString()
                                    + ",";
                            Log.i("Column-" + columnNames[i],
                                    "Value-" + cT.getString(i));
                        } catch (Exception e) {
                            data += e.getLocalizedMessage() + "";
                        }
                    }
                    data = data.substring(0, data.length() - 1);
                    data += "\n";
                } while (cT.moveToNext());
            }
            cT.close();
        }
        dbCursor.close();
        return data;
    }

    // -------------END Export Database Functions---------------------//
}
