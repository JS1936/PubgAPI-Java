import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

//Make additional class for handling/executing the request?


//Helpful sites:
//https://stackoverflow.com/questions/2885173/how-do-i-create-a-file-and-write-to-it
//https://stackoverflow.com/questions/4105795/pretty-print-json-in-java
//https://github.com/Corefinder89/SampleJavaCodes/blob/master/src/Dummy1.java
//https://www.w3schools.com/java/java_files_read.asp

//First, store the file somewhere.
//Second, store information that is likely to be asked for
//That way, if likely info is asked for, it can be foudn quickly. If unlikely info is asked for, it can still be found (albeit more slowly, looking through the files)
//Is that even worth it?

//How do you store stats across games/time?



//What if you had a map of players
//Once you found a player, you added them to the map
//Then, if you encounter that player again, you can just look in the map instead of having to search for their basic information again
//Basic information: player name, id,
//Could store history: weapon usage, teammates, killcount avg, rankings, (numGamesRecorded), etc.


//Current functionalities:
//
//      Using JSONObjects:
//              -returnObject
//              -ranking
//              -winnerWeapons
//              -weaponFrequencies
//
//      Using Scanner:
//              -read from and write to a file
//              -"prettify" a file
//              -countBotsAndPeople
//              -calculateKillCounts
//              -printKillCounts

//Thinking...

//Files (feed in the files?)

//Individual
//Team
//Everyone/Game

//killcounts
//ranking
//weapons
//maps
//countBots

//print
//store

//makePretty
//printKillCountsToHistory
//printKillCounts
//printKillCountsJSON
//calculateKillCountsJSON
//calculateKillCounts
//ranking
//weapon frequencies
//winner weapons
//returnMultipleObjects
// returnObject
// printPlayersByTeam
// countBotsAndPeople
// maps
// printMapNames
// getMapName
// getFile
// pseudomain
// getInfo
// printFunctionalities
// initializeFunctionalities
// main


//is an eof error happening...?
//read the file to string first...
public class Main extends Request { //added "extends Memory" 6/16/2022 //added Request.java 9/15, removed extends Memory

    static File currentFile = null; //added 9/15
    static File requestHistory = null;
    /*
     * Takes the contents of an "ugly" file and makes a new file
     * where that content is stored in a way that looks "pretty".
     */
    //NOTE: if this returned the prettyFile, calls like winnerWeapons could be made from main
    public static File makePretty(File fileName) throws IOException {
        //read in file as string
        String uglyString = FileUtils.readFileToString(fileName);

        //make "pretty" version of the string
        Gson gson = new GsonBuilder().setPrettyPrinting().setLenient().create();
        JsonElement je = JsonParser.parseString(uglyString);
        String prettyJsonString = gson.toJson(je);

        //make a file to put the "pretty" text in, if needed
        //C:\Users\jmast\pubgFilesExtracted
        File theDir = new File("C:\\Users\\jmast\\pubgFilesExtracted\\prettyFiles"); //custom pathname?
        if (!theDir.exists()) {
            theDir.mkdirs();
        }

        File prettyFile = new File("C:\\Users\\jmast\\pubgFilesExtracted\\prettyFiles\\" + fileName.getName());

        //write "pretty" text to new file
        FileUtils.writeStringToFile(prettyFile, prettyJsonString);

        //KEEP THESE:
        //countBotsAndPeople(prettyFile);
        //calculateKillCounts(prettyFile); //seems "done"
        //printPlayersByTeam(prettyFile); //in progress
        //winnerWeapons(prettyFile);
        return prettyFile; //added recently (4/27/2022)
    }

