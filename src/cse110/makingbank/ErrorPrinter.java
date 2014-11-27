package cse110.makingbank;

import android.widget.TextView;

/**
 * Class error printer
 * A central helper class that stores the appropriate error messages for each rule.
 * This way, we don't need to put magic constants in classes that utilize the
 * Rule objects, since the error codes are coordinated here.
 */
public class ErrorPrinter {
    private int errorFlags;
    private String errorMessage;

    /**
     * Constructor method for the class
     * @param code the error codes that we will work with
     */
    public ErrorPrinter(int code){
        errorFlags = code;
        errorMessage = "";
        determineErrors();
    }

    /**
     * Method determineErrors
     * Determine the errors for this object and update the appropriate error message
     */
    public void determineErrors(){
        // UPDATE THIS AS NEW ERROR CODES ARE CREATED, PLEASE
        if (((errorFlags >> 0) & 0x1) == 1){
            errorMessage += "ERROR: EMPTY INPUT FIELD FOUND!\n";
        }
    }

    /**
     * Method printError
     * Prints out the errors associated with whatever had an error
     *
     * @param displayer The TextView that displays the message
     */
    public void printError(TextView displayer){
        if (errorMessage.equals("")) // Just in case there wasn't an error
            errorMessage = displayer.getText().toString();
        displayer.setText(errorMessage);
    }
}
