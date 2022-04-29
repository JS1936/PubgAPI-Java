import java.util.*;
public class Player extends Main { //extends Main (reverse order?)
    String accountID;
    String username;
    Vector<Weapon> weapons;

    Player(String id)
    {
        accountID = id;
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
