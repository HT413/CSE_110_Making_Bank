package cse110.makingbank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.parse.ParseUser;

/**
 * Class: MainPage
 * This is the home page for all events after a successful log in.
 */
public class BankHomePage extends Activity {
    private int tapIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Otherwise, load normal page
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        // Display to the user their dashboard message
        displayDashboardMessage();

        // Hide action bar
        try {
            getActionBar().hide();
        }catch (Exception e){}

        // For the app secret feature
        tapIndex = 1;
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

    /**
     * Method displayDashboardMessage
     * Fetches the user's account state and displays the appropriate message
     */
    private void displayDashboardMessage(){
        // We will notify users of accounts under their current set threshold, if any
        final TextView dashBoard = (TextView) findViewById(R.id.dashboardMessage);
        dashBoard.setText((new BalanceNotifier(this, ParseUser.getCurrentUser().getUsername())).getMessage());
    }

    /**
     * Method secret
     * Activates a bonus feature in the app!
     */
    public void secret(View view){
        ++tapIndex;
        if (tapIndex == 10){
            Intent intent = new Intent (this, TeleportingButton.class);
            startActivity(intent);
        }
    }
}
