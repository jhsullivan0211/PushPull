package com.example.pushpull.triggers;


import android.graphics.Color;

import com.example.pushpull.game_logic.Level;
import com.example.pushpull.game_objects.GameObject;
import com.example.pushpull.game_objects.Player;
import com.example.pushpull.myLibrary.Vector2D;

public class Transformer extends Trigger{

    private boolean filled;
    private Player.Type type;

    public Transformer(Player.Type type, Vector2D location, Level level) {
        super(location, level);
        filled = level.isPositionFilled(location);
        this.type = type;

        switch(type) {
            case PUSH:
                color = Color.RED;
                break;
            case PULL:
                color = Color.BLUE;
                break;
            case GRABALL:
                color = Color.YELLOW;

        }

    }

    public Player.Type getType() {
        return type;
    }

    @Override
    public void act(GameObject filler) {
        if (!filled && filler instanceof Player) {
            Player player = (Player) filler;
            player.changeType(type);
        }
    }

    @Override
    public void undo(GameObject leaver) {
        filled = false;
    }


}