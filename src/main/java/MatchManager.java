import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

/*
 * The MatchManager class reports data regarding a single match.
 * For example, match id, type of match, or what weapons the winners used.
 */
public class MatchManager {
    public static void printMatchInfo(File prettyFile)
    {
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
    }
    //Added 9/18
    //Uses JSONObjects  to return the match id of a prettified file.
    public static String getMatchID(File prettyFile)
    {
        JSONObject match_definition = JSONManager.returnObject(prettyFile, "LogMatchDefinition");
        String match_id = match_definition.get("MatchId").toString();
        //System.out.println("match_id = " + match_id);
        return match_id;
    }

    /*
     * Returns the player perspective (fpp or tpp) for the given match_id.
     *      fpp = first player perspective
     *      tpp = third player perspective
     */
    public static String getPlayerPerspective(String match_id)
    {
        if(match_id.contains("fpp"))
        {
            return "fpp";
        }
        return "tpp";
    }

    /*
     * Returns the "type" of match for the given match_id.
     *      arcade = two teams, maximum of 8v8, minimum start of 4v4
     *      official = 25-100 teams, can be individual, duo, or squad/one-man-squad (maximum team size of 4)
     */
    public static String getMatchType(String match_id)
    {
        if(match_id.contains("arcade"))
        {
            return "arcade";
        }
        return "official";
    }

    //Returns the specific team size for an official match (solo = 1, duo = 2, squad can be [1,4]).
    public static String getTeamSizeForOfficialMatch(String match_id)
    {
        //what if match_id for arcade match is entered?


        if(match_id.contains("duo"))
        {
            return "duo";
        }
        else if(match_id.contains("solo"))
        {
            return "solo";
        }
        else
        {
            return "squad"; //is this accurate?
        }
    }

    //Stores and prints what weapons were used by a specific group (winnersOnly or everyone), and in what frequencies.
    //Currently only works for winnersOnly
    //Made private (check other methods-- see if they need this as well)
    private static void weaponFrequencies(Vector<String> weaponSlot, boolean winnersOnly, String weaponSlotName) throws IOException {
        //System.out.println(weaponSlotName.toUpperCase() + ": (winners only)");
        FileManager.writeToFileAndConsole(weaponSlotName.toUpperCase() + ": (winners only)");
        Vector<String> alreadyListed = new Vector<String>();
        for(int i = 0; i < weaponSlot.size(); i++) //One weapon
        {
            //System.out.println("Weapons already listed: " + alreadyListed.toString());
            int count = 0; //how many times this weapon was used (EX: 2x could be in two slots by same person or 1x by 2people)
            String weapon = weaponSlot.get(i);

            boolean inAlreadyListed = false;
            for(int index = 0; index < alreadyListed.size(); index++)
            {
                if(alreadyListed.get(index).equalsIgnoreCase(weapon))
                {
                    inAlreadyListed = true;
                }
            }
            if (!inAlreadyListed) { //already listed does not seem like it is getting updated properly...
                alreadyListed.add(weapon);
                count = 0; //Check this
                for (int j = i; j < weaponSlot.size(); j++) //Compare against all the other weapons
                {
                    String other_weapon = weaponSlot.get(j);
                    if (weaponSlot.get(i).equalsIgnoreCase(weaponSlot.get(j))) {
                        count++;
                    }

                }
                FileManager.writeToFileAndConsole("\t" + weapon + " x" + count);
                //System.out.println("\t" + weapon + " x" + count);
            }
        }
        FileManager.writeToFileAndConsole(""); //Essentially "System.out.println() visually", but for both
        //System.out.println();
    }

