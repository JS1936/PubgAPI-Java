import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Vector;


//custom request option? (For matches)
//wait, acquiring vs using...
//add "uses" diagram? Domain diagram?

//Consider: incorporating request history here
public class API_Request extends API {

    private String player = "<playerName>";
    private HttpURLConnection connection = null;
    //private File responseFile;// = null;
    private File specificRequest;
    private URL recentMatches;// = null;// = "https://api.pubg.com/shards/steam/players?filter[playerNames]="; //needs getPlayer
    private File match_list;
    private long timestamp;
    //private URL oneMatch;// = null;
    //private boolean isValidRequest = false;
    //private boolean isConnected = false;

    public String getPlayer() { return this.player; }
    public HttpURLConnection getConnection() { return this.connection; }
    public long getTimestamp() { return this.timestamp; }

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
    public API_Request(String player) throws IOException {

        System.out.println("Creating an API_Request about player: " + player);

        this.player = player;
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

        //connect
        connectToAPI(this.recentMatches);
        Path summary_Path = Path.of(specificRequest + "/summary_matchList"); //no .txt here (don't want duplicate)

        //save
        storeResponseToSpecifiedFileLocation(summary_Path.toString());
        //TODO - test .json vs .txt
        //File summary_File = new File(summary_Path.toString() + ".txt");
        File summary_File = new File(summary_Path.toString() + ".json");
        //if(f.isFile())
        //{
        //    System.out.println("file f exists...");
        //}

        //create "matches" subdirectory for timestamp
        this.match_list =  (Files.createDirectory(Path.of(specificRequest + "/matches")).toFile());

        //use summary to get recent match_ids. They are then formatted to be more visually user-friendly.
        Vector<String> match_ids = getMatchIDsFromRequestPath(summary_File);
        int numMatches = 0;
        for(String match_id : match_ids) {
            URL oneMatch_ = new URL("https://api.pubg.com/shards/steam/matches/" + match_id);
            connectToAPI(oneMatch_);
            Path match_Path = Path.of(specificRequest + "/matches/match_id_" + match_id);
            File ugly = storeResponseToSpecifiedFileLocation(match_Path.toString()); //save

            //then do the thing

            File pretty = ugly;//FileManager.makePretty(ugly); //check this
            URL oneMatch_telemetryURL_ = getTelemetryURL(pretty);
            connectToAPI(oneMatch_telemetryURL_);
            Path telemetry_Path = Path.of(specificRequest + "/matches_telemetry/telemetry_for_match_id_" + match_id);

            File ugly_telemetry = storeResponseToSpecifiedFileLocation(telemetry_Path.toString());
            //String telemetryIndicator = "-telemetry";


            //Path telemetry_Path = Path.of(specificRequest + "/matches_telemetry/telemetry_for_match_id_" + match_id);
            System.out.println("telemetry path: " + telemetry_Path.toString());
            String ugly_telemetry_as_string = FileManager.storeFileAsString(ugly_telemetry);
            System.out.println(oneMatch_telemetryURL_.openConnection());
            //System.out.println(ugly_telemetry_as_string);


            //File telemetry = new File(telemetry_Path.toString());
            //FileUtils.copyURLToFile(oneMatch_telemetryURL_, telemetry);
            //System.out.println("AFTER copy url to file");
            //String telemetryFileToString = FileManager.storeFileAsString(telemetry);
            //System.out.println("telemetry file to string: " + telemetryFileToString);
           // File telemetry_Path_ugly = storeResponseToSpecifiedFileLocation(telemetry_Path.toString());
            //System.out.println("telemetry file: " + telemetry_Path_ugly);

            /*
            File uglyTelemetry = telemetry_Path.toFile();
            if(uglyTelemetry.isFile())
            {
                System.out.println("ugly telemetry location: " + uglyTelemetry.getAbsolutePath());
            }
            else
            {
                System.out.println("uglyTelemetry is not yet a file. Creating it now.");
                uglyTelemetry.mkdirs();
            }
            */
            //File uglyTelemetry = storeResponseToSpecifiedFileLocation(telemetry_Path.toString());
            //JSONObject jsonobject = new JSONObject(pretty.toString()); //check this
            //System.out.println("LOOKL " + jsonobject);
        }
        //getNumMatches();
        ///getMatches();
    }

    //Guide: https://www.tutorialspoint.com/how-can-we-read-a-json-file-in-java
    public static URL getTelemetryURL(File f) throws IOException {
        System.out.println("Attempting to get the telemetry URL");
        String fileAsString = FileUtils.readFileToString(f);
        //System.out.println("fileAsString: \n\n" + fileAsString);

        int index = fileAsString.indexOf("https://telemetry-");
        //System.out.println("INDEX: " + index);
        String https = fileAsString.substring(index, index + 119);
        System.out.println("telemetry URL is " + https);

        URL telemetryURL = new URL(https);
        File telemetryFile = new File("hello.json");
        FileUtils.copyURLToFile(telemetryURL, telemetryFile);

        //FileUtils.toFile(telemetryURL);
        String telemetryString = FileManager.storeFileAsString(telemetryFile);
        System.out.println("telemetry string: \n \n " + telemetryString);
        //File toFile = new File("/Users/jenniferStibbins/Documents/GitHub/PubgAPI-Java/requestsDir/" + telemetryURL.getFile());
        //System.out.println("toFile absolute path = " + toFile.getAbsolutePath());
        //if (toFile.exists()) {
        //    System.out.println("toFile exists");
        //} else {
        //    System.out.println("toFile does not exist. Creating it now.");

            //toFile.createNewFile();
            //toFile.getParentFile().mkdirs();

            //API_Request.storeResponseToSpecifiedFileLocation("Path");

        //}
        return telemetryURL;
    }

    //acquire?



    public HttpURLConnection connectToAPI(URL url) throws IOException {
        this.connection = (HttpURLConnection) url.openConnection();

        if(connection == null) //!isConnected
        {
            System.out.println("connection is null");
            System.exit(0);
        }
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization","Bearer " + this.getAPIkey());
        connection.setRequestProperty("Accept", "application/vnd.api+json");

        System.out.println("Response code: " + this.connection.getResponseCode()); //expect: 200
        if(this.connection.getResponseCode() == 200) //response is valid/OK
        {
            System.out.println("Connection made. URL: " + url.toString());
            //System.out.println("Preparing to get matches telemetry");
        }
        else
        {
            System.out.println("Error: connection to api has invalid response");
        }
        return this.connection;

    }

    //Consider: returning file so that it can be custom-saved
    public File storeResponseToSpecifiedFileLocation(String dstPath) throws IOException {
        System.out.println("dstPath = " + dstPath);
        InputStream inputStream = connection.getInputStream();
        File responseFile = new File(dstPath + ".json"); //could just make brand new file instead of using this.responseFile (remove global variable)
        //Note: changed .txt to .json (11/19/2022)
        if(responseFile.exists())
        {
            System.out.println("Response file exists!");
        }
        else
        {
            System.out.println("Response file does not exist. Creating it now");

            responseFile.getParentFile().mkdirs(); //previously order was create self, then check parent... (switched to parent, then self 11/21)
            responseFile.createNewFile();
        }

        OutputStream output = new FileOutputStream(responseFile);
        inputStream.transferTo(output);
        output.close();
        return responseFile;
    }


    //TRIAL
    //Moved from APIManager to API_Request
    public static Vector<String> getMatchIDsFromRequestPath(File s) throws IOException
    {
        if(s.isFile())
        {
            System.out.println("is file");
        }
        else
        {
            System.out.println("is not file");
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