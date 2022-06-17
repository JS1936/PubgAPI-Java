import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Memory extends MakeRequest {
    //Players
    //Files//Games
    static Map<String, Player> players = new HashMap<>();
    //static Vector<Player> players = new Vector<Player>();
    static Vector<String> gameHTTPS = new Vector<String>();

    public static void printPlayers()
    {

    }

    public static void printGameHTTPS()
    {

    }

    //MakeRequests

    //maybe make requests HERE
    //


    public static void main(String[] args)
    {
        System.out.println("Hello");
        Main.main(args);
    }

}

//*the* base class
//everything else is derived from it?


//1) Only search through files when asked, store info as you find it
//