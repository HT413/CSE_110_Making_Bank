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
        results = new DatabaseQuery("bankAccount", "user", user).get();
    }

    public String getMessage(){
        if (results != null) {
            for (ParseObject account : results) {
                if (account.getDouble("balance") < account.getInt("threshold"))
                    message += "\nAccount " + account.getString("accountNumber") +
                            " is under $" + account.getInt("threshold") + "!";
            }
            if (message.equals(""))
                return "Have a nice day!";
            else
                return "You have accounts with low funds!" + message;
        }
        return "NO INTERNET CONNECTION";
    }
}
