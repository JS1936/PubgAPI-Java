import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.Vector;

public class KillCounts extends Request {

    public static void printKillCountsToHistory(Vector<String> counts) throws IOException {
        //time of request?
        //FileUtils.writeStringToFile();
       //String context = "Printing #kills per person. EX: Die first? Your #kills is printed first. Die last? Your #kills is printed last.";
        //try {
        //FileUtils.writeStringToFile(Main.requestHistory, "\n" + context, (Charset) null, true); //added 9/15
        FileManager.writeToFileAndConsole("Printing #kills per person. EX: Die first? Your #kills is printed first. Die last? Your #kills is printed last.");
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
                FileUtils.writeStringToFile(Main.requestHistory, "\n", (Charset) null, true); //added 9/15
            }
            int numKills = Integer.valueOf(counts.get(i));
            if (maxKills < numKills) {
                maxKills = numKills;
            }
            if (counts.size() - 10 <= i) {
                killsByTopTen += numKills;
            }
            frequencies[numKills]++;
            FileManager.writeToFileAndConsole(counts.get(i) + " ");
            //System.out.print(counts.get(i) + " ");
            //FileUtils.writeStringToFile(Main.requestHistory, counts.get(i) + " ", (Charset) null, true); //added 9/15
        }

        //Print out how many people got X number of kills
        FileManager.writeToFileAndConsole("\nKill Frequencies:");
        //System.out.println("\nKill Frequencies:");
        //FileUtils.writeStringToFile(Main.requestHistory, "\nKill Frequencies:", (Charset) null, true); //added 9/15

        for (int index = 0; index <= maxKills; index++) {
            FileManager.writeToFileAndConsole(frequencies[index] + " got " + index + " kills.");
            //System.out.println(frequencies[index] + " got " + index + " kills.");
            //FileUtils.writeStringToFile(Main.requestHistory, "\n" + frequencies[index] + " got " + index + " kills.", (Charset) null, true); //added 9/15
        }
        ///System.out.println("MAX #kills by a single person: " + maxKills + " (#people who achieved this: " + frequencies[maxKills] + ")");
        ///System.out.println("#people killed by 'TOP TEN' : " + killsByTopTen + " of " + counts.size());
        ///System.out.println("--------------------------------------------------------------------------");
        ///FileUtils.writeStringToFile(Main.requestHistory, "\nMAX #kills by a single person: " + maxKills + " (#people who achieved this: " + frequencies[maxKills] + ")", (Charset) null, true); //added 9/15
       /// FileUtils.writeStringToFile(Main.requestHistory, "\n#people killed by 'TOP TEN' : " + killsByTopTen + " of " + counts.size(), (Charset) null, true); //added 9/15
        ///FileUtils.writeStringToFile(Main.requestHistory, "\n--------------------------------------------------------------------------", (Charset) null, true); //added 9/15
        FileManager.writeToFileAndConsole("MAX #kills by a single person: " + maxKills + " (#people who achieved this: " + frequencies[maxKills] + ")");
        FileManager.writeToFileAndConsole("#people killed by 'TOP TEN' : " + killsByTopTen + " of " + counts.size());
        FileManager.writeToFileAndConsole("--------------------------------------------------------------------------");
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
    //Accidentally removed this! Found it again through github commits history
    //Could re-implement this using jsonobjects (AND also be able to get teams, kills by team)
    //Vector<Integer> killsByTeam = new Vector<Integer>();
    //Vector<Vector<Integer>> teams = new Vector<Vector<Integer>>();
    //IS A MANUAL VERSION: Does not use JSONObjects. Instead, scans line by line.
    public static void calculateKillCounts(File prettyFile) {
        Vector<String> killCounts = new Vector<String>();
        try {
            Scanner scan = new Scanner(prettyFile);
            while (scan.hasNextLine()) {
                String data = scan.nextLine();
                if (data.contains("killCount")) {
                    String killNum = data.substring(data.length() - 2, data.length() - 1);
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
    //this.scopes;
      //  this.types;
    KillCounts() //for everyone
    {
        if(this.getRequest_scope() == 0) //single person
        {

        }
    }

    public static void print()
    {

    }
    public static void printJSON()
    {

    }
    public static void calculate()
    {

    }
    public static void calculateJSON()
    {

    }
    public static void write()
    {

    }
    public static void writeJSON()
    {

    }
    //calculate
    //print
    //write...
    //constructor
    //how does it get called...?
}
