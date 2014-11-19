package cse110.makingbank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class SelfTransfer extends Activity{
    private String theAccount, accountNum;
    private ParseObject source, destination;
    private double fromBalance, toBalance;

    /**
     * Method onCreate
     * Defines the operations to take upon creation of this class
     */
    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_transfer);

        // Hide action bar
        try {
            getActionBar().hide();
        }catch (Exception e){}

        // Get the extra passed in information
        Bundle extras = getIntent().getExtras();
        theAccount = extras.getString("transferFrom");
        accountNum = extras.getString("transferTo");
        fromBalance = extras.getDouble("maxBalance");

        // Now we need to fetch the accounts in question
        ParseQuery<ParseObject> query = ParseQuery.getQuery("bankAccount");
        query.whereEqualTo("user", ParseUser.getCurrentUser().getUsername()); // base it on username
        query.findInBackground(new FindCallback<ParseObject>() {
            // Store the bank accounts in to a list
            public void done(List<ParseObject> al, ParseException e) {
                // Must find the appropriate account to have stored locally
                for (ParseObject account: al){
                    String accountNumber = account.getString("accountNumber");
                    if (accountNumber.equals(theAccount))
                        source = account;
                    if (accountNumber.equals(accountNum))
                        destination = account;
                }
            }
        });

        // And finally update the account information lines
        ((TextView) findViewById(R.id.fromAccountLine)).setText("Account " + theAccount +
            ", balance: $" + fromBalance);
        ((TextView) findViewById(R.id.toAccountLine)).setText("Account " + accountNum +
            ", balance: $" + (toBalance = destination.getDouble("balance")));
    }

    /**
     * Method submitTransfer
     * Defines the operations to take when the user submits the amount they would like to transfer
     */
    public void submitTransfer(View view){
        // Due to Android itself limiting the keyboard, all numbers entered will have been
        // numerical
        EditText amountField = (EditText) findViewById(R.id.transactionAmountField);
        TextView transferPrompt = (TextView) findViewById(R.id.transferPrompt);
        double transactionAmount;
        if (amountField.getText().toString().equals("")) { // Check for an entry
            transactionAmount = -1;
        }
        else // If there is one, then we can get the entry
            transactionAmount = Double.parseDouble(amountField.getText().toString());
        if (transactionAmount <= 0) // Check if non-negative
            transferPrompt.setText("Must enter a positive number!");
        else{ // Now proceed to round it to 2 decimal places if valid entry
            transactionAmount = round(transactionAmount, 2);
            if (transactionAmount > fromBalance) // Check if enough money
                transferPrompt.setText("INSUFFICIENT FUNDS!");
            else{ // Sufficient funds, proceed with transaction
                // Due to minor precision problems with doubles, we need to round again
                fromBalance = round(fromBalance - transactionAmount, 2);
                toBalance = round(fromBalance + transactionAmount, 2);
                // Now store and save the new balances
                source.put("balance", fromBalance);
                destination.put("balance", toBalance);
                source.saveInBackground();
                destination.saveInBackground();
                // Notify user of successful transaction
                transactionComplete();
            }
        }
    }



    /**
     * Method round
     * This will round any decimal number to the nearest 2 decimal digits
     */
    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_DOWN);
        return bd.doubleValue();
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
     * Method onBackPressed
     * We will go back to the original account menu page that led to this transfer page
     */
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, AccountOptions.class);
        intent.putExtra("accountNum", theAccount);
        startActivity(intent);
    }
}
