/**
 * Contains the main logic part of the game, as it processes.
 */
public class GameLogic {
    HumanPlayer player;
    BotPlayer bot;
    private Map map;
    // Representation of the map
    private char[][] gameMap;
    // Human player's current gold total
    private int gold = 0;
    // Human player's x coordinate
    private int humanX;
    // Human player's y coordinate
    private int humanY;
    // The x coordinate of the player whose turn it currently is
    private int playerX;
    // The y coordinate of the player whose turn it currently is
    private int playerY;
    // Token that is currently underneath the human player
    private char underPlayer;
    // Bot player's x coordinate
    private int botX;
    // Bot player's y coordinate
    private int botY;
    // Is true if it's currently the human player's turn
    private boolean humanTurn = true;

    /**
     * Default constructor
     */
    public GameLogic() {
        // Creates a new instance of the map.
        // Enter the path of the map you would like to play with.
        map = new Map();
        gameMap = map.getMap();
    }

    /**
     * Main body of the game.
     * Handles the turn by turn logic.
     */
    protected void gameRunning() {
        // Creates a new instance of the human player.
        player = new HumanPlayer(gameMap);
        // Fetches the human player's starting coordinates.
        humanX = player.gethumanX();
        humanY = player.gethumanY();
        underPlayer = player.getUnderPlayer();
        // Creates a new instance of the bot player.
        bot = new BotPlayer(gameMap, humanX, humanY);
        // Fetches the bot player's starting coordinates.
        botX = bot.getBotX();
        botY = bot.getBotY();
        int turnCount = 1;
        // Loop ensures the game runs indefinitely until the user quits.
        while (true) {
            String input;
            // If it's the human player's turn, get input from the console.
            if (humanTurn) {
                System.out.println("\nTurn " + turnCount);
                turnCount += 1;
                input = player.getInputFromConsole();
                playerX = humanX;
                playerY = humanY;
            }
            // If it's the bot player's turn, establish what command it is going to use.
            else {
                System.out.println("\nVillain's turn:");
                input = bot.getInputFromBot(turnCount, botX, botY);
                System.out.println(input);
                playerX = botX;
                playerY = botY;
            }
            switch (input.toUpperCase()) {
            // If the player used the move command, call the move method to handle the movement and update the map.
            case "MOVE N":
                System.out.println(move('N'));
                break;
            case "MOVE S":
                System.out.println(move('S'));
                break;
            case "MOVE E":
                System.out.println(move('E'));
                break;
            case "MOVE W":
                System.out.println(move('W'));
                break;
            // The hello command prints the gold required to win the game.
            case "HELLO":
                System.out.println("Gold to win: " + hello());
                break;
            // The gold command prints how much gold the player earns.
            case "GOLD":
                System.out.println("Gold owned: " + gold());
                break;
            // The look command prints the player's nearby surroundings.
            case "LOOK":
                System.out.println(look());
                break;
            // If there is gold underneath the player, the pickup command adds one to your gold and updates the map.
            case "PICKUP":
                System.out.println(pickup());
                break;
            // The quit command handles the ending of the game, and prints WIN or LOSE.
            case "QUIT":
                quitGame();
            // Invalid input also uses up the player's turn.
            default:
                System.out.println("INVALID");
            }
            // If at any point the bot is on the same tile as the human player, the human loses and the game quits.
            if (botX == humanX && botY == humanY) {
                System.out.println("LOSE the villain caught you.");
                System.exit(0);

            }
            // Alternates between human player's turn and the bot player's turn.
            humanTurn = !humanTurn;
        }
    }

    /**
     * Returns the gold required to win.
     * @return Gold required to win.
     */
    protected String hello() {
        return String.valueOf(map.getGoldRequired());
    }

    /**
     * Returns the gold currently owned by the player.
     * @return Gold currently owned.
     */
    protected String gold() {
        return String.valueOf(gold);
    }

    /**
     * Returns the name of the current map.
     * @return Map name.
     */
    protected String mapName() {
        return map.getMapName();
    }

    /**
     * Checks if movement is legal and updates player's location on the map.
     * @param direction The direction of the movement.
     * @return Protocol if success or not.
     */
    protected String move(char direction) {
        boolean success = false;
        switch (direction){
            // Checks whether the tile north of the player is a wall.
            case 'N':
                if (gameMap[playerY-1][playerX] != '#'){ 
                    // If it's not a wall then change the player's y coordinate.
                    playerY = playerY - 1;
                    success = true;
                }
                break;
            // Checks whether the tile south of the player is a wall.
            case 'S':
                if (gameMap[playerY+1][playerX] != '#'){ 
                    // If it's not a wall the change the player's y coordinate.
                    playerY = playerY + 1;
                    success = true;
                }
                break;
            case 'E':
                if (gameMap[playerY][playerX+1] != '#'){ 
                    playerX = playerX + 1;
                    success = true;
                }
                break;
            case 'W':
                if (gameMap[playerY][playerX-1] != '#'){ 
                    playerX = playerX - 1;
                    success = true;
                }
                break;
        }
        // Update the player's coordinates.
        if (humanTurn) {
            humanX = playerX;
            humanY = playerY;
            // Stores the token hidden underneath the player.
            underPlayer = gameMap[humanY][humanX];
            // Returns whether the move was successful or not.
            if (success) {
                return "SUCCESS";
            }
            else {
                return "FAIL";
            }
        }
        else {
            botX = playerX;
            botY = playerY;
            return "";
        }
    }

    /**
     * Converts the map from a 2D char array to a single string.
     * @return A String representation of the immediate surroundings.
     */
    protected String look() {
        String strMap = "";
        // For every character in the 2D array within two floor tiles either side of the player.
        for (int i = (playerY-2); i <= (playerY+2); i++) {
            for (int j = (playerX-2); j <= (playerX+2); j++) {
                // Positions the player in the centre of the nearby map.
                if (i == humanY & j == humanX) {
                    strMap = strMap + " P ";
                }
                // If the floor tile contains the bot.
                else if (i == botY & j == botX) {
                    // Add it to the map.
                    strMap = strMap + " B ";
                }
                // Otherwise try and add the character to the map
                else {
                    try {
                        strMap = strMap + " " + gameMap[i][j] + " ";
                    // The only exceptions should be when the 2D array is out of range.
                    } catch (Exception e) {
                        // In which case add a wall tile to the map.
                        strMap = strMap + " # ";
                    }
                }
            }
            // After every row add a new line.
            strMap = strMap + "\n";
        }
        // If it is the bot's turn, find out if the player is showing on the bot's nearby map.
        if (!humanTurn){
            bot.playerNearBot(strMap, humanX, humanY);
            return "";
        }
        // On the human player's turn, return the map of the nearby surroundings.
        else {
            return strMap;
        }
        
    }

    /**
     * Processes the player's pickup command, updating the map and the player's gold
     * amount.
     * @return If the player successfully picked-up gold or not.
     */
    protected String pickup() {
        boolean success = false;
        // If the player is standing on gold...
        if (underPlayer == 'G') {
            // Add 1 to their total gold...
            gold += 1;
            // And update the map to show the gold has been picked up.
            gameMap[humanY][humanX] = '.';
            success = true;
        }
        if (success) {
            return "SUCCESS. Gold owned: " + gold();
        }
        else {
            return "FAIL. Gold owned: " + gold();
        }
    }

    /**
     * Quits the game, shutting down the application.
     */
    protected void quitGame() {
        // The win condition is that the player should have collected all the gold and also be standing on an "E".
        if ((underPlayer == 'E') & (gold == map.getGoldRequired())) {
            // If they meet the conditions they win.
            System.out.println("WIN");
        }
        // Otherwise the player loses.
        else {
            System.out.println("LOSE");
        }
        // Quits the game.
        System.exit(0);
    }

    
    /** 
     * Main method.
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("\nDungeon of Doom");
        // Creates new instance of GameLogic.
        GameLogic logic = new GameLogic();
        // Prints the name of the current map.
        System.out.println("\nYou have entered the " + logic.mapName());
        // Runs the game.
        logic.gameRunning();
    }
}