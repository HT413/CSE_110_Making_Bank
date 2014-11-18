package cse110.makingbank;

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

import java.util.List;

public class TransferSelfPage extends Activity{
    private String theAccount;
    private double balance;

    /**
     * Method onCreate
     * Defines what happens when this activity is created
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // open loading page
        setContentView(R.layout.activity_view_account);
        // Hide action bar
        try {
            getActionBar().hide();
        }catch (Exception e){}

        // Retrieve the data passed in
        Bundle extras = getIntent().getExtras();
        theAccount = extras.getString("accountNum");
        balance = extras.getDouble("maxBalance");

        // retrieve the user info for the account
        String username = ParseUser.getCurrentUser().getUsername();

        // Find all other bank accounts associated with the user
        ParseQuery<ParseObject> query = ParseQuery.getQuery("bankAccount");
        query.whereEqualTo("user", username); // base it on username
        query.findInBackground(new FindCallback<ParseObject>() {
            // store the bank accounts in to a list
            public void done(List<ParseObject> al, ParseException e) {
                // Create a linear layout to place fetched accounts in.
                // Functions similarly to a combo box, so objects can be constantly added in
                LinearLayout layout = new LinearLayout(TransferSelfPage.this);
                layout.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT));
                layout.setOrientation(LinearLayout.VERTICAL);

                // Set the buttons to be 20 pixels from the sides and apart from each other
                LinearLayout.LayoutParams params = new TableLayout.LayoutParams
                        (TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(20, 20, 20, 0);

                // Only display this part if user has more than 1 account, i.e. has more than just
                // the current account that he/she wants to transfer from
                if (e == null && al.size() > 1) {
                    // Display a prompt message to the user
                    TextView notice = new TextView(TransferSelfPage.this);
                    notice.setLayoutParams
                            (new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                                    TableLayout.LayoutParams.WRAP_CONTENT));
                    notice.setX(20);
                    notice.setY(20);
                    notice.setText("Please select an account to transfer money to.");
                    notice.setTextSize(30);
                    notice.setTextColor(Color.GREEN);
                    layout.addView(notice);
                    // Now add all accounts that we can transfer to to the layout
                    for (ParseObject account: al) {
                        final String accountNum = account.getString("accountNumber");
                        // Only display the account to choose if it differs from the current one
                        if (!accountNum.equals(theAccount)) {
                            // make them a button so that we can view in more detail
                            Button b = new Button(TransferSelfPage.this);
                            String bText = account.getString("accountType") + " account " + accountNum;
                            b.setText(bText);
                            b.setBackgroundResource(R.drawable.button_round_corners);
                            b.setLayoutParams(params);

                            // Go to account transaction history page to view the account
                            b.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    Intent intent = new Intent(TransferSelfPage.this,
                                            AccountOptions.class);
                                    intent.putExtra("transferFrom", theAccount);
                                    intent.putExtra("transferTo", accountNum);
                                    intent.putExtra("maxBalance", balance);
                                    TransferSelfPage.this.startActivity(intent);
                                }
                            });
                            layout.addView(b);
                        }
                    }
                } else // Display a notice if no other accounts are found
                {
                    // display to the user that he has no accounts
                    TextView notice = new TextView(TransferSelfPage.this);
                    notice.setLayoutParams
                            (new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));
                    notice.setX(20);
                    notice.setY(20);
                    notice.setText("You have no other accounts. Press the back button to return.");
                    notice.setTextSize(30);
                    notice.setTextColor(Color.RED);
                    layout.addView(notice);
                }
                TransferSelfPage.this.setContentView(layout);
            }
        });
    }

    /**
     * Method onBackPressed
     * Go back to the account options page if pressed
     */
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, AccountOptions.class);
        intent.putExtra("accountNum", theAccount);
        this.startActivity(intent);
    }
}
