import java.util.*;
public class Weapon {

    private String name;
    private String user;
    private int uses;
    private boolean isMain;
    private Vector<String> attachments;

    public String get_name() {
        return name;
    }

    public String get_user() {
        return user;
    }

    public int get_uses() {
        return uses;
    }

    public boolean get_isMain() {
        return isMain;
    }

    public Vector<String> get_attachments() {
        return attachments;
    }

    public void use() {
        uses++;
    }

    public void attach(String attachment) {
        attachments.add(attachment);
    }

    public void summary()
    {
        System.out.println("Player: " + user);
        System.out.println("\tWeapon:" + name);
        System.out.println("\tIsMainWeapon?: " + get_isMain());
        System.out.println("\t\tAnything ever attached to " + name + ":");

        Vector<String> attached = get_attachments();
        for(int i = 0; i < attachments.size(); i++)
        {

            System.out.println("\t\t\t" + attached.get(i));
        }
        System.out.println("END of " + user + "'s weapon summary for " + name);
    }

    public Weapon(String user_name, String weapon_name)
    {
        user = user_name;
        name = weapon_name;
    }




}
