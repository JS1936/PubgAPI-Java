import org.apache.commons.io.FileUtils;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.*;

//The Main class serves as a driver for the PubgAPI-Java project.
//As of 10/5: being restructured.
//
//Consider: removing Request.java
//Consider: adding some sort of PresetReader
//public class Main extends Request {
//Idea: numAlive players against timestamp, graphing that

//NOTE: get the json via the URL link... don't just convert the general/OVERVIEW txt file to a json!
//NOTE: make sure file order reset with new command within same run
public class Main {


    //static File currentFile = null; //added 9/15 //not currently used (11/18)
    //public static Request requestCurrent;// = new Request(0,0); //not currently used (11/18)

    static File requestHistory = null; //Consider: make non-global
    public static Vector<String> mapsPlayed = new Vector<String>();
    public static Vector<String> functionalities = new Vector<String>(); //call it options instead ("functionalities" could be like the method calls) //not public?


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

    //Note: example.txt is showing up as a folder, but should appear as .txt
    //Note: example is not yet included (filled in)
    //Note: follows same process as setupRequestHistory()
    private static void setupPresetsFolder()
    {
        File presets = new File("presetsDir/example.txt");//FileManager.getFile("presetsDir/example.txt");
        if(!presets.exists())
        {
            System.out.println("presetsDir/example.txt Does not yet exist. Creating it now.");
            presets.mkdirs();
            //presets.getParentFile().mkdirs();
        }
        //System.out.println("presetsDir/example is stored at " + presets.getAbsolutePath());
    }


    private static void checkIfUsingPreset()
    {

    }
    //Conducts setup (initialize files, create Scanner, etc.) so that pseudoMain can do the brunt of the work.
    //TO-DO: Use FileManager.java to incorporate specific file activation/inactivation.
    //WON'T: include requestScopes (as of 11/18)
    public static void main(String[] args) throws IOException {
        //Do basic setup
        mapsPlayed.clear(); //clear at the beginning
        initiateFunctionalities();
        setupRequestHistory(); //if needed, creates a file to store information about requests
        setupPresetsFolder();

        //Provide Scanner for pseudoMain to use.
        Scanner input = new Scanner(System.in);
        File desiredFolder = chooseFolder(input); //only choose once per runtime.
        psuedoMain(input, desiredFolder);
        input.close();
        System.out.println("Shutting down program.");
    }

