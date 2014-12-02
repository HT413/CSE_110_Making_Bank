package cse110.makingbank;

import android.content.Intent;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Class AccountDeleter
 * A helper class that deletes an account
 */
public class AccountDeleter{
    private ParseUser currentUser;
    private String accountNum;

    public AccountDeleter (ParseObject toDelete){
        currentUser = ParseUser.getCurrentUser();
        accountNum = toDelete.getString("accountNumber");
        // Close the account by making it inactive
        toDelete.put("isActive", false);
        toDelete.saveInBackground();
        // Change the default account if this was the previous default
        if (currentUser.getString("defaultAccount").equals(accountNum))
            changeDefault();
    }

    /**
     * Method changeDefault()
     * Change the default account of the user
     */
    private void changeDefault(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("bankAccount");
        query.whereEqualTo("user", currentUser.getUsername()); // base it on username
        query.whereEqualTo("isActive", true);
        query.findInBackground(new FindCallback<ParseObject>() {
            // Store the bank accounts in to a list
            public void done(List<ParseObject> list, ParseException e) {
                // Make the first account found be the default account
                if (list.size() > 0){
                    currentUser.put("defaultAccount", list.get(list.size() - 1)
                            .getString("accountNumber"));
                }
                // Otherwise, user has no default account
                else {
                    currentUser.put("defaultAccount", "0");
                }
                currentUser.saveInBackground();
            }
        });
    }
}
