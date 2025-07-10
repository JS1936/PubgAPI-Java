import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

//The Ranking class calculates and prints the ranking of a specific player in a specific match or matches.
public class MatchRanking {

    /*
     * Returns ranking as a string
     * Console and history printouts intentionally differ slightly for readability of console user.
     */
    public static String ranking(String name, File prettyFile) throws IOException {
        JSONObject match_definition = JSONManager.returnObject(prettyFile, "LogMatchDefinition");
        String match_id = match_definition.get("MatchId").toString();
        JSONObject match_end = JSONManager.returnObject(prettyFile, "LogMatchEnd");

        JSONArray players = match_end.getJSONArray("characters");
        for (int i = 0; i < players.length(); i++) {
            JSONObject player = players.getJSONObject(i);
            JSONObject player_details = player.getJSONObject("character");
            String player_name = player_details.get("name").toString();

            if (player_name.equalsIgnoreCase(name)) {
                String player_ranking = player_details.get("ranking").toString();

                //String casual_rankStatement = name + " rank in this game: " + player_details.get("ranking").toString();
                String formal_rankStatement = "\n-player:" + player_name + ", rank: " + player_ranking + ", match: " + match_id;

                //FileManager.writeToFileAndConsole(casual_rankStatement);
                FileManager.writeToFileAndConsole(formal_rankStatement);
               // System.out.println(name + " rank in this game: " + player_details.get("ranking").toString());

               // try {
               //     FileUtils.writeStringToFile(Main.requestHistory, "\n-player:" + player_name + ", rank: " + player_ranking + ", match: " + match_id, (Charset) null, true); //changed requestedResults to currentFile //added 9/15
               // } catch (IOException e) {
               //     e.printStackTrace();
               // }
                return player_details.get("ranking").toString();
            }
        }
        String playerWasAbsent = name + " was not in the game (" + prettyFile.getName() + ")";
        FileManager.writeToFileAndConsole(playerWasAbsent);
        return ""; //was not in the game
    }

    /*
    public static String ranking_debuggingVersion(String name, File prettyFile) throws IOException {
        JSONObject match_definition = JSONManager.returnObject(prettyFile, "LogMatchDefinition");
        System.out.println("match definition: " + match_definition);
        String match_id = match_definition.get("MatchId").toString();
        System.out.println("match_id = " + match_id);
        JSONObject match_end = JSONManager.returnObject(prettyFile, "LogMatchEnd");
        System.out.println("Attempting to print match_end content: " + match_end);

        JSONArray players = match_end.getJSONArray("characters");
        for (int i = 0; i < players.length(); i++) {
            JSONObject player = players.getJSONObject(i);
            System.out.println("Attempting to print match_end content (1line): " + player);
            JSONObject player_details = player.getJSONObject("character");
            String player_name = player_details.get("name").toString();

            if (player_name.equalsIgnoreCase(name)) {
                String player_ranking = player_details.get("ranking").toString();
                System.out.println(name + " rank in this game: " + player_details.get("ranking").toString());

                try {
                    FileUtils.writeStringToFile(Main.requestHistory, "\n-player:" + player_name + ", rank: " + player_ranking + ", match: " + match_id, (Charset) null, true); //changed requestedResults to currentFile //added 9/15
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return player_details.get("ranking").toString();
            }
        }
        String playerWasAbsent = name + " was not in the game (" + prettyFile.getName() + ")";
        FileManager.writeToFileAndConsole(playerWasAbsent);
        return ""; //was not in the game
    }
     */
}


//Notes-buffer (will be removed soon):
//
//  Note: match_id says whether fpp, tdm, etc.
//  Note #2: make writeStringToFile result look cleaner
//
//  Potential future changes:
//      just get the matchID, too
//      Given a name, searches for that person, and if they were in the provided games, gives their ranking(s)
//      COULD use printPlayersByTeam (excess printouts) OR do it independently
//      could take in String[] names instead of String name? (In case of multiple names...?)
//  public static void ranking(String[] team, File prettyFile)
//  {
//    for(String name : team)
//    {
//        ranking(name, prettyFile);
//    }
//  }