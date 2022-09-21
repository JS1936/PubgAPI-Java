import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class Ranking {

//just get the matchID, too
    //Given a name, searches for that person, and if they were in the provided games, gives their ranking(s)
    //COULD use printPlayersByTeam (excess printouts) OR do it independently
    //could take in String[] names instead of String name? (In case of multiple names...?)
    //public static void ranking(String[] team, File prettyFile)
    //{
    //    for(String name : team)
    //    {
    //        ranking(name, prettyFile);
    //    }
    //}

    //Returns ranking as a string
    public static String ranking(String name, File prettyFile) {
        JSONObject match_definition = JSONManager.returnObject(prettyFile, "LogMatchDefinition");
        //System.out.println("match definition: " + match_definition);
        String match_id = match_definition.get("MatchId").toString();
        System.out.println("match_id = " + match_id);


        JSONObject match_end = JSONManager.returnObject(prettyFile, "LogMatchEnd");
        //System.out.println("Attempting to print match_end content: " + match_end);
        JSONArray players = match_end.getJSONArray("characters");
        for (int i = 0; i < players.length(); i++) {
            JSONObject player = players.getJSONObject(i);
            //System.out.println("Attempting to print match_end content (1line): " + player);
            JSONObject player_details = player.getJSONObject("character");
            String player_name = player_details.get("name").toString();
            //System.out.println("\t" + player_name);
            if (player_name.equalsIgnoreCase(name)) {
                String player_ranking = player_details.get("ranking").toString();
                System.out.println(name + "rank in this game: " + player_details.get("ranking").toString());
                //added 9/15-> write to file here?

                //probably want the matchID, too
                //can it be made to automatically open the file, too?
                try {
                    //System.out.println("Inside try");
                    //LOOK HERE-> fix
                    FileUtils.writeStringToFile(Main.currentFile, "\n-" + player_name + ", " + player_ranking + ", " + match_id, (Charset) null, true); //
                    FileUtils.writeStringToFile(Main.requestHistory, "\n-player:" + player_name + ", rank: " + player_ranking + ", match: " + match_id, (Charset) null, true); //changed requestedResults to currentFile //added 9/15
                    //Note: match_id says whether fpp, tdm, etc.
                    //Note #2: make writeStringToFile result look cleaner
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //System.out.println("After try");
                return player_details.get("ranking").toString();
            }
        }
        return ""; //was not in the game
    }
}
