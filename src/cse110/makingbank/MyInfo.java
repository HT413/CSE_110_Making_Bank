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
            final EditText firstNameField = (EditText) findViewById(R.id.EditTextFirstName);
            final EditText lastNameField = (EditText) findViewById(R.id.EditTextLastName);
            final EditText addressField = (EditText) findViewById(R.id.EditTextAddress1);
            final EditText cityField = (EditText) findViewById(R.id.EditTextCity);
            final EditText POField = (EditText) findViewById(R.id.EditTextBoxNumber);
            final EditText phoneField = (EditText) findViewById(R.id.EditTextPhoneNumber);

            boolean validPO = true;
            boolean validPhone = true;
            int PONumber = -1;

            // Get text from all fields
            String firstName = firstNameField.getText().toString();
            String lastName = lastNameField.getText().toString();
            String address = addressField.getText().toString();
            String city = cityField.getText().toString();
            String PO = POField.getText().toString();
            String phoneNum = phoneField.getText().toString();

            // Check if PO box is valid or not
            if (PO.equals(""))
                validPO = false;
            for (int i = 0; i < PO.length(); i++) {
                if (!(PO.charAt(i) >= '0' && PO.charAt(i) <= '9')) {
                    validPO = false;
                    break;
                }
            }
            if (validPO) {
                PONumber = Integer.parseInt(PO);
                if (!(PONumber >= 10000 && PONumber <= 99999)) // Check if valid PO box number
                    PONumber = -1;
            }

            // Check if phone number is valid or not
            if (!(phoneNum.length() == 12 && phoneNum.charAt(3) == '-' && phoneNum.charAt(7) == '-')){
                validPhone = false;
                phoneNum = "";
            }

            String currentState = stateSpinner.getSelectedItem().toString();

            //All fields must be filled!
            if (!firstName.equals("") && !lastName.equals("") && !city.equals("")
                    && !currentState.equals("State") && !address.equals("")
                    && PONumber != -1 && !phoneNum.equals("")) {
                final ParseUser currentUser = ParseUser.getCurrentUser(); // Get current user\

                // Append the following info to the current user
                currentUser.put("firstName", firstName); // First name
                currentUser.put("lastName", lastName); // Last Name
                currentUser.put("address", address); // Address
                currentUser.put("phone", phoneNum); // Phone number
                currentUser.put("city", city); // Current city
                currentUser.put("state", currentState); // Current state
                currentUser.put("zipCode", PONumber); // Zip Code
                // Save this info & send to Parse
                currentUser.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e == null) { // No error was thrown
                            // User has given info, now user can use the app normally
                            currentUser.put("givenInfo", true);
                            currentUser.saveInBackground();
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
                pageNotice.setText("Please double check all fields.");
                if (!validPO){
                    ((TextView) findViewById(R.id.zipCodePrompt)).setText("INVALID ZIP CODE");
                }
                if (!validPhone) {
                    ((TextView) findViewById(R.id.createAccountPhoneDesc))
                            .setText("MUST BE XXX-YYY-ZZZ FORMAT!");
                }
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

    private void goHome(){
        // Now go back to the main page
        Intent intent = new Intent(this, BankHomePage.class);
        startActivity(intent);
    }
}
