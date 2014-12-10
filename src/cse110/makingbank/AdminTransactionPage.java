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

import java.util.List;

public class AdminTransactionPage extends Activity {
    private Spinner transactionTypeSpinner; // The dropdown menu for transaction type
    private double balanceAfter;

    /**
     * Method onCreate
     * Create the page
     */
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide action bar
        try {
            getActionBar().hide();
        }catch (Exception e){}

        // Switch to transaction page layout
        setContentView(R.layout.bank_transaction_page);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            ((EditText) findViewById(R.id.accountNameField)).setText(extras.getString("ID"));
        }

        // Set up spinner for account transaction type
        transactionTypeSpinner = (Spinner) findViewById(R.id.spinnerTransactionType);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.account_transaction_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        transactionTypeSpinner.setAdapter(adapter);
    }

    /**
     * Method onBackPressed
     * When the back button is pressed, go back to the main menu
     */
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, AdminHomePage.class);
        startActivity(intent);
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
            final double transactionAmount = new Round(Double.parseDouble
                    (transactionAmtField.getText().toString()), 2).toDouble();
            final String transactionType = transactionTypeSpinner.getSelectedItem().toString();

            boolean valid = true;

            // Check if valid operations were selected
            if (transactionType.equals("Select")) {
                ((TextView) findViewById(R.id.transactionTypePrompt))
                        .setText("SELECT TRANSACTION TYPE");
                valid = false;
            }
            if (transactionAmount <= 0.0) {
                ((TextView) findViewById(R.id.transactionAmountPrompt))
                        .setText("MUST BE POSITIVE NUMBER");
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
                            // Inform admin that
                            ((TextView) findViewById(R.id.accountNamePrompt)).
                                    setText("ACCOUNT DOES NOT EXIST");
                        }
                        else { // Account found, perform transaction on it if possible
                            ParseObject account = aList.get(0);
                            double balanceBefore = account.getDouble("balance");
                            if (transactionType.equals("Credit")){
                                // Round to nearest 2 decimal places
                                balanceAfter = new Round(balanceBefore + transactionAmount, 2).toDouble();
                                account.put("balance", balanceAfter); // Apply new balance
                                account.saveInBackground(); // Update account
                                new TransactionLog(balanceBefore, transactionAmount,
                                        balanceAfter, transactionType, accountNumber);
                                completeTransaction();
                            }
                            else if (transactionType.equals("Debit")){
                                // Check if sufficient funds
                                if (balanceBefore < transactionAmount)
                                    ((TextView) findViewById(R.id.transactionAmountPrompt))
                                            .setText("INSUFFICIENT FUNDS");
                                else{
                                    // Round to nearest 2 decimal places
                                    balanceAfter = new Round(balanceBefore - transactionAmount, 2).toDouble();
                                    account.put("balance", balanceAfter); // Apply new balance
                                    account.saveInBackground(); // Update account
                                    new TransactionLog(balanceBefore, transactionAmount,
                                            balanceAfter, transactionType, accountNumber);
                                    completeTransaction();
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    /**
     * Method completeTransaction
     * Tell the user the transaction is done
     */
    private void completeTransaction(){
        setContentView(R.layout.page_with_message);
        ((TextView) findViewById(R.id.theMessage)).setText("Transaction complete. ");
    }
}
