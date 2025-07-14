import java.io.IOException;

/*
 * The Main_API class allows users to request and store match telemetry data from the pubg API about specific players. 
 * When making a request, users have the option to specify a maximum number of matches to gather data on (EX: 15).
 * If a match limit is unspecified, data on 5 matches will be collected (or fewer, if fewer than 5 games have been played recently).
 * 
 * Associated classes: API, API_Request
 * Associated test classes: APITest, API_RequestTest
 * 
 * Example requests:
 *      - 1. API request = new API_Request("CoorsLatte", 15)
 *      - 2. API request = new API_Request("CoorsLatte") [default 5]
 * 
 * Example players:
 *      - Professional, high-activity player: "shrimzy", "CoorsLatte"
 *      - Low-activity player: "matt112"
 *      - Inactive player: "JS1936"
 *      - Nonexistent player: "not-a-real-player"
 * 
 * Future considerations:
 *      - Impose rules on match_limit values (EX: 15, rather than 0, -1, 200+)
 *      - Add the ability to enter matches manually OR choose request (timestamp)
 *      - Enable preset creation and use
 *      - Allow collection of data on a whole team at a time
 *      
 * 
 * Current errors/issues:
 *      - Not all real players register properly. Here are some pro player examples:
 *              ==> Team Falcons (hwinn, Kickstart, shrimzy, TGLTN) | OK, OK, OK, OK
 *              ==> Team DN Freecs (DIEL, Gyumin, Heaven, Salute)   | fails, fails, fails, fails
 */
public class Main_API {

    public static void main(String[] args) throws IOException {
        //API request = new API_Request("DIEL", 15);    // South Korea      | Fails
        //API request = new API_Request("seoul", 15);   // South Korea      | Fails
        //API request = new API_Request("Capitan", 5);  // Argentina        | Fails
        //API request = new API_Request("Viss", 5)      // United States    | Inactive
        
        
        //API request = new API_Request("chocoTaco", 3); // Succeeds. High activity.
        //API request2 = new API_Request("WackyJacky101", 5); // United States
        API request = new API_Request("CoorsLatte"); // United States
    }
}