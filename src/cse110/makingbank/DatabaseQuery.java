package cse110.makingbank;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Class DatabaseQuery
 * A helper class that queries a database and returns a list of found ParseObject objects
 */
public class DatabaseQuery{
    private List results;

    public DatabaseQuery (String className, String key, String value){
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
}
