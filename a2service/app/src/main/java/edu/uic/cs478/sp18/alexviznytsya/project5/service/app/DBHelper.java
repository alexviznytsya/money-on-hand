/**
 * DBHelper.java
 *
 * Project #5, Service Application
 *
 * Author: Alex Viznytsya
 *
 * CS 478 Software Development for Mobile Platforms
 * Spring 2018, UIC
 *
 * Date: 05/01/2018
 */

package edu.uic.cs478.sp18.alexviznytsya.project5.service.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    final private static String DB_NAME = "federal_money_db";
    final private static int DB_VERSION = 1;

    final public static String TABLE_NAME = "federal_money";
    final public static String[] TABLE_COLUMNS = {"id", "year", "month", "day", "dayOfWeek",
                                                    "moneyOpenDay", "moneyCloseDay"};

    final private String CREATE_CMD = "CREATE TABLE " + TABLE_NAME + " (" +
        TABLE_COLUMNS[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        TABLE_COLUMNS[1] + " INTEGER NOT NULL, " +
        TABLE_COLUMNS[2] + " INTEGER NOT NULL, " +
        TABLE_COLUMNS[3] + " INTEGER NOT NULL, " +
        TABLE_COLUMNS[4] + " TEXT NOT NULL, " +
        TABLE_COLUMNS[5] + " INTEGER NOT NULL, " +
        TABLE_COLUMNS[6] + " INTEGER NOT NULL)";

    final private Context mContext;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("DBHelper", CREATE_CMD);
        db.execSQL(CREATE_CMD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    void deleteDatabase() {
        mContext.deleteDatabase(DB_NAME);
    }
}
