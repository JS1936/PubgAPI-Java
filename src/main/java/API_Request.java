import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Vector;


//
public class API_Request extends API {

    protected String player = "<playerName>";         //  player-chosen name (EX: JS1936)
    protected HttpURLConnection connection = null;    //

    protected File specificRequest;                   //
    protected URL recentMatches;                      //
    protected File match_list;                        //
    protected long timestamp;                         // time of initial request
    protected int matchLimit;                         // maximum number of matches analyzed per request
    //private File responseFile;// = null;

    public String getPlayer() { return this.player; }
    public HttpURLConnection getConnection() { return this.connection; }
    public long getTimestamp() { return this.timestamp; }
    public int getMatchLimit() { return this.matchLimit; };


    public void initializeAPI_Request() throws IOException {
        this.player = player;
        this.matchLimit = matchLimit; //default
        this.recentMatches = new URL("https://api.pubg.com/shards/steam/players?filter[playerNames]=" + this.player);

        //initialize specificRequest
        this.timestamp = System.currentTimeMillis();
        this.specificRequest = new File("requestsDir/" + this.player + "/timestamp_" + this.timestamp);
        if(!specificRequest.exists())
        {
            System.out.println("specific request Does not yet exist");
            specificRequest.mkdirs();
            specificRequest.getParentFile().mkdirs();
        }
        //create "matches" subdirectory for timestamp
        this.match_list =  (Files.createDirectory(Path.of(specificRequest + "/matches")).toFile());
    }

    protected API_Request()
    {

    }
    //If no limit is specified on the number of matches to look at, then the default is 5.
    public API_Request(String player) throws IOException {
        this(player, 5);
    }
    //Would need a way to tell the file history how many (/which) matches
    //-->could adjust summary_Path

    //Note: could check how many requests  there already are about the player...


    public API_Request(String player, int matchLimit) throws IOException {

        System.out.println("Creating an API_Request about player: " + player);
        initializeAPI_Request();
        doRequest();
    }


    /*
    public File getTelemetryContent(String match_id, File pretty) throws IOException {
        //Match Overview
        URL telemetryURL = getTelemetryURL(pretty); //changed to ugly from pretty (back to pretty)
        Path telemetry_Path = Path.of(specificRequest + "/matches/telemetry-for-match_id_" + match_id);
        File newFile = new File(telemetry_Path.toString());
        if(!newFile.exists())
        {
            createNewFileAndParentFilesIfTheyDoNotExist(newFile);
        }
        File newFile2 = FileManager.makePretty(newFile);
        connectToAPI_wantZIP(telemetryURL, newFile2);
        telemetry_urls.add(telemetryURL);
    }
     */
    public void doRequest() throws IOException {
        //connect
        API_Request_MatchesListed matchesListed = new API_Request_MatchesListed(player, matchLimit);
        API_Request_MatchOverview matchOverview = new API_Request_MatchOverview(player, matchLimit);
        API_Request_MatchTelemetry matchTelemetry = new API_Request_MatchTelemetry(player);

        File summary_File = matchesListed.doRequest_returnFileForMatchesListed();


        //use summary to get recent match_ids. They are then formatted to be more visually user-friendly.
        Vector<String> match_ids = matchesListed.getMatchIDsFromRequestPath(summary_File);
        Vector<URL> telemetry_urls = new Vector<URL>();
        int numMatches = 0;
        for(String match_id : match_ids) {

            if(numMatches >= this.matchLimit) { break; }

            //Match Overview
            File pretty = matchOverview.getMatchOverviewContent(match_id);
            pretty.deleteOnExit(); //revisit


            //Match Telemetry
            ; //changed to ugly from pretty (back to pretty)
            URL telemetryURL = matchTelemetry.getTelemetryURL(pretty);
            matchTelemetry.getMatchTelemetryContent(telemetryURL, match_id);
            telemetry_urls.add(telemetryURL);

            numMatches++;
        }
    }
    /*
    public File getMatchFile(URL url, String match_id) throws IOException {
        URL oneMatch_ = new URL("https://api.pubg.com/shards/steam/matches/" + match_id);
        connectToAPI(oneMatch_);
        Path match_Path = Path.of(specificRequest + "/matches/match_id_" + match_id);
        File ugly = storeResponseToSpecifiedFileLocation(match_Path.toString()); //save

        return ugly;
    }
     */


