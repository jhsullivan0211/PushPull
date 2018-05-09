package com.example.pushpull.game_logic;




    import com.example.pushpull.game_objects.Block;
    import com.example.pushpull.game_objects.BlockCluster;
    import com.example.pushpull.game_objects.GameObject;
    import com.example.pushpull.game_objects.Player;
    import com.example.pushpull.game_objects.Wall;
    import com.example.pushpull.myLibrary.Vector2D;
    import com.example.pushpull.triggers.Target;
    import com.example.pushpull.triggers.Transformer;
    import com.example.pushpull.triggers.Trigger;
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.Collection;
    import java.util.Collections;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;


/**
 * This class handles the game logic behind the application.  It handles object location and
 * movement, checks win conditions, processes all game events, and allows for specific levels
 * to be loaded using special string sequences.
 * <p>
 * The level is structured as a grid of squares (currently 10x10, but may change in the future),
 * with each square potentially holding a trigger, which cannot be moved but influences other
 * pieces, and a game object which can be moved around.  Game objects "take up space", i.e.
 * they cannot be moved over or overlap in any way, while triggers cannot move and do not
 * take up space (they can have a game object occupying the same space).
 *<p>
 * The level is manipulated using directional movement (using the Direction enum from
 * @link myLibrary.Vector2D) processed from inputs.  Each movement causes all of the players
 * in the level to attempt to move in the specified direction, pushing/pulling blocks in the
 * way if possible.
 *<p>
 * The winning condition occurs when all of the targets (a type of trigger) are covered
 * with blocks.
 *
 *
 *
 */

public class Level {


    private static final int columnNumber = 10;
    private static final int rowNumber = 10;

    private Vector2D levelBounds = new Vector2D(columnNumber - 1, rowNumber - 1);
    private String layout;
    private List<GameObject> gameObjects = new ArrayList<>();
    private Map<Vector2D, GameObject> filledPositions = new HashMap<>();
    private Map<Vector2D, GameObject> gameState = new HashMap<>();
    private List<Player> players = new ArrayList<>();
    private List<Trigger> triggers = new ArrayList<>();
    private List<Target> targets = new ArrayList<>();
    private Map<String, List<BlockCluster>> clusterGroups = new HashMap<>();
    private Map<GameObject, List<Vector2D.Direction>> borderMap = new HashMap<>();
    private boolean isComplete = false;


    /**
     * Constructor which builds a level given a specifying layout String.  Fills
     * the level with triggers and game objects according to the characters in
     * the layout string.
     *
     * @param   layout    The String defining the layout for this level.
     */
    Level(String layout) {
        this.layout = layout;
        this.load(layout);
        update();
    }

    /**
     * Reads the specified String and adds game objects and triggers to the level.
     * Reads pairs of characters at a time and fills in the level in normal English
     * reading order (left to right, top to bottom), where each pair can be a game
     * object and/or a trigger, or empty space.  Throws an error if a pair contains
     * illegally overlappign entities (i.e. two game objects in the same position).
     *
     * @param layout    The String defining the layout of the level.
     */
    private void load(String layout) {
        this.gameObjects.clear();
        int position = 0;
        boolean doShift = false;
        int currentCode = -2;

       for (char id : layout.toCharArray()) {

           if (id == ','|| id == '\n' || id == ' ') {
               continue;
           }

           int y = position / rowNumber;
           int x = position % columnNumber;

           int previousCode = currentCode;
           currentCode = processID(id, new Vector2D(x, y));

           if (doShift) {

               if (currentCode != 0 && currentCode == previousCode) {
                   //TODO: throw error here
                   return;
               }

               position += 1;
           }
           else {

           }
           doShift = !doShift;
        }

        for (List<BlockCluster> clusterList : clusterGroups.values()) {
            for (BlockCluster cluster : clusterList) {
                cluster.makeCluster(clusterList);
            }
        }

        gameState = new HashMap<>(filledPositions);
    }


