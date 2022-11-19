import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

//The Request class represents a request about a pubg file (or files) made by the user.
//TO-DO: restructure/rewrite into more clear/efficient/intuitive format.
public class Request {
/*
    //Vector<String> typesOfRequests = new Vector<String>();//{"countBotsAndPeople};
    String[] types = {"countBotsAndPeople", "calculateKillCounts", "printPlayersByTeam", "winnerWeapons", "ranking", "calculateKillCountsJSON", "mapsPlayed"};
    String[] scopes = {"individual", "team", "everyone"};

    private String requestID;
    private int requestType; //changed from String to int (then back to String) ...back to int
    private int requestScope;
    private File requestResults_File = null; //for now
    private boolean requestUsesJSON;

    public Request getRequest()
    {
        return this;
    }
    public String[] getTypes()
    {
        return this.types;
    }
    //public Vector<String> getTypesOfRequests()
    //{
    //    return this.typesOfRequests;
    //}
    public String[] getScopes()
    {
        return this.scopes;
    }
    //make the naming more clear...
    public int getRequest_type()
    {
        return requestType;
    }
    public int getRequest_scope()
    {
        return requestScope;
    }
    //Default: individual scope, using JSON (if possible)?

    Request()
    {
        System.out.println("Default request object");
    }
    Request(int type, int scope)
    {
        System.out.println("Creating a request of type: " + types[type] + ", scope: " + scopes[scope]);
        requestID = "temporary requestID";
        requestType = type;
        requestScope = scope;
        //typesOfRequests;
        //fix this
    }

    /*
    public static void initiatePossibleTypesOfRequests()
    {
        typesOfRequests.add("countBotsAndPeople");
        typesOfRequests.add("calculateKillCounts");
        typesOfRequests.add("printPlayersByTeam");
        typesOfRequests.add("winnerWeapons");
        functionalities.add("ranking (of a specific person)");
        functionalities.add("calculateKillCountsJSON");
        functionalities.add("printMapsPlayed");
    }
    */
    /*
    //For getting the date: https://stackabuse.com/how-to-get-current-date-and-time-in-java/
    Request(int type, int scope, boolean useJSON)
    {
        System.out.println("Non-default request object");
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat desiredFormat = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        //requestScope = scopes[scope];
        //requestType = types[type];
        requestUsesJSON = useJSON;
        requestID = desiredFormat.format(date) + "_" + requestScope + "_" + requestType + "_" + "usesJSON";
        if(!requestUsesJSON)
        {
            requestID += "!";
        }

    }
    */



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


//NOTES:
//countBotsAndPeople --> match
//calculateKillCoutns --> individual, team, or match
//printPlayersByTeam --> team, match
//ranking --> individual, team, match   *Note: all members of the same team end up getting the same ranking, right?
//winnerWeapons --> match
//calculateKillCountsJSON --> individual, team, or match
//mapsPlayed --> individual, team, or match


//What would it mean to count bots and people...?
//SCOPE:
//  -individual:    Of the given match(es), how many bots and people has Person X encountered?
//                          EX: Match 1 had 5 bots of 95 total; Match 2 had 10 bots of 60 total; Match 3 had 2 bots of 99 total.
//                              Cumulative result: (5 + 10 + 2) / (95 + 60 + 99) = 17 bots out of 254 total.
//                                      //How is this information going to be stored? Does it *need* to be stored?
//  -team      :    Of the given match(es), how many bots and people has this TEAM played against?
//                          -> How is "team" being defined? EX: group of exactly (or more?) the same people VS any team Person X participates in/joins
//                              EX: 2person team playing squads, 2person team having 1 extra person, etc.
//  -match     :    Of the given match(es), how many bots and people existed?
//                          -->
//Very much the same math. Just redefining which matches to examine.            //Search parameter
//Individual : Is Person X in that match? If so, look at it.                        Person  X
//Team       : Is Team X in that match? If so, look at it.                          Team    X (EX: Person X AND Person Y AND Person Z)
//Match      : Does the user want data for this match? If so, look at it.           matchID X
//
// Database?
//Pass the request aroudn instead of the input stuff?

//Want user to be able to put in a match id (or a directory that holds many match ids, for instance...?)
//Customize the API call thing (to user's request details) EX: looking for Person X info vs Person Y
//EX: Request r = new KillCounts?
//going to need a name...?