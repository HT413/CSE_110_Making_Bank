package cse110.makingbank;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Class: CreateAccountPage.java
 * This is the class created after the user presses on the Create Account
 * button in the main Menu.
 *
 * Date: October 21, 2014
 */

public class CreateAccountPage extends Activity {

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
    }

    /**
     * Method: submitName
     * This method defines what happens when the button_submit_info
     * button is pressed. User MUST have filled in all fields.
     *
     * @param button  The button that was pressed
     */
    public void submitName(View button)
    {
    	final EditText firstNameField = (EditText) findViewById(R.id.EditTextFirstName);
    	final EditText lastNameField = (EditText) findViewById(R.id.EditTextLastName);
    	
    	String firstName = firstNameField.getText().toString();
    	String lastName = lastNameField.getText().toString();

        //TODO action when pressing submit button
    }
}
