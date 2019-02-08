/**
 * SearchResults.java
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

package edu.uic.cs478.sp18.alexviznytsya.project5.client;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import edu.uic.cs478.sp18.alexviznytsya.project5.service.aidl.DailyCash;


public class SearchResults extends ListActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new CustomAdapter(this, getIntent().<DailyCash>getParcelableArrayListExtra("data")));
    }
}
