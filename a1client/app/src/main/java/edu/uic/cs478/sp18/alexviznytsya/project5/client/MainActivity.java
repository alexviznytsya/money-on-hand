/**
 * MainActivity.java
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

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.uic.cs478.sp18.alexviznytsya.project5.service.aidl.DailyCash;
import edu.uic.cs478.sp18.alexviznytsya.project5.service.aidl.LocalDB;

public class MainActivity extends AppCompatActivity {


    private TextView yearTxt, monthTxt, dayTxt, numDaysTxt;
    private TextView yearErrTxt, monthErrTxt, dayErrTxt, numDaysErrTxt;
    private Button createBtn, queryBtn;

    private int year, month, day, numDays;

    private LocalDB localDB;
    private boolean sIsBound = false;
    private boolean isConnected = false;

    private final ServiceConnection sConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder iservice) {
            MainActivity.this.localDB = LocalDB.Stub.asInterface(iservice);
            MainActivity.this.sIsBound = true;
        }

        public void onServiceDisconnected(ComponentName className) {
            localDB = null;
            sIsBound = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.year = 0;
        this.month = 0;
        this.day = 0;
        this.numDays = 0;

        this.yearTxt = (TextView) findViewById(R.id.yearTxt);
        this.monthTxt = (TextView) findViewById(R.id.monthTxt);
        this.dayTxt = (TextView) findViewById(R.id.dayTxt);
        this.numDaysTxt = (TextView) findViewById(R.id.numDaysTxt);

        this.yearErrTxt = (TextView) findViewById(R.id.yearErrTxt);
        this.monthErrTxt = (TextView) findViewById(R.id.monthErrTxt);
        this.dayErrTxt = (TextView) findViewById(R.id.dayErrTxt);
        this.numDaysErrTxt = (TextView) findViewById(R.id.numDaysErrTxt);

        this.createBtn = (Button) findViewById(R.id.createBtn);
        this.queryBtn = (Button) findViewById(R.id.queryBtn);

        if (!sIsBound) {
            Intent i = new Intent(LocalDB.class.getName());
            ResolveInfo info = getPackageManager().resolveService(i, 0);
            i.setComponent(new ComponentName(info.serviceInfo.packageName, info.serviceInfo.name));
            bindService(i, this.sConnection, Context.BIND_AUTO_CREATE);
        }

        this.createBtn.setEnabled(true);

        this.createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean dbCreateResult = false;

                try {
                    dbCreateResult = MainActivity.this.localDB.createDatabase();
                    MainActivity.this.isConnected = true;
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                if(dbCreateResult == true) {
                    Toast.makeText(MainActivity.this, "Database has been created successfully.", Toast.LENGTH_SHORT).show();
                    MainActivity.this.createBtn.setEnabled(false);
                } else {
                    Toast.makeText(MainActivity.this, "Error: Cannot create database.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        this.queryBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ArrayList<DailyCash> response = null;
                if(MainActivity.this.isConnected == false) {
                    Toast.makeText(MainActivity.this, "Please create database.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(MainActivity.this.validateInput() == true) {

                    try {
                        response = new ArrayList<DailyCash>(MainActivity.this.localDB.dailyCash(MainActivity.this.day,
                                MainActivity.this.month, MainActivity.this.year, MainActivity.this.numDays));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Cannot send request.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please check your input.", Toast.LENGTH_SHORT).show();
                }

                if(response != null && response.size() == 0) {
                    Toast.makeText(MainActivity.this, "No data for selected date available.", Toast.LENGTH_SHORT).show();
                } else if(response != null){
                    Intent i = new Intent(MainActivity.this, SearchResults.class);
                    i.putParcelableArrayListExtra("data", response);
                    startActivity(i);
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        if (sIsBound) {
            unbindService(this.sConnection);
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mmAbout:
                openAboutDialog();
                return true;
            case R.id.mmExit:
                finishAndRemoveTask();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //
    // Clear error fields:
    //
    private void clearErrors() {
        this.yearErrTxt.setText("");
        this.monthErrTxt.setText("");
        this.dayErrTxt.setText("");
        this.numDaysErrTxt.setText("");
    }

    //
    // Validate input:
    //
    private boolean validateInput() {

        boolean isValid = true;

        this.clearErrors();

        if(this.yearTxt.getText().length() > 0) {
            this.year = Integer.parseInt(this.yearTxt.getText().toString());
        } else {
            this.yearErrTxt.setText("Field cannot be empty.");
            isValid = false;
        }

        if(this.monthTxt.getText().length() > 0) {
            this.month = Integer.parseInt("" + this.monthTxt.getText().toString());
        } else {
            this.monthErrTxt.setText("Field cannot be empty.");
            isValid = false;
        }

        if(this.dayTxt.getText().length() > 0) {
            this.day = Integer.parseInt("" + this.dayTxt.getText().toString());
        } else {
            this.dayErrTxt.setText("Field cannot be empty.");
            isValid = false;
        }

        if(this.numDaysTxt.getText().length() > 0) {
            this.numDays = Integer.parseInt("" + this.numDaysTxt.getText().toString());
        } else {
            this.numDaysErrTxt.setText("Field cannot be empty.");
            isValid = false;
        }

        if(this.year < 2017 || this.year > 2018) {
            this.yearErrTxt.setText("2017 or 2018");
            isValid = false;
        }

        if(this.month < 1 || this.month > 12){
            this.monthErrTxt.setText("1 ... 12");
            isValid = false;
        }

        if(this.day < 1 || this.day > 28){
            if(this.month == 2 && this.day > 28) {
                this.dayErrTxt.setText("1 ... 28");
                isValid = false;
            } else if(this.day > 30 && (this.month == 4 || this.month == 6 || this.month == 9 || this.month == 11)) {
                this.dayErrTxt.setText("1 ... 30");
                isValid = false;
            } else if(this.day > 31){
                this.dayErrTxt.setText("1 ... 31");
                isValid = false;
            }
        }

        if(this.numDays < 1 || this.numDays > 30 ) {
            this.numDaysErrTxt.setText("1 .. 30");
            isValid = false;
        }

        return isValid;
    }

    //
    // About dialog popup:
    //
    private void openAboutDialog() {
        final View dialogLayout = getLayoutInflater().inflate(R.layout.dialog_about, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogLayout);
        final AlertDialog dialogAbout = dialogBuilder.create();

        // Add "Cancel" button listener
        ((Button)dialogLayout.findViewById(R.id.dialogClose)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAbout.dismiss();
            }
        });

        dialogAbout.show();
    }
}
