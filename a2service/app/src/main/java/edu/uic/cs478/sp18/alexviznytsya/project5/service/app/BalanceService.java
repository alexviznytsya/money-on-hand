/**
 * BalanceService.java
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

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.uic.cs478.sp18.alexviznytsya.project5.service.aidl.DailyCash;
import edu.uic.cs478.sp18.alexviznytsya.project5.service.aidl.LocalDB;

public class BalanceService extends Service {

    private DBHelper dbHelper = null;
    private List<DailyCash> dailyCash = new ArrayList<DailyCash>();

    private LocalDB.Stub mBinder = new LocalDB.Stub() {
        @Override
        public synchronized boolean createDatabase() throws RemoteException {
            BalanceService.this.dbHelper = new DBHelper(BalanceService.this);
            BalanceService.this.dbHelper.deleteDatabase();
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(
                        new InputStreamReader(BalanceService.this.getAssets().open(
                                "treasury-io-final.txt"), "UTF-8"));
                String fileLine = null;
                String sql = null;
                while ((fileLine = reader.readLine()) != null) {
                    String[] separated = fileLine.split(",");
                    sql = "INSERT INTO "+ DBHelper.TABLE_NAME + " (" + DBHelper.TABLE_COLUMNS[0] + ", " +
                    DBHelper.TABLE_COLUMNS[1] + ", " +
                    DBHelper.TABLE_COLUMNS[2] + ", " +
                    DBHelper.TABLE_COLUMNS[3] + ", " +
                    DBHelper.TABLE_COLUMNS[4] + ", " +
                    DBHelper.TABLE_COLUMNS[5] + ", " +
                    DBHelper.TABLE_COLUMNS[6] + ") VALUES (NULL, " +
                            separated[0] + ", " + separated[1] + ", " +
                            separated[2] + ", '" + separated[3] + "', " +
                            separated[4] + ", " + separated[5] + ");";
                    BalanceService.this.dbHelper.getWritableDatabase().execSQL(sql);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }

            return true;
        }

        @Override
        public synchronized List<DailyCash> dailyCash(int d, int m, int y, int nwd) throws RemoteException {

            BalanceService.this.dailyCash.clear();

            String sql = "SELECT * FROM " + DBHelper.TABLE_NAME + " WHERE " +
                    DBHelper.TABLE_COLUMNS[1] + " = ? AND " + DBHelper.TABLE_COLUMNS[2] + "= ? " +
                    "AND " + DBHelper.TABLE_COLUMNS[3] + " = ? ";

            Cursor validEntryCursor = BalanceService.this.dbHelper.getReadableDatabase().rawQuery(sql,
                    new String[] {String.valueOf(y), String.valueOf(m), String.valueOf(d)});

            validEntryCursor.moveToFirst();

            while(validEntryCursor.getCount() == 0) {
                d++;
                if(m == 2 && d > 28) {
                    m += 1;
                    d = 1;
                } else if(d > 30 && (m == 4 || m == 6 || m == 9 || m == 11)) {
                    m += 1;
                    d = 1;
                } else if(d > 31){
                   m += 1;
                   d = 1;
                }
                if(m > 12) {
                    m = 1;
                    d = 1;
                    y += 1;
                }
                if(y > 2018) {
                    return BalanceService.this.dailyCash;
                }
                validEntryCursor = BalanceService.this.dbHelper.getReadableDatabase().rawQuery(sql,
                        new String[] {String.valueOf(y), String.valueOf(m), String.valueOf(d)});
                validEntryCursor.moveToFirst();
            }

            Cursor cursor = null;
            String sql2 = "SELECT * FROM " + DBHelper.TABLE_NAME + " WHERE " +
                    DBHelper.TABLE_COLUMNS[0] + " >= ? LIMIT ?";

            cursor = BalanceService.this.dbHelper.getReadableDatabase().rawQuery(sql2,
                    new String[] {validEntryCursor.getString(0), String.valueOf(nwd)});
            cursor.moveToFirst();

            for(int i = 0; i < cursor.getCount(); i++) {
                BalanceService.this.dailyCash.add(new DailyCash(cursor.getInt(3),
                        cursor.getInt(2), cursor.getInt(1),
                                cursor.getInt(6), cursor.getString(4)));
                cursor.moveToNext();
            }
            validEntryCursor.close();
            cursor.close();

            return BalanceService.this.dailyCash;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }
}
