/** IMPORTANT NOTICE
 *  This file is a direct copy + paste with minor modifications tailored
 *  to integrate into team Making Bank's application, from the source code of the
 *  "Teleporting Button" app, designed by Hoang Tran in November, 2014.
 *  DO NOT REPRODUCE THIS FILE WITHOUT PERMISSION
 *
 *  Author: Hoang Tran
 *  Original Source: "Teleporting Button" app, by Hoang Tran (architect,
 *  developer, and business analyst of team Making Bank), Google Play Store */

package cse110.makingbank;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.*;

/**
 * Class: EasyGamePage
 * This class contains the behavioral definitions for the game in
 * easy mode.
 *
 * Author: Hoang Tran
 * DO NOT REPRODUCE WITHOUT PERMISSION!
 * This class is a slight modification to the original source code in
 * V1.2 of app, released November 17, 2014.
 */
public class EasyGamePage extends Activity{

    private int score, highestScore;
    private TextView time, points, endScore, bestScore;
    private String timeStr, scoreStr, yourScore;
    private CountDownTimer timer;
    private MediaPlayer mMediaPlayer;
    private Uri add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Sound files
        mMediaPlayer = new MediaPlayer();
        add = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.winpoint);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.offline_game_screen_easy_page);

        // Find strings to change
        time = (TextView) findViewById(R.id.timeLeft);
        points = (TextView) findViewById(R.id.scorePoints);
        bestScore = (TextView) findViewById(R.id.highScore);

        timeStr = time.getText().toString();
        scoreStr = points.getText().toString();

        // Start game
        score = 0;
        points.setText(scoreStr + " " + score);

        timer = new CountDownTimer(30000, 100) {
            // Update timer with each tick
            @Override
            public void onTick(long msLeft) {
                time.setText(timeStr + " " + (msLeft)/1000 + "." + ((msLeft)/100) % 10);
            }

            @Override
            public void onFinish() {
                highestScore = fetchScore();
                setContentView(R.layout.offline_game_over_page);
                endScore = (TextView) findViewById(R.id.finalScore);
                yourScore = endScore.getText().toString();
                endScore.setText(yourScore + " " + score);
                bestScore = (TextView) findViewById(R.id.highScore);
                if (score > highestScore){
                    updateHighScore ("" + score);
                    bestScore.setText(R.string.new_high_score);
                }
                else{
                    bestScore.setText(getString(R.string.high_score) + " " + highestScore);
                }
            }
        };
        timer.start();
    }

    /**
     * Tapped the background! No penalty, luckily.
     */
    public void minusPoints (View view){}

    /**
     * Tapped the button! Add points!
     */
    public void addPoints (View view){
        ++score;
        points.setText(scoreStr + " " + score);
        playSound(add);
    }

    /**
     * Play again button pressed
     */
    public void startAgain (View view){
        Intent intent = new Intent(this, EasyGamePage.class);
        startActivity(intent);
    }

    /**
     * Exit to main menu
     */
    public void menuExit (View view){
        Intent intent = new Intent(this, TeleportingButton.class);
        startActivity(intent);
    }

    /**
     * Fetch the highest score
     */
    private int fetchScore(){
        try{
            BufferedReader fr = new BufferedReader(new InputStreamReader
                    (openFileInput("scoresEasyFile")));
            return Integer.parseInt(fr.readLine());
        } catch (Exception e){
            return 0;
        }
    }

    /**
     * New high score!
     */
    private void updateHighScore(String score){
        try{
            FileOutputStream fos = openFileOutput("scoresEasyFile", Context.MODE_PRIVATE);
            fos.write(score.getBytes());
            fos.close();
        } catch (Exception e){}
    }

    /**
     * Play sound
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
}
