package cse110.makingbank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class TransferOtherPage extends Activity{
    private String accountNum, destinationNum;
    private double currentBalance, destinationBalance;
    private ParseObject source, destination;
    private ParseUser transferTo = null;

    /**
     * Method onCreate
     * Defines the actions this class should do when it is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_transfer);
        // Hide action bar
        try {
            getActionBar().hide();
        }catch (Exception e){}
        // Fetch the extra passed in data
        Bundle extras = getIntent().getExtras();
        accountNum = extras.getString("accountNum");
        currentBalance = extras.getDouble("maxBalance");

        // We must again retrieve the account from the server as custom objects
        ParseQuery<ParseObject> query = ParseQuery.getQuery("bankAccount");
        query.whereEqualTo("accountNumber", accountNum);
        query.findInBackground( new FindCallback<ParseObject>() {
            // User must have had at least one account to even get here, so exceptions
            // will not be thrown. There is only one single account in the list as account
            // numbers are unique.
            public void done (List<ParseObject> list, ParseException e){
                source = list.get(0);
            }
        });

        // Display the appropriate info
        ((TextView) findViewById(R.id.fromAccountLine)).setText("Account " + accountNum +
            ", balance: $" + currentBalance);
    }

    /**
     * Method submitTransfer
     * This method is called after submitting all info for the transfer operation
     */
    public void submitSearch(View view){
        // Ensure that the user actually entered some information
        String theValue = ((EditText) findViewById(R.id.criteriaData)).getText().toString();
        if (theValue.equals(""))
            ((TextView) findViewById(R.id.toWhatTypePrompt)).setText("Cannot be empty!");
        else{ // Now we search for the other user and make a transfer, if possible.
            // Query parse users
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("email", theValue);
            ParseQuery<ParseUser> query2 = ParseUser.getQuery();
            query2.whereEqualTo("phone", theValue);
            // Now search for the user. If user is found, load up the default account
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> parseUsers, ParseException e) {
                    if (parseUsers.size() > 0){
                        transferTo = parseUsers.get(0);
                        if (transferTo.getInt("numAccounts") > 0)
                            setUpTransfer();
                        else
                            ((TextView) findViewById(R.id.toWhatTypePrompt)).setText("User has no accounts!");
                    }
                    else{
                        ((TextView) findViewById(R.id.toWhatTypePrompt)).setText("No user found!");
                    }
                }
            });
            query2.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> parseUsers, ParseException e) {
                    if (parseUsers.size() > 0){
                        transferTo = parseUsers.get(0);
                        if (transferTo.getInt("numAccounts") > 0)
                            setUpTransfer();
                        else
                            ((TextView) findViewById(R.id.toWhatTypePrompt)).setText("User has no accounts!");
                    }
                    else{
                        ((TextView) findViewById(R.id.toWhatTypePrompt)).setText("No user found!");
                    }
                }
            });
        }
    }

    /**
     * Method onBackPressed
     * We will go back to the original account menu page that led to this transfer page
     */
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, AccountOptions.class);
        intent.putExtra("accountNum", accountNum);
        startActivity(intent);
    }

    /**
     * Method transactionComplete
     * Tell the user the transaction is done
     */
    private void transactionComplete(){
        setContentView(R.layout.page_with_message);
        ((TextView) findViewById(R.id.theMessage)).setText("Transaction complete. " +
                "Press back button to return.");
    }

    /**
     * Method setUpTransfer
     * Sets up the transfer page between current account and destination account
     */
    private void setUpTransfer(){
        // Find the default account to transfer to
        ParseQuery<ParseObject> accountQuery = ParseQuery.getQuery("bankAccount");
        accountQuery.whereEqualTo("accountNumber", (destinationNum = transferTo.getString("defaultAccount")));
        // Now find that default account in the database
        accountQuery.findInBackground( new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, ParseException e)
            {
                if (e == null){ // Typically no error should happen here
                    destination = list.get(0); // There should only be 1 account
                    destinationBalance = destination.getDouble("balance");
                } else {
                    destination = null;
                }
            }
        });
        setContentView(R.layout.transfer_other_amount_page);
        // Display the appropriate info
        ((TextView) findViewById(R.id.fromAccountLine)).setText("Account " + accountNum +
                ", balance: $" + currentBalance);
    }

    /**
     * Method submitTransfer
     * Submit the info for the account money transfer
     */
    public void submitTransfer(View view) {
        TextView transferPrompt = ((TextView) findViewById(R.id.transferPrompt));
        // Finally, attempt to do the transaction
        if (((EditText) findViewById(R.id.transferAmountField)).getText().toString().equals("")){
            transferPrompt.setText("Cannot be empty!");
        }
        else{
            // Get the transaction amount
            double transactionAmount = new Round(Double.parseDouble(((EditText) findViewById
                    (R.id.transferAmountField)).getText().toString()), 2).toDouble();
            if (transactionAmount > currentBalance) // Check if enough money
                transferPrompt.setText("INSUFFICIENT FUNDS!");
            else { // Sufficient funds, proceed with transaction
                // Due to minor precision problems with doubles, we need to round again
                double fromAfterBalance = new Round(currentBalance - transactionAmount, 2).toDouble();
                double toAfterBalance = new Round(destinationBalance + transactionAmount, 2).toDouble();
                // Now store and save the new balances
                source.put("balance", fromAfterBalance);
                destination.put("balance", toAfterBalance);
                source.saveInBackground();
                destination.saveInBackground();
                // Log the transactions
                new TransactionLog(currentBalance, transactionAmount, fromAfterBalance,
                        "Transfer", accountNum);
                new TransactionLog(destinationBalance, transactionAmount, toAfterBalance,
                        "Receive", destinationNum);
                // Notify user of successful transaction
                transactionComplete();
            }
        }
    }
}
