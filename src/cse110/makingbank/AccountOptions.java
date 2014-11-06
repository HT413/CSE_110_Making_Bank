package cse110.makingbank;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class AccountOptions extends Activity {

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

        // Get the passed in data
        Bundle extras = getIntent().getExtras();
        String accountName = extras.getString("accountName");
        TextView pageTitle = (TextView) findViewById(R.id.accountInfoPageTitle);
        pageTitle.setText(pageTitle.getText().toString() + " " + accountName);

        // Display the account balance
        // First, query for the account
        ParseQuery<ParseObject> query = ParseQuery.getQuery("bankAccount");
        query.whereEqualTo("accountNumber", accountName);
        query.findInBackground( new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, ParseException e){
                if (list.size() > 0 && e == null){
                    TextView balance = (TextView) findViewById(R.id.currentBalance);
                    balance.setText(balance.getText().toString() + " " + list.get(0).getDouble("balance"));
                }
                else{
                    TextView balance = (TextView) findViewById(R.id.currentBalance);
                    balance.setText("NETWORK CONNECTION ERROR!");
                }
            }
        });
	}

    /**
     * Method accountAddMoney
     * Change layout to credit the account
     */
    public void accountAddMoney (View view){

    }

    /**
     * Method accountDeductMoney
     * Change layout to debit the account
     */
    public void accountDeductMoney(View view){

    }
}
