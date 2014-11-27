package cse110.makingbank;

import android.widget.EditText;

/**
 * Class Rule
 * This is a base Rule object.
 * Can be extended for custom tailored rule checking.
 */
public class Rule {
    protected EditText[] inputs;
    protected int errorFlags; // For determining the error flags
    protected final int emptyInput = 0x00000001; // Used for setting the error flags

    /**
     * Constructor method for the Rule object
     *
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
                errorFlags |= emptyInput; // If yes, set error flag
        }
    }

    /**
     * Method hasErrors()
     * This method determines if there was some error based on the rules we use
     *
     * @return Returns true if error flags are set, false otherwise
     */
    public boolean hasErrors(){
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
}