    //Determines (and prints) what weapons were used by the players who won the match described in the given file.
    public static void winnerWeapons(File prettyFile) throws IOException {
        Vector<String> winnerSecondary = new Vector<String>(); //stores names of winners' match-end secondary weapons
        Vector<String> winnerPrimary = new Vector<String>(); //stores names of winners' match-end primary weapons

        //Gather "LogMatchEnd" data and store in jsonObject
        JSONObject jsonObject = JSONManager.returnObject(prettyFile, "LogMatchEnd");
        if(jsonObject == null)
        {
            System.out.println("Error");
            return;
        }

        //Look at each player (WINNER) and store their weapons data
        JSONArray players = jsonObject.getJSONArray("characters");
        for(int i = 0; i < players.length(); i++)
        {
            JSONObject one_player = players.getJSONObject(i);
            JSONObject player_details = one_player.getJSONObject("character");

            //Weapons
            String secondaryWeapon = one_player.get("secondaryWeapon").toString();
            String primaryWFirst = one_player.get("primaryWeaponFirst").toString();
            String primaryWSecond = one_player.get("primaryWeaponSecond").toString();

            //Ranking
            String ranking = player_details.get("ranking").toString();

            //Account ID and team ID
            String accountId = player_details.get("accountId").toString();
            String team_id = player_details.get("teamId").toString();
            int team_id_int = Integer.parseInt(team_id);

            //Add winners' match-end weapons to the appropriate vector.
            //Note: ranking.equals("1") included because was initially trying to store data for winners AND everyone
            if(ranking.equals("1") && team_id_int < 200) //Only include real people, not bots or guards
            {
                System.out.println("accountId: " + accountId);
                if(secondaryWeapon.equalsIgnoreCase(""))
                {
                    secondaryWeapon = "N/A";
                }
                if(primaryWFirst.equalsIgnoreCase(""))
                {
                    primaryWFirst = "N/A";
                }
                if(primaryWSecond.equalsIgnoreCase(""))
                {
                    primaryWSecond = "N/A";
                }

                winnerSecondary.add(secondaryWeapon);
                winnerPrimary.add(primaryWFirst);
                winnerPrimary.add(primaryWSecond);

                //OR: print one player at a time
                FileManager.writeToFileAndConsole(player_details.get("name") + " Summary (Winner): ");
                FileManager.writeToFileAndConsole("\tPrimary Weapon #1: \t" + primaryWFirst);
                FileManager.writeToFileAndConsole("\tPrimary Weapon #2: \t" + primaryWSecond);
                FileManager.writeToFileAndConsole("\tSecondary Weapon: \t" + secondaryWeapon);
                FileManager.writeToFileAndConsole("-------------------------------------------------");
            }
        }

        //System.out.println("PRINTING WEAPON FREQUENCIES: \n" );
        FileManager.writeToFileAndConsole("PRINTING WEAPON FREQUENCIES: \n" );
        weaponFrequencies(winnerPrimary, true, "primary weapons");
        weaponFrequencies(winnerSecondary, true, "secondary weapon");
    }