    /**
     * Reads in a character and spawns game objects, triggers, or nothing at the
     * specified location.
     *
     * @param idChar        The character id which determines what, if any, thing spawns.
     * @param location      The location of the spawn.
     * @return              Returns 1 for game objects, -1 for triggers, and 0 for empty space.
     */
    private int processID(Character idChar, Vector2D location) {
        //TODO: find a better way for this

        String id = Character.toString(idChar);

        if (id.equals("b")) {
            spawnGameObject(new Block(), location);
            return 1;
        }

        else if (id.equals("w")) {
            spawnGameObject(new Wall(), location);
            return 1;
        }

        else if (id.equals("o")) {
            Target target = new Target(location, this);
            triggers.add(target);
            targets.add(target);
            return -1;
        }

        else if (id.matches("[PQR]")) {
            triggers.add(new Transformer(Player.idToType(idChar), location, this));
            return -1;
        }

        else if (id.matches("[\\d!@#$%^&*]")) {

            BlockCluster spawned = new BlockCluster(idChar);
            spawnGameObject(spawned, location);

            if (!clusterGroups.containsKey(id)) {
                clusterGroups.put(id, new ArrayList<BlockCluster>());
            }
            clusterGroups.get(id).add(spawned);
            return 1;
        }

        else if (id.matches("[pqr]")) {
            Player.Type spawnType = Player.idToType(idChar);
            Player spawnPlayer = new Player(this, spawnType);
            spawnGameObject(spawnPlayer, location);
            players.add(spawnPlayer);
            return 1;


        }
        else if (id.equals("x")) {
            return 0;
        }
        else {
            throw new RuntimeException("Object ID '" +  ""
                    + id +  "' does not correspond to a known object.");
        }
    }


    /**
     * Reverts to the previously saved state defined by game object positions.
     */
    void revertState() {
        for (Vector2D position : gameState.keySet()) {
            gameState.get(position).setLocation(position);
        }
        this.filledPositions = gameState;
        update();
    }

    /**
     * Clears all game objects from the level.
     */
    private void clearGameObjects() {
        gameObjects.clear();
        filledPositions.clear();
        players.clear();
    }

    /**
     * Spawns a game object in the level at a specified position.
     * //TODO: throw an error if overlapping spawns?
     *
     * @param gameObject    The game object to spawn.
     * @param spawnPoint    The location of the object in the game.
     */
    private void spawnGameObject(GameObject gameObject, Vector2D spawnPoint) {
        gameObject.setLocation(spawnPoint);
        filledPositions.put(spawnPoint, gameObject);
        gameObjects.add(gameObject);
    }

    /**
     * Gets all of the game objects that are edge adjacent to the specified game object.
     *
     * @param gameObject    The game object that has its adjacents returned.
     * @return              The collection of adjacents.
     */

    public Collection<GameObject> getAdjacentObjects(GameObject gameObject) {
        Collection<Vector2D> adjacentPoints = gameObject.getLocation().getAdjacentPoints();
        List<GameObject> adjacents = new ArrayList<>();
        for (Vector2D point : adjacentPoints) {
            GameObject adj = filledPositions.get(point);
            if (adj != null) {
                adjacents.add(adj);
            }
        }
        return adjacents;
    }


    /**
     * Converts the game objects in the level into character codes and returns a map
     * of positions to game object codes.  This is used to serialize the game state for
     * saving in Activities.
     *
     * //TODO: externalize this method and return the state only?
     *
     * @return  The current state, with game objects converted to character codes.
     */
    HashMap<Vector2D, Character> getSerialState() {
        HashMap<Vector2D, Character> serialState = new HashMap<>();
        for (Vector2D point : filledPositions.keySet()) {
            GameObject obj = filledPositions.get(point);

            if (obj instanceof Player) {
                Player objPlayer = (Player) obj;
                switch (objPlayer.getType()) {
                    case PUSH:
                        serialState.put(point, 'p');
                        break;
                    case PULL:
                        serialState.put(point, 'q');
                        break;
                    case GRABALL:
                        serialState.put(point, 'r');

                }
            }

            if (obj instanceof Block) {
                serialState.put(point, 'b');
            }

            if (obj instanceof BlockCluster) {
                BlockCluster bc = (BlockCluster) obj;
                serialState.put(point, bc.getClusterID());
            }

            if (obj instanceof Wall) {
                serialState.put(point, 'w');
            }

        }
        return serialState;
    }

