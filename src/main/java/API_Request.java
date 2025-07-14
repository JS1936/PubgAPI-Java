import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Vector;

/*
 * The API_Request class lets users connect to the pubg API and collect data from it.
 * 
 * Future considerations:
 *  - Refactor doRequest()
 *  - Decide whether to keep doRequest() summmary file or deleteOnExit
 *  - Update to no longer use deprecated resources
 */
public class API_Request extends API {

    private String player = "<playerName>";         //  player-chosen name (EX: JS1936)
    private HttpURLConnection connection = null;    //  connection to pubg API

    private File specificRequest;                   //  EX: ../requestsDir/<playerName>/timestamp_<timestamp>
    private URL recentMatches;                      //  EX: "https://api.pubg.com/shards/<platform>/players?filter[playerNames]=<player>"
    private long timestamp;                         // time of initial request
    private int matchLimit;                         // maximum number of matches analyzed per request

    public String getPlayer() { return this.player; }
    public HttpURLConnection getConnection() { return this.connection; }
    public long getTimestamp() { return this.timestamp; }
    public int getMatchLimit() { return this.matchLimit; };


    /*
     * If no limit is specified on the number of matches to look at, then the default is 5.
     */
    public API_Request(String player) throws IOException {  this(player, 5);    }

    /*
     * Create/save a specificRequest based on the established player and timestamp.
     */
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

    /*
     * Given a player name and a maximum number of matches to collect data on, gathers up to "matchLimit" number of 
     * match telemetry files and stores them at "../requestsDir/<player>/<timestamp>/matches"
     * Expect: player exists and has played recently (within the last 2 weeks)
     */
    public API_Request(String player, int matchLimit) throws IOException {

        System.out.println("Creating an API_Request about player: " + player);

        this.player = player;
        this.matchLimit = matchLimit; //default is 5 unless specified
        this.recentMatches = new URL("https://api.pubg.com/shards/" + getAPIplatform() + 
                                    "/players?filter[playerNames]=" + this.player);

        initializeSpecificRequest();

        //create "matches" subdirectory for timestamp
        Files.createDirectory(Path.of(specificRequest + "/matches")).toFile();

        doRequest();
    }

    /*
     * Returns a file holding formatted / "pretty" telemetry corresponding to the match with the given match id.
     */
    private File getMatchOverviewContent(String match_id) throws IOException {
        //Match Overview
        URL oneMatch_ = new URL("https://api.pubg.com/shards/" + getAPIplatform() + "/matches/" + match_id);
        Path match_Path = Path.of(specificRequest + "/matches/match_id_" + match_id);

        connectToAPI(oneMatch_);

        File ugly = storeResponseToSpecifiedFileLocation(match_Path.toString()); //save
        File pretty = FileManager.makePretty(ugly);
        return pretty;
    }
    
    /*
     * Pre: "matches" subdirectory for the desired player exists
     * Adds up to this.mathLimit number of match files into the "matches" directory for the desired player.
     * - Uses summary file to get recent match ids. 
     * - For each match id, gets, prettifies, and stores corresponding match data (match overview and match telemetry)
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
        int numMatches = 0;
        for(String match_id : match_ids) {

            if(numMatches >= this.matchLimit) { break; }

            //Match Overview
            File pretty = getMatchOverviewContent(match_id);
            pretty.deleteOnExit();

            //Match Telemetry
            URL telemetryURL = getTelemetryURL(pretty); //changed to ugly from pretty (back to pretty)
            Path telemetry_Path = Path.of(specificRequest + "/matches/telemetry-for-match_id_" + match_id);
            File newFile = new File(telemetry_Path.toString());
            if(!newFile.exists()) {
                FileManager.createNewFileAndParentFilesIfTheyDoNotExist(newFile);
            }
            File newFile2 = FileManager.makePretty(newFile);
            connectToAPI_wantZIP(telemetryURL, newFile2);

            numMatches++;
        }
    }

    /*
     * Guide: https://www.tutorialspoint.com/how-can-we-read-a-json-file-in-java
     * Returns the telemetry url for a specific match.
     */
    private URL getTelemetryURL(File f) throws IOException {
        String fileAsString = FileUtils.readFileToString(f);
        int index = fileAsString.indexOf("https://telemetry-");
        String https = fileAsString.substring(index, index + 119);
        System.out.println("telemetry URL is " + https);
        URL telemetryURL = new URL(https);
        return telemetryURL;
    }

    /*
     * Given a URL, checks the current response code for the HttpURLConnection connection.
     * Expect: response code is 200 (valid/OK). Otherwise, invalid connection.
     *         If invalid connection, exit with status 0
     */
    private void printResponseCodeSuccessFail(URL url) throws IOException {
        System.out.println("Response code: " + this.connection.getResponseCode()); //expect: 200
        if(this.connection.getResponseCode() == 200) { //response is valid/OK
            System.out.println("Connection made. URL: " + url.toString());
        } else {
            System.out.println("Error: connection to api has invalid response");
            System.exit(0);
        }
    }

