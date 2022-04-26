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

    }

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
    //IT SEEMS LIKE IT'S ADDING PEOPLE TO THE TEAMS BUT NOT SAVING THE CONTENTS OF THE TEAMS...
    //EX: matt112 and JS1936 each the "first" added to team#14
    public static void singleStringOfFile(File prettyFile) //??? WHAT IS HAPPENING HERE???
    {
        System.out.println("NEW FILE_______________________________________________");
        Vector<JSONObject> allPlayers = new Vector<JSONObject>();
        Vector<JSONArray> allTeams = new Vector<JSONArray>();

        //TEAM 1 --> person, person, person, person (EX)

        //team# ==> find
        //assign to that slot in array (put(i,obj))
        //Or what if you just added teams in whatever order they appeared, and then searched each time to see if they existed...?
        //Vector<String> player_info = new Vector<String>();

        int bot_count = 0;
        int person_count = 0;
        int highest_team_id = 0;

        String file_content = "";
        try {
            file_content = FileUtils.readFileToString(prettyFile);
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //get each character
        //use characterwrapper to get weapon data, too...
        JSONArray jsonArray = new JSONArray(file_content);
        //Map string/object?
        for (int i = 0; i < jsonArray.length(); i++) {

            //System.out.println("i = " + i + "; object is: " + jsonArray.getJSONObject(i));

            //Find where the match starts, THEN start counting players/bots
            //Maybe counts of bots and team allocations can be done here, too


            //WHAT IS HAPPENING
                //jsonArray holds file contents
                //each jsonObject is an object (from the file)
                    //Some objects describe participants in the game (like real people and bots)
                    //These get put into the JSONArray playersList
                    //Which gets transferred to Vector<JSONArray> allPlayers for some reason?
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String type = jsonObject.getString("_T");
            // System.out.println("TYPE: " + type);
            if (type.equalsIgnoreCase("LogMatchStart")) {
                System.out.println(type);
                JSONArray playersList = jsonObject.getJSONArray("characters"); //oh, goes out of scope, hence allPlayers being used (fix?)

                for (int j = 0; j < playersList.length(); j++) {
                    if (allPlayers.isEmpty()) {
                        System.out.println("allPlayers is empty");
                    }
                    JSONObject player = playersList.getJSONObject(j);
                    System.out.println("PLAYER: " + player.toString());

                    if (player.has("character")) {
                        //System.out.println("Does list of all players already have this player?");
                        System.out.println(" --> includes character");
                        if (allPlayers.contains(player)) {
                            System.out.println("allPlayers list : already has that player");
                        }
                        if (!allPlayers.contains(player)) {
                            System.out.println("ADDING PLAYER: " + player);
                            allPlayers.add(player);
                            /*
                            JSONObject character = player.getJSONObject("character");
                            System.out.println("Character: " + character.toString());
                            String teamNum = character.get("teamId").toString();
                            int teamNumInt = Integer.parseInt(teamNum);
                            System.out.println("team num int: " + teamNumInt);
                            if(allTeams.get(teamNumInt) == null)
                            {
                                //allTeams.get(teamNumInt) = new JSONArray();
                                JSONArray team = new JSONArray(4);
                                team.put(0, character);
                                allTeams.add(team);
                                System.out.println("team.length() : " + team.length());

                            }
                           //tring teamNum = player.get("teamId");
                            //String teamNum = (player.get("character")).
                            //allTeams.add();
                            //add to team at the same time?
                            //add

                             */

                        }
                    } else {
                        System.out.println(" --> does NOT include character");
                    }
                }

            }

            //System.out.println(playersList);

            //System.exit(0);
        }
        System.out.println("end of playerslist printing");

        System.out.println("allplayers.size() : " + allPlayers.size());

        System.out.println("PRINTING allPlayers:" );
        for (int index = 0; index < allPlayers.size(); index++) {
            //System.out.println(allPlayers.get(i).getString("character"));
            System.out.println(allPlayers.get(index).toString());
        }

        System.out.println("PRINTING accountID of each player: ");
        for (int index = 0; index < allPlayers.size(); index++) {

            //System.out.println(allPlayers.get(i).getString("character"));
            JSONObject one_player = allPlayers.get(index);
            //System.out.println("LOOK: " + one_player.has("character"));
            //System.out.println("get character: " + one_player.get("character"));
            //System.out.println("LOOK2:" + one_player.get("character").toString());
            JSONObject character = one_player.getJSONObject("character"); //problem here
            //System.out.println("HERE");
            //System.out.println("LOOK3: " + character.toString());
            String id = character.get("accountId").toString();
            String team_id = character.get("teamId").toString();
            String name = character.get("name").toString();
            System.out.println(name + " has id : " + id + " and is in team # : " + team_id);

            int team_id_index = Integer.parseInt(team_id);
            System.out.println(team_id_index + " VS current highest of " + highest_team_id);
            if(team_id_index >= highest_team_id) {
                System.out.println(team_id_index + " > " + highest_team_id);
                System.out.print("size changed from: " + allTeams.size() + " to ");
                highest_team_id = team_id_index + 1; //+1 so that it can actually hold something AT index of team_id_index
                allTeams.setSize(highest_team_id);
                System.out.println(allTeams.size());
            }
            //if(allTeams.isEmpty())
            //{
            //    System.out.println("allTeams is empty");
            //}
            //System.out.println("allTeams.size() = " + allTeams.size()); //null pointer exception
            //System.out.println("HELLO");
            System.out.println("Checking if allTeams has a team at index: " + team_id_index);
            //System.out.println(allTeams.get(0)); //null
            if(allTeams.get(team_id_index) == null)
            {
                System.out.println("THERE") ;
                System.out.println(name + " is the first to be registered to team# : " + team_id);
                JSONArray team = new JSONArray();
                System.out.println("Trying to add " + name + "to team #" + team_id);
                team.put(one_player); //want to be able to print this as their names, though
                allTeams.add(team_id_index, team);
                System.out.println("Added? Attempting to confirm : " + team.get(0));
            }
            else
            {
                System.out.println("THERE2");
                JSONArray team = allTeams.get(team_id_index);
                boolean inTeam = false;
                for(int i = 0; i < team.length(); i++)
                {
                    if(team.get(i) == one_player)
                    {
                        inTeam = true;
                        System.out.println("Already in the team");
                    }
                }
                if(!inTeam)
                {
                    team.put(one_player);
                    System.out.println("team.length() *should be at least 1* = " + team.length());
                }

                //print members in team
                for(int i = 0; i < team.length(); i++)
                {
                    System.out.println("TEAM MEMBER: " + team.get(i).toString()); //make clearer my printing actual name?
                    System.out.println("Also known as: " + name);
                }
                //allTeams.get(team_id_index).put(one_player);
                //but what if they somehow already have this player? Would that even come up?
            }
            if(id.contains("ai"))
            {
                bot_count++;
            }
            else
            {
                person_count++;
            }
            System.out.println("attempting to print account id: " + character.get("accountId")); //YES!

            System.out.println("---------");
        }
        System.out.println("#players (including bots): " + allPlayers.size());
        System.out.println("#people: " + person_count);
        System.out.println("#bots: " + bot_count);
        System.out.println("TEAMS: " );
        System.out.println("allTeams.size() : " + allTeams.size());
        for(int i = 0; i < allTeams.size(); i++) {
            if(allTeams.get(i) != null)
            {
                JSONArray team = allTeams.get(i);
                System.out.println("\tTEAM " + i + " #members: " + team.length());
                for (int p = 0; p < team.length(); p++) {

                    //System.out.println("\t\tMember: " + team.get(p).toString()); //problen gere
                    JSONObject person = team.getJSONObject(p);
                    if(person.has("character"))
                    {
                        //System.out.println("HELLO");
                        JSONObject character = person.getJSONObject("character");
                        //System.out.println("WE MEET AGAIN.... MWAHAHAHA");
                        if(character.has("accountId"))
                        {
                            //System.out.println("Made it here...");
                            //System.out.println("ID: " + character.get("accountId"));
                            System.out.println( "\t\t" + character.get("accountId") + " ( " + character.get("name") + ")");
                            //String accountId = character.get("accountId").toString(); //problem here
                            //System.out.println("Also known as : " + person.get("accountId"));
                        }
                        else
                        {
                            System.out.println("Character but NOT accountID found");
                        }


                    }
                }
            }
            else
            {
                //System.out.println("allTeams.get(" + i + ") is null");
                //INTERESTING: GUARDS/npc's start at TEAM 400, whereas bots/fake-players start at 200 --> are both getting included in the count?
                //      only looking for ai., I suppose, so guard not included  (but also doesn't account for the guards in any way)
            }

        }
        System.out.println();
    }

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
            //System.out.println(totalPlayers.trim());

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

