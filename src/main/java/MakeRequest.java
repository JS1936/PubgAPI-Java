import java.util.Scanner;

//what if it's like...
//calculateKillCounts(Player)
//calculateKillCounts(Team)
//calculateKillCounts(Match)
//diff object types...
public class MakeRequest {

    //print
    //track
    //etc.?

    public static void main(String[] args)
    {
        System.out.println("Type the corresponding number and then press enter.");
        System.out.println("I want information about a...");
        System.out.println("0: Game");
        System.out.println("1: Player");

        Scanner input = new Scanner(System.in);

        //str.isDigit?

        int request = Integer.parseInt(input.nextLine()); //careful... //fails if not an int?
        /*
         String request_original = input.nextLine(); //careful... //fails if not an int?
        int request = Integer.parseInt(request_original);
         */
        //how do you check if something is an integer, then?

        //String request = input.next();
        if(request == 0)
        {
            System.out.println("Okay. You want information about a specific game.");
        }
        else if(request == 1)
        {
            System.out.println("Okay. You want information about a specific player.");
            System.out.println("Enter player name (EX: matt112): ");
            if(input.hasNextLine())
            {
                System.out.println("input has next line");
            }
            String playerName = input.nextLine();
            System.out.println("Looking for information about " + playerName + "...");
            System.out.println("Calling playerExists on " + playerName);
            playerExists(playerName);
        }
        else
        {
            System.out.println(request + " is not 0 or 1. Invalid request.");

        }





        //get user input
        //if GAME/PLAYER exists,
        //print possible things to know about that GAME/PLAYER.

        //if GAME/PLAYER doesn't exist,
        //"sorry" message

        //if 0, check if information is stored for that game
        //if 1, check if information is stored for that player
    }

    public static boolean playerExists(String name)
    {
        if(Memory.players.containsKey(name))
        {
            System.out.println("We have encountered " + name + " before!");
        }
        else
        {
            System.out.println("We do not know of any player called " + name + "...");
        }
        return false;
    }

    public static boolean gameExists(String game_id)
    {
        //if(Memory.gameHTTPS.containsKey())
        return false;
    }
}


//MakeRequest ==> say what you want

//MakeRequest asks Memory if it knows it

//The information Memory knows comes from Main

//An example of information known could be #kills in a game by a specific Player


//Transferred 9/17, incorporate soon
/*

import java.io.*;
import java.net.*;

public class Main
{
    //URL url = new URL("https://api.pubg.com/shards/$steam/players?filter[playerNames]=$JS1936");
    //https://api.pubg.com/shards/$platform/samples?filter[createdAt-start]=$startTime"
    //"https://api.pubg.com/shards/$steam/players?filter[playerNames]=$JS1936"
    //curl -g "https://api.pubg.com/shards/$platform/players?filter[playerIds]=$playerId-1,$playerId-2" \
    //"https://api.pubg.com/shards/$platform/players?filter[playerNames]=$playerName"
    //shards/$platform - the platform shard --> steam
    //conn.setRequestProperty("Accept-Encoding","gzip");
    //EX of game mode: squad fpp
    //conn.setRequestProperty("Authorization","Bearer <api-key>");
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

        File data = new File("C:\\sampleFile_" + playerName + ".txt"); //add time component?
        OutputStream output = new FileOutputStream(data);
        inputStream.transferTo(output);
        //inputStreamReader.close();
        output.close();
        ///data.deleteOnExit();
        //account.9c618f3851b646f9a6de9bbd6962d73f
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



