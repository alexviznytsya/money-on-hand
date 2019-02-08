/**
 * DailyCash.java
 *
 * Project #5, Client Application
 *
 * Author: Alex Viznytsya
 *
 * CS 478 Software Development for Mobile Platforms
 * Spring 2018, UIC
 *
 * Date: 05/01/2018
 */

package edu.uic.cs478.sp18.alexviznytsya.project5.service.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class DailyCash implements Parcelable {
    public int day = -1;
    public int month = -1;
    public int year = -1;
    public int cash = -1;
    public String weekDay = null;

    public DailyCash(int d, int m, int y, int c, String w) {
        this.day = d;
        this.month = m;
        this.year = y;
        this.cash = c;
        this.weekDay = w;
    }

    public DailyCash(Parcel in) {
        this.day = in.readInt() ;
        this.month = in.readInt() ;
        this.year = in.readInt() ;
        this.cash = in.readInt() ;
        this.weekDay = in.readString() ;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.day);
        out.writeInt(this.month) ;
        out.writeInt(this.year) ;
        out.writeInt(this.cash) ;
        out.writeString(this.weekDay) ;
    }

    public static final Creator<DailyCash> CREATOR
            = new Creator<DailyCash>() {

        public DailyCash createFromParcel(Parcel in) {
            return new DailyCash(in) ;
        }

        public DailyCash[] newArray(int size) {
            return new DailyCash[size];
        }
    };

    public int describeContents()  {
        return 0 ;
    }

}