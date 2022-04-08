import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Scanner;
import java.util.Vector;


import static java.util.Locale.US;
//import static jdk.nashorn.internal.runtime.regexp.joni.encoding.CharacterType.ASCII;

//import
//read the file to string first...

//could clear out original file
//then for each line in pretty file, write to original file?
public class Main {

    public static void makePretty(File fileName) throws IOException {
        //read in file as string
        //put that string (pretty version) into new file
        //read from that file
        //String prettyString = "";
        /////Gson gson = new GsonBuilder().setPrettyPrinting().create();
        /////JsonElement je = JsonParser.parseString(uglyJsonString);
        //String prettyJsonString = gson.toJson(je);
        String originalName = fileName.getName();
        String originalPath = fileName.getAbsolutePath();
        //System.out.println("original name: " + originalName);
        //System.out.println("original path: " + originalPath);


        String uglyString = FileUtils.readFileToString(fileName);

        //System.out.println("pretty string: " + prettyString);
        Gson gson = new GsonBuilder().setPrettyPrinting().setLenient().create();
        JsonElement je = JsonParser.parseString(uglyString);
        String prettyJsonString = gson.toJson(je);

        //write to file


        //System.out.println("Made it here...");

        /*
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println("gson: " + gson);
        //JsonReader.setLenient() == true;
        //String uglyJsonString = "telemetry_json0cfba5cb-c088-488d-86fc-86458cb91b9d.json"; //file access here
        System.out.println("LOOK: " + fileName.toString());
        JsonElement je = JsonParser.parseString("\"" + gson.toString() + "\"");
        String prettyJsonString = gson.toJson(je);
        System.out.println("pretty: "+ prettyJsonString); //something is wrong around here... (see below printout example)
        //LOOK: C:\Users\jmast\OneDrive\Documents\pubg games\__MACOSX
        //pretty: "{serializeNulls:false,factories:[Factory[typeHierarchy\u003dcom.google.gson.JsonElement,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$28@6500df86], com.google.gson.internal.bind.ObjectTypeAdapter$1@402a079c, com.google.gson.internal.Excluder@59ec2012, Factory[type\u003djava.lang.String,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$15@4cf777e8], Factory[type\u003djava.lang.Integer+int,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$7@2f686d1f], Factory[type\u003djava.lang.Boolean+boolean,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$3@3fee9989], Factory[type\u003djava.lang.Byte+byte,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$5@73ad2d6], Factory[type\u003djava.lang.Short+short,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$6@7085bdee], Factory[type\u003djava.lang.Long+long,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$11@1ce92674], Factory[type\u003djava.lang.Double+double,adapter\u003dcom.google.gson.Gson$1@b7f23d9], Factory[type\u003djava.lang.Float+float,adapter\u003dcom.google.gson.Gson$2@61d47554], com.google.gson.internal.bind.NumberTypeAdapter$1@5bcab519, Factory[type\u003djava.util.concurrent.atomic.AtomicInteger,adapter\u003dcom.google.gson.TypeAdapter$1@e45f292], Factory[type\u003djava.util.concurrent.atomic.AtomicBoolean,adapter\u003dcom.google.gson.TypeAdapter$1@5f2108b5], Factory[type\u003djava.util.concurrent.atomic.AtomicLong,adapter\u003dcom.google.gson.TypeAdapter$1@69b794e2], Factory[type\u003djava.util.concurrent.atomic.AtomicLongArray,adapter\u003dcom.google.gson.TypeAdapter$1@3f200884], Factory[type\u003djava.util.concurrent.atomic.AtomicIntegerArray,adapter\u003dcom.google.gson.TypeAdapter$1@1e397ed7], Factory[type\u003djava.lang.Character+char,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$14@490ab905], Factory[type\u003djava.lang.StringBuilder,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$19@56ac3a89], Factory[type\u003djava.lang.StringBuffer,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$20@27c20538], Factory[type\u003djava.math.BigDecimal,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$16@72d818d1], Factory[type\u003djava.math.BigInteger,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$17@6e06451e], Factory[type\u003dcom.google.gson.internal.LazilyParsedNumber,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$18@59494225], Factory[type\u003djava.net.URL,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$21@6e1567f1], Factory[type\u003djava.net.URI,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$22@5cb9f472], Factory[type\u003djava.util.UUID,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$24@cb644e], Factory[type\u003djava.util.Currency,adapter\u003dcom.google.gson.TypeAdapter$1@13805618], Factory[type\u003djava.util.Locale,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$27@56ef9176], Factory[typeHierarchy\u003djava.net.InetAddress,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$23@4566e5bd], Factory[type\u003djava.util.BitSet,adapter\u003dcom.google.gson.TypeAdapter$1@1ed4004b], com.google.gson.internal.bind.DateTypeAdapter$1@ff5b51f, Factory[type\u003djava.util.Calendar+java.util.GregorianCalendar,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$26@25bbe1b6], com.google.gson.internal.sql.SqlTimeTypeAdapter$1@5702b3b1, com.google.gson.internal.sql.SqlDateTypeAdapter$1@69ea3742, com.google.gson.internal.sql.SqlTimestampTypeAdapter$1@4b952a2d, com.google.gson.internal.bind.ArrayTypeAdapter$1@3159c4b8, Factory[type\u003djava.lang.Class,adapter\u003dcom.google.gson.TypeAdapter$1@73846619], com.google.gson.internal.bind.CollectionTypeAdapterFactory@4d339552, com.google.gson.internal.bind.MapTypeAdapterFactory@f0f2775, com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory@5a4aa2f2, com.google.gson.internal.bind.TypeAdapters$29@6adede5, com.google.gson.internal.bind.ReflectiveTypeAdapterFactory@6591f517],instanceCreators:{}}"
        //pretty file name: "{serializeNulls:false,factories:[Factory[typeHierarchy\u003dcom.google.gson.JsonElement,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$28@6500df86], com.google.gson.internal.bind.ObjectTypeAdapter$1@402a079c, com.google.gson.internal.Excluder@59ec2012, Factory[type\u003djava.lang.String,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$15@4cf777e8], Factory[type\u003djava.lang.Integer+int,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$7@2f686d1f], Factory[type\u003djava.lang.Boolean+boolean,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$3@3fee9989], Factory[type\u003djava.lang.Byte+byte,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$5@73ad2d6], Factory[type\u003djava.lang.Short+short,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$6@7085bdee], Factory[type\u003djava.lang.Long+long,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$11@1ce92674], Factory[type\u003djava.lang.Double+double,adapter\u003dcom.google.gson.Gson$1@b7f23d9], Factory[type\u003djava.lang.Float+float,adapter\u003dcom.google.gson.Gson$2@61d47554], com.google.gson.internal.bind.NumberTypeAdapter$1@5bcab519, Factory[type\u003djava.util.concurrent.atomic.AtomicInteger,adapter\u003dcom.google.gson.TypeAdapter$1@e45f292], Factory[type\u003djava.util.concurrent.atomic.AtomicBoolean,adapter\u003dcom.google.gson.TypeAdapter$1@5f2108b5], Factory[type\u003djava.util.concurrent.atomic.AtomicLong,adapter\u003dcom.google.gson.TypeAdapter$1@69b794e2], Factory[type\u003djava.util.concurrent.atomic.AtomicLongArray,adapter\u003dcom.google.gson.TypeAdapter$1@3f200884], Factory[type\u003djava.util.concurrent.atomic.AtomicIntegerArray,adapter\u003dcom.google.gson.TypeAdapter$1@1e397ed7], Factory[type\u003djava.lang.Character+char,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$14@490ab905], Factory[type\u003djava.lang.StringBuilder,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$19@56ac3a89], Factory[type\u003djava.lang.StringBuffer,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$20@27c20538], Factory[type\u003djava.math.BigDecimal,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$16@72d818d1], Factory[type\u003djava.math.BigInteger,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$17@6e06451e], Factory[type\u003dcom.google.gson.internal.LazilyParsedNumber,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$18@59494225], Factory[type\u003djava.net.URL,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$21@6e1567f1], Factory[type\u003djava.net.URI,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$22@5cb9f472], Factory[type\u003djava.util.UUID,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$24@cb644e], Factory[type\u003djava.util.Currency,adapter\u003dcom.google.gson.TypeAdapter$1@13805618], Factory[type\u003djava.util.Locale,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$27@56ef9176], Factory[typeHierarchy\u003djava.net.InetAddress,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$23@4566e5bd], Factory[type\u003djava.util.BitSet,adapter\u003dcom.google.gson.TypeAdapter$1@1ed4004b], com.google.gson.internal.bind.DateTypeAdapter$1@ff5b51f, Factory[type\u003djava.util.Calendar+java.util.GregorianCalendar,adapter\u003dcom.google.gson.internal.bind.TypeAdapters$26@25bbe1b6], com.google.gson.internal.sql.SqlTimeTypeAdapter$1@5702b3b1, com.google.gson.internal.sql.SqlDateTypeAdapter$1@69ea3742, com.google.gson.internal.sql.SqlTimestampTypeAdapter$1@4b952a2d, com.google.gson.internal.bind.ArrayTypeAdapter$1@3159c4b8, Factory[type\u003djava.lang.Class,adapter\u003dcom.google.gson.TypeAdapter$1@73846619], com.google.gson.internal.bind.CollectionTypeAdapterFactory@4d339552, com.google.gson.internal.bind.MapTypeAdapterFactory@f0f2775, com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory@5a4aa2f2, com.google.gson.internal.bind.TypeAdapters$29@6adede5, com.google.gson.internal.bind.ReflectiveTypeAdapterFactory@6591f517],instanceCreators:{}}"
        //An error occurred.
        */


        //C:\Users\jmast\pubgFilesExtracted
        File theDir = new File("C:\\Users\\jmast\\pubgFilesExtracted\\prettyFiles");
        if(!theDir.exists())
        {
            theDir.mkdirs();
        }

        //System.out.println("HERE I AM!!!!!!!");
        //fileName.delete(); //GAHHH ACCIDENTALLY DELETED THEFILES
        File prettyFile = new File("C:\\Users\\jmast\\pubgFilesExtracted\\prettyFiles\\" + originalName); //prettyJsonString //LOOK //fix this
        //System.out.println("File accessing... : " + prettyFile.getAbsolutePath());
       //https://stackoverflow.com/questions/2885173/how-do-i-create-a-file-and-write-to-it
        //overwrite the file! ...vs create new one?
        //PrintWriter writer = new PrintWriter(prettyFile, "UTF-8"); //will overwrite if already exists //WHAT IS HAPPENING???
        //writer.println(prettyJsonString);
        //Scanner scan = new Scanner(prettyJsonString);

        FileUtils.writeStringToFile(prettyFile, prettyJsonString);

        /////System.out.println("MADE IT HERE, TOO");

        //while(scan.hasNextLine())
        //{
        //    System.out.println("Weeeeeeeee"); //not making it here
        //    String line = scan.nextLine();
        //    writer.println(line);
        //    System.out.println(line);
        //}
       // writer.close();
        //writer.println();
        //System.out.println("pretty file name: " + prettyFile);
        //System.out.println("absolute path: " + prettyFile.getAbsolutePath());
        //prettyFile;
        //System.out.println("absolute path2 updated: " + fileName.getAbsolutePath());
        //theDir.createNewFile();
        countBotsAndPeople(prettyFile); //WHY IS IT INSISTING THERE ARE NO BOTS?

    }
    public static void countBotsAndPeople(File prettyFile)
    {
        try
        {
            Scanner scan = new Scanner(prettyFile);
            Vector<String> playerNames = new Vector<String>();
            Vector<String> botNames = new Vector<String>(); //ai
            String totalPlayers = ""; //why is this a string...?

            boolean gameHasStarted = false;

            while(scan.hasNextLine()) {
                //cat
                String data = scan.nextLine();

                //System.out.println(data);

                if(data.contains("numStartPlayers"))
                {
                    //System.out.println(data);
                    totalPlayers = data;
                    gameHasStarted = true;
                }

                if(data.contains("accountId") && gameHasStarted)
                {
                    if(data.contains("account.")) //real person
                    {
                        int accountStart = data.indexOf("account.");
                        String account_id = data.substring(accountStart); //could remove this...
                        if(account_id.contains(","))
                        {
                            account_id = account_id.substring(0, account_id.length() -1);
                        }
                        if(!playerNames.contains(account_id)) {
                            //new player to add to list
                            playerNames.add(account_id);
                            //System.out.println(account_id); //doubles...
                        }
                    }
                    else //bot
                    {

                        if(data.contains("ai.") && gameHasStarted) //important that gameHasStarted
                        {
                            int accountStart = data.indexOf("ai.");
                            //System.out.println(accountStart);
                            String account_id = data.substring(accountStart, accountStart + 6); ///? Why are some ""? (-1)
                            //System.out.println(account_id);
                            //System.out.println("\nCurrent botNames list:");
                            for(int i =0; i<botNames.size(); i++)
                            {
                                //System.out.print("" + botNames.get(i) + " ");
                            }
                            //System.out.println();
                            if(!botNames.contains(account_id))
                            {
                                //.out.println("FRESH NAME! : " + account_id);
                                botNames.add(account_id);
                                //System.out.println("adding: " + account_id); //repeats...
                            }
                        }
                    }
                }


            }

            //HashSet<String> removeDuplicates = new HashSet<String>();
            //for(String x : playerNames)
            //{
            //    removeDuplicates.add(x);
            //
            //System.out.println("#players after duplicates removed: " + removeDuplicates.size());
            //Just in case some duplicates somehow managed to stick along...

            //System.out.println("#players:    "  + playerNames.size());
            System.out.println("#bots:       "  + botNames.size() + " / " + playerNames.size());
            //System.out.println("------------------------");
            //System.out.println("total:       " + (playerNames.size() + botNames.size()));
            //System.out.println(totalPlayers.trim());

            //System.out.println("\n\n");
            //System.out.println("------------------------------------------------------\n\n\n");



        } catch (FileNotFoundException e)
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public static void main(String[] args)
    {
        //System.out.println("HELLO!");
        //File[] prettiedFiles = new File("C:\\Users\\jmast\\pubgFilesExtracted\\prettyFiles").listFiles();
        //if(prettiedFiles.Exists())
        File[] files = new File("C:\\Users\\jmast\\pubgFilesExtracted").listFiles();

        for(File fileName : files)
        {
            //if(fileName.getName() != "prettyFiles")
            //{
                System.out.println(fileName);
                try {
                    makePretty(fileName); //error here
                } catch (IOException e) {
                    e.printStackTrace();
                }
            //}

        }

        /*
        //https://stackoverflow.com/questions/4105795/pretty-print-json-in-java
        //FileUtils.readFileToString(file, US-ASCII);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String uglyJsonString = "telemetry_json0cfba5cb-c088-488d-86fc-86458cb91b9d.json"; //file access here
        JsonElement je = JsonParser.parseString(uglyJsonString);
        String prettyJsonString = gson.toJson(je);


        //take in file
        //"prettify" it
        //JSONObject jsonObject = new JSONObject(obj);
        //String prettyJson = jsonObject.toString(4);

        try
        {
            //URL url = new URL(url here);
            //except you HAVE the file (EX: telemetry events(2))
            //File myObj = new File("telemetryEventsFile.json");
            //C:\\Users\\jmast\\OneDrive\\Documents\\pubg games\\telemetry_json0cfba5cb-c088-488d-86fc-86458cb91b9d.json
            //String fileLocation = "C:\\Users\\jmast\\IdeaProjects\\blankProject\\src\\com\\company\\telemetryEventsFile.json";
            String fileLocation = "C:\\Users\\jmast\\OneDrive\\Documents\\pubg games\\jsonformatter.txt"; //"prettified" file
            File myObj = new File(fileLocation);
            Scanner scan = new Scanner(myObj);
            Vector<String> playerNames = new Vector<String>();
            Vector<String> botNames = new Vector<String>(); //ai
            //HashSet<String> playerNames2 = new HashSet<String>();
            String totalPlayers = ""; //why is this a string...?

            boolean gameHasStarted = false;
            while(scan.hasNextLine()) {
                //cat
                String data = scan.nextLine();
                System.out.println(data);

                if(data.contains("numStartPlayers"))
                {
                    //System.out.println(data);
                    totalPlayers = data;
                    gameHasStarted = true;
                }

                if(data.contains("accountId") && gameHasStarted)
                {
                    if(data.contains("account.")) //real person
                    {
                        int accountStart = data.indexOf("account.");
                        String account_id = data.substring(accountStart); //could remove this...
                        if(account_id.contains(","))
                        {
                            account_id = account_id.substring(0, account_id.length() -1);
                        }
                        if(!playerNames.contains(account_id)) {
                            //new player to add to list
                            playerNames.add(account_id);
                            System.out.println(account_id); //doubles...
                        }
                    }
                    else //bot
                    {

                        if(data.contains("ai.") && gameHasStarted) //important that gameHasStarted
                        {
                            int accountStart = data.indexOf("ai.");
                            //System.out.println(accountStart);
                            String account_id = data.substring(accountStart, accountStart + 6); ///? Why are some ""? (-1)
                            //System.out.println(account_id);
                            //System.out.println("\nCurrent botNames list:");
                            for(int i =0; i<botNames.size(); i++)
                            {
                                //System.out.print("" + botNames.get(i) + " ");
                            }
                            //System.out.println();
                            if(!botNames.contains(account_id))
                            {
                                //.out.println("FRESH NAME! : " + account_id);
                                botNames.add(account_id);
                                //System.out.println("adding: " + account_id); //repeats...
                            }
                        }
                    }
                }
            }
            //HashSet<String> removeDuplicates = new HashSet<String>();
            //for(String x : playerNames)
            //{
            //    removeDuplicates.add(x);
            //
            //System.out.println("#players after duplicates removed: " + removeDuplicates.size());
            //Just in case some duplicates somehow managed to stick along...

            System.out.println("#players:    "  + playerNames.size());
            System.out.println("#bots:       "  + botNames.size());
            System.out.println("------------------------");
            System.out.println("total:       " + (playerNames.size() + botNames.size()));
            System.out.println(totalPlayers.trim());

            System.out.println("\n\n");
            System.out.println("------------------------------------------------------\n\n\n");

        } catch (FileNotFoundException e)
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        */
    }
}

//HEAVY REFERENCE: https://github.com/Corefinder89/SampleJavaCodes/blob/master/src/Dummy1.java
//                 https://www.w3schools.com/java/java_files_read.asp

//first check if one other file works

//If wanting to test multiple files...
//Could have an array of the file locations
//Pass thosde to a method
//Said method execute


//URL url = new URL(url here);
//except you HAVE the file (EX: telemetry events(2))
//File myObj = new File("telemetryEventsFile.json");
//C:\\Users\\jmast\\OneDrive\\Documents\\pubg games\\telemetry_json0cfba5cb-c088-488d-86fc-86458cb91b9d.json
//String fileLocation = "C:\\Users\\jmast\\IdeaProjects\\blankProject\\src\\com\\company\\telemetryEventsFile.json";
//String fileLocation = "C:\\Users\\jmast\\OneDrive\\Documents\\pubg games\\jsonformatter.txt"; //"prettified" file
//File myObj = new File(fileLocation);