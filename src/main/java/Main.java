import org.apache.commons.io.FileUtils;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

//The Main class serves as a driver for the PubgAPI-Java project.
//As of 10/5: being restructured.
public class Main extends Request {

    static File currentFile = null; //added 9/15
    static File requestHistory = null;

    public static Request requestCurrent;// = new Request(0,0);
    public static Vector<String> mapsPlayed = new Vector<String>();

    public static Vector<String> functionalities = new Vector<String>(); //call it options instead ("functionalities" could be like the method calls) //not public?

    //public static Vector<String> functionalities = requestCurrent.getTypesOfRequests();
    //public static Vector<String> requestScopes = new Vector<String>(); //added 9/17


    //TO-DO: allow customizable location for requestHistory
    /*
    public static void setPathForRequestHistory(Scanner input)
    {
        System.out.println("Please enter fi")
        System.out.println("Where would you like your requestHistory to be stored?");
        System.out.println("0: default location --> 'C:\\\\Users\\\\jmast\\\\pubg_requestHistory\"");
        System.out.println("1: custom location");
        int requestHistoryStorage = input.nextInt();
    }
     */

    //Conducts setup (initialize files, create Scanner, etc.) so that pseudoMain can do the brunt of the work.
    //TO-DO: Use FileManager.java to incorporate specific file activation/inactivation.
    public static void main(String[] args)
    {
        //try {
         //   APIManager.workingWithSampleFile();
        //    //APIManager.getMatchIDsFromSampleFile();
        //} catch (IOException e) {
        ////    e.printStackTrace();
        //}

        mapsPlayed.clear(); //clear at the beginning
        initiateFunctionalities();
        //
        //initiateRequestScopes(); //added 9/17

        requestHistory = FileManager.getFile("C:\\Users\\jmast\\pubg_requestHistory");
        currentFile = FileManager.getFile("C:\\Users\\jmast\\sampleFile"); //added 9/15

        Scanner input = new Scanner(System.in);
        //setPathForRequestHistory(input);
        psuedoMain(input);
        input.close();
    }

    //Allows for multiple requests in a single running of the program.
    public static void psuedoMain(Scanner input)//removed "String desiredThing"
    {
        File[] files = new File("C:\\Users\\jmast\\pubgFilesExtracted").listFiles(); //Let user decide, though?

        mapsPlayed.clear(); //avoid duplicates...

        input = new Scanner(System.in);

        int request = getRequestType(input); //(requestType)
        ///int requestScope = getRequestScope(input);
        //Request r = new Request(request, requestScope);
        int dummyForNow = 0;
        int requestScope = dummyForNow;
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
        for (File fileName : files)
        {
            System.out.println(fileName);
            try {
                if(filesSoFar >= 10)
                {
                    //System.out.println("Reached max_files of " + max_files + "...terminating program");
                } else
                {
                    if(!fileName.isDirectory()) //added 10/5
                    {
                        File pretty = FileManager.makePretty(fileName);
                        getInfo(request, pretty, name);
                        MatchManager.printMatchInfo(pretty); //added 9/18
                        filesSoFar++;
                        FileUtils.writeStringToFile(requestHistory, "\t" + fileName.getAbsolutePath(), (Charset) null, true); //changed requestedResults to currentFile //added 9/18
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(request == 6)
        {
            System.out.println("REQUEST = 6");
            System.out.println("mapsPlayed.size() : " + mapsPlayed.size());
            //mapsPlayed.add(MapManager.getMapName(fileName));
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
            psuedoMain(input); //Caution: this causes a problem with having multiple scanners open...
        }
        System.out.println("Shutting down program.");
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

    /*
    //Not yet fully implemented
    public static void initiateRequestScopes()
    {
        requestScopes.add("individual (EX: matt112)");
        requestScopes.add("team       (EX: team of matt112)");
        requestScopes.add("match      (all individuals in that match)");
    }
    */

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

    //Returns an integer from [0, functionalities.size()-1] representing a specific type of request/functionality.
    public static int getRequestType(Scanner input)
    {
        String prompt_requestType = "What would you like to know?";
        printOptionsToChooseFrom(functionalities, prompt_requestType);
        int requestType = getInput(input, functionalities);
        return requestType;
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
}

//NOTES:
//-Check for occasional eof error.
//-Check to make sure request 6 (maps played) is still working properly
//-Possibly make the default storage location for active files --> .../main/resources
//-File storage --> ask user if/where. Could automatically store it, then only keep at the end if user says to keep it
// (at the end...) --> both more and less work
