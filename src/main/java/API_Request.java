import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
//import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Vector;


//
public class API_Request extends API {

    private String player = "<playerName>";         //  player-chosen name (EX: JS1936)
    private HttpURLConnection connection = null;    //

    private File specificRequest;                   //
    private URL recentMatches;                      //
    private File match_list;                        //
    private long timestamp;                         // time of initial request
    private int matchLimit;                         // maximum number of matches analyzed per request
    //private File responseFile;// = null;

    public String getPlayer() { return this.player; }
    public HttpURLConnection getConnection() { return this.connection; }
    public long getTimestamp() { return this.timestamp; }
    public int getMatchLimit() { return this.matchLimit; };


    //If no limit is specified on the number of matches to look at, then the default is 5.
    public API_Request(String player) throws IOException {  this(player, 5);    }

    //In-progress
    // public API_Request(Preset preset) throws IOException {
    //     System.out.println("Creating a preset-based API_request");
    //     initializeSpecificRequest();
    //     //this.matchLimit =...
    //     this.match_list = (Files.createDirectory(Path.of(specificRequest + "/matches")).toFile());
    //     //
    // }
    //public void doCustomRequestPreset()
    //{

    //}

    //Create/save a specificRequest based on the established player and timestamp.
    private void initializeSpecificRequest()
    {
        this.timestamp = System.currentTimeMillis();
        this.specificRequest = new File("requestsDir/" + this.player + "/timestamp_" + this.timestamp);
        if(!specificRequest.exists())
        {
            System.out.println("specific request Does not yet exist");
            specificRequest.mkdirs();
            specificRequest.getParentFile().mkdirs();
        }
    }

    public API_Request(String player, int matchLimit) throws IOException {

        System.out.println("Creating an API_Request about player: " + player);

        this.player = player;
        this.matchLimit = matchLimit; //default is 5 unless specified
        this.recentMatches = new URL("https://api.pubg.com/shards/steam/players?filter[playerNames]=" + this.player);

        initializeSpecificRequest(); //added 12/28/2022

        //create "matches" subdirectory for timestamp
        this.match_list =  (Files.createDirectory(Path.of(specificRequest + "/matches")).toFile());

        doRequest();
    }

