import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

public class JSONManager {
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
        String file_content = FileManager.storeFileAsString(prettyFile);
        return getJSONObject(file_content, type);
    }
    //VERY similar to returnObject, but can return multiple occurrences of a type via vector (instead of limited to 1)
    public static Vector<JSONObject> returnMultipleObjects(File prettyFile, String type)
    {
        Vector<JSONObject> multipleObjects = new Vector<JSONObject>();
        System.out.println("NEW FILE_______________________________________________"); //remove?
        //Store contents of prettyFile in a String called file_content
        String file_content = "";
        try {
            file_content = FileUtils.readFileToString(prettyFile);
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Return portion of file that matches given String type
        System.out.println(type);
        JSONArray jsonArray = new JSONArray(file_content);
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String type_T = jsonObject.getString("_T");

            if (type_T.equalsIgnoreCase(type)) {
                //System.out.println(type);
                multipleObjects.add(jsonObject);
                //return jsonObject;
            }
        }
        //return null; //What if _T type is not found?
        return multipleObjects;
    }
}
