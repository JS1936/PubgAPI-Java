import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileAlreadyExistsException;

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
        //String pathToDir  = "C:\\Users\\jmast\\pubgFilesExtracted"; //look for copyOfSampleFile_JS1936.txt here...
        //File theDir = new File(pathToDir); //custom pathname?
        //if (!theDir.exists()) {
        //    theDir.mkdirs();
        //}
        System.out.println("filename.getName(): " + fileName.getName());
        //File prettyFile = new File(pathToDir + fileName.getName());
        File prettyFile = new File("C:\\Users\\jmast\\pubgFilesExtracted\\prettyFiles\\" + fileName.getName());

        //write "pretty" text to new file
        FileUtils.writeStringToFile(prettyFile, prettyJsonString);

        return prettyFile;
    }

    //Store contents of prettyFile in a String called file_content
    //Example of prettified file result: telemetry_json0cfba5cb-c088-488d-86fc-86458cb91b9d.json (in src/java/resources)
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

    //public static void deleteFile(File fileToDelete) throws IOException {
    //    writeToFileAndConsole("(deleteFile) File '" + fileToDelete.getAbsolutePath() + "' should be deleted when program terminates.");
    //    fileToDelete.deleteOnExit();
    //}

    //Pre: file exists and is in the file folder meant for "active" files; does NOT exist in file folder meant for inactive files
    //Post: moves fileName into file folder called inactiveFiles, should no longer exist in folder meant for active files (or whatever folder it was originally in)
    //UNTESTED
    //Seems to be working! (basic tests)
    //
    public static void inactivateFile(File fileToInactivate) throws IOException {
        System.out.println("SRC: " + fileToInactivate.getAbsolutePath());
        if(!fileToInactivate.exists())
        {
            throw new FileNotFoundException("Error: file not found.");
        }
        //System.out.println("LENGTH: " + fileToInactivate.length());
        if(fileToInactivate.length() == 0)
        {
            System.out.println("File has a length of 0.");
            return;
            //throw new IOException("Error: File has a length of 0.");
        }
        String fileName = fileToInactivate.getAbsolutePath();
        //String tail = "";
        for(int index = fileName.length() - 1; index >= 0; index--)
        {
            //System.out.println(fileName.substring(index));
            if(fileName.substring(index, index+1) == "\\")
            {
                //System.out.println("FILENAME: " + fileName);
                fileName = fileName.substring(0, index) + "\\" + fileName.substring(index+1);
                //tail += fileName.substring(index + 1);
                index = -1;
            }
        }
        String[] pathParts = fileToInactivate.getAbsolutePath().split("\\\\");
        String fileTail = pathParts[pathParts.length - 1];
        System.out.println("TAIL: " + fileTail);
        File inactive = new File("C:\\Users\\jmast\\pubgFilesExtracted\\inactiveFiles\\" + fileTail); //fileTail
        System.out.println("Attempted DEST: " + inactive.getAbsolutePath());
        if(!inactive.exists())
        {
            System.out.println("INACTIVE doesn't exist");
            //inactive.createNewFile();
            //return;
        }
        else
        {
            throw new FileAlreadyExistsException("File already exists in inactive");
        }
        System.out.println("About to write");
        System.out.println("Absolute path of fileToInactivate: " + fileToInactivate.getAbsolutePath());
        if(fileToInactivate.isFile())
        {
            System.out.println("IS FILE");
            if(fileToInactivate.exists())
            {
                System.out.println("EXISTS");
            }
        }
       // writeToFileAndConsole("(inactivateFolder) File '" + fileToInactivate.getAbsolutePath() + "' should be moved to inactiveFolder when program terminates.");
        System.out.println("About to move file");
        //FileUtils.moveFile(fileToInactivate, inactive);

        fileToInactivate.renameTo(inactive); //fileTail
        System.out.println("Post-move");
        //return fileToInactivate;
    }

    //Writes the given text both to the requestHistory file and to console.
    public static void writeToFileAndConsole(String text) throws IOException {
        System.out.println(text);
        FileUtils.writeStringToFile(Main.requestHistory, "\n" + text, (Charset) null, true);
    }
}
