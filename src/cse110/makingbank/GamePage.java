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
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.*;

/**
 * Class: GamePage
 * This class defines the behavioral patterns for the game in Normal mode.
 * Originally the only available game mode in V1.0 of the app, hence the
 * class name GamePage rather than NormalGamePage.
 *
 * Author: Hoang Tran
 * DO NOT REPRODUCE WITHOUT PERMISSION!
 * This class is a slight modification to the original source code in
 * V1.0 of the app, released October 31, 2014.
 */
public class GamePage extends Activity{

    private int score, highestScore, bX, bY, minY;
    private TextView time, points, endScore, bestScore;
    private String timeStr, scoreStr, yourScore;
    private CountDownTimer timer;
    private ImageView button;
    private MediaPlayer mMediaPlayer;
    private Uri deduct, add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Sound files
        mMediaPlayer = new MediaPlayer();
        deduct = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.losepoint);
        add = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.winpoint);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.offline_game_screen_page);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final int w = metrics.widthPixels;
        final int h = metrics.heightPixels;
        time = (TextView) findViewById(R.id.timeLeft);
        ViewTreeObserver observer = time.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                minY = time.getHeight(); // Top scroll height
            }
        });
        button = (ImageView) findViewById(R.id.gameButton);
        ViewTreeObserver observer2 = button.getViewTreeObserver();
        observer2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                bX = w - button.getWidth(); // Button width
                bY = h - minY - button.getHeight(); // Button height
            }
        });

        // Find strings to change
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
     * Missed the button! Dock points.
     */
    public void minusPoints (View view){
        --score;
        if (score < 0)
            score = 0;
        points.setText(scoreStr + " " + score);
        playSound(deduct);
    }

    /**
     * Tapped the button! Add points!
     */
    public void addPoints (View view){
        ++score;
        points.setText(scoreStr + " " + score);
        playSound(add);
        moveButton();
    }

    /**
     * Move the button to a new location
     */
    private void moveButton(){
        button.setX((int) (Math.random() * bX));
        button.setY((int) (minY + Math.random() * bY));
    }

    /**
     * Play again button pressed
     */
    public void startAgain (View view){
        Intent intent = new Intent(this, GamePage.class);
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
                    (openFileInput("scoresFile")));
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
            FileOutputStream fos = openFileOutput("scoresFile", Context.MODE_PRIVATE);
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
