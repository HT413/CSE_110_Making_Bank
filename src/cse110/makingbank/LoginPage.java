package cse110.makingbank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.*;

/**
 * Class: MainPage
 *
 * This is the first page to be called whenever the application is launched.
 */
public class LoginPage extends Activity {

    /**
     * Method onCreate
     * The first method to be called when this object is created.
     *
     * @param savedInstanceState a saved instance of the object
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        // Hide action bar
        try {
            getActionBar().hide();
        }catch (Exception e){}
    }

    /**
     * Method: submitLogin
     * This method is called when the user presses the Sign In button.
     * Completes the sign in and moves to the home page if successful.
     *
     * @param view The button that activated this method
     */
    public void submitLogin(View view){
        final EditText usernameField = (EditText) findViewById(R.id.EditTextUserName);
        final EditText passwordField = (EditText) findViewById(R.id.EditTextPassword);

        // Get the inputs from each field
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        // Now log in
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null && e == null) { // No exception, user can log in normaly
                    if (ParseUser.getCurrentUser().getBoolean("emailVerified"))
                        completeLogin();
                    else{
                        TextView pageNotice = (TextView) findViewById(R.id.loginPagePrompt);
                        pageNotice.setText ("Email not verified!");
                    }
                } else { // Login failed
                    TextView pageNotice = (TextView) findViewById(R.id.loginPagePrompt);
                    pageNotice.setText ("Invalid username or password.");
                }
            }
        });
    }

    /**
     * Go back to login screen if back button pressed
     */
    @Override
    public void onBackPressed(){
        setContentView(R.layout.activity_log_in);
    }

    /**
     * Method: completeLogin
     * Defines what happens when the user successfully logs in.
     */
    private void completeLogin(){
        Intent intent = new Intent(this, BankHomePage.class);
        startActivity(intent);
    }

    /**
     * Method: goRegister
     * This method is called when the user presses the Sign Up button.
     * Transitions to the register page
     *
     * @param view  The button that activated this method
     */
    public void goRegister(View view){
        //Bring up the sign up page
        setContentView(R.layout.activity_sign_up);
    }

    /**
     * Function: submitSignUp
     * This function defines what happens when the user taps on the
     * Register! button in the sign up form.
     *
     * @param view The View object from which this method was called
     */
    public void submitSignUp(View view){
        EditText usernameField = (EditText) findViewById(R.id.EditTextUserNameR);
        EditText passwordField = (EditText) findViewById(R.id.EditTextPasswordR);
        EditText emailField = (EditText) findViewById(R.id.EditTextEmailR);
        EditText securityField = (EditText) findViewById(R.id.EditTextAnswerR);
        EditText securityQuestionField = (EditText) findViewById(R.id.EditTextSecurityQuestion);

        // Get the inputs from each field
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        String email = emailField.getText().toString();
        String answer = securityField.getText().toString();
        String question = securityQuestionField.getText().toString();

        // Complete the registration if the user filled in all fields
        if (!username.equals("") && !password.equals("") && !email.equals("")
                && !answer.equals("") && !question.equals("")){
            ParseUser user = new ParseUser();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.put ("securityQuestion", question);
            user.put ("questionAnswer", answer);
            user.put ("numAccounts", 0);
            user.put ("isAdmin", false);
            // Complete the registration and go back to the login screen
            user.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) { // No exception, we can proceed normally
                            setContentView(R.layout.activity_log_in);
                    } else { // Invalid username throws error
                        TextView pageNotice = (TextView) findViewById(R.id.registerPagePrompt);
                        if (e.getCode() == ParseException.USERNAME_TAKEN)
                            pageNotice.setText ("Username already taken!");
                        else if (e.getCode() == ParseException.INVALID_EMAIL_ADDRESS)
                            pageNotice.setText ("Invalid email!");
                        else if (e.getCode() == ParseException.EMAIL_TAKEN)
                            pageNotice.setText ("Email already taken!");
                        else if (e.getCode() == ParseException.CONNECTION_FAILED)
                            pageNotice.setText ("Failed to connect to server!");
                        else
                            pageNotice.setText ("Something went wrong and we don't know why!");
                    }
                }
            });
        }
        // Not all fields complete, give an error message until the user fills in everything
        else{
            TextView pageNotice = (TextView) findViewById(R.id.registerPagePrompt);
            pageNotice.setText ("One or more fields are missing.");
        }
    }
}
