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
    private int loginAttempts; // For keeping track of login attempts

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
        loginAttempts = 0;
    }

    /**
     * Method: submitLogin
     * This method is called when the user presses the Sign In button.
     * Completes the sign in and moves to the home page if successful.
     *
     * @param view The button that activated this method
     */
    public void submitLogin(View view){
        ++loginAttempts;
        final EditText usernameField = (EditText) findViewById(R.id.EditTextUserName);
        final EditText passwordField = (EditText) findViewById(R.id.EditTextPassword);

        // Get the inputs from each field
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        // Now log in
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null && e == null) { // No exception, user can log in normaly
                    if (user.getBoolean("emailVerified"))
                        completeLogin();
                    else{
                        TextView pageNotice = (TextView) findViewById(R.id.loginPagePrompt);
                        pageNotice.setText ("Email not verified!");
                    }
                } else { // Login failed
                    TextView pageNotice = (TextView) findViewById(R.id.loginPagePrompt);
                    pageNotice.setText ("Invalid username or password.");
                    if (loginAttempts > 5){ // We don't want malicious login attempts
                        verifyHuman();
                    }
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
        // Determines which page to transition to.
        if (ParseUser.getCurrentUser().getBoolean("givenInfo") &&
            !ParseUser.getCurrentUser().getUsername().equals("admin")) {
            Intent intent = new Intent(this, BankHomePage.class);
            startActivity(intent);
        }
        // Check if this is the admin. If yes, go to admin page
        else if (ParseUser.getCurrentUser().getUsername().equals("admin")){
                Intent intent = new Intent (this, AdminHomePage.class);
                startActivity(intent);
            }
        else{
            Intent intent = new Intent(this, MyInfo.class);
            startActivity(intent);
        }
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
        // Get the input fields
        EditText usernameField = (EditText) findViewById(R.id.EditTextUserNameR);
        EditText passwordField = (EditText) findViewById(R.id.EditTextPasswordR);
        EditText emailField = (EditText) findViewById(R.id.EditTextEmailR);
        EditText questionField = (EditText) findViewById(R.id.EditTextSecurityQuestion);
        EditText answerField = (EditText) findViewById(R.id.EditTextAnswerR);
        // Check validity of inputs
        EditText[] inputs = {usernameField, passwordField, emailField,
                questionField, answerField};
        Rule checker = new AppRegisterRule(inputs);
        // Determine if there are errors are not
        if (checker.hasErrors()){
            TextView pageNotice = (TextView) findViewById(R.id.registerPagePrompt);
            checker.printError(pageNotice);
        }
        else{ // Sign up the user if no errors. Cannot be done in a helper class
            ParseUser user = new ParseUser();
            user.setUsername(usernameField.getText().toString());
            user.setPassword(passwordField.getText().toString());
            user.setEmail(emailField.getText().toString());
            user.put ("securityQuestion", questionField.getText().toString());
            user.put ("questionAnswer", answerField.getText().toString());
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
                        else
                            pageNotice.setText ("Server connection error!");
                    }
                }
            });
        }
    }

    /**
     * Method passwordReset
     * User must enter their security question answer, then get sent a password reset email
     */
    public void passwordReset(View view){
        Intent intent = new Intent (this, PasswordReset.class);
        startActivity(intent);
    }

    /**
     * Method verifyHuman
     * Go to the verification page to make sure this user is human
     */
    private void verifyHuman(){
        Intent intent = new Intent(this, HumanityCheck.class);
        startActivity(intent);
    }
}
