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
        File match_telemetries_list = (Files.createDirectory(Path.of(specificRequest + "/telemeTREE!")).toFile()); //added 11/29
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
            //connection.disconnect();
            //connection.setRequestProperty("Accept-Encoding","gzip");;
            connectToAPI(telemetryURL);
            //InputStream f = connection.getInputStream();
            //System.out.println("LOOK...: + " + f.available());
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
        //System.out.println("MATCH TELEMETRY URLS: ");
        //numMatches = 0;
        //for(URL match_telemetry_url : telemetry_urls) {
        //    if (numMatches >= 5) {
        //        break;
        //    }
        //    System.out.println(match_telemetry_url.toString());
        //    connectToAPI(match_telemetry_url);
        //}
    }

    //Guide: https://www.tutorialspoint.com/how-can-we-read-a-json-file-in-java
    public URL getTelemetryURL(File f) throws IOException {
        //System.out.println(">Entering getTelemetryURL");
        //System.out.println("Attempting to get the telemetry URL for file: " + f.getName());


        String fileAsString = FileUtils.readFileToString(f);
        //File pretty_version = FileManager.makePretty(f);
        //System.out.println("Segment: " + fileAsString.substring(0,100));
        //System.out.println("fileAsString: \n\n" + fileAsString);

        int index = fileAsString.indexOf("https://telemetry-");
        //System.out.println("INDEX: " + index);
        String https = fileAsString.substring(index, index + 119);
        System.out.println("telemetry URL is " + https);

        URL telemetryURL = new URL(https);
        //JSONObject j = (JSONObject) (telemetryURL.getContent());
        //System.out.println("j = " + j.toString(4));
        //j.keySet();
        System.out.println("\t\t>Attempting to connect to API. Looking at telemetryURL");
        connectToAPI_wantZIP(telemetryURL);
        /*
        InputStream is = telemetryURL.openStream();
        InputStreamReader isr = new InputStreamReader(is);
        System.out.println("isr encoding: " + isr.getEncoding()); //UTF8
        if(isr.ready())
        {
            System.out.println("isr is ready");
            System.out.println("isr.toString(): " + isr.toString());
            Writer wr = new Writer();
            WriterOutputStream w = new WriterOutputStream(new File("hello"));
            isr.transferTo(w));
            System.out.println("Reads..." + isr.read());
        }
         */
        System.out.println("QUERY: " + telemetryURL.getQuery());

       // GZIPCompressorInputStream
        //GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream());

        //storeResponseToSpecifiedFileLocation("storeTelemetry");
        File unknown = storeResponseToSpecifiedFileLocation("storeTelemetry");
        if(!unknown.exists())
        {
            System.out.println("File <unknown> does not yet exist. Attempting to make it now.");
            unknown.mkdirs();


        }
        System.out.println("unknown.length = " + unknown.length());
        Scanner input = new Scanner(unknown);
        int linesRead = 0;
        while(linesRead < 5)
        {
            //String text = input.();
            System.out.println(unknown.canRead());
            //System.out.println(text);
            //System.out.println(input.nextLine());
            linesRead++;
        }
        //System.out.println("List: " + unknown.list().toString());
        System.out.println("-----");


        System.out.println("\t\t<Exiting  getTelemetryURL, returning the url");
        return telemetryURL;
    }

    public HttpURLConnection connectToAPI(URL url) throws IOException {

        System.out.println("url.getFile() : " + url.getFile());
        //System.out.println("CONTENT TYPE: " + url.getContent());
        System.out.println("url.getRef() : " + url.getRef());
        this.connection = (HttpURLConnection) url.openConnection();

        if(connection == null) //!isConnected
        {
            System.out.println("connection is null");
            System.exit(0);
        }
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization","Bearer " + this.getAPIkey());
        connection.setRequestProperty("Accept", "application/vnd.api+json");
        //connection.setRequestProperty("Content-Type", "UTF-16" + "; charset=utf-16"); //added 12/15
        //connection.setRequestProperty("Accept", "gzip"); //added 11/29

        System.out.println("Response code: " + this.connection.getResponseCode()); //expect: 200
        if(this.connection.getResponseCode() == 200) //response is valid/OK
        {
            System.out.println("Connection made. URL: " + url.toString());
            File newFile = new File(specificRequest + "/connect");


            //System.out.println("Preparing to get matches telemetry");
        }
        else
        {
            System.out.println("Error: connection to api has invalid response");
        }
        return this.connection;

    }
    public HttpURLConnection connectToAPI_wantZIP(URL url) throws IOException {
        System.out.println("CONTENT ENCODING: " + connection.getContentEncoding());
        this.connection = (HttpURLConnection) url.openConnection();

        if(connection == null) //!isConnected
        {
            System.out.println("connection is null");
            System.exit(0);
        }

        //System.out.println("REQUEST PROPERTIes: " + connection.getRequestProperties().toString());//getProperty("file.encoding"));
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization","Bearer " + this.getAPIkey());
        connection.setRequestProperty("Accept", "application/vnd.api+json");
        connection.setRequestProperty("Content-Type", "UTF-16" + "; charset=utf-16"); //added 12/15
        connection.setRequestProperty("Accept", "gzip"); //added 11/29

        System.out.println("Response code: " + this.connection.getResponseCode()); //expect: 200
        if(this.connection.getResponseCode() == 200) //response is valid/OK
        {
            System.out.println("Connection made. URL: " + url.toString());
            File newFile = new File(specificRequest + "/connect");
            File newFile2 = new File("/Users/jenniferstibbins/PubgAPI-Java-pubgEndOfNov/file");


            //SOURCE: https://www.baeldung.com/java-curl

            //Command format (curl)
            String command = "curl --compressed " + url + " -H Accept: application/vnd.api+json";

            //Process builder to help with curl command
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));

            //Process
            Process process = processBuilder.start();

            //Input stream
            InputStream inputStream = process.getInputStream();

            File file = new File("/Users/jenniferstibbins/PubgAPI-Java-pubgEndOfNov/file");

            //Transfer the information
            OutputStream outputStream = new FileOutputStream(file);
            inputStream.transferTo(outputStream);

            //Note: this does NOT print the content
            System.out.println("output stream = " + outputStream.toString());

            processBuilder.command(command);
            //////storeResponseToSpecifiedFileLocation(newFile2.toString(), true);

            //File newFile = new File("file");
            //if(!newFile.exists())
            //{
            //    newFile.createNewFile();
                //newFile.createNewFile();
                //newFile.mkdirs();

            //}
            //BORROWED:
            //InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
            //if (responseCode >= 200 && responseCode < 400) {
            //     inputStreamReader =
            //} else {
            //    inputStreamReader = new InputStreamReader(con.getErrorStream());
            //}
            //BufferedReader in = new BufferedReader(inputStreamReader);
            //String inputLine;
            //StringBuilder response = new StringBuilder();
            //while ((inputLine = in.readLine()) != null) {
            //    response.append(inputLine);
            //}
            //in.close();

           // System.out.println(response.toString());
            //END OF BORROWED
            //Object o = this.connection.getContent(); //added 12/15
            //System.out.println("object o.tostring = " + o.toString()); //added 12/15

            //System.out.println("Preparing to get matches telemetry");
        }
        else
        {
            System.out.println("Error: connection to api has invalid response");
        }


        return this.connection;

    }

    public File storeResponseToSpecifiedFileLocation(String dstPath, boolean gzip) throws IOException {
        System.out.println("dstPath = " + dstPath);


        InputStream inputStream = connection.getInputStream();
        System.out.println("input stream:" + inputStream.toString());

        File responseFile = new File(dstPath + ".gz"); //could just make brand new file instead of using this.responseFile (remove global variable)
        //Note: changed .txt to .json (11/19/2022)
        if (responseFile.exists()) {
            System.out.println("Response file exists!");
        } else {
            System.out.println("Response file does not exist. Creating it now");

            if (responseFile.getParentFile() != null) {
                responseFile.getParentFile().mkdirs(); //previously order was create self, then check parent... (switched to parent, then self 11/21)
            }

            responseFile.createNewFile();
        }
        FileOutputStream output = new FileOutputStream(responseFile);
        //System.out.println(output.)
        //Gzip input stream
        inputStream.transferTo(output);
        //System.out.println("LOOK: " + output.g());
        //inputStream.close();
        output.close();
        return responseFile;
    }
    //Consider: returning file so that it can be custom-saved
    public File storeResponseToSpecifiedFileLocation(String dstPath) throws IOException {
        System.out.println("dstPath = " + dstPath);


        InputStream inputStream = connection.getInputStream();
        System.out.println("input stream:" + inputStream.toString());

        File responseFile = new File(dstPath + ".json"); //could just make brand new file instead of using this.responseFile (remove global variable)
        //Note: changed .txt to .json (11/19/2022)
        if(responseFile.exists())
        {
            System.out.println("Response file exists!");
        }
        else
        {
            System.out.println("Response file does not exist. Creating it now");

            if(responseFile.getParentFile() != null)
            {
                responseFile.getParentFile().mkdirs(); //previously order was create self, then check parent... (switched to parent, then self 11/21)
            }

            responseFile.createNewFile();
        }

        //BORROWED: https://www.codejava.net/java-se/networking/use-httpurlconnection-to-download-file-from-an-http-url
        /*
        FileOutputStream outputStream = new FileOutputStream(responseFile);
        int BUFFER_SIZE = 4096 * 16 * 16;
        int bytesRead = -1;
        byte[] buffer = new byte[BUFFER_SIZE];
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
            System.out.println(outputStream.toString());

            System.out.println("\tResponse file length: " + responseFile.length());
            //System.out.println("\tCapacity: " + responseFile.getTotalSpace());
            //System.out.println("\tUsable space:" + responseFile.getUsableSpace() );
            //System.out.println("\tFree space:" + responseFile.getFreeSpace());
            //System.out.println();
            System.out.println();
        }

        outputStream.close();

        inputStream.close();

        System.out.println("File downloaded");

        */
        /////////
        //BORROWED #2:
        // headers to import
//import java.io.*;
//import java.net.*;

        //URL url = new URL("https://telemetry-cdn.pubg.com/pc-krjp/2018/01/01/0/0/1ad97f85-cf9b-11e7-b84e-0a586460f004-telemetry.json");
        //HttpURLConnection con = (HttpURLConnection) url.openConnection();

        //con.setRequestProperty("Accept", "application/vnd.api+json");

        //con.setRequestMethod("GET");

        //int responseCode = con.getResponseCode();
        //System.out.println("Response code: " + responseCode);



        /////
        OutputStream output = new FileOutputStream(responseFile);
        //System.out.println(output.)
            //Gzip input stream
       inputStream.transferTo(output);
        //System.out.println("LOOK: " + output.g());
        //inputStream.close();
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