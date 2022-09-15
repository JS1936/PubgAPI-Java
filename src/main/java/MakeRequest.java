import java.util.Scanner;

public class MakeRequest {

    public static void main(String[] args)
    {
        System.out.println("Type the corresponding number and then press enter.");
        System.out.println("I want information about a...");
        System.out.println("0: Game");
        System.out.println("1: Player");

        Scanner input = new Scanner(System.in);

        //str.isDigit?

        int request = Integer.parseInt(input.nextLine()); //careful... //fails if not an int?
        /*
         String request_original = input.nextLine(); //careful... //fails if not an int?
        int request = Integer.parseInt(request_original);
         */
        //how do you check if something is an integer, then?

        //String request = input.next();
        if(request == 0)
        {
            System.out.println("Okay. You want information about a specific game.");
        }
        else if(request == 1)
        {
            System.out.println("Okay. You want information about a specific player.");
            System.out.println("Enter player name (EX: matt112): ");
            if(input.hasNextLine())
            {
                System.out.println("input has next line");
            }
            String playerName = input.nextLine();
            System.out.println("Looking for information about " + playerName + "...");
            System.out.println("Calling playerExists on " + playerName);
            playerExists(playerName);
        }
        else
        {
            System.out.println(request + " is not 0 or 1. Invalid request.");

        }





        //get user input
        //if GAME/PLAYER exists,
        //print possible things to know about that GAME/PLAYER.

        //if GAME/PLAYER doesn't exist,
        //"sorry" message

        //if 0, check if information is stored for that game
        //if 1, check if information is stored for that player
    }

    public static boolean playerExists(String name)
    {
        if(Memory.players.containsKey(name))
        {
            System.out.println("We have encountered " + name + " before!");
        }
        else
        {
            System.out.println("We do not know of any player called " + name + "...");
        }
        return false;
    }

    public static boolean gameExists(String game_id)
    {
        //if(Memory.gameHTTPS.containsKey())
        return false;
    }
}


//MakeRequest ==> say what you want

//MakeRequest asks Memory if it knows it

//The information Memory knows comes from Main

//An example of information known could be #kills in a game by a specific Player