    /**
     * Reads a map of locations to character codes and spawns objects according to their
     * code.
     * //TODO: externalize this?
     *
     * @param serialState   A map of positions to character codes; the "serialized state".
     */
    public void inflateSerialState(Map<Vector2D, Character> serialState) {
        clearGameObjects();
        for (Vector2D point : serialState.keySet()) {
           processID(serialState.get(point), point);
        }

        update();
    }


    /**
     * Applies input in a certain direction, i.e. attempts to move all of the
     * players in the level (and push/pull any blocks the players can).  Repeatedly
     * attempts to move each player until there is no longer any change occurring.
     *
     *
      * @param moveDirection  The input direction.
     */
    public void processInput(Vector2D.Direction moveDirection) {
        //TODO: fix this up
        gameState = new HashMap<>(filledPositions);

        List<Player> failures = new ArrayList<>();
        int count = players.size();
        while (failures.size() != count) {
            count = failures.size();
            failures = new ArrayList<>();
            movePlayers(moveDirection, failures);
        }

        moveFailures(moveDirection);
        update();


    }

    /**
     * Attempts a single iteration of movement of all the players in a specified direction.
     * Moves all of the GRABALL type players first, then the remaining players.  Collects
     * any players that fail to move in the specified collection.
     *
     * @param moveDirection     The direction to attempt the move.
     * @param failures          A collection to put all players that fail to move.
     */
    private void movePlayers(Vector2D.Direction moveDirection,
                             Collection<Player> failures) {

        for (Player player : players) {
            if (player.getType() == Player.Type.GRABALL
                    && !player.move(moveDirection)) {

                //TODO: do we need this here?
                failures.add(player);
            }

        }

        for (Player player : players) {
            if (player.getType() != Player.Type.GRABALL
                    && !player.move(moveDirection)) {

                failures.add(player);

            }
        }
    }

    /**
     * Moves the PULL-type players alone (i.e. not pulling) if they have failed so far.
     * Repeatedly attempts to move them until there is no more movement.
     *
     * @param moveDirection     Direction to move.
     */
    private void moveFailures(Vector2D.Direction moveDirection) {
        int count = 1;
        while (count > 0) {
            count = 0;
            for (Player player : players) {
                if (player.canMove() && player.getType() == Player.Type.PULL) {
                    //TODO: evaluate following if statement's necessity
                    if (!player.move(moveDirection)) {
                        if (moveObject(player, moveDirection)) {
                            count += 1;
                        }
                    }
                    else {
                        count += 1;
                    }
                }
            }
        }
    }

    /**
     * Moves a game object in the specified direction, if possible.
     *
     * @param object        Game object to move.
     * @param direction     Direction in which to move the game object.
     * @return              Returns the success of the movement.
     */

    private boolean moveObject(GameObject object, Vector2D.Direction direction) {
        List<GameObject> obj = new ArrayList<>();
        obj.add(object);
        return moveGroup(obj, direction);
    }

    /**
     * Moves a group of objects all at once in the specified direction.  If any of the
     * objects fails in movement for some reason (e.g. the location it is trying to fill is
     * occupied) then they all fail, and no movement occurs.
     *
     * @param group         The group of game objects to move.
     * @param direction     The direction to move the game objects.
     * @return              Returns the success of the movement.
     */

    public boolean moveGroup(Collection<GameObject> group, Vector2D.Direction direction) {
        List<GameObject> movers = new ArrayList<>();
        for (GameObject obj : group) {
            if (obj.canMove()) {
                movers.add(obj);
            }
        }

        for (GameObject obj : movers) {
            Vector2D movePoint = obj.getLocation().getPointInDirection(direction);
            if (!isPointValid(movePoint, movers)) {
                return false;
            }
        }

        for (GameObject obj : movers) {
            filledPositions.remove(obj.getLocation());
        }
        for (GameObject obj : movers) {
            obj.setLocation(obj.getLocation().getPointInDirection(direction));
            filledPositions.put(obj.getLocation(), obj);

            obj.setMove(false);
        }
        return true;
    }


    /**
     * Performs all of the necessary activities that need to occur after an input has
     * been processed.  Checks for victory, resets ability of game objects to move if
     * they moved before, updates the border graphic calculations, and checks triggers,
     * acting if they have a game object on them.
     */
    private void update() {
        for (Trigger trigger : triggers) {
            GameObject filler = filledPositions.get(trigger.getLocation());
            if (trigger.isFilled()) {
                trigger.act(filler);
            }
            else {
                trigger.undo(filler);
            }
        }

        for (GameObject obj : gameObjects) {
            obj.setMove(true);
            //TODO: do we need to update borders every time? I think not. Just at the beginning.
            updateBorders(obj);
        }

        int targetCount = 0;
        for (Target target : targets) {
            GameObject filler = filledPositions.get(target.getLocation());
            if (filler != null && !(filler instanceof Player)) {
                targetCount += 1;
            }
        }
        if (targetCount == targets.size()) {
            isComplete = true;
        }


    }

    /**
     * Calculates what borders a gameObjects needs to have drawn by checking its type
     * and adjacencies.  Sections of wall and BlockClusters technically exist
     * as a bunch of connected units, but to contribute to the illusion that
     * they are single entities the borders which are inside these conglomerates
     * are not to be drawn, and that depends on level logic, so is calculated here.
     * Borders to be drawn are kept as a collection of directions.
     *
     * @param gameObject    The game objects whose borders should be calculated.
     */

    private void updateBorders(GameObject gameObject) {
        List<Vector2D.Direction> borders = new ArrayList<>();
        borders.addAll(Arrays.asList(Vector2D.Direction.values()));

        if (!(gameObject instanceof Wall || gameObject instanceof BlockCluster)) {
            //TODO: throw an error here?
            borderMap.put(gameObject, borders);
            return;
        }


        for (GameObject other : getAdjacentObjects(gameObject)) {
            if (!other.getClass().equals(gameObject.getClass())) {
                continue;
            }
            if (gameObject instanceof BlockCluster) {
                BlockCluster objCluster = (BlockCluster) gameObject;
                BlockCluster otherCluster = (BlockCluster) other;
                if (!objCluster.getClusterID().equals(otherCluster.getClusterID())) {
                    continue;
                }
            }

            Vector2D.Direction removal = Vector2D.relativeDirection(gameObject.getLocation(),
                    other.getLocation());
            borders.remove(removal);

        }
        borderMap.put(gameObject, borders);
    }

    /**
     * Returns a collection of borders for the specified object.  Borders
     * are defined by Vector2D.Directions.
     *
     * @param obj       The object to get the borders of
     * @return          Returns a collection of Vector2D.Directions that represent borders
     *                  to draw.
     */
    public List<Vector2D.Direction> getObjectBorders(GameObject obj) {
        return this.borderMap.get(obj);
    }


    /**
     * Checks whether a specified point is a valid point, i.e. that it is not occupied
     * by another game object and is within the level bounds.  The exclusion list provided
     * gives any game objects that can be ignored when checking if the point is occupied.
     *
     * @param point             The point to check.
     * @param exclusionList     A Collection of GameObjects to ignore when checking if the point
     *                          is occupied.
     * @return
     */
    private boolean isPointValid(Vector2D point, Collection<GameObject> exclusionList) {
        if (!point.isWithin(Vector2D.zero(), levelBounds)) {
            return false;
        }
        GameObject blocker = filledPositions.get(point);
        return (blocker == null || exclusionList.contains(blocker));
    }


    /**
     * @return Returns an unmodifiable list of the game objects in the level.
     */

    public List<GameObject> getGameObjects() {
        return Collections.unmodifiableList(this.gameObjects);
    }

    /**
     * @return  Returns an unmodifiable list of the triggers in the level.
     */
    public List<Trigger> getTriggers() {
        return Collections.unmodifiableList(this.triggers);
    }

    /**
     * @param point     The position to check.
     * @return          Returns whether a position is filled with a game object or not.
     */
    public boolean isPositionFilled(Vector2D point) {
        return filledPositions.containsKey(point);
    }

    /**
     * @param position      The location to check.
     * @return              Returns the game object at the specified position.
     */
    public GameObject getObjectAt(Vector2D position) {
        return filledPositions.get(position);
    }

    /**
     * @return      Returns whether or not the level is complete, i.e. all of the
     *              targets are covered by blocks/clusters.
     */
    boolean isComplete() {
        return isComplete;
    }

    /**
     * @return      Returns the string layout of this level.
     */
    public String getLayout() {
        return this.layout;
    }

    /**
     * @return      Returns the width of the grid (currently always 10).
     */
    public int getGridLength() {
        return columnNumber;
    }




}
