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

import java.util.List;

/**
 * Class: MainPage
 *
 * This is the home page for all events after a successful log in.
 */
public class AdminHomePage extends Activity {

    private Spinner transactionTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home_page);
        // Hide action bar
        try {
            getActionBar().hide();
        }catch (Exception e){}
    }

    /**
     * Nullify the back button for this page
     */
    @Override
    public void onBackPressed(){
        setContentView(R.layout.admin_home_page);
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
     * Method doTransaction
     * Go to the account transaction page when this button is pressed
     */
    public void doTransaction(View view){
        // Switch to transaction page layout
        setContentView(R.layout.bank_transaction_page);

        // Set up spinner for account transaction type
        transactionTypeSpinner = (Spinner) findViewById(R.id.spinnerTransactionType);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.account_transaction_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        transactionTypeSpinner.setAdapter(adapter);
    }

    /**
     * Method confirmTransaction
     * Confirm the user wants to perform the bank transaction
     */
    public void confirmTransaction (View view){
        synchronized(this) {
            final EditText accountNumField = (EditText) findViewById(R.id.accountNameField);
            final EditText transactionAmtField = (EditText) findViewById(R.id.transactionAmountField);

            final String accountNumber = accountNumField.getText().toString();
            if (transactionAmtField.getText().toString().equals(""))
                transactionAmtField.setText("-1");
            final Double transactionAmount = Double.parseDouble(transactionAmtField.getText().toString());
            final String transactionType = transactionTypeSpinner.getSelectedItem().toString();

            boolean valid = true;

            // Check if valid operations were selected
            if (transactionType.equals("Select")) {
                ((TextView) findViewById(R.id.transactionTypePrompt)).setText("SELECT TRANSACTION TYPE");
                valid = false;
            }
            if (transactionAmount <= 0.0) {
                ((TextView) findViewById(R.id.transactionAmountPrompt)).setText("MUST BE POSITIVE NUMBER");
                transactionAmtField.setText("");
                valid = false;
            }
            // Valid operations, now continue on
            if (valid){
                // find the bank account with specified account Number
                ParseQuery<ParseObject> query = ParseQuery.getQuery("bankAccount");
                query.whereEqualTo("accountNumber", accountNumber);
                query.findInBackground( new FindCallback<ParseObject>(){
                    // Store bank account in a list
                    public void done(List<ParseObject> aList, ParseException e) {
                        if (aList.size() == 0 || e != null){ // NO ACCOUNT FOUND!
                            accountNumField.setText("ACCOUNT DOES NOT EXIST");
                        }
                        else { // Account found, perform transaction on it if possible
                            ParseObject account = aList.get(0);
                            double balanceBefore = account.getDouble("balance");
                            if (transactionType.equals("Credit")){
                                double balanceAfter = balanceBefore + transactionAmount;
                                account.put("balance", balanceAfter); // Apply new balance
                                account.saveInBackground(); // Update account
                                logTransaction(balanceBefore, transactionAmount,
                                        balanceAfter, transactionType, accountNumber);
                            }
                            else if (transactionType.equals("Debit")){
                                // Check if sufficient funds
                                if (balanceBefore < transactionAmount)
                                    ((TextView) findViewById(R.id.transactionAmountPrompt))
                                            .setText("INSUFFICIENT FUNDS");
                                else{
                                    double balanceAfter = balanceBefore - transactionAmount;
                                    account.put("balance", balanceAfter); // Apply new balance
                                    account.saveInBackground(); // Update account
                                    logTransaction(balanceBefore, transactionAmount,
                                            balanceAfter, transactionType, accountNumber);
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    /**
     * Create transaction log
     */
    private void logTransaction(double balanceBefore, double amountChanged,
                                double balanceAfter, String transactionType, String accountNumber){
        // Log this successful transaction and save it
        ParseObject transaction = new ParseObject("transaction");
        transaction.put("account", accountNumber);
        transaction.put("type", transactionType);
        transaction.put("before", balanceBefore);
        transaction.put("amount", amountChanged);
        transaction.put("after", balanceAfter);
        // Save the transaction.
        transaction.saveInBackground();
        onBackPressed(); // Go back to home page
    }
}
