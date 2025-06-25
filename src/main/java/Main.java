import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.*;


//The Main class serves as a driver for the PubgAPI-Java project.
public abstract class Main {

    public static File requestHistory = null; //Consider: make non-global
    public static Vector<String> mapsPlayed = new Vector<String>();
    public static Vector<String> functionalities = new Vector<String>();


    //Conducts setup (initialize files, create Scanner, etc.) so that pseudoMain can do the brunt of the work.
    public static void main(String[] args) throws IOException {

        //Do basic setup
        initiateFunctionalities();  // adds desired abilities to vector "functionalities" (EX: countBotsAndPeople)
        setupRequestHistory();      // if needed, creates a file to store information about requests

        //Provide Scanner for pseudoMain to use.
        Scanner input = new Scanner(System.in);
        File desiredFolder = chooseFolder(input); //only choose once per runtime.
        psuedoMain(input, desiredFolder);
        input.close();
        System.out.println("Shutting down program.");
    }

    //Preserves requestHistory from previous program runs. (Further histories will be appended.)
    private static void setupRequestHistory()
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

    //Allows for multiple requests in a single running of the program.
    //Consider: acquisition vs evaluation vs edit
    //TODO: remove reliance on specific file locations and files pre-downloaded locally
    //TODO: switch to target specific requestDir item
    //TODO: list the files to console successfully here
    //Need: location of user's desired folder.
    //Error message 11/19: "Caused by: com.google.gson.stream.MalformedJsonException: Use JsonReader.setLenient(true) to accept malformed JSON at line 1 column 26 path $"
    //11/19: trying to simplify file selection process. I/O...
    public static File chooseFolder(Scanner input)
    {
        System.out.println("Enter the name of the folder you want to focus on.");
        System.out.println("EX: requestsDir/CoorsLatte/timestamp_1671342490462/matches");

        Path name_path = Path.of(input.nextLine());
        System.out.println("NAME path: " + name_path);
        File desiredFolder = new File(name_path.toFile().getAbsolutePath());
        System.out.println("desiredFolder: " + desiredFolder.getAbsolutePath());
        if(desiredFolder.isDirectory())
        {
            System.out.println("Found it.");
            //desiredFolder.deleteOnExit();
        }
        else
        {
            System.out.println("Did not find it. Note: Must be a directory.");
        }
        return desiredFolder;
    }

