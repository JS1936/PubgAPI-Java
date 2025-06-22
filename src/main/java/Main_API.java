import java.io.IOException;
import org.json.JSONObject;
import org.json.JSONArray;


//Associated classes: API, API_Request
//Associated test classes: APITest, API_RequestTest
//Note: Main_API seems to function when cloned from GitHub onto a new device.
//However, Main still relies on local files.
public class Main_API {


    //Consider: adding/allowing limit for #files to include (EX: 10, rather than 200+)
    public static void main(String[] args) throws IOException {
	// write your code here
        System.out.println("Hello, world--()");
        //API api = new API_Request();
        
        //API request = new API_Request("CoorsLatte", 15);
        API request = new API_Request("seoul", 5);
        //API request = new API_Request("JS1936");
        //Main.main();
        //API request2 = new API_Request("WackyJacky101");

        //getRecentActivityTeamFalcons();


        

    }
    public static void getRecentActivityTeamFalcons() throws IOException {
        API request1 = new API_Request("GUNNER", 5);
        //API request2 = new API_Request("hwinn", 5);
        //API request3 = new API_Request("Kickstart", 5);
        //API request4 = new API_Request("Shrimzy", 5);
        //API request5 = new API_Request("TGLTN", 5);

    }

    //DN Freecs team: DIEL, Gyumin, Heaven, Salute
}

//Enter matches manually OR choose request (timestamp)
//Enable preset creation and use