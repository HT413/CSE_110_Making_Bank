package cse110.makingbank;

import com.parse.ParseObject;

/* Class TransactionLog
 * This class will create a transaction log and save it onto the database
 */
public class TransactionLog{
    public TransactionLog(double balanceBefore, double amountChanged,
                          double balanceAfter, String transactionType, String accountNumber){
        // Log this successful transaction and save it
        ParseObject transaction = new ParseObject("transaction");
        transaction.put("account", accountNumber);
        transaction.put("type", transactionType);
        transaction.put("before", balanceBefore);
        transaction.put("amount", amountChanged);
        transaction.put("after", balanceAfter);
        // Save the transaction to the database.
        transaction.saveInBackground();
    }
}
