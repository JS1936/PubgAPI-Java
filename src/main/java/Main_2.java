import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

//public static File makePretty(File fileName) throws IOException {
//public static String storeFileAsString(File prettyFile)
//public static JSONObject getJSONObject(String object_type, String file_content)
//public static JSONObject returnObject(File prettyFile, String type)


public class Main_2 {

    static File currentFile = null; //added 9/15
    static File requestHistory = null;

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
        String pathToDir  = "C:\\Users\\jmast\\pubgFilesExtracted\\prettyFiles";
        File theDir = new File(pathToDir); //custom pathname?
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
        File prettyFile = new File(pathToDir + fileName.getName());

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

    public static JSONObject getJSONObject(String file_content, String object_type)
    {
        JSONArray jsonArray = new JSONArray(file_content);
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String type_T = jsonObject.getString("_T");

            if (type_T.equalsIgnoreCase(object_type)) {
                System.out.println(object_type);
                return jsonObject;

            }
        }
        return null; //What if _T type is not found?
    }
    //Returns JSONObject in prettyFile corresponding to String type. "_T" in prettyFile precedes JSONObject name.
    //WHAT IF you could search through a prettyfile for any _T and that method would return the contents?
    public static JSONObject returnObject(File prettyFile, String type)
    {
        System.out.println("NEW FILE_______________________________________________"); //remove?
        String file_content = storeFileAsString(prettyFile);
        return getJSONObject(file_content, type);
    }
}



