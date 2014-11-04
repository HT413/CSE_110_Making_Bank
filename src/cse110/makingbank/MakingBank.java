package cse110.makingbank;

import android.app.Application;

import com.parse.*;

/**
 * Class: MainPage
 *
 * This is the first page to be called whenever the application is launched.
 */
public class MakingBank extends Application {

    /**
     * Method onCreate
     * The first method to be called when this application is created.
     */
    public void onCreate() {
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "eUnkK8i47gQC0hsPlTuuzFOge4ASo1sw5AokYoE4",
                "Csx3JZmQdTWWc1EgVQhRZJdxWPUsImUKhOFClM1K"); //App ID for online storage
    }
}