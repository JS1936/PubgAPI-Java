import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;

import static org.junit.jupiter.api.Assertions.*;

class API_RequestTest {

    //@Test
    //void getAPIkey() {
    //}

    //@Test
    //void getAPIplatform() {
    //}

    @Test
    //Currently very minimal
    void getPlayer() throws IOException {
        String player = "Bob123_b";

        API_Request test = new API_Request(player);

        String playerActual = test.getPlayer();
        String playerExpect = player;

        assertEquals(playerActual, playerExpect);

        //Get opposite case for each character in string player
        //for(int i = 0; i < player.length(); i++)
        //{
        //    if(player.substring(i, i+1).toLowerCase() == player.substring(i))
        //    {
        //        player.substring(i, i+1).toUpperCase();
        //    }
        //    else
        //    {
        //        player.substring(i, i+1).toLowerCase();
        //    }
       // }

        //Get all uppercase
        //String uppercasePlayer = player.toUpperCase();
        //String lowercasePlayer = player.toLowerCase();
        //assertNotEquals(uppercasePlayer, playerExpect);
        //assertNotEquals(lowercasePlayer, playerExpect);



    }

    //@Test
    /*
    void isConnectedToAPI() throws IOException {
        String player = "WackyJacky101";
        API_Request test = new API_Request(player);


        //test.connectToAPI();
        boolean actualConnected = test.isConnectedToAPI();
        if(actualConnected)
        {
            System.out.println("connected");
        }
        //test.getConnection().disconnect();
        //boolean expectDisconnected = test.isConnectedToAPI();
        //assertEquals(true, actualConnected);
        //assertNotEquals(expectConnected, expectDisconnected);

    }
    */
    @Test
    void getConnection() throws IOException {
        String player = "Bob123_b";
        API_Request test = new API_Request(player);
        HttpURLConnection connection = test.getConnection();
        System.out.println("connection: " + connection.toString());
        //if(connection.)
    }

    @Test
    void getMatchTelemetry() {
    }

    @Test
    void connectToAPI() {
    }

    @Test
    void getRequest() {
    }

    @Test
    void storeResponseToFile() {
    }

    @Test
    void storeResponseToSpecifiedFileLocation() {
    }
}