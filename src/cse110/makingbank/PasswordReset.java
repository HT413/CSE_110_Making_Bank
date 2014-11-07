package cse110.makingbank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import java.util.List;

// This is the password reset class to help user reset their password!
public class PasswordReset extends Activity {

    private TextView pagePrompt;
    private EditText fillForm;
    private boolean checkAccount;
    private ParseUser currentUser;
    private TextView message;

    /**
     * Method onCreate
     * The first method to be called when this object is created.
     *
     * @param savedInstanceState a saved instance of the object
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset_query);
        // Hide action bar
        try {
            getActionBar().hide();
        }catch (Exception e){}
        checkAccount = true;
        pagePrompt = (TextView) findViewById(R.id.resetPrompt);
        fillForm = (EditText) findViewById(R.id.queryForm);
        message = (TextView) findViewById(R.id.resetPageTitle);
    }

    /**
     * Method onBackPressed
     * Return to login page
     */
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
    }

    /**
     * Method submitReset
     * This method will check what the user submits for filling in
     */
    public void submitReset (View view){
        final String submittedInfo = fillForm.getText().toString();
        if (checkAccount) {
            // Check if user exists
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("username", submittedInfo);
            query.findInBackground( new FindCallback<ParseUser>() {
                public void done(List<ParseUser> list, ParseException e) {
                    if (list.size() > 0 && e == null && !submittedInfo.equals("admin")){
                        // Get current user and have them answer security question
                        currentUser = list.get(0);
                        pagePrompt.setText(currentUser.getString("securityQuestion"));
                        checkAccount = false;
                        fillForm.setText("");
                        fillForm.setHint("Answer");
                        message.setText(R.string.page_title_reset);
                    }
                    else { // ERROR!
                        message.setText("INVALID USERNAME");
                    }
                }
            });
        }
        else{ // Check security question answer
            String answer = currentUser.getString("questionAnswer");
            // Match, send password reset email
            if (submittedInfo.equals(answer)){
                ParseUser.requestPasswordResetInBackground(currentUser.getString("email"),
                    new RequestPasswordResetCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                message.setText("Password reset email sent. Please press the Back" +
                                                " button to return.");
                            } else {
                                message.setText("NETWORK CONNECTION ERROR");
                            }
                        }
                    });
            }
            else{
                message.setText("Wrong answer! Please try again.");
            }
        }
    }
}
