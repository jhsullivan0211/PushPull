package com.example.pushpull.user_interface;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


public class LevelSelect extends AppCompatActivity {

    private int levelCount;
    private Intent myIntent;
    public static final String chosenIndexID = "PUSHPULL.LEVEL_INDEX_CHOSEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);

       myIntent = getIntent();

        levelCount = myIntent.getIntExtra(MainActivity.levelCountID, 0);
        if (levelCount == 0) {
            //TODO: put a popup here
            return;
        }

        List<Integer> test = new ArrayList<>();
        for (int i = 0; i < levelCount; i += 1) {
            test.add(i);
        }


        GridView gridView = findViewById(R.id.gridView);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, 0, test) {

            @Override @NonNull
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                final int levelNumber = position;
                Button button = new Button(context);
                button.setText(String.valueOf(levelNumber + 1));
                button.setLayoutParams(new LinearLayout.LayoutParams(
                                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                                    LinearLayout.LayoutParams.MATCH_PARENT));

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setLevel(levelNumber);
                    }
                });
                return button;
            }
        };
        gridView.setAdapter(adapter);



    }

    private void setLevel(int levelNumber) {
        myIntent.putExtra(chosenIndexID, levelNumber);
        setResult(RESULT_OK, myIntent);
        finish();
    }




}
