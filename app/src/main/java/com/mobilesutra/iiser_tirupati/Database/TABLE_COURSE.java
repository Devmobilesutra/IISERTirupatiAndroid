package com.mobilesutra.iiser_tirupati.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mobilesutra.iiser_tirupati.Config.IISERApp;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by KALYANI on 02/05/2016.
 */
public class TABLE_COURSE {
    public static String NAME = "tbl_course",LOG_TAG="TABLE_COURSE";

    public static String
            COL_ID = "id",
            COL_COURSE_NAME = "course_name",
            COL_SEMESTER_NAME = "semester_name",
            COL_SUBJECT_NAME = "subject_name",
            COL_CREDIT = "credit",
            COL_STUDENT_ID = "student_id",
            COL_IS_SELECTED = "is_selected",
            COL_IS_OPTIONAL = "is_optional";

    public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME + " ("
            + COL_ID + " TEXT ,"
            + COL_COURSE_NAME + " TEXT,"
            + COL_SEMESTER_NAME + " TEXT,"
            + COL_SUBJECT_NAME + " TEXT,"
            + COL_CREDIT + " TEXT,"
            + COL_STUDENT_ID + " TEXT,"
            + COL_IS_SELECTED + " TEXT,"
            + COL_IS_OPTIONAL + " TEXT)";

    public static String QUERY_INSERT = "INSERT INTO " + NAME + " (" + COL_ID + "," + COL_COURSE_NAME + ","
            + COL_SEMESTER_NAME + "," + COL_SUBJECT_NAME + "," + COL_CREDIT + "," + COL_IS_SELECTED + "," + COL_IS_OPTIONAL
            +","+ COL_STUDENT_ID+ ")" +
            " VALUES (?,?,?,?,?,?,?,?);";

    //public static String QUERY_UPDATE1 = "UPDATE " + NAME + "SET" +COL_IS_SELECTED + "= 'Y' ";

   public static String QUERY_UPDATE = " UPDATE " + NAME + " SET " + COL_IS_SELECTED + "='Y' " + " WHERE " + COL_ID + "=? ";
    //public static String UPDATE="SELECT * FROM " + TABLE_COURSE.NAME + "WHERE" +COL_IS_SELECTED + "='Y'";
    //public static String QUERY_UPDATE = "UPDATE " + NAME + " SET " + COL_IS_SELECTED + "='Y' " + " WHERE " + COL_ID + "=? ";

  //  public static String QUERY_UPDATE1 = " UPDATE " + NAME +" SET " +

  /*  public static String QUERY_INSERT1 = "INSERT INTO " + NAME + " ("+ COL_ID + ","  + COL_COURSE_NAME + ","
            + COL_SEMESTER_NAME+ "," + COL_SUBJECT_NAME + "," + COL_CREDIT + ","+ COL_IS_SELECTED + ","+ COL_IS_OPTIONAL  + ")" +
            " VALUES ('BIO312','Animal Physiology I','VI','Biology','4',?,?),('BIO321','Plant Biology I','VI','Biology','4',?,?);";
*/


