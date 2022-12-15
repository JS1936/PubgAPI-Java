import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.internal.JsonReaderInternalAccess;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.WriterOutputStream;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONPointer;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

//
public class API_Request extends API {

    private String player = "<playerName>";
    private HttpURLConnection connection = null;

    private File specificRequest;
    private URL recentMatches;
    private File match_list;
    private long timestamp;
    //private File responseFile;// = null;

    public String getPlayer() { return this.player; }
    public HttpURLConnection getConnection() { return this.connection; }
    public long getTimestamp() { return this.timestamp; }


    //public API_Request(String player, int match_limit) { }
    //Would need a way to tell the file history how many (/which) matches
    //-->could adjust summary_Path

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

        //save/create summary file
        storeResponseToSpecifiedFileLocation(summary_Path.toString());
        File summary_File = new File(summary_Path.toString() + ".json");

        //create "matches" subdirectory for timestamp
        this.match_list =  (Files.createDirectory(Path.of(specificRequest + "/matches")).toFile());

        //use summary to get recent match_ids. They are then formatted to be more visually user-friendly.
        Vector<String> match_ids = getMatchIDsFromRequestPath(summary_File);
        Vector<URL> telemetry_urls = new Vector<URL>();
        int numMatches = 0;
        for(String match_id : match_ids) {
            if(numMatches >= 3)
            {
                break;
            }
            URL oneMatch_ = new URL("https://api.pubg.com/shards/steam/matches/" + match_id);
            connectToAPI(oneMatch_);
            Path match_Path = Path.of(specificRequest + "/matches/match_id_" + match_id);
            File ugly = storeResponseToSpecifiedFileLocation(match_Path.toString()); //save
            File pretty = FileManager.makePretty(ugly);


            URL telemetryURL = getTelemetryURL(pretty); //changed to ugly from pretty (back to pretty)
            connectToAPI(telemetryURL);


            Path telemetry_Path = Path.of(specificRequest + "/matches/telemetry-for-match_id_" + match_id);
            File newFile = new File(telemetry_Path.toString());//)//storeResponseToSpecifiedFileLocation(telemetry_Path.toString());
            //newFile.mkdirs();
            if(newFile.exists())
            {
                File newFile2 = FileManager.makePretty(newFile);
            }

            telemetry_urls.add(telemetryURL);
            numMatches++;
        }

    }

    //Guide: https://www.tutorialspoint.com/how-can-we-read-a-json-file-in-java
    public URL getTelemetryURL(File f) throws IOException {

        String fileAsString = FileUtils.readFileToString(f);

        int index = fileAsString.indexOf("https://telemetry-");     //unique...
        String https = fileAsString.substring(index, index + 119);
        System.out.println("telemetry URL is " + https);

        URL telemetryURL = new URL(https);
        connectToAPI_wantZIP(telemetryURL);

        return telemetryURL;
    }

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

    public HttpURLConnection connectToAPI(URL url) throws IOException {

        this.connection = (HttpURLConnection) url.openConnection();

        if(connection == null) //!isConnected
        {
            System.out.println("Error: connection is null");
            System.exit(0);
        }
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization","Bearer " + this.getAPIkey());
        connection.setRequestProperty("Accept", "application/vnd.api+json");

        printResponseCodeSuccessFail(url);
        return this.connection;

    }
    public HttpURLConnection connectToAPI_wantZIP(URL url) throws IOException {
        //System.out.println("CONTENT ENCODING: " + connection.getContentEncoding());
        this.connection = (HttpURLConnection) url.openConnection();

        if(connection == null) //!isConnected
        {
            System.out.println("connection is null");
            System.exit(0);
        }

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization","Bearer " + this.getAPIkey());
        connection.setRequestProperty("Accept", "application/vnd.api+json");
        connection.setRequestProperty("Content-Type", "UTF-16" + "; charset=utf-16"); //added 12/15 (change to utf-8?)
        connection.setRequestProperty("Accept", "gzip"); //added 11/29

        System.out.println("Response code: " + this.connection.getResponseCode()); //expect: 200
        if(this.connection.getResponseCode() == 200) //response is valid/OK
        {
            System.out.println("Connection made. URL: " + url.toString());

            //SOURCE: https://www.baeldung.com/java-curl //////////////////////////////
            //
            //Command format (curl)
            String command = "curl --compressed " + url + " -H Accept: application/vnd.api+json";
            //
            //Process builder to help with curl command
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            //
            //Process
            Process process = processBuilder.start();
            //
            //Input stream
            InputStream inputStream = process.getInputStream();
            //
            //Path telemetry_Path = Path.of(specificRequest + "/matches/telemetry-for-match_id_" + match_id);
            //            File newFile = new File(telemetry_Path.toString());//)
            File file = new File("/Users/jenniferstibbins/PubgAPI-Java-pubgEndOfNov/file");
            //
            //Transfer the information
            transferInputStreamToFile(inputStream, file);
            //OutputStream outputStream = new FileOutputStream(file);
            //inputStream.transferTo(outputStream);
            FileManager.makePretty(file); //seems to work
            //
            //Note: this does NOT print the content
            //System.out.println("output stream = " + outputStream.toString());
            //
            //processBuilder.command(command);
            ////////////////////////////////////////////////////////////////////////////////

        }
        else
        {
            System.out.println("Error: connection to api has invalid response");
        }

        return this.connection;

    }

    //Pre: File file exists and is not null.
    //Transfers desired content from input stream into given file.
    public void transferInputStreamToFile(InputStream inputStream, File file) throws IOException
    {
        OutputStream output = new FileOutputStream(file);
        inputStream.transferTo(output);
        inputStream.close();
        output.close();
    }

    //Consider: returning file so that it can be custom-saved
    public File storeResponseToSpecifiedFileLocation(String dstPath) throws IOException {
        System.out.println("dstPath = " + dstPath);


        InputStream inputStream = connection.getInputStream();
        System.out.println("input stream:" + inputStream.toString());

        File responseFile = new File(dstPath + ".json"); //could just make brand new file instead of using this.responseFile (remove global variable)

        //Check if file exists (and create it, if needed)
        if(responseFile.exists()) {
            System.out.println("Response file exists!");
        }
        else {
            System.out.println("Response file does not exist. Creating it now");
            if(responseFile.getParentFile() != null) {
                responseFile.getParentFile().mkdirs(); //previously order was create self, then check parent... (switched to parent, then self 11/21)
            }
            responseFile.createNewFile();
        }

        transferInputStreamToFile(inputStream, responseFile);
        //Transfer desired content into responseFile
        //OutputStream output = new FileOutputStream(responseFile);
        //inputStream.transferTo(output);
        //inputStream.close();
        //output.close();
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