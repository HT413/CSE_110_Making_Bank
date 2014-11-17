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
public class BankHomePage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Check if this is the admin. If yes, go to admin page
        if (ParseUser.getCurrentUser().getUsername().equals("admin")){
            Intent intent = new Intent (this, AdminHomePage.class);
            startActivity(intent);
        }

        // Otherwise, load normal page
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
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
     * Function: goViewAccount
     * This function switches the activity to the view account activity
     * 
     * @param view The View object from which this method was called
     */
    public void goViewAccount(View view) {
        Intent intent = new Intent(this, ViewAccountPage.class);
        startActivity(intent);
    }
    /**
     * Function: createAccount
     * This function defines what happens when the user taps on the
     * Create Account button in our main menu.
     *
     * @param view The View object from which this method was called
     */
    public void createAccount(View view){
        //We will transition to this new intent
        Intent intent = new Intent(this, CreateAccountPage.class);
        startActivity(intent);
    }

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
     * Method: changeInfo
     * This function defines what happens when the user taps on the
     * change info button in our main menu
     */
    public void goEditInfo(View view){
        Intent intent = new Intent (this, MyInfo.class);
        startActivity(intent);
    }
}
