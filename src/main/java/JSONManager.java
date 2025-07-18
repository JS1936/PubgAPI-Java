import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.Vector;

/*
 * The JSONManager class helps with searching through PUBG match telemetry files and returning specific chunks.
 */
public class JSONManager {

    /*
     * Retrieves JSONObject of object_type for method returnObject(File prettyFile, String type).
     * Returns null if object_type not found in file_content.
     * Example object_type: "LogMatchDefinition"
     */
    public static JSONObject getJSONObject(String file_content, String object_type)
    {
        JSONArray jsonArray = new JSONArray(file_content);
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String type_T = jsonObject.getString("_T");

            if (type_T.equalsIgnoreCase(object_type)) {
                return jsonObject;
            }
        }
        return null; //_T object type not found
    }

    /*
     * Returns JSONObject in prettyFile corresponding to String type. "_T" in prettyFile precedes JSONObject name.
     */
    public static JSONObject returnObject(File prettyFile, String type)
    {
        //System.out.println("NEW FILE_______________________________________________"); //Helps visually with debugging
        String file_content = FileManager.storeFileAsString(prettyFile);
        return getJSONObject(file_content, type);
    }

    /*
     * VERY similar to returnObject, but can return multiple occurrences of ONE type via vector (instead of limited to 1).
     * Assumes valid type (_T type). Example type: "LogPlayerKillV2". If invalid type, expect empty vector returned.
     */
    public static Vector<JSONObject> returnMultipleObjects(File prettyFile, String type)
    {
        System.out.println("type = " + type);
        //System.out.println("NEW FILE_______________________________________________"); //Helps visually with debugging
        Vector<JSONObject> multipleObjects = new Vector<JSONObject>();
        String file_content = FileManager.storeFileAsString(prettyFile);

        //Return portions of file that matches given String type
        JSONArray jsonArray = new JSONArray(file_content);
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String type_T = jsonObject.getString("_T");

            if (type_T.equalsIgnoreCase(type)) {
                multipleObjects.add(jsonObject);
            }
        }
        return multipleObjects;
    }

    /* 
     * Returns value from a particular JSON object section.
     */
    public static String getJSONValue(File prettyFile, String section, String key) 
    {
        JSONObject JSON = JSONManager.returnObject(prettyFile, section);
        return JSON.get(key).toString();
    }
}