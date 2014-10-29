package cse110.makingbank;

import java.util.List;

import com.parse.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Class: ViewAccountPage
 *
 * This page shows the account information.
 */

public class ViewAccountPage extends Activity{

	protected List<ParseObject> accountList = null;

	/**
	 * @param savedInstanceState
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_account);
        
        // retrieve the user info for the account
        ParseUser current = ParseUser.getCurrentUser();
        String username = current.getUsername();
        
        // find the bank accounts
        ParseQuery<ParseObject> query = ParseQuery.getQuery("BankAccount");
        query.whereEqualTo("user", username); // base it on username
        query.findInBackground( new FindCallback<ParseObject>() {
        	public void done(List<ParseObject> al, ParseException e) 
        	{
        		// if we found accounts
        		if ( e == null )
        		{
        			// store the list into the class
        			ViewAccountPage.this.accountList = al;
        		}
        		else
        		{
        			//user has no bank accounts
        		}
        	}
        } );
        
        LinearLayout l = new LinearLayout(this);
        // display the bank accounts
        for ( int i = 0; i < accountList.size(); i++ )
        {
        	ParseObject account = accountList.remove(i);
        	// make them a button so that we can view in more detail
        	TextView b = new TextView(this);
        	String bText = "Account Type:" + account.getString("accountType") + "\tBal: 0";
        	b.setText(bText);
        	l.addView(b);
        }
        setContentView(l);
    }
}
