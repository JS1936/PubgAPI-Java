import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

//add a method to return the whole file?
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
        return null; //_T object type not found
    }

    //Returns JSONObject in prettyFile corresponding to String type. "_T" in prettyFile precedes JSONObject name.
    public static JSONObject returnObject(File prettyFile, String type)
    {
        //System.out.println("NEW FILE_______________________________________________"); //Helps visually with debugging
        String file_content = FileManager.storeFileAsString(prettyFile);
        return getJSONObject(file_content, type);
    }

    //VERY similar to returnObject, but can return multiple occurrences of ONE type via vector (instead of limited to 1)
    public static Vector<JSONObject> returnMultipleObjects(File prettyFile, String type)
    {
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
        //return null; //What if _T type is not found?
        return multipleObjects;
    }
}
