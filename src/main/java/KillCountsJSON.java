import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

//The KillCountsJSON class uses JSONObjects to calculate and print data on kills from a match.
//TODO: Decide whether to print full calculations to file (not just console)
public class KillCountsJSON {

    /*
     * Prints (to console and requestHistory) the names of all players in order of how many kills they got in the match.
     * Console includes minor additional printouts.
     */
    public static void printKillCountsJSON(Vector<Vector<String>> namesByNumKills) throws IOException {
        for (int index = 0; index < namesByNumKills.size(); index++) {
            Vector<String> names = namesByNumKills.get(index);
            if (!names.isEmpty()) {
                FileManager.writeToFileAndConsole("\n" + index + " KILLS: ");
                for (int indexOfNames = 0; indexOfNames < names.size(); indexOfNames++) {
                    FileManager.writeToFileAndConsole("\t" + names.get(indexOfNames));
                }
                FileManager.writeToFileAndConsole("\n---------------");
            }
        }
    }

    /*
     * Calculates how many kills each player got in the match described by the given file.
     * Calls printKillCountsJSON.
     *
     * Note: At least one player in the winning team will survive. Thus, the tracking of kills made by the winning team
     * is separate from that for kills made by players not on the winning team. Winning team kills are only written
     * to console, not to history.
     */
    public static void calculateKillCountsJSON(File prettyFile) {

        Vector<JSONObject> kill_events = JSONManager.returnMultipleObjects(prettyFile, "LogPlayerKillV2");
        Vector<Vector<String>> namesByNumKills = new Vector<Vector<String>>();
        Vector<String> namesList = new Vector<>(); //temp
        //temporary:
        //Set<String> namesList = new HashSet<String>();

        //Assumes no one will get more than 30 kills (make more efficient later... -> int maxKills)
        for (int i = 0; i < 30; i++) {
            Vector<String> names = new Vector<String>();
            namesByNumKills.add(names);
        }

        //NON-WINNERS. Kill events.
        System.out.println("NOTE: this does not yet include the winners...");
        int countKillEvents = 0; //temp
        //temp:
        for(int kill_event_index = kill_events.size() - 1; kill_event_index >= 0; kill_event_index--)
        {
            JSONObject kill_event = kill_events.get(kill_event_index);
        //}
        //for (JSONObject kill_event : kill_events) {

            JSONObject victimGameResult = kill_event.getJSONObject("victimGameResult");
            //int rank = Integer.parseInt(victimGameResult.get("rank").toString());
            ////System.out.println("rank: " + rank);

            JSONObject stats = victimGameResult.getJSONObject("stats");
            int killCount = Integer.parseInt(stats.get("killCount").toString());
            //System.out.println("   killcount: " + killCount);
            JSONObject victim = kill_event.getJSONObject("victim");
            String name = victim.get("name").toString();
            //System.out.println(name + " got " + killCount + " kills"); //comment out

            //index = number of kills they got
            //TEMP:
            //if(killCount > 0)
            //{
            //    boolean removed = namesByNumKills.get(killCount - 1).remove(name); //temporary. Trying this to try to get rid of duplicate / progression. 12/22/2022.
            //    if(removed)
            //    {
            //        System.out.println("REMOVED");
            //        countKillEvents--;
            //    }
            //}
            //namesByNumKills.remove(name); //temporary. Trying this to try to get rid of duplicate / progression. 12/22/2022.
            if(!namesList.contains(name))
            {
                namesByNumKills.get(killCount).add(name);
                namesList.add(name);
                countKillEvents++; //temp
            }
            else
            {
                System.out.println("Already have: " + name.toString());
            }
            //victim --> name
        }
        try {
            printKillCountsJSON(namesByNumKills);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Winners
        JSONObject match_end = JSONManager.returnObject(prettyFile, "LogMatchEnd");
        //JSONArray winners = match_end.getJSONArray("characters");
        JSONObject game_result_on_finished = match_end.getJSONObject("gameResultOnFinished");
        JSONArray results = game_result_on_finished.getJSONArray("results");
        //temp:
        System.out.println("countKillEvents pre-winners: " + countKillEvents);
        System.out.println("Kills by Winners:");
        int killedByAnyWinner = 0;
        for (int i = 0; i < results.length(); i++) {
            //JSONObject winner = winners.getJSONObject(i);
            //System.out.println("winner.toString() : " + winner.toString());
            JSONObject player_result = results.getJSONObject(i);
            //System.out.println("i = " + i + "; results: " + results.get(i).toString());
            JSONObject stats = player_result.getJSONObject("stats");
            String num_kills = stats.get("killCount").toString();
            //temp:
            //countKillEvents++;
            //

            int killCount = Integer.parseInt(num_kills);
            System.out.println("\t" + killCount);
            killedByAnyWinner += killCount;
        }
        System.out.println("#people killed by winning team: " + killedByAnyWinner);
        System.out.println("adjusted #kill events: " + countKillEvents); //temp
        //having trouble getting the winners' names... (account ids fine/okay, though)
    }
}

//Notes-buffer (will be removed soon):
//
//  Can print more details
//  maybe print these to a file, instead
//  What if: individual means PRINT ALL, then individual; team--> print all, then SPECIFICALLY team, etc.?
//  names of 0 kills go into index 0
//  name of 1 kill go into index 1
//  etc.