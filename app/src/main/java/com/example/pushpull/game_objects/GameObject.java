package com.example.pushpull.game_objects;




import com.example.pushpull.game_logic.Actor;
import com.example.pushpull.myLibrary.Vector2D;

import java.io.Serializable;

public interface GameObject extends Actor {

    public void setLocation(Vector2D location);
    public void setMove(boolean move);
    public boolean canMove();




}