    /*
     * Ends the program if null connection.
     */
    private void endProgramIfNullConnection() {
        if(connection == null) {
            System.out.println("Error: connection is null");
            System.exit(0);
        }
    }

    /*
     * Returns a connection to the pubg API.
     */
    private HttpURLConnection connectToAPI(URL url) throws IOException {

        this.connection = (HttpURLConnection) url.openConnection();

        endProgramIfNullConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization","Bearer " + this.getAPIkey());
        connection.setRequestProperty("Accept", "application/vnd.api+json");

        printResponseCodeSuccessFail(url);
        return this.connection;
    }

    /* This method was created following/using
     * SOURCE: https://www.baeldung.com/java-curl.
     * Includes minor modifications from original source.
     * 
     * Assumes: url is valid and destFile exists.
     */
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

    /*
     * Connect to pubg API and attempt to store gzip data to destFile.
     * If connection successful, prints success message and saves contents of gzip to destFile.
     * If connection null, ends program. 
     * If connection otherwise fails, prints error message.
     * Returns the connection as an HttpURLConnection.
     */
    private HttpURLConnection connectToAPI_wantZIP(URL url, File destFile) throws IOException {
        //System.out.println("CONTENT ENCODING: " + connection.getContentEncoding());
        this.connection = (HttpURLConnection) url.openConnection();
        endProgramIfNullConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization","Bearer " + this.getAPIkey());
        connection.setRequestProperty("Accept", "application/vnd.api+json");
        connection.setRequestProperty("Content-Type", "UTF-8" + "; charset=utf-8");
        connection.setRequestProperty("Accept", "gzip");

        System.out.println("Response code: " + this.connection.getResponseCode()); //expect: 200
        if(this.connection.getResponseCode() == 200) //response is valid/OK
        {
            System.out.println("Connection made. URL: " + url.toString());
            transferInputUsingProcessBuilder(url, destFile); //Credit: https://www.baeldung.com/java-curl.
            FileManager.makePretty(destFile);
        }
        else
        {
            System.out.println("Error: connection to api has invalid response");
        }
        return this.connection;
    }

    /*
     * Pre: File file exists and is not null.
     * Transfers desired content from input stream into given file.
     */
    private void transferInputStreamToFile(InputStream inputStream, File file) throws IOException
    {
        OutputStream output = new FileOutputStream(file);
        inputStream.transferTo(output);
        inputStream.close();
        output.close();
    }

    /* 
     * Given a String destination path, stores one match's telemetry data at that location in a file.
     * Creates a new file and new directories as needed.
     * Returns file holding one match's telemetry data.
     * Note: Returning the file could be used to allow custom save.
     */
    private File storeResponseToSpecifiedFileLocation(String dstPath) throws IOException {
        System.out.println("dstPath = " + dstPath);

        InputStream inputStream = connection.getInputStream();
        //System.out.println("input stream:" + inputStream.toString()); //Does NOT print content

        File responseFile = new File(dstPath + ".json");
        FileManager.createNewFileAndParentFilesIfTheyDoNotExist(responseFile);
        transferInputStreamToFile(inputStream, responseFile);

        return responseFile;
    }

    /*
     * Given a summary_matchList.json file, gathers the match ids it mentions.
     * Returns a Vector<String> where each String is a match id gathered,
     */
    private Vector<String> getMatchIDsFromRequestPath(File s) throws IOException
    {
        if(!s.isFile())
        {
            System.out.println("Error: file " + s.getAbsolutePath() + " should be a file but is not a file.");
            System.exit(0);
        }
        System.out.println("Attempting to get match ids from request file " + s.getAbsolutePath());
        File s_pretty = FileManager.makePretty(s);

        System.out.println("Pretty path: " + s_pretty.getAbsolutePath());

        String fileAsString = FileManager.storeFileAsString(s_pretty);
        //System.out.println(fileAsString);

        JSONObject jsonObject = new JSONObject(fileAsString);
        JSONArray jsonArrayOfMatches = jsonObject.getJSONArray("data").getJSONObject(0).getJSONObject("relationships").getJSONObject("matches").getJSONArray("data");
        //System.out.println("jsonArray.length = " + jsonArrayOfMatches.length());
        
        //Store match ids in vector "match_ids"
        Vector<String> match_ids = new Vector<String>();
        for(int i = 0; i < jsonArrayOfMatches.length(); i++)
        {
            JSONObject id = jsonArrayOfMatches.getJSONObject(i);
            String match_id = id.get("id").toString();
            match_ids.add(match_id);
        }
        System.out.println("match_ids.size() = " + match_ids.size());
        return match_ids;
    }
}