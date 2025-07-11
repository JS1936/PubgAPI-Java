import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class MatchPlayers {


    // public static Vector<JSONObject> getPlayersByTeam(File prettyFile)
    // {
    //     exitIfGameIsCustom(prettyFile);

    //     Vector<JSONObject> peopleByTeam = new Vector<JSONObject>(); //Holds every participant, from lowest to highest team_ids
    //     JSONArray playersList = new JSONArray();

    //     String match_id = MatchManager.getMatchID(prettyFile);
    //     int team_capacity = MatchManager.getMaximumTeamSizeForOfficialMatch(match_id);

    //     //Establish maximum number of teams
    //     int max_num_teams = 600; //>100 (+ buffer), otherwise arbitrary. Caution: 2xx bot teams
    //     if(team_capacity > 4) //Only deathmatch is then an option currently, since custom games are not handled
    //     {
    //         System.out.println("Not a normal battle royale (EX: could be deathmatch with teams up of up to 8");
    //         max_num_teams = 2;
    //     }
    //     System.out.println("Maximum team capacity: " + team_capacity);

    //     //Determine size of peopleByTeam
    //     int maxIndices = max_num_teams * team_capacity; //maybe adjust this depending on what the type of game is (singles, duos, squads, flexible squads)
    //     peopleByTeam.setSize(maxIndices);

    //     //Store data from LogMatchEnd in jsonObject
    //     JSONObject jsonObject = JSONManager.returnObject(prettyFile, "LogMatchEnd");
    //     playersList = jsonObject.getJSONArray("characters"); //attempt to fix

    //     peopleByTeam = addPlayers(prettyFile, peopleByTeam, playersList, team_capacity);
    //     return peopleByTeam;
    // }

    public static Vector<JSONObject> addPlayers(File prettyFile, Vector<JSONObject> peopleByTeam, JSONArray playersList, int team_capacity)
    {
        //Set up allTeams by storing each player in an index that makes sense for their team_id
        for (int j = 0; j < playersList.length(); j++) {

            //Get access to one player and their details (such as team_id)
            JSONObject player = playersList.getJSONObject(j);
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
                if (peopleByTeam.get(insert + loc) != null) { //Some team member holds that spot.
                    if(peopleByTeam.get(insert + loc).get("name") == character.get("name")) //Self holds that spot
                    {
                        System.out.println("Already listed " + character + " as part of the team. Don't want to list duplicately");
                        loc = team_capacity;
                    }
                } else {
                    peopleByTeam.set(insert + loc, character); //Adding player here
                    loc = team_capacity; //Don't want to add the multiple times (leave room for other people!)
                }
            }
        }
        return peopleByTeam;
    }

    private static void exitIfGameIsCustom(File prettyFile)
    {
        JSONObject match_start = JSONManager.returnObject(prettyFile, "LogMatchStart");
        boolean is_custom_game = match_start.getBoolean("isCustomGame");
        if(is_custom_game)
        {
            System.out.println("Sorry, we don't compute data for custom games");
            System.exit(0);
        }
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
     *                      2xx (like 201) or 3xx means bots
     *                      5xx (like 501) means guards
     *                      100000+ --> custom game, maybe?
     *
     *  arcade/deathmatch:  only two teams. IDs are 1 and 2.
     */
    //TO-DO: Adjust printouts so println and history align better.
    public static Vector<JSONObject> printPlayersByTeam(File prettyFile)
    {
        Vector<JSONObject> peopleByTeam = new Vector<JSONObject>(); //Holds every participant, from lowest to highest team_ids
        JSONArray playersList = new JSONArray();

        exitIfGameIsCustom(prettyFile);

        String match_id = MatchManager.getMatchID(prettyFile);
        int team_capacity = MatchManager.getMaximumTeamSizeForOfficialMatch(match_id);

        //Establish maximum number of teams
        int max_num_teams = 600; //>100 (+ buffer), otherwise arbitrary. Caution: 2xx bot teams
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
        playersList = jsonObject.getJSONArray("characters"); //attempt to fix

        peopleByTeam = addPlayers(prettyFile, peopleByTeam, playersList, team_capacity);
       
        actuallyPrintTheTeam(team_capacity, peopleByTeam);
        return peopleByTeam; //adding this so that ranking method can be more "independent"
    }

    
    //Print name, teamId, and ranking of all players (from lowest to highest team id).
    public static void actuallyPrintTheTeam(int team_capacity, Vector<JSONObject> peopleByTeam)
    {
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
    }
    //public static Vector<JSONObject> addPlayerToTeam(Vector<Object> peopleByTeam)
    //{
    //    return peopleByTeam;
    //}
}



// //Store data from LogMatchStart in match_start JSONObject
// JSONObject match_start = JSONManager.returnObject(prettyFile, "LogMatchStart");
        
// //Check if the game is custom
// boolean is_custom_game = match_start.getBoolean("isCustomGame");
// if(is_custom_game)
// {
//     System.out.println("Sorry, we don't compute data for custom games");
//     System.exit(0);
// }