    public static List<LinkedHashMap<String, String>> get_course_listnew(String courses_name)
    {
        IISERApp.log(LOG_TAG, "abc:- " + courses_name);

        SQLiteDatabase db = IISERApp.db.getReadableDatabase();
        List<LinkedHashMap<String, String>> menuItems = new ArrayList();
//        String str1 = "select * from " + NAME + " where "
//                + COL_SEMESTER_NAME + " =?  order by " + COL_SUBJECT_NAME + " ;";
        /*String str1 = "select * from " + NAME + " where "
                + COL_SEMESTER_NAME + " =?  order by " + COL_SUBJECT_NAME + " ;";
        Cursor c = db.rawQuery(str1,null);*/
       // String str1="select * from tbl_course where subject_name IN (select DISTINCT subject_name FROM tbl_course)";
       // String str1="select DISTINCT subject_name FROM tbl_course";
       //String str1 = "select  course_id, DISTINCT course_name, semester_name, subject_name, credit, is_selected from "+TABLE_COURSE.NAME ;
       // String str1="select DISTINCT COL_IS_SELECTED from "+TABLE_COURSE.NAME;
        //String str1 = "select  is_selected   from    tbl_course";
        //String str1
       /* String abc=" select distinct "+COL_SUBJECT_NAME+" from "+TABLE_COURSE.NAME +" where " + COL_SEMESTER_NAME + " = '"
                +courses_name+"'";/**/


       // String sql=" select distinct "+COL_SUBJECT_NAME+" from "+TABLE_COURSE.NAME;


        String sql=" select distinct "+COL_SUBJECT_NAME+ " from "+TABLE_COURSE.NAME+" where "+ COL_SEMESTER_NAME + " = '"+courses_name+"'";
        Cursor c = db.rawQuery(sql, null);
        //Cursor c= db.rawQuery(sql,null);
        IISERApp.log(LOG_TAG, "Query11 : " + sql);
        int count = c.getCount();
        IISERApp.log(LOG_TAG, "Count of records subject:- " + count);

        if(c!= null)
        {
            if (c.moveToFirst())
            {
                do
                {
                    String subject_name= c.getString(c.getColumnIndexOrThrow(COL_SUBJECT_NAME));
                    IISERApp.log(LOG_TAG, "Count of specific subject records:- " + subject_name);
                    String sql_details=" select * from "+TABLE_COURSE.NAME +" where " + COL_SUBJECT_NAME + " = '" +subject_name+"' and "+ COL_SEMESTER_NAME + " = '"
                        +courses_name+"'";

                 //   String sql_details=" select * from "+TABLE_COURSE.NAME ;

                    Cursor c1 = db.rawQuery(sql_details, null);
                    IISERApp.log(LOG_TAG, "Query : " + sql_details);
                    int count1 = c1.getCount();
                    IISERApp.log(LOG_TAG, "Count of courses records1:- " + c1);
                    IISERApp.log(LOG_TAG, "Count of courses records2:- " + count1);
                    int column =c1.getColumnCount();
                    String [] names = c1.getColumnNames();
                    IISERApp.log(LOG_TAG, "Count of courses records3:- " + column);
                    IISERApp.log(LOG_TAG, "Count of courses records4:- " + names);
                    if(c1 != null) {

                        if (c1.moveToFirst()) {
                            do {
                                IISERApp.log(LOG_TAG, "In do Array Menu Items11  : " + menuItems);
                                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                                map.put("course_id",
                                        c1.getString(c1.getColumnIndexOrThrow(COL_ID)));
                                map.put("course_name",
                                        c1.getString(c1.getColumnIndexOrThrow(COL_COURSE_NAME)));
                                map.put("semester_name",
                                        c1.getString(c1.getColumnIndexOrThrow(COL_SEMESTER_NAME)));
                                map.put("subject_name",
                                        c1.getString(c1.getColumnIndexOrThrow(COL_SUBJECT_NAME)));
                                map.put("credit",
                                        c1.getString(c1.getColumnIndexOrThrow(COL_CREDIT)));
                                map.put("is_selected",
                                        c1.getString(c1.getColumnIndexOrThrow(COL_IS_SELECTED)));
                                menuItems.add(map);
                            } while (c1.moveToNext());
                        }
                    }
                } while (c.moveToNext());
            }
            IISERApp.log("Cant ", "MAP:-" );
        }
        else {
            IISERApp.log("Cant ", "Return from db:");
        }
          //  Toast.makeText(TABLE_COURSE.this, "Can not fetch data",Toast.LENGTH_SHORT).show();
        IISERApp.log("database", "menuitems:" + menuItems);
        c.close();
        IISERApp.log("database", "menuitems:" + menuItems);
        return menuItems;
    }


