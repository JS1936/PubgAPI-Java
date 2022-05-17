import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.*;
import java.util.*;


import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
            //REDO THIS
    //should return a "prettified" file
    //verify that prettified file is actually pretty
    //to do that, need to independently make it pretty
    //maybe you only need to look at the actual string...
    //wouldn;t check to make sure you are storing the new file in the rightr place...

    //have 2 copies of the file
    //read to file VS write to file
    /*
    //read in file as string
        String uglyString = FileUtils.readFileToString(fileName);

        //make "pretty" version of the string
        Gson gson = new GsonBuilder().setPrettyPrinting().setLenient().create();
        JsonElement je = JsonParser.parseString(uglyString);
        String prettyJsonString = gson.toJson(je);

        //make a file to put the "pretty" text in, if needed
        //C:\Users\jmast\pubgFilesExtracted
        File theDir = new File("C:\\Users\\jmast\\pubgFilesExtracted\\prettyFiles");
        if (!theDir.exists()) {
            theDir.mkdirs();
        }

        File prettyFile = new File("C:\\Users\\jmast\\pubgFilesExtracted\\prettyFiles\\" + fileName.getName());

        //write "pretty" text to new file
        FileUtils.writeStringToFile(prettyFile, prettyJsonString);
     */

    //check contents of known_pretty against attempted_pretty
    //Dependencies... --> want to be able to run this on a different computer, too...
    //

    //PRETTY example file: turn it into a string
    //maybe compare them line by line?
    //C:\Users\jmast\pubgFilesExtracted\testcaseFiles\jsonformatter.example.pretty_of_telemetryFile6.json.txt
    // // C:\Users\jmast\pubgFilesExtracted\testcaseFiles
    ////assertEquals(expectedPrettyString, actualPrettyString);
    void makePretty() throws IOException {

        File known_pretty = new File("C:\\Users\\jmast\\pubgFilesExtracted\\testcaseFiles\\jsonformatter.example.FourSpaces.pretty_of_telemetryFile2.json.txt");
        File uglyOriginal = new File("C:\\Users\\jmast\\pubgFilesExtracted\\telemetryFile2.json"); //original)
        File attempted_pretty = Main.makePretty(uglyOriginal);

        if (!known_pretty.exists() || !uglyOriginal.exists() || !attempted_pretty.exists()) {
            fail("Not all the needed files exist");
        }
        String expected = FileUtils.readFileToString(known_pretty);
        String actual = FileUtils.readFileToString(attempted_pretty);

        System.out.println("expected.length() : " + expected.length());
        System.out.println("actual.length()   : " + actual.length());
        System.out.println("DIFF: " + (expected.length() - actual.length()));

        //while each has next line, print it, check if they are eqaul (maybe onnly print it not eaul)
        Scanner scan = new Scanner(attempted_pretty);
        Scanner scan2 = new Scanner(known_pretty);
        int count = 0;
        while(scan2.hasNext() && count < 10000 )
        {
            String attempt = scan.next();
            String known = scan2.next();
            if(known.contains("\"vehicle\":")) //weird exception where some files have extra 2 lines saying vehicle is null
            {
                known = scan2.next();
                known = scan2.next();
            }

            if(!attempt.equalsIgnoreCase(known))
            {
                System.out.println("Attempt: " + attempt);
                System.out.println("Known:   " + known);
                System.out.println(count + 1);

                System.out.println("NOT EQUAL" );
            }
            System.out.println("------------------------");
            count++;
            //scan2.nextByte();
            //System.out.println("Attempt: " + attempt);
            //System.out.println("Known: " + known);

            //if(attempt.contentEquals(known))
            //{

            //}
           // else
            //{
                //System.out.println("------------------------------");
                //System.out.println("!=");
                //System.out.println(attempt);
                //System.out.println(known);
            //}

        }

        //System.out.println(attempted_pretty);
        //assertEquals(FileUtils.readFileToString(attempted_pretty),FileUtils.readFileToString(known_pretty));
        //Step 1: try to make it pretty
        //attempted_pretty = Main.makePretty(uglyOriginal);
        //System.out.println("Made attempted_pretty pretty");
        //actual = FileUtils.readFileToString(attempted_pretty);
        //System.out.println("Comparing... returns");
        //System.out.println(expected.compareTo(actual)); //-113 --> s1<s2 //-91 now
        //System.out.println(attempted_pretty); //prints: C:\Users\jmast\pubgFilesExtracted\testcaseFiles\jsonformatter.example.attempted_pretty_of_telemetryFile6.json.txt

        //System.out.println(actual);
        //System.out.println(expected);


    }
    @Test
    void printKillCounts() {
    }

    @Test
    void printKillCountsJSON() {
    }

    @Test
    void calculateKillCountsJSON() {
    }

    @Test
    void calculateKillCounts() {
    }

    @Test
    void ranking() {
    }

    @Test
    void winnerWeapons() {
    }

    @Test
    void returnMultipleObjects() {
    }

    @Test
    void returnObject() {
    }

    @Test
    void printPlayersByTeam() {
    }

    @Test
    void countBotsAndPeople() {
    }

    @Test
    void maps() {
    }

    @Test
    void printMapNames() {
    }

    @Test
    void getMapName() {
    }

    @Test
    void psuedoMain() {
    }

    @Test
    void getInfo() {
    }

    @Test
    void getMethods() {
    }

    @Test
    void printFunctionalities() {
    }

    @Test
    void initiateFunctionalities() {
    }

    @Test
    void getRequest() {
    }

    @Test
    void main() {
    }
}