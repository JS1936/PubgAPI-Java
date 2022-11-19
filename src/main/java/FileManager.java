import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static java.nio.file.Files.copy;

/*
 * The FileManager class allows access to and manipulation of files.
 *
 * Functionalities as of 10/5/2022:
 *          - getAbsolutePathToActiveFolder
 *          - getAbsolutePathToInactiveFolder
 *          - makeTheFileExist
 *          - moveFileToPath
 *          - makePretty
 *          - storeFileAsString
 *          - getFile
 *          - printWhetherFileExists
 *          - moveFile
 *          - getTailOfFile
 *          - activateFile
 *          - inactivateFile
 *          - writeToFileAndConsole
 */
//TO-DO: Preferably, allow only one instance of FileManager class (singleton).
//TODO: simplify class.
public class FileManager {

    //TODO: update storage use of "activeFolder" and "inactiveFolder"
    //static File activeFolder = new File("C:\\activeFolder");
    //static File inactiveFolder = new File("C:\\inactiveFolder");
    //Remove: static File activeFolder = new File("requestsDir/" + this.player + "/timestamp_" + this.timestamp);



    //Returns a String holding the absolute path to file activeFolder.
   // public static String getAbsolutePathToActiveFolder()
   // {
   //     System.out.println("Absolute path to active folder: " + activeFolder.getAbsolutePath());
   //     return activeFolder.getAbsolutePath();
   // }

    //Returns a String holding the absolute path to file inactiveFolder.
    //public static String getAbsolutePathToInactiveFolder()
   // {
   //     System.out.println("Absolute path to inactive folder: " + inactiveFolder.getAbsolutePath());
   //     return inactiveFolder.getAbsolutePath();
   // }

    /*
    public static void printListOfActiveFiles()
    {
        System.out.println("active folder = " + activeFolder.getAbsolutePath());
        String newPath = "C:\\sampleFile\\path\\newPathToActiveFolder";
        moveFileToPath(activeFolder, newPath);
        System.out.println("active folder = " + activeFolder.getAbsolutePath());
        if(!activeFolder.isDirectory())
        {
            System.out.println("active folder is not a directory");
            return;
        }
        File[] listFiles_activeFolder = activeFolder.listFiles();
        System.out.println(listFiles_activeFolder.toString());
    }
    */

    /*
    public static void setAbsolutePathToInactiveFolder(String newPath) throws IOException {
        moveFile(inactiveFolder, new File(newPath));
    }

    public static void setAbsolutePathToActiveFolder(String newPath) throws IOException {
        moveFile(activeFolder, new File(newPath));
    }
     */

    //Changing folder to something that already exists, or making a new one?

    //ASSUMES: src file exists
    //ASSUMES: file does not yet exist at path destPath
    //Helps moveFileToPath

    //Assumes src file does not yet exist. Creates it.
    public static void makeTheFileExist(File src)
    {
        try {
            src.mkdirs();
            src.createNewFile();
        } catch (IOException e) {
            //throw new FileNotFoundException("Error: Folder to move does not exist.");//To move a folder, it needs to exist.");
            e.printStackTrace();
        }
    }

    //If possible, moves src folder (and nested contents) to a new path described by destPath.
    /*
    public static void moveFileToPath(File src, String destPath) {//throws FileNotFoundException {
        //src.listFiles();
        if(!src.exists())
        {
            makeTheFileExist(src);
        }

        File dest = new File(destPath);
        if(dest.exists())
        {
            //System.out.println("Deleting dest file (should not exist yet).");
            dest.delete();
        }
        try {
            FileUtils.moveDirectory(src, new File(destPath));
            //copy and delete?
            System.out.println("Trial move was attempted");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
     */
    //1) Does activeFolder exist?
    //2) Does currentlyInactive exist?
    //3) If both exist, copy the contents of activeFolder to currentlyInactive.
    //4) Delete activeFolder if it exists.
    //5) Assign activeFolder to be the currentlyInactive folder/file.
    /*
    public static void setAbsolutePathActiveFolder_FolderExistsAlready(File currentlyInactive) throws IOException {
        System.out.println("Attempting to copy directory from active folder to currently inactive");
        if(activeFolder.exists())
        {
            System.out.println("activeFolder file exists");
        }
        else
        {
            if(activeFolder.isFile())
            {
                System.out.println("activeFolder is a file");
            }
            //System.out.println(activeFolder.getAbsolutePath());
            //activeFolder.createNewFile();// = new File("C:\\activeFolder");
            //activeFolder = getFile( "C:\\activeFolder");
            System.out.println("activeFolder file does not exist");
        }
        System.out.println("DEST: " + currentlyInactive.getAbsolutePath());
        FileUtils.copyDirectory(activeFolder, currentlyInactive);
        Files.deleteIfExists(activeFolder.toPath());
        activeFolder = currentlyInactive;
    }
*/

