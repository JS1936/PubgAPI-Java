import org.json.JSONObject;

import java.io.File;
import java.util.Collections;

public class MapManager {
    //Prints the names of maps played and how many times they were played.
    //EX: "<mapName> x5" means <mapName> was played 5 times.
    public static void printMapNames() //don't need this parameter
    {
        Collections.sort(Main.mapsPlayed); //import java.util.Collections

        System.out.println("Frequencies of each map played: "); //added "played" 9/15
        int frequency = 1;
        int i = 0;
        while(i < Main.mapsPlayed.size()) {
            if(i + 1 == Main.mapsPlayed.size())
            {
                System.out.println(Main.mapsPlayed.get(i) + " x" + frequency);
                return;
            }
            for (int j = i + 1; j < Main.mapsPlayed.size(); j++)
            {
                if (Main.mapsPlayed.get(i).equalsIgnoreCase(Main.mapsPlayed.get(j))) {
                    //System.out.println("val at index " + i + " = j");
                    frequency++;
                } else {
                    System.out.println(Main.mapsPlayed.get(i) + " x" + frequency);
                    frequency = 1;
                }
                i++;
            }
        }
    }

    //Given a prettified file holding data on a pubg match, returns the name of the map played on in the match.
    public static String getMapName(File prettyFile)
    {
        JSONObject match_start = JSONManager.returnObject(prettyFile, "LogMatchStart");
        String mapName = match_start.get("mapName").toString();
        //System.out.println("mapName: " + mapName);
        return mapName;
    }
}
