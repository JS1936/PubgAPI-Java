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
    void makePretty() {

        //you need two files:

        //get the actual file
        File actual = new File("C:\\Users\\jmast\\pubgFilesExtracted\\telemetryFile6.json"); //original
        assertNotEquals(null, actual);

        //if actual file exists, copy contents into new file called expect
        if(actual.exists())
        {
            System.out.println("ACTUAL exists");
            File expect = new File(actual.getPath() + "test_pretty.json");//https://www.tabnine.com/code/java/methods/org.apache.commons.io.FileUtils/copyFile
            //FileUtils.write()

            try {
                FileUtils.copyFile(actual, expect); //src, dest //https://www.tabnine.com/code/java/methods/org.apache.commons.io.FileUtils/copyFile
                //S//tring fileString = actual.toString();
                //System.out.println(fileString);
                //System.out.println(expect.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }

            //File expect = new File("C:\\Users\\jmast\\test_if_pretty_for_telemetryFile6.json");

            String actualPrettyString = "";

            try {
                File actualResult = Main.makePretty(actual);
                actualPrettyString = FileUtils.readFileToString(actual);
            } catch (IOException e) {
                e.printStackTrace(); //"Can't test makePretty becuase of IOExcpetion in file 'actual'"
            }

            //JsonElement je = JsonParser.parseString(uglyString);
            //String prettyJsonString = gson.toJson(je);
            //JSON.stringify(jsonobj,null,'\t') //https://stackoverflow.com/questions/4810841/pretty-print-json-using-javascript
            Gson gson = new GsonBuilder().setPrettyPrinting().create(); //https://mkyong.com/java/how-to-enable-pretty-print-json-output-gson/
            //File prettyFile = gson.to
            String expectedPrettyString = gson.toJson(expect);
            try {
                FileUtils.writeStringToFile(expect, expectedPrettyString);
            } catch (IOException e) {
                e.printStackTrace();
            }


            //System.out.println("ACTUAL: ");
            System.out.println(actualPrettyString);

            //System.out.println("\n\n\nEXPECTED: ");
            System.out.println(expectedPrettyString);

            //check if string representation of file matches expected
            //assertEquals(expectedPrettyString, actualPrettyString);
            //assertEquals(expectedPrettyString, actualPrettyString);
            expect.delete();

        }
        else
        {
            System.out.println("ACTUAL does not exist");
            fail();
        }



    /*
        try {
            FileUtils.copyFile(expect , actual);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */





        //actualPrettyString
        //FileUtils
                //
                //writeFileToString();
        //FileUtils.writeStringToFile(prettyFile, prettyJsonString);
        //File expectedResult = new File(); //string child, File parent
        //FileUtils.writeStringToFile(expectedResult, json);


        //for each line, assert contents are equal



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