  public static List<LinkedHashMap<String, String>> get_course_list(String courses_name) {
        SQLiteDatabase db = IISERApp.db.getReadableDatabase();
        List<LinkedHashMap<String, String>> menuItems = new ArrayList();
        String str1 = "select * from " + NAME + " where "
                + COL_COURSE_NAME + " =?  order by " + COL_SUBJECT_NAME + " ;";
        Cursor c = db.rawQuery(str1, new String[]{courses_name});
        //String str2 = "select * from " + NAME +  " ;";
        //Cursor c = db.rawQuery(str1, null);

        if (c.moveToFirst()) {
            do {
                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                map.put("course_id",
                        c.getString(c.getColumnIndexOrThrow(COL_ID)));
                map.put("course_name",
                        c.getString(c.getColumnIndexOrThrow(COL_COURSE_NAME)));
                map.put("semester_name",
                        c.getString(c.getColumnIndexOrThrow(COL_SEMESTER_NAME)));
                map.put("subject_name",
                        c.getString(c.getColumnIndexOrThrow(COL_SUBJECT_NAME)));
                map.put("credit",
                        c.getString(c.getColumnIndexOrThrow(COL_CREDIT)));
                menuItems.add(map);

            } while (c.moveToNext());
        }
        IISERApp.log("database", "menuitems:" + menuItems);
        c.close();
        return menuItems;
    }

    public static int get_selected_course_list() {
        SQLiteDatabase db = IISERApp.db.getReadableDatabase();

        String str1 = "select * from " + NAME + " where "
                + COL_IS_SELECTED + " ='Y' " + " ;";
        Cursor c = db.rawQuery(str1, null);
        c.getCount();
        IISERApp.log(LOG_TAG, "count of courses count:" + c.getCount());
        if(c.getCount() > 0)
            return 1;
        else
            return 0;



    }
    public static LinkedHashMap<String, String> get_selected_course() {
        SQLiteDatabase db = IISERApp.db.getReadableDatabase();

        String str1 = "select * from " + NAME + " where "
                + COL_IS_SELECTED + " ='Y' " + " ;";
        Cursor c = db.rawQuery(str1, null);
        c.getCount();
        IISERApp.log(LOG_TAG,"count of courses is:");


        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        if (c.moveToFirst()) {
            do {

                map.put(c.getString(c.getColumnIndexOrThrow(COL_ID)), c.getString(c.getColumnIndexOrThrow(COL_COURSE_NAME)));

            } while (c.moveToNext());
        }
        IISERApp.log("database", "menuitems:" + map);
     //   c.close();
        return map;
    }

    public static  int  get_selected_course_count() {

        IISERApp.log(LOG_TAG," in get_selected_course_count():");

        SQLiteDatabase db = IISERApp.db.getReadableDatabase();

        String str1 = "select * from " + NAME + " where "
                + COL_IS_SELECTED + " ='Y' " + " ;";
        Cursor c = db.rawQuery(str1, null);
        IISERApp.log(LOG_TAG, "count of courses is:");
        if (c.getCount() > 0)
        {
            IISERApp.log(LOG_TAG,"count of courses is:"+c.getCount());
            return 0;
        }
        else
        {
            IISERApp.log(LOG_TAG,"count of courses is:"+c.getCount());
            return  1;
        }



    }
    public static void insert_student_id_with_course(String student_id,String sem_name,String course_code,String course_name,
                                                     String subject_name,String credit,String str_is_selected)
    {
        IISERApp.log(LOG_TAG," in insert_student_id_with_course()"+student_id);
        SQLiteDatabase db=IISERApp.db.getWritableDatabase();

       /* String sql=" select * from "+TABLE_COURSE.NAME+" where "+COL_SEMESTER_NAME+" =? and "+COL_STUDENT_ID+"=? and "
                +COL_ID+" =?";*/




       /* String sql=" select * from "+TABLE_COURSE.NAME+" where "+COL_COURSE_NAME+"=? and "
                +COL_ID+" =? and "+COL_STUDENT_ID+"=?";

        IISERApp.log(LOG_TAG," sql:"+sql);
       // Cursor c=db.rawQuery(sql,new String[]{sem_name,student_id,course_code});
        Cursor c=db.rawQuery(sql, new String[]{course_name, course_code, student_id});
        int count=c.getCount();
        IISERApp.log(LOG_TAG, " count: for Insert " + count);*/
        ContentValues cv=new ContentValues();

        /*IISERApp.log(LOG_TAG, " value of str_is_selected:" + str_is_selected);
        cv.put(COL_STUDENT_ID, student_id);
        cv.put(COL_SEMESTER_NAME,sem_name);
        cv.put(COL_ID,course_code);
        cv.put(COL_COURSE_NAME,course_name);
        cv.put(COL_SUBJECT_NAME,subject_name);
        cv.put(COL_CREDIT,credit);
        if(str_is_selected.equalsIgnoreCase("null")) {
            cv.put(COL_IS_SELECTED, "N");
        }
        else{
            cv.put(COL_IS_SELECTED, str_is_selected);
        }*/
        cv.put(COL_STUDENT_ID, student_id);

        cv.put(COL_SEMESTER_NAME,sem_name);
        cv.put(COL_ID,course_code);
        cv.put(COL_COURSE_NAME,course_name);
        cv.put(COL_SUBJECT_NAME,subject_name);
        cv.put(COL_CREDIT,credit);
        cv.put(COL_IS_SELECTED, str_is_selected);



        /*if(count>0)
        {
          *//*  int count_up=db.update(TABLE_COURSE.NAME,cv,COL_SEMESTER_NAME+" =? and "+COL_STUDENT_ID+"=? and "
                    +COL_ID+" =?",new String[]{sem_name,student_id,course_code});*//*

            int count_up=db.update(TABLE_COURSE.NAME,cv,COL_COURSE_NAME+"=? and "
                    +COL_ID+" =? and "+COL_STUDENT_ID+"=? ",new String[]{course_name,course_code,student_id});
            IISERApp.log(LOG_TAG, " count_up:" + count_up);
            IISERApp.log(LOG_TAG, "Vishnu1:" + count_up);

        }else
        {*/
            long count_in=db.insert(TABLE_COURSE.NAME,null,cv);
            IISERApp.log(LOG_TAG, "Insert Originall count_in:" + count_in);
            IISERApp.log(LOG_TAG, "Vishnu2:" + count_in);
        //}
        //c.close();
    }


