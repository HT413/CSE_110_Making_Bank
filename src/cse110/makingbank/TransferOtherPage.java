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

public class TransferOtherPage extends Activity{
    private String accountNum;
    private double currentBalance;
    private String destinationCriteria;
    private ParseObject source, destination;
    private Spinner transferCriteria;

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

        // Now display the appropriate info
        currentBalance = source.getDouble("balance");
        ((TextView) findViewById(R.id.fromAccountLine)).setText("Your account " + accountNum +
            ", balance: $" + currentBalance);

        // Finally populate the Spinner
        // Set up spinner for selecting the account type
        transferCriteria = (Spinner) findViewById(R.id.selectCriteria);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.transfer_criteria_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        transferCriteria.setAdapter(adapter);
    }

    /**
     * Method submitTransfer
     * This method is called after submitting all info for the transfer operation
     */
    public void submitTransfer(View view){
        // Ensure that the user actually entered some information
        String theCriteria = transferCriteria.getSelectedItem().toString();
        String theValue = ((EditText) findViewById(R.id.criteriaData)).getText().toString();
        if (theCriteria.equals("Select") || theValue.equals(""))
            ((TextView) findViewById(R.id.toWhatTypePrompt)).setText("Invalid entries!");
        else{ // Now we search for the other user and make a transfer, if possible.

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
     * Method logTransaction
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
