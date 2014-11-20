package cse110.makingbank;

import java.math.BigDecimal;
import java.math.RoundingMode;

// Class RoundDouble
// This class rounds a double to a specified number of decimal digits
public class Round {
    private double val;

    public Round(double value, int places){
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_DOWN);
        val = bd.doubleValue();
    }

    // Method toDouble
    // Returns the value that was rounded
    public double toDouble(){
        return val;
    }
}
