package com.example.pushpull.user_interface;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    private Drawable lockIcon;
    private int maxLevel;

    public static final String chosenIndexID = "PUSHPULL.LEVEL_INDEX_CHOSEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);

        myIntent = getIntent();
        levelCount = myIntent.getIntExtra(MainActivity.levelCountID, 0);
        currentLevelIndex = myIntent.getIntExtra(MainActivity.currentLevelID, -1);

        SharedPreferences preferences = getSharedPreferences(MainActivity.dataFileName, 0);

        //TODO: swap the following
        //maxLevel = preferences.getInt(MainActivity.maxLevelID, -1);
        maxLevel = 50;
        //TODO: end here


        if (maxLevel == -1) {
            //TODO: put a popup here
            return;
        }

        if (levelCount == 0) {
            //TODO: put a popup here
            return;
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
        myIntent.putExtra(chosenIndexID, levelIndex);
        setResult(RESULT_OK, myIntent);
        finish();

    }




}
