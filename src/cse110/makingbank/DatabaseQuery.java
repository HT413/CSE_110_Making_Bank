package cse110.makingbank;

import android.content.Intent;
import android.os.CountDownTimer;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Class DatabaseQuery
 * A helper class that queries a database and returns a list of found ParseObject objects
 */
public class DatabaseQuery{
    private List results;

    public DatabaseQuery (String className, String key, String value){
        // A timer to time whether the application connects to the Internet or not
        // It's a bonus feature!
        CountDownTimer timer = new CountDownTimer(20000, 20000) {
            public void onTick(long l) {}
            public void onFinish() { noInternet();}
        };
        timer.start();

        ParseQuery<ParseObject> query = ParseQuery.getQuery(className);
        query.whereEqualTo(key, value);
        try {
            results = query.find();
        }catch(Exception e){}
    }

    /**
     * Method get
     * Returns the list of query results
     */
    public List<ParseObject> get(){
        return results;
    }

    /**
     * Overloaded method get
     * Returns a specific object in the list of query results
     */
    public ParseObject get(int index){
        if (results.size() <= index)
            return null;
        else
            return (ParseObject) results.get(index);
    }

    private void noInternet(){
        BankHomePage b = new BankHomePage();
        b.startActivity(new Intent(b, TeleportingButton.class));
    }
}
