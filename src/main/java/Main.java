import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Scanner;
import java.util.Vector;


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
        calculateKillCounts(prettyFile); //in progress

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

