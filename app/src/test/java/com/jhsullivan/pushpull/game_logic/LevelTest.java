package com.jhsullivan.pushpull.game_logic;

import com.jhsullivan.pushpull.game_objects.GameObject;
import com.jhsullivan.pushpull.game_objects.Wall;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class LevelTest {

    public static String emptyLevel =   "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //0
                                        "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //1
                                        "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //2
                                        "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //3
                                        "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //4
                                        "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //5
                                        "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //6
                                        "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //7
                                        "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //8
                                        "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx";    //9


    /**
     * Tests whether a player can push a block.  Expected result:  both player and block move to
     * the right.
     *
     * @throws LevelLoadException  Throws this exception if the Level fails to load due to String
     *                             not being properly formatted.
     */
    @Test
    public void testPushBlockBasic() throws LevelLoadException {
        //                   0  1  2  3  4  5  6  7  8  9
        String testLayout = "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //0
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //1
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //2
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //3
                            "xx,xx,xx,xx,px,bx,xx,xx,xx,xx\n" + //4
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //5
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //6
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //7
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //8
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx";    //9

        Level testLevel = new Level(testLayout);
        List<GameObject> movers = getMovers(testLevel, new HashSet<>());
        Map<GameObject, Vector2D> resultMap = getResultPointMap(testLevel, Vector2D.Direction.RIGHT, movers);
        testLevel.processInput(Vector2D.Direction.RIGHT);
        testGameObjectEndPoints(testLevel, resultMap);

    }

    /**
     * Tests whether a player can push a block cluster.  Expected result: Player and cluster both
     * move to the right.
     *
     * @throws LevelLoadException Thrown on failure to load a level.
     */
    @Test
    public void testPushClusterBasic() throws LevelLoadException {
        //                   0  1  2  3  4  5  6  7  8  9
        String testLayout = "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //0
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //1
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //2
                            "xx,xx,xx,xx,xx,1x,xx,xx,xx,xx\n" + //3
                            "xx,xx,xx,xx,px,1x,xx,xx,xx,xx\n" + //4
                            "xx,xx,xx,xx,xx,1x,xx,xx,xx,xx\n" + //5
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //6
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //7
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //8
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx";    //9

        Level testLevel = new Level(testLayout);
        List<GameObject> movers = getMovers(testLevel);
        Map<GameObject, Vector2D> resultMap = getResultPointMap(testLevel, Vector2D.Direction.RIGHT, movers);
        testLevel.processInput(Vector2D.Direction.RIGHT);
        testGameObjectEndPoints(testLevel, resultMap);
    }


    /**
     * Tests whether a player can push a wall.  Expected result: nothing moves.
     *
     * @throws LevelLoadException  Throws this exception if the Level fails to load due to String
     *                             not being properly formatted.
     */
    @Test
    public void testPushWall() throws LevelLoadException {
        //                   0  1  2  3  4  5  6  7  8  9
        String testLayout = "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //0
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //1
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //2
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //3
                            "xx,xx,xx,xx,px,wx,xx,xx,xx,xx\n" + //4
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //5
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //6
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //7
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //8
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx";    //9

        Level testLevel = new Level(testLayout);
        Map<GameObject, Vector2D> resultMap = getResultPointMap(testLevel);
        testLevel.processInput(Vector2D.Direction.RIGHT);
        testGameObjectEndPoints(testLevel, resultMap);
    }


    /**
     * Tests whether a player can push a block through a wall.  Expected result: nothing moves.
     *
     * @throws LevelLoadException  Throws this exception if the Level fails to load due to String
     *                             not being properly formatted.
     */
    @Test
    public void testPushBlockThroughWall() throws LevelLoadException {
        //                   0  1  2  3  4  5  6  7  8  9
        String testLayout = "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //0
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //1
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //2
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //3
                            "xx,xx,xx,xx,px,bx,wx,xx,xx,xx\n" + //4
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //5
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //6
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //7
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //8
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx";    //9

        Level testLevel = new Level(testLayout);
        Map<GameObject, Vector2D> resultMap = getResultPointMap(testLevel);
        testLevel.processInput(Vector2D.Direction.RIGHT);
        testGameObjectEndPoints(testLevel, resultMap);
    }


    /**
     * Tests whether a player can push a cluster through a wall.  Expected result: nothing moves.
     *
     * @throws LevelLoadException  Throws this exception if the Level fails to load due to String
     *                             not being properly formatted.
     */
    @Test
    public void testPushClusterThroughWall() throws LevelLoadException {
        //                   0  1  2  3  4  5  6  7  8  9
        String testLayout = "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //0
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //1
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //2
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //3
                            "xx,xx,xx,xx,xx,1x,wx,xx,xx,xx\n" + //4
                            "xx,xx,xx,xx,px,1x,xx,xx,xx,xx\n" + //5
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //6
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //7
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //8
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx";    //9

        Level testLevel = new Level(testLayout);
        Map<GameObject, Vector2D> resultMap = getResultPointMap(testLevel);
        testLevel.processInput(Vector2D.Direction.RIGHT);
        testGameObjectEndPoints(testLevel, resultMap);
    }


    /**
     * Tests whether a player can push more than one block.  Expected result: nothing moves.
     *
     * @throws LevelLoadException  Throws this exception if the Level fails to load due to String
     *                             not being properly formatted.
     */
    @Test
    public void testPushTwoBlocks() throws LevelLoadException {
        //                   0  1  2  3  4  5  6  7  8  9
        String testLayout = "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //0
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //1
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //2
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //3
                            "xx,xx,xx,xx,px,bx,bx,xx,xx,xx\n" + //4
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //5
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //6
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //7
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //8
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx";    //9

        Level testLevel = new Level(testLayout);
        Map<GameObject, Vector2D> resultMap = getResultPointMap(testLevel);
        testLevel.processInput(Vector2D.Direction.RIGHT);
        testGameObjectEndPoints(testLevel, resultMap);
    }


    /**
     * Tests whether a player can push two clusters, where the second is not directly in front of
     * the  player. Expected result: nothing moves.
     *
     * @throws LevelLoadException  Throws this exception if the Level fails to load due to String
     *                             not being properly formatted.
     */
    @Test
    public void testPushTwoClusters() throws LevelLoadException {
        //                   0  1  2  3  4  5  6  7  8  9
        String testLayout = "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //0
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //1
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //2
                            "xx,xx,xx,xx,xx,xx,2x,xx,xx,xx\n" + //3
                            "xx,xx,xx,xx,xx,1x,2x,xx,xx,xx\n" + //4
                            "xx,xx,xx,xx,px,1x,xx,xx,xx,xx\n" + //5
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //6
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //7
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //8
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx";    //9

        Level testLevel = new Level(testLayout);
        Map<GameObject, Vector2D> resultMap = getResultPointMap(testLevel);
        testLevel.processInput(Vector2D.Direction.RIGHT);
        testGameObjectEndPoints(testLevel, resultMap);
    }


    /**
     * Tests basic functionality of the GRABALL Player type, which should move with all contiguous
     * blocks.  Expected result: player and all blocks move to the right.
     *
     * @throws LevelLoadException  Throws this exception if the Level fails to load due to String
     *                             not being properly formatted.
     */

    @Test
    public void testGrabAllBasic() throws LevelLoadException {
        //                   0  1  2  3  4  5  6  7  8  9
        String testLayout = "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //0
                            "xx,xx,xx,bx,bx,bx,xx,xx,xx,xx\n" + //1
                            "xx,xx,xx,xx,bx,xx,xx,xx,xx,xx\n" + //2
                            "xx,bx,xx,xx,bx,xx,xx,xx,xx,xx\n" + //3
                            "xx,bx,bx,bx,rx,bx,xx,xx,xx,xx\n" + //4
                            "xx,bx,xx,xx,bx,xx,xx,xx,xx,xx\n" + //5
                            "xx,xx,xx,xx,bx,xx,xx,xx,xx,xx\n" + //6
                            "xx,xx,xx,xx,bx,xx,xx,xx,xx,xx\n" + //7
                            "xx,xx,xx,bx,bx,bx,xx,xx,xx,xx\n" + //8
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx";    //9

        Level testLevel = new Level(testLayout);
        List<GameObject> movers = getMovers(testLevel, new HashSet<>());
        Map<GameObject, Vector2D> resultMap = getResultPointMap(testLevel, Vector2D.Direction.RIGHT, movers);
        testLevel.processInput(Vector2D.Direction.RIGHT);
        testGameObjectEndPoints(testLevel, resultMap);
    }

    /**
     * Tests whether the Graball Player can be indirectly blocked by a wall in the way of one of the
     * blocks connected to it.  Expected result: no movement of anything.
     *
     * @throws LevelLoadException  Throws this exception if the Level fails to load due to String
     *                             not being properly formatted.
     */

    @Test
    public void testGrabAllBlocked() throws LevelLoadException {
        //                   0  1  2  3  4  5  6  7  8  9
        String testLayout = "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //0
                            "xx,xx,xx,bx,bx,bx,wx,xx,xx,xx\n" + //1
                            "xx,xx,xx,xx,bx,xx,xx,xx,xx,xx\n" + //2
                            "xx,bx,xx,xx,bx,xx,xx,xx,xx,xx\n" + //3
                            "xx,bx,bx,bx,rx,bx,xx,xx,xx,xx\n" + //4
                            "xx,bx,xx,xx,bx,xx,xx,xx,xx,xx\n" + //5
                            "xx,xx,xx,xx,bx,xx,xx,xx,xx,xx\n" + //6
                            "xx,xx,xx,xx,bx,xx,xx,xx,xx,xx\n" + //7
                            "xx,xx,xx,bx,bx,bx,xx,xx,xx,xx\n" + //8
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx";    //9

        Level testLevel = new Level(testLayout);
        Map<GameObject, Vector2D> resultMap = getResultPointMap(testLevel);
        testLevel.processInput(Vector2D.Direction.RIGHT);
        testGameObjectEndPoints(testLevel, resultMap);
    }


    /**
     * Tests whether the "splitting" condition of the GrabAll holds.  That is, when a chain of
     * pieces moved by the GrabAll player is blocked but one of the intermediate pieces is a
     * different kind of player, then the player can move on its own, freeing up the rest
     * of the pieces to move with the GrabAll player, assuming the rest of the pieces are able
     * to move.  Expected result: the left part (everything to the left of the push player, and
     * the push player) should move down, the right part (everything else) should not move.
     *
     * @throws LevelLoadException  Throws this exception if the Level fails to load due to String
     *                             not being properly formatted.
     */
    @Test
    public void testGrabAllSplit() throws LevelLoadException {
        //                   0  1  2  3  4  5  6  7  8  9
        String testLayout = "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //0
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //1
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //2
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //3
                            "xx,xx,xx,rx,bx,bx,px,1x,1x,xx\n" + //4
                            "xx,xx,xx,xx,xx,xx,xx,xx,wx,xx\n" + //5
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //6
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //7
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //8
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx";    //9

        Level testLevel = new Level(testLayout);
        Set<GameObject> nonMovers = new HashSet<>();
        nonMovers.add(testLevel.getObjectAt(7,4));
        nonMovers.add(testLevel.getObjectAt(8, 4));
        List<GameObject> movers = getMovers(testLevel, nonMovers);
        Map<GameObject, Vector2D> resultMap = getResultPointMap(testLevel, Vector2D.Direction.DOWN, movers);
        testLevel.processInput(Vector2D.Direction.DOWN);
        testGameObjectEndPoints(testLevel, resultMap);
    }


    /**
     * Tests whether the PULL type player can pull a single block.  Expected result:  both player
     * and block move to the right.
     *
     * @throws LevelLoadException  Throws this exception if the Level fails to load due to String
     *                             not being properly formatted.
     */
    @Test
    public void testPullBlockBasic() throws LevelLoadException {
        //                   0  1  2  3  4  5  6  7  8  9
        String testLayout = "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //0
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //1
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //2
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //3
                            "xx,xx,xx,xx,bx,qx,xx,xx,xx,xx\n" + //4
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //5
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //6
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //7
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //8
                            "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx";    //9

        Level testLevel = new Level(testLayout);
        List<GameObject> movers = getMovers(testLevel, new HashSet<>());
        Map<GameObject, Vector2D> resultMap = getResultPointMap(testLevel, Vector2D.Direction.RIGHT, movers);
        testLevel.processInput(Vector2D.Direction.RIGHT);
        testGameObjectEndPoints(testLevel, resultMap);

    }


    /**
     * Tests whether the PULL type player can pull a block cluster.  Expected result:  both player
     * and block move to the right.
     *
     * @throws LevelLoadException  Throws this exception if the Level fails to load due to String
     *                             not being properly formatted.
     */
    @Test
    public void testPullClusterBasic() throws LevelLoadException {
        //                   0  1  2  3  4  5  6  7  8  9
        String testLayout = "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //0
                "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //1
                "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //2
                "xx,xx,xx,xx,1x,xx,xx,xx,xx,xx\n" + //3
                "xx,xx,xx,xx,1x,qx,xx,xx,xx,xx\n" + //4
                "xx,xx,xx,xx,1x,xx,xx,xx,xx,xx\n" + //5
                "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //6
                "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //7
                "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx\n" + //8
                "xx,xx,xx,xx,xx,xx,xx,xx,xx,xx";    //9

        Level testLevel = new Level(testLayout);
        List<GameObject> movers = getMovers(testLevel, new HashSet<>());
        Map<GameObject, Vector2D> resultMap = getResultPointMap(testLevel, Vector2D.Direction.RIGHT, movers);
        testLevel.processInput(Vector2D.Direction.RIGHT);
        testGameObjectEndPoints(testLevel, resultMap);

    }



    //--------------------------Helper methods---------------------------------------------------




    /**
     * Checks that each GameObject in the specified map is at the location defined by the value of
     * the GameObject key in the map.
     *
     * @param testLevel         The Level to act upon.
     * @param targetPositions   A map of game objects to positions, which indicates where each
     *                          game object should end up.
     * @param message           The message to display on assertion errors.
     */
     private  void testGameObjectEndPoints(Level testLevel,
                                Map<GameObject, Vector2D> targetPositions) {

        for (Map.Entry<GameObject, Vector2D> entry : targetPositions.entrySet()) {
            GameObject obj = entry.getKey();
            Vector2D loc = entry.getValue();


            assertNotNull(testLevel.getObjectAt(loc));
            assertEquals(testLevel.getObjectAt(loc), obj);
            assertEquals(obj.getLocation(), loc);

        }
    }


    /**
     * Given a list of directions and a list of game objects, returns a mapping of each
     * game object to a new point relative to that object's location, after moving in all of the
     * directions in the specified list.
     *
     * @param level             The Level that the game objects reside in.
     * @param directions        List of directions to move in.
     * @param movers            List of of game objects expected to move.
     * @return                  Returns a map of each specified game object to a new location after
     *                          moving.
     */
    private Map<GameObject, Vector2D> getResultPointMap(Level level,
                                                        List<Vector2D.Direction> directions,
                                                        List<GameObject> movers) {

        Map<GameObject, Vector2D> result = new HashMap<>();

        for (GameObject obj : level.getGameObjects()) {
            if (movers.contains(obj)) {
                Vector2D resultingLocation = obj.getLocation().getPointFromDirections(directions);
                result.put(obj, resultingLocation);
            }
            else {
                result.put(obj, obj.getLocation());
            }
        }

        return result;
    }

    /**
     *  Given a direction and a list number of game objects, returns a mapping of each
     * game object to a new point relative to that object's location, after moving in all of the
     * directions in the specified list.
     *
     * @param level             The Level in which the game objects reside.
     * @param direction         Directions to move in.
     * @param movers            List of game objects expected to move.
     * @return                  Returns a map of each specified game object to a new location after
     *                          moving.
     */

    private Map<GameObject, Vector2D> getResultPointMap(Level level,
                                                        Vector2D.Direction direction,
                                                        List<GameObject> movers) {

        List<Vector2D.Direction> dummyList = new ArrayList<>();
        dummyList.add(direction);
        return getResultPointMap(level, dummyList, movers);

    }

    /**
     *  Given a level, returns the map of game objects to their locations.
     *
     * @return                  Returns a map of each specified game object to a new location after
     *                          moving.
     */

    private Map<GameObject, Vector2D> getResultPointMap(Level level) {

        List<Vector2D.Direction> dummyList = new ArrayList<>();
        List<GameObject> dummyObjects = new ArrayList<>();
        return getResultPointMap(level, dummyList, dummyObjects);

    }



    /**
     * Given a level, gets all game objects that are not walls and that are not in the var args
     * following the level argument.
     *
     * @param level         The level to search.
     * @param nonMovers     A variable number of game objects to exclude.
     * @return              An array of game objects expected to move.
     */
    private List<GameObject> getMovers(Level level, Set<GameObject> nonMovers) {

        List<GameObject> moverList = level.getGameObjects();

        moverList = moverList.stream()
                             .filter(item -> (!(item instanceof Wall || nonMovers.contains(item))))
                             .collect(Collectors.toList());

        return moverList;

    }

    /**
     * Given a level, gets all game objects that are not walls.  Overloaded version of the above
     * without the nonmover set.
     *
     * @param level         The level to search.
     * @return              An array of game objects expected to move.
     */
    private List<GameObject> getMovers(Level level) {
        return getMovers(level, new HashSet<>());
    }


}