import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.Vector;

//The KillCounts class is a MANUAL approximation of the KillCountsJSON class. Reports killCount data on a single match.
public class KillCounts extends Request {

    /*
     * IS A MANUAL VERSION: Does not use JSONObjects. Scanner-based.
     *
     * Prints killCounts to requestHistory AND to user console.
     *
     * Includes:
     *  -chronological killCounts (the first killCount shown represents the first player to die)
     *  -frequency-based killCounts (specifies how many people got ___ #kills).
     *  -maxKills by a single person
     *  -how many people were killed by the last ten surviving players
     */
    public static void printKillCountsToHistoryAndConsole(Vector<String> counts) throws IOException {

        FileManager.writeToFileAndConsole("Printing #kills per person. EX: Die first? Your #kills is printed first. Die last? Your #kills is printed last.");

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
        }

        //Print out how many people got X number of kills
        FileManager.writeToFileAndConsole("\nKill Frequencies:");

        for (int index = 0; index <= maxKills; index++) {
            FileManager.writeToFileAndConsole(frequencies[index] + " got " + index + " kills.");
        }
        FileManager.writeToFileAndConsole("MAX #kills by a single person: " + maxKills + " (#people who achieved this: " + frequencies[maxKills] + ")");
        FileManager.writeToFileAndConsole("#people killed by 'TOP TEN' : " + killsByTopTen + " of " + counts.size());
        FileManager.writeToFileAndConsole("--------------------------------------------------------------------------");
    }

    /*
     * IS A MANUAL VERSION: Does not use JSONObjects. Scanner-based.
     * Deliberately has primary function of calling printKillCountsToHistoryAndConsole(counts).
     */
    public static void printKillCounts(Vector<String> counts) {
        try {
            printKillCountsToHistoryAndConsole(counts); //added 9/15
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * IS A MANUAL VERSION: Does not use JSONObjects. Scanner-based. Scans line by line.
     *
     * Given a file describing a match, anonymously counts how many kills each player got in the match. Tracks players'
     * killCounts based on when they themselves die, and therefore will not get any more kills in the match.
     *
     * Uses a String to track the statistics chronologically
     * (players who die first have their killCount appear earlier in the String).
     *
     * Uses a vector to tally how many players in the game got ___ number of kills. The index corresponds to #kills,
     * and the value at that index indicates how many players in the match got precisely index number of kills.
     * In other words, vector[#kills] = #players who got that number of kills)
     * Example: If index 5 holds 3, that means 3 players got 5 kills.
     *
     * Calls printKillCounts.
     *
     * NOTE: Could re-implement this using jsonobjects (AND also be able to get teams, kills by team)
     *       --> Vector<Integer> killsByTeam = new Vector<Integer>();
     *      --> Vector<Vector<Integer>> teams = new Vector<Vector<Integer>>();
     * Uses both a String to track order of events, and a vector  ___ number of kills using a vector where the index
     * Calls printKillCounts. printing a player's kill count when they themselves
     * actually die.
     */
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
}
