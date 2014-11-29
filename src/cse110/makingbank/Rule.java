package cse110.makingbank;

import android.widget.EditText;
import android.widget.TextView;

/**
 * Class Rule
 * This is a base Rule object.
 * Can be extended for custom tailored rule checking.
 */
public class Rule {
    protected EditText[] inputs;
    protected int errorFlags; // For determining the error flags
    private String errorMessage; // For determining the error message to display
    // Below are error flags for use in the application.
    // Please set successive bits to 1 if new error codes are created.
    protected final int EMPTY_INPUT = 0x00000001;
    protected final int USERNAME_SHORT = 0x00000002;

    /**
     * Constructor method for the Rule object
     * @param args An array of arguments, typically the input fields
     */
    public Rule (EditText[] args){
        inputs = args;
        errorFlags = 0;
    }

    /**
     * Method checkErrors
     * This method will perform all error checks defined for the class.
     * By default, the object will only check if the input fields are not empty.
     * Should be overridden by extending classes.
     */
    public void checkErrors(){
        checkEmpty();
    }

    /**
     * Method checkNotEmpty
     * This method will check if any input fields are empty.
     * Sets the appropriate error flag for the rule object.
     */
    public void checkEmpty(){
        for (EditText input: inputs){
            // Check if the input is empty
            if (input.getText().toString().equals(""))
                errorFlags |= EMPTY_INPUT; // If yes, set error flag
        }
    }

    /**
     * Method hasErrors()
     * This method determines if there was some error based on the rules we use
     *
     * @return Returns true if error flags are set, false otherwise
     */
    public boolean hasErrors(){
        checkErrors(); // Check if there are any errors
        return errorFlags != 0;
    }

    /**
     * Method getErrorCode
     * This method will return the error code to the user.
     * @return Returns the error flags.
     */
    public int getErrorCode(){
        return errorFlags;
    }

    /**
     * Method determineErrors
     * Determine the errors for this object and update the appropriate error message
     */
    private void determineErrorMessages(){
        // UPDATE THIS AS NEW ERROR CODES ARE CREATED, PLEASE
        if (((errorFlags >> 0) & 0x1) == 1){
            errorMessage += "EMPTY INPUT FIELD FOUND!\n";
        }
        if (((errorFlags >> 1) & 0x1) == 1){
            errorMessage += "USERNAME MUST BE AT LEAST 4 CHARACTERS LONG!\n";
        }
    }

    /**
     * Method printError
     * Prints out the errors associated with whatever had an error
     *
     * @param display The TextView that displays the message
     */
    public void printError(TextView display){
        determineErrorMessages();
        if (errorMessage.equals("")) // Just in case there wasn't an error
            errorMessage = display.getText().toString();
        display.setText(errorMessage);
    }
}
