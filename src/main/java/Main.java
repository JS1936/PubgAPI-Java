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
    public static void singleStringOfFile(File prettyFile) //??? WHAT IS HAPPENING HERE???
    {
        System.out.println("NEW FILE_______________________________________________");
        Vector<JSONObject> allPlayers = new Vector<JSONObject>();
        Vector<String> player_info = new Vector<String>();

        int bot_count = 0;
        int person_count = 0;

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
            //System.out.println("HI");

            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String type = jsonObject.getString("_T");
            // System.out.println("TYPE: " + type);
            if (type.equalsIgnoreCase("LogMatchStart")) {
                System.out.println(type);
                JSONArray playersList = jsonObject.getJSONArray("characters");
                //System.out.println("playersList: " + playersList.toString());
                //
                //
                for (int j = 0; j < playersList.length(); j++) {
                    if (allPlayers.isEmpty()) {
                        System.out.println("allPlayers is empty");
                    }
                    JSONObject player = playersList.getJSONObject(j);
                    //System.out.println("PLAYER: " + player.toString());

                    if (player.has("character")) {
                        //System.out.println("Does list of all players already have this player?");
                        System.out.println(" --> includes character");
                        if (allPlayers.contains(player)) {
                            System.out.println("allPlayers list : already has that player");
                        }
                        if (!allPlayers.contains(player))
                            System.out.println("ADDING PLAYER: " + player);
                            allPlayers.add(player);
                            System.out.println(player.has("accountId"));
                            //String id = player.getString("accountId"); //false
                            //System.out.println("Hopefully id: " + id);
                           //Object character_info =  player.get("character");

                    } else {
                        System.out.println(" --> does NOT include character");
                    }
                }

            }

            //System.out.println(playersList);

            //System.exit(0);
        }
        System.out.println("end of playerslist printing");
            /*
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(jsonObject.has("mapName")) //list of all the characters...
            {
                    //System.out.println("CHARACTERS ARE: " + jsonObject.getString("characters"));
                System.out.println("LOOK HERE!");
                    JSONArray arrayOfCharacters = jsonObject.getJSONArray("characters");
                    System.out.println("CHARACTERS: " + arrayOfCharacters);
            }
            else
            {
                System.out.println("No 'characters'");
            }
            if(jsonObject.has("character"))
            {
                System.out.println("\t\tFOUND A CHARACTER THING!");
            }
            if(jsonObject.has("accountId"))
            {
                System.out.println("Has account id");
                String id = jsonObject.getString("accountId");
                System.out.println("ID is : " + id);
            }
            JSONArray names = jsonObject.names();
            //if(names[0].has());

            for(int index = 0; index < names.length(); index++)
            {
                System.out.println("     " + names.get(index)+ " : " + jsonObject.get(names.get(index).toString()) + " ");
                //EX:                          _T:                      LogItemUse

            }
            */
        System.out.println("PRINTING allPlayers:" );
        for (int index = 0; index < allPlayers.size(); index++) {
            //System.out.println(allPlayers.get(i).getString("character"));
            System.out.println(allPlayers.get(index).toString());
        }

        System.out.println("PRINTING accountID of each player: ");
        for (int index = 0; index < allPlayers.size(); index++) {

            //System.out.println(allPlayers.get(i).getString("character"));
            JSONObject one_player = allPlayers.get(index);
            System.out.println("LOOK: " + one_player.has("character"));
            System.out.println("get character: " + one_player.get("character"));
            System.out.println("LOOK2:" + one_player.get("character").toString());
            JSONObject character = one_player.getJSONObject("character"); //problem here
            System.out.println("HERE");
            System.out.println("LOOK3: " + character.toString());
            //String keys = one_player.keys().toString();

            //System.out.println("KEYS: " + character_keys);
            /*
            if(allPlayers.get(index).get("character").get("accountId"))
            {
                System.out.println("has accountId");
            }
            else
            {
                System.out.println("does not have accountId");
            }
            */

            //System.out.println(allPlayers.get(index).keys());
            System.out.println("---------");
        }
        System.out.println("#players (including bots): " + allPlayers.size());
        System.out.println();
    }
            //jsonObject.get()
            //System.out.println("NAMES: " + jsonObject.names()); //EX: character, item, common, _D, _T, healAmount
            //System.out.println("LENGTH: " + jsonObject.length());
            //System.out.println("KEYSET: " + jsonObject.keySet());
            //Set<String> keys = jsonObject.keySet();

            //keys.toArray();
            //What if you want to get (STAT X) from character?
            //for (int x = 0; x < keys.size(); x++)
            //{
            //    String key = keys.toString();
            //    System.out.println("LOOK: " + jsonObject.get(key));
            //}

            //String accountID = jsonObject.getJSONObject("accountID").getString("accountID");
            //System.out.println("PRINTING: " + accountID);
            //System.out.println("Is object empty? : " + jsonObject.isEmpty() + "(false = " + false + ", true = " + true);
        /*
            if(!allPlayers.contains(jsonObject))
            {
                allPlayers.add(jsonObject);
            }
            else
            {
                System.out.println("Player " + jsonObject.names() + " is already registered");
            }
            */

            //https://examples.javacodegeeks.com/java-map-example/-------------
            /*
            Map<String, Integer> map = new HashMap<String, Integer>();
            map.put("key1", 1);
            map.put("name", 7);
            map.put("age", 9);
            //System.out.println("Total #objects: " + map.size());
            ///---------------========
            for(String key : map.keySet())

            {
                System.out.println(key + " - " + map.get(key));
                System.out.println();
            }
            */

            //-----------------------
            //for(int index = 0; index < allPlayers.size(); index++)
            //{
             //   JSONObject info = allPlayers.get(index);
                //System.out.println("KEYS: " + info.keys());

                //jsonArray.putAll(Iter i);
                //String accountID = info.getJSONObject("accountID").getString("accountID");
                //System.out.println("PRINTING: " + accountID);
                //System.out.println("PLAYER: " + allPlayers.get(index));
                //Object character = info.get("\"character\"");
                //System.out.println("CHARACTER: " + character);
            //}



       // }
        //JSONObject jsonObject = jsonArray.getJSONObject(i); //in a for loop?
        //jsonArray.
        //String value = jsonObject.getString("key");

        /*
        String file_content = "";
        try {
            Scanner scan = new Scanner(prettyFile);
            while(scan.hasNextLine()) {
                file_content += scan.nextLine();
            }

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        System.out.println("FILE CONTENT: ");
        System.out.println(file_content);
        */


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

