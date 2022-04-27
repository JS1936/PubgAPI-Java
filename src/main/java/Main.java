import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.*;
import java.util.*;


//Helpful sites:
//https://stackoverflow.com/questions/2885173/how-do-i-create-a-file-and-write-to-it
//https://stackoverflow.com/questions/4105795/pretty-print-json-in-java
//https://github.com/Corefinder89/SampleJavaCodes/blob/master/src/Dummy1.java
//https://www.w3schools.com/java/java_files_read.asp


//is an eof error happening...?
//read the file to string first...

//could clear out original file
//then for each line in pretty file, write to original file?
public class Main {

    /*
     * Takes the contents of an "ugly" file and makes a new file
     * where that content is stored in a way that looks "pretty".
     */
    //NOTE: if this returned the prettyFile, calls like winnerWeapons could be made from main
    public static void makePretty(File fileName) throws IOException {
        String originalName = fileName.getName();
        String originalPath = fileName.getAbsolutePath();
        //System.out.println("original name: " + originalName);
        //System.out.println("original path: " + originalPath);

        //read in file as string
        String uglyString = FileUtils.readFileToString(fileName);

        //System.out.println("pretty string: " + prettyString);
        //make "pretty" version of the string
        Gson gson = new GsonBuilder().setPrettyPrinting().setLenient().create();
        JsonElement je = JsonParser.parseString(uglyString);
        String prettyJsonString = gson.toJson(je);

        //make a file to put the "pretty" text in, if needed
        //C:\Users\jmast\pubgFilesExtracted
        File theDir = new File("C:\\Users\\jmast\\pubgFilesExtracted\\prettyFiles");
        if (!theDir.exists()) {
            theDir.mkdirs();
        }

        File prettyFile = new File("C:\\Users\\jmast\\pubgFilesExtracted\\prettyFiles\\" + originalName);
        //prettyJsonString //LOOK //fix this

        //write "pretty" text to new file
        FileUtils.writeStringToFile(prettyFile, prettyJsonString);


        //countBotsAndPeople(prettyFile);
        //calculateKillCounts(prettyFile); //seems "done"
        singleStringOfFile(prettyFile); //in progress
        //winnerWeapons(prettyFile);


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
    public static void printKillCounts(Vector<String> counts)
    {
        System.out.println("Printing #kills per person. EX: Die first? Your #kills is printed first. Die last? Your #kills is printed last.");// People who die first and printed first. People who die first get their num of kills printed last.");
        int[] frequencies = new int[30];
        int maxKills = 0;
        int  killsByTopTen = 0;
        for(int i =0; i < counts.size(); i++)
        {
            if(i%10 ==0)
            {
                System.out.println();
            }
            int numKills = Integer.valueOf(counts.get(i));
            if(maxKills < numKills)
            {
                maxKills = numKills;
            }
            if(counts.size() - 10 <= i)
            {
                killsByTopTen += numKills;
            }
            frequencies[numKills]++;
            System.out.print(counts.get(i) + " ");
        }

        //Print out how many people got X number of kills
        System.out.println("\nKill Frequencies:");
        for(int index = 0; index <= maxKills; index++)
        {
            System.out.println(frequencies[index] + " got " + index + " kills." );
        }
        System.out.println("MAX #kills by a single person: " + maxKills + " (#people who achieved this: " + frequencies[maxKills] + ")");
        System.out.println("#people killed by 'TOP TEN' : " + killsByTopTen + " of " + counts.size());
        System.out.println("--------------------------------------------------------------------------");
    }

    //Accidentally removed this! Found it again through github commits history
    public static void calculateKillCounts(File prettyFile)
    {
        //Vector<Integer> killsByTeam = new Vector<Integer>();
        //Vector<Vector<Integer>> teams = new Vector<Vector<Integer>>();

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


    //WHAT IF IT WAS JUST ONE VECTOR
    //Since data was gathered at the beginning of the game, everyone has rank 0 so far --> need the pretty file?
    //_T, logmatchend, l
    //Given a name, searches for that person, and if they were in the provided games, gives their ranking(s)
    public static void ranking(String name, Vector<JSONObject> peopleByTeam)
    {
        boolean playedInGame = false;
        for(JSONObject person : peopleByTeam)
        {
            //System.out.println("HERE");
            if(person != null && person.get("name").equals(name))
            {
                playedInGame = true;
                System.out.println(name + "'s ranking in this game was: " + person.get("ranking"));
            }
        }
        if(!playedInGame)
        {
            System.out.println(name + " wasn't in that game");
        }
    }

    //Where would this be called from?
    //secondaryWeapon
    //character
        //includes ranking
    //primaryWeaponFirst
    //primaryWeaponSecond

    //THAT CANNOT BE RIGHT:
    //"Players (including winners)93/95 ended with (None)(primaryWeaponFirst) in this match."
    public static void weaponFrequencies(Vector<String> weaponSlot,  boolean winnersOnly, String weaponSlotName)
    {
        System.out.println(weaponSlotName.toUpperCase() + ": (winners only)");
        Vector<String> alreadyListed = new Vector<String>();
        for(int i = 0; i < weaponSlot.size(); i++) //One weapon
        {
            //System.out.println("Weapons already listed: " + alreadyListed.toString());
            int count = 0;
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
                count = 0;
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

    //PRINTING WEAPON FREQUENCIES:
    //Winners Only: 1/6 ended with WeapMosinNagant_C(primaryWeaponFirst) in this match. (WHAT?)
    //ALSO: what about if slot is empty (including in a primary, for instance)
    //Would getting everyone else's weapons be a different call?
    public static void winnerWeapons(File prettyFile)
    {
        Vector<String> winnerSecondary = new Vector<String>();
        Vector<String> winnerPrimary = new Vector<String>();
        //Vector<String> winnerPrimary2 = new Vector<String>(); //remove?

        Vector<String> everyoneSecondary = new Vector<String>();
        Vector<String> everyonePrimary = new Vector<String>();
        //Vector<String> everyonePrimary2 = new Vector<String>(); //remove?


        JSONObject jsonObject = returnObject(prettyFile, "LogMatchEnd");
        if(jsonObject == null)
        {
            System.out.println("Error");
            return;
        }
        //System.out.println("Contents of jsonObject: " + jsonObject.toString());
        JSONArray players = jsonObject.getJSONArray("characters");
        for(int i = 0; i < players.length(); i++)
        {
            JSONObject one_player = players.getJSONObject(i); //object VS json object?
            JSONObject player_details = one_player.getJSONObject("character");

            //Weapons
            String secondaryWeapon = one_player.get("secondaryWeapon").toString();
            String primaryWFirst = one_player.get("primaryWeaponFirst").toString();
            String primaryWSecond = one_player.get("primaryWeaponSecond").toString();

            //Ranking
            //It seems like only the weapons of the winners are registering...
            String ranking = player_details.get("ranking").toString();
            //String name = player_details.get("name").toString();
            String accountId = player_details.get("accountId").toString();
            String team_id = player_details.get("teamId").toString();

            //System.out.println("TEAM ID: " + team_id);
            int team_id_int = Integer.parseInt(team_id);
            if(ranking.equals("1") && team_id_int < 200)
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
                System.out.println("Found a winner...");
                winnerSecondary.add(secondaryWeapon);
                winnerPrimary.add(primaryWFirst); //changed from winnerPrimary2.
                winnerPrimary.add(primaryWSecond);

                //OR: print one player at a time
                System.out.println(player_details.get("name") + " Summary (Winner): ");
                System.out.println("\tPrimary Weapon #1: \t" + primaryWFirst);
                System.out.println("\tPrimary Weapon #2: \t" + primaryWSecond);
                System.out.println("\tSecondary Weapon: \t" + secondaryWeapon);
                System.out.println("-------------------------------------------------");
                //considering 1 and 2 essentially the same? //like only considering primary weapons VS secondary... or even just weapons
            }
            //Note: winners get included in "everyone" data
            //System.out.println("Now including general populus..."); //weird
            //everyoneSecondary.add(secondaryWeapon);
            //everyonePrimary1.add(primaryWFirst);
            //everyonePrimary2.add(primaryWSecond);

            //System.out.println(one_player.toString());
            //System.out.println("RANKING IS: " );
            //System.out.println(one_player.get("ranking").toString());
            //if(one_player.get("ranking") == "1")
        }

        //System.out.println("everyonPrimary1 list: " + everyonePrimary1.toString());
        System.out.println("PRINTING WEAPON FREQUENCIES: " );
        System.out.println();
        //weaponFrequencies(everyonePrimary1, false, "primaryWeaponFirst");
        //weaponFrequencies(everyonePrimary2, false, "primaryWeaponSecond");
        //weaponFrequencies(everyoneSecondary, false, "secondaryWeapon");
        weaponFrequencies(winnerPrimary, true, "primary weapons");
        //weaponFrequencies(winnerPrimary2, true, "primaryWeaponSecond");
        weaponFrequencies(winnerSecondary, true, "secondary weapon");


        //  ->(NOT) find "gameResultOnFinished
        //  -> (DO) find "characters" --> beacuse those hold the character wrappers --> which hold weapon details
        //end results
        //character wrapper
        //primaryWeaponFirst
        //primaryWeaponSecond
        //secondaryWeapon
    }
    
    //WHAT IF you could search through a prettyfile for any _T and that method would return the contents?
    //Could also keep track of items equipped
    public static JSONObject returnObject(File prettyFile, String type)
    {
        System.out.println("NEW FILE_______________________________________________");
        String file_content = "";
        try {
            file_content = FileUtils.readFileToString(prettyFile);
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
    public static void singleStringOfFile(File prettyFile) //??? WHAT IS HAPPENING HERE??? //rename this, too
    {
        System.out.println("NEW FILE_______________________________________________");
        Vector<JSONObject> peopleByTeam = new Vector<JSONObject>();

        JSONArray playersList = new JSONArray();

        int bot_count = 0;
        int person_count = 0;
        int highest_team_id = 0;
        int team_count = 0; //still need to calculate this in code

        //ALSO: log match teamSize... -> so not always 4

        JSONObject match_start = returnObject(prettyFile, "LogMatchStart");
        int team_capacity = match_start.getInt("teamSize");
        boolean is_custom_game = match_start.getBoolean("isCustomGame");
        if(is_custom_game)
        {
            System.out.println("Sorry, we don't compute data for custom games");
            return;
        }

        //Use 425 or 500 or something else? 150?
        int max_num_teams = 500; //because bots start at 200 and guards start at 400 -->could have sorted them that way, too (identifying type)
        //int team_capacity = 4;
        if(team_capacity > 4)
        {
            System.out.println("Not a normal battle royale (EX: could be deathmatch with teams up of up to 8");
            max_num_teams = 2;
        }
        System.out.println("Maximum team capacity: " + team_capacity);


        int maxIndices = max_num_teams * team_capacity; //maybe adjust this depending on what the type of game is (singles, duos, squads, flexible squads)
        peopleByTeam.setSize(maxIndices);

        JSONObject jsonObject = returnObject(prettyFile, "LogMatchEnd");
        if(jsonObject == null)
        {
            System.out.println("jsonObject was null... uh oh");
            return;
        }

        //for (int i = 0; i < jsonArray.length(); i++) {

            //JSONObject jsonObject = jsonArray.getJSONObject(i);
            //String type = jsonObject.getString("_T");
            //if (type.equalsIgnoreCase("LogMatchStart")) {
            //if (type.equalsIgnoreCase("LogMatchEnd")) { //LogMatchEnd includes rankings
            //   System.out.println(type);
                playersList = jsonObject.getJSONArray("characters"); //attempt to fix

                //Set up allTeams
                for (int j = 0; j < playersList.length(); j++) {
                    JSONObject player = playersList.getJSONObject(j);
                    JSONObject character = player.getJSONObject("character"); //problem here
                    String team_id = character.get("teamId").toString();

                    int team_id_index = Integer.parseInt(team_id);
                    if(team_id_index >= 100000)
                    {
                        team_id_index = (team_id_index % 100000) + 1; //EX: weirdness with file6
                    }
                    int insert = (team_id_index * team_capacity); //replaced 4 with team_capacity (save space/time)
                    //if(team_id_index >= 200) //adjusting for bots and guards (which overwhelmed the array size-wise)
                    //{
                    //    insert = team_id_index;
                    //}
                    System.out.println("string of team_id: " + team_id);
                    System.out.println("val of team_id_index: " + team_id_index + " for " + character.get("accountId").toString());
                    System.out.println("val of insert: " + insert);
                    System.out.println("peopleByTeam so far:" );
                    for(int x = 0; x < peopleByTeam.size(); x++)
                    {
                        if(peopleByTeam.get(x) == null)
                        {
                            //System.out.println("null");
                        }
                        else
                        {
                            System.out.print(peopleByTeam.get(x).get("name") + " ");
                        }

                    }
                    for (int loc = 0; loc < team_capacity; loc++) {
                        if((insert + loc) >= peopleByTeam.size())
                        {
                            System.out.println("Can't add to index: " + (insert + loc) + "because peopleByTeam.size() is " + peopleByTeam.size());
                            System.exit(0);
                        }
                        if (peopleByTeam.get(insert + loc) != null) { //added insert + loc < peopleByTeam.size() trying to avoid "Array index out of range: " issue
                            loc++;
                        } else {
                            peopleByTeam.set(insert + loc, character);
                            loc = team_capacity;
                        }
                    }
                    for (int in = 0; in < peopleByTeam.size(); in++) {

                        if (in % team_capacity == 0 && peopleByTeam.get(in) != null) {
                            System.out.println("-----");
                        }
                        if (peopleByTeam.get(in) != null) {
                            System.out.print(peopleByTeam.get(in).get("name").toString());
                            System.out.print(" " + peopleByTeam.get(in).get("teamId").toString());
                            System.out.println(" " + peopleByTeam.get(in).get("ranking").toString());
                            //peopleByTeam.s

                        }
                    }
                }
            //}
        //}
        System.out.println("RANKING SEARCH: ");
        ranking("JS1936", peopleByTeam); //Is this working?
        ranking("matt112", peopleByTeam);
        ranking("SlipperyKoala", peopleByTeam); //should be 1 (at least once)
    }
    /*
     * Reads from a file to determine how many "people" in a pubg match were actually bots.
     * The file includes many, many details about the match.
     */
    public static void countBotsAndPeople(File prettyFile) {
        try {
            Scanner scan = new Scanner(prettyFile);
            Vector<String> playerNames = new Vector<String>();
            Vector<String> botNames = new Vector<String>(); //ai
            String totalPlayers = ""; //why is this a string...?

            boolean gameHasStarted = false;

            while (scan.hasNextLine()) {
                String data = scan.nextLine();
                //System.out.println(data);

                if (data.contains("numStartPlayers")) {
                    //System.out.println(data);
                    totalPlayers = data;
                    gameHasStarted = true;
                }

                if (data.contains("accountId") && gameHasStarted) {
                    if (data.contains("account.")) //real person
                    {
                        int accountStart = data.indexOf("account.");
                        String account_id = data.substring(accountStart); //could remove this...
                        if (account_id.contains(",")) {
                            account_id = account_id.substring(0, account_id.length() - 1);
                        }
                        if (!playerNames.contains(account_id)) //new player to add to the list
                        {
                            playerNames.add(account_id);
                            //System.out.println(account_id); //doubles...
                        }
                    } else //bot
                    {

                        if (data.contains("ai.")) //important that gameHasStarted (which it has, if it reaches here)
                        {
                            int accountStart = data.indexOf("ai.");
                            //System.out.println(accountStart);
                            String account_id = data.substring(accountStart, accountStart + 6); ///? Why are some ""? (-1)
                            //System.out.println(account_id);
                            //System.out.println("\nCurrent botNames list:");
                            for (int i = 0; i < botNames.size(); i++) {
                                //System.out.print("" + botNames.get(i) + " ");
                            }
                            //System.out.println();
                            if (!botNames.contains(account_id)) //new bot to add to the list
                            {
                                botNames.add(account_id);
                                //System.out.println("adding: " + account_id); //repeats...
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

    public static void main(String[] args) {
        File[] files = new File("C:\\Users\\jmast\\pubgFilesExtracted").listFiles();

        for (File fileName : files) {
            //if(fileName.getName() != "prettyFiles")
            //{
            System.out.println(fileName);
            try {
                makePretty(fileName); //error here
            } catch (IOException e) {
                e.printStackTrace();
            }
            //}

        }
    }
}

/*

                //if(!foundWinners)
                //{
                //    if(data.contains("results"))
                //    {
                //        foundWinners = true;
               //     }
                    //go to the next line
               // }
                //else //gameHasEnded = true
                //{
                //    String currentID = "";
                    /*
                    if(data.contains("\"ranking\": 1,")) //, to exclude 10, 12, 100, etc.
                    {
                        System.out.println(data);
                        if (scan.hasNextLine()) {
                            String nameID = scan.nextLine();
                            boolean newWinner = true;
                            for (int i = 0; i < winners.size(); i++) {
                                if (winners.get(i).accountID == nameID) {
                                    newWinner = false;
                                }
                            }
                            if (newWinner) {
                                Player winner = new Player(nameID);
                                winners.add(winner);
                            }
                        } else {
                            System.out.println("No more lines to read");
                            scan.close();
                        }
                    }
                    */
//}

