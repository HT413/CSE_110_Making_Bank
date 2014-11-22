package cse110.makingbank;

import android.content.Intent;

import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Class AccountDeleter
 * A helper class that deletes an account
 */
public class AccountDeleter{
    private ParseUser currentUser;

    public AccountDeleter (ParseObject toDelete){
        currentUser = ParseUser.getCurrentUser();
        // Close the account
        toDelete.deleteInBackground();
        // Decrement the accounts count for the user
        int oldIndex = currentUser.getInt("numAccounts");
        currentUser.put("numAccounts", oldIndex - 1);
        currentUser.saveInBackground();
    }
}
