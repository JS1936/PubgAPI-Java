import java.util.*;
public class Player {
    String accountID;
    //String username;
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
}
