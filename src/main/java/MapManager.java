import java.io.IOException;
import java.util.Collections;

//The MapManager tracks the frequency of various pubg maps across multiple matches.
public class MapManager {

    //Prints the names of maps played and how many times they were played.
    //EX: "<mapName> x5" means <mapName> was played 5 times.
    public static void printMapNames() throws IOException //don't need this parameter
    {
        Collections.sort(Main.mapsPlayed); //import java.util.Collections

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

    /*
    //NOTE: Commented out because instead used in MatchManager.
    //Given a prettified file holding data on a pubg match, returns the name of the map played on in the match.
    public static String getMapName(File prettyFile)
    {
        JSONObject match_start = JSONManager.returnObject(prettyFile, "LogMatchStart");
        String mapName = match_start.get("mapName").toString();
        return mapName;
    }
     */
}
