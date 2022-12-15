import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;

public class API_Request_MatchOverview extends API_Request{

    public API_Request_MatchOverview(String player) throws IOException {
        super(player);
    }
    public API_Request_MatchOverview(String player, int matchLimit) throws IOException {
        super();
    }
    //constructor...

    public File getMatchOverviewContent(String match_id) throws IOException {
        //Match Overview
        URL oneMatch_ = new URL("https://api.pubg.com/shards/steam/matches/" + match_id);
        Path match_Path = Path.of(specificRequest + "/matches/match_id_" + match_id);

        connectToAPI(oneMatch_);

        File ugly = storeResponseToSpecifiedFileLocation(match_Path.toString()); //save
        File pretty = FileManager.makePretty(ugly);
        return pretty;
    }

    public HttpURLConnection connectToAPI(URL url) throws IOException {

        this.connection = (HttpURLConnection) url.openConnection();

        endProgramIfNullConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization","Bearer " + this.getAPIkey());
        connection.setRequestProperty("Accept", "application/vnd.api+json");

        printResponseCodeSuccessFail(url);
        return this.connection;

    }

    //public File getMatchOverviewContent(String match_id) throws IOException {
    //public HttpURLConnection connectToAPI(URL url) throws IOException {
}
