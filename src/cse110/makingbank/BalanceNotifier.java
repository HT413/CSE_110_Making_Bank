package cse110.makingbank;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Class BalanceNotifier
 * A helper class that checks which accounts' balances the user needs to be notified about
 */
public class BalanceNotifier{
    private String message = "";
    private List<ParseObject> results;
    BankHomePage homePage;

    public BalanceNotifier(BankHomePage caller, String user){
        homePage = caller;

        // Fetch all user accounts first
        ParseQuery<ParseObject> query = ParseQuery.getQuery("bankAccount");
        // Search based on the current user
        query.whereEqualTo("user", user);
        try {
            results = query.find();
        }catch(Exception e){
            results = null;
        }
    }

    public String getMessage(){
        if (results != null) {
            for (ParseObject account : results) {
                if (account.getDouble("balance") < account.getInt("threshold"))
                    message += "\nAccount " + account.getString("accountNumber") +
                            " is way over $" + account.getInt("threshold") + "!";
            }
            if (message.equals(""))
                return "Have a nice day!";
            else
                return "Dude, spend more money!" + message;
        }
        return "NO INTERNET CONNECTION";
    }
}