    //find winners first (and log who they are)
    //THEN record stuff?
    //        //compare killcounts with everybody else?
    //        //find num teams
    //        //make a vector of that many teams
    //        //within that vector, hold a vector of ints of numKills made by each person on that team
    //        //only start once game has started...?
    /*
     *
     *  boolean foundWinners = false;
        //it's already in order for you...
        Vector<Player> winners = new Vector<Player>();
        Vector<Integer> winnerKills = new Vector<Integer>();
     */
    //add parameter for scope? (Request scope: individual, team, everyone/match)
    //LOOK!
    public static void printKillCountsToHistory(Vector<String> counts, Request r)
    {

    }
    //string array? (passed in)
    //EX: individual-> 1
    //EX: team-> usually 1-4
    //EX: everyone --> empty (already printing the "everyone" data no matter what, just don't print it again at the end)
    public static void printKillCountsToHistory(Vector<String> counts) throws IOException
    {
        //time of request?
        //FileUtils.writeStringToFile();
        String context = "Printing #kills per person. EX: Die first? Your #kills is printed first. Die last? Your #kills is printed last.";
        //try {
            FileUtils.writeStringToFile(requestHistory, "\n" + context, (Charset) null, true); //added 9/15
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}
        System.out.println("Printing #kills per person. EX: Die first? Your #kills is printed first. Die last? Your #kills is printed last.");// People who die first and printed first. People who die first get their num of kills printed last.");
        int[] frequencies = new int[30]; //Assumed no single individual will get more than 30 kills in a single game //could change this to be start-size? EX: like 100
        int maxKills = 0;
        int killsByTopTen = 0;
        for (int i = 0; i < counts.size(); i++) {
            if (i % 10 == 0) //For display clarity
            {
                System.out.println();
                FileUtils.writeStringToFile(requestHistory, "\n", (Charset) null, true); //added 9/15
            }
            int numKills = Integer.valueOf(counts.get(i));
            if (maxKills < numKills) {
                maxKills = numKills;
            }
            if (counts.size() - 10 <= i) {
                killsByTopTen += numKills;
            }
            frequencies[numKills]++;
            System.out.print(counts.get(i) + " ");
            FileUtils.writeStringToFile(requestHistory, counts.get(i) +" ", (Charset) null, true); //added 9/15
        }

        //Print out how many people got X number of kills
        System.out.println("\nKill Frequencies:");
        FileUtils.writeStringToFile(requestHistory, "\nKill Frequencies:", (Charset) null, true); //added 9/15

