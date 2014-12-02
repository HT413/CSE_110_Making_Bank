package cse110.makingbank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseUser;

public class AccountOptions extends Activity {

    private String accountNumber;
    private double currentBalance;
    private ParseUser currentUser;
    private ParseObject theAccount;

    /**
     * Method onCreate
     * Create the ViewTransactions page
     */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_actions);
        // Hide action bar
        try {
            getActionBar().hide();
        }catch (Exception e){}

        // Get current user
        currentUser = ParseUser.getCurrentUser();

        // Get the passed in account information
        String val = getIntent().getExtras().getString("accountNum");
        theAccount = (new DatabaseQuery("bankAccount", "accountNumber", val)).get(0);
        accountNumber = theAccount.getString("accountNumber");
        currentBalance = theAccount.getDouble("balance");

        // Now update the appropriate text views
        TextView balance = (TextView) findViewById(R.id.currentBalance);
        balance.setText(balance.getText().toString() + currentBalance);

        TextView pageTitle = (TextView) findViewById(R.id.accountInfoPageTitle);
        pageTitle.setText(pageTitle.getText().toString() + " " + accountNumber);
	}

    /**
     * Overridden method onBackPressed
     * Send user back to this same account options page
     */
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, AccountOptions.class);
        intent.putExtra("accountNum", accountNumber);
        this.startActivity(intent);
    }

    /**
     * Method requestClose
     * Close the account since the user wants it, well, closed.
     */
    public void requestClose(View view){
        // Do not allow user to delete an account with money on it
        if (currentBalance > 0){
            TextView pageTitle = (TextView) findViewById(R.id.accountInfoPageTitle);
            pageTitle.setText("CAN'T DELETE AN ACCOUNT WITH MONEY");
        }
        // If no money, then delete the account
        new AccountDeleter(theAccount);
        // Finally, return to the main menu since we don't want to access this account anymore
        backToHome(view);
    }

    /**
     * Method backToHome
     * Go back to the bank home page
     */
    public void backToHome(View view){
        Intent intent = new Intent(this, BankHomePage.class);
        startActivity(intent);
    }

    /**
     * Method viewHistory
     * Go to the transaction history (statement) for the specific bank account
     */
    public void viewHistory(View view){
        Intent intent = new Intent (this, ViewStatementPage.class);
        intent.putExtra("accountNum", accountNumber);
        startActivity(intent);
    }

    /**
     * Method changeThreshold
     * This method will be called if user clicks on the Change Threshold button
     * Change the current account notification threshold
     */
    public void changeThreshold(View view){
        setContentView(R.layout.change_threshold_page);
        // Change the page layout and display the current threshold
        ((EditText) findViewById(R.id.valueForNotify)).
            setText("" + theAccount.getInt("threshold"));
    }

    /**
     * Method submitNewThreshold
     * This method will be called if user clicks on the Submit button.
     * Change the account notification threshold to the new value.
     */
    public void submitNewThreshold(View view){
        // Android limits the keypad to be non-negative numerical only, so no error checks needed
        theAccount.put("threshold", Integer.parseInt
                      (((TextView) findViewById(R.id.valueForNotify)).getText().toString()));
        // Now save the info
        theAccount.saveInBackground();
        // Go back to the main account view page
        onBackPressed();
    }

    /**
     * Method selectTransferOption
     * This method is called when the user clicks on the transfer money button.
     * Let the user pick which money transfer option to use.
     */
    public void selectTransferOption(View view){
        setContentView(R.layout.select_transfer_option_page);
    }

    /**
     * Method transferSelf
     * This method is called when the user chooses to transfer money between his/her own
     * accounts. Passes the current account onto the new account selection page.
     */
    public void transferSelf(View view){
        Intent intent = new Intent (this, TransferSelfPage.class);
        intent.putExtra("accountNum", accountNumber);
        intent.putExtra("maxBalance", currentBalance);
        startActivity(intent);
    }

    /**
     * Method transferOther
     * This method is called when the user chooses to transfer money to another user.
     * Still passes the current account onto the newly created page.
     */
    public void transferOther(View view){
        Intent intent = new Intent (this, TransferOtherPage.class);
        intent.putExtra("accountNum", accountNumber);
        intent.putExtra("maxBalance", currentBalance);
        startActivity(intent);
    }

    /**
     * Method makeDefault
     * This method is called when the user clicks the Make Default Account button.
     * Simply update the current user's default account.
     */
    public void makeDefault(View view){
        currentUser.put("defaultAccount", accountNumber);
        currentUser.saveInBackground();
    }
}
