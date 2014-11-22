package cse110.makingbank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.parse.ParseUser;

/**
 * Class: MainPage
 *
 * This is the home page for all events after a successful log in.
 */
public class AdminHomePage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home_page);
        // Hide action bar
        try {
            getActionBar().hide();
        }catch (Exception e){}
    }

    /**
     * Nullify the back button for this page
     */
    @Override
    public void onBackPressed(){}

    /**
     * Method accountLogout
     * Log out when the user presses the logout button
     */
    public void accountLogout(View view){
        ParseUser.logOut(); // Log out and go back to login page
        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
    }

    /**
     * Method doTransaction
     * Go to the account transaction page when this button is pressed
     */
    public void doTransaction(View view){
        Intent intent = new Intent (this, AdminTransactionPage.class);
        startActivity(intent);
    }
}
