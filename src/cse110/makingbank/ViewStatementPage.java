package cse110.makingbank;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Class ViewStatementPage
 * This class allows the user to see a history of all their transactions to date
 */
public class ViewStatementPage extends Activity{
    private String accountNumber;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // Fetch the passed in data
        accountNumber = getIntent().getExtras().getString("accountNum");

        // Query for transactions first
        ParseQuery<ParseObject> query = ParseQuery.getQuery("transaction");
        // Query for all transactions from this specific account
        query.whereEqualTo("account", accountNumber);
        // Now find all the transactions
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, ParseException e) {
                // Layout to list all fetched transactions
                LinearLayout l = new LinearLayout(ViewStatementPage.this);
                l.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT));
                l.setOrientation(LinearLayout.VERTICAL);

                // List transactions only if no errors and if there were transactions in the list
                if (list.size() > 0 && e == null) {
                    // Set the buttons to be 5 pixels from the sides and apart from each other
                    LinearLayout.LayoutParams params = new TableLayout.LayoutParams
                            (TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(5, 5, 5, 0);
                    // List transactions one by one, in reverse chronological order
                    for (int i = list.size() - 1; i >= 0; i--) {
                        ParseObject transaction = list.get(i);
                        // Make the transactions be displayed in nice little buttons so it does the
                        // formatting for us.
                        Button b = new Button(ViewStatementPage.this);
                        // Display the transaction info
                        b.setText("On " + transaction.getCreatedAt().toString() + ":\n" +
                                transaction.getString("type") + ": $" +
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
                else {
                    // display to the user that he has no accounts
                    TextView error = new TextView(ViewStatementPage.this);
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
                ViewStatementPage.this.setContentView(l);
            }
        });

        // Hide action bar for this page
        try {
            getActionBar().hide();
        }catch (Exception e){}
    }

    /**
     * Method onBackPressed
     * Return to the original view accounts page
     */
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, AccountOptions.class);
        intent.putExtra("accountNum", accountNumber);
        startActivity(intent);
    }

}
