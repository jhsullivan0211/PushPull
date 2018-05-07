package com.example.pushpull.game_logic;



    import android.os.Build;

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
    import java.util.Collection;
    import java.util.Collections;
    import java.util.Comparator;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;




public class Level {





    private static final int columnNumber = 10;
    private static final int rowNumber = 10;

    private Vector2D levelBounds = new Vector2D(columnNumber - 1, rowNumber - 1);

    private String layout;

    private List<GameObject> gameObjects = new ArrayList<>();
    private Map<Vector2D, GameObject> filledPositions = new HashMap<>();
    private Map<Vector2D, GameObject> gameState = new HashMap<>();
    private Map<Vector2D, String> serializeableGameState = new HashMap<>();
    private List<Player> players = new ArrayList<>();
    private List<Trigger> triggers = new ArrayList<>();
    private List<Target> targets = new ArrayList<>();

    private Map<String, List<BlockCluster>> clusterGroups = new HashMap<>();
    private Map<GameObject, List<Vector2D.Direction>> borderMap = new HashMap<>();

    private boolean isComplete = false;

    public Level(String layout) {
        this.layout = layout;
        this.load(layout);
        update();
    }


    private void load(String layout) {
        this.gameObjects.clear();


       int position = 0;
       boolean doShift = false;

       for (char id : layout.toCharArray()) {
           if (id == ','|| id == '\n' || id == ' ') {
               continue;
           }
           int y = position / rowNumber;
           int x = position % columnNumber;
           processID(id, new Vector2D(x, y));
           if (doShift) {
               position += 1;
           }

           doShift = !doShift;

        }

        for (List<BlockCluster> clusterList : clusterGroups.values()) {
            for (BlockCluster cluster : clusterList) {
                cluster.makeCluster(clusterList);
            }
        }

        gameState = new HashMap<>(filledPositions);
        sortPlayers();
    }



    private void processID(Character idChar, Vector2D location) {
        //TODO: find a better way for this

        String id = Character.toString(idChar);
        if (id.equals("b")) {
            spawnGameObject(new Block(), location);
        }

        else if (id.equals("w")) {
            Wall spawnWall = new Wall();
            spawnGameObject(spawnWall, location);
        }
        else if (id.equals("o")) {
            Target target = new Target(location, this);
            triggers.add(target);
            targets.add(target);
        }

        else if (id.matches("[PQR]")) {
            triggers.add(new Transformer(Player.idToType(idChar), location, this));
        }

        else if (id.matches("[\\d!@#$%^&*]")) {

            BlockCluster spawned = new BlockCluster(idChar);
            spawnGameObject(spawned, location);

            if (!clusterGroups.containsKey(id)) {
                clusterGroups.put(id, new ArrayList<BlockCluster>());
            }
            clusterGroups.get(id).add(spawned);
        }

        else if (id.matches("[pqr]")) {
            Player.Type spawnType = Player.idToType(idChar);
            Player spawnPlayer = new Player(this, spawnType);
            spawnGameObject(spawnPlayer, location);
            players.add(spawnPlayer);
        }

        else if (!id.equals("x")){
            throw new RuntimeException("Object ID '" +  "" + id +  "' does not correspond to a known object.");
        }
    }

    public void revertState() {
        for (Vector2D position : gameState.keySet()) {
            gameState.get(position).setLocation(position);
        }
        this.filledPositions = gameState;
        update();
    }

    public void clearGameObjects() {
        gameObjects.clear();
        filledPositions.clear();
        players.clear();
    }

    public void spawnGameObject(GameObject gameObject, Vector2D spawnPoint) {
        gameObject.setLocation(spawnPoint);
        filledPositions.put(spawnPoint, gameObject);
        gameObjects.add(gameObject);
    }

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

    public Collection<List<BlockCluster>> getClusterGroups() {
        return this.clusterGroups.values();
    }

