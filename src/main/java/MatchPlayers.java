import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class MatchPlayers {

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
        System.out.println("NEW FILE_______________________________________________");
        Vector<JSONObject> peopleByTeam = new Vector<JSONObject>(); //Holds every participant, from lowest to highest team_ids

        JSONArray playersList = new JSONArray();

        //Store data from LogMatchStart in match_start JSONObject
        JSONObject match_start = JSONManager.returnObject(prettyFile, "LogMatchStart");

        //int team_capacity = match_start.getInt("teamSize"); //1/5/2023 remove
        //Added 1/5/2023:
        String match_id = MatchManager.getMatchID(prettyFile);
        int team_capacity = MatchManager.getMaximumTeamSizeForOfficialMatch(match_id);
        System.out.println("Team capacity = " + team_capacity);

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
        playersList = jsonObject.getJSONArray("characters"); //attempt to fix

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
                //System.out.println("CURR: " + peopleByTeam.get(insert + loc));
                //System.out.println("ADD?: " + character.get("name"));
                if (peopleByTeam.get(insert + loc) != null) { //Some team member holds that spot.
                    if(peopleByTeam.get(insert + loc).get("name") == character.get("name")) //Self holds that spot
                    {
                        System.out.println("Already listed " + character + " as part of the team. Don't want to list duplicately");
                        //j++;
                        loc = team_capacity;
                    }
                    else //Other team member holds that spot
                    {
                        //loc++;
                    }
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
    
}