        for (int index = 0; index <= maxKills; index++) {
            System.out.println(frequencies[index] + " got " + index + " kills.");
            FileUtils.writeStringToFile(requestHistory, "\n" + frequencies[index] + " got " + index + " kills.", (Charset) null, true); //added 9/15
        }
        System.out.println("MAX #kills by a single person: " + maxKills + " (#people who achieved this: " + frequencies[maxKills] + ")");
        System.out.println("#people killed by 'TOP TEN' : " + killsByTopTen + " of " + counts.size());
        System.out.println("--------------------------------------------------------------------------");
        FileUtils.writeStringToFile(requestHistory, "\nMAX #kills by a single person: " + maxKills + " (#people who achieved this: " + frequencies[maxKills] + ")", (Charset) null, true); //added 9/15
        FileUtils.writeStringToFile(requestHistory, "\n#people killed by 'TOP TEN' : " + killsByTopTen + " of " + counts.size(), (Charset) null, true); //added 9/15
        FileUtils.writeStringToFile(requestHistory, "\n--------------------------------------------------------------------------", (Charset) null, true); //added 9/15
    }

    //IS A MANUAL VERSION: Does not use JSONObjects. Scanner-based.
    public static void printKillCounts(Vector<String> counts) {

        try {
            printKillCountsToHistory(counts); //added 9/15
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Printing #kills per person. EX: Die first? Your #kills is printed first. Die last? Your #kills is printed last.");// People who die first and printed first. People who die first get their num of kills printed last.");
        int[] frequencies = new int[30]; //Assumed no single individual will get more than 30 kills in a single game //could change this to be start-size? EX: like 100
        int maxKills = 0;
        int killsByTopTen = 0;
        for (int i = 0; i < counts.size(); i++) {
            if (i % 10 == 0) //For display clarity
            {
                System.out.println();
            }
            int numKills = Integer.valueOf(counts.get(i));
            if (maxKills < numKills) {
                maxKills = numKills;
            }
            if (counts.size() - 10 <= i) {
                killsByTopTen += numKills;
            }
            frequencies[numKills]++;
            System.out.print(counts.get(i) + " ");
        }

        //Print out how many people got X number of kills
        System.out.println("\nKill Frequencies:");
        for (int index = 0; index <= maxKills; index++) {
            System.out.println(frequencies[index] + " got " + index + " kills.");
        }
        System.out.println("MAX #kills by a single person: " + maxKills + " (#people who achieved this: " + frequencies[maxKills] + ")");
        System.out.println("#people killed by 'TOP TEN' : " + killsByTopTen + " of " + counts.size());
        System.out.println("--------------------------------------------------------------------------");
    }

    //Can print more details
    //maybe print these to a file, instead
    //What if: individual means PRINT ALL, then individual; team--> print all, then SPECIFICALLY team, etc.?
    public static void printKillCountsJSON(Vector<Vector<String>> namesByNumKills)
    {
        System.out.println("\n\n\nLOOK: printingKillCountsRequest SCOPE = " + requestCurrent.getScopes()[requestCurrent.getRequest_scope()]); //remove later

        for(int index = 0; index < namesByNumKills.size(); index++)
        {
            Vector<String> names = namesByNumKills.get(index);
            if(!names.isEmpty())
            {
                System.out.println(index + " KILLS: ");
                //FileUtils.writeStringToFile();
                try {
                    FileUtils.writeStringToFile(requestHistory, "\n" + index + " KILLS: ", (Charset) null, true); //added 9/15
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for(int indexOfNames = 0; indexOfNames < names.size(); indexOfNames++)
                {
                    System.out.println("\t" + names.get(indexOfNames));
                    try {
                        FileUtils.writeStringToFile(requestHistory, "\n\t" + names.get(indexOfNames), (Charset) null, true); //added 9/15
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
                    FileUtils.writeStringToFile(requestHistory, "\n---------------", (Charset) null, true); //added 9/15
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        //names of 0 kills go into index 0
        //name of 1 kill go into index 1
        //etc.
    }

    //"LogPlayerKillV2"
    //victimGameResult
        //rank
        //stats
             //killCount
    //victim
        //name

    //BIG, NOT TESTED MESS
    //NOTE: DOES NOT INCLUDE WINNERS
    //EX: individual VS team VS everyone
    //Default current is EVERYONE.
    //For TEAM, need to know the names looking for.
    //For INDIVIDUAL, need to know the singular name looking for.
    //LOOK!!
    public static void calculateKillCountsJSON(File prettyFile)
    {
        //Vector<String> killCounts = new Vector<String>();

        Vector<JSONObject> kill_events = returnMultipleObjects(prettyFile, "LogPlayerKillV2"); //winners don't die, though...?
        Vector<Vector<String>> namesByNumKills = new Vector<Vector<String>>();

        int maxKills = 0;

        //Assumes no one will get more than 30 kills (make more efficient later... -> maxKills)
        for(int i = 0; i < 30; i++)
        {
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
        for(JSONObject kill_event : kill_events)
        {
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
        JSONObject match_end = returnObject(prettyFile, "LogMatchEnd");
        //JSONArray winners = match_end.getJSONArray("characters");
        JSONObject game_result_on_finished = match_end.getJSONObject("gameResultOnFinished");
        JSONArray results = game_result_on_finished.getJSONArray("results");
        System.out.println("Kills by Winners:");
        int killedByAnyWinner = 0;
        for(int i = 0; i < results.length(); i++) {
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

    //Accidentally removed this! Found it again through github commits history
    //Could re-implement this using jsonobjects (AND also be able to get teams, kills by team)
    //Vector<Integer> killsByTeam = new Vector<Integer>();
    //Vector<Vector<Integer>> teams = new Vector<Vector<Integer>>();
    //IS A MANUAL VERSION: Does not use JSONObjects. Instead, scans line by line.
    public static void calculateKillCounts(File prettyFile)
    {
        Vector<String> killCounts = new Vector<String>();
        try {
            Scanner scan = new Scanner(prettyFile);
            while(scan.hasNextLine())
            {
                String data = scan.nextLine();
                if(data.contains("killCount"))
                {
                    String killNum = data.substring(data.length() -2, data.length() -1);
                    killCounts.add(killNum);
                }
            }
            scan.close();
            printKillCounts(killCounts);

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
//just get the matchID, too
    //Given a name, searches for that person, and if they were in the provided games, gives their ranking(s)
    //COULD use printPlayersByTeam (excess printouts) OR do it independently
    //could take in String[] names instead of String name? (In case of multiple names...?)
    //Returns ranking as a string
    public static String ranking(String name, File prettyFile) {
        JSONObject match_definition = returnObject(prettyFile, "LogMatchDefinition");
        //System.out.println("match definition: " + match_definition);
        String match_id = match_definition.get("MatchId").toString();
        System.out.println("match_id = " + match_id);


        JSONObject match_end = returnObject(prettyFile, "LogMatchEnd");
        //System.out.println("Attempting to print match_end content: " + match_end);
        JSONArray players = match_end.getJSONArray("characters");
        for (int i = 0; i < players.length(); i++) {
            JSONObject player = players.getJSONObject(i);
            System.out.println("Attempting to print match_end content (1line): " + player);
            JSONObject player_details = player.getJSONObject("character");
            String player_name = player_details.get("name").toString();
            System.out.println("\t" + player_name);
            if (player_name.equalsIgnoreCase(name)) {
                String player_ranking = player_details.get("ranking").toString();
                System.out.println(name + "rank in this game: " + player_details.get("ranking").toString());
                //added 9/15-> write to file here?

                //probably want the matchID, too
                //can it be made to automatically open the file, too?
                try {
                    FileUtils.writeStringToFile(currentFile, "\n-" + player_name + ", " + player_ranking + ", " + match_id, (Charset) null, true); //changed requestedResults to currentFile //added 9/15
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return player_details.get("ranking").toString();
            }
        }
        return ""; //was not in the game
    }

    //Stores and prints what weapons were used by a specific group (winnersOnly or everyone)
    //Currently only works for winnersOnly
    //Made private (check other methods-- see if they need this as well)
    private static void weaponFrequencies(Vector<String> weaponSlot,  boolean winnersOnly, String weaponSlotName)
    {
        System.out.println(weaponSlotName.toUpperCase() + ": (winners only)");
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
                System.out.println("\t" + weapon + " x" + count);
            }
        }
        System.out.println();
    }

    //Would getting everyone else's weapons be a different call?
    /*
        Vector<String> everyoneSecondary = new Vector<String>();
        Vector<String> everyonePrimary = new Vector<String>();
     */
    public static void winnerWeapons(File prettyFile)
    {
        Vector<String> winnerSecondary = new Vector<String>(); //stores names of winners' match-end secondary weapons
        Vector<String> winnerPrimary = new Vector<String>(); //stores names of winners' match-end primary weapons

        //Gather "LogMatchEnd" data and store in jsonObject
        JSONObject jsonObject = returnObject(prettyFile, "LogMatchEnd");
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
                System.out.println(player_details.get("name") + " Summary (Winner): ");
                System.out.println("\tPrimary Weapon #1: \t" + primaryWFirst);
                System.out.println("\tPrimary Weapon #2: \t" + primaryWSecond);
                System.out.println("\tSecondary Weapon: \t" + secondaryWeapon);
                System.out.println("-------------------------------------------------");
            }
        }

        System.out.println("PRINTING WEAPON FREQUENCIES: \n" );
        weaponFrequencies(winnerPrimary, true, "primary weapons");
        weaponFrequencies(winnerSecondary, true, "secondary weapon");
    }

    //VERY similar to returnObject, but can return multiple occurrences of a type via vector (instead of limited to 1)
    public static Vector<JSONObject> returnMultipleObjects(File prettyFile, String type)
    {
        Vector<JSONObject> multipleObjects = new Vector<JSONObject>();
        System.out.println("NEW FILE_______________________________________________");
        //Store contents of prettyFile in a String called file_content
        String file_content = "";
        try {
            file_content = FileUtils.readFileToString(prettyFile);
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Return portion of file that matches given String type
        System.out.println(type);
        JSONArray jsonArray = new JSONArray(file_content);
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String type_T = jsonObject.getString("_T");

            if (type_T.equalsIgnoreCase(type)) {
                //System.out.println(type);
                multipleObjects.add(jsonObject);
                //return jsonObject;

            }
        }
        //return null; //What if _T type is not found?
        return multipleObjects;
    }
    //WHAT IF you could search through a prettyfile for any _T and that method would return the contents?
    //Could also keep track of items equipped
    //Note: What if "String type" occurs multiple times (EX: item equip sort of thing)
    public static JSONObject returnObject(File prettyFile, String type)
    {
        System.out.println("NEW FILE_______________________________________________");
        //Store contents of prettyFile in a String called file_content
        String file_content = "";
        try {
            file_content = FileUtils.readFileToString(prettyFile);
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //if(file_content.length() == 0)
        //{
        //    System.out.println("Empty file...");
        //}
        //Return portion of file that matches given String type
        JSONArray jsonArray = new JSONArray(file_content);
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String type_T = jsonObject.getString("_T");

            if (type_T.equalsIgnoreCase(type)) {
                System.out.println(type);
                return jsonObject;

            }
        }
        return null; //What if _T type is not found?
    }


    /* ////////////////???
    C:\Users\jmast\pubgFilesExtracted\telemetryFile6.json
NEW FILE_______________________________________________
NEW FILE_______________________________________________
LogMatchStart
Maximum team capacity: 4
NEW FILE_______________________________________________
LogMatchEnd
val of team_id_index: 100000 for account.09126421272d4bbfac0dda6625d953b5
val of insert: 400000
peopleByTeam so far:
Can't add to index: 400000because peopleByTeam.size() is 2000
     */
    //NOTE: For deathmatches, does not account for people leaving and entering midgame... (issue here?)
    //team id: <100 means real people
    //         20_ (like 201) means bots
    //         50_ (like 501) means guards
    //         100000+ --> custom game, maybe?
    //if only team ids ar 1 and 2 --> deathmatch
    //NOTE: Team size not always 4 (or even <=4!)
    public static Vector<JSONObject> printPlayersByTeam(File prettyFile) //used to be called singleString...
    {
        System.out.println("NEW FILE_______________________________________________");
        Vector<JSONObject> peopleByTeam = new Vector<JSONObject>(); //Holds every participant, from lowest to highest team_ids

        JSONArray playersList = new JSONArray();

        //Store data from LogMatchStart in match_start JSONObject
        JSONObject match_start = returnObject(prettyFile, "LogMatchStart");
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
        JSONObject jsonObject = returnObject(prettyFile, "LogMatchEnd");

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
            if (peopleByTeam.get(in) != null) {
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

    /*
     * Reads from a file to determine how many "people" in a pubg match were actually bots.
     * The file includes many, many details about the match.
     * IS A MANUAL VERSION: Does not use JSONObjects. Instead, scans line by line.
     * NOTE: does not count guards as bots.
     */
    public static void countBotsAndPeople(File prettyFile) { //make private?
        //System.out.println("HELLO! HERE! HELLO! IN COUNTBOTSANDPEOPLE");
        try {
            Scanner scan = new Scanner(prettyFile);
            Vector<String> playerNames = new Vector<String>(); //account.
            Vector<String> botNames = new Vector<String>(); //ai.

            boolean gameHasStarted = false;

            while (scan.hasNextLine()) {
                String data = scan.nextLine();

                //Only start counting bots and people IF the game has started (people can enter and leave beforehand)
                if (data.contains("numStartPlayers")) {
                    gameHasStarted = true;
                }

                //Account found, game has started
                if (data.contains("accountId") && gameHasStarted) {
                    if (data.contains("account.")) //real person
                    {
                        //Store the account_id
                        int accountStart = data.indexOf("account.");
                        String account_id = data.substring(accountStart);

                        //Store it "pretty"/nicely
                        if (account_id.contains(",")) {
                            account_id = account_id.substring(0, account_id.length() - 1);
                        }

                        //If new player, add to list of players (playerNames)
                        if (!playerNames.contains(account_id)) //new player
                        {
                            playerNames.add(account_id);
                        }
                    } else //bot (or guard, potentially)
                    {
                        if (data.contains("ai.")) //bot
                        {
                            //Store account_id
                            int accountStart = data.indexOf("ai.");
                            String account_id = data.substring(accountStart, accountStart + 6);

                            //If new bot, add to list of bots (botNames).
                            if (!botNames.contains(account_id)) //new bot
                            {
                                botNames.add(account_id);
                            }
                        }
                    }
                }

            }
            System.out.println("#bots:       " + botNames.size() + " / " + playerNames.size());

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    //Prints the names of maps played and how many times they were played.
    //EX: "<mapName> x5" means <mapName> was played 5 times.
    public static void printMapNames() //don't need this parameter
    {
        Collections.sort(mapsPlayed); //import java.util.Collections

        System.out.println("Frequencies of each map played: "); //added "played" 9/15
        int frequency = 1;
        int i = 0;
        while(i < mapsPlayed.size()) {
            if(i + 1 == mapsPlayed.size())
            {
                System.out.println(mapsPlayed.get(i) + " x" + frequency);
                return;
            }
            for (int j = i + 1; j < mapsPlayed.size(); j++)
            {
                if (mapsPlayed.get(i).equalsIgnoreCase(mapsPlayed.get(j))) {
                    //System.out.println("val at index " + i + " = j");
                    frequency++;
                } else {
                    System.out.println(mapsPlayed.get(i) + " x" + frequency);
                    frequency = 1;
                }
                i++;
            }
        }
    }

    //Given a prettified file holding data on a pubg match, returns the name of the map played on in the match.
    public static String getMapName(File prettyFile)
    {
        JSONObject match_start = returnObject(prettyFile, "LogMatchStart");
        String mapName = match_start.get("mapName").toString();
        //System.out.println("mapName: " + mapName);
        return mapName;
    }

    //ADDED 9/15/2022 for file creation in psuedomain for storing data (EX: request 4)
    //Returns a file called fileName.
    //If this file does not yet exist, attempts to create a new file called fileName and return it.
    public static File getFile(String fileName)
    {
        File file = new File(fileName);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
    //For each file, there is a call to the corresponding request
    //VERY IN PROGRESS
    public static void psuedoMain(Scanner input) //removed "String desiredThing"
    {
        File[] files = new File("C:\\Users\\jmast\\pubgFilesExtracted").listFiles();
        //what if history of requests?
        ///requestHistory = getFile("C:\\Users\\jmast\\pubg_requestHistory");
        ///currentFile = getFile("C:\\Users\\jmast\\sampleFile"); //added 9/15
        //File requestedResults = getFile("C:\\Users\\jmast\\sampleFile"); //added 9/15
        //FileUtils.writeStringToFile(currentFile, "\n-" + name, (Charset) null, true);
        mapsPlayed.clear(); //avoid duplicates...
        //printFunctionalities();
        input = new Scanner(System.in);
        //printOptionsToChooseFrom(functionalities, "What would you like to know?");

        int request = getRequestType(input); //(requestType)
        int requestScope = getRequestScope(input);
        //Request r = new Request(request, requestScope);
        requestCurrent = new Request(request, requestScope);
        //int request = getRequest(input); //string or int? (Getting confused)


        //Added the try/catch writeStringToFile for requestHistory 9/15
        try {
            //could even have a log-in system where differentiating user histories
            FileUtils.writeStringToFile(requestHistory, "request=" + request + "_requestScope=" + requestScope + "_", (Charset) null, true); //changed requestedResults to currentFile
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(request == -1) //remove?
        {
            System.out.println("Invalid request");
            return;
        }
        String name = "";
        if(request == 4)
        {
            System.out.println("Who are you looking for? (EX: matt112)");
            name = input.next();
            System.out.println("Now looking for: '" + name + "'");

        }

        int max_files = 10; //temporary (remove later)
        int filesSoFar = 0;
        for (File fileName : files) {
            //if(fileName.getName() != "prettyFiles")
            //{
            System.out.println(fileName);

            try {
                //makePretty(fileName); //error here
                //System.out.println("LOOK HERE!");
                if(filesSoFar >= 10)
                {
                    //System.out.println("Reached max_files of " + max_files + "...terminating program");
                } else
                {
                    File pretty = makePretty(fileName);
                    getInfo(request, pretty, name);
                    filesSoFar++;
                    //FileUtils.writeStringToFile(currentFile, name, (Charset) null, true); //changed requestedResults to currentFile
                //writeStringToFile(requestedResults, name); //added 9/15
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(request == 6) //trying this...
        {
            System.out.println("REQUEST = 6");
            System.out.println("mapsPlayed.size() : " + mapsPlayed.size());
            printMapNames(); //this would hold repeats (but it also calculates the amt of repetitions)
            //printMapNameFrequencies(mapsPlayed); //no longer needed
        }

        String response = "y";
        System.out.println("Any other requests? (y/n)");
        response = input.next();

        if(response.equalsIgnoreCase("Y"))
        {
            psuedoMain(input); //Caution: this causes a problem with having multiple scanners open...
        }
        //
        //input.close(); //put here?
        System.out.println("Shutting down program."); //change wording...

        //Call the appropriate method(s) based on user input
    }


    //What if there were a container of "things you could access" about any given game (or all games?)
    //Like a user interface, where they are presented with options and type which ones they want

    //switch stmt?
    //what about for ONE specific file, or  for specific fileS?
    //factory-like abstraction thing here? (instead)
    //abstract this further?
    //Is there a way to make this just thing.callRequestType() and it calls the right one?
    //EX: class called countBotsAndPeople
    //doing a method call but not (string from vector).call() --> actually call the method
    public static boolean getInfo(int request, File prettyFile, String nameIfNeeded) //changed return from void -> boolean 5/17/2022
    {
        //Could also have request be a string... (to try to avoid the nextInt(), nextLine(), etc. issue (and verifying if actually int)
        //EX: request.equalsIgnoreCase("0")
        //If doing separate task-objects (EX: kill counts), could "create" them here in an array, call via the ifs)
        if(request == 0)
        {
            countBotsAndPeople(prettyFile); //seems to work
            //could this be called somehow using vector (like something[0]) to make this more "adjustable"?
        }
        else if(request == 1)
        {
            calculateKillCounts(prettyFile); //seems to work

        }else if(request == 2) {

            printPlayersByTeam(prettyFile); //seems to work (but quite messy)

        }else if(request == 3) {

            winnerWeapons(prettyFile); //seems to work

        }else if(request == 4) { //NOT WORKING (issue with scanner) -->
            // NOW switched... but still lots of extra printouts, and passing name in seems silly
            ///TRY WRITING TO FILE HERE
            ranking(nameIfNeeded, prettyFile); //seems to work (ALMOST --> getting null errors) //INTERESTING: new request, asks for name with every file...

        }else if(request == 5) {
            //NOT WORKING
            calculateKillCountsJSON(prettyFile);
            //having trouble accessing names of the winners specifically

        }else if(request == 6) {
            //getMaps
            //Vector<String> mapNames = new Vector<String>(); //this is getting re-made with every file...
            if(prettyFile == null)
            {
                System.out.println("FILE IS NULL");
            }
            String name = getMapName(prettyFile);
            if(name != null) //Tried to fix EOF exception with this but it didn't work (which makes sense, I guess)
            {
                //mapNames.add(getMapName(prettyFile));
               // System.out.println("Attempting to add: " + name + " to mapsPlayed...");
                mapsPlayed.add(name);
            }

        }else
        {
            System.out.println("Invalid request"); //for example's sake (currently)
            return (request < 0 || request >= 7); //invalid requests should not have valid numbers
        }
        return (request < 7 && request > -1); //valid requests should have valid numbers
    }


    public static void printOptionsToChooseFrom(Vector<String> options, String prompt)
    {
        System.out.print(prompt);
        System.out.println(" Type the corresponding number and then press enter.\n");
        for(int i = 0; i < options.size(); i++)
        {
            System.out.println(i + ": " + options.get(i));
        }
    }


    //IN PROGRESS
    public static void initiateFunctionalities()
    {
        functionalities.add("countBotsAndPeople");
        functionalities.add("calculateKillCounts");
        functionalities.add("printPlayersByTeam");
        functionalities.add("winnerWeapons");
        functionalities.add("ranking (of a specific person)");
        functionalities.add("calculateKillCountsJSON");
        functionalities.add("printMapsPlayed");
    }

    public static void initiateRequestScopes()
    {
        requestScopes.add("individual (EX: matt112)");
        requestScopes.add("team       (EX: team of matt112)");
        requestScopes.add("match      (all individuals in that match)");
    }

    //If requests are objects, then this is easier...?
    //abstract these better...!
    //write to file (requestHistory) here? 9/15
    public static int getInput(Scanner input, Vector<String> optionsToChooseFrom)
    {
        int request = -1; //not yet a valid request
        boolean requestAccepted = false;

        while(!requestAccepted)
        {
            request = Integer.parseInt(input.next()); //careful...
            System.out.println("request is: " + request);
            if(request >= 0 && request < optionsToChooseFrom.size())
            {
                requestAccepted = true; //technically not needed, but helps with clarity
                System.out.println("Request accepted!");
                return request;
            }
            System.out.println("Sorry, '" + request + "' does not match any available options. Try again.");
        }
        return request;
    }

    public static int getRequestType(Scanner input)
    {
        String prompt_requestType = "What would you like to know?";
        printOptionsToChooseFrom(functionalities, prompt_requestType);
        int requestType = getInput(input, functionalities);
        return requestType;
    }

    //I want data about a particular player (EX: matt112).
    //I want data about a particular team (EX: team of matt112)
    //I want data about a particular match (everyone in that match).
    ///WHAT ABOUT WANTING INFO ON MULTIPLE PEOPLE WHO ARE NOT ON THE SAME TEAM?
    ///int request = Integer.parseInt(input.next());
    //print the request scopes
    //could make prompt index 0? (or last, I suppose)
    public static int getRequestScope(Scanner input)
    {
        String prompt_requestScope = "WHO are we learning about?";
        printOptionsToChooseFrom(requestScopes, prompt_requestScope);
        int requestScope = getInput(input, requestScopes);
        //String[] requestScopeOptions = {"individual (EX: matt112)", "team (EX: team of matt112)", "match (everyone in that match)"};
        return requestScope;

    }


    //"Do you want to store this information in its own file?" idea
    //Could just automatically store it, then only keep at the end if user says to keep it (at the end...) --> both more and less work
    //IN PROGRESS
    public static Vector<String> mapsPlayed = new Vector<String>();
    public static Vector<String> functionalities = new Vector<String>(); //call it options instead ("functionalities" could be like the method calls) //not public?
    public static Vector<String> requestScopes = new Vector<String>(); //added 9/17
    //public static Request dummyRequest;// = new Request(0,0);
    public static Request requestCurrent;// = new Request(0,0);

    public static void main(String[] args) //maybe put the "while" in here to having multiple requests actually works?
    {
        //String[] types = dummyRequest.getTypes();
        Vector<String> winnersRecorded; //winners across different games
        mapsPlayed.clear(); //clear at the beginning

        initiateFunctionalities();
        initiateRequestScopes(); //added 9/17

        requestHistory = getFile("C:\\Users\\jmast\\pubg_requestHistory");
        currentFile = getFile("C:\\Users\\jmast\\sampleFile"); //added 9/15

        Scanner input = new Scanner(System.in);
        psuedoMain(input);
        input.close();
    }
}

//record maps used
//for each file, get the map
//have a vector existing outside of this that holds the map names
//print names (and frequency)



//Note: option to keep history/request or not?
//what if it could make a separate file for the individual request, then link this file in the history file?
//EX: "destination" component (console only VS file only VS both)
//what if it has already calculated something before? (Redo or try to track, store?)
//how do you edit a file's contents?
//could different request types go in different .cpp files entirely? (Likely more readable that way)
//EX: record number of kills in a single game for matt112 --> over multiple games... 1, 3, 2, 6, 6, 7, 3, --> record should be 7, show only 7 (+ maybe match id)
//could do file log with testcases passing/not passing records (Gradle...)


//Only handles one request at a time, right?



//"Removal buffer" (in case wanting it later):

//mapNames.add(mapName);
//mapsPlayed.add(mapName);
//LogMatchStart
//mapName

//IN PROGRESS //make this more efficient...
    /*
    public static Vector<String> printFunctionalities() { //changed from void to Vector<String>

        Vector<String> outputVerify = new Vector<String>();
        System.out.println("What would you like to know? Type the corresponding number and then press enter.\n");
        for(int i = 0; i < functionalities.size(); i++)
        {
            System.out.println(i + ": " + functionalities.get(i));
            outputVerify.add(i + ": " + functionalities.get(i));
        }
        return outputVerify;
    }
    */

  /*
    public static int getRequest(Scanner input) //more like validation?
    {
        //Scanner input = new Scanner(System.in);
        int request = Integer.parseInt(input.next()); //careful...
        System.out.println("request is: " + request);
        //write to file (requestHistory) here? 9/15

        boolean requestAccepted = false;
        if(request >= 0 && request < functionalities.size())
        {
            requestAccepted = true;
        }
        if(!requestAccepted)
        {
            System.out.println("Sorry, '" + request + "' does not match any available functionalities.");
            return -1; //invalid request
        }
        else
        {
            System.out.println("Request accepted!");

            return request;
            //Do it...
        }
    }
     */


    /*
    public static void printMapNameFrequencies(Vector<String> mapNames)
    {
        Map<String, Integer> mapOfMaps = new HashMap<>();
        //for each map, if not key yet, add key
        //increment qty of appearance either way
        for(String mapName : mapNames)
        {
            if(mapOfMaps.containsKey(mapName))
            {
                int frequency = mapOfMaps.get(mapName) + 1;
                mapOfMaps.put(mapName, frequency); //should replace previous value (same key)
            }
            else
            {
                System.out.println(mapName + "is a NEW MAP!");
                mapOfMaps.put(mapName, 1);
            }
        }
        //for each key in mapOfMaps, print out value (a.k.a. frequency)
        mapOfMaps.keySet();

    }
    */


//printOptionsToChooseFrom(requestScopes);
//Player p = new Player("15511");
//System.out.println("Account ID: "+ p.getAccountID());

///Map<Integer, String> m = new HashMap<>();
///m.put(1, "hi");

//print options for what you can do
//ask them what they want to do
//do it (if possible), otherwise error message
//continue running? (y/n)

//A: "HMBAP" -- HowManyBotsAndPeople?
//B: "WWDTWU" -- WhatWeaponsDidTheWinnersUse?
//"I want all the maps played and how often they were played"
///File[] files = new File("C:\\Users\\jmast\\pubgFilesExtracted").listFiles();
//what if history of requests?


//IN PROGRESSS
//public static void getMethods()
//{
//        Vector<String> methods = new Vector<String>();
//        methods.add("countBotsAndPeople");
//}


////EVERYTHING BELOW THIS POINT IS A MESS CURRENTLY

//Hopefully: Only for battle royale* (max 4 people per team)
//Also, deathmatches probably don't have bots (would custom games?)
////Trying to get data that goes beyond just a single game
    /*
    public static void maps(File prettyFile, Vector<String> theMapsPlayed) //added "the"... (because mapsPlayed  static was getting messed up)
    {
        JSONObject jsonObject = returnObject(prettyFile, "LogMatchStart");
        String mapName = jsonObject.getString("mapName");
        int max_team_size = jsonObject.getInt("teamSize");
        if(max_team_size <= 4)
        {
            //System.out.println("ATTEMPTING TO ADD " + mapName + " to mapsPlayed");
            mapsPlayed.add(mapName);
        }
        else
        {
            System.out.println("NOT adding " + mapName + " because team size > 4");
        }

        //logmatchstart
        //mapname
        //could use team size (EX: only look at the ones that are actually "classic" battle royale games, not custom or deathmatch)
        //EX: desiredThing could be mapnames
    }
    */

//10 maps:
//Desert
//Heaven
//Summerland
//Tiger
//Baltic
//Summerland
//Baltic
//Tiger
//Desert
//Baltic

//Frequencies:              CLAIMS
//Desert:       2           1
//Heaven:       1           0
//Summerland:   2           1
//Tiger:        2           doesn't even show up
//Baltic:       3           3
//-------------------
//             =10

