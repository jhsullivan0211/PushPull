package com.example.pushpull.user_interface;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.example.pushpull.R;

import java.util.ArrayList;
import java.util.List;


public class LevelSelectActivity extends AppCompatActivity {

    private int levelCount;
    private int currentLevelIndex;
    private Intent myIntent;
    private int maxLevel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);

        myIntent = getIntent();
        levelCount = myIntent.getIntExtra(ActivityUtility.levelCountID, 0);
        currentLevelIndex = myIntent.getIntExtra(ActivityUtility.currentLevelID, -1);
        SharedPreferences preferences = getSharedPreferences(ActivityUtility.dataFileName, 0);
        maxLevel = preferences.getInt(ActivityUtility.maxLevelID, -1);


        if (levelCount == 0 || maxLevel == -1) {
            ActivityUtility.showAlert("Level Error", "An error occurred while " +
                    "accessing app files.  The application will close.", this);
        }

        List<Integer> levels = new ArrayList<>();
        for (int i = 0; i < levelCount; i += 1) {
            levels.add(i);
        }

        GridView gridView = findViewById(R.id.gridView);
        ArrayAdapter<Integer> adapter = getLevelSelectAdapter(context, levels);
        gridView.setAdapter(adapter);
    }

    private ArrayAdapter<Integer> getLevelSelectAdapter(final Context context, List<Integer> levels) {
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, 0, levels) {

            @Override @NonNull
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {

                final int levelIndex = position;

                if (levelIndex > maxLevel) {
                    View buttonLayout = LayoutInflater.from(context).inflate(R.layout.lock_button, null);
                    return buttonLayout.findViewById(R.id.lockButton);
                }
                else {
                    View buttonLayout = LayoutInflater.from(context).inflate(R.layout.unlock_button, null);
                    Button button = buttonLayout.findViewById(R.id.unlockButton);
                    button.setText(String.valueOf(levelIndex + 1));

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
            }
        };
        return adapter;
    }
    private void setLevel(int levelIndex) {

        if (levelIndex == currentLevelIndex) {
            setResult(RESULT_CANCELED, myIntent);
        }
        else {
            myIntent.putExtra(ActivityUtility.chosenIndexID, levelIndex);
            setResult(RESULT_OK, myIntent);
        }

        finish();

    }




}
