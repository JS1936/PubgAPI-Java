import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.UnexpectedException;

public class FileManagerTests {

    void checkIfExpectEqualsActual(String expect, String actual) throws UnexpectedException {
        if(!expect.equals(actual))
        {
            throw new UnexpectedException("(EXPECT)" + expect + " != " + actual + " (ACTUAL)");
        }
    }

    @Test
    //Assumes default original file and path is --> File activeFolder = new File("C:\\activeFolder");
    void getAbsolutePathToActiveFolder_Test() throws UnexpectedException {
        String expect = "C:\\activeFolder";
        String actual = FileManager.getAbsolutePathToActiveFolder();
        if(!expect.equals(actual))
        {
            throw new UnexpectedException("(EXPECT)" + expect + " != " + actual + " (ACTUAL)");
        }
    }
    @Test
    //Assumes default original file and path is --> File inactiveFolder = new File("C:\\inactiveFolder");
    void getAbsolutePathToInactiveFolder_Test() throws UnexpectedException {
        String expect = "C:\\inactiveFolder";
        String actual = FileManager.getAbsolutePathToInactiveFolder();
        checkIfExpectEqualsActual(expect, actual);
    }

    //@Test
    //void printListOfActiveFiles_Test()
    //{
    //    FileManager.printListOfActiveFiles();
    //}
    @Test
    //Seems to be working
    void setAbsolutePathToActiveFolder_TestSendingFile() throws IOException {
        String newPath = "C:\\sampleFile\\path\\newPathToActiveFolder";
        //System.out.println(FileManager.getAbsolutePathToInactiveFolder());
        FileManager.moveFileToPath(FileManager.activeFolder, newPath);
        //System.out.println(FileManager.getAbsolutePathToInactiveFolder());
        //checkIfExpectEqualsActual(FileManager.activeFolder.getAbsolutePath(), newPath);
    }

    @Test
    //Assumes inactiveFolder exists and file at newPath does not yet exist.
    void setAbsolutePathToInactiveFolder_TestSendingFile() throws IOException {
        String newPath = "C:\\sampleFile\\path\\newPathToInactiveFolder";
        FileManager.moveFileToPath(FileManager.inactiveFolder, newPath);
    }

    @Test
    //Assumes activeFolder exists and file at newPath does not yet exist.
    void setAbsolutePathToActiveFolder_Test() throws IOException {
        String newPath = "C:\\sampleFile\\path\\newPathToActiveFolder";
        FileManager.moveFileToPath(FileManager.activeFolder, newPath);
    }

    /*
    @Test
    void setAbsolutePathToInactiveFolder_Indirect() throws IOException {
        String newPath = "C:\\sampleFile\\path\\newPathToInactiveFolder";
        FileManager.setAbsolutePathToInactiveFolder(newPath);// throws IOException {
    }

    @Test
    void setAbsolutePathToActiveFolder_Indirect() throws IOException {
        String newPath = "C:\\sampleFile\\path\\newPathToActiveFolder";
        FileManager.setAbsolutePathToActiveFolder(newPath);// throws IOException {
    }
    */


    @Test
    void activateFile() throws IOException {
        System.out.println("Attempting to activate file");
        File f = new File("C:\\Users\\jmast\\pubgFilesExtracted\\testcaseFiles\\testcaseFile_activate.txt");
        if(f.exists())
        {
            System.out.println("File exists!");
        }
        else
        {
            System.out.println("File did not exist. Making it now.");
            FileManager.makeTheFileExist(f);
            //throw new FileNotFoundException("Error: File not found.");
        }
        FileManager.activateFile(f);
    }
    @Test
    void inactivateFile() throws IOException {
        System.out.println("Attempting to inactivate file...");
        File f = new File("C:\\Users\\jmast\\pubgFilesExtracted\\prettyFiles\\testcaseFile_activate.txt");
        //File f = new File("C:\\Users\\jmast\\pubgFilesExtracted\\testcaseFiles\\testcaseFile_inactivate.txt");
        //////"C:\\MainTest-inactivateFile-sampleFolder\\testcaseFile_inactivate.txt");
        ////C:\Users\jmast\pubgFilesExtracted\testcaseFiles
        if(f.exists())
        {
            System.out.println("Found the file!");
            FileManager.inactivateFile(f);
        }
        else
        {
            throw new FileNotFoundException("Error: File not found.");
        }
    }
}

 /*
    @Test
    void setAbsolutePathToInactiveFolder() throws IOException {
        System.out.println("Current path: " + FileManager.getAbsolutePathToInactiveFolder());
        //getAbsolutePathToInactiveFolder_Test();
        String newPath = "C:\\sample\\path\\newPathToInactiveFolder";
        String expect = newPath;
        FileManager.setAbsolutePathToInactiveFolder(newPath);
        String actual = FileManager.getAbsolutePathToInactiveFolder();
        checkIfExpectEqualsActual(expect, actual);
    }

     */
    /*
    public static void setAbsolutePathToActiveFolder(String proposedPath) throws FileAlreadyExistsException, FileNotFoundException {
        File newDestForActiveFolder = new File(proposedPath);
        moveFile(activeFolder, newDestForActiveFolder);
        //if(isValidProposedPath(proposedPath))
        //{
        //    activeFolder.renameTo(newDestForActiveFolder);
        //}
    }
    public static void setAbsolutePathToInactiveFolder(String proposedPath) throws FileAlreadyExistsException, FileNotFoundException {
        File newDestForInactiveFolder = new File(proposedPath);
        moveFile(inactiveFolder, newDestForInactiveFolder);
    }
    */
   /*
    public static File inactivateFile(File fileToInactivate) throws IOException {
        String fileName = fileToInactivate.getAbsolutePath();
        for(int index = fileName.length() - 1; index >= 0; index--)
        {
            System.out.println("FILENAME: " + fileName);
            if(fileName.substring(index, index+1) == "\\")
            {
                fileName = fileName.substring(0, index) + "\\" + fileName.substring(index+1);
            }
        }
        String[] pathParts = fileToInactivate.getAbsolutePath().split("\\\\");
        String fileTail = pathParts[pathParts.length - 1];
        writeToFileAndConsole("(inactivateFolder) File '" + fileToInactivate.getAbsolutePath() + "' should be moved to inactiveFolder when program terminates.");
        fileToInactivate.renameTo(new File("C:\\Users\\jmast\\pubgFilesExtracted\\inactiveFiles\\" + fileTail));
    }
     */
