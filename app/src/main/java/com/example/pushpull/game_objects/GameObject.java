package com.example.pushpull.game_objects;




import com.example.pushpull.myLibrary.Vector2D;

public interface GameObject {

    public Vector2D getLocation();
    public void setLocation(Vector2D location);
    public void setMove(boolean move);
    public boolean canMove();
    public int getColor();


}
