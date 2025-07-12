import org.apache.commons.io.FileUtils;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.*;

/*
 * The Main class serves as a driver for the PubgAPI-Java project.
 * 
 * Future considerations:
 *  - Enable presets
 *  - Make requestHistory non-global
 *  - Decide retention period for telemetry data (avg >1million lines per file)
 *  - Allow user to choose a different folder (EX: change files to analyze without re-running the entire program)
 *  - Allow user to choose a single file to analyze (rather than a folder/directory)
 */
public abstract class Main {

    public static File requestHistory = null;
    public static Vector<String> mapsPlayed = new Vector<String>();
    public static Vector<String> functionalities = new Vector<String>();

    /*
     * Conducts setup (initialize files, create Scanner, etc.) so that pseudoMain can do the brunt of the work.
     */
    public static void main(String[] args) throws IOException {

        //Do basic setup
        initiateFunctionalities();  // adds desired abilities to vector "functionalities" (EX: countBotsAndPeople)
        setUpRequestHistory();      // if needed, creates a file to store information about requests

        //Provide Scanner for pseudoMain to use.
        Scanner input = new Scanner(System.in);
        File desiredFolder = chooseFolder(input); //only choose once per runtime.
        psuedoMain(input, desiredFolder);
        input.close();
        System.out.println("Shutting down program.");
    }

    /*
     * Adds desired functionalities to vector of functionalities. Customizable before/after runtime.
     */
    protected static void initiateFunctionalities()
    {
        functionalities.add("countBotsAndPeople");
        functionalities.add("calculateKillCounts");
        functionalities.add("printPlayersByTeam");
        functionalities.add("winnerWeapons");
        functionalities.add("ranking (of a specific person)");
        functionalities.add("calculateKillCountsJSON");
        functionalities.add("printMapsPlayed");
        //functionalities.add("watchDeathBoard"); //not fully implemented
    }

    /*
     * Preserves requestHistory from previous program runs. (Further histories will be appended.)
     */
    private static void setUpRequestHistory()
    {
        requestHistory = new File("requestHistory.txt");
        if(!requestHistory.exists())
        {
            System.out.println("requestHistory.txt Does not yet exist. Creating it now.");
            requestHistory.mkdirs();
            requestHistory.getParentFile().mkdirs();
        }
        //System.out.println("requestHistory is stored at " + requestHistory.getAbsolutePath());
    }

    /*
     * Using a Scanner, allows the user to choose a folder.
     * That folder's contents will be analyzed when the user chooses functionalities.
     * Returns the desired folder.
     * If the folder does not exist, prints an error message to console.
     */
    public static File chooseFolder(Scanner input)
    {
        System.out.println("Enter the name of the folder you want to focus on.");
        System.out.println("EX: requestsDir/Shrimzy/timestamp_1750557892980/matches");

        Path name_path = Path.of(input.nextLine()); //EX: requestsDir/Shrimzy/timestamp_1750557892980/matches
        System.out.println("NAME path: " + name_path);
        File desiredFolder = new File(name_path.toFile().getAbsolutePath());
        System.out.println("desiredFolder: " + desiredFolder.getAbsolutePath());
        if(desiredFolder.isDirectory()) {
            System.out.println("Found it.");
            //desiredFolder.deleteOnExit();
        } else {
            System.out.println("Did not find it. Note: Must be a directory.");
        }
        return desiredFolder;
    }

