import java.util.*;
import java.util.HashMap;
import java.io.*;
//import java.util.Map.Entry;

//Games?
    //Main
    //Player (includes player history...?)

public class Player extends Main { //extends Main (reverse order?)
    String accountID;
    String username;
    Vector<Weapon> weapons;
    Map<Integer, Vector<String>> gamesHistory = new HashMap<>();

    //Is there a game id?
    //What if someone wants to know how many kills they get on each map? (on avg)
    //Having a hashmap that already knows what maps/games they played would be a lot faster than searching through EVERY game and trying to figure out if they played in it
    /*
    public void addGame()
    {
        Vector<String> playerStatsForGame = new Vector<String>();
        gamesHistory.put(gamesHistory.size(), playerStatsForGame);

        //string numKills, string ranking, string numGame, string weaponsUsed with specific delimiter, string teammate names with specific delimiter)

    }
    */

    //Would it be more efficient to just store EACH game's information, and then look for whatever is asked for
    //OR
    //Store anticipated asked-for-information, let the original file go?


    //m.put("hi", 1);
    ///LinkedHashMap<String, String> gamesHistory = new LinkedHashMap<String, String>();
    ///AbstractMap.SimpleEntry("hi", "bye");
    //Entry("a")
    //gamesHistory.put("a", "b");

    //What would their history look like?
    ////What if you had a map of players
    ////Once you found a player, you added them to the map
    ////Then, if you encounter that player again, you can just look in the map instead of having to search for their basic information again
    ////Basic information: player name, id,
    ////Could store history: weapon usage, teammates, killcount avg, rankings, (numGamesRecorded), etc.
    //Could have map => player ==> then a linked list of each game (and that data)

    //map[game X] = {int numKills, int ranking, vector<Player> teammates, int numGame (recorded so far), vector<Weapon> weaponsUsed);
    //What if you made it a linkedList of strings...
    //string numKills, string ranking, string numGame, string weaponsUsed with specific delimiter, string teammate names with specific delimiter)

    Player(String id)
    {
        accountID = id;
        //Map<Integer, String> m = new HashMap<>();
        //m.put(1, "hi");

        //no weapons
    }
    public void addWeapon(Weapon w)
    {
        if(!weapons.contains(w))
        {
            weapons.add(w);
        }
        else
        {
            System.out.println(accountID + "already has that weapon");
        }

    }
    public Vector<Weapon> getWeapons()
    {
        return weapons;
    }

    public void printWeapons()
    {
        System.out.println(username + " currently has weapons: ");
        for(int i = 0; i < weapons.size(); i++)
        {
            System.out.println(weapons.get(i));
        }
    }

    public String getAccountID()
    {
        return accountID;
    }
}
