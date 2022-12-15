import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Vector;

public class API_Request_MatchesListed extends API_Request {




    public API_Request_MatchesListed(String player, int matchLimit) throws IOException {
        super();
    }

    public File doRequest_returnFileForMatchesListed() throws IOException {
        this.connection = connectToAPI(this.recentMatches);
        Path summary_Path = Path.of(specificRequest + "/summary_matchList"); //no .txt here (don't want duplicate)

        //save/create summary file
        storeResponseToSpecifiedFileLocation(summary_Path.toString());
        File summary_File = new File(summary_Path.toString() + ".json");
        FileManager.createNewFileAndParentFilesIfTheyDoNotExist(summary_File);
        return summary_File;
    }

    public Vector<String> getMatchIDsFromRequestPath(File s) throws IOException
    {
        if(!s.isFile())
        {
            System.out.println("Error: file " + s.getAbsolutePath() + " should be a file but is not a file.");
            System.exit(0);
        }
        System.out.println("Attempting to get match ids from request file");
        File s_pretty = FileManager.makePretty(s);

        System.out.println("Pretty path: " + s_pretty.getAbsolutePath());

        String fileAsString = FileManager.storeFileAsString(s_pretty);
        System.out.println(fileAsString);

        JSONObject jsonObject = new JSONObject(fileAsString);
        JSONArray jsonArrayOfMatches = jsonObject.getJSONArray("data").getJSONObject(0).getJSONObject("relationships").getJSONObject("matches").getJSONArray("data");
        System.out.println("jsonArray.length = " + jsonArrayOfMatches.length());
        Vector<String> match_ids = new Vector<String>();

        for(int i = 0; i < jsonArrayOfMatches.length(); i++)
        {
            JSONObject id = jsonArrayOfMatches.getJSONObject(i);
            String match_id = id.get("id").toString();
            //System.out.println("match_id = " + match_id);

            match_ids.add(match_id);
        }
        System.out.println("match_ids.size() = " + match_ids.size());
        return match_ids;
    }
}
