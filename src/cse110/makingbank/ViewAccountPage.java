package cse110.makingbank;

import java.util.List;

import com.parse.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
	 * Method onCreate
     * Create a list of all accounts belonging to the user.
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // open loading page
        setContentView(R.layout.activity_view_account);
        // Hide action bar
        try {
            getActionBar().hide();
        }catch (Exception e){}
        
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
                // create a layout to fetched accounts them in
                LinearLayout l = new LinearLayout(ViewAccountPage.this);
                l.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT));
                l.setOrientation(LinearLayout.VERTICAL);

                // Set the buttons to be 30 pixels from the sides and apart from each other
                LinearLayout.LayoutParams params = new LayoutParams
                        (LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                params.setMargins(30, 30, 30, 0);

                // if we found accounts
                if ( e == null && al.size() != 0 ) {
                    // add the layouts to the view
                    for (ParseObject account : al) {
                        // make them a button so that we can view in more detail
                        Button b = new Button(ViewAccountPage.this);
                        String bText = account.getString("accountType") + " account " +
                                                          account.get("nickname");
                        b.setText(bText);
                        b.setBackgroundResource(R.drawable.button_round_corners);
                        b.setLayoutParams(params);

                        // Go to account transaction history page to view the account
                        b.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                Intent intent = new Intent(ViewAccountPage.this,
                                        ViewTransactions.class);
                                ViewAccountPage.this.startActivity(intent);
                            }
                        });
                        l.addView(b);
                    }
                }
                else // if no accounts are found
                {
                    // display to the user that he has no accounts
                    TextView poor = new TextView(ViewAccountPage.this);
                    poor.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT));
                    poor.setText("You have no accounts");
                }
                ViewAccountPage.this.setContentView(l);
               }
        } );
    }

    /**
     * Go back to the bank home page if back button is pressed
     */
    @Override
    public void onBackPressed(){
        Intent intent = new Intent (this, BankHomePage.class);
        startActivity(intent);
    }
}