    /*
    public static void setAbsolutePathToActiveFolder(String proposedPath) throws IOException {
        System.out.println("Proposed path: " + proposedPath);
        File newDestForActiveFolder = new File(proposedPath);
        if(!newDestForActiveFolder.exists())
        {
            System.out.println("DEST does not yet exist");
         //   newDestForActiveFolder.mkdirs();
        //    newDestForActiveFolder.createNewFile();
       }//
        ///System.out.println("Pre: " + getAbsolutePathToActiveFolder());

        //FileUtils.moveDirectory()
        //FileUtils.moveDirectoryToDirectory(activeFolder, newDestForActiveFolder);
        ///if(newDestForActiveFolder.exists())
        //{

        //}
        //FileUtils.moveDirectoryToDirectory(activeFolder, newDestForActiveFolder, true);
        //activeFolder.renameTo(newDestForActiveFolder); //f
        moveFile(activeFolder, newDestForActiveFolder);
        ///activeFolder.getAbsolutePath();
        //activeFolder.renameTo(newDestForActiveFolder);//. moveFile(activeFolder, newDestForActiveFolder);
        ///System.out.println("Post: " + getAbsolutePathToActiveFolder());
        //if(isValidProposedPath(proposedPath))
        //{
        //    activeFolder.renameTo(newDestForActiveFolder);
        //}
    }

     */
    /*
    public static void setAbsolutePathToInactiveFolder(String proposedPath) throws IOException {

        File newDestForInactiveFolder = new File(proposedPath);
        moveFile(inactiveFolder, newDestForInactiveFolder);
        //FileUtils.moveDirectoryToDirectory(inactiveFolder, newDestForInactiveFolder, false);
    }

     */

