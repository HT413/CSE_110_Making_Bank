package cse110.makingbank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.*;

import java.text.NumberFormat;

/**
 * Class: CreateAccountPage.java
 * This is the class created after the user presses on the Create Account
 * button in the main Menu.
 *
 * Date: October 21, 2014
 */

public class CreateAccountPage extends Activity{

	private Spinner accountTypeSpinner;
	
    /**
     * Method: onCreate
     * This method defines what happens when the object is created.
     * Brings up the options for account creation.
     *
     * @param savedInstanceState   a saved instance of the object
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        // Hide action bar
        try {
            getActionBar().hide();
        }catch (Exception e){}
        
        // Set up spinner for selecting the account type
        accountTypeSpinner = (Spinner) findViewById(R.id.SelectAccountType);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.account_creation_account_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        accountTypeSpinner.setAdapter(adapter);
    }

    /**
     * Method: submitName
     * This method defines what happens when the button_submit_info
     * button is pressed. User MUST have filled in all fields.
     *
     * @param button  The button that was pressed
     */
    public void submitInfo(View button)
    {
        synchronized(this){
            String accountType = accountTypeSpinner.getSelectedItem().toString();

            //All fields must be filled!
            if (!accountType.equals("Select")) {
                final ParseUser currentUser = ParseUser.getCurrentUser(); // Get current user
                String username = currentUser.getUsername();
                String email = currentUser.getEmail();
                String phoneNum = currentUser.getString("phone");

                // Assign a unique account number to the account
                final int accountIndex = currentUser.getInt("numAccounts");
                // 0x17 chosen so that values returned are always 99 max
                String name = "" + (username.toString().charAt(0) - 0x17) +
                    (username.toString().charAt(1) - 0x17) +
                    (username.toString().charAt(2) - 0x17) + accountIndex;
                int accountRnd = generateRnd(10 - name.length());

                // Create a new Parse object of type BankAccount
                ParseObject bankAccount = new ParseObject("bankAccount");
                bankAccount.put("accountNumber", name + accountRnd);
                bankAccount.put("user", username); // Object holds username
                bankAccount.put("balance", 0); // Object holds current balance
                bankAccount.put("threshold", 0); // Object holds current default threshold
                bankAccount.put("email", email); // Object holds user email
                bankAccount.put("phone", phoneNum); // Object holds phone number
                bankAccount.put("accountType", accountType); // Finally, record the account type
                // Save this info & send to Parse
                bankAccount.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e == null) { // No error was thrown
                            currentUser.put("numAccounts", accountIndex + 1);
                            currentUser.saveInBackground(); // Save this new user info
                            goHome(); // Go back to the main page
                        } else { // Some error occured!
                            if (e.getCode() == ParseException.CONNECTION_FAILED){
                                TextView pageNotice = (TextView) findViewById(R.id.createAccountPageDesc);
                                pageNotice.setText("Can't connect to server!");
                                ScrollView mainView = (ScrollView) findViewById(R.id.scrollviewCreateAccount);
                                mainView.fullScroll(ScrollView.FOCUS_UP);
                            }
                            else{
                                TextView pageNotice = (TextView) findViewById(R.id.createAccountPageDesc);
                                pageNotice.setText("Unknown error!");
                                ScrollView mainView = (ScrollView) findViewById(R.id.scrollviewCreateAccount);
                                mainView.fullScroll(ScrollView.FOCUS_UP);
                            }
                        }
                    }
                });
            }
            //Throw error and tell user to fill in all fields
            else {
                TextView pageNotice = (TextView) findViewById(R.id.createAccountPageDesc);
                pageNotice.setText("Please select account type.");
                ScrollView mainView = (ScrollView) findViewById(R.id.scrollviewCreateAccount);
                mainView.fullScroll(ScrollView.FOCUS_UP);
            }
        }
    }

    /**
     * Method generateRnd
     * This method allows the generation of randomized final digits for the account
     * number, so that users have an extremely low chance of having the same account
     * numbers.
     */
    private int generateRnd(int length){
        if (length <= 0)
            return ParseUser.getCurrentUser().getInt("numAccounts") % 10;
        NumberFormat fmt = NumberFormat.getInstance();
        fmt.setMinimumIntegerDigits(length);
        fmt.setMaximumIntegerDigits(length);
        fmt.setGroupingUsed(false);
        return Integer.parseInt(fmt.format((int) (Math.random() * Math.pow(10, length))));
    }

    /**
     * Go back to main menu if back button is pressed.
     */
    @Override
    public void onBackPressed(){
        goHome();
    }

    private void goHome(){
        // Now go back to the main page
        Intent intent = new Intent(this, BankHomePage.class);
        startActivity(intent);
    }
}
