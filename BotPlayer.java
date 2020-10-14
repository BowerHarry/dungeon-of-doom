// This method is used to generate a random number, which is used to place the bot in a random position on the map.
import java.util.concurrent.ThreadLocalRandom;
// This library is used to find the magnitide of an integer.
import java.lang.Math;

/**
 * Runs the game with a bot player and contains code needed to chase the player.
 */
public class BotPlayer {
    // X coordinate of the bot player.
    private int botX;
    // Y coordinate of the bot player.
    private int botY;
    // Number of turns since the bot used the look command.
    private int turnsSinceLook;
    // X coordinate of the human player last time they were seen.
    private int lastSeenX;
    // Y coordinate of the human player last time they were seen by the bot player.
    private int lastSeenY;
    // Number of turns since the bot player last saw the human player.
    private int turnsSinceSeen;
    // Resolves to true if the human player was seen within the last 2 turns.
    private boolean playerNearby = false;
    // Stores the command the bot used on it's last move.
    private String lastMove;

    /**
     * Default constructor
     * Positions the human player in a random position on the map.
     * @param gameMap Representation of the map
     * @param playerX Human player's starting x coordinate
     * @param playerY Human player's starting y coordinate
     */
    public BotPlayer(char[][] gameMap, int playerX, int playerY){
        int randNumber = -1;
        boolean bool = true;
        // The first iteration of the loop counts every potential floor tile that the bot player could start on.
        // The second iteration of the loop randomly chooses a position for the bot player.
        // If the bot is positioned on the same tile as the human then a new tile is chosen.
        while(bool) {
            int counter = 0;
            for (int i = 0; i < gameMap.length; i++) {
                for (int j = 0; j < gameMap[i].length; j++) {
                    // '.' and 'E' are the only tiles that the player can start on.
                    if (gameMap[i][j] == '.' || gameMap[i][j] == 'E') {
                        if (counter == randNumber && j!=playerX && i!=playerY) {
                            // Sets initial bot position.
                            botX = j;
                            botY = i;
                            bool = false;
                        }
                        counter++;
                    }
                }
            }
            // Chooses a random number between 0 and the number of available floor tiles.
            randNumber = ThreadLocalRandom.current().nextInt(0, counter + 1);
        }
    }

    /**
     * Establishes the command the bot will use.
     * @param turnCount Current turn in the game.
     * @return A string containing the bot's command for this turn.
     */
    protected String getInputFromBot(int turnCount, int botX, int botY){
        turnsSinceLook +=1;
        turnsSinceSeen +=1;
        // The bot will always use the look command on it's first turn.
        if (turnCount == 1){
            return "LOOK";
        } 
        // If the bot has recently seen the human player.
        if (playerNearby) {
            // Two turns after seeing the player the bot will use the look command again.
            if (turnsSinceSeen != 3) {
                int distanceX = lastSeenX - botX;
                int distanceY = lastSeenY - botY;
                // For two turns it will travel towards the last known position of the player.
                // If it is on the last known position of the player it will move in the same direction as last turn.
                // Otherwise the bot would never catch the player.
                if (distanceX == 0 && distanceY == 0) {
                    return lastMove;
                }
                if (Math.abs(distanceX) >= Math.abs(distanceY)) {
                    if (distanceX > 0) {
                        lastMove = "MOVE E";
                        return "MOVE E";
                    }
                    if (distanceX < 0) {
                        lastMove = "MOVE W";
                        return "MOVE W";
                    }
                }
                if (distanceY > 0){
                    lastMove = "MOVE S";
                    return "MOVE S";
                }
                if (distanceY < 0) {
                    lastMove = "MOVE N";
                    return "MOVE N";
                }
                return "LOOK";
            }
            else {
                playerNearby = false;
                lastMove = randomMove();
                return "LOOK";
            } 
        }
        // If the human player is not known to be nearby the bot will use the look command once every 2 turns.
        else {
            if (turnsSinceLook == 3) {
                return "LOOK";
            }
            // When the bot doesn't know of the human player's position it moves randomly.
            else {
                return randomMove();
            }
        }
    }

    /**
     * Returns a string containing a random direction to move in.
     * @return A string containing a random direction to move in
     */
    private String randomMove(){
        // Generates a random number between 1 and 4.
        int randInput = ThreadLocalRandom.current().nextInt(1, 4);
        switch (randInput) {
        case 1:
            return "MOVE N";
        case 2:
            return "MOVE S";
        case 3:
            return "MOVE E";
        case 4:
            return "MOVE W";
        // Should never be reached.
        default:
            return "LOOK";
                }
    }

    /**
     * Checks to see if the human player is near to bot.
     * @param strMap The bot's nearby surroundings stored in a string
     * @param humanX Human player's x coordinate
     * @param humanY Human player's y coordinate
     */
    protected void playerNearBot(String strMap, int humanX, int humanY){
        turnsSinceLook = 0;
        // If the human player is shown to be in the string, it must be nearby.
        if (strMap.contains("P")){
            playerNearby = true;
            // Store the last known location of the human player.
            lastSeenX = humanX;
            lastSeenY = humanY;
            turnsSinceSeen = 0;
        }  
    }

    /**
     * Returns the bot player's x coordinate.
     * @return Bot player's x coordinate
     */
    protected int getBotX(){
        return botX;
    }

    /**
     * Returns the bot player's y coordinate.
     * @return Bot player's y coordinate
     */
    protected int getBotY(){
        return botY;
    }
}