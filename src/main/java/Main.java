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

    //TEAM 1 --> person, person, person, person (EX)

    //team# ==> find
    //assign to that slot in array (put(i,obj))
    //Or what if you just added teams in whatever order they appeared, and then searched each time to see if they existed...?
    //Vector<String> player_info = new Vector<String>();

    /*

            //System.out.println("i = " + i + "; object is: " + jsonArray.getJSONObject(i));

            //Find where the match starts, THEN start counting players/bots
            //Maybe counts of bots and team allocations can be done here, too


            //WHAT IS HAPPENING
                //jsonArray holds file contents
                //each jsonObject is an object (from the file)
                    //Some objects describe participants in the game (like real people and bots)
                    //These get put into the JSONArray playersList
                    //Which gets transferred to Vector<JSONArray> allPlayers for some reason?
     */
    public static void singleStringOfFile(File prettyFile) //??? WHAT IS HAPPENING HERE???
    {
        System.out.println("NEW FILE_______________________________________________");
        Vector<JSONObject> allPlayers = new Vector<JSONObject>();
        Vector<JSONArray> allTeams = new Vector<JSONArray>(); //just a list, then? //ArrayList?
        Vector<JSONObject> one_team = new Vector<JSONObject>();
        Vector<Vector> teams = new Vector<Vector>(); //Inner vector: 1 team, outer vector: all teams
        Vector<JSONObject> peopleByTeam = new Vector<JSONObject>();

        //WHAT IF IT WAS JUST ONE VECTOR
        //FOUR SLOTS AVAILABLE per team, up to 500 teams
        //Team 0: indices 0,        1,      2,      3
        //Team 1: indices 4,        5,      6,      7
        //Team 2: indices 8,        9,      10,     11
        //Team 3: indices 12,       13,     14,     15
        //beginning index of Team X is (x*4)

        //Each team shared the same team_id...
        // index 0 || index 1 || index 2 || index 3 IN VECTOR

        //   ^
        //   Here: {person1, person2, person3, person4}
        JSONArray playersList = new JSONArray();

        int bot_count = 0;
        int person_count = 0;
        int highest_team_id = 0;
        int team_count = 0; //still need to calculate this in code

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
        int max_num_teams = 425;
        int maxIndices = max_num_teams * 4;
        peopleByTeam.setSize(maxIndices);
        //reset with each file?
        //for(int i = 0; i < maxIndices ; i++) {

            //peopleByTeam.set(i, null); //default value
            //Vector<JSONObject> single_team = new Vector<JSONObject>();
            // teams.get(i) = single_team;
        //}
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String type = jsonObject.getString("_T");
            // System.out.println("TYPE: " + type);
            if (type.equalsIgnoreCase("LogMatchStart")) {
                System.out.println(type);
                //JSONArray playersList = jsonObject.getJSONArray("characters"); //oh, goes out of scope, hence allPlayers being used (fix?)
                playersList = jsonObject.getJSONArray("characters"); //attempt to fix

                //Set up allTeams
                for (int j = 0; j < playersList.length(); j++) {
                    JSONObject player = playersList.getJSONObject(j);
                    JSONObject character = player.getJSONObject("character"); //problem here

                    //String id = character.get("accountId").toString();
                    String team_id = character.get("teamId").toString();
                    //String name = character.get("name").toString();
                    ///System.out.println(name + " has id : " + id + " and is in team # : " + team_id);

                    int team_id_index = Integer.parseInt(team_id);
                    int insert = (team_id_index * 4);
                    for(int loc = 0; loc < 4; loc++) {
                        if (peopleByTeam.get(insert + loc) != null) {
                            loc++;
                        } else {
                            peopleByTeam.set(insert + loc, character);
                            //System.out.println("HELLO! HELLO!");
                            //System.out.println("NAME of person inserted: " + peopleByTeam.get(insert + loc).get("name"));
                            loc = 4;
                        }
                        // System.out.println("peopleByTeam.size() : " + peopleByTeam.size());
                        //    String index0 = peopleByTeam.get(0).toString();
                        //   System.out.println("index0: " + index0);



                        }
                        //if (peopleByTeam.get(insert) != null) {
                        //}
                       // if (team_id_index == 2) {
                         //   one_team.add(character);
                           // for (int x = 0; x < one_team.size(); x++) {
                           //     System.out.println(character.get("name") + " is in team 2.0 (" + one_team.size() + " members total)");
                           // }
                        //}
                    }
                    for (int in = 0; in < peopleByTeam.size(); in++) {

                        if(in % 4 == 0 && peopleByTeam.get(in) != null)
                        {
                            System.out.println("-----");
                        }
                        if (peopleByTeam.get(in) != null) {
                            //teamExists = true;
                            //System.out.println("LOOK!");
                            System.out.print(peopleByTeam.get(in).get("name").toString());
                            System.out.println(" " + peopleByTeam.get(in).get("teamId").toString());
                            //peopleByTeam.s

                        } else {

                            //System.out.println("NULL");
                        }

                        //if(in < peopleByTeam.size() -1 && peopleByTeam.get(in + 1) != null)
                        //{
//
                        //    if(peopleByTeam.get(in).get("teamId").toString() != (peopleByTeam.get(in-1).get("teamId").toString()))
                        //        System.out.println("----");
                        //    }
                    //PUT HERE
                    /*
                    //System.out.println(team_id_index + " VS current highest of " + highest_team_id);
                    if (team_id_index >= highest_team_id) {
                        System.out.println(team_id_index + " >= " + highest_team_id);
                        System.out.print("size changed from: " + teams.size() + " to ");
                        highest_team_id = team_id_index + 1; //+1 so that it can actually hold something AT index of team_id_index
                        System.out.println(highest_team_id);
                        //allTeams.setSize(highest_team_id);
                        teams.setSize(highest_team_id);
                        System.out.println("HELLO");
                        //if(one_team.size() == 0)
                        if(teams.get(team_id_index))
                        {

                        }
                            System.out.println("First member: " + character.get("name"));
                            teams.add(team_id_index, one_team);
                            one_team.add(character);
                        }
                        else
                        {
                            System.out.println("NOT first member");
                            if(!one_team.contains(character))
                            {

                                int size = teams.get(team_id_index).size();
                                one_team.add(size, character);
                            }
                        }

                        //teams.setElementAt(one_team, team_id_index);

                        //teams.get(team_id_index).add(size, character);
                        System.out.println("Size of team: " + one_team.size() + "for team#" + team_id_index);

                     */
                        //if(allTeams.get(team_id_index) == null) //Why is this always null?
                        //{
                        //   System.out.println("Beginning new team: #" + team_id_index);
                        //   JSONArray list_of_members = new JSONArray();
                        //   list_of_members.put(character);
                        //   allTeams.add(team_id_index, list_of_members); //goes out of scope?

                        //}
                        // else
                        //{
                        //System.out.println("Adding to established team: #" + team_id_index);
                        //JSONArray current_members = allTeams.get(team_id_index);
                        //current_members.put(current_members.length(), character);
                            /*
                            JSONArray your_team = allTeams.get(team_id_index);
                            int your_team_length = your_team.length();
                            System.out.println("Your_team_length (before adding another person): " + your_team_length);
                            //int team_size = allTeams.get(team_id_index).length(); //EX: 2 people already means size 2, insert at 0, 1 --> INDEX 2
                            System.out.println("Trying to add : " + name + " to team " + team_id);
                            your_team.put(your_team_length, character);
                            //allTeams.get(team_id_index).put(team_size, character); //(index to insert at, object)
                            */



                    // System.out.println(allTeams.size());
                }
            }
        }
    }

                    //These are all null right now... no one is getting added (/saved) to their teams
                    //for(int team = 0; team < allTeams.size(); team++)
                    //{
                     //   System.out.println("LOOKING AT TEAM#" + team);
                     //   if(allTeams.get(team) != null)
                     //   {
                     //       JSONArray members = allTeams.get(team);
                     //       System.out.println("\t#members in team " + team + ": " + members.length());
                     //   }
                        //JSONArray members = allTeams.get(team);

                        //System.out.println("#members: " + members.length());
                    //}
                    //System.out.println("PLAYER: " + player.toString());

                    //if (player.has("character")) {
                        ////System.out.println("Does list of all players already have this player?");
                        //System.out.println(" --> includes character");
                        //if (allPlayers.contains(player)) {
                        //    System.out.println("allPlayers list : already has that player");
                        //}
                        //if (!allPlayers.contains(player)) {
                        //    System.out.println("ADDING PLAYER: " + player);
                        //    allPlayers.add(player);
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

                        //}
                    //} else {
                        //System.out.println(" --> does NOT include character");
                    //}
                //}

          //  }

            //System.out.println(playersList);

            //System.exit(0);
        //}
        //System.out.println("end of playerslist printing");

        //System.out.println("allplayers.size() : " + allPlayers.size());
        //System.out.println("playersList.length() : " + playersList.length()); //playersList.legnth() and allplayers.size() should match

        /*
        System.out.println("PRINTING allPlayers:" );
        for (int index = 0; index < allPlayers.size(); index++) {
            //System.out.println(allPlayers.get(i).getString("character"));
            System.out.println(allPlayers.get(index).toString());
        }
        */

        //System.out.println("PRINTING accountID of each player: ");
        //for (int index = 0; index < allPlayers.size(); index++) {
        //}
    //}

            //JSONObject one_player = allPlayers.get(index);
            //JSONObject character = one_player.getJSONObject("character"); //problem here

            /*
            String id = character.get("accountId").toString();
            String team_id = character.get("teamId").toString();
            String name = character.get("name").toString();
            System.out.println(name + " has id : " + id + " and is in team # : " + team_id);
            */


            /*
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
                    System.out.println(team.get(i).toString() + "--VS--");
                    System.out.println(one_player.toString());
                    if(team.get(i) == one_player)
                    {
                        inTeam = true;
                        System.out.println("Already in the team");
                    }
                }
                if(!inTeam) {
                    System.out.println("LOOK: Adding " + name + " to team #" + team_id_index);
                    team.put(team.length() - 1, one_player);
                    System.out.println("Team size: " + team.length());
                    System.out.println("Team members are: ");
                    for (int index2 = 0; index2 < team.length(); index2++)
                    {
                        String member = team.get(index2).toString();
                        System.out.println("------> " + member);
                    }
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