    public static void printOptions(File[] f_array)
    {
        for(int i = 1; i < f_array.length; i++)
        {
            System.out.println(i + ": " + f_array[i].getName());
        }
    }
    /*
    public static void selectPreset()
    {
        System.out.println("\nWhich preset would you like to use? Type the corresponding number and then press enter.");

    }
     */
    public static void validateChosenOption(int chosenOption, File[] f_array)
    {
        if(chosenOption <= 0 || chosenOption >= f_array.length)
        {
            System.out.println("Error. Invalid input.");
        }
        else
        {
            System.out.println("Using " + f_array[chosenOption].getName());
        }
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
        }
        else
        {
            System.out.println("Did not find it.");
        }
        return desiredFolder;
    }


    public static void psuedoMain(Scanner input, File desiredFolder) throws IOException//removed "String desiredThing"
    {
        mapsPlayed.clear(); //avoid duplicates...


        //Directory holding presets. Print the names of the files in that directory.
        //System.out.println("\nWhich preset would you like to use? Type the corresponding number and then press enter.");
        //File presets = new File("presetsDir");
        //File[] filesPresets = presets.listFiles(); //Let user decide, though?
        //printOptions(filesPresets);

        //Receive and validate preset choice
        //input = new Scanner(System.in);
        //int chosenPreset = input.nextInt();
        //validateChosenOption(chosenPreset, filesPresets);



        //File requestsDir = new File("requestsDir");
        //System.out.println("\nWhich requestDir would you like to use? Type the corresponding number and then press enter.");

        //File[] filesRequestDir = requestsDir.listFiles(); //Let user decide, though?
        //printOptions(filesRequestDir);
        //int chosenRequestDir = input.nextInt();
        //System.out.println("Chosen requestDir = " + chosenRequestDir);
        //validateChosenOption(chosenRequestDir, filesRequestDir);

        System.out.println("Makes it here");

        File[] files = new File((desiredFolder).toString()).listFiles();
        //System.out.println("FILES.length = " + files.length);
        //NOTE: case where null
        for(File file : files)
        {
            System.out.println("\t" + file.getName());
        }

        //TODO: allow presets to utilize this
        int request = getRequestType(input); //(requestType)


        //Added the try/catch writeStringToFile for requestHistory 9/15
        //Consider: storing request name and/or content (if using a preset)
        try {
            //could even have a log-in system where differentiating user histories
           //TODO: ensure print to console and write to file are synced properly
            //FileUtils.writeStringToFile(requestHistory, "\nrequest=" + request + "_requestScope=" + requestScope + "_", (Charset) null, true); //changed requestedResults to currentFile

            FileUtils.writeStringToFile(requestHistory, "\nrequest=" + request + "_", (Charset) null, true); //changed requestedResults to currentFile
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        //TODO: allow max_files to be specified (EX: in a preset?)
        int max_files = 10; //temporary (remove later)
        int filesSoFar = 0;
        int count = 0;
        for (File fileName : files)
        {
            try {
                //if(filesSoFar >= 10 || count == 0) //added count == 0 on 11/19
                //{
                //    count++;
                    //System.out.println("Reached max_files of " + max_files + "...terminating program");
                //} else
                //{
                    if(!fileName.isDirectory()) //added 10/5
                    {
                        FileManager.writeToFileAndConsole("File name is: " + fileName.getName());
                        File pretty = fileName;
                        getInfo(request, pretty, name); //changed name to name.toString... --> and reverted
                        filesSoFar++;
                        FileManager.writeToFileAndConsole("\t" + fileName.getAbsolutePath(), true);
                    }
                //}
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

    //Call the appropriate class(es) and method(s) based on user input
    public static boolean getInfo(int request, File prettyFile, String nameIfNeeded) throws IOException //changed return from void -> boolean 5/17/2022
    {
        //Could also have request be a string... (to try to avoid the nextInt(), nextLine(), etc. issue (and verifying if actually int)
        //EX: request.equalsIgnoreCase("0")
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


    //For a specific request type, print the appropriate prompt to console, as well the options the use can choose.
    public static void printOptionsToChooseFrom(Vector<String> options, String prompt)
    {
        System.out.print(prompt);
        System.out.println(" Type the corresponding number and then press enter.\n");
        for(int i = 0; i < options.size(); i++)
        {
            System.out.println(i + ": " + options.get(i));
        }
    }

    //Add desired functionalities to vector of functionalities.
    public static void initiateFunctionalities()
    {
        functionalities.add("countBotsAndPeople");
        functionalities.add("calculateKillCounts");
        functionalities.add("printPlayersByTeam");
        functionalities.add("winnerWeapons");
        functionalities.add("ranking (of a specific person)");
        functionalities.add("calculateKillCountsJSON");
        functionalities.add("printMapsPlayed");
    }


    //If requests are objects, then this is easier...?
    //abstract these better...!
    //write to file (requestHistory) here? 9/15
    //TODO: allow presets to utilize this ("input"...)
    public static int getInput(Scanner input, Vector<String> optionsToChooseFrom)
    {
        int request = -1; //not yet a valid request
        boolean requestAccepted = false;

        while(!requestAccepted)
        {
            request = Integer.parseInt(input.next()); //careful...
            System.out.println("request is: " + request);
            if(request >= 0 && request < optionsToChooseFrom.size())
            {
                requestAccepted = true; //technically not needed, but helps with clarity
                System.out.println("Request accepted!");
                return request;
            }
            System.out.println("Sorry, '" + request + "' does not match any available options. Try again.");
        }
        return request;
    }

    //Returns an integer from [0, functionalities.size()-1] representing a specific type of request/functionality.
    public static int getRequestType(Scanner input)
    {
        String prompt_requestType = "What would you like to know?";
        printOptionsToChooseFrom(functionalities, prompt_requestType);
        int requestType = getInput(input, functionalities);
        return requestType;
    }
}
    /*
    //Not yet fully implemented
    public static int getRequestScope(Scanner input)
    {
        String prompt_requestScope = "WHO are we learning about?"; //Alternative: putting prompt as index 0
        printOptionsToChooseFrom(requestScopes, prompt_requestScope);
        int requestScope = getInput(input, requestScopes);
        //String[] requestScopeOptions = {"individual (EX: matt112)", "team (EX: team of matt112)", "match (everyone in that match)"};
        return requestScope;
    }
     */


    /*
    //Not yet fully implemented
    public static void initiateRequestScopes()
    {
        requestScopes.add("individual (EX: matt112)");
        requestScopes.add("team       (EX: team of matt112)");
        requestScopes.add("match      (all individuals in that match)");
    }
    */