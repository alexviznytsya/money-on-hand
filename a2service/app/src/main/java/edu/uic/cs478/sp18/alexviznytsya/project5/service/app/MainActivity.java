/**
 * MainActivity.java
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

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import edu.uic.cs478.sp18.alexviznytsya.project5.service.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
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
