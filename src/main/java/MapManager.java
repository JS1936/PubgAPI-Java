import java.io.IOException;
import java.util.Collections;

/*
 * The MapManager class tracks and reports the frequency of various pubg maps across multiple matches.
 * Note: getMapName(File prettyFile), previously located in MapManager.java, is now in MatchManager.java.
 */
public class MapManager {

    /*
     * Prints the names of maps played and how many times they were played.
     * EX: "<mapName> x5" means <mapName> was played 5 times.
     */
    public static void printMapNames() throws IOException
    {
        Collections.sort(Main.mapsPlayed); //alphabetical
        FileManager.writeToFileAndConsole("Frequencies of each map played: ");
        int frequency = 1;
        int i = 0;
        while(i < Main.mapsPlayed.size()) {
            if(i + 1 == Main.mapsPlayed.size())
            {
                FileManager.writeToFileAndConsole(Main.mapsPlayed.get(i) + " x" + frequency);
                return;
            }
            for (int j = i + 1; j < Main.mapsPlayed.size(); j++)
            {
                if (Main.mapsPlayed.get(i).equalsIgnoreCase(Main.mapsPlayed.get(j))) {
                    frequency++;
                } else {
                    System.out.println(Main.mapsPlayed.get(i) + " x" + frequency);
                    frequency = 1;
                }
                i++;
            }
        }
    }
}