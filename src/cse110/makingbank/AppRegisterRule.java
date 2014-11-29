package cse110.makingbank;

import android.widget.EditText;

/**
 * Class AppRegisterRule
 * A more detailed rule that extends the base Rule class
 */
public class AppRegisterRule extends Rule{
    public AppRegisterRule(EditText[] args){
        super(args);
    }

    /**
     * Method checkErrors
     * Extend this rule object to also check whether the username is long enough
     */
    @Override
    public void checkErrors(){
        super.checkErrors();
        checkUsernameLength();
    }

    /**
     * Method checkUsernameLength
     * Check the length of the username to ensure that it is sufficiently long
     */
    private void checkUsernameLength(){
        if (inputs[0].getText().toString().length() < 4)
            errorFlags |= USERNAME_SHORT;
    }
}
