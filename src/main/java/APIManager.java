//EX:
//get player-focused list of matches
//prettify that file
//look through that file to get the list of match_ids and store them
//for each stored match_id, request full match telemetry information
//for each stored match_id, store the full match telemetry information
//put full match telemetry information in a directory such that this program can use it...


import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Vector;

import static java.lang.System.exit;

public class APIManager {

    //https://mkyong.com/java/java-read-a-file-from-resources-folder/
    //InputStream is = JavaClassName.class.getClassLoader().getResourceAsStream("file.txt");
    public static void workingWithSampleFile() throws IOException {
        //File[] files = new File("C:\\Users\\jmast\\pubgFilesExtracted").listFiles(); //Let user decide, though?
        System.out.println("Attempting to work with sample file...");
        //File sample = new File("C:\\Users\\jmast\\pubgFilesExtracted\\sampleFromAPIManager");

        Path src = (Path) Paths.get("/Users", "jmast", "IdeaProjects", "PubgAPI-Java", "src", "main", "resources", "sampleFile_JS1936.txt");
        //Path dest = (Path) Paths.get("/Users", "jmast", "IdeaProjects", "PubgAPI-Java", "src", "main", "resources", "copyOfSampleFile_JS1936.txt");
        System.out.println("Src: " + src.toString());
        File s = src.toFile();
        File s_pretty = FileManager.makePretty(s);
        System.out.println("Pretty path: " +  s_pretty.getAbsolutePath());

        String fileAsString = FileManager.storeFileAsString(s_pretty);
        System.out.println(fileAsString);

        JSONObject json = new JSONObject(fileAsString);
        //System.out.println(json.toString());
        System.out.println(json.keySet()); //data, meta, links
        String desiredKey = "data";
        JSONArray jsonArrayData = json.getJSONArray("data");
        //System.out.println(json.getJSONArray("data").getJSONObject(0).getJSONObject("relationships").getJSONObject("matches").getJSONArray("data"));
        JSONArray jsonArray = json.getJSONArray("data").getJSONObject(0).getJSONObject("relationships").getJSONObject("matches").getJSONArray("data");
        System.out.println("jsonArray.length = " + jsonArray.length());
        Vector<String> match_ids = new Vector<String>();

        for(int i = 0; i < jsonArray.length(); i++)
        {
            //System.out.println("i = " + i);

            //System.out.println(jsonArray.getJSONObject(i));
            JSONObject id = jsonArray.getJSONObject(i);
            String match_id = id.get("id").toString();
            System.out.println("match_id = " + match_id);
            match_ids.add(match_id);
            //System.out.println(id.toString());
            //System.out.println(id.keySet());
        }
        System.out.println("match_ids.size() = " + match_ids.size());
        //System.out.println("LOOK: " + json.getJSONArray("data"));
        //JSONObject objectFromKey = json.getJSONObject(json.keySet.begin());
        //JSONArray jsonArray = new JSONArray(fileAsString);
        //for(int i = 0; i < )

        //String match = json.getString("data");
        //System.out.println(match);
        //JSONArray jsonArray = new JSONArray(fileAsString);
        //JSONObject jsonObject = fileAsString
        //JSONArray jsonArray = new JSONArray(fileAsString);
        ///for (int i = 0; i < jsonArray.length(); i++) {

        ///    JSONObject jsonObject = jsonArray.getJSONObject(i);
            //String type_T = jsonObject.getString("_T");
        ///    System.out.println(jsonObject.toString());
        //}
        //Scanner input = new Scanner(System.in);



        //File d = new File(dest.toString());
       // d = FileManager.makePretty(s);
       // File d =
        //if(!d.exists())
        //{
       //     d.createNewFile();
       //     FileUtils.copyFile(s, d);
        //    d = FileManager.makePretty(d);
       // }
        //C:\Users\jmast\pubgFilesExtracted


        //FileManager.makePretty(file);
        //if(!sample.exists())
        //{
        //    sample.mkdirs();
        //}
        //C:\Users\jmast\IdeaProjects\PubgAPI-Java\src\main\resources
        //FileManager.makePretty(sample);
        exit(0);
        //InputStream inputStream = APIManager.class.getResourceAsStream("sampleFile_JS1936_prettifiedViaOnlineJSONFormatter.txt");
        //File temp = new File(inputStream);
        //File f = new File("C:\\Users\\jmast\\IdeaProjects\\PubgAPI-Java\\src\\main\\resources\\sampleFile_JS1936_prettifiedViaOnlineJsonFormatter_2.txt");
    }
}
/*
import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;

//This is just one example of a request that could be made to the pubg API.
//There are MANY possible requests.
//For instance, you could make a request about a single player, but you could also make a request multiple players.
//You can make requests regarding certain seasons. You could filter your data to include only ranked games. (Etc.)

//Guide: documentation.pubg.com
public class Main
{
    public static String getDateOfRequest()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date currentDate = new Date();
        //System.out.println(dateFormat.format(currentDate));
        return dateFormat.format(currentDate);
    }

    //Resource: https://documentation.pubg.com/en/telemetry.html#telemetry
    public void getTelemetries_usingMatch_IDs(Queue<String> match_ids)
    {
        while(!match_ids.isEmpty())
        {
            String currentMatch_ID = match_ids.poll();
            //call getTelemetry_usingMatch_ID(currentMatch_ID);

        }
    }
    public void getTelemetry_usingMatch_ID(String match_id) throws IOException
    {
        String API_key = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJmMjdiZDY0MC05ODk5LTAxM2EtYjVmNS0wYzc0NWVlZDY1NjQiLCJpc3MiOiJnYW1lbG9ja2VyIiwiaWF0IjoxNjQ5MzMzNTYxLCJwdWIiOiJibHVlaG9sZSIsInRpdGxlIjoicHViZyIsImFwcCI6InB1YmdfYXBpX2xlYXJuIn0.aQZbXGdwOM8HwXLvulYN2nmUUCVgG6susMmAE6oKopY";
        String playerName = "JS1936";
        URL url = new URL("https://api.pubg.com/shards/steam/matches/"+ match_id);
        //URL url = new URL("https://api.pubg.com/shards/steam/players?filter[playerNames]=" + playerName);


        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization","Bearer " + API_key);
        conn.setRequestProperty("Accept", "application/vnd.api+json");
        //conn.setRequestProperty("Accept-Encoding","gzip"); //confused

        //"https://api.pubg.com/shards/$platformn/matches/$matchId" \

    }
    public void getMatchObject_usingMatchIDD()
    {

    }
    public static void given2() throws IOException
    {

    }
    //private static String API_key = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJmMjdiZDY0MC05ODk5LTAxM2EtYjVmNS0wYzc0NWVlZDY1NjQiLCJpc3MiOiJnYW1lbG9ja2VyIiwiaWF0IjoxNjQ5MzMzNTYxLCJwdWIiOiJibHVlaG9sZSIsInRpdGxlIjoicHViZyIsImFwcCI6InB1YmdfYXBpX2xlYXJuIn0.aQZbXGdwOM8HwXLvulYN2nmUUCVgG6susMmAE6oKopY";
    public static void given() throws IOException
    {

        String API_key = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJmMjdiZDY0MC05ODk5LTAxM2EtYjVmNS0wYzc0NWVlZDY1NjQiLCJpc3MiOiJnYW1lbG9ja2VyIiwiaWF0IjoxNjQ5MzMzNTYxLCJwdWIiOiJibHVlaG9sZSIsInRpdGxlIjoicHViZyIsImFwcCI6InB1YmdfYXBpX2xlYXJuIn0.aQZbXGdwOM8HwXLvulYN2nmUUCVgG6susMmAE6oKopY";
        String playerName = "JS1936";
        URL url = new URL("https://api.pubg.com/shards/steam/players?filter[playerNames]=" + playerName);


        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization","Bearer " + API_key);
        conn.setRequestProperty("Accept", "application/vnd.api+json");
        //conn.setRequestProperty("Accept-Encoding","gzip"); //confused

        InputStream inputStream = conn.getInputStream();
        //Reader inputStreamReader = new InputStreamReader(inputStream);
        //ObjectInputStream ois = new ObjectInputStream(inputStream);
        //System.out.println("Look2: " + inputStreamReader.toString());
        String dateOfRequest = getDateOfRequest();
        //System.out.println("Date of request: " + dateOfRequest);
        //String filePath = "C:\\sampleFile_" + playerName + "_[" + dateOfRequest + "].txt";
        //System.out.println("FILE PATH: " + filePath);
        File data = new File("C:\\sampleFile_" + playerName + "_[" + dateOfRequest + "].txt"); //add time component?
        //if(!data.exists())
        //{
        //    System.out.println("File does not yet exist");
            data.getParentFile().mkdirs();//createNewFile();
            data.createNewFile();
        //}

        OutputStream output = new FileOutputStream(data);
        inputStream.transferTo(output);
                //inputStreamReader.close();
        output.close();
                ///data.deleteOnExit();
                //account.9c618f3851b646f9a6de9bbd6962d73f
        //How would these files get added into the other program?
    }
    public static void main(String[] args)
    {
        try {
            given();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


//

/*
//URL url = new URL("https://api.pubg.com/shards/$steam/players?filter[playerNames]=$JS1936");
    //https://api.pubg.com/shards/$platform/samples?filter[createdAt-start]=$startTime"
    //"https://api.pubg.com/shards/$steam/players?filter[playerNames]=$JS1936"
    //curl -g "https://api.pubg.com/shards/$platform/players?filter[playerIds]=$playerId-1,$playerId-2" \
    //"https://api.pubg.com/shards/$platform/players?filter[playerNames]=$playerName"
    //shards/$platform - the platform shard --> steam
    //conn.setRequestProperty("Accept-Encoding","gzip");
    //EX of game mode: squad fpp
    //conn.setRequestProperty("Authorization","Bearer <api-key>");
 */

