package cse110.makingbank;

import android.os.CountDownTimer;

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

        // A timer to time whether the application connects to the Internet or not
        // It's a bonus feature!
        CountDownTimer timer = new CountDownTimer(20000, 20000) {
            public void onTick(long l) {}
            public void onFinish() { noInternet();}
        };
        timer.start();

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
                            " is under $" + account.getInt("threshold") + "!";
            }
            if (message.equals(""))
                return "Have a nice day!";
            else
                return "You have accounts with low funds!" + message;
        }
        return "NO INTERNET CONNECTION";
    }

    /**
     * Method noInternet
     * Defines what happens when there's no internet. It's a bonus feature!
     */
    private void noInternet(){
        homePage.noInternet();
    }
}
