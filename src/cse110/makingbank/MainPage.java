package cse110.makingbank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.parse.*;

/**
 * Class: MainPage
 *
 * This is the first page to be called whenever the application is launched.
 * TODO: Add login method later on
 */
public class MainPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Parse.initialize(this, "eUnkK8i47gQC0hsPlTuuzFOge4ASo1sw5AokYoE4",
              "Csx3JZmQdTWWc1EgVQhRZJdxWPUsImUKhOFClM1K"); //App ID for online storage
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
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
                if (user != null && e == null) { // No exception, user can log in normally
                    completeLogin();
                } else {
                    // TODO Logging in failed. Look at the ParseException to see what happened.
                }
            }
        });
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
        final EditText usernameField = (EditText) findViewById(R.id.EditTextUserNameR);
        final EditText passwordField = (EditText) findViewById(R.id.EditTextPasswordR);
        final EditText emailField = (EditText) findViewById(R.id.EditTextEmailR);

        // Get the inputs from each field
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        String email = emailField.getText().toString();

        // Complete the registration if the user filled in all fields
        if (username != null && password != null && email != null){
            ParseUser user = new ParseUser();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            // Complete the registration and go back to the login screen
            user.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) { // No exception, we can proceed normally
                        setContentView(R.layout.activity_log_in);
                    } else {
                        // TODO Sign up didn't succeed. Look at the ParseException
                        // to figure out what went wrong
                    }
                }
            });
        }
        // Not all fields complete, give an error message until the user fills in everything
        else{
            //TODO give error notice
        }
    }
}