    /*
     * Given a file representing data on a specific match, prints all players, sorted by team.
     *
     * NOTES:
     * -Does not printPlayersByTeam for custom games.
     * -For deathmatches, does not account for people leaving and entering midgame
     * -Team size not always 4 (or even <=4!) Example of size>4: team deathmatches.
     *
     * TEAM IDs:
     *  official game:      <100 means real people
     *                      20_ (like 201) means bots
     *                      50_ (like 501) means guards
     *                      100000+ --> custom game, maybe?
     *
     *  arcade/deathmatch:  only two teams. IDs are 1 and 2.
     */
    //TO-DO: Adjust printouts so println and history align better.
    public static Vector<JSONObject> printPlayersByTeam(File prettyFile) //used to be called singleString...
    {
        System.out.println("NEW FILE_______________________________________________");
        Vector<JSONObject> peopleByTeam = new Vector<JSONObject>(); //Holds every participant, from lowest to highest team_ids

        JSONArray playersList = new JSONArray();

        //Store data from LogMatchStart in match_start JSONObject
        JSONObject match_start = JSONManager.returnObject(prettyFile, "LogMatchStart");
        int team_capacity = match_start.getInt("teamSize");

        //Check if the game is custom
        boolean is_custom_game = match_start.getBoolean("isCustomGame");
        if(is_custom_game)
        {
            System.out.println("Sorry, we don't compute data for custom games");
            System.exit(0);
        }

        //Use 425 or 500 or something else? 150?
        //Establish maximum number of teams*
        int max_num_teams = 500; //because bots start at 200 and guards start at 400 -->could have sorted them that way, too (identifying type)
        if(team_capacity > 4) //Only deathmatch is then an option currently, since custom games are not handled
        {
            System.out.println("Not a normal battle royale (EX: could be deathmatch with teams up of up to 8");
            max_num_teams = 2;
        }
        System.out.println("Maximum team capacity: " + team_capacity);


        //Determine size of peopleByTeam
        int maxIndices = max_num_teams * team_capacity; //maybe adjust this depending on what the type of game is (singles, duos, squads, flexible squads)
        peopleByTeam.setSize(maxIndices);

        //Store data from LogMatchEnd in jsonObject
        JSONObject jsonObject = JSONManager.returnObject(prettyFile, "LogMatchEnd");

        //Holds info for every player
        //if(!jsonObject.has("characters"))
        //{
        ////   System.out.println("JSONArray 'characters' not found, so can't complete the task");
        //    return null; //carefull
        //}
        playersList = jsonObject.getJSONArray("characters"); //attempt to fix

        //Set up allTeams by storing each player in an index that makes sense for their team_id
        for (int j = 0; j < playersList.length(); j++) {

            //Get access to one player and their details (such as team_id)
            JSONObject player = playersList.getJSONObject(j);
            //if(!player.has("character"))
            //{
            //    System.out.println("ERROR: player.getJSONObject(\"character\") == null");
            //    return null; //careful
            //}
            JSONObject character = player.getJSONObject("character");
            String team_id = character.get("teamId").toString();
            int team_id_index = Integer.parseInt(team_id);

            //Special case
            if(team_id_index >= 100000)
            {
                team_id_index = (team_id_index % 100000) + 1; //EX: weirdness with file6 (cause = ?)
            }

            //Determine at what index to store this player in peopleByTeam
            int insert = (team_id_index * team_capacity); //replaced 4 with team_capacity (save space/time)

            //Put player in peopleByTeam according to their team_id
            for (int loc = 0; loc < team_capacity; loc++) {
                if((insert + loc) >= peopleByTeam.size()) //Should not occur
                {
                    System.out.println("Can't add to index: " + (insert + loc) + "because peopleByTeam.size() is " + peopleByTeam.size());
                    return null; //careful
                }
                if (peopleByTeam.get(insert + loc) != null) { //Other team member holds that spot.
                    loc++;
                } else {
                    peopleByTeam.set(insert + loc, character); //Adding player here
                    loc = team_capacity; //Don't want to add the multiple times (leave room for other people!)
                }
            }
        }
        //Print name, teamId, and ranking of all players (from lowest to highest team id).
        for (int in = 0; in < peopleByTeam.size(); in++) {

            if (in % team_capacity == 0 && peopleByTeam.get(in) != null) {
                System.out.println("-----"); //For display clarity. Separates teams.
            }
            if (peopleByTeam.get(in) != null) { //change the order? (EX: teamId, name, ranking) //add type of match? (EX: fpp duo)
                System.out.print(peopleByTeam.get(in).get("name").toString());
                System.out.print(" " + peopleByTeam.get(in).get("teamId").toString());
                System.out.println(" " + peopleByTeam.get(in).get("ranking").toString());
            }
        }

        return peopleByTeam; //adding this so that ranking method can be more "independent"
        /*
        System.out.println("RANKING SEARCH: ");
        ranking("JS1936", peopleByTeam);
        ranking("matt112", peopleByTeam);
        ranking("SlipperyKoala", peopleByTeam); //should be 1 (at least once)
         */
    }

    //ALSO EXISTS IN MAP MANAGER:
    public static String getMapName(File prettyFile)
    {
        JSONObject match_start = JSONManager.returnObject(prettyFile, "LogMatchStart");
        String mapName = match_start.get("mapName").toString();
        //System.out.println("mapName: " + mapName);
        return mapName;
    }
}
