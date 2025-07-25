import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

/*
 * The MatchPlayers class prints the names of all players in a match, sorted by team.
 * 
 * Future considerations:
 *  - Refactor addPlayers()
 *  - Use List instead of Vector
 */
public class MatchPlayers {

    /*
     * Given a file representing data on a specific official (non-custom) match, prints all players, sorted by team.
     * EX for one player: "zaeliax 18 15". Match team id is 18, match ranking is 15.
     * 
     * NOTES:
     * -Does not printPlayersByTeam for custom games (EX: deathmatches use IDs 1 and 2, teams of up to 8 players)
     * -Team size not always 4 (or even <=4!)
     * -Official game team ids: team id less than or equal to 100 = real people, 2xx (like 201) = bots, 5xx = guards
     */
    public static Vector<JSONObject> printPlayersByTeam(File prettyFile) throws IOException
    {
        Vector<JSONObject> peopleByTeam = populatePlayersByTeam(prettyFile);
        String match_id = MatchManager.getMatchID(prettyFile);
        int team_capacity = MatchManager.getMaximumTeamSizeForOfficialMatch(match_id);
        printPlayersByTeam(team_capacity, peopleByTeam);
        return peopleByTeam;
    }

    /*
     * Creates, populates, and returns peopleByTeam. Holds every participant, from lowest to highest team_ids
     * Public in case of use by other functionalties. 
     */
    public static Vector<JSONObject> populatePlayersByTeam(File prettyFile) throws IOException
    {
        exitIfGameIsCustom(prettyFile);
        
        String match_id = MatchManager.getMatchID(prettyFile);
        int team_capacity = MatchManager.getMaximumTeamSizeForOfficialMatch(match_id);

        //Determine size of peopleByTeam
        int max_num_teams = getMaxTeams(match_id, team_capacity);
        int maxIndices = max_num_teams * team_capacity;

        //Create and populate peopleByTeam. Holds every participant, from lowest to highest team_ids
        Vector<JSONObject> peopleByTeam = new Vector<JSONObject>();
        peopleByTeam.setSize(maxIndices);
        peopleByTeam = addPlayers(prettyFile, peopleByTeam, team_capacity);
        
        return peopleByTeam;
    }

    /*
     * Exits program if the match is custom.
     * Note: Deathmatches are a form of custom match.
     */
    private static void exitIfGameIsCustom(File prettyFile) throws IOException
    {
        JSONObject match_start = JSONManager.returnObject(prettyFile, "LogMatchStart");
        if(match_start.getBoolean("isCustomGame")) {
            FileManager.writeToFileAndConsole("Sorry, we don't compute data for custom games");
            System.exit(0);
        }
    }
    
    /*
     * Returns playersList (JSONArray of "characters" from "LogMatchEnd")
     */
    private static JSONArray getPlayersList(File prettyFile)
    {
        return (JSONManager.returnObject(prettyFile, "LogMatchEnd")).getJSONArray("characters"); //playersList
    }

    /*
     * The maximum number of teams is decided by the maximum possible teamId.
     * Returns 600 if team_capacity is 1, 2, or 4. 
     * Returns 2 if team_capacity > 4, such as with a deathmatch (up to 8 per team). Not a normal battle royale.
     */
    private static int getMaxTeams(String match_id, int team_capacity)
    {
        return (team_capacity > 4) ? 2 : 600;
    }
    
    /*
     * Prints a match's players (real and bots) by name, teamId, and ranking of all players (from lowest to highest team id).
     * EX: "<name> <teamId> <ranking>" could look like "WE__mao_ 200 19". A teamId of 2xx means they are a bot.
     */
    private static void printPlayersByTeam(int team_capacity, Vector<JSONObject> peopleByTeam) throws IOException
    {
        for (int i = 0; i < peopleByTeam.size(); i++) {
            JSONObject player = peopleByTeam.get(i);
            if (player != null) {
                if(i % team_capacity == 0) {
                    FileManager.writeToFileAndConsole("-----"); //For display clarity. Separates teams.
                }
                String output = String.format("%s %s %s",
                    player.get("name"),
                    player.get("teamId"),
                    player.get("ranking"));
                FileManager.writeToFileAndConsole(output);
            }
        }
    }

    /*
     * Returns a Vector<JSONObject> holding "character" information on each player in the match, sorted by team.
     * Helper for populatePlayersByTeam(File prettyFile)
     */
    private static Vector<JSONObject> addPlayers(File prettyFile, Vector<JSONObject> peopleByTeam, int team_capacity)
    {
        JSONArray playersList = getPlayersList(prettyFile);
       
        //Set up all teams by storing each player in an index that makes sense for their team_id
        for (int j = 0; j < playersList.length(); j++) {

            //Get access to one player and their details (such as team_id)
            JSONObject player = playersList.getJSONObject(j);
            JSONObject character = player.getJSONObject("character");
            String team_id = character.get("teamId").toString();
            int team_id_index = Integer.parseInt(team_id);

            //Determine at what index to store this player in peopleByTeam
            int insert = team_id_index * team_capacity;

            //Put player in peopleByTeam according to their team_id
            for (int loc = 0; loc < team_capacity; loc++) {
                if (peopleByTeam.get(insert + loc) != null) { //Some team member holds that spot.
                    if(peopleByTeam.get(insert + loc).get("name") == character.get("name")) { //Self holds that spot
                        loc = team_capacity;
                    }
                } else {
                    peopleByTeam.set(insert + loc, character); //Adding player here
                    loc = team_capacity; //Don't want to add the player multiple times (leave room for other people!)
                }
            }
        }
        return peopleByTeam;
    }
}