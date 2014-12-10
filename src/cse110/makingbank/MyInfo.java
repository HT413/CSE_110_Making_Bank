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
 * Class: MyInfo.java
 * This class will allow first time users to enter in their personal info.
 */

public class MyInfo extends Activity{

    private Spinner stateSpinner;
    private EditText firstNameField, lastNameField, addressField, cityField, zipField, phoneField;
    private ParseUser currentUser;

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
        setContentView(R.layout.edit_info_page);
        // Hide action bar
        try {
            getActionBar().hide();
        }catch (Exception e){}

        // Set up spinner for current state
        stateSpinner = (Spinner) findViewById(R.id.SelectCurrentState);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.account_creation_states, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        stateSpinner.setAdapter(adapter);

        // Initialize the private variables
        firstNameField = (EditText) findViewById(R.id.EditTextFirstName);
        lastNameField = (EditText) findViewById(R.id.EditTextLastName);
        addressField = (EditText) findViewById(R.id.EditTextAddress1);
        cityField = (EditText) findViewById(R.id.EditTextCity);
        zipField = (EditText) findViewById(R.id.EditTextBoxNumber);
        phoneField = (EditText) findViewById(R.id.EditTextPhoneNumber);
        currentUser = ParseUser.getCurrentUser();

        // Populate the current fields if entries already previously entered
        if (currentUser.getBoolean("givenInfo")){
            firstNameField.setText(currentUser.getString("firstName"));
            lastNameField.setText(currentUser.getString("lastName"));
            addressField.setText(currentUser.getString("address"));
            cityField.setText(currentUser.getString("city"));
            zipField.setText("" + currentUser.getInt("zipCode"));
            phoneField.setText(currentUser.getString("phone"));
        }
    }

    /**
     * Method: submitName
     * This method defines what happens when the button_submit_info
     * button is pressed. User MUST have filled in all fields.
     *
     * @param button  The button that was pressed
     */
    public void changeInfo(View button)
    {
        synchronized(this){
            // Get all the inputs
            EditText[] inputs = {firstNameField, lastNameField, addressField, cityField, zipField,
                    phoneField};
            Rule checker = new InfoRule(inputs);

            String currentState = stateSpinner.getSelectedItem().toString();

            //All fields must be filled!
            if (!checker.hasErrors() && !currentState.equals("State")) {
                final ParseUser currentUser = ParseUser.getCurrentUser(); // Get current user\

                // Append the following info to the current user
                currentUser.put("firstName", firstNameField.getText().toString()); // First name
                currentUser.put("lastName", lastNameField.getText().toString()); // Last Name
                currentUser.put("address", addressField.getText().toString()); // Address
                currentUser.put("phone", phoneField.getText().toString()); // Phone number
                currentUser.put("city", cityField.getText().toString()); // Current city
                currentUser.put("state", currentState); // Current state
                currentUser.put("zipCode", zipField.getText().toString()); // Zip Code
                // Save this info & send to Parse
                currentUser.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e == null) { // No error was thrown
                            // User has given info, now user can use the app normally
                            currentUser.put("givenInfo", true);
                            currentUser.saveInBackground();
                            goHome(); // Go back to the main page
                        } else {
                            TextView pageNotice = (TextView) findViewById(R.id.createAccountPageDesc);
                            pageNotice.setText("An error occured!");
                            ScrollView mainView = (ScrollView) findViewById(R.id.scrollviewCreateAccount);
                            mainView.fullScroll(ScrollView.FOCUS_UP);
                        }
                    }
                });
            }
            //Throw error and tell user to fill in all fields
            else {
                TextView pageNotice = (TextView) findViewById(R.id.editPageTitle);
                checker.printError(pageNotice);
                ScrollView mainView = (ScrollView) findViewById(R.id.scrollviewCreateAccount);
                mainView.fullScroll(ScrollView.FOCUS_UP);
            }
        }
    }

    /**
     * Go back to main menu if back button is pressed. Only allowed if the user
     * has actually submitted their personal info before.
     */
    @Override
    public void onBackPressed(){
        if (ParseUser.getCurrentUser().getBoolean("givenInfo"))
            goHome();
    }

    /**
     * Method goHome
     * Goes back to the home menu
     */
    private void goHome(){
        // Now go back to the main page
        Intent intent = new Intent(this, BankHomePage.class);
        startActivity(intent);
    }
}
