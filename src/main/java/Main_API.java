import java.io.IOException;

//Associated classes: API, API_Request
//Associated test classes: APITest, API_RequestTest
//Note: Main_API seems to function when cloned from GitHub onto a new device.
//However, Main still relies on local files.
public class Main_API {


    //Consider: adding/allowing limit for #files to include (EX: 10, rather than 200+)
    public static void main(String[] args) throws IOException {
	// write your code here
        System.out.println("Hello, world");
        //API api = new API_Request();
        API request = new API_Request("CoorsLatte");
        //API request2 = new API_Request("WackyJacky101");

    }
}

//Enter matches manually OR choose request (timestamp)
//Enable preset creation and use