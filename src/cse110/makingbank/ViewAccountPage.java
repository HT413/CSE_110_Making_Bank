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
import android.widget.TableLayout.LayoutParams;

/**
 * Class: ViewAccountPage
 *
 * This page shows the account information.
 */

public class ViewAccountPage extends Activity{

	/**
	 * @param savedInstanceState
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // open loading page
        setContentView(R.layout.activity_view_account);
        
        // retrieve the user info for the account
        ParseUser current = ParseUser.getCurrentUser();
        String username = current.getUsername();
        
        // find the bank accounts
        ParseQuery<ParseObject> query = ParseQuery.getQuery("bankAccount");
        query.whereEqualTo("user", username); // base it on username
        query.findInBackground( new FindCallback<ParseObject>() {
        	// store the bank accounts in to a list
        	public void done(List<ParseObject> al, ParseException e) 
        	{
        		// create a layout to place them in
                LinearLayout l = new LinearLayout(ViewAccountPage.this);
                l.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                l.setOrientation(LinearLayout.VERTICAL);
        		// if we found accounts
        		if ( e == null )
        		{
        			// add the layouts to the view
                    for ( int i = 0; i < al.size(); i++ )
                    {
            	        ParseObject account = al.get(i);
                        // make them a button so that we can view in more detail
                        Button b = new Button(ViewAccountPage.this);
                        String bText = account.getString("accountType")+ " account.      " + "Bal: $"+account.getDouble("balance");
                        b.setText(bText);
                        b.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                        // make it so if u click them then it shows transaction history
                        b.setOnClickListener( new View.OnClickListener() {
                        	public void onClick(View v)
                        	{
                        		Intent intent = new Intent(ViewAccountPage.this, ViewTransactions.class);
                        		ViewAccountPage.this.startActivity(intent);
                        	}
                        });
                        l.addView(b);
                    }
        		}
        		else // if no accounts
        		{
        			// display to the user that he has no accounts
        			TextView poor = new TextView(ViewAccountPage.this);
                    poor.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    poor.setText("You have no accounts");
        		}
                ViewAccountPage.this.setContentView(l);
        	}
        } );

        // display the bank accounts
        // if no accounts found
    }
}