    private File getMatchOverviewContent(String match_id) throws IOException {
        //Match Overview
        URL oneMatch_ = new URL("https://api.pubg.com/shards/steam/matches/" + match_id);
        Path match_Path = Path.of(specificRequest + "/matches/match_id_" + match_id);

        connectToAPI(oneMatch_);

        File ugly = storeResponseToSpecifiedFileLocation(match_Path.toString()); //save
        File pretty = FileManager.makePretty(ugly);
        return pretty;
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
    private void doRequest() throws IOException {
        //connect
        connectToAPI(this.recentMatches);
        Path summary_Path = Path.of(specificRequest + "/summary_matchList"); //no .txt here (don't want duplicate)

        //save/create summary file
        storeResponseToSpecifiedFileLocation(summary_Path.toString());
        File summary_File = new File(summary_Path.toString() + ".json");



        //use summary to get recent match_ids. They are then formatted to be more visually user-friendly.
        Vector<String> match_ids = getMatchIDsFromRequestPath(summary_File);
        Vector<URL> telemetry_urls = new Vector<URL>();
        int numMatches = 0;
        for(String match_id : match_ids) {

            if(numMatches >= this.matchLimit) { break; }

            //Match Overview
            File pretty = getMatchOverviewContent(match_id);
            pretty.deleteOnExit(); //revisit


            //Match Telemetry
            URL telemetryURL = getTelemetryURL(pretty); //changed to ugly from pretty (back to pretty)
            Path telemetry_Path = Path.of(specificRequest + "/matches/telemetry-for-match_id_" + match_id);
            File newFile = new File(telemetry_Path.toString());
            if(!newFile.exists()) {
                FileManager.createNewFileAndParentFilesIfTheyDoNotExist(newFile);
            }
            File newFile2 = FileManager.makePretty(newFile);
            connectToAPI_wantZIP(telemetryURL, newFile2);

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
    //Guide: https://www.tutorialspoint.com/how-can-we-read-a-json-file-in-java
    private URL getTelemetryURL(File f) throws IOException {

        String fileAsString = FileUtils.readFileToString(f);

        int index = fileAsString.indexOf("https://telemetry-");
        String https = fileAsString.substring(index, index + 119);
        System.out.println("telemetry URL is " + https);

        URL telemetryURL = new URL(https);

        return telemetryURL;
    }

    //Given a URL, checks the current response code for the HttpURLConnection connection.
    //Expect: response code is 200 (valid/OK). Otherwise, invalid connection.
    //        If invalid connection, exit with status 0.
    private void printResponseCodeSuccessFail(URL url) throws IOException {
        System.out.println("Response code: " + this.connection.getResponseCode()); //expect: 200
        if(this.connection.getResponseCode() == 200) //response is valid/OK
        {
            System.out.println("Connection made. URL: " + url.toString());
        }
        else
        {
            System.out.println("Error: connection to api has invalid response");
            System.exit(0); // Added 06/22/25.
        }
    }

    private void endProgramIfNullConnection()
    {
        if(connection == null) //!isConnected
        {
            System.out.println("Error: connection is null");
            System.exit(0);
        }
    }
    private HttpURLConnection connectToAPI(URL url) throws IOException {

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
    private void transferInputUsingProcessBuilder(URL url, File destFile) throws IOException {

        //Command format (curl)
        String command = "curl --compressed " + url + " -H Accept: application/vnd.api+json";
        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
        Process process = processBuilder.start();

        //Get the input
        InputStream inputStream = process.getInputStream();

        //Transfer the input
        transferInputStreamToFile(inputStream, destFile);

        process.destroy();
    }


    private HttpURLConnection connectToAPI_wantZIP(URL url, File destFile) throws IOException {
        //System.out.println("CONTENT ENCODING: " + connection.getContentEncoding());
        this.connection = (HttpURLConnection) url.openConnection();
        endProgramIfNullConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization","Bearer " + this.getAPIkey());
        connection.setRequestProperty("Accept", "application/vnd.api+json");
        connection.setRequestProperty("Content-Type", "UTF-8" + "; charset=utf-8"); //added 12/15 (change to utf-8?)
        connection.setRequestProperty("Accept", "gzip"); //added 11/29

        System.out.println("Response code: " + this.connection.getResponseCode()); //expect: 200
        if(this.connection.getResponseCode() == 200) //response is valid/OK
        {
            System.out.println("Connection made. URL: " + url.toString());
            transferInputUsingProcessBuilder(url, destFile); //Credit: https://www.baeldung.com/java-curl.
            FileManager.makePretty(destFile); //seems to work

        }
        else
        {
            System.out.println("Error: connection to api has invalid response");
        }

        return this.connection;

    }

    //Pre: File file exists and is not null.
    //Transfers desired content from input stream into given file.
    private void transferInputStreamToFile(InputStream inputStream, File file) throws IOException
    {
        OutputStream output = new FileOutputStream(file);
        inputStream.transferTo(output);
        inputStream.close();
        output.close();
        //file.deleteOnExit(); //revisit
    }

    //Consider: returning file so that it can be custom-saved
    private File storeResponseToSpecifiedFileLocation(String dstPath) throws IOException {
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
    private Vector<String> getMatchIDsFromRequestPath(File s) throws IOException
    {
        if(!s.isFile())
        {
            System.out.println("Error: file " + s.getAbsolutePath() + " should be a file but is not a file.");
            System.exit(0);
        }
        System.out.println("Attempting to get match ids from request file");
        File s_pretty = FileManager.makePretty(s);

        System.out.println("Pretty path: " + s_pretty.getAbsolutePath());

        String fileAsString = FileManager.storeFileAsString(s_pretty);
        System.out.println(fileAsString);

        JSONObject jsonObject = new JSONObject(fileAsString);
        JSONArray jsonArrayOfMatches = jsonObject.getJSONArray("data").getJSONObject(0).getJSONObject("relationships").getJSONObject("matches").getJSONArray("data");
        System.out.println("jsonArray.length = " + jsonArrayOfMatches.length());
        Vector<String> match_ids = new Vector<String>();

        for(int i = 0; i < jsonArrayOfMatches.length(); i++)
        {
            JSONObject id = jsonArrayOfMatches.getJSONObject(i);
            String match_id = id.get("id").toString();
            //System.out.println("match_id = " + match_id);

            match_ids.add(match_id);
        }
        System.out.println("match_ids.size() = " + match_ids.size());
        return match_ids;
    }


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

//Would need a way to tell the file history how many (/which) matches
//-->could adjust summary_Path

//Note: could check how many requests  there already are about the player...

/////