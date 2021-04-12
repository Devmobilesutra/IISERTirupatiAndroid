package com.mobilesutra.iiser_tirupati.Database;

/**
 * Created by kalyani on 02/05/2016.
 */
public class TABLE_BANNER {
    public static String NAME = "tbl_banner";

    public static String
            COL_ID = "id",
            COL_BANNER_LINK = "banner_link";

    public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_BANNER_LINK + " TEXT)";


    public static String QUERY_INSERT = "INSERT INTO " + NAME + " (" + COL_BANNER_LINK+") VALUES (?);";


}
