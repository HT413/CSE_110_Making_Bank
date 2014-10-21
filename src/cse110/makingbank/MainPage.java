package cse110.makingbank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
/*These following imports pertain to the online server/database
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
UNCOMMENT WHEN READY TO CREATE LOGIN PAGE AND/OR ONLINE STORAGE*/

/**
 * Class: MainPage
 *
 * This is the first page to be called whenever the application is launched.
 * TODO: Add login method later on
 */
public class MainPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* THIS INITIALIZES CONNECTION TO ONLINE SERVER
        Parse.initialize(this, "eUnkK8i47gQC0hsPlTuuzFOge4ASo1sw5AokYoE4",
              "Csx3JZmQdTWWc1EgVQhRZJdxWPUsImUKhOFClM1K"); //App ID for online storage
       UNCOMMENT WHEN READY TO CREATE LOGIN PAGE AND/OR ONLINE STORAGE*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

   /**
    * Function: createAccount
    * This function defines what happens when the user taps on the
    * Create Account button in our main menu.
    *
    * @param view The View object from which this method was called
    */
    public void createAccount(View view){
        //We will transition to this new intent
        Intent intent = new Intent(this, CreateAccountPage.class);
        startActivity(intent);
    }
}
