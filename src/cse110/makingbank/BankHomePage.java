package cse110.makingbank;


import android.app.Activity;
import android.os.Bundle;

/**
 * NOTE ABOUT THIS PAGE
 *
 * This page is to be swapped with the current MainPage.java after logging in is implemented.
 * For the time being, this is a placeholder page to implement a logging in function.
 */
public class BankHomePage extends Activity {


    /**
     * Method: onCreate
     * This method defines what happens when the object is created.
     * Brings up the options for account creation.
     *
     * @param savedInstanceState   a saved instance of the object
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }
}
