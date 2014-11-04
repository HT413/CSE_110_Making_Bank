package cse110.makingbank;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class ViewTransactions extends Activity {

    /**
     * Method onCreate
     * Create the ViewTransactions page
     */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_transactions);
        // Hide action bar
        try {
            getActionBar().hide();
        }catch (Exception e){}
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
