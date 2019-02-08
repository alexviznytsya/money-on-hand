/**
 * LocalDB.aidl
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

import edu.uic.cs478.sp18.alexviznytsya.project5.service.aidl.DailyCash;

interface LocalDB {

    boolean createDatabase();

    List<DailyCash> dailyCash(int d, int m, int y, int nwd);

}