    //Allows multiple requests per runtime.
    public static void psuedoMain(Scanner input, File desiredFolder) throws IOException//removed "String desiredThing"
    {
        mapsPlayed.clear(); //avoid duplicates...

        File[] files = new File((desiredFolder).toString()).listFiles();
        printFileNames(files);

        //TODO: allow presets to utilize this
        String prompt_requestType = "What would you like to know?";
        printOptionsToChooseFrom(functionalities, prompt_requestType);
        int request = getRequestType(input); //(requestType)


        try {
            FileUtils.writeStringToFile(requestHistory, "\nrequest=" + request + "_", (Charset) null, true); //changed requestedResults to currentFile
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!chosenOptionIsValid(request))
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

        //TODO: allow max_files to be specified (EX: in a preset?)
        for (File fileName : files)
        {
            try {
                    if(!fileName.isDirectory()) //added 10/5
                    {
                        FileManager.writeToFileAndConsole("\n\n----------------\nFile name is: " + fileName.getName()); //added 06/24/25
                        File pretty = fileName;

                        //if file type is OFFICIAL, then do getInfo, otherwise, don't count it
                        if(MatchManager.isOfficialMatch(fileName))
                        {
                            getInfo(request, pretty, name);
                            FileManager.writeToFileAndConsole("\t" + fileName.getAbsolutePath(), true);
                        }
                        else
                        {
                            FileManager.writeToFileAndConsole(fileName.toString() + " is not an official match." +
                                    " Not going to analyze it.", true);
                        }
                    }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

        String response = "y";
        System.out.println("Any other requests? (y/n)");
        response = input.next();

        if(response.equalsIgnoreCase("Y"))
        {
            psuedoMain(input, desiredFolder);
        }
        //program shutdown should begin shortly
        //System.out.println("Shutting down program.");
    }


    //Calls the appropriate class(es) and method(s) based on user input
    public static boolean getInfo(int request, File prettyFile, String nameIfNeeded) throws IOException //changed return from void -> boolean 5/17/2022
    {
        //If doing separate task-objects (EX: kill counts), could "create" them here in an array, call via the ifs)
        if(request == 0)
        {
            BotCounts.countBotsAndPeople(prettyFile); //seems to work
        }
        else if(request == 1)
        {
            KillCounts.calculateKillCounts(prettyFile); //seems to work

        }else if(request == 2) {

            MatchManager.printPlayersByTeam(prettyFile); //seems to work (but quite messy)

        }else if(request == 3) {

            MatchManager.winnerWeapons(prettyFile); //seems to work

        }else if(request == 4) {

            Ranking.ranking(nameIfNeeded, prettyFile); //seems to work (ALMOST --> getting null errors)

        }else if(request == 5) {
            //NOT WORKING
            KillCountsJSON.calculateKillCountsJSON(prettyFile);
            //having trouble accessing names of the winners specifically

        }else if(request == 6) {
            String name = MatchManager.getMapName(prettyFile);
            mapsPlayed.add(name);
            System.out.println("map name: " + name);

        }else
        {
            System.out.println("Invalid request"); //for example's sake (currently)
            return (request < 0 || request >= functionalities.size()); //invalid requests should not have valid numbers //changed 7 to functionalities.size()
        }
        return (request < functionalities.size() && request >= 0); //valid requests should have valid numbers
    }


    //For a specific request type, prints the appropriate prompt to console, as well the options the use can choose.
    public static void printOptionsToChooseFrom(Vector<String> options, String prompt)
    {
        System.out.println(prompt + " Type the corresponding number and then press enter.\n");
        for(int i = 0; i < options.size(); i++)
        {
            System.out.println(i + ": " + options.get(i));
        }
    }

    private static void printFileNames(File[] directory)
    {
        //System.out.println("FILES.length = " + files.length);
        //NOTE: case where null
        for(File file : directory)
        {
            System.out.println("\t" + file.getName());
            //file.deleteOnExit();
        }
    }

    //Adds desired functionalities to vector of functionalities. Customizable before/after runtime.
    protected static void initiateFunctionalities()
    {
        functionalities.add("countBotsAndPeople");
        functionalities.add("calculateKillCounts");
        functionalities.add("printPlayersByTeam");
        functionalities.add("winnerWeapons");
        functionalities.add("ranking (of a specific person)");
        functionalities.add("calculateKillCountsJSON");
        functionalities.add("printMapsPlayed");
    }

    //TODO: allow presets to utilize this ("input"...) //abandon
    private static int getInput(Scanner input, Vector<String> optionsToChooseFrom)
    {
        int request = -1; //not yet a valid request
        boolean requestAccepted = false;

        while(!requestAccepted)
        {
            request = Integer.parseInt(input.next()); //careful...
            System.out.println("request is: " + request);
            if(chosenOptionIsValid(request))
            {
                //requestAccepted = true; //technically not needed, but helps with clarity
                System.out.println("Request accepted!");
                return request;
            }
            System.out.println("Sorry, '" + request + "' does not match any available options. Try again.");
        }
        return request;
    }

    //Returns an integer from [0, functionalities.size()-1] representing a specific type of request/functionality.
    private static int getRequestType(Scanner input)
    {
        int requestType = getInput(input, functionalities);
        return requestType;
    }

    //Returns true if chosenOption >= 0 and chosenOption < functionalities.size(). Otherwise, returns false.
    private static boolean chosenOptionIsValid(int chosenOption)
    {
        return (chosenOption >= 0 && chosenOption < functionalities.size());
    }
}

    /*
    private static void setupFolderGivenPathname(String pathname) {
        File newFolder = new File(pathname);
        if (!newFolder.exists())
        {
            newFolder.mkdirs();
            newFolder.getParentFile().mkdirs();
            System.out.println(pathname + " does not yet exist. Creating it now.");
        }
    }
     */
