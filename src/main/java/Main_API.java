// To run: 
// 1. Navigate to this src folder in local terminal.
// 2. "javac Main_API.java" 
// 3. "java Main_API"     

import java.io.IOException;

//Associated classes: API, API_Request
//Associated test classes: APITest, API_RequestTest
//Note: Main_API seems to function when cloned from GitHub onto a new device.
//However, Main still relies on local files.
public class Main_API { //extends API_Request


    //Consider: adding/allowing limit for #files to include (EX: 10, rather than 200+)
    public static void main(String[] args) throws IOException 
    {
        API a = new API_Request("WackyJacky101"); 
        System.out.println("Hello, world");
        //API api = new API();
        System.out.println("API key: \n" + a.getAPIkey());

        //API request = new API_Request("CoorsLatte", 15);
    }
}








//Enter matches manually OR choose request (timestamp)
//Enable preset creation and use



// Test: 
// write your code here
//API api = new API_Request();
//API request = new API_Request("JS1936");
//Main.main();
//API request2 = new API_Request("WackyJacky101");