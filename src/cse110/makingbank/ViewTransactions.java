package cse110.makingbank;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewTransactions extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_transactions);
		
        LinearLayout trans = new LinearLayout(this);
        trans.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        trans.setOrientation(LinearLayout.VERTICAL);
        TextView transList = new TextView(this);
        transList.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        transList.setText("No Transactions");
        trans.addView(transList);
        setContentView(trans);
		
	}
}