/*
import java.io.*;
import java.net.*;
public class Main
{
    public static void given() throws IOException
    {
        String API_key = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJmMjdiZDY0MC05ODk5LTAxM2EtYjVmNS0wYzc0NWVlZDY1NjQiLCJpc3MiOiJnYW1lbG9ja2VyIiwiaWF0IjoxNjQ5MzMzNTYxLCJwdWIiOiJibHVlaG9sZSIsInRpdGxlIjoicHViZyIsImFwcCI6InB1YmdfYXBpX2xlYXJuIn0.aQZbXGdwOM8HwXLvulYN2nmUUCVgG6susMmAE6oKopY";
        //URL url = new URL("endpoint-url");
        ///URL url = new URL("https://api.pubg.com/shards/$steam/players?filter[playerNames]=$JS1936,matt112");
        ///URL url = new URL("https://api.pubg.com/shards/$steam/samples?");
        URL url = new URL("https://api.pubg.com/shards/steam/players?filter[playerNames]=JS1936");
        //URL url = new URL("https://api.pubg.com/shards/$steam/players?filter[playerNames]=$JS1936");
        //https://api.pubg.com/shards/$platform/samples?filter[createdAt-start]=$startTime"
        //"https://api.pubg.com/shards/$steam/players?filter[playerNames]=$JS1936"
        //curl -g "https://api.pubg.com/shards/$platform/players?filter[playerIds]=$playerId-1,$playerId-2" \
        //"https://api.pubg.com/shards/$platform/players?filter[playerNames]=$playerName"
        //shards/$platform - the platform shard --> steam
        //conn.setRequestProperty("Accept-Encoding","gzip");
        //EX of game mode: squad fpp
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        //conn.setRequestProperty("Authorization","Bearer <api-key>");
        conn.setRequestProperty("Authorization","Bearer " + API_key);
        conn.setRequestProperty("Accept", "application/vnd.api+json");
        //conn.setRequestProperty("Accept-Encoding","gzip");
        InputStream inputStream = conn.getInputStream();
        //System.out.println("Look..." + inputStream.readAllBytes());
        Reader inputStreamReader = new InputStreamReader(inputStream);
        //ObjectInputStream ois = new ObjectInputStream(inputStream);
        System.out.println("Look2: " + inputStreamReader.toString());
        File data = new File("C:\\sampleFile.txt");
        OutputStream output = new FileOutputStream(data);
        inputStream.transferTo(output);
       /// while(inputStreamReader.)
        //while(inputStream.
        //transfer it to an output stream?
        ///while(inputStreamReader.ready())
        ///{
        ///    char ch = (char) inputStreamReader.read();
            //System.out.println("Look3: " + inputStreamReader.read());
        ///    System.out.print(ch);
            //convert ascii to character
        //}
        inputStreamReader.close();
        //`conn.set
        //System.out.println("Attempting to print..." + conn.getInputStream());
        //conn.setRequestProperty("Accept-Encoding","gzip");
        //"...pubg.com/shards/steam/endpoint..."
        //filter?
    }
    public static void main(String[] args)
    {
        try {
            given();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
 */
