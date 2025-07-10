import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;

//The Ranking class calculates and prints the ranking of a specific player in a specific match or matches.
public class MatchRanking {

    /*
     * Returns the ranking of a player in a specific match as a String.
     * If the searched player was not in the match, both returns an empty string
     * and prints a message to console and requestHistory.
     */
    public static String ranking(String name, File prettyFile) throws IOException {
        String match_id = JSONManager.getJSONValue(prettyFile, "LogMatchDefinition", "MatchId");
        JSONObject match_end = JSONManager.returnObject(prettyFile, "LogMatchEnd");

        JSONArray players = match_end.getJSONArray("characters");
        for (int i = 0; i < players.length(); i++) {
            JSONObject player = players.getJSONObject(i);
            JSONObject player_details = player.getJSONObject("character");
            String player_name = player_details.get("name").toString();

            if (player_name.equalsIgnoreCase(name)) {
                String player_ranking = player_details.get("ranking").toString();
                String formal_rankStatement = "\n-player:" + player_name + ", rank: " + player_ranking + ", match: " + match_id;
                FileManager.writeToFileAndConsole(formal_rankStatement);
                return player_ranking;
            }
        }
        String playerWasAbsent = name + " was not in the game (" + prettyFile.getName() + ")";
        FileManager.writeToFileAndConsole(playerWasAbsent);
        return ""; //was not in the game
    }
}