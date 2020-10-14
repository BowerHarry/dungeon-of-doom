// This method is used to generate a random number, which is used to place the player in a random position on the map.
import java.util.concurrent.ThreadLocalRandom;

/**
 * Runs the game with a human player and contains code needed to read inputs.
 */ 
public class HumanPlayer { 
    // X coordinate of the human player
    private int humanX;
    // Y coordinate of the human player
    private int humanY;
    // Token that is currently underneath the human player
    private char underPlayer;

    /**
     * Default constructor
     * Positions the human player in a random position on the map.
     * @param gameMap Representation of the map.
     */
    public HumanPlayer(char[][] gameMap){
        int randNumber = -1;
        // The first iteration of the loop counts every potential floor tile that the player could start on.
        // The second iteration of the loop randomly chooses a position for the player.
        for (int iteration = 0; iteration < 2; iteration++) {
            int counter = 0;
            for (int i = 0; i < gameMap.length; i++) {
                for (int j = 0; j < gameMap[i].length; j++) {
                    // '.' and 'E' are the only tiles that the player can start on.
                    if (gameMap[i][j] == '.' || gameMap[i][j] == 'E') {
                        if (iteration == 1 && counter == randNumber) {
                            underPlayer = gameMap[i][j];
                            // Sets initial player position.
                            humanX = j;
                            humanY = i;
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
     * Reads player's input from the console.
     * <p>
     * @return A string containing the input the player entered.
     */
    protected String getInputFromConsole() {
        System.out.println("Enter command:");
        String input = System.console().readLine();
        return input;
    }

    /**
     * Returns the human player's x coordinate.
     * @return Human player's x coordinate.
     */
    protected int gethumanX(){
        return humanX;
    }

    /**
     * Returns the human player's y coordinate.
     * @return Human player's y coordinate.
     */
    protected int gethumanY(){
        return humanY;
    }

    /**
     * Returns the token currently underneath the player.
     * @return Token currently underneath the player.
     */
    protected char getUnderPlayer(){
        return underPlayer;
    }
}