    public static void updateSelectedCourse(String str_flag)
    {
        try
        {
            ContentValues cv=new ContentValues();
            SQLiteDatabase db=IISERApp.db.getWritableDatabase();
            cv.put(COL_IS_SELECTED, str_flag);
            int count_up=db.update(TABLE_COURSE.NAME,cv,null,null);
            IISERApp.log(LOG_TAG,"courses updated successfully"+count_up);
        }catch (Exception e)
        {
            IISERApp.log(LOG_TAG,"Error->"+e);
        }


    }

    public static void updateSelectedCourse(String student_id,String course_id)
    {
        IISERApp.log(LOG_TAG,"in updateSelectedCourse");
        try
        {
            ContentValues cv=new ContentValues();
            SQLiteDatabase db=IISERApp.db.getWritableDatabase();
            cv.put(COL_IS_SELECTED, "Y");
            int count_up=db.update(TABLE_COURSE.NAME,cv,COL_STUDENT_ID + "=? AND "+ COL_ID + "=?" ,new String[]{student_id,course_id});

            IISERApp.log(LOG_TAG,"courses updated successfully"+count_up);
        }catch (Exception e)
        {
            IISERApp.log(LOG_TAG,"Error->"+e);
        }




    }





//    public static void newupdateSelectedCourse(String str_flag)
//    {
//        try
//        {
//            ContentValues cv=new ContentValues();
//            SQLiteDatabase db=IISERApp.db.getWritableDatabase();
//            cv.put(COL_IS_SELECTED, str_flag="Y");
//            int count_up=db.update(TABLE_COURSE.NAME,cv,COL_ID+" =? ",new String[]{str_flag});
//
//            IISERApp.log(LOG_TAG," new courses updated successfully"+count_up);
//
//        }catch (Exception e)
//        {
//            IISERApp.log(LOG_TAG,"Error1->"+e);
//        }
//
//    }
public static void delete_tbl_course()
{
    SQLiteDatabase db = IISERApp.db.getWritableDatabase();
    int numRows = db.delete(NAME, null, null);
    IISERApp.log(LOG_TAG,"In delete_tbl_course_sync");
    IISERApp.log(LOG_TAG, "DeletedRows:->" + numRows);
}

}
