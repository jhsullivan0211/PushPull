package com.example.pushpull.triggers;

import android.graphics.Canvas;
import android.graphics.Color;

import com.example.pushpull.game_logic.Actor;
import com.example.pushpull.game_logic.Level;
import com.example.pushpull.game_objects.GameObject;
import com.example.pushpull.myLibrary.Vector2D;
import com.example.pushpull.user_interface.LevelView;

public interface Trigger extends Actor {

    public boolean isFilled();
    public void act(GameObject filler);
    public void undo(GameObject leaver);


}