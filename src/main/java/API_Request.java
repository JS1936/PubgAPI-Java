import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Vector;

//custom request option? (For matches)
//wait, acquiring vs using...
//add "uses" diagram? Domain diagram?

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
    //public boolean isConnectedToAPI() { return this.isConnected; }
    public HttpURLConnection getConnection() { return this.connection; }
    public long getTimestamp() { return this.timestamp; }

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
    //
    //URL url = new URL("https://api.pubg.com/shards/steam/matches/"+ match_id);
//this.oneMatch = new URL("https://api.pubg.com/shards/steam/matches/" + match_id);
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
        File summary_File = new File(summary_Path.toString() + ".txt");
        //if(f.isFile())
        //{
        //    System.out.println("file f exists...");
        //}

        //create "matches" subdirectory for timestamp
        this.match_list =  (Files.createDirectory(Path.of(specificRequest + "/matches")).toFile());

        //use summary to get recent match_ids. They are then formatted to be more visually user-friendly.
        Vector<String> match_ids = APIManager.getMatchIDsFromRequestPath(summary_File);
        int numMatches = 0;
        for(String match_id : match_ids) {
            URL oneMatch_ = new URL("https://api.pubg.com/shards/steam/matches/" + match_id);
            connectToAPI(oneMatch_);
            Path match_Path = Path.of(specificRequest + "/matches/match_id_" + match_id);
            File ugly = storeResponseToSpecifiedFileLocation(match_Path.toString()); //save
            FileManager.makePretty(ugly);
        }
        //getNumMatches();
        ///getMatches();

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
        File responseFile = new File(dstPath + ".txt"); //could just make brand new file instead of using this.responseFile (remove global variable)

        if(responseFile.exists())
        {
            System.out.println("Response file exists!");
        }
        else
        {
            System.out.println("Response file does not exist. Creating it now");
            responseFile.createNewFile();
            responseFile.getParentFile().mkdirs();
        }

        OutputStream output = new FileOutputStream(responseFile);
        inputStream.transferTo(output);
        output.close();
        return responseFile;
    }

    //Consider: allowing custom dst
    //public void getRequest(URL url) throws IOException {

    //connection.disconnect();
    //}

    //Consider: make for single match instead...
    /*
    public void getMatchTelemetry() throws IOException { //fix?
            Vector<String> match_ids = null; //fix

            for(String match_id : match_ids)
            {
                this.oneMatch = new URL("https://api.pubg.com/shards/steam/matches/"+ match_id);
                connectToAPI(this.oneMatch);
                //if(isConnected)
                //{
                       //
                //}
                //else
                //{
                    //error. Did not connect
                //}

            }
            String id = "1e1e3cd3-1a5e-4c97-9444-1e882dda0628";
            this.oneMatch = new URL("https://api.pubg.com/shards/steam/matches/"+ id);
            connectToAPI(this.oneMatch);
            //Path summary_Path = Path.of(specificRequest + "/summary_matchList.txt");
            //Files.createDirectory(Path.of(specificRequest + "/matches"));
            //storeResponseToSpecifiedFileLocation(summary_Path.toString());
    }
     */
    //reportMatchTelemetry


    //get_matchID
    //get_match_info

    // HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    //     conn.setRequestMethod("GET");
    //             conn.setRequestProperty("Authorization","Bearer " + API_key);
    //             conn.setRequestProperty("Accept", "application/vnd.api+json");

    //constructor(s)
    //copy constructor
    //destructor

    //getters
    //setters
}