    public HashMap<Vector2D, Character> getSerialState() {
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

    public void inflateSerialState(Map<Vector2D, Character> serialState) {
        clearGameObjects();
        for (Vector2D point : serialState.keySet()) {
           processID(serialState.get(point), point);
        }

        update();
    }

    public void getNewObjectFromID(String id) {

    }

    public void sortPlayers() {
        Collections.sort(players, new Comparator<Player>() {
            @Override
            public int compare(Player first, Player second) {
                int firstVal = Player.getTypeInteger(first.getType());
                int secondVal = Player.getTypeInteger(second.getType());

                if (firstVal > secondVal) {
                    return 1;
                }
                if (firstVal == secondVal) {
                    return 0;
                }
                else {
                    return -1;
                }
            }
        });
    }


    public List<Player> getPlayers() {

        return Collections.unmodifiableList(players);
    }

    public List<GameObject> getGameObjects() {
        return Collections.unmodifiableList(this.gameObjects);
    }

    public List<Trigger> getTriggers() {
        return Collections.unmodifiableList(this.triggers);
    }

    public void processInput(Vector2D.Direction moveDirection) {
        //TODO: fix this up
        gameState = new HashMap<>(filledPositions);

        List<Player> failures = new ArrayList<>();
        int count = players.size();
        while (failures.size() != count) {
            count = failures.size();
            failures = new ArrayList<>();
            for (Player player : players) {
                if (player.getType() == Player.Type.GRABALL) {
                    if (!player.move(moveDirection)) {
                        failures.add(player);
                    }
                }

            }

            for (Player player : players) {
                if (player.getType() != Player.Type.GRABALL) {
                    if (!player.move(moveDirection)) {
                        failures.add(player);
                    }
                }
            }
        }

        count = 1;
        while (count > 0) {
            count = 0;
            for (Player player : players) {
                if (player.canMove() && player.getType() == Player.Type.PULL) {
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




        update();


    }






    public boolean moveObject(GameObject object, Vector2D.Direction direction) {
        List<GameObject> obj = new ArrayList<>();
        obj.add(object);
        return moveGroup(obj, direction);
    }

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

        sortPlayers();

    }

    public void updateBorders(GameObject obj) {
        List<Vector2D.Direction> borders = new ArrayList<>();
        for (Vector2D.Direction direction : Vector2D.Direction.values()) {
            borders.add(direction);
        }

        if (obj instanceof Wall) {
            for (GameObject other : getAdjacentObjects(obj)) {
                if (other instanceof Wall) {
                    Vector2D.Direction removal = Vector2D.relativeDirection(obj.getLocation(),
                                                    other.getLocation());

                    borders.remove(removal);
                }
            }
        }

        if (obj instanceof BlockCluster) {
            for (GameObject other : getAdjacentObjects(obj)) {
                if (other instanceof BlockCluster) {
                    BlockCluster objCluster = (BlockCluster) obj;
                    BlockCluster otherCluster = (BlockCluster) other;

                    if (objCluster.getClusterID().equals(otherCluster.getClusterID())) {
                        Vector2D.Direction removal = Vector2D.relativeDirection(obj.getLocation(),
                                other.getLocation());

                        borders.remove(removal);
                    }
                }
            }
        }

        borderMap.put(obj, borders);

    }

    public List<Vector2D.Direction> getObjectBorders(GameObject obj) {
        return this.borderMap.get(obj);
    }


    public void replace(GameObject original, GameObject replacement) {
        filledPositions.remove(original.getLocation());
        gameObjects.remove(original);
        spawnGameObject(replacement, original.getLocation());

    }

    public boolean isPointValid(Vector2D point) {
        return (!filledPositions.containsKey(point)
                && point.isWithin(Vector2D.zero(), levelBounds));
    }


    public boolean isPointValid(Vector2D point, Collection<GameObject> exclusionList) {
        if (!point.isWithin(Vector2D.zero(), levelBounds)) {
            return false;
        }
        GameObject blocker = filledPositions.get(point);
        return (blocker == null || exclusionList.contains(blocker));
    }

    public boolean isPositionFilled(Vector2D point) {
        return filledPositions.containsKey(point);
    }


    public void clearPosition(Vector2D position) {
        filledPositions.remove(position);
    }

    public GameObject getObjectAt(Vector2D position) {
        return filledPositions.get(position);
    }

    public boolean isComplete() {
        return isComplete;
    }

    public String getLayout() {
        return this.layout;
    }

    public int getGridLength() {
        return columnNumber;
    }




}
