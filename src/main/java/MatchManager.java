import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

/*
 * The MatchManager class reports basic match data (EX: match id, map name).
 */
public class MatchManager {

    /*
     * Returns the match id of a prettified file.
     * Example: match.bro.official.pc-2018-36.steam.squad-fpp.na.2025.06.14.23.6d3a19db-fb2c-4d29-b64c-47cc67073bad
     */
    public static String getMatchID(File prettyFile)
    {
        JSONObject match_definition = JSONManager.returnObject(prettyFile, "LogMatchDefinition");
        return match_definition.get("MatchId").toString();
    }

    /*
     * Returns the player perspective (fpp or tpp) for the given match_id.
     *      fpp = first player perspective
     *      tpp = third player perspective
     */
    public static String getPlayerPerspective(String match_id)
    {
        if(match_id.contains("fpp")) { return "fpp"; }
        return "tpp";
    }

    /*
     * Returns the "type" of match for the given match_id.
     *      arcade = two teams, maximum of 8v8, minimum start of 4v4
     *      official = 25-100 teams, can be solo, duo, or squad
     */
    public static String getMatchType(String match_id)
    {
        if(match_id.contains("arcade"))   {  return "arcade"; }
        if(match_id.contains("seasonal")) {  return "seasonal"; }
        return "official";
    }

    /*
     * Given a pubg match telemetry file (not match id), returns true if the 
     * match is official. Otherwise, returns false.
     */
    public static boolean isOfficialMatch(File file)
    {
        JSONObject jsonObject = JSONManager.returnObject(file, "LogMatchDefinition");
        String match_id = jsonObject.get("MatchId").toString();
        return (match_id.contains("official"));
    }

    /*
     * Returns the specific team size for an official match 
     * (solo = 1, duo = [1,2], squad = [1,4]).
     */
    public static int getMaximumTeamSizeForOfficialMatch(String match_id)
    {
        if(match_id.contains("solo")) {  return 1; }
        else if(match_id.contains("duo")) {  return 2; }
        else if(match_id.contains("squad")) { return 4; }
        else { return -1; } //not an official match
    }

    /*
     * Returns the name of the map used in the match represented by prettyFile.
     * Note: This function was originally in MapManager.java, but was moved to
     * MatchManager.java because it is involves a single match.
     */
    public static String getMapName(File prettyFile)
    {
        JSONObject match_start = JSONManager.returnObject(prettyFile, "LogMatchStart");
        return match_start.get("mapName").toString();
    }
}


//--------
    //public static void printMatchInfo(File prettyFile)
    //{
        //String match_id = getMatchID(prettyFile);
        //String player_perspective = getPlayerPerspective(match_id);
        //String match_type = getMatchType(match_id);
        //String teamSizeForOfficialMatch = getTeamSizeForOfficialMatch(match_id); //what about for arcade?
        //String match_info_summary = match_type +"-" + teamSizeForOfficialMatch + "-" + player_perspective;
        ////System.out.println(match_info_summary); //still need to "history" this
        //try {
        //    FileManager.writeToFileAndConsole(match_info_summary);
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}
    //}
    //Added 9/18