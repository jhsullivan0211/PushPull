package com.jhsullivan.pushpull.user_interface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import com.jhsullivan.pushpull.R;

/**
 * The Activity displayed after the user beats the final level.  Displays a message, and allows
 * the user to close the program.
 */
public class VictoryActivity extends AppCompatActivity {

    /**
     * The method called when this Activity is created.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victory);

        ImageButton levelSelectButton = findViewById(R.id.levelSelectButton);
        levelSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeGame();
            }
        });

    }

    /**
     * Closes the app.
     */
    private void closeGame() {
        Intent closingIntent = new Intent(this, StartupActivity.class);
        closingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        closingIntent.putExtra(ActivityUtility.exitID, true);
        startActivity(closingIntent);
    }

}
