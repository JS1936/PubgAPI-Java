import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Request {
    String[] types = {"countBotsAndPeople", "calculateKillCounts", "printPlayersByTeam", "winnerWeapons", "ranking", "calculateKillCountsJSON", "mapsPlayed"};
    String[] scopes = {"individual", "team", "everyone"};

    private String requestID;
    private String requestType; //changed from String to int (then back to String)
    private String requestScope;
    private File requestResults_File;
    private boolean requestUsesJSON;

    //Default: individual scope, using JSON (if possible)?

    Request()
    {
        System.out.println("Default request object");
    }
    Request(int type, int scope)
    {
        //fix this
    }
    //For getting the date: https://stackabuse.com/how-to-get-current-date-and-time-in-java/
    Request(int type, int scope, boolean useJSON)
    {
        System.out.println("Non-default request object");
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat desiredFormat = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        requestScope = scopes[scope];
        requestType = types[type];
        requestUsesJSON = useJSON;
        requestID = desiredFormat.format(date) + "_" + requestScope + "_" + requestType + "_" + "usesJSON";
        if(!requestUsesJSON)
        {
            requestID += "!";
        }

    }


}
/*
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
 */