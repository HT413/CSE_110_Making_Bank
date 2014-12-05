package cse110.makingbank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Class HumanityCheck
 * This class helps check if a user is human or not
 */
public class HumanityCheck extends Activity{
    private String solution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_human);
        // Hide action bar
        try {
            getActionBar().hide();
        }catch (Exception e){}
        determineAnimal();
    }

    /**
     * Method determineAnimal
     * Determine the animal to question the user about
     */
    private void determineAnimal(){
        switch((int) (Math.random() * 3)){
            case 0:
                solution = "duck";
                findViewById(R.id.animalImage).setBackgroundResource(R.drawable.duck);
                break;
            case 1:
                solution = "cat";
                findViewById(R.id.animalImage).setBackgroundResource(R.drawable.cat);
                break;
            default:
                solution = "dog";
                findViewById(R.id.animalImage).setBackgroundResource(R.drawable.dog);
                break;
        }
    }

    /**
     * Method checkAnswer
     * Determine if the user inputted correctly the answer
     */
    public void checkAnswer(View view){
        String answer = ((EditText) findViewById(R.id.myAnswer)).getText().toString();
        if (answer.toLowerCase().equals(solution)){
            Intent intent = new Intent(this, LoginPage.class);
            startActivity(intent);
        }
        else{
            ((TextView) findViewById(R.id.animalPageTitle)).setText("WRONG ANSWER!");
        }
    }
}
