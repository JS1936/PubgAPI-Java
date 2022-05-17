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
    void makePretty()
    {
        String expected = "";
        File known_pretty = new File("C:\\Users\\jmast\\pubgFilesExtracted\\testcaseFiles\\jsonformatter.example.pretty_of_telemetryFile6.json.txt");

        //makePrettyExample
        //for each line

        if(known_pretty.exists())
        {
            System.out.println("known_pretty file exists");
            try {
                expected = FileUtils.readFileToString(known_pretty);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println("known_pretty file does not exist");
            System.exit(0);
        }

        File attempted_pretty = new File("C:\\Users\\jmast\\pubgFilesExtracted\\telemetryFile6.json"); //original
        if(attempted_pretty.exists())
        {
            //Step 1: try to make it pretty
            try {
                Main.makePretty(attempted_pretty);
            } catch (IOException e) {
                //fail("error when calling Main.makePretty(attempted_pretty)");
                e.printStackTrace();
            }
        }
        //Do they ahve the same content?
        try {
            FileUtils.contentEquals(known_pretty, attempted_pretty);
            System.out.println("Content is equal");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Comparing the two files");
        //equal returns 0
        //<-

        System.out.println(known_pretty.compareTo(attempted_pretty));
        /*
        String actual = null;
        try {
            actual = FileUtils.readFileToString(attempted_pretty);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Cannot read file called actual into string");
        }
        System.out.println("HERE" );
        if(actual.equals(expected))
        {

            return;
        }
        else
        {
            //System.out.println("ACTUAL: " + actual);
            //System.out.println("EXPECT: " + expected);
            fail("The pretty_file was received, but is unexpectedly formatted");
        }
        */


        known_pretty.compareTo(attempted_pretty);
        //C:\Users\jmast\pubgFilesExtracted\testcaseFiles
    }
    /*
      //Step 2: Read each file to a string

        /*
        String expected = null;
        try {
            expected = FileUtils.readFileToString(known_pretty);
        } catch (IOException e) {
            e.printStackTrace();
            //fail("Cannot read file called expected into string");
        }
        */



    /*
    void makePretty() {
//C:\Users\jmast\pubgFilesExtracted\testcaseFiles
        //you need two files:

        //get the actual file
        File actual = new File("C:\\Users\\jmast\\pubgFilesExtracted\\telemetryFile6.json"); //original
        assertNotEquals(null, actual);

        //if actual file exists, copy contents into new file called expect
        if(actual.exists())
        {
            System.out.println("ACTUAL exists");
            File expect = new File(actual.getPath() + "test_pretty.json");//https://www.tabnine.com/code/java/methods/org.apache.commons.io.FileUtils/copyFile
            try {
                System.out.println("Trying to copy file");
                FileUtils.copyFile(actual, expect); //src, dest //https://www.tabnine.com/code/java/methods/org.apache.commons.io.FileUtils/copyFile
            } catch (IOException e) {
                e.printStackTrace();
            }

            //String expectedUglyString = "";
            //try {
             //   FileUtils.readFileToString(expect, expectedUglyString);
            //} catch (IOException e) {
             //   e.printStackTrace();
            //}

            //assertEquals(expectedUglyString)

            //File expect = new File("C:\\Users\\jmast\\test_if_pretty_for_telemetryFile6.json");

            String actualPrettyString = "";
            //actual.compareTo(expect);
            //get pretty string using makePretty
            //FileUtils.readLines(actual)

            try {
                File actualResult = Main.makePretty(actual);
                actualPrettyString = FileUtils.readFileToString(actual);
            } catch (IOException e) {
                e.printStackTrace(); //"Can't test makePretty becuase of IOExcpetion in file 'actual'"
            }

            //get pretty using testcode
            //JsonElement je = JsonParser.parseString(uglyString);
            //String prettyJsonString = gson.toJson(je);
            //JSON.stringify(jsonobj,null,'\t') //https://stackoverflow.com/questions/4810841/pretty-print-json-using-javascript
            //Gson gson = new GsonBuilder().setPrettyPrinting().create(); //https://mkyong.com/java/how-to-enable-pretty-print-json-output-gson/
            //File prettyFile = gson.to

           // String expectedPrettyString = gson.toJson(expect);
            //try {
            //    FileUtils.writeStringToFile(expect, expectedPrettyString);
            //} catch (IOException e) {
            //    e.printStackTrace();
            //} //make "pretty" version of the string
            String uglyString = null;
            try {
                uglyString = FileUtils.readFileToString(expect, uglyString);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Gson gson = new GsonBuilder().setPrettyPrinting().setLenient().create();
            JsonElement je = JsonParser.parseString(uglyString);
            String prettyJsonString = gson.toJson(je);
            String expectedPrettyString = prettyJsonString;

            //make a file to put the "pretty" text in, if needed
            //C:\Users\jmast\pubgFilesExtracted
            //File theDir = new File("C:\\Users\\jmast\\pubgFilesExtracted\\prettyFiles");
            //if (!theDir.exists()) {
            //    theDir.mkdirs();
           // }



            //System.out.println("ACTUAL: ");
            //System.out.println(actualPrettyString);

            //System.out.println("\n\n\nEXPECTED: ");
            //System.out.println(expectedPrettyString);

            ///assertEquals(actualPrettyString, expectedPrettyString); //right now it seems like one is pretty and the other is not...

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



    //}


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