import java.io.IOException;

//Associated classes: API, API_Request
//Associated test classes: APITest, API_RequestTest
public class Main_API {


    //Consider: adding/allowing limit for #files to include (EX: 10, rather than 200+)
    public static void main(String[] args) throws IOException {
	// write your code here
        System.out.println("Hello, world");
        //API api = new API_Request();
        API request = new API_Request("TGLTN");
        //API request2 = new API_Request("CDOME");

    }
}

//Enter matches manually OR choose request (timestamp)
//Enable preset creation and use