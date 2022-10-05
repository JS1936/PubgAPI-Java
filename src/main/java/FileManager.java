import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;

//add file? Remove file?
public class FileManager {
    //constructor? //singleton?

    /*
     * Takes the contents of an "ugly" file and makes a new file
     * where that content is stored in a way that looks "pretty" (formatted).
     */
    public static File makePretty(File fileName) throws IOException {

        //read in file as string
        String uglyString = FileUtils.readFileToString(fileName);

        //make "pretty" version of the string
        Gson gson = new GsonBuilder().setPrettyPrinting().setLenient().create();
        JsonElement je = JsonParser.parseString(uglyString);
        String prettyJsonString = gson.toJson(je);

        //make a file to put the "pretty" text in, if needed
        //C:\Users\jmast\pubgFilesExtracted
        //Remove this?
        String pathToDir  = "C:\\Users\\jmast\\pubgFilesExtracted"; //look for copyOfSampleFile_JS1936.txt here...
        File theDir = new File(pathToDir); //custom pathname?
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
        /*
        filename.getName(): prettyFilescopyOfSampleFile_JS1936.txt
#bots:       0 / 0
C:\Users\jmast\pubgFilesExtracted\prettyFilesprettyFilescopyOfSampleFile_JS1936.txt
filename.getName(): prettyFilesprettyFilescopyOfSampleFile_JS1936.txt
#bots:       0 / 0
C:\Users\jmast\pubgFilesExtracted\prettyFilesprettyFilesprettyFilescopyOfSampleFile_JS1936.txt
filename.getName(): prettyFilesprettyFilesprettyFilescopyOfSampleFile_JS1936.txt
#bots:       0 / 0
C:\Users\jmast\pubgFilesExtracted\prettyFilesprettyFilesprettyFilesprettyFilesprettyFilesprettyFilesprettyFilesprettyFilesprettyFilesprettyFilesprettyFilestelemetryFile0.json
filename.getName(): prettyFilesprettyFilesprettyFilesprettyFilesprettyFilesprettyFilesprettyFilesprettyFilesprettyFilesprettyFilesprettyFilestelemetryFile0.json
#bots:       10 / 85
         */
        System.out.println("filename.getName(): " + fileName.getName());
        //File prettyFile = new File(pathToDir + fileName.getName());
        File prettyFile = new File("C:\\Users\\jmast\\pubgFilesExtracted\\prettyFiles\\" + fileName.getName());

        //write "pretty" text to new file
        FileUtils.writeStringToFile(prettyFile, prettyJsonString);

        return prettyFile;
    }

    //Store contents of prettyFile in a String called file_content
    public static String storeFileAsString(File prettyFile)
    {
        String file_content = "";
        try {
            file_content = FileUtils.readFileToString(prettyFile);
        } catch (FileNotFoundException e) {
            System.out.println("Error: file not found.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file_content;
    }

    //If a file called fileName exists, returns it. If it does not exist, creates one and returns it.
    public static File getFile(String fileName)
    {
        File file = new File(fileName);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    //Writes the given text both to the requestHistory file and to console.
    public static void writeToFileAndConsole(String text) throws IOException {
        System.out.println(text);
        FileUtils.writeStringToFile(Main.requestHistory, "\n" + text, (Charset) null, true);
    }
}
