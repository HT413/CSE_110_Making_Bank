/** IMPORTANT NOTICE
 *  This file is a direct copy + paste from the source code of the
 *  "Teleporting Button" app, designed by Hoang Tran in November, 2014.
 *  This layout file is only to be loaded when the device goes offline, to
 *  allow the user to kill time while waiting to reconnect to the Internet.
 *  DO NOT REPRODUCE THIS FILE WITHOUT PERMISSION
 *
 *  Author: Hoang Tran
 *  Original Source: "Teleporting Button" app, by Hoang Tran (architect,
 *  developer, and business developer of team Making Bank), Google Play Store */
package cse110.makingbank;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/**
 * Class: TeleportingButton
 * This will be the main menu page for the app.
 *
 * Author: Hoang Tran
 */
public class TeleportingButton extends Activity {

    // For playing sounds
    private MediaPlayer mMediaPlayer;
    private Uri chime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.offline_activity_main_page);

        // Sound files
        mMediaPlayer = new MediaPlayer();
        chime = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ceg_chime);
    }

    /**
     * Override back button activity; simply sends user back to main menu.
     */
    @Override
    public void onBackPressed(){
        setContentView(R.layout.offline_activity_main_page);
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
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Time to start the game, but first, select the game mode!
     */
    public void startGame(View view){
        playSound(chime);
        setContentView(R.layout.offline_select_game_page);
    }


    // THIS SECTION PERTAINS TO THE INSTRUCTIONS PAGE
    /**
     * Instruction button pressed. Bring up instructions page.
     */
    public void helpPage(View view){
        playSound(chime);
        setContentView(R.layout.offline_help_page);
    }

    /**
     * Go back to the main menu
     */
    public void backToMenu(View view){
        playSound(chime);
        setContentView(R.layout.offline_activity_main_page);
    }



    // THIS SECTION PERTAINS TO THE HIGH SCORES PAGE

    /**
     * Display the high scores page
     */
    public void viewScores(View view){
        int scoreEasy = fetchScore("scoresEasyFile");
        int scoreNormal = fetchScore("scoresFile");
        int scoreHard = fetchScore("scoresHardFile");
        setContentView(R.layout.offline_scores_page);
        String eHSText = ((TextView) findViewById(R.id.easyModeHS)).
                getText().toString() + " " + scoreEasy;
        String mHSText = ((TextView) findViewById(R.id.normalModeHS)).
                getText().toString() + " " + scoreNormal;
        String hHSText = ((TextView) findViewById(R.id.hardModeHS)).
                getText().toString() + " " + scoreHard;
        ((TextView) findViewById(R.id.easyModeHS)).setText(eHSText);
        ((TextView) findViewById(R.id.normalModeHS)).setText(mHSText);
        ((TextView) findViewById(R.id.hardModeHS)).setText(hHSText);
    }

    /**
     * Fetch the user high score in easy mode
     */
    private int fetchScore(String file){
        try{
            BufferedReader fr = new BufferedReader(new InputStreamReader
                    (openFileInput(file)));
            return Integer.parseInt(fr.readLine());
        } catch (Exception e){
            return 0;
        }
    }

    /**
     * Reset all high scores
     */
    public void highScoreReset(View view){
        try{
            FileOutputStream fos = openFileOutput("scoresFile", Context.MODE_PRIVATE);
            fos.write("0".getBytes());
            fos.close();
            fos = openFileOutput("scoresEasyFile", Context.MODE_PRIVATE);
            fos.write("0".getBytes());
            fos.close();
            fos = openFileOutput("scoresHardFile", Context.MODE_PRIVATE);
            fos.write("0".getBytes());
            fos.close();
            fos = openFileOutput("scoresRunFile", Context.MODE_PRIVATE);
            fos.write("0".getBytes());
            fos.close();
            viewScores(view);
        } catch (Exception e){}
    }

    //THIS SECTION PERTAINS TO THE GAME MODE SELECTION PAGE

    /**
     * Start easy mode
     */
    public void startGameEasy(View view){
        Intent intent = new Intent(this, EasyGamePage.class);
        startActivity(intent);
    }

    /**
     * Start normal mode game
     */
    public void startGameNormal(View view){
        Intent intent = new Intent(this, GamePage.class);
        startActivity(intent);
    }

    /**
     * Start hard mode game
     */
    public void startGameHard(View view){
        Intent intent = new Intent(this, HardGamePage.class);
        startActivity(intent);
    }

    //THIS SECTION PERTAINS TO MISCELLANEOUS METHODS

    /**
     * Play a sound
     */
    private void playSound(Uri uri) {
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(this, uri);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (Exception e) {
            // don't care
        }
    }

    /**
     * Method goHome
     *
     * Go back to the "Making Bank" application home page
     */
    public void goHome(View view){
        Intent intent = new Intent (this, BankHomePage.class);
        startActivity(intent);
    }
}