    /*
     * Takes the contents of an "ugly" file and makes a new file
     * where that content is stored in a way that looks "pretty" (formatted).
     * Returns "prettified" file.
     */
    public static File makePretty(File fileName) throws IOException {

        //read in file as string
        String uglyString = FileUtils.readFileToString(fileName);

        //make "pretty" version of the string
        Gson gson = new GsonBuilder().setPrettyPrinting().setLenient().create();
        JsonElement je = JsonParser.parseString(uglyString);
        String prettyJsonString = gson.toJson(je);

        System.out.println("filename.getName(): " + fileName.getName());
        System.out.println("filename.getPath(): " + fileName.getPath());
        System.out.println("filename.getParentFile():" + fileName.getParentFile());
        //File prettyFile = new File("C:\\Users\\jmast\\pubgFilesExtracted\\prettyFiles\\" + fileName.getName());
        File prettyFile = new File(fileName.getPath());
        //File prettyFile = new File(fileName.getPath() + "-userFriendly");

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


    public static void printWhetherFileExists(File file)
    {
        System.out.println("FILE: " + file.getAbsolutePath());
        if(file.exists())
        {
            System.out.println("File exists");
        }
        else
        {
            System.out.println("File does not exist.");
        }
    }

    //Move File src (and nested files) to exist at location for File dest
    //call with string (string name) or file itself?
    //Note: make this private helper?
    /*
    public static void moveFile(File src, File dest) throws IOException {

        Path sourcePath = Path.of(src.getPath());
        Path destPath = Path.of(dest.getPath());

        System.out.println("sourcePath: " + sourcePath);
        System.out.println("destPath: " + destPath);

        System.out.println("DEST: " + dest.getAbsolutePath());
        src = getFile(src.getAbsolutePath());
        if(dest.exists())
        {

            System.out.println("DEST file exists");
        }
        else
        {
            dest.mkdirs();
            dest.createNewFile();
            System.out.println("DEST file does not exist");
        }
        //dest = getFile(dest.getAbsolutePath());
        printWhetherFileExists(src);
        printWhetherFileExists(dest);
        System.out.println("source path's file name: " + sourcePath.getFileName());
        //dest.delete();
        //Files.move(sourcePath, destPath);

        //https://www.codejava.net/java-se/file-io/how-to-rename-move-file-or-directory-in-java
        copy(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);
        //Files.deleteIfExists(sourcePath);
        //Files.move(sourcePath, destPath,
        //        StandardCopyOption.REPLACE_EXISTING);
        /*
        System.out.println("src:" + src.getAbsolutePath());
        System.out.println("dest: " + dest.getAbsolutePath());
        if(!src.exists())
        {
            src.mkdirs();
            src.createNewFile();
            throw new FileNotFoundException("Error: src file not found");
        }
        if(dest.exists())
        {

            dest.deleteOnExit();
            throw new FileAlreadyExistsException("Error: dest file already exists but should not exist yet");
        }
        System.out.println("SRC: " + src.getAbsolutePath());
        System.out.println("Attempted DEST: " + dest.getAbsolutePath());
        System.out.println("About to move file");
        boolean isRenamed = src.renameTo(dest); //fileTail


        if(isRenamed)
        {
            System.out.println("isRenamed");
        }
        else
        {
            System.out.println("is not renamed");
        }


        System.out.println("Post-move");
    }
    */

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
    //Pre: fileToActivate should exist
    /*
    public static void activateFile(File fileToActivate) throws IOException {
        System.out.println("Attempting to activate file");
        if(!fileToActivate.exists())
        {
            throw new FileNotFoundException("Error: file not found.");
        }
        String fileTail = getTailOfFile(fileToActivate);
        //TODO: (activateFile) remove reliance on the specific file locations and files pre-downloaded locally
        File active = new File( "C:\\Users\\jmast\\pubgFilesExtracted\\prettyFiles\\" + fileTail);
        moveFile(fileToActivate, active); //add bool to know if succeeded?
    }
    */
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
    /*
    public static void inactivateFile(File fileToInactivate) throws IOException { // /deactivate
        System.out.println("Attempting to in-/de-activate file");
        if(!fileToInactivate.exists())
        {
            throw new FileNotFoundException("Error: file not found.");
        }
        String fileTail = getTailOfFile(fileToInactivate);
        //TODO: (inactivateFile) remove reliance on the specific file locations and files pre-downloaded locally
        File inactive = new File("C:\\Users\\jmast\\pubgFilesExtracted\\inactiveFiles\\" + fileTail); //fileTail  //make this a global var?
        moveFile(fileToInactivate, inactive);
    }
     */

    //Writes the given text both to the requestHistory file and to console.
    public static void writeToFileAndConsole(String text) throws IOException {
        System.out.println(text);
        FileUtils.writeStringToFile(Main.requestHistory, text + "\n", (Charset) null, true);
        //Note: ln, \n are affecting printout for (1)
        //Would "text + \n" work? (Rather than "\n + text")
    }
}



//File pretty = FileManager.makePretty(fileToActivate); //Note: may ALREADY be pretty...
//File[] files = new File("C:\\Users\\jmast\\pubgFilesExtracted").listFiles(); //Let user decide, though?

//public static void deleteFile(File fileToDelete) throws IOException {
//    writeToFileAndConsole("(deleteFile) File '" + fileToDelete.getAbsolutePath() + "' should be deleted when program terminates.");
//    fileToDelete.deleteOnExit();
//}

//"Activating a file" is essentially done by "makePretty" right now... but the user can't control than manually
//Move file from original location into folder where pubg pretty files are read from.
//DEST:

//Activate and inactivate are really just the same thing... moving a file. (And both could assume the file is already "pretty")


//TO-DO: Move pubgFilesExtracted telemetry files into appropriate folders (active, inactive)
//          -Note: not code. Just move them manually for now.