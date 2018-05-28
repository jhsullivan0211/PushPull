package com.example.pushpull.user_interface;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pushpull.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class LevelSelectActivity extends AppCompatActivity {

    private int levelCount;
    private int currentLevelIndex;
    private Intent myIntent;
    public static final String chosenIndexID = "PUSHPULL.LEVEL_INDEX_CHOSEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);

       myIntent = getIntent();

        levelCount = myIntent.getIntExtra(MainActivity.levelCountID, 0);
        currentLevelIndex = myIntent.getIntExtra(MainActivity.currentLevelID, -1);
        if (levelCount == 0) {
            //TODO: put a popup here
            return;
        }

        List<Integer> levels = new ArrayList<>();
        for (int i = 0; i < levelCount; i += 1) {
            levels.add(i);
        }

        GridView gridView = findViewById(R.id.gridView);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, 0, levels) {

            @Override @NonNull
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                final int levelIndex = position;
                Button button = new Button(context);
                button.setText(String.valueOf(levelIndex + 1));
                button.setLayoutParams(new LinearLayout.LayoutParams(
                                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                                    LinearLayout.LayoutParams.MATCH_PARENT));


                if (levelIndex == currentLevelIndex) {
                    button.getBackground().setColorFilter(Color.rgb(50, 205, 50),
                            PorterDuff.Mode.MULTIPLY);
                }


                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setLevel(levelIndex);
                    }
                });
                return button;
            }
        };
        gridView.setAdapter(adapter);



    }

    private void setLevel(int levelIndex) {
        myIntent.putExtra(chosenIndexID, levelIndex);
        setResult(RESULT_OK, myIntent);
        if (myIntent.getBooleanExtra(StartupActivity.startupID, true)) {
            Intent starterIntent = new Intent(this, MainActivity.class);
            this.startActivity(starterIntent);
        }
        else {
            finish();
        }

    }




}
