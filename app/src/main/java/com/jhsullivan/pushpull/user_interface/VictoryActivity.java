package com.example.pushpull.user_interface;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.example.pushpull.R;

public class VictoryActivity extends AppCompatActivity {



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


    private void closeGame() {
        Intent closingIntent = new Intent(this, StartupActivity.class);
        closingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        closingIntent.putExtra(ActivityUtility.exitID, true);
        startActivity(closingIntent);
    }

}
