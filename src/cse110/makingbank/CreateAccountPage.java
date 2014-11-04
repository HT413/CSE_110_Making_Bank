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

/**
 * Class: CreateAccountPage.java
 * This is the class created after the user presses on the Create Account
 * button in the main Menu.
 *
 * Date: October 21, 2014
 */

public class CreateAccountPage extends Activity{

	private Spinner accountTypeSpinner, stateSpinner;
	
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
        
        // Set up spinners
        accountTypeSpinner = (Spinner) findViewById(R.id.SelectAccountType);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.account_creation_account_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        accountTypeSpinner.setAdapter(adapter);

        stateSpinner = (Spinner) findViewById(R.id.SelectCurrentState);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.account_creation_states, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        stateSpinner.setAdapter(adapter2);
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
            final EditText firstNameField = (EditText) findViewById(R.id.EditTextFirstName);
            final EditText lastNameField = (EditText) findViewById(R.id.EditTextLastName);
            final EditText addressField = (EditText) findViewById(R.id.EditTextAddress1);
            final EditText cityField = (EditText) findViewById(R.id.EditTextCity);
            final EditText POField = (EditText) findViewById(R.id.EditTextBoxNumber);

            boolean validPO = true;
            int PONumber = -1;

            // Get text from all fields
            String firstName = firstNameField.getText().toString();
            String lastName = lastNameField.getText().toString();
            String address = addressField.getText().toString();
            String city = cityField.getText().toString();
            String PO = POField.getText().toString();

            for (int i = 0; i < PO.length(); i++) {
                if (!(PO.charAt(i) >= '0' && PO.charAt(i) <= '9')) {
                    TextView pageNotice = (TextView) findViewById(R.id.createAccountPageDesc);
                    pageNotice.setText("Invalid PO box format.");
                    ScrollView mainView = (ScrollView) findViewById(R.id.scrollviewCreateAccount);
                    mainView.fullScroll(ScrollView.FOCUS_UP);
                    validPO = false;
                    break;
                }
            }
            if (validPO)
                PONumber = Integer.parseInt(POField.getText().toString());
            String accountType = accountTypeSpinner.getSelectedItem().toString();
            String currentState = stateSpinner.getSelectedItem().toString();

            //All fields must be filled!
            if (!accountType.equals("Select") && !firstName.equals("") && !lastName.equals("")
                    && !city.equals("") && !currentState.equals("State") && !address.equals("")
                    && PONumber != -1) {
                final ParseUser currentUser = ParseUser.getCurrentUser(); // Get current user
                String username = currentUser.getUsername();
                String email = currentUser.getEmail();
                final int accountIndex = currentUser.getInt ("bankAccounts");
                String name = username;
                if (username.length() > 8)
                    name = username.substring(0, 8);
                // Create a new Parse object of type BankAccount
                ParseObject bankAccount = new ParseObject("bankAccount");
                bankAccount.put("nickname", name + accountIndex);
                bankAccount.put("user", username); // Object holds username
                bankAccount.put("email", email); // Object holds user email
                bankAccount.put("balance", 0); // Start out with zero balance
                bankAccount.put("firstName", firstName); // Object holds first name
                bankAccount.put("lastName", lastName); // Object holds last name
                bankAccount.put("address", address); // Object holds address
                bankAccount.put("city", city); // Object holds current city
                bankAccount.put("state", currentState); // Object holds current state
                bankAccount.put("poBox", PONumber); // Object holds P.O. number
                bankAccount.put("accountType", accountType); // Finally, record the account type
                // Save this info & send to Parse
                bankAccount.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e == null) { // No error was thrown
                            currentUser.remove("bankAccounts"); // Update account index
                            currentUser.put("bankAccounts", (accountIndex + 1));
                            goHome(); // Go back to the main page
                        } else { // Some error occured!
                            if (e.getCode() == ParseException.CONNECTION_FAILED){
                                TextView pageNotice = (TextView) findViewById(R.id.createAccountPageDesc);
                                pageNotice.setText("Can't connect to server!");
                                ScrollView mainView = (ScrollView) findViewById(R.id.scrollviewCreateAccount);
                                mainView.fullScroll(ScrollView.FOCUS_UP);
                            }
                            else if (e.getCode() == ParseException.INCORRECT_TYPE){
                                TextView pageNotice = (TextView) findViewById(R.id.createAccountPageDesc);
                                pageNotice.setText("Invalid type entry!");
                                ScrollView mainView = (ScrollView) findViewById(R.id.scrollviewCreateAccount);
                                mainView.fullScroll(ScrollView.FOCUS_UP);
                            }
                            else if (e.getCode() == ParseException.MISSING_OBJECT_ID){
                                TextView pageNotice = (TextView) findViewById(R.id.createAccountPageDesc);
                                pageNotice.setText("Error with object ID!");
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
                pageNotice.setText("One or more fields are missing.");
                ScrollView mainView = (ScrollView) findViewById(R.id.scrollviewCreateAccount);
                mainView.fullScroll(ScrollView.FOCUS_UP);
            }
        }
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
