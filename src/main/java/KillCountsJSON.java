import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Vector;

public class KillCountsJSON {

    //Can print more details
    //maybe print these to a file, instead
    //What if: individual means PRINT ALL, then individual; team--> print all, then SPECIFICALLY team, etc.?
    public static void printKillCountsJSON(Vector<Vector<String>> namesByNumKills) {
        System.out.println("\n\n\nLOOK: printingKillCountsRequest SCOPE = " + Main.requestCurrent.getScopes()[Main.requestCurrent.getRequest_scope()]); //remove later

        for (int index = 0; index < namesByNumKills.size(); index++) {
            Vector<String> names = namesByNumKills.get(index);
            if (!names.isEmpty()) {
                System.out.println(index + " KILLS: ");
                //FileUtils.writeStringToFile();
                try {
                    FileUtils.writeStringToFile(Main.requestHistory, "\n" + index + " KILLS: ", (Charset) null, true); //added 9/15
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (int indexOfNames = 0; indexOfNames < names.size(); indexOfNames++) {
                    System.out.println("\t" + names.get(indexOfNames));
                    try {
                        FileUtils.writeStringToFile(Main.requestHistory, "\n\t" + names.get(indexOfNames), (Charset) null, true); //added 9/15
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //if(indexOfNames % 3 == 0)
                    //{
                    //    System.out.println();
                    //}
                }
                System.out.println("---------------");
                try {
                    FileUtils.writeStringToFile(Main.requestHistory, "\n---------------", (Charset) null, true); //added 9/15
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        //names of 0 kills go into index 0
        //name of 1 kill go into index 1
        //etc.
    }
    public static void calculateKillCountsJSON(File prettyFile) {
        //Vector<String> killCounts = new Vector<String>();

        Vector<JSONObject> kill_events = JSONManager.returnMultipleObjects(prettyFile, "LogPlayerKillV2"); //winners don't die, though...?
        Vector<Vector<String>> namesByNumKills = new Vector<Vector<String>>();

        int maxKills = 0;

        //Assumes no one will get more than 30 kills (make more efficient later... -> maxKills)
        for (int i = 0; i < 30; i++) {
            Vector<String> names = new Vector<String>();
            namesByNumKills.add(names);
        }
        //characters
        //gameresultonfinished
        //results
        //stats
        //killcount

        //NON-WINNERS
        System.out.println("NOTE: this does not yet include the winners...");
        for (JSONObject kill_event : kill_events) {
            //System.out.println("HELLO! Looking at a kill event");
            JSONObject victimGameResult = kill_event.getJSONObject("victimGameResult");
            int rank = Integer.parseInt(victimGameResult.get("rank").toString());
            ////System.out.println("rank: " + rank);

            JSONObject stats = victimGameResult.getJSONObject("stats");
            int killCount = Integer.parseInt(stats.get("killCount").toString());
            //System.out.println("   killcount: " + killCount);
            JSONObject victim = kill_event.getJSONObject("victim");
            String name = victim.get("name").toString();
            //System.out.println(name + " got " + killCount + " kills");

            //index = number of kills they got
            namesByNumKills.get(killCount).add(name);
            //victim --> name
        }
        printKillCountsJSON(namesByNumKills);

        //Winners
        JSONObject match_end = JSONManager.returnObject(prettyFile, "LogMatchEnd");
        //JSONArray winners = match_end.getJSONArray("characters");
        JSONObject game_result_on_finished = match_end.getJSONObject("gameResultOnFinished");
        JSONArray results = game_result_on_finished.getJSONArray("results");
        System.out.println("Kills by Winners:");
        int killedByAnyWinner = 0;
        for (int i = 0; i < results.length(); i++) {
            //JSONObject winner = winners.getJSONObject(i);
            //System.out.println("winner.toString() : " + winner.toString());
            JSONObject player_result = results.getJSONObject(i);
            System.out.println("i = " + i + "; results: " + results.get(i).toString());
            JSONObject stats = player_result.getJSONObject("stats");
            String num_kills = stats.get("killCount").toString();
            int killCount = Integer.parseInt(num_kills);
            System.out.println("\t" + killCount);
            killedByAnyWinner += killCount;
        }
        System.out.println("#people killed by winning team: " + killedByAnyWinner);
        //or maybe just say "#kills by winning team" (in case of deathmatch)
        //having trouble getting the winners' names... (account ids fine/okay, though)
        //printKillCoutns2(#kills per person, effects?)
        //printKillCounts(killCounts);
    }
}
