import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.*;
import java.nio.charset.Charset;
import java.util.*;


//is an eof error happening...?
//read the file to string first...
public class Main extends Request { //added "extends Memory" 6/16/2022 //added Request.java 9/15, removed extends Memory

    static File currentFile = null; //added 9/15
    static File requestHistory = null;


    public static void psuedoMain(Scanner input) //removed "String desiredThing"
    {
        File[] files = new File("C:\\Users\\jmast\\pubgFilesExtracted").listFiles(); //Let user decide, though?

        mapsPlayed.clear(); //avoid duplicates...

        input = new Scanner(System.in);

        int request = getRequestType(input); //(requestType)
        int requestScope = getRequestScope(input);
        //Request r = new Request(request, requestScope);
        requestCurrent = new Request(request, requestScope);
        //int request = getRequest(input); //string or int? (Getting confused)


        //Added the try/catch writeStringToFile for requestHistory 9/15
        try {
            //could even have a log-in system where differentiating user histories
            FileUtils.writeStringToFile(requestHistory, "\nrequest=" + request + "_requestScope=" + requestScope + "_", (Charset) null, true); //changed requestedResults to currentFile
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

        int max_files = 10; //temporary (remove later)
        int filesSoFar = 0;
        for (File fileName : files) {

            System.out.println(fileName);
            try {
                if(filesSoFar >= 10)
                {
                    //System.out.println("Reached max_files of " + max_files + "...terminating program");
                } else
                {
                    File pretty = FileManager.makePretty(fileName);
                    getInfo(request, pretty, name);
                    MatchManager.printMatchInfo(pretty); //added 9/18
                    filesSoFar++;
                    FileUtils.writeStringToFile(requestHistory, "\t" + fileName.getAbsolutePath(), (Charset) null, true); //changed requestedResults to currentFile //added 9/18
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(request == 6)
        {
            System.out.println("REQUEST = 6");
            System.out.println("mapsPlayed.size() : " + mapsPlayed.size());
            MapManager.printMapNames();
        }

        String response = "y";
        System.out.println("Any other requests? (y/n)");
        response = input.next();

        if(response.equalsIgnoreCase("Y"))
        {
            psuedoMain(input); //Caution: this causes a problem with having multiple scanners open...
        }
        System.out.println("Shutting down program.");
    }

    //Call the appropriate class(es) and method(s) based on user input
    public static boolean getInfo(int request, File prettyFile, String nameIfNeeded) //changed return from void -> boolean 5/17/2022
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

        }else if(request == 4) { //NOT WORKING (issue with scanner) -->
            // NOW switched... but still lots of extra printouts, and passing name in seems silly
            Ranking.ranking(nameIfNeeded, prettyFile); //seems to work (ALMOST --> getting null errors) //INTERESTING: new request, asks for name with every file...

        }else if(request == 5) {
            //NOT WORKING
            KillCountsJSON.calculateKillCountsJSON(prettyFile);
            //having trouble accessing names of the winners specifically

        }else if(request == 6) {
            //getMaps
            //Vector<String> mapNames = new Vector<String>(); //this is getting re-made with every file...
            //if(prettyFile == null)
            //{
            //    System.out.println("FILE IS NULL");
            //6}
            String name = MatchManager.getMapName(prettyFile);
            System.out.println("map name: " + name);
            //if(name != null) //Tried to fix EOF exception with this but it didn't work (which makes sense, I guess)
            //{
                //mapNames.add(getMapName(prettyFile));
               // System.out.println("Attempting to add: " + name + " to mapsPlayed...");
            //    mapsPlayed.add(name);
            //}

        }else
        {
            System.out.println("Invalid request"); //for example's sake (currently)
            return (request < 0 || request >= functionalities.size()); //invalid requests should not have valid numbers //changed 7 to functionalities.size()
        }
        return (request < functionalities.size() && request >= 0); //valid requests should have valid numbers
    }


    public static void printOptionsToChooseFrom(Vector<String> options, String prompt)
    {
        System.out.print(prompt);
        System.out.println(" Type the corresponding number and then press enter.\n");
        for(int i = 0; i < options.size(); i++)
        {
            System.out.println(i + ": " + options.get(i));
        }
    }

    //IN PROGRESS
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

    public static void initiateRequestScopes()
    {
        requestScopes.add("individual (EX: matt112)");
        requestScopes.add("team       (EX: team of matt112)");
        requestScopes.add("match      (all individuals in that match)");
    }

    //If requests are objects, then this is easier...?
    //abstract these better...!
    //write to file (requestHistory) here? 9/15
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

    public static int getRequestType(Scanner input)
    {
        String prompt_requestType = "What would you like to know?";
        printOptionsToChooseFrom(functionalities, prompt_requestType);
        int requestType = getInput(input, functionalities);
        return requestType;
    }

    public static int getRequestScope(Scanner input)
    {
        String prompt_requestScope = "WHO are we learning about?"; //Alternative: putting prompt as index 0
        printOptionsToChooseFrom(requestScopes, prompt_requestScope);
        int requestScope = getInput(input, requestScopes);
        //String[] requestScopeOptions = {"individual (EX: matt112)", "team (EX: team of matt112)", "match (everyone in that match)"};
        return requestScope;

    }


    //"Do you want to store this information in its own file?" idea
    //Could just automatically store it, then only keep at the end if user says to keep it (at the end...) --> both more and less work
    //IN PROGRESS
    public static Vector<String> mapsPlayed = new Vector<String>();
    public static Vector<String> functionalities = new Vector<String>(); //call it options instead ("functionalities" could be like the method calls) //not public?
    public static Vector<String> requestScopes = new Vector<String>(); //added 9/17

    public static Request requestCurrent;// = new Request(0,0);

    public static void main(String[] args) //maybe put the "while" in here to having multiple requests actually works?
    {

        mapsPlayed.clear(); //clear at the beginning

        initiateFunctionalities();
        initiateRequestScopes(); //added 9/17

        requestHistory = FileManager.getFile("C:\\Users\\jmast\\pubg_requestHistory");
        currentFile = FileManager.getFile("C:\\Users\\jmast\\sampleFile"); //added 9/15

        Scanner input = new Scanner(System.in);
        psuedoMain(input);
        input.close();

        /*
        delete fileManager;
        delete botCounter;
        delete doRequest;
        delete JSONManager = new JSONManager();
        delete killCounts;
        delete killCountsJSON;
        delete mapManager;
        delete matchManager;
         */

    }

}
//Was right above main:
//public static Request dummyRequest;// = new Request(0,0);

//Was in main:
//String[] types = dummyRequest.getTypes();
//Vector<String> winnersRecorded; //winners across different games




//For each file, there is a call to the corresponding request

//VERY IN PROGRESS
//public static final FileManager fileManager = new FileManager();
//public static final BotCounts botCounter = new BotCounts();
//public static final DoRequest doRequest = new DoRequest();
//public static final JSONManager JSONManager = new JSONManager();
//public static final KillCounts killCounts = new KillCounts();
//public static final KillCountsJSON killCountsJSON = new KillCountsJSON();
//public static final MapManager mapManager = new MapManager();
//public static final MatchManager matchManager = new MatchManager();


//Make additional class for handling/executing the request?


//First, store the file somewhere.
//Second, store information that is likely to be asked for
//That way, if likely info is asked for, it can be foudn quickly. If unlikely info is asked for, it can still be found (albeit more slowly, looking through the files)
//Is that even worth it?

//How do you store stats across games/time?



//What if you had a map of players
//Once you found a player, you added them to the map
//Then, if you encounter that player again, you can just look in the map instead of having to search for their basic information again
//Basic information: player name, id,
//Could store history: weapon usage, teammates, killcount avg, rankings, (numGamesRecorded), etc.
