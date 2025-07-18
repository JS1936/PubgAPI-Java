import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

/*
 * The MatchWeapons class gathers and reports weapons-related match statistics.
 */
public class MatchWeapons {
    /*
     * Prints the weapons used by the match winners to terminal and requestHistory.txt
     * Note: With some modification, this function could be generalized to apply to any team (search by team id).
     */
    private static void weaponFrequencies(Vector<String> weaponSlot, boolean winnersOnly, String weaponSlotName) throws IOException {
        FileManager.writeToFileAndConsole(weaponSlotName.toUpperCase() + ": (winners only)");
        Vector<String> alreadyListed = new Vector<String>();
        for(int i = 0; i < weaponSlot.size(); i++) //One weapon
        {
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
            }
        }
        FileManager.writeToFileAndConsole(""); //Essentially "System.out.println() visually", but for both
    }

    /*
     * Determines (and prints) what weapons were used by the players who won the match described in the given file.
     * Note: Dead winners have N/A on all weapons. Although technically possible for a living player to have N/A on all
     *       weapons, it is very unlikely.
     */
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
                if(secondaryWeapon.equalsIgnoreCase(""))    {   secondaryWeapon = "N/A";    }
                if(primaryWFirst.equalsIgnoreCase(""))      {   primaryWFirst = "N/A";      }
                if(primaryWSecond.equalsIgnoreCase(""))     {   primaryWSecond = "N/A";     }

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

    
}
