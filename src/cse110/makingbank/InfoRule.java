package cse110.makingbank;

import android.widget.EditText;

/**
 * Class InfoRule
 * A more detailed rule that extends the base Rule class
 */
public class InfoRule extends Rule{

    public InfoRule(EditText[] args) {
        super(args);
    }

    /**
     * Method checkErrors
     * Extend this rule object to also check whether entries have valid formats
     */
    public void checkErrors(){
        super.checkErrors();
        checkZIP();
    }

    /**
     * Method checkZIP
     * Checks if the ZIP code entered was valid or not
     */
    private void checkZIP(){
        if (inputs[4].getText().toString().length() < 4)
            errorFlags |= ZIP_SHORT;
    }
}
