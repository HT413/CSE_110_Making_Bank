package cse110.makingbank;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TableLayout.LayoutParams;

import com.parse.*;

/**
 * Class: ViewAccountPage
 * This page lists all accounts belonging to the current user, and allows the user
 * to select an account to perform transactions on.
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
        String username = ParseUser.getCurrentUser().getUsername();

        // find the bank accounts
        ParseQuery<ParseObject> query = ParseQuery.getQuery("bankAccount");
        query.whereEqualTo("user", username); // base it on username
        query.whereEqualTo("isActive", true); // Must be an active account
        query.findInBackground( new FindCallback<ParseObject>() {
        	// store the bank accounts in to a list
        	public void done(List<ParseObject> al, ParseException e)
        	{
                // create a layout to put fetched accounts in
                LinearLayout layout = new LinearLayout(ViewAccountPage.this);
                layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT));
                layout.setOrientation(LinearLayout.VERTICAL);

                // Set the buttons to be 30 pixels from the sides and apart from each other
                LinearLayout.LayoutParams params = new LayoutParams
                        (LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                params.setMargins(30, 30, 30, 0);

                // Display accounts only if we found any
                if ( e == null && al.size() != 0 ) {
                    for (int i = 0; i < al.size(); i++) {
                        final ParseObject account = al.get(i);
                        // Add them as a list of buttons
                        Button b = new Button(ViewAccountPage.this);
                        final String number = account.getString("accountNumber");
                        String bText = account.getString("accountType") + " account " + number;
                        b.setText(bText);
                        b.setBackgroundResource(R.drawable.button_round_corners);
                        b.setLayoutParams(params);

                        // Go to account options page if button is pressed
                        b.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                Intent intent = new Intent(ViewAccountPage.this,
                                        AccountOptions.class);
                                intent.putExtra("accountNum", number);
                                ViewAccountPage.this.startActivity(intent);
                            }
                        });
                        layout.addView(b);
                    }
                }
                else // No accounts found
                {
                    // Give user a warning if no accounts are found
                    TextView notice = new TextView(ViewAccountPage.this);
                    notice.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT));
                    notice.setX(20);
                    notice.setY(20);
                    notice.setText("You have no accounts. Press the back button to return.");
                    notice.setTextSize(30);
                    notice.setTextColor(Color.RED);
                    layout.addView(notice);
                }
                ViewAccountPage.this.setContentView(layout);
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
