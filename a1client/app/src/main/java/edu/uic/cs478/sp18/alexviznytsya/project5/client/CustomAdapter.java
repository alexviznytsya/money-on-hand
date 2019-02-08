/**
 * CustomAdapter.java
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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.uic.cs478.sp18.alexviznytsya.project5.service.aidl.DailyCash;

public class CustomAdapter extends ArrayAdapter {

    ArrayList<DailyCash> dailyCash = null;

    public CustomAdapter(Context context, ArrayList<DailyCash> dailyCash) {
        super(context, 0, dailyCash);
        this.dailyCash = dailyCash;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_layout, parent, false);
        }

        TextView dateTxt = (TextView) view.findViewById(R.id.dateTxt);
        TextView moneyTxt = (TextView) view.findViewById(R.id.moneyTxt);

        DailyCash dc = dailyCash.get(position);

        dateTxt.setText(dc.weekDay + " " + dc.month + "/" + dc.day + "/" + dc.year);
        moneyTxt.setText("Money (in millions): " + dc.cash);
        return view;
    }
}
