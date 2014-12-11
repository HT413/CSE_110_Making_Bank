package cse110.makingbank;

import android.app.Application;

import com.parse.*;

/**
 * Class: MakingBank
 *
 * This is the first class to be created whenever the application is launched.
 */
public class MakingBank extends Application {

    /**
     * Method onCreate
     * The first method to be called when this application is created.
     * This will establish a connection to the Parse server, so we can do
     * server related stuff later on.
     */
    public void onCreate() {
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "eUnkK8i47gQC0hsPlTuuzFOge4ASo1sw5AokYoE4",
                "Csx3JZmQdTWWc1EgVQhRZJdxWPUsImUKhOFClM1K"); //App ID for online storage
    }
}