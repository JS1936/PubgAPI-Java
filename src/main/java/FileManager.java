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
    static File activeFolder = new File("C:\\activeFolder");
    static File inactiveFolder = new File("C:\\inactiveFolder");

    //public static boolean isValidProposedPath(String path)
    //{
    //    return false;
    //}
    public static String getAbsolutePathToActiveFolder()
    {
        System.out.println("Absolute path to active folder: " + activeFolder.getAbsolutePath());
        return activeFolder.getAbsolutePath();
    }
    public static String getAbsolutePathToInactiveFolder()
    {
        System.out.println("Absolute path to inactive folder: " + inactiveFolder.getAbsolutePath());
        return inactiveFolder.getAbsolutePath();
    }
    public static void setAbsolutePathToActiveFolder(String proposedPath) throws IOException {
        File newDestForActiveFolder = new File(proposedPath);
        //if(!newDestForActiveFolder.exists())
        //{
        //    System.out.println("DEST does not yet exist");
         //   newDestForActiveFolder.mkdirs();
        //    newDestForActiveFolder.createNewFile();
       // }//
        System.out.println("Pre: " + getAbsolutePathToActiveFolder());

        //FileUtils.moveDirectory()
        //FileUtils.moveDirectoryToDirectory(activeFolder, newDestForActiveFolder);
        ///if(newDestForActiveFolder.exists())
        //{

        //}
        //FileUtils.moveDirectoryToDirectory(activeFolder, newDestForActiveFolder, true);
        //activeFolder.renameTo(newDestForActiveFolder); //f
        moveFile(activeFolder, newDestForActiveFolder);
        activeFolder.getAbsolutePath();
        //activeFolder.renameTo(newDestForActiveFolder);//. moveFile(activeFolder, newDestForActiveFolder);
        System.out.println("Post: " + getAbsolutePathToActiveFolder());
        //if(isValidProposedPath(proposedPath))
        //{
        //    activeFolder.renameTo(newDestForActiveFolder);
        //}
    }
    public static void setAbsolutePathToInactiveFolder(String proposedPath) throws IOException {
        File newDestForInactiveFolder = new File(proposedPath);
        moveFile(inactiveFolder, newDestForInactiveFolder);
    }

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

        System.out.println("filename.getName(): " + fileName.getName());
        File prettyFile = new File("C:\\Users\\jmast\\pubgFilesExtracted\\prettyFiles\\" + fileName.getName());

        //write "pretty" text to new file
        FileUtils.writeStringToFile(prettyFile, prettyJsonString);

        return prettyFile;
    }
    //^
    // make a file to put the "pretty" text in, if needed
    //C:\Users\jmast\pubgFilesExtracted
    //Remove this?
    //String pathToDir  = "C:\\Users\\jmast\\pubgFilesExtracted"; //look for copyOfSampleFile_JS1936.txt here...
    //File theDir = new File(pathToDir); //custom pathname?
    //if (!theDir.exists()) {
    //    theDir.mkdirs();
    //}

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

    //"Activating a file" is essentially done by "makePretty" right now... but the user can't control than manually
    //Move file from original location into folder where pubg pretty files are read from.
    //DEST:

    //Activate and inactivate are really just the same thing... moving a file. (And both could assume the file is already "pretty")

    //call with string (string name) or file itself?
    //Note: make this private helper?
    public static void moveFile(File src, File dest) throws IOException {
        System.out.println("src:" + src.getAbsolutePath());
        System.out.println("dest: " + dest.getAbsolutePath());
        if(!src.exists())
        {
            //src.mkdirs();
            //src.createNewFile();
            throw new FileNotFoundException("Error: src file not found");
        }
        if(dest.exists())
        {
            throw new FileAlreadyExistsException("Error: dest file already exists but should not exist yet");
        }
        System.out.println("SRC: " + src.getAbsolutePath());
        System.out.println("Attempted DEST: " + dest.getAbsolutePath());
        System.out.println("About to move file");
        src.renameTo(dest); //fileTail
        System.out.println("Post-move");
    }

    public static String getTailOfFile(File file)
    {
        String src_fileName = file.getAbsolutePath();
        for(int index = src_fileName.length() - 1; index >= 0; index--)
        {
            if(src_fileName.substring(index, index+1) == "\\")
            {
                src_fileName = src_fileName.substring(0, index) + "\\" + src_fileName.substring(index+1);
                index = -1;
            }
        }
        String[] pathParts = file.getAbsolutePath().split("\\\\");
        String fileTail = pathParts[pathParts.length - 1];
        System.out.println("TAIL: " + fileTail);
        return fileTail;
    }


    //Pre: assumes (for now, at least) that file is "pretty"
    public static void activateFile(File fileToActivate) throws IOException {
        System.out.println("Attempting to activate file");
        if(!fileToActivate.exists())
        {
            throw new FileNotFoundException("Error: file not found.");
        }
        String fileTail = getTailOfFile(fileToActivate);
        File active = new File( "C:\\Users\\jmast\\pubgFilesExtracted\\prettyFiles\\" + fileTail);
        moveFile(fileToActivate, active); //add bool to know if succeeded?
    }
    //Pre: file exists and is in the file folder meant for "active" files; does NOT exist in file folder meant for inactive files
    //Post: moves fileName into file folder called inactiveFiles, should no longer exist in folder meant for active files (or whatever folder it was originally in)
    //UNTESTED
    //Seems to be working! (basic tests)
    //
      /*
        String fileName = fileToInactivate.getAbsolutePath();
        for(int index = fileName.length() - 1; index >= 0; index--)
        {
            if(fileName.substring(index, index+1) == "\\")
            {
                fileName = fileName.substring(0, index) + "\\" + fileName.substring(index+1);
                index = -1;
            }
        }
        String[] pathParts = fileToInactivate.getAbsolutePath().split("\\\\");
        String fileTail = pathParts[pathParts.length - 1];
        System.out.println("TAIL: " + fileTail);

         */
       /*
        if(!inactive.exists())
        {
            System.out.println("INACTIVE doesn't exist");
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
        //Writing to file is currently causing nullpointer exception (for file)
        //writeToFileAndConsole("(inactivateFolder) File '" + fileToInactivate.getAbsolutePath() + "' should be moved to inactiveFolder when program terminates.");
        System.out.println("About to move file");
        fileToInactivate.renameTo(inactive); //fileTail
        System.out.println("Post-move");
        */
    public static void inactivateFile(File fileToInactivate) throws IOException { // /deactivate
        System.out.println("Attempting to in-/de-activate file");
        if(!fileToInactivate.exists())
        {
            throw new FileNotFoundException("Error: file not found.");
        }
        String fileTail = getTailOfFile(fileToInactivate);
        File inactive = new File("C:\\Users\\jmast\\pubgFilesExtracted\\inactiveFiles\\" + fileTail); //fileTail  //make this a global var?
        moveFile(fileToInactivate, inactive);
    }

    //Writes the given text both to the requestHistory file and to console.
    public static void writeToFileAndConsole(String text) throws IOException {
        System.out.println(text);
        FileUtils.writeStringToFile(Main.requestHistory, "\n" + text, (Charset) null, true);
    }
}



//File pretty = FileManager.makePretty(fileToActivate); //Note: may ALREADY be pretty...
//File[] files = new File("C:\\Users\\jmast\\pubgFilesExtracted").listFiles(); //Let user decide, though?