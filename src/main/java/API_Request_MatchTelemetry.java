import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;

public class API_Request_MatchTelemetry extends API_Request{
    public API_Request_MatchTelemetry(String player) throws IOException {
        super();
    }


    //public URL getTelemetryURL(File f) throws IOException {
    //Guide: https://www.tutorialspoint.com/how-can-we-read-a-json-file-in-java
    public URL getTelemetryURL(File f) throws IOException {

        String fileAsString = FileUtils.readFileToString(f);

        int index = fileAsString.indexOf("https://telemetry-");
        String https = fileAsString.substring(index, index + 119);
        System.out.println("telemetry URL is " + https);

        URL telemetryURL = new URL(https);

        return telemetryURL;
    }
    //public void transferInputUsingProcessBuilder(URL url, File destFile) throws IOException {
    public void transferInputUsingProcessBuilder(URL url, File destFile) throws IOException {

        //Command format (curl)
        String command = "curl --compressed " + url + " -H Accept: application/vnd.api+json";
        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
        Process process = processBuilder.start();

        //Get the input
        InputStream inputStream = process.getInputStream();

        //Transfer the input
        transferInputStreamToFile(inputStream, destFile);

        process.destroy();
    }
    //public HttpURLConnection connectToAPI_wantZIP(URL url, File destFile) throws IOException {
    public HttpURLConnection connectToAPI_wantZIP(URL url, File destFile) throws IOException {
        //System.out.println("CONTENT ENCODING: " + connection.getContentEncoding());
        this.connection = (HttpURLConnection) url.openConnection();
        endProgramIfNullConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization","Bearer " + this.getAPIkey());
        connection.setRequestProperty("Accept", "application/vnd.api+json");
        connection.setRequestProperty("Content-Type", "UTF-8" + "; charset=utf-8"); //added 12/15 (change to utf-8?)
        connection.setRequestProperty("Accept", "gzip"); //added 11/29

        System.out.println("Response code: " + this.connection.getResponseCode()); //expect: 200
        if(this.connection.getResponseCode() == 200) //response is valid/OK
        {
            System.out.println("Connection made. URL: " + url.toString());
            transferInputUsingProcessBuilder(url, destFile); //Credit: https://www.baeldung.com/java-curl.
            FileManager.makePretty(destFile); //seems to work

        }
        else
        {
            System.out.println("Error: connection to api has invalid response");
        }

        return this.connection;

    }
    public File getMatchTelemetryContent(URL telemetryURL, String match_id) throws IOException {
        Path telemetry_Path = Path.of(specificRequest + "/matches/telemetry-for-match_id_" + match_id);
        File newFile = new File(telemetry_Path.toString());
        if(!newFile.exists()) {
            FileManager.createNewFileAndParentFilesIfTheyDoNotExist(newFile);
        }
        File newFile2 = FileManager.makePretty(newFile);
        //API_Request_MatchTelemetry telemetryRequest = new API_Request_MatchTelemetry(pretty);
        // telemetryRequest.
        connectToAPI_wantZIP(telemetryURL, newFile2);
        return newFile2;
    }
}
