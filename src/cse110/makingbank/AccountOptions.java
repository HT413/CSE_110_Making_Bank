package cse110.makingbank;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class AccountOptions extends Activity {

    private String accountNumber;
    private double currentBalance;
    private ParseUser currentUser;

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

        // Get the passed in data
        Bundle extras = getIntent().getExtras();
        accountNumber = extras.getString("accountName");
        TextView pageTitle = (TextView) findViewById(R.id.accountInfoPageTitle);
        pageTitle.setText(pageTitle.getText().toString() + " " + accountNumber);

        // Display the account balance
        // First, query for the account
        ParseQuery<ParseObject> query = ParseQuery.getQuery("bankAccount");
        query.whereEqualTo("accountNumber", accountNumber);
        query.findInBackground( new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, ParseException e){
                if (list.size() > 0 && e == null){
                    TextView balance = (TextView) findViewById(R.id.currentBalance);
                    currentBalance = list.get(0).getDouble("balance");
                    balance.setText(balance.getText().toString() + currentBalance);
                }
                else{
                    TextView balance = (TextView) findViewById(R.id.currentBalance);
                    balance.setText("NETWORK CONNECTION ERROR!");
                }
            }
        });
	}

    /**
     * Overridden method onBackPressed
     * Send user back to view accounts page
     */
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, ViewAccountPage.class);
        this.startActivity(intent);
    }

    /**
     * Method requestClose
     * Close the account since the user wants it, well, closed.
     */
    public void requestClose(View view){
        // First, query for the account
        ParseQuery<ParseObject> query = ParseQuery.getQuery("bankAccount");
        query.whereEqualTo("accountNumber", accountNumber);
        query.findInBackground( new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, ParseException e){
                if (list.size() > 0 && e == null){
                    ParseObject account = list.get(0);
                    if (currentBalance > 0){
                        // Should not let user delete the account if it has money
                        TextView pageTitle = (TextView) findViewById(R.id.accountInfoPageTitle);
                        pageTitle.setText("CAN'T DELETE AN ACCOUNT WITH MONEY");
                    }
                    else{
                        // Close the account
                        account.deleteInBackground();
                        // Update the account index again
                        int oldIndex = currentUser.getInt("numAccounts");
                        currentUser.put("numAccounts", oldIndex - 1);
                        currentUser.saveInBackground();
                        // Now go back
                        onBackPressed();
                    }
                }
                else{
                    TextView balance = (TextView) findViewById(R.id.currentBalance);
                    balance.setText("NETWORK CONNECTION ERROR!");
                }
            }
        });
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
     * Display the transaction history to the user
     */
    public void viewHistory(View view){
        // Query for transactions first
        ParseQuery<ParseObject> query = ParseQuery.getQuery("transaction");
        // Query for all transactions from this specific account
        query.whereEqualTo("account", accountNumber);
        // Now find all the transactions
        query.findInBackground( new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, ParseException e) {
                // Layout to list all fetched transactions
                LinearLayout l = new LinearLayout(AccountOptions.this);
                l.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT));
                l.setOrientation(LinearLayout.VERTICAL);

                // List transactions only if no errors and if there were transactions in the list
                if (list.size() > 0 && e == null){

                    // Set the buttons to be 10 pixels from the sides and apart from each other
                    LinearLayout.LayoutParams params = new TableLayout.LayoutParams
                            (TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(10, 10, 10, 0);
                    // List transactions one by one, in reverse chronological order
                    for (int i = list.size() - 1; i >= 0; i--) {
                        ParseObject transaction = list.get(i);
                        // Make the transactions be displayed in nice little buttons so it does the
                        // formatting for us.
                        Button b = new Button(AccountOptions.this);
                        // Display the transaction info
                        b.setText("On " + transaction.getCreatedAt().toString() + ":\n" +
                                  transaction.getString("type") + "ed $" +
                                  transaction.getDouble("amount") + ".\n" +
                                  "Balance before: $" + transaction.getDouble("before") +
                                  "\nBalance after: $" + transaction.getDouble("after"));
                        b.setTextSize(12);
                        b.setBackgroundResource(R.drawable.button_round_corners);
                        b.setLayoutParams(params);
                        l.addView(b);
                    }
                }
                // No transactions, tell user that.
                else{
                    // display to the user that he has no accounts
                    TextView error = new TextView(AccountOptions.this);
                    error.setLayoutParams(new TableLayout.LayoutParams
                        (TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    error.setX(20);
                    error.setY(20);
                    error.setText("No transactions recorded for this account. " +
                                  "Press the back button to return.");
                    error.setTextSize(30);
                    error.setTextColor(Color.RED);
                    l.addView(error);
                }
                AccountOptions.this.setContentView(l);
            }
        });
    }
}
