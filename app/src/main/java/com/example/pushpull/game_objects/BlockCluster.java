package com.example.pushpull.game_objects;




    import android.graphics.Color;


    import com.example.pushpull.game_logic.Level;
    import com.example.pushpull.myLibrary.Vector2D;

    import java.util.ArrayList;
    import java.util.Collection;
    import java.util.Collections;
    import java.util.HashSet;
    import java.util.List;
    import java.util.Set;

public class BlockCluster implements GameObject{


    private int color = Color.rgb(50,205,50);
    private Vector2D location;
    private List<BlockCluster> clusters = new ArrayList<>();
    private boolean move = true;
    private Character clusterID;

    public BlockCluster(Character clusterID) {
        this.clusterID = clusterID;
    }

    public void makeCluster(List<BlockCluster> template) {
        clusters = new ArrayList<>(template);
    }


    @Override
    public Vector2D getLocation() {
        return this.location;
    }



    @Override
    public int getColor() {
        return this.color;
    }


    @Override
    public void setLocation(Vector2D location) {
        this.location = location;
    }

    public List<BlockCluster> getCluster() {
        return Collections.unmodifiableList(clusters);
    }

    @Override
    public void setMove(boolean move) {
        this.move = move;
    }

    @Override
    public boolean canMove() {
        return move;
    }

    public Character getClusterID() {
        return clusterID;
    }





}