    //Given a URL, checks the current response code for the HttpURLConnection connection.
    //Expect: response code is 200 (valid/OK). Otherwise, invalid connection.
    public void printResponseCodeSuccessFail(URL url) throws IOException {
        System.out.println("Response code: " + this.connection.getResponseCode()); //expect: 200
        if(this.connection.getResponseCode() == 200) //response is valid/OK
        {
            System.out.println("Connection made. URL: " + url.toString());
        }
        else
        {
            System.out.println("Error: connection to api has invalid response");
        }
    }

    public void endProgramIfNullConnection()
    {
        if(connection == null) //!isConnected
        {
            System.out.println("Error: connection is null");
            System.exit(0);
        }
    }
    public HttpURLConnection connectToAPI(URL url) throws IOException {

        this.connection = (HttpURLConnection) url.openConnection();

        endProgramIfNullConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization","Bearer " + this.getAPIkey());
        connection.setRequestProperty("Accept", "application/vnd.api+json");

        printResponseCodeSuccessFail(url);
        return this.connection;
    }

    //This method was created following/using
    //SOURCE: https://www.baeldung.com/java-curl.
    //Includes minor modifications from original source.
    //
    //Assumes: url is valid and destFile exists.



    //Pre: File file exists and is not null.
    //Transfers desired content from input stream into given file.
    public void transferInputStreamToFile(InputStream inputStream, File file) throws IOException
    {
        OutputStream output = new FileOutputStream(file);
        inputStream.transferTo(output);
        inputStream.close();
        output.close();
        //file.deleteOnExit(); //revisit
    }

    //Consider: returning file so that it can be custom-saved
    public File storeResponseToSpecifiedFileLocation(String dstPath) throws IOException {
        System.out.println("dstPath = " + dstPath);

        InputStream inputStream = connection.getInputStream();
        //System.out.println("input stream:" + inputStream.toString()); //Does NOT print content

        File responseFile = new File(dstPath + ".json");
        FileManager.createNewFileAndParentFilesIfTheyDoNotExist(responseFile);
        transferInputStreamToFile(inputStream, responseFile);

        return responseFile;
    }

    //Consider: adding a checker for whether a file is pretty already? (Or rather, is NOT pretty)

    //TRIAL
    //Moved from APIManager to API_Request
    //Note: was previously static



    //Consider: allowing custom dst
    //public void getRequest(URL url) throws IOException {


    //get_matchID
    //get_match_info

    //constructor(s)
    //copy constructor
    //destructor

    //getters
    //setters
}
//NOTES BUFFER:
//
// EX:
//get player-focused list of matches
//prettify that file
//look through that file to get the list of match_ids and store them
//for each stored match_id, request full match telemetry information
//for each stored match_id, store the full match telemetry information
//put full match telemetry information in a directory such that this program can use it...

//public static void workingWithSampleFile2() throws IOException {
//    File[] files = new File(FileManager.getAbsolutePathToActiveFolder()).listFiles();
//}



//custom request option? (For matches)
//wait, acquiring vs using...
//add "uses" diagram? Domain diagram?

//Consider: incorporating request history here

//public boolean isConnectedToAPI() { return this.isConnected; }



//Incomplete
    /*
    public void getNumMatches()
    {
        System.out.println("List match_list files: " + Arrays.toString(this.match_list.listFiles()));
        System.out.println(" match_list length: " + this.match_list.length());
        //this.match_list.listFiles();
        //requestsDir, name, timestamp, matches, how many matches
        System.out.println("num matches = ?");
    }
     */
// public void getMatches() {
//     System.out.println("GET MATCHES");
//     System.out.println(match_list.list());
//     match_list.listFiles();
// }
//getSummary? //getMatches?

//FileManager HAS makePretty(File)



//System.out.println("MATCH TELEMETRY URLS: ");
//numMatches = 0;
//for(URL match_telemetry_url : telemetry_urls) {
//    if (numMatches >= 5) {
//        break;
//    }
//    System.out.println(match_telemetry_url.toString());
//    connectToAPI(match_telemetry_url);
//}