package cse110.makingbank;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Class: CreateAccountPage.java
 * This is the class created after the user presses on the Create Account
 * button in the main Menu.
 *
 * Date: October 21, 2014
 */

public class CreateAccountPage extends Activity implements OnItemSelectedListener{

	private Spinner accountTypeSpinner;
	String accountType;
	
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
        
        
        // Set up spinner
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
    public void submitName(View button)
    {
    	final EditText firstNameField = (EditText) findViewById(R.id.EditTextFirstName);
    	final EditText lastNameField = (EditText) findViewById(R.id.EditTextLastName);
    	
    	String firstName = firstNameField.getText().toString();
    	String lastName = lastNameField.getText().toString();

        //TODO action when pressing submit button
    }

   /**
    * Method: onItemSelected
    * This method defines what happens when an option from the spinner drop down menu 
    * is selected.
    */
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		accountType = accountTypeSpinner.getSelectedItem().toString();
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}
