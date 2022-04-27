import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.*;


//Helpful sites:
//https://stackoverflow.com/questions/2885173/how-do-i-create-a-file-and-write-to-it
//https://stackoverflow.com/questions/4105795/pretty-print-json-in-java
//https://github.com/Corefinder89/SampleJavaCodes/blob/master/src/Dummy1.java
//https://www.w3schools.com/java/java_files_read.asp

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


//is an eof error happening...?
//read the file to string first...
public class Main {

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
        File theDir = new File("C:\\Users\\jmast\\pubgFilesExtracted\\prettyFiles");
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
    //IS A MANUAL VERSION: Does not use JSONObjects. Scanner-based.
    public static void printKillCounts(Vector<String> counts)
    {
        System.out.println("Printing #kills per person. EX: Die first? Your #kills is printed first. Die last? Your #kills is printed last.");// People who die first and printed first. People who die first get their num of kills printed last.");
        int[] frequencies = new int[30]; //Assumed no single individual will get more than 30 kills in a single game
        int maxKills = 0;
        int  killsByTopTen = 0;
        for(int i =0; i < counts.size(); i++)
        {
            if(i%10 ==0) //For display clarity
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

    //Given a name, searches for that person, and if they were in the provided games, gives their ranking(s)
    public static void ranking(String name, File prettyFile)
    {
        Vector<JSONObject> peopleByTeam = printPlayersByTeam(prettyFile); //misleading method call? //ALSO: people or players?
        //System.out.println("RANKING SEARCH: ");
        //ranking("JS1936", peopleByTeam);
        //ranking("matt112", peopleByTeam);
        //ranking("SlipperyKoala", peopleByTeam); //should be 1 (at least once)
        boolean playedInGame = false;
        for(JSONObject person : peopleByTeam)
        {
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
                    System.exit(0);
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

    ////EVERYTHING BELOW THIS POINT IS A MESS CURRENTLY

    //Hopefully: Only for battle royale* (max 4 people per team)
    //Also, deathmatches probably don't have bots (would custom games?)
    ////Trying to get data that goes beyond just a single game
    public static void maps(File prettyFile, Vector<String> mapsPlayed)
    {
        JSONObject jsonObject = returnObject(prettyFile, "LogMatchStart");
        String mapName = jsonObject.getString("mapName");
        int max_team_size = jsonObject.getInt("teamSize");
        if(max_team_size <= 4)
        {
            mapsPlayed.add(mapName);
        }

        //logmatchstart
        //mapname
        //could use team size (EX: only look at the ones that are actually "classic" battle royale games, not custom or deathmatch)
        //EX: desiredThing could be mapnames
    }
    /*
    public static void getInfo(String desiredThing)
    {
        Vector<String> mapsPlayed = new Vector<String>();
    }
     */

    //VERY IN PROGRESS
    public static void psuedoMain(String desiredThing)
    {
        //https://stackoverflow.com/questions/160970/how-do-i-invoke-a-java-method-when-given-the-method-name-as-a-string
        //https://www.tutorialspoint.com/How-do-I-invoke-a-Java-method-when-given-the-method-name-as-a-string
        // Class<?> c = Class.forName("class name");
        //Method method = c.getDeclaredMethod("method name", parameterTypes);
        //method.invoke(objectToInvokeOn, params);
        //
        //Vector<String>
                //Could make a vector of methods, if you find a match, call that one
        //OR just be less efficient and call individually with lots of ifs
        /*
        for(Method method : Main.class.getMethods())//getMethods(); //https://stackoverflow.com/questions/3023354/how-to-get-string-name-of-a-method-in-java
        {
            System.out.println("HERE!");
            String name = method.getName();
            if(name.equalsIgnoreCase(desiredThing))
            {
                TypeVariable<Method>[] parameter_types = method.getTypeParameters(); //?
                try {
                    System.out.println("Trying...");
                    method.invoke(null, parameter_types);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("NAME of method : " + name);

        }

        //Check if it's a command, too, though


        //Method countBotsAndPeople = Operations.class.getDeclaredMethod("somethinghere", File.class);

        //Class c = null;
        //try {
        //    c = Class.forName("Main.java");
         //   System.out.println("FOUND IT!");
        ////    System.out.println("Class '" + c + "' not found");
         //   e.printStackTrace();
        //    return;
        //}
        //Method method_name = this.getDeclaredMethod("methodName", parameterTypes);
        //Is there a way to check if the files (content or qty) have changed since last time?
        */

        File[] files = new File("C:\\Users\\jmast\\pubgFilesExtracted").listFiles();
        printFunctionalities();
        Scanner input = new Scanner(System.in);
        int request = getRequest(input); //string or int? (Getting confused)
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


        for (File fileName : files) {
            //if(fileName.getName() != "prettyFiles")
            //{
            System.out.println(fileName);
            try {
                //makePretty(fileName); //error here
                //System.out.println("LOOK HERE!");
                File pretty = makePretty(fileName);
                getInfo(request, pretty, name);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        input.close(); //put here?
        //Call the appropriate method(s) based on user input
    }

    //What if there were a container of "things you could access" about any given game (or all games?)
    //Like a user interface, where they are presented with options and type which ones they want

    //switch stmt?
    //what about for ONE specific file, or  for specific fileS?
    public static void getInfo(int request, File prettyFile, String nameIfNeeded)
    {
        //
        if(request == 0)
        {
            countBotsAndPeople(prettyFile); //seems to work
        }
        else if(request == 1)
        {
            calculateKillCounts(prettyFile); //seems to work

        }else if(request == 2) {

            printPlayersByTeam(prettyFile); //seems to work (but quite messy)

        }else if(request == 3) {

            winnerWeapons(prettyFile); //seems to work

        }else if(request == 4) { //NOT WORKING (issue with scanner)

            ranking(nameIfNeeded, prettyFile); //seems to work //INTERESTING: new request, asks for name with every file...

        }else
        {
            System.out.println("Invalid request"); //for example's sake (currently)
        }

        //if(option1)
        //{
           //call option1 method
        //}
    }
    //IN PROGRESSS
    public static void getMethods()
    {
            Vector<String> methods = new Vector<String>();
            methods.add("countBotsAndPeople");
    }

    //IN PROGRESS //make this more efficient...
    public static void printFunctionalities() {

        System.out.println("What would you like to know? Type the corresponding letter and then press enter.");
        System.out.println();
        System.out.println("0: countBotsAndPeople");
        System.out.println("1: calculateKillCounts");
        System.out.println("2: printPlayersByTeam");
        System.out.println("3: winnerWeapons");
        System.out.println("4: ranking (of a specific person)");
        //pass in and for loop instead?

    }

    //IN PROGRESS
    public static int getRequest(Scanner input)
    {
        //Scanner input = new Scanner(System.in);
        int request = Integer.parseInt(input.next()); //careful...
        System.out.println("request is: " + request);
        //String nameToLookfor = "";
        //if(request == 4)
        //{
        //    nameToLookfor = input.nextLine();
        //}
        //input.close();
        //System.out.println("LOOK: " + input.next());


        Vector<String> functionalities = new Vector<String>(); //call it options instead ("functionalities" could be like the method calls)
        functionalities.add("countBotsAndPeople");
        functionalities.add("calculateKillCounts");
        functionalities.add("printPlayersByTeam");
        functionalities.add("winnerWeapons");
        functionalities.add("ranking (of a specific person)");

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

    //IN PROGRESS
    public static void main(String[] args)
    {

        //print options for what you can do
        //ask them what they want to do
        //do it (if possible), otherwise error message
        //continue running? (y/n)

        //A: "HMBAP" -- HowManyBotsAndPeople?
        //B: "WWDTWU" -- WhatWeaponsDidTheWinnersUse?
        //"I want all the maps played and how often they were played"

        psuedoMain("desiredThing");
    }
}
