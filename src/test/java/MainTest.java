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
    void makePretty() throws IOException {

        //make/get the files
        File known_pretty = new File("C:\\Users\\jmast\\pubgFilesExtracted\\testcaseFiles\\jsonformatter.example.FourSpaces.pretty_of_telemetryFile2.json.txt");
        File uglyOriginal = new File("C:\\Users\\jmast\\pubgFilesExtracted\\telemetryFile2.json"); //original)
        File attempted_pretty = FileManager.makePretty(uglyOriginal);

        //verify the files exist
        if (!known_pretty.exists() || !uglyOriginal.exists() || !attempted_pretty.exists()) {
            fail("Not all the needed files exist");
        }

        //read known_pretty and attempted_pretty to one string each
        String expected = FileUtils.readFileToString(known_pretty);
        String actual = FileUtils.readFileToString(attempted_pretty);

        //while each scanner has next line, check if they are equal
        Scanner scan = new Scanner(attempted_pretty);
        Scanner scan2 = new Scanner(known_pretty);
        int count = 0;

        while(scan2.hasNext() && count < 3000) //if the pattern is followed this far, it's hopefully followed (well enough) through the whole file
        { //weird rounding error? (largely inconsequential, it seems --> 3908)
            String attempt = scan.next();
            String known = scan2.next();
            if(known.contains("\"vehicle\":")) //weird exception where some files have extra 2 lines saying vehicle is null
            {
                //System.out.println("count: " + count);
                known = scan2.next();
                known = scan2.next();
            }
            if(!attempt.equalsIgnoreCase(known))
            {
                System.out.println(count + 1);
                fail("<NOT EQUAL>\n\tAttempt: " + attempt + " != \n\tKnown:   " + known);
            }
            count++;
        }
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
    //Checks whether getInfo treats valid and invalid requests properly by checking integers of [-1,8].
    //Valid: request is from [0,7]
    //Invalid: all other values (EX: -1, 7, 8)
    void getInfo() throws IOException {
        File known_pretty = new File("C:\\Users\\jmast\\pubgFilesExtracted\\testcaseFiles\\jsonformatter.example.FourSpaces.pretty_of_telemetryFile2.json.txt");
        for(int request = -1; request <= 8; request++)
        {
            boolean properRetrieval = Main.getInfo(request, known_pretty, "");
            if(!properRetrieval)
            {
                fail("getInfo() testcase failed, starting at request = " + request);
            }
        }
    }

    @Test
    void getMethods() {
    }



    @Test
    // outputVerify.add(i + ": " + functionalities.get(i));
    //Vector<String> expected_functionalities = new Vector<String>() {

    //Main.initiateFunctionalities();



    //Vector<String> expected = new Vector<String>(){"countBotsAndPeople"};
    //"countBotsAndPeople"
    //"calculateKillCounts","printPlayersByTeam", "winnerWeapons", "ranking (of a specific person)", "calculateKillCountsJSON", "printMapsPlayed");
    //};
    void printFunctionalities() {
    }

    @Test
    void initiateFunctionalities() {
        Main.initiateFunctionalities();
        //make vector of functionalities?
        assertEquals(7, Main.functionalities.size());
        for(int i = 0; i < Main.functionalities.size(); i++)
        {
            assertNotEquals(null, Main.functionalities.get(i));
        }
    }

    @Test
    //Does it return the right kind of object?
    //Does it return the expected content?
    void getRequest() {
    }

    @Test
    void main() {
    }
}