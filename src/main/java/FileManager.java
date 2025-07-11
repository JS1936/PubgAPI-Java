import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;

/*
 * The FileManager class allows access to and manipulation of files.
 *
 * Functionalities as of 12/17/2022:
 *          - makePretty
 *          - storeFileAsString
 *          - createNewFileAndParentFilesIfTheyDoNotExist
 *          - writeToFileAndConsole
 */
//TO-DO: Preferably, allow only one instance of FileManager class (singleton).
//TODO: simplify class.
public class FileManager {

    /*
     * Takes the contents of an "ugly" file and makes a new file
     * where that content is stored in a way that looks "pretty" (formatted).
     * Returns "prettified" file.
     */
    public static File makePretty(File fileName) throws IOException {

        //read in file as string
        String uglyString = FileUtils.readFileToString(fileName, "UTF-8");
        System.out.println("filename = " + fileName);

        //make "pretty" version of the string
        Gson gson = new GsonBuilder().setPrettyPrinting().setLenient().create();
        JsonElement je = JsonParser.parseString(uglyString);
        String prettyJsonString = gson.toJson(je);

        File prettyFile = new File(fileName.getPath());

        //write "pretty" text to new file
        FileUtils.writeStringToFile(prettyFile, prettyJsonString, "UTF-8");

        return prettyFile;
    }

    //Store contents of prettyFile in a String called file_content
    //Example of prettified file result: telemetry_json0cfba5cb-c088-488d-86fc-86458cb91b9d.json (in src/java/resources)
    public static String storeFileAsString(File prettyFile)
    {
        String file_content = "";
        try {
            file_content = FileUtils.readFileToString(prettyFile, "UTF-8");
        } catch (FileNotFoundException e) {
            System.out.println("Error: file not found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error: IOException.");
            e.printStackTrace();
        }
        return file_content;
    }

    //Creates a new file and parent files if they do not exist.
    public static void createNewFileAndParentFilesIfTheyDoNotExist(File file) throws IOException {
        if(file.exists()) {
            System.out.println("Response file exists!");
        }
        else {
            System.out.println("Response file does not exist. Creating it now");
            if(file.getParentFile() != null) {
                file.getParentFile().mkdirs(); //check/create parent, then self
            }
            file.createNewFile();
        }
    }

    //Writes the given text both to the requestHistory file and to console. Both outputs conclude with a new line.
    public static void writeToFileAndConsole(String text) throws IOException {
        System.out.println(text);
        FileUtils.writeStringToFile(Main.requestHistory, text + "\n", (Charset) null, true);
    }

    /*
     * Writes the given text to the file Main.requestHistory. Also writes the text to console.
     * If newLineAfter = true, the output concludes with a new line. Otherwise, the line will continue upon next output.
     */
    public static void writeToFileAndConsole(String text, boolean newLineAfter) throws IOException {
        System.out.print(text);
        FileUtils.writeStringToFile(Main.requestHistory, text, (Charset) null, true);

        if(newLineAfter)
        {
            System.out.println();
            FileUtils.writeStringToFile(Main.requestHistory, "\n", (Charset) null, true);
        }
    }

}

//Note: for writeToFileAndConsole --> ln, \n are affecting printout for (1). Would "text + \n" work? (Rather than "\n + text")