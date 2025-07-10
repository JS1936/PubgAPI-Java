import java.io.File;

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
        return JSONManager.getJSONValue(prettyFile, "LogMatchDefinition", "MatchId");
    }

    /*
     * Returns the player perspective (fpp or tpp) for the given match_id.
     *      fpp = first player perspective
     *      tpp = third player perspective
     */
    public static String getPlayerPerspective(String match_id)
    {
        return match_id.contains("fpp") ? "fpp" : "tpp";
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
    public static boolean isOfficialMatch(File prettyFile)
    {
        String match_id = getMatchID(prettyFile);
        return (match_id.contains("official"));
    }

    /*
     * Returns the specific team size for an official match 
     * (solo = 1, duo = [1,2], squad = [1,4]). Returns -1 if unofficial match.
     */
    public static int getMaximumTeamSizeForOfficialMatch(String match_id)
    {
        if(match_id.contains("solo")) {  return 1; }
        else if(match_id.contains("duo")) {  return 2; }
        else if(match_id.contains("squad")) { return 4; }
        else { return -1; } //not an official match
    }

    /*
     * Returns the match's map name.
     */
    public static String getMapName(File prettyFile)
    {
        return JSONManager.getJSONValue(prettyFile, "LogMatchStart", "mapName");
    }
}