    /*
     * Allows multiple requests per runtime.
     * Note: In the future, consider allowing presets to usizilize prompt_requestType and printOptionsToChooseFrom()
     */
    public static void psuedoMain(Scanner input, File desiredFolder) throws IOException
    {
        //Request 6 requires mapsPlayed to be cleared before a new request in order to avoid duplicates
        mapsPlayed.clear();

        //Print the names of the files present in folder "desiredFolder"
        File[] files = new File((desiredFolder).toString()).listFiles();
        FileManager.printFileNames(files);

        //Prompt the user to choose a request type
        String prompt_requestType = "What would you like to know?";
        printOptionsToChooseFrom(functionalities, prompt_requestType);
        int request = getRequestType(input, functionalities); //(requestType)

        //Declare request type (EX: "request=0") before printing request results.
        try {
            FileUtils.writeStringToFile(requestHistory, "\nrequest=" + request + ":", (Charset) null, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //If chosen option is invalid (EX: -1), print error message and return
        if(!chosenOptionIsValid(request)) {
            System.out.println("Invalid request");
            return;
        }

        //Request 4 requires addition console input (player name) before the files are analyzed.
        String name = "";
        if(request == 4)
        {
            System.out.println("Who are you looking for? (EX: matt112)");
            name = input.next();
            System.out.println("Now looking for: '" + name + "'");
        }

        //For each file in directory "files", analyze to according to the user-selected functionality
        for (File fileName : files) 
        {
            try {
                if(!fileName.isDirectory())
                {
                    FileManager.writeToFileAndConsole("\n\n----------------\nFile name: \t" + fileName.getName());
                    FileManager.writeToFileAndConsole("Absolute path: \t" + fileName.getAbsolutePath(), true);
                    File pretty = fileName;

                    //If file type is OFFICIAL, then do getInfo, otherwise, don't count it
                    if(MatchManager.isOfficialMatch(fileName)){    
                        getInfo(request, pretty, name);
                    } else {
                        FileManager.writeToFileAndConsole(fileName.toString() + " is not an official match." +
                                " Not going to analyze it.", true);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Request 6 requires additional console output after each file has been analyzed.
        if(request == 6)
        {
            FileManager.writeToFileAndConsole("REQUEST = 6");
            FileManager.writeToFileAndConsole("mapsPlayed.size() : " + mapsPlayed.size());
            try {
                MapManager.printMapNames();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Allow the user to shut down the program or continue and make further requests. 
        //If pseudoMain is not called again, program shutdown should begin shortly.
        String response = "y";
        System.out.println("Any other requests? (y/n)");
        response = input.next();
        if(response.equalsIgnoreCase("Y"))
        {
            psuedoMain(input, desiredFolder);
        }
    }

    /*
     * Calls the appropriate class(es) and method(s) based on user input.
     * Returns true if request is valid [0 <= request < functionalities.size()]. Returns false if request is invalid.
     * Note: "Invalid request" check is not needed in getInfo because getRequestType performs that check.
     * Note: The previous boolean return type is not needed because the true/false return does not get acted on.
     */
    public static void getInfo(int request, File prettyFile, String nameIfNeeded) throws IOException //changed return from void -> boolean 5/17/2022
    {
        if(request == 0)        {   BotCounts.countBotsAndPeople(prettyFile);    } 
        else if(request == 1)   {   KillCounts.calculateKillCounts(prettyFile);  }
        else if(request == 2)   {   MatchPlayers.printPlayersByTeam(prettyFile); }
        else if(request == 3)   {   MatchWeapons.winnerWeapons(prettyFile);      }
        else if(request == 4)   {   MatchRanking.ranking(nameIfNeeded, prettyFile);}
        else if(request == 5)   {   KillCountsJSON.calculateKillCountsJSON(prettyFile); } ////NOT WORKING. Having trouble accessing names of the winners specifically
        else if(request == 6)   {   String name = MatchManager.getMapName(prettyFile);
                                    mapsPlayed.add(name);
                                    System.out.println("map name: " + name);
        }else if(request == 7) {    System.out.println("Death Board is not fully implemented yet."); }
    }

    /*
     * For a specific request type, prints the appropriate prompt to console, as well the options the use can choose.
     */
    public static void printOptionsToChooseFrom(Vector<String> options, String prompt)
    {
        System.out.println(prompt + " Type the corresponding number and then press enter.\n");
        for(int i = 0; i < options.size(); i++) {
            System.out.println(i + ": " + options.get(i));
        }
    }

    /*
     * Returns an integer from [0, functionalities.size()-1] representing a specific type of request/functionality.
     */
    private static int getRequestType(Scanner input, Vector<String> optionsToChooseFrom)
    {
        int request = -1; //not yet a valid request
        boolean requestAccepted = false;
        while(!requestAccepted){
            request = Integer.parseInt(input.next()); // EX: 0
            System.out.println("request is: " + request);
            if(chosenOptionIsValid(request)){
                //requestAccepted = true; //technically not needed, but helps with clarity
                System.out.println("Request accepted!");
                return request;
            }
            System.out.println("Sorry, '" + request + "' does not match any available options. Try again.");
        }
        return request;
    }

    /*
     * Returns true if chosenOption >= 0 and chosenOption < functionalities.size(). Otherwise, returns false.
     */
    private static boolean chosenOptionIsValid(int chosenOption)
    {
        return (chosenOption >= 0 && chosenOption < functionalities.size());
    }
}