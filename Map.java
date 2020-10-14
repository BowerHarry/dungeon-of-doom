// These two methods are used for reading from the text file.
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Reads and contains in memory the map of the game.
 */
public class Map {
	// Representation of the map
	private char[][] map;
	// Map name
	private String mapName;
	// Gold required for the human player to win
	private int goldRequired;
	
	/** 
	 * Default constructor, creates the default map "Very small Labyrinth of doom".
	 * @return 
	 */
	public Map() {
		mapName = "Very small Labyrinth of Doom";
		goldRequired = 2;
		map = new char[][]{
		{'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'},
		{'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
		{'#','.','.','.','.','.','.','G','.','.','.','.','.','.','.','.','.','E','.','#'},
		{'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
		{'#','.','.','E','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
		{'#','.','.','.','.','.','.','.','.','.','.','.','G','.','.','.','.','.','.','#'},
		{'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
		{'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
		{'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'}
		};
	}

	/**
	 * Constructor that accepts a map to read in from.
	 * @param fileName The filename of the map file.
	 */
	public Map(String fileName) {
		map = readMap(fileName);
	}

    /**
	 * Returns the gold required to win.
     * @return Gold required to exit the current map.
     */
    protected int getGoldRequired() {
        return goldRequired;
    }

    /**
	 * Returns the map stored in memory.
     * @return The map as stored in memory.
     */
    protected char[][] getMap() {
        return map;
    }

    /**
	 * Returns the name of the current map.
     * @return The name of the current map.
     */
    protected String getMapName() {
        return mapName;
    }

    /**
	 * Reads the map from a text file.
	 * @param fileName Name of the map's file.
	 * @return The map as read from the file.
	 */
    private char[][] readMap(String fileName) {
		String strMap = "";
		// Tries to store the contents of the file as a string.
		try {
			strMap = new String ( Files.readAllBytes(Paths.get(fileName)));
		} 
		// Any errors produced are ignored (eg. reaching the end of the file).
		catch (Exception e) {
			e.printStackTrace();
		}
		// The contents of the file are split into its respective rows.
		String[] rows = strMap.split(System.getProperty("line.separator")); 
		// The first row always contains the map name in the format "name ....".
		mapName = rows[0].replace("name ", "");
		// The second row always contains the gold required to win in the format "win ....".
		char number = (rows[1].replace("win ", "")).charAt(0);
		goldRequired = Character.getNumericValue(number);
		// Creates an empty 2D char array with the same size as the map.
		map = new char[(rows.length-2)][rows[2].length()-1];
		// Loops through every character in the map which is stored as a string, and places it in the 2D char array.
		for (int j = 0; j <= (rows.length)-3; j++){
			for (int i = 0; i <= (rows[2].length()-2); i++){
				try {
					map[j][i] = rows[j+2].charAt(i);
				} 
				catch (Exception e) {
					map[j][i] = '#';
                }
			}
		}
		return map;
	